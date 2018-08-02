package me.joseph.murder.listeners;

import me.joseph.murder.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpenVoteGUI implements Listener {
   Main plugin;

   public OpenVoteGUI(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   private void ClickItem23(PlayerInteractEvent var1) {
      if (var1.getPlayer().getItemInHand() != null) {
         Player var2 = var1.getPlayer();
         if (var1.getAction().name().toLowerCase().contains("right")) {
            if (var2.getItemInHand().getType() == Material.AIR) {
               return;
            }

            ItemStack var3;
            ItemMeta var4;
            String var5;
            if ((var3 = var2.getItemInHand()).hasItemMeta() && (var4 = var3.getItemMeta()).hasDisplayName() && (var5 = var4.getDisplayName()) != null && var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("map.item-name")))) {
               var1.setCancelled(true);
               this.plugin.OpenVote(var2);
            }
         }
      }

   }
}
