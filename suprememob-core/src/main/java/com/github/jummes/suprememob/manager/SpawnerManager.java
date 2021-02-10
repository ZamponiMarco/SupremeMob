package com.github.jummes.suprememob.manager;

import com.github.jummes.supremeitem.libs.model.ModelManager;
import com.github.jummes.suprememob.spawner.Spawner;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class SpawnerManager extends ModelManager<Spawner> implements Listener {

    private final List<Spawner> spawners;

    public SpawnerManager(Class<Spawner> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, ImmutableMap.of());
        this.spawners = database.loadObjects();
        this.spawners.forEach(Spawner::onCreation);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        spawners.stream().filter(spawner -> spawner.getLocation().getWrapped().getChunk().equals(e.getChunk())).
                forEach(Spawner::stopSpawnTask);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        spawners.stream().filter(spawner -> spawner.getLocation().getWrapped().getChunk().equals(e.getChunk())).
                forEach(Spawner::startSpawnTask);
    }

}
