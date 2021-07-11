package com.github.jummes.suprememob.goal.impl;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Random;

public class CustomFloatTargetGoal implements Goal<Mob> {

    private final Mob mob;

    public CustomFloatTargetGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean shouldActivate() {
        return mob.isInWater() || mob.isInLava();
    }

    @Override
    public boolean shouldStayActive() {
        return Goal.super.shouldStayActive();
    }

    @Override
    public void start() {
        Goal.super.start();
    }

    @Override
    public void stop() {
        Goal.super.stop();
    }

    @Override
    public void tick() {
        if (new Random().nextFloat() < 0.8F) {
            this.mob.setJumping(true);
        }
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return GoalKey.of(Mob.class, NamespacedKey.randomKey());
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.JUMP);
    }
}
