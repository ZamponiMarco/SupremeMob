package com.github.jummes.suprememob.goal.impl;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class CustomThrowProjectileTargetGoal implements Goal<Mob> {

    private final Mob mob;
    private final EntityType projectile;
    private final int maxProjectileTimer = 20;
    private int projectileTimer;
    private int pathRecalcTicks;

    public CustomThrowProjectileTargetGoal(Mob mob, EntityType projectile) {
        this.mob = mob;
        this.projectile = projectile;
    }

    @Override
    public boolean shouldActivate() {
        return mob.getTarget() != null;
    }

    @Override
    public boolean shouldStayActive() {
        return mob.getTarget() != null && mob.getPathfinder().hasPath();
    }

    @Override
    public void start() {
        mob.getPathfinder().moveTo(mob.getTarget());
        pathRecalcTicks = 0;
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        mob.lookAt(mob.getTarget());

        double distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());

        if (--pathRecalcTicks <= 0) {
            pathRecalcTicks = 10;
            if (distance > 100) {
                mob.getPathfinder().moveTo(mob.getTarget());
            } else {
                mob.getPathfinder().stopPathfinding();
            }
        }

        if (--projectileTimer <= 0 && distance <= 100) {
            mob.launchProjectile((Class<? extends Projectile>) projectile.getEntityClass(),
                    mob.getTarget().getLocation().toVector().clone().subtract(mob.getLocation().toVector()).normalize());
            projectileTimer = maxProjectileTimer;
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
