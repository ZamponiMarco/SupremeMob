package com.github.jummes.suprememob.goal.impl;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class CustomMeleeAttackTargetGoal implements Goal<Mob> {

    private final Mob entity;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private final int attackReload;
    private final Condition condition;
    private Location targetLocation;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private int canUseCounter;

    public CustomMeleeAttackTargetGoal(Mob entity, double speedModifier, int attackReload,
                                       boolean followingTargetEvenIfNotSeen, Condition condition) {
        this.entity = entity;
        this.speedModifier = speedModifier;
        this.attackReload = attackReload;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.condition = condition;
    }

    @Override
    public boolean shouldActivate() {

        if (canUseCounter++ < 20) {
            return false;
        }

        LivingEntity target = entity.getTarget();

        if (target == null || target.isDead()) {
            return false;
        }

        if (!this.condition.checkCondition(new EntityTarget(target), new EntitySource(entity,
                new ItemStack(Material.CARROT)))) {
            return false;
        }

        if (this.entity.getPathfinder().findPath(target) != null) {
            return true;
        }

        return this.getAttackReachSqr(target) >= this.entity.getLocation().
                distanceSquared(target.getLocation());

    }

    protected double getAttackReachSqr(LivingEntity target) {
        return this.entity.getWidth() * 2.0F * this.entity.getWidth() * 2.0F + target.getWidth();
    }

    @Override
    public boolean shouldStayActive() {
        LivingEntity target = this.entity.getTarget();

        if (target == null || target.isDead()) {
            return false;
        }

        if (!this.followingTargetEvenIfNotSeen) {
            return !this.entity.getLocation().equals(targetLocation);
        }

        return !(target instanceof Player) || !((Player) target).getGameMode().equals(GameMode.SPECTATOR) &&
                !((Player) target).getGameMode().equals(GameMode.CREATIVE);
    }

    @Override
    public void start() {
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        this.entity.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();

        entity.lookAt(target, 30.0f, 30.0f);
        double distance = this.entity.getLocation().distanceSquared(target.getLocation());
        if ((this.followingTargetEvenIfNotSeen ||
                this.entity.hasLineOfSight(target)) && --this.ticksUntilNextPathRecalculation <= 0 &&
                (this.targetLocation == null || target.getLocation().distanceSquared(targetLocation) >= 1.0)) {
            this.targetLocation = target.getLocation();
            this.ticksUntilNextPathRecalculation = 5;

            if (distance > 1024.0D) {
                this.ticksUntilNextPathRecalculation += 10;
            } else if (distance > 256.0D) {
                this.ticksUntilNextPathRecalculation += 5;
            }

            if (entity.getPathfinder().moveTo(target, this.speedModifier)) {
                this.ticksUntilNextPathRecalculation += 15;
            }

        }

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        this.checkAndPerformAttack(target, distance);
    }

    protected void checkAndPerformAttack(LivingEntity target, double distance) {
        double bodyDistance = this.getAttackReachSqr(target);
        if (distance <= bodyDistance && this.ticksUntilNextAttack <= 0) {
            this.ticksUntilNextAttack = attackReload;
            this.entity.swingMainHand();
            this.entity.attack(target);
        }

    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return GoalKey.of(Mob.class, NamespacedKey.randomKey());
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE);
    }
}
