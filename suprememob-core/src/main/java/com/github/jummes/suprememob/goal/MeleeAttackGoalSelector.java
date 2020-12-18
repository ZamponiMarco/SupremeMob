package com.github.jummes.suprememob.goal;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.AlwaysTrueCondition;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.utils.HeadUtils;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Mob;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lMelee attack &cgoal selector", description = "gui.goal-selector.melee.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class MeleeAttackGoalSelector extends GoalSelector {

    private static final boolean FOLLOWING_DEFAULT = false;

    private static final String CONDITION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=";

    @Serializable(headTexture = SPEED_HEAD, description = "gui.goal-selector.speed")
    @Serializable.Optional(defaultValue = "SPEED_DEFAULT")
    private NumericValue speedModifier;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    @Serializable.Optional(defaultValue = "FOLLOWING_DEFAULT")
    private boolean followingTargetEvenIfNotSeen;
    @Serializable(headTexture = CONDITION_HEAD, description = "gui.goal-selector.condition",
            additionalDescription = {"gui.additional-tooltips.recreate"})
    private Condition condition;

    public MeleeAttackGoalSelector() {
        this(SPEED_DEFAULT.clone(), FOLLOWING_DEFAULT, new AlwaysTrueCondition());
    }

    public MeleeAttackGoalSelector(Map<String, Object> map) {
        super(map);
        this.speedModifier = (NumericValue) map.getOrDefault("speedModifier", SPEED_DEFAULT.clone());
        this.followingTargetEvenIfNotSeen = (boolean) map.getOrDefault("followingTargetEvenIfNotSeen", FOLLOWING_DEFAULT);
        this.condition = (Condition) map.getOrDefault("condition", new AlwaysTrueCondition());
    }

    public MeleeAttackGoalSelector(NumericValue speedModifier, boolean followingTargetEvenIfNotSeen, Condition condition) {
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.condition = condition;
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        if (e instanceof Creature) {
            SupremeMob.getInstance().getWrapper().getGoalSelector().setMeleeGoal(e, speedModifier.
                    getRealValue(target, source), followingTargetEvenIfNotSeen, condition);
        }
    }

    @Override
    public String getName() {
        return "&6&lMelee attack &cgoal selector";
    }
}
