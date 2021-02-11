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
@Enumerable.Displayable
public class FollowTargetGoalSelector extends ConditionalGoalSelector{

    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    private NumericValue speedModifier;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    private NumericValue stopDistance;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    private NumericValue areaSize;

    public FollowTargetGoalSelector() {
        this(CONDITION_DEFAULT.clone(), new NumericValue(1), new NumericValue(2), new NumericValue(2));
    }

    public FollowTargetGoalSelector(Condition condition, NumericValue speedModifier, NumericValue stopDistance, NumericValue areaSize) {
        super(condition);
        this.speedModifier = speedModifier;
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
    }

    public FollowTargetGoalSelector(Map<String, Object> map) {
        super(map);
        this.speedModifier = (NumericValue) map.getOrDefault("speedModifier", new NumericValue(1));
        this.stopDistance = (NumericValue) map.getOrDefault("stopDistance", new NumericValue(2));
        this.areaSize = (NumericValue) map.getOrDefault("areaSize", new NumericValue(2));
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        SupremeMob.getInstance().getWrapper().getGoalSelector().setFollowTarget(e, speedModifier.
                getRealValue(target, source), stopDistance.getRealValue(target, source).floatValue(), areaSize.
                getRealValue(target, source).floatValue(), condition);
    }

    @Override
    public String getName() {
        return "&6&lFollow target &cgoal selector";
    }
}
