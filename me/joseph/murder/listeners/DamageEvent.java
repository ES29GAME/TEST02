package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DamageEvent implements Listener {
   Main plugin;

   public DamageEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void entitydamage(EntityDamageEvent var1) {
      if (var1.getEntity() instanceof Player) {
         Player var2 = (Player)var1.getEntity();
         if (!Arenas.isInArena(var2) && this.plugin.getConfig().getBoolean("bungee")) {
            var1.setCancelled(true);
         }

         if (Arenas.isInArena(var2)) {
            Arena var3 = Arenas.getArena(var2);
            if (var3.specs.contains(var2)) {
               var1.setCancelled(true);
            }

            if (this.plugin.settings.getConfig().getBoolean("no-fall-damage") && var1.getCause() == DamageCause.FALL) {
               var1.setCancelled(true);
            }

            if (var1.getCause() == DamageCause.LAVA) {
               var1.setCancelled(true);
               if (var3.getState() == GameState.INGAME) {
                  var3.removePlayer(var2, "death");
               }
            }

            if (var1.getCause() == DamageCause.FIRE) {
               var1.setCancelled(true);
               var2.setFireTicks(0);
            }

            if (var1.getCause() == DamageCause.FIRE_TICK) {
               var1.setCancelled(true);
               var2.setFireTicks(0);
            }

            if (var1.getCause() == DamageCause.SUFFOCATION) {
               var1.setCancelled(true);
            }

            if (var3.getState() != GameState.INGAME) {
               var1.setCancelled(true);
            }
         }
      }

   }
}
