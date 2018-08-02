package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvents implements Listener {
   Main plugin;

   public BlockEvents(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onBreak(BlockBreakEvent var1) {
      if (Arenas.isInArena(var1.getPlayer())) {
         var1.setCancelled(true);
      }

      if (this.plugin.getConfig().getBoolean("bungee") && !var1.getPlayer().isOp() && !var1.getPlayer().hasPermission("murder.admin")) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void onPlace(BlockPlaceEvent var1) {
      if (Arenas.isInArena(var1.getPlayer())) {
         var1.setCancelled(true);
      }

      if (this.plugin.getConfig().getBoolean("bungee") && !var1.getPlayer().isOp() && !var1.getPlayer().hasPermission("murder.admin")) {
         var1.setCancelled(true);
      }

   }
}
