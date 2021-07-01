package com.github.jummes.suprememob.wrapper;

import com.github.jummes.supremeitem.condition.Condition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public interface GoalSelectorUtils {

    void setMeleeGoal(Mob mob, double speed, boolean following, Condition condition);

    void setAvoidTarget(Mob mob, EntityType type, float maxDist, double walkSpeedModifier,
                        double sprintSpeedModifier, Condition condition);

    void setRandomStrollLand(Mob mob, double speed, float probability, Condition canUse);

    void setFollowTarget(Mob mob, double speedModifier, float stopDistance, float areaSize, Condition condition);

    void setFloat(Mob mob);

    void clearEntityGoals(Mob mob);

}
