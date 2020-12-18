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
@Enumerable.Displayable(name = "&6&lDeath &cactuator", description = "gui.actuator.death.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYzZmQ3ODIyMmQ1OTg4YzZiZTFjYzlmYTk2ZTg1Mjg5MTViYjY5NzQ2NDY0ZDIzOGY5MzZlYmViYjIzYzUyIn19fQ==")
public class DeathActuator extends Actuator {

    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private List<Action> onEntityActions;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private List<Action> onKillerActions;

    public DeathActuator() {
        this(Lists.newArrayList(), Lists.newArrayList());
    }

    public DeathActuator(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.get("onEntiyActions");
        this.onKillerActions = (List<Action>) map.get("onKillerActions");
    }

    public DeathActuator(List<Action> onEntityActions, List<Action> onKillerActions) {
        this.onEntityActions = onEntityActions;
        this.onKillerActions = onKillerActions;
    }

    public ActuatorResult executeSkill(LivingEntity mob, LivingEntity killer) {
        boolean cancelled = false;

        cancelled = onEntityActions.stream().anyMatch(action -> action.execute(new EntityTarget(mob),
                new EntitySource(mob)).equals(Action.ActionResult.CANCELLED));

        if (killer != null) {
            cancelled |= onKillerActions.stream().anyMatch(action -> action.execute(new EntityTarget(killer),
                    new EntitySource(mob)).equals(Action.ActionResult.CANCELLED));
        }

        return cancelled ? ActuatorResult.CANCELLED : ActuatorResult.SUCCESS;
    }

}