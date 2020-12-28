package com.github.jummes.suprememob.listener;

import com.github.jummes.suprememob.actuator.*;
import com.github.jummes.suprememob.loot.DropTable;
import com.github.jummes.suprememob.mob.Mob;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MobListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        Mob damaged = Mob.fromEntity(e.getEntity());
        LivingEntity damagerEntity = null;
        if (e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
            damagerEntity = (LivingEntity) e.getDamager();
        } else if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            damagerEntity = (LivingEntity) projectile.getShooter();
        }

        if (damaged != null) {
            damaged.getActuators().stream().filter(actuator -> actuator instanceof DamageActuator).findFirst().
                    ifPresent(actuator -> cancelled.set(((DamageActuator) actuator).getActuatorResult((LivingEntity) e.getEntity(),
                            (LivingEntity) e.getEntity(), (LivingEntity) e.getDamager()).equals(Actuator.ActuatorResult.CANCELLED)));
        }

        Mob damager = Mob.fromEntity(damagerEntity);
        if (damager != null) {
            damager.getActuators().stream().filter(actuator -> actuator instanceof HitActuator).findFirst().
                    ifPresent(actuator -> cancelled.set(cancelled.get() || ((HitActuator) actuator).
                            getActuatorResult((LivingEntity) e.getDamager(), (LivingEntity) e.getDamager(), (LivingEntity) e.getEntity()).
                            equals(Actuator.ActuatorResult.CANCELLED)));
        }

        if (cancelled.get()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        Mob mob = Mob.fromEntity(e.getEntity());

        if (mob == null) {
            return;
        }

        mob.getActuators().stream().filter(actuator -> actuator instanceof DeathActuator).findFirst().
                ifPresent(actuator -> ((DeathActuator) actuator).executeSkill(e.getEntity(), e.getEntity().getKiller()));

        modifyDrops(e, mob);
    }

    @EventHandler
    public void onMobInteract(PlayerInteractAtEntityEvent e) {
        Mob mob = Mob.fromEntity(e.getRightClicked());

        if (!(e.getRightClicked() instanceof LivingEntity) || mob == null || e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        LivingEntity entity = (LivingEntity) e.getRightClicked();

        mob.getActuators().stream().filter(actuator -> actuator instanceof InteractActuator).findFirst().ifPresent(actuator ->
                ((InteractActuator) actuator).getActuatorResult(entity, entity, e.getPlayer()));
    }

    private void modifyDrops(EntityDeathEvent e, Mob mob) {
        List<ItemStack> drops = e.getDrops();

        Player killer = e.getEntity().getKiller();
        DropTable dropTable = mob.getGeneralOptions().getDropTable();

        LootContext.Builder builder = new LootContext.Builder(e.getEntity().getLocation()).lootedEntity(e.getEntity());
        if (killer != null) {
            builder.killer(killer);
            PotionEffect luck = killer.getPotionEffect(PotionEffectType.LUCK);
            if (luck != null) {
                builder.luck(luck.getAmplifier());
            }
            EntityEquipment equipment = killer.getEquipment();
            if (equipment != null) {
                builder.lootingModifier(equipment.getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        if (!dropTable.isDefaultDropsEnabled()) {
            drops.clear();
            e.setDroppedExp(0);
        }

        drops.addAll(dropTable.populateLoot(new Random(), builder.build()));
    }

}
