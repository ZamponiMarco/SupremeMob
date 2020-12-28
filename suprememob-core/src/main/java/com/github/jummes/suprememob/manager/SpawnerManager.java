package com.github.jummes.suprememob.manager;

import com.github.jummes.supremeitem.libs.model.ModelManager;
import com.github.jummes.suprememob.spawner.Spawner;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class SpawnerManager extends ModelManager<Spawner> {

    private final List<Spawner> spawners;

    public SpawnerManager(Class<Spawner> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin);
        this.spawners = database.loadObjects();
        this.spawners.forEach(Spawner::onCreation);
    }
}
