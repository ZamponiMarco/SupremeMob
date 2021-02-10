package com.github.jummes.suprememob.manager;

import com.github.jummes.supremeitem.libs.model.ModelManager;
import com.github.jummes.suprememob.mob.Mob;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class MobManager extends ModelManager<Mob> {

    private final List<Mob> mobs;

    public MobManager(Class<Mob> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, ImmutableMap.of());
        this.mobs = database.loadObjects();
    }

    public Mob getByName(String name) {
        return mobs.stream().filter(mob -> mob.getName().equals(name)).findFirst().orElse(null);
    }
}