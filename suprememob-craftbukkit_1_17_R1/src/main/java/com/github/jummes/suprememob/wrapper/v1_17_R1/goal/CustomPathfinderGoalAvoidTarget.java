package com.github.jummes.suprememob.wrapper.v1_17_R1.goal;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

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
                new EntitySource((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), this.a)));
    }
}
