package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoSpecDamage implements Listener {
   Main plugin;

   public NoSpecDamage(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void entitydamages(org.bukkit.event.entity.EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Player && var1.getEntity() instanceof Player) {
         Player var2 = (Player)var1.getDamager();
         if (Arenas.isInArena(var2)) {
            Arena var3 = Arenas.getArena(var2);
            if (var3.specs.contains(var2)) {
               var1.setCancelled(true);
            }
         }
      }

   }
}
