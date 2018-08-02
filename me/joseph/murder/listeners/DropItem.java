package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem implements Listener {
   Main plugin;

   public DropItem(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onDrop(PlayerDropItemEvent var1) {
      if (Arenas.isInArena(var1.getPlayer())) {
         var1.setCancelled(true);
      }

      if (this.plugin.getConfig().getBoolean("bungee") && !var1.getPlayer().isOp() && !var1.getPlayer().hasPermission("murder.admin")) {
         var1.setCancelled(true);
      }

   }
}
