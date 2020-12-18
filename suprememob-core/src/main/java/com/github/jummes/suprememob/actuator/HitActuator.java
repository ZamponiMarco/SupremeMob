package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lHit &cactuator", description = "gui.actuator.hit.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=")
public class HitActuator extends CooldownActuator{

    @Serializable(headTexture = DAMAGER_HEAD, description = "gui.skill.hit-entity.damager-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onDamagerActions;
    @Serializable(headTexture = DAMAGED_HEAD, description = "gui.skill.hit-entity.damaged-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onDamagedActions;

    public HitActuator() {
        this(0, Lists.newArrayList(), Lists.newArrayList());
    }

    public HitActuator(int cooldown, List<Action> onDamagedActions, List<Action> onDamagerActions) {
        super(cooldown);
        this.onDamagedActions = onDamagedActions;
        this.onDamagerActions = onDamagerActions;
    }

    public HitActuator(Map<String, Object> map) {
        super(map);
        this.onDamagedActions = (List<Action>) map.getOrDefault("onDamagedActions", Lists.newArrayList());
        this.onDamagerActions = (List<Action>) map.getOrDefault("onDamagerActions", Lists.newArrayList());
    }

    @Override
    protected boolean executeExactSkill(LivingEntity... e) {
        return onDamagedActions.stream().anyMatch(action ->
                action.execute(new EntityTarget(e[1]), new EntitySource(e[0])).
                        equals(Action.ActionResult.CANCELLED)) ||
                onDamagerActions.stream().anyMatch(action ->
                        action.execute(new EntityTarget(e[0]), new EntitySource(e[0])).
                                equals(Action.ActionResult.CANCELLED));
    }
}
