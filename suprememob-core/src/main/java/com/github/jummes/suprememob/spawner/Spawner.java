package com.github.jummes.suprememob.spawner;

import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.supremeitem.libs.model.wrapper.LocationWrapper;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.mob.Mob;
import com.github.jummes.suprememob.utils.HeadUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Spawner implements Model, Listener {

    public static final NamespacedKey SPAWNER_KEY = new NamespacedKey(SupremeMob.getInstance(), "spawner");
    @Serializable(stringValue = true)
    private UUID id;

    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private String mob;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private LocationWrapper location;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private int range;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private int cooldown;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private int maxEntities;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD)
    private int level;

    private int currentCooldown = 0;
    private int currentEntities = 0;

    private int taskId = 0;
    private Mob supremeMob;

    public Spawner(Player p) {
        this(UUID.randomUUID(), "", new LocationWrapper(p.getLocation()), 3, 20, 2, 1);
    }

    public Spawner(UUID id, String mob, LocationWrapper location, int range, int cooldown, int maxEntities, int level) {
        this.id = id;
        this.mob = mob;
        this.location = location;
        this.range = range;
        this.cooldown = cooldown;
        this.maxEntities = maxEntities;
        this.level = level;
    }

    public Spawner(Map<String, Object> map) {
        this.id = UUID.fromString((String) map.get("id"));
        this.mob = (String) map.get("mob");
        this.location = (LocationWrapper) map.get("location");
        this.range = (int) map.get("range");
        this.cooldown = (int) map.get("cooldown");
        this.maxEntities = (int) map.get("maxEntities");
        this.level = (int) map.get("level");
    }

    @Override
    public void onCreation() {
        startSpawnTask();
    }

    @Override
    public void onModify(Field field) {
        stopSpawnTask();
        startSpawnTask();
    }

    @Override
    public void onRemoval() {
        stopSpawnTask();
    }

    private boolean isActive() {
        return taskId != 0;
    }

    private void startSpawnTask() {
        supremeMob = SupremeMob.getInstance().getMobManager().getByName(mob);
        if (supremeMob != null) {
            if (!isActive()) {
                taskId = Bukkit.getScheduler().runTaskTimer(SupremeMob.getInstance(), () -> {
                    if (currentCooldown > 0) {
                        currentCooldown--;
                    } else if (currentCooldown == 0 && currentEntities < maxEntities) {
                        currentCooldown = cooldown;
                        Entity e = supremeMob.spawn(location.getWrapped(), level);
                        e.getPersistentDataContainer().set(SPAWNER_KEY,
                                PersistentDataType.STRING, id.toString());
                        currentEntities++;
                    }
                }, 0, 20).getTaskId();
            }
        }
    }

    private void stopSpawnTask() {
        if (isActive()) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent e) {
        if (Objects.equals(e.getEntity().getPersistentDataContainer().get(SPAWNER_KEY, PersistentDataType.STRING),
                this.id.toString())) {
            currentEntities--;
        }
    }
}
