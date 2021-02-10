package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.manager.CooldownManager;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public abstract class CooldownActuator extends Actuator {

    protected static final String DAMAGER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";
    protected static final String DAMAGED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJlMmRkNTM3Y2I3NDM0YzM5MGQwNDgyZmE0NzI0N2NhM2ViNTZmMTlhOTNjMDRjNmM4NTgxMzUyYjhkOTUzOCJ9fX0=";

    protected static final int COOLDOWN_DEFAULT = 0;

    protected static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";


    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.actuator.cooldown")
    @Serializable.Optional(defaultValue = "COOLDOWN_DEFAULT")
    private int cooldown;

    public CooldownActuator(int cooldown) {
        this.cooldown = cooldown;
    }

    public CooldownActuator(Map<String, Object> map) {
        super(map);
        this.cooldown = (int) map.getOrDefault("cooldown", 0);
    }

    protected void cooldown(LivingEntity e, int cooldown) {
        if (cooldown > 0) {
            SupremeMob.getInstance().getCooldownManager().addCooldown(e,
                    new CooldownManager.CooldownInfo(cooldown, getClass()));
        }
    }

    public void getActuatorResult(Map<String, Object> map, LivingEntity mob, LivingEntity... e) {
        int currentCooldown = SupremeMob.getInstance().getCooldownManager().getCooldown(mob, getClass());
        if (currentCooldown == 0) {
            executeExactSkill(map, e);
            cooldown(mob, cooldown);
        }
    }

    protected abstract void executeExactSkill(Map<String, Object> map, LivingEntity... e);
}
