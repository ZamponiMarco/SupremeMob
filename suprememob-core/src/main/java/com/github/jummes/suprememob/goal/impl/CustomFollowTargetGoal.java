package com.github.jummes.suprememob.goal.impl;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class CustomFollowTargetGoal implements Goal<Mob> {

    private final Mob mob;
    private final double stopDistance;
    private final double speedModifier;
    private int pathRecalcTicks;

    public CustomFollowTargetGoal(Mob mob, double speedModifier, double stopDistance) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
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
        mob.getPathfinder().moveTo(mob.getTarget(), speedModifier);
        pathRecalcTicks = 0;
    }

    @Override
    public void stop() {
        mob.getPathfinder().stopPathfinding();
    }

    @Override
    public void tick() {
        mob.lookAt(mob.getTarget(), 10.0f, 40.0f);
        if (--pathRecalcTicks <= 0) {
            pathRecalcTicks = 10;
            if (mob.getLocation().distanceSquared(mob.getTarget().getLocation()) > stopDistance * stopDistance) {
                mob.getPathfinder().moveTo(mob.getTarget(), speedModifier);
            } else {
                mob.getPathfinder().stopPathfinding();
            }
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
