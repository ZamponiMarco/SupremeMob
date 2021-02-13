package com.github.jummes.suprememob.targetted;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.entity.selector.EntitySelector;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.github.jummes.supremeitem.libs.util.MapperUtils;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.utils.HeadUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lNearest &ctarget selector", description = "gui.target-selector.nearest.description",
        headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==")
public class NearestAttackableTargetSelector extends TargetSelector {

    private static final List<EntitySelector> SELECTORS_DEFAULT = Lists.newArrayList();
    private static final NumericValue INTERVAL_DEFAULT = new NumericValue(10);
    private static final boolean SEE_DEFAULT = true;
    private static final boolean REACH_DEFAULT = false;


    private static final String SELECTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";

    @Serializable(displayItem = "typeHead", stringValue = true, description = "gui.target-selector.nearest.type", fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType type;
    @Serializable(headTexture = SELECTOR_HEAD, description = "gui.target-selector.nearest.selectors")
    @Serializable.Optional(defaultValue = "SELECTORS_DEFAULT")
    private List<EntitySelector> selectors;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    @Serializable.Optional(defaultValue = "INTERVAL_DEFAULT")
    private NumericValue randomInterval;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    @Serializable.Optional(defaultValue = "SEE_DEFAULT")
    private boolean mustSee;
    @Serializable(headTexture = HeadUtils.ATTENTION_HEAD, description = "gui.additional-tooltips.unknown")
    @Serializable.Optional(defaultValue = "REACH_DEFAULT")
    private boolean mustReach;

    public NearestAttackableTargetSelector() {
        this(EntityType.PLAYER, Lists.newArrayList(), INTERVAL_DEFAULT.clone(), SEE_DEFAULT, REACH_DEFAULT);
    }

    public NearestAttackableTargetSelector(Map<String, Object> map) {
        super(map);
        this.type = EntityType.valueOf((String) map.getOrDefault("type", "PLAYER"));
        this.selectors = (List<EntitySelector>) map.getOrDefault("selectors", Lists.newArrayList());
        this.randomInterval = (NumericValue) map.getOrDefault("randomInterval", INTERVAL_DEFAULT.clone());
        this.mustSee = (boolean) map.getOrDefault("mustSee", SEE_DEFAULT);
        this.mustReach = (boolean) map.getOrDefault("mustReach", REACH_DEFAULT);
    }

    public NearestAttackableTargetSelector(EntityType type, List<EntitySelector> selectors, NumericValue randomInterval, boolean mustSee, boolean mustReach) {
        this.type = type;
        this.selectors = selectors;
        this.randomInterval = randomInterval;
        this.mustSee = mustSee;
        this.mustReach = mustReach;
    }

    public static List<Object> getSpawnableEntities(ModelPath path) {
        return Arrays.stream(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> spawnableEntitiesMapper() {
        return MapperUtils.getEntityTypeMapper();
    }

    public ItemStack typeHead() {
        return MapperUtils.getEntityTypeMapper().apply(type);
    }

    @Override
    public void applyToEntity(Mob mob, Source source, Target target) {
        Predicate<LivingEntity> select = selectors.stream().map(selector -> selector.getFilter(new EntitySource(mob),
                target)).reduce(e -> true, Predicate::and);
        SupremeMob.getInstance().getWrapper().getTargetSelector().setNearestEntityTarget(mob, type, select,
                randomInterval.getRealValue(target, source).intValue(), mustSee, mustReach);
    }

    @Override
    public String getName() {
        return "&6&lNearest &c" + WordUtils.capitalizeFully(type.name().replace("_", " "));
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(MapperUtils.getEntityTypeMapper().apply(type), getName(), Lists.newArrayList());
    }
}
