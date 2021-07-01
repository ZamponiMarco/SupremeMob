package com.github.jummes.suprememob.wrapper.v1_17_R1.goal;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.condition.Condition;
import lombok.SneakyThrows;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowEntity;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.level.pathfinder.PathType;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

public class CustomPathfinderGoalFollowTarget extends PathfinderGoalFollowEntity {

    private Condition condition;

    private EntityLiving toFollow;
    private Predicate<EntityLiving> pr;

    @SneakyThrows
    public CustomPathfinderGoalFollowTarget(EntityInsentient var0, double var1, float var3, float var4, Condition condition) {
        super(var0, var1, var3, var4);
        this.condition = condition;
        pr = entityLiving -> condition.checkCondition(
                new EntityTarget((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), entityLiving)),
                new EntitySource((LivingEntity) CraftEntity.getEntity((CraftServer) Bukkit.getServer(), var0)));
    }

    public boolean a() {
        List<EntityLiving> var0 = this.getA().getWorld().a(EntityLiving.class, this.getA().getBoundingBox().g(
                this.getI()), pr);
        if (!var0.isEmpty()) {
            for (EntityLiving a : var0) {
                if (!a.isInvisible()) {
                    this.toFollow = a;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean b() {
        return this.toFollow != null && !this.getE().m() && this.getA().i(this.toFollow) >
                (double)(this.getG() * this.getG());
    }

    public void c() {
        setF(0);
        setH(this.getA().a(PathType.i));
        this.getA().a(PathType.i, 0.0F);
    }

    public void d() {
        this.toFollow = null;
        this.getE().o();
        this.getA().a(PathType.i, this.getH());
    }

    public void e() {
        if (this.toFollow != null && !this.getA().isLeashed()) {
            this.getA().getControllerLook().a(this.toFollow, 10.0F, this.getA().getHeadRotation());
            setF(this.getF() - 1);
            if (this.getF() <= 0) {
                setF(10);
                double var0 = this.getA().locX() - this.toFollow.locX();
                double var2 = this.getA().locY() - this.toFollow.locY();
                double var4 = this.getA().locZ() - this.toFollow.locZ();
                double var6 = var0 * var0 + var2 * var2 + var4 * var4;
                if (!(var6 <= (double)(this.getG() * this.getG()))) {
                    this.getE().a(this.toFollow, this.getD());
                } else {
                    this.getE().o();
                    if (var6 <= (double)this.getG()) {
                        double var9 = this.toFollow.locX() - this.getA().locX();
                        double var11 = this.toFollow.locZ() - this.getA().locZ();
                        this.getE().a(this.getA().locX() - var9, this.getA().locY(), this.getA().locZ() - var11, this.getD());
                    }

                }
            }
        }
    }

    @SneakyThrows
    private EntityInsentient getA() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("a");
        return (EntityInsentient) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private float getH() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("h");
        return (float) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private float getI() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("i");
        return (float) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private double getD() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("d");
        return (double) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private NavigationAbstract getE() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("e");
        return (NavigationAbstract) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private int getF() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("f");
        return (int) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private float getG() {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("g");
        return (float) FieldUtils.readField(field, this, true);
    }

    @SneakyThrows
    private void setF(int f) {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("f");
        FieldUtils.writeField(field, this, f,true);
    }

    @SneakyThrows
    private void setH(float h) {
        Field field = PathfinderGoalFollowEntity.class.getDeclaredField("h");
        FieldUtils.writeField(field, this, h,true);
    }

}
