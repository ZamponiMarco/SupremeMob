package com.github.jummes.suprememob.wrapper.v1_16_R3.goal;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.PathfinderGoalAvoidTarget;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class CustomPathfinderGoalAvoidTarget<T extends EntityLiving> extends PathfinderGoalAvoidTarget<T> {

    private Condition canUse;

    public CustomPathfinderGoalAvoidTarget(EntityCreature var0, Class<T> var1, float var2, double var3, double var5,
                                           Condition canUse) {
        super(var0, var1, var2, var3, var5);
        this.canUse = canUse;
    }

    @Override
    public boolean a() {
        return super.a() && canUse.checkCondition(
                new EntityTarget((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a.getGoalTarget())),
                new EntitySource((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a), new ItemStack(Material.CARROT)));
    }
}
