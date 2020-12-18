package com.github.jummes.suprememob.wrapper.v1_15_R1.goal;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public class CustomPathfinderGoalMeleeAttack extends PathfinderGoalMeleeAttack {

    private Condition condition;

    public CustomPathfinderGoalMeleeAttack(EntityCreature creature, double speedModifier,
                                           boolean followingTargetEvenIfNotSeen, Condition canUse) {
        super(creature, speedModifier, followingTargetEvenIfNotSeen);
        this.condition = canUse;
    }

    /**
     * canUse
     */
    @Override
    public boolean a() {
        return super.a() && condition.checkCondition(
                new EntityTarget((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a.getGoalTarget())),
                new EntitySource((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a)));
    }
}
