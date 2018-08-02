package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SpectateEvent implements Listener {
   Main plugin;

   public SpectateEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent var1) {
      if (Arenas.isInArena((Player)var1.getWhoClicked()) && var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         var1.setCancelled(true);
      }

      if (var1.getInventory().getTitle().equalsIgnoreCase(this.plugin.settings.getConfig().getString("spectate-inventory-title"))) {
         if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
            var1.setCancelled(true);
            Player var2 = (Player)var1.getWhoClicked();
            if (Bukkit.getPlayer(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName())) == null) {
               this.plugin.OpenSpec(var2);
               return;
            }

            if (Bukkit.getPlayer(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName())) != null) {
               var2.teleport(Bukkit.getPlayer(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName())));
            }
         }

      }
   }
}
