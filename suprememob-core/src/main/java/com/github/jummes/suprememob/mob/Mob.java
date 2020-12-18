package com.github.jummes.suprememob.mob;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.github.jummes.supremeitem.libs.util.MapperUtils;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.actuator.Actuator;
import com.github.jummes.suprememob.actuator.SpawnActuator;
import com.github.jummes.suprememob.mob.options.GeneralOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Mob extends NamedModel {

    private static final String ACTUATORS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJiMTI1NmViOWY2NjdjMDVmYjIxZTAyN2FhMWQ1MzU1OGJkYTc0ZTI0MGU0ZmE5ZTEzN2Q4NTFjNDE2ZmU5OCJ9fX0=";
    private static final String OPTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMyZmYyNDRkZmM5ZGQzYTJjZWY2MzExMmU3NTAyZGM2MzY3YjBkMDIxMzI5NTAzNDdiMmI0NzlhNzIzNjZkZCJ9fX0=";

    private static int counter = 1;

    @Serializable(displayItem = "typeHead", stringValue = true, description = "gui.mob.type", fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType type;
    @Serializable(headTexture = ACTUATORS_HEAD, description = "gui.mob.actuators")
    private Set<Actuator> actuators;
    @Serializable(headTexture = OPTIONS_HEAD, description = "gui.mob.general-options")
    private GeneralOptions generalOptions;

    public Mob() {
        this(nextAvailableName(), EntityType.ZOMBIE, Sets.newHashSet(), new GeneralOptions(), true);
    }

    public Mob(String name, EntityType type, Set<Actuator> actuators, GeneralOptions generalOptions) {
        this(name, type, actuators, generalOptions, true);
        counter++;
    }

    public Mob(String name, EntityType type, Set<Actuator> actuators, GeneralOptions generalOptions, boolean increasedCounter) {
        super(name);
        this.type = type;
        this.actuators = actuators;
        this.generalOptions = generalOptions;
    }

    public Mob(Map<String, Object> map) {
        super((String) map.get("name"));
        this.type = EntityType.valueOf((String) map.get("type"));
        this.actuators = Sets.newHashSet((List<Actuator>) map.get("actuators"));
        this.generalOptions = (GeneralOptions) map.getOrDefault("generalOptions", new GeneralOptions());
        counter++;
    }

    private static String nextAvailableName() {
        String name;
        do {
            name = "mob" + counter;
            counter++;
        } while (SupremeItem.getInstance().getSavedSkillManager().getByName(name) != null);
        return name;
    }

    public static List<Object> getSpawnableEntities(ModelPath path) {
        return Arrays.stream(EntityType.values()).filter(type -> type.isSpawnable() &&
                org.bukkit.entity.Mob.class.isAssignableFrom(type.getEntityClass())).
                collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> spawnableEntitiesMapper() {
        return MapperUtils.getEntityTypeMapper();
    }

    public static Mob fromEntity(Entity e) {
        if (e == null) {
            return null;
        }

        String name = e.getPersistentDataContainer().getOrDefault(new NamespacedKey(SupremeMob.getInstance(), "mob"),
                PersistentDataType.STRING, "");

        if (name.equals("")) {
            return null;
        }

        return SupremeMob.getInstance().getMobManager().getByName(name);
    }

    public static boolean isMob(Entity e) {
        return fromEntity(e) != null;
    }

    public static int getLevel(Entity e) {
        if (!isMob(e)) {
            return 0;
        }

        return e.getPersistentDataContainer().getOrDefault(new NamespacedKey(SupremeMob.getInstance(), "level"),
                PersistentDataType.INTEGER, 1);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type.name());
        map.put("actuators", Lists.newArrayList(actuators));
        map.put("generalOptions", generalOptions);
        return map;
    }

    @SneakyThrows
    public Entity spawn(Location l, int level) {
        return spawn(l, level, null);
    }

    public Entity spawn(Location l, int level, Source source) {
        org.bukkit.entity.Mob e = (org.bukkit.entity.Mob) l.getWorld().spawnEntity(l, type);
        if (source == null) {
            source = new EntitySource(e);
        }
        EntityTarget target = new EntityTarget(e);
        e.getPersistentDataContainer().set(new NamespacedKey(SupremeMob.getInstance(), "mob"),
                PersistentDataType.STRING, name);
        e.getPersistentDataContainer().set(new NamespacedKey(SupremeMob.getInstance(), "level"),
                PersistentDataType.INTEGER, level);
        generalOptions.buildOptions(e, source, target);
        actuators.stream().filter(actuator -> actuator instanceof SpawnActuator).findFirst().ifPresent(actuator ->
                ((SpawnActuator) actuator).executeActuator(e));
        return e;
    }

    public ItemStack typeHead() {
        return MapperUtils.getEntityTypeMapper().apply(type);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(typeHead(), "&6&lName: &c" + name, Libs.getLocale().
                getList("gui.additional-tooltips.delete"));
    }

}
