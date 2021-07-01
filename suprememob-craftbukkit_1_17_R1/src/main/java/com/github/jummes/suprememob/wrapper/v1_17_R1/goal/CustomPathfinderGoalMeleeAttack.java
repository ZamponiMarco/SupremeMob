package com.github.jummes.suprememob.wrapper.v1_17_R1.goal;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

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
                new EntitySource((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a), new ItemStack(Material.CARROT)));
    }
}
