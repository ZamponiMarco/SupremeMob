package com.github.jummes.suprememob.wrapper;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface TargetSelectorUtils {


    void setNearestEntityTarget(Mob mob, EntityType type, Predicate<LivingEntity> entityPredicate, int randomInterval,
                                boolean mustSee, boolean mustReach);

    void clearEntityTargets(Mob mob);
}
