package com.github.jummes.suprememob.goal;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.utils.HeadUtils;
import org.bukkit.entity.Mob;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lRandom stroll land &cgoal selector", description = "gui.goal-selector.stroll-land.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRjODU0MjllYjViYzJiZWEyYzVkYmVjODllNTI4ZWQzYTU0ZDIwN2ZiMzc0NDY4MGQ2MmY4NGVkYzVlOTQxIn19fQ==")
public class RandomStrollLandGoalSelector extends ConditionalGoalSelector {

    private final static NumericValue PROBABILITY_DEFAULT = new NumericValue(0.001);

    @Serializable(headTexture = SPEED_HEAD, description = "gui.goal-selector.speed")
    @Serializable.Optional(defaultValue = "SPEED_DEFAULT")
    private NumericValue speed;

    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    private NumericValue probability;

    public RandomStrollLandGoalSelector() {
        this(CONDITION_DEFAULT.clone(), SPEED_DEFAULT.clone(), PROBABILITY_DEFAULT.clone());
    }

    public RandomStrollLandGoalSelector(Map<String, Object> map) {
        super(map);
        this.speed = (NumericValue) map.getOrDefault("speed", SPEED_DEFAULT.clone());
        this.probability = (NumericValue) map.getOrDefault("probability", PROBABILITY_DEFAULT.clone());
    }

    public RandomStrollLandGoalSelector(Condition condition, NumericValue speed, NumericValue probability) {
        super(condition);
        this.speed = speed;
        this.probability = probability;
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        SupremeMob.getInstance().getWrapper().getGoalSelector().setRandomStrollLand(e,
                speed.getRealValue(target, source), probability.getRealValue(target, source).floatValue(), condition);
    }

    @Override
    public String getName() {
        return "&6&lRandom stroll land &cgoal selector";
    }
}
