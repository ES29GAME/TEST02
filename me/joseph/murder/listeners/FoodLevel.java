package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevel implements Listener {
   Main plugin;

   public FoodLevel(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void Food(FoodLevelChangeEvent var1) {
      Player var2 = (Player)var1.getEntity();
      if (Arenas.isInArena(var2)) {
         var1.setCancelled(true);
      }

      if (this.plugin.getConfig().getBoolean("bungee")) {
         var1.setCancelled(true);
      }

   }
}
