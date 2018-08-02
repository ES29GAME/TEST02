package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class VoteEvent implements Listener {
   Main plugin;

   public VoteEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void Vote(InventoryClickEvent var1) {
      Player var2 = (Player)var1.getWhoClicked();
      if (this.plugin.getConfig().getBoolean("bungee") && !var1.getWhoClicked().isOp() && !var2.hasPermission("murder.admin") && var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         var1.setCancelled(true);
      }

      if (var1.getInventory().getTitle().contains(this.plugin.FormatText(this.plugin.settings.getConfig().getString("vote-inventory.name")))) {
         if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
            var1.setCancelled(true);
         }

         if (!var1.getInventory().contains(var1.getCurrentItem())) {
            return;
         }

         if (this.plugin.bungee != null) {
            var1.getWhoClicked().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("vote-error")));
            return;
         }

         if (!var1.getWhoClicked().hasPermission("murder.vote")) {
            var1.getWhoClicked().closeInventory();
            var1.getWhoClicked().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("vote-error-perm")));
            return;
         }

         if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR && var1.getCurrentItem().getItemMeta().hasDisplayName()) {
            Arena var3 = Arenas.getArena(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName()));
            if (var3 != null) {
               if (this.plugin.votes.containsKey(var2.getName()) && ((Integer)this.plugin.point.get(this.plugin.votes.get(var2.getName()))).intValue() > 0) {
                  this.plugin.point.put((String)this.plugin.votes.get(var2.getName()), ((Integer)this.plugin.point.get(this.plugin.votes.get(var2.getName()))).intValue() - 1);
               }

               if (!this.plugin.votes.containsKey(var2.getName()) || !((String)this.plugin.votes.get(var2.getName())).contains(var3.getName())) {
                  this.plugin.votes.put(var2.getName(), var3.getName());
                  this.plugin.point.put(var3.getName(), ((Integer)this.plugin.point.get(var3.getName())).intValue() + 1);
                  var2.sendMessage(this.plugin.FormatText(this.plugin.settings.getConfig().getString("vote-message").replaceAll("%map%", var3.getName())));
                  var2.closeInventory();
               }
            }
         }
      }

   }
}
