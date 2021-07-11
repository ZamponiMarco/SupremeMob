package com.github.jummes.suprememob;

import com.github.jummes.supremeitem.entity.Entity;
import com.github.jummes.supremeitem.libs.command.PluginCommandExecutor;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.util.InjectUtils;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.suprememob.actuator.Actuator;
import com.github.jummes.suprememob.command.HelpCommand;
import com.github.jummes.suprememob.command.MobListCommand;
import com.github.jummes.suprememob.command.MobSpawnCommand;
import com.github.jummes.suprememob.command.SpawnerListCommand;
import com.github.jummes.suprememob.goal.GoalSelector;
import com.github.jummes.suprememob.inject.entity.SupremeMobEntity;
import com.github.jummes.suprememob.inject.placeholder.numeric.MobLevelPlaceholder;
import com.github.jummes.suprememob.inject.placeholder.numeric.MobNumericPlaceholder;
import com.github.jummes.suprememob.listener.MobListener;
import com.github.jummes.suprememob.loot.DropTable;
import com.github.jummes.suprememob.loot.drop.Drop;
import com.github.jummes.suprememob.manager.CooldownManager;
import com.github.jummes.suprememob.manager.MobManager;
import com.github.jummes.suprememob.manager.SpawnerManager;
import com.github.jummes.suprememob.mob.Mob;
import com.github.jummes.suprememob.mob.options.AttributeOptions;
import com.github.jummes.suprememob.mob.options.BehaviorOptions;
import com.github.jummes.suprememob.mob.options.EquipmentOptions;
import com.github.jummes.suprememob.mob.options.GeneralOptions;
import com.github.jummes.suprememob.spawner.Spawner;
import com.github.jummes.suprememob.targetted.TargetSelector;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SupremeMob extends JavaPlugin {

    static {
        InjectUtils.injectClass(Entity.class, SupremeMobEntity.class);
        ConfigurationSerialization.registerClass(SupremeMobEntity.class,
                "com.github.jummes.supremeitem.entity.SupremeMobEntity");

        InjectUtils.injectClass(NumericPlaceholder.class, MobNumericPlaceholder.class);
        ConfigurationSerialization.registerClass(MobNumericPlaceholder.class,
                "com.github.jummes.supremeitem.placeholder.numeric.mob.MobNumericPlaceholder");
        ConfigurationSerialization.registerClass(MobLevelPlaceholder.class,
                "com.github.jummes.supremeitem.placeholder.numeric.mob.MobLevelPlaceholder");

        ConfigurationSerialization.registerClass(Mob.class);

        ConfigurationSerialization.registerClass(GeneralOptions.class);
        ConfigurationSerialization.registerClass(AttributeOptions.class);
        ConfigurationSerialization.registerClass(BehaviorOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.Equipment.class);

        ConfigurationSerialization.registerClass(Actuator.class);

        ConfigurationSerialization.registerClass(TargetSelector.class);
        ConfigurationSerialization.registerClass(GoalSelector.class);

        ConfigurationSerialization.registerClass(DropTable.class);
        ConfigurationSerialization.registerClass(Drop.class);

        ConfigurationSerialization.registerClass(Spawner.class);
    }

    private MobManager mobManager;
    private SpawnerManager spawnerManager;
    private CooldownManager cooldownManager;

    public static SupremeMob getInstance() {
        return getPlugin(SupremeMob.class);
    }

    @Override
    public void onEnable() {
        setUpLocale();
        setUpData();
        setUpListener();
        setUpCommands();
    }

    @Override
    public void onDisable() {
        spawnerManager.getSpawners().forEach(Spawner::stopSpawnTask);
    }

    private void setUpLocale() {
        Libs.getLocale().registerLocaleFiles(this, Lists.newArrayList("en-US"), "en-US");
    }

    private void setUpData() {
        this.mobManager = new MobManager(Mob.class, "comp", this);
        this.spawnerManager = new SpawnerManager(Spawner.class, "yaml", this);
        this.cooldownManager = new CooldownManager();
    }

    private void setUpListener() {
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(spawnerManager, this);
    }

    private void setUpCommands() {
        PluginCommandExecutor ex = new PluginCommandExecutor(HelpCommand.class, "help");
        ex.registerCommand("mobs", MobListCommand.class);
        ex.registerCommand("spawners", SpawnerListCommand.class);
        ex.registerCommand("spawn", MobSpawnCommand.class);
        getCommand("sm").setExecutor(ex);
    }
}
