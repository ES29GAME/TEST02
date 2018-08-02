package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import me.joseph.murder.PlayerType;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityDamageByEntityEvent implements Listener {
   Main plugin;

   public EntityDamageByEntityEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void DamageEvent(org.bukkit.event.entity.EntityDamageByEntityEvent var1) {
      Player var2;
      if (var1.getDamager() instanceof Arrow && var1.getEntity() instanceof Player) {
         var2 = (Player)var1.getEntity();
         if (Arenas.isInArena(var2)) {
            Arena var3 = Arenas.getArena(var2);
            Arrow var4 = (Arrow)var1.getDamager();
            if (var4.getShooter() instanceof Player) {
               Player var5 = (Player)var4.getShooter();
               if (var5 == var2) {
                  var1.setCancelled(true);
                  return;
               }

               if (var5 != var2) {
                  if (var3.getType(var2) == PlayerType.Murderer) {
                     var3.Hero = var5.getName();
                  }

                  if (var3.time <= 0) {
                     var1.setCancelled(true);
                     return;
                  }

                  var1.setDamage(1000.0D);
               }
            }
         }
      }

      if (!(var1.getDamager() instanceof Arrow) && var1.getEntity() instanceof Player && var1.getDamager() instanceof Player) {
         var2 = (Player)var1.getEntity();
         Player var6 = (Player)var1.getDamager();
         if (Arenas.isInArena(var6) && Arenas.isInArena(var2)) {
            Arena var7 = Arenas.getArena(var6);
            if (var7.getType(var6) == PlayerType.Murderer && var6.getItemInHand().getType() == Material.getMaterial(this.plugin.settings.getConfig().getInt("murderer-weapon.item-id"))) {
               var1.setDamage(1000.0D);
               return;
            }

            if (var7.getType(var6) == PlayerType.Innocents && var7.getType(var2) == PlayerType.Detective) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Innocents && var7.getType(var2) == PlayerType.Innocents) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Innocents && var7.getType(var2) == PlayerType.Murderer) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Murderer && var7.getType(var2) == PlayerType.Innocents) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Murderer && var7.getType(var2) == PlayerType.Detective) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Murderer && var7.getType(var2) == PlayerType.Murderer) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Detective && var7.getType(var2) == PlayerType.Detective) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Detective && var7.getType(var2) == PlayerType.Innocents) {
               var1.setCancelled(true);
               return;
            }

            if (var7.getType(var6) == PlayerType.Detective && var7.getType(var2) == PlayerType.Murderer) {
               var1.setCancelled(true);
               return;
            }
         }
      }

   }

   @EventHandler
   public void DamgetByEntity2(org.bukkit.event.entity.EntityDamageByEntityEvent var1) {
      if (var1.getEntity().getType() == EntityType.ARMOR_STAND && var1.getDamager() instanceof Player) {
         Player var2 = (Player)var1.getDamager();
         if (Arenas.isInArena(var2) || this.plugin.getConfig().getBoolean("bungee")) {
            var1.setCancelled(true);
         }
      }

   }
}
