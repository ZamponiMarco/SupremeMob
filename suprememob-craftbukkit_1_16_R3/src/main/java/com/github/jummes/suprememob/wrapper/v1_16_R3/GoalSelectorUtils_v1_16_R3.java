package com.github.jummes.suprememob.wrapper.v1_16_R3;

import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.suprememob.wrapper.GoalSelectorUtils;
import com.github.jummes.suprememob.wrapper.v1_16_R3.goal.CustomPathfinderGoalAvoidTarget;
import com.github.jummes.suprememob.wrapper.v1_16_R3.goal.CustomPathfinderGoalFollowTarget;
import com.github.jummes.suprememob.wrapper.v1_16_R3.goal.CustomPathfinderGoalMeleeAttack;
import com.github.jummes.suprememob.wrapper.v1_16_R3.goal.CustomPathfinderGoalRandomStrollLand;
import lombok.SneakyThrows;
import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.PathfinderGoalWrapped;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import java.util.Set;

import static com.github.jummes.suprememob.wrapper.v1_16_R3.Utils_v1_16_R3.getNMSEntityType;
import static com.github.jummes.suprememob.wrapper.v1_16_R3.Utils_v1_16_R3.nextId;

public class GoalSelectorUtils_v1_16_R3 implements GoalSelectorUtils {

    @Override
    public void setMeleeGoal(Mob mob, double speed, boolean following, Condition condition) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.d()), new CustomPathfinderGoalMeleeAttack(entity, speed,
                following, condition));
    }

    @Override
    public void setAvoidTarget(Mob mob, EntityType type, float maxDist, double walkSpeedModifier,
                               double sprintSpeedModifier, Condition condition) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.d()), new CustomPathfinderGoalAvoidTarget<>(entity,
                getNMSEntityType(mob, type), maxDist, walkSpeedModifier, sprintSpeedModifier, condition));
    }

    @Override
    public void setRandomStrollLand(Mob mob, double speed, float probability, Condition canUse) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.d()), new CustomPathfinderGoalRandomStrollLand(entity, speed,
                probability, canUse));
    }

    @Override
    public void setFollowTarget(Mob mob, double speedModifier, float stopDistance, float areaSize, Condition condition) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.d()), new CustomPathfinderGoalFollowTarget(entity, speedModifier,
                stopDistance, areaSize, condition));
    }

    @SneakyThrows
    @Override
    public void clearEntityGoals(Mob mob) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Set<PathfinderGoalWrapped> goals = (Set<PathfinderGoalWrapped>) FieldUtils.readField(FieldUtils.
                        getDeclaredField(entity.goalSelector.getClass(), "d", true), entity.goalSelector,
                true);
        goals.clear();
    }

}
