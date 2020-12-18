package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.suprememob.utils.HeadUtils;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

@Enumerable.Child
public class InteractActuator extends CooldownActuator {

    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private List<Action> onEntityActions;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private List<Action> onInteractorActions;

    public InteractActuator() {
        this(COOLDOWN_DEFAULT, Lists.newArrayList(), Lists.newArrayList());
    }

    public InteractActuator(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.get("onEntityActions");
        this.onInteractorActions = (List<Action>) map.get("onInteractorActions");
    }

    public InteractActuator(int cooldown, List<Action> onEntityActions, List<Action> onInteractorActions) {
        super(cooldown);
        this.onEntityActions = onEntityActions;
        this.onInteractorActions = onInteractorActions;
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onEntityActions.stream().anyMatch(action -> action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                equals(Action.ActionResult.CANCELLED)) || onInteractorActions.stream().anyMatch(action -> action.
                execute(new EntityTarget(e[1]), new EntitySource(e[0])).equals(Action.ActionResult.CANCELLED));
    }
}
