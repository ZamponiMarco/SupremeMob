package com.github.jummes.suprememob.goal.impl;

import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class CustomAvoidTargetGoal implements Goal<Mob> {

    private final Mob mob;
    private Pathfinder pathfinder;
    private Location targetLocation;
    private double walkSpeedModifier;
    private double sprintSpeedModifier;

    public CustomAvoidTargetGoal(Mob mob, double walkSpeedModifier,
                                 double sprintSpeedModifier) {
        this.mob = mob;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = sprintSpeedModifier;
        this.pathfinder = mob.getPathfinder();
    }

    @Override
    public boolean shouldActivate() {
        if (this.mob.getTarget() == null) {
            return false;
        }

        Vector vector = mob.getLocation().toVector().clone().subtract(mob.getTarget().getLocation().toVector()).normalize();
        this.targetLocation = mob.getLocation().clone().add(vector);
        return true;
    }

    @Override
    public boolean shouldStayActive() {
        return mob.getTarget() != null && mob.getLocation().equals(targetLocation);
    }

    @Override
    public void start() {
        pathfinder.moveTo(targetLocation, walkSpeedModifier);
    }

    @Override
    public void stop() {
        pathfinder.stopPathfinding();
        this.targetLocation = null;
    }

    @Override
    public void tick() {
        if (this.mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 49.0D) {
            pathfinder.moveTo(targetLocation, sprintSpeedModifier);
        } else {
            pathfinder.moveTo(targetLocation, walkSpeedModifier);
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
