package com.github.jummes.suprememob.wrapper;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

@Getter
public class VersionWrapper {

    private static final String PACKAGE_PREFIX = "com.github.jummes.suprememob.wrapper.";
    private static final String TARGET_PREFIX = "TargetSelectorUtils_";
    private static final String GOAL_PREFIX = "GoalSelectorUtils_";

    private GoalSelectorUtils goalSelector;
    private TargetSelectorUtils targetSelector;

    @SneakyThrows
    public VersionWrapper() {
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName();
        String version = serverVersion.substring(serverVersion.lastIndexOf('.') + 1);

        this.goalSelector = (GoalSelectorUtils) Class.forName(PACKAGE_PREFIX + version + "." + GOAL_PREFIX + version).getConstructor().newInstance();
        this.targetSelector = (TargetSelectorUtils) Class.forName(PACKAGE_PREFIX + version + "." + TARGET_PREFIX + version).getConstructor().newInstance();
    }
}
