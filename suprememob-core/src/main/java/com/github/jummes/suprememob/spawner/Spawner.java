package com.github.jummes.suprememob.spawner;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.model.wrapper.LocationWrapper;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.mob.Mob;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

@Getter
@Setter
public class Spawner implements Model, Listener {

    public static final NamespacedKey SPAWNER_KEY = new NamespacedKey(SupremeMob.getInstance(), "spawner");

    private static final String MOB_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ";
    private static final String LOCATION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";
    private static final String RANGE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19";
    private static final String COOLDOWN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";
    private static final String MAX_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";
    private static final String LEVEL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUwNTU2YTg0MTY0MDY5ZGYzYjg5NTkzOWQwYWI1MDhmZmE4ZTE0MDQ3MTA2OTM4YjU1OWY1ODg5ZTViZmJlNCJ9fX0=";

    @Serializable(stringValue = true)
    private UUID id;

    @Serializable(headTexture = MOB_HEAD, fromList = "getMobs", fromListMapper = "mobsMapper", description = "gui.spawner.mob")
    private String mob;
    @Serializable(headTexture = LOCATION_HEAD, description = "gui.spawner.location")
    private LocationWrapper location;
    @Serializable(headTexture = RANGE_HEAD, description = "gui.spawner.range")
    @Serializable.Number(minValue = 0)
    private double range;
    @Serializable(headTexture = COOLDOWN_HEAD, description = "gui.spawner.cooldown")
    @Serializable.Number(minValue = 0)
    private int cooldown;
    @Serializable(headTexture = MAX_HEAD, description = "gui.spawner.max-entities")
    @Serializable.Number(minValue = 0)
    private int maxEntities;
    @Serializable(headTexture = LEVEL_HEAD, description = "gui.spawner.level")
    @Serializable.Number(minValue = 1)
    private int level;

    private int currentCooldown = 0;
    private List<Entity> currentEntities = Lists.newArrayList();

    private int taskId = 0;
    private Mob supremeMob;

    public Spawner(Player p) {
        this(UUID.randomUUID(), "", new LocationWrapper(p.getLocation()), 3, 20, 2, 1);
    }

    public Spawner(UUID id, String mob, LocationWrapper location, double range, int cooldown, int maxEntities, int level) {
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
        this.range = (double) map.get("range");
        this.cooldown = (int) map.get("cooldown");
        this.maxEntities = (int) map.get("maxEntities");
        this.level = (int) map.get("level");
    }


    public static List<Object> getMobs(ModelPath path) {
        return new ArrayList<>(SupremeItem.getInstance().getSupremeMobHook().getMobsList());
    }

    public static Function<Object, ItemStack> mobsMapper() {
        return obj -> {
            String mobStr = (String) obj;
            Mob mob = SupremeItem.getInstance().getSupremeMobHook().getByName(mobStr);
            if (mob != null) {
                return ItemUtils.getNamedItem(mob.getGUIItem(), mob.getGUIItem().getItemMeta().getDisplayName(),
                        Lists.newArrayList());
            }
            return new ItemStack(Material.CARROT);
        };
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

    public void startSpawnTask() {
        supremeMob = SupremeMob.getInstance().getMobManager().getByName(mob);
        if (supremeMob != null) {
            if (!isActive()) {
                Bukkit.getPluginManager().registerEvents(this, SupremeMob.getInstance());
                taskId = Bukkit.getScheduler().runTaskTimer(SupremeMob.getInstance(), () -> {
                    if (currentCooldown > 0) {
                        currentCooldown--;
                    } else if (currentCooldown == 0 && currentEntities.size() < maxEntities) {
                        currentCooldown = cooldown;
                        Random random = new Random();
                        Location toSpawn = location.getWrapped().clone().add(random.nextDouble() * 2 * range - range,
                                0, random.nextDouble() * 2 * range - range);
                        Entity e = supremeMob.spawn(toSpawn, level);
                        e.getPersistentDataContainer().set(SPAWNER_KEY,
                                PersistentDataType.STRING, id.toString());
                        currentEntities.add(e);
                    }
                }, 0, 1).getTaskId();
            }
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGViMDdlZWEzODBhZjU4ZGM5MWVlZWUxNWQ5NWQ4NzkwYTA3ODFjNjk1ZWMwYThmZDhmZTMxZDQ4MzljYTU2MiJ9fX0="),
                "&6&lSpawner of: &c" + mob, Libs.getLocale().getList("gui.additional-tooltips.delete"));
    }

    public void stopSpawnTask() {
        if (isActive()) {
            HandlerList.unregisterAll(this);
            Bukkit.getScheduler().cancelTask(taskId);
            currentEntities.forEach(Entity::remove);
            taskId = 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent e) {
        if (Objects.equals(e.getEntity().getPersistentDataContainer().get(SPAWNER_KEY, PersistentDataType.STRING),
                this.id.toString())) {
            currentEntities.remove(e.getEntity());
        }
    }
}
