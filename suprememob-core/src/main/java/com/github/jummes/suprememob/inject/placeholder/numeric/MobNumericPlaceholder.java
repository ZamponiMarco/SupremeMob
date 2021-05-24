package com.github.jummes.suprememob.inject.placeholder.numeric;

import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.suprememob.SupremeMob;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

@Enumerable.Parent(classArray = {MobLevelPlaceholder.class})
@Enumerable.Displayable(name = "&c&lSupremeMob Placeholders", condition = "supremeMobsEnabled", description = "gui.placeholder.double.mob.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjEyYjU4Yzg0MWIzOTQ4NjNkYmNjNTRkZTFjMmFkMjY0OGFmOGYwM2U2NDg5ODhjMWY5Y2VmMGJjMjBlZTIzYyJ9fX0=")
public abstract class MobNumericPlaceholder extends NumericPlaceholder {

    public MobNumericPlaceholder(boolean target) {
        super(target);
    }

    public MobNumericPlaceholder(Map<String, Object> map) {
        super(map);
    }

    public static boolean supremeMobsEnabled(ModelPath path) {
        return SupremeMob.getInstance().isEnabled();
    }

    protected LivingEntity getEntity(Target target, Source source) {
        if (this.target) {
            if (target instanceof EntityTarget) {
                return ((EntityTarget) target).getTarget();
            }
            return null;
        }
        return source.getCaster();
    }
}
