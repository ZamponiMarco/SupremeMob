package com.github.jummes.suprememob.manager;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.suprememob.actuator.Actuator;
import com.github.jummes.suprememob.actuator.DamageActuator;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CooldownManager {

    private final Map<LivingEntity, List<CooldownInfo>> cooldowns;

    public CooldownManager() {
        this.cooldowns = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(SupremeItem.getInstance(), getCooldownProcess(), 0, 1);
    }

    private Runnable getCooldownProcess() {
        return () -> {
            Iterator<Map.Entry<LivingEntity, List<CooldownInfo>>> i = cooldowns.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<LivingEntity, List<CooldownInfo>> entry = i.next();
                entry.getValue().forEach(info -> info.setRemainingCooldown(info.getRemainingCooldown() - 1));
                entry.getValue().removeIf(info -> info.getRemainingCooldown() == 0);
                if (entry.getValue().isEmpty()) {
                    i.remove();
                }
            }
        };
    }

    public void addCooldown(LivingEntity e, CooldownInfo info) {
        cooldowns.computeIfAbsent(e, k -> Lists.newArrayList());
        cooldowns.get(e).add(info);
    }

    public int getCooldown(LivingEntity e, Class<? extends Actuator> actuator) {
        CooldownInfo info = getCooldownInfo(e, actuator);
        return info.getRemainingCooldown();
    }

    public CooldownInfo getCooldownInfo(LivingEntity e, Class<? extends Actuator> actuator) {
        if (cooldowns.containsKey(e)) {
            return cooldowns.get(e).stream().filter(info -> info.getActuator().equals(actuator)).
                    findFirst().orElse(new CooldownInfo(0, DamageActuator.class));
        }
        return new CooldownInfo(0, DamageActuator.class);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CooldownInfo {
        private int remainingCooldown;
        @EqualsAndHashCode.Include
        private Class<? extends Actuator> actuator;
    }
}
