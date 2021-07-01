package com.github.jummes.suprememob.wrapper.v1_17_R1;

import com.github.jummes.suprememob.wrapper.AttributeUtils;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class AttributeUtils_v1_17_R3 implements AttributeUtils {

    @Override
    public void registerAttribute(Attribute attribute, LivingEntity entity) {
        try {
            EntityCreature entityCreature = (EntityCreature) ((CraftEntity) entity).getHandle();

            Map<AttributeBase, AttributeModifiable> map = (Map<AttributeBase, AttributeModifiable>) FieldUtils.
                    getDeclaredField(AttributeMapBase.class, "b", true).get(entityCreature.getAttributeMap());
            AttributeBase attributeBase = CraftAttributeMap.toMinecraft(attribute);
            AttributeModifiable attributeModifiable = new AttributeModifiable(attributeBase, AttributeModifiable::getAttribute);
            map.put(attributeBase, attributeModifiable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
