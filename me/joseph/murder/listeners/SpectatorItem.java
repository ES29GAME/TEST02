package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpectatorItem implements Listener {
   Main plugin;

   public SpectatorItem(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   private void InteractItem(PlayerInteractEvent var1) {
      Player var2 = var1.getPlayer();
      if (Arenas.isInArena(var2)) {
         Arena var3 = Arenas.getArena(var2);
         if (var3.specs.contains(var2)) {
            var1.setCancelled(true);
            var2.updateInventory();
         }
      }

      if (var2.getItemInHand() != null && var2.getItemInHand().getType() != Material.AIR && var1.getAction().name().toLowerCase().contains("right")) {
         if (var2.getItemInHand() == null || var2.getItemInHand().getType() == Material.AIR) {
            return;
         }

         ItemMeta var4;
         String var5;
         ItemStack var7;
         if (Arenas.isInArena(var2) && Arenas.getArena(var2).players.size() > 0 && (var7 = var2.getItemInHand()).hasItemMeta() && (var4 = var7.getItemMeta()).hasDisplayName() && (var5 = var4.getDisplayName()) != null && var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("spec.item-name"))) && Arenas.isInArena(var2)) {
            Arena var6 = Arenas.getArena(var2);
            if (var6.specs.contains(var2)) {
               this.plugin.OpenSpec(var2);
               return;
            }
         }
      }

   }
}
