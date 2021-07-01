package com.github.jummes.suprememob.wrapper.v1_16_R3;

import com.github.jummes.suprememob.wrapper.AttributeUtils;
import net.minecraft.server.v1_16_R3.AttributeBase;
import net.minecraft.server.v1_16_R3.AttributeMapBase;
import net.minecraft.server.v1_16_R3.AttributeModifiable;
import net.minecraft.server.v1_16_R3.EntityCreature;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class AttributeUtils_v1_16_R3 implements AttributeUtils {

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
