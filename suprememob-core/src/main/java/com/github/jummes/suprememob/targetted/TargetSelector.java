package com.github.jummes.suprememob.targetted;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Enumerable.Parent(classArray = {NearestAttackableTargetSelector.class, ClearTargetSelector.class})
public abstract class TargetSelector implements Model {
    public TargetSelector() {
    }

    public TargetSelector(Map<String, Object> map) {

    }

    public abstract void applyToEntity(Mob e, Source source, Target target);

    public abstract String getName();

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().
                getAnnotation(Enumerable.Displayable.class).headTexture()), getName(), Libs.getLocale().
                getList("gui.additional-tooltips.delete"));
    }
}
