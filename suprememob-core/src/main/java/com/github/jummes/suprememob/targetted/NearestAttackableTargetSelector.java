package com.github.jummes.suprememob.targetted;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.AlwaysTrueCondition;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.github.jummes.supremeitem.libs.util.MapperUtils;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.targetted.impl.NearestAttackableTargetGoal;
import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lNearest &ctarget selector", description = "gui.target-selector.nearest.description",
        headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public class NearestAttackableTargetSelector extends TargetSelector {


    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";

    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.target-selector.nearest.selectors")
    private Condition condition;

    public NearestAttackableTargetSelector() {
        this(new AlwaysTrueCondition());
    }

    public NearestAttackableTargetSelector(Map<String, Object> map) {
        super(map);
        this.condition = (Condition) map.getOrDefault("condition", new AlwaysTrueCondition());
    }

    public NearestAttackableTargetSelector(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void applyToEntity(Mob mob, Source source, Target target) {
        int currentSize = Bukkit.getMobGoals().getAllGoals(mob).size();
        Bukkit.getMobGoals().addGoal(mob, currentSize, new NearestAttackableTargetGoal(mob, condition));
    }

    @Override
    public String getName() {
        return "&6&lNearest target such that " + condition.getName();
    }
}
