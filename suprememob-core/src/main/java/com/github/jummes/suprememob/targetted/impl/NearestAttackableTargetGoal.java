package com.github.jummes.suprememob.targetted.impl;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import net.minecraft.server.v1_16_R3.Pathfinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Random;

public class NearestAttackableTargetGoal implements Goal<Mob> {

    private final Mob mob;
    private final int randomInterval;
    private final Condition condition;
    private final boolean mustSee;
    private final int MAX_UNSEEN_TICKS = 60;
    private LivingEntity target;
    private int unseenTicks;

    public NearestAttackableTargetGoal(Mob mob, Condition condition) {
        this.mob = mob;
        this.condition = condition;
        this.randomInterval = 10;
        this.mustSee = true;
    }

    @Override
    public boolean shouldActivate() {
        if (this.randomInterval > 0 && new Random().nextInt(randomInterval) != 0) {
            return false;
        } else {
            this.findTarget();
            return this.target != null;
        }
    }

    protected void findTarget() {
        this.target = this.mob.getLocation().getNearbyLivingEntities(getFollowDistance(), e -> !e.equals(mob) && condition.
                checkCondition(new EntityTarget(e), new EntitySource(mob, new ItemStack(Material.CARROT)))).stream().
                min((e1, e2) -> (int) (mob.getLocation().distanceSquared(e1.getLocation()) -
                        mob.getLocation().distanceSquared(e2.getLocation()))).orElse(null);
    }

    @Override
    public boolean shouldStayActive() {
        if (target == null) {
            return false;
        }

        if (target.isDead()) {
            return false;
        }

        if (target.isInvulnerable()) {
            return false;
        }

        Team mobTeam = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(mob.getUniqueId().toString());
        Team targetTeam = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(target.getUniqueId().toString());
        if (mobTeam != null && targetTeam == mobTeam) {
            return false;
        }

        double followDistance = this.getFollowDistance();
        if (this.mob.getLocation().distanceSquared(target.getLocation()) > followDistance * followDistance) {
            return false;
        }

        if (this.mustSee) {
            if (this.mob.hasLineOfSight(target)) {
                this.unseenTicks = 0;
            } else return ++this.unseenTicks <= this.MAX_UNSEEN_TICKS;
        }

        return true;
    }

    protected double getFollowDistance() {
        return mob.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue();
    }

    @Override
    public void start() {
        this.mob.setTarget(target);
        this.unseenTicks = 0;
    }

    @Override
    public void stop() {
        this.mob.setTarget(null);
        this.target = null;
    }

    @Override
    public void tick() {
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return GoalKey.of(Mob.class, NamespacedKey.randomKey());
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.TARGET);
    }
}
