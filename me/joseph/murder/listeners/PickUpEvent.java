package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import me.joseph.murder.PlayerType;
import me.joseph.murder.events.TitleAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PickUpEvent implements Listener {
   Main plugin;

   public PickUpEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onPickup(final PlayerPickupItemEvent var1) {
      if (Arenas.isInArena(var1.getPlayer())) {
         Arena var2 = Arenas.getArena(var1.getPlayer());
         if (var2.getState() != GameState.INGAME) {
            var1.setCancelled(true);
         }

         if (var2.specs.contains(var1.getPlayer())) {
            var1.setCancelled(true);
            return;
         }

         if (var1.getItem().getItemStack().getType() == Material.getMaterial(this.plugin.settings.getConfig().getInt("dropped-item-id"))) {
            if (var2.getState() != GameState.INGAME) {
               return;
            }

            var2.addscore(var1.getPlayer(), this.plugin.settings.getConfig().getInt("score-on-gold"), this.plugin.FormatText(this.plugin.messages.getConfig().getString("gold-reason")));
            if (var2.getType(var1.getPlayer()) == PlayerType.Innocents) {
               if (var2.golds.contains(var1.getItem())) {
                  var2.golds.remove(var1.getItem());
               }

               if (this.plugin.settings.getConfig().getBoolean("enable-sounds")) {
                  var1.getPlayer().playSound(var1.getPlayer().getLocation(), Sound.valueOf(this.plugin.settings.getConfig().getString("PICK_UP")), 1.0F, 1.0F);
               }

               (new BukkitRunnable() {
                  public void run() {
                     if (var1.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.getMaterial(PickUpEvent.this.plugin.settings.getConfig().getInt("dropped-item-id"))), PickUpEvent.this.plugin.settings.getConfig().getInt("gold-amount-to-get-bow"))) {
                        var1.getPlayer().getInventory().removeItem(new ItemStack[]{new ItemStack(Material.getMaterial(PickUpEvent.this.plugin.settings.getConfig().getInt("dropped-item-id")), PickUpEvent.this.plugin.settings.getConfig().getInt("gold-amount-to-get-bow"))});
                        if (!var1.getPlayer().getInventory().contains(new ItemStack(Material.BOW)) && !var1.getPlayer().getInventory().contains(Material.BOW)) {
                           var1.getPlayer().getInventory().addItem(new ItemStack[]{new ItemStack(Material.BOW)});
                        }

                        var1.getPlayer().getInventory().addItem(new ItemStack[]{new ItemStack(Material.ARROW, 1)});
                        TitleAPI.sendTitle(var1.getPlayer(), Integer.valueOf(0), Integer.valueOf(40), Integer.valueOf(0), PickUpEvent.this.plugin.messages.getConfig().getString("you-have-bow-title").replaceAll("&", "\u00a7"));
                        TitleAPI.sendSubtitle(var1.getPlayer(), Integer.valueOf(0), Integer.valueOf(40), Integer.valueOf(0), PickUpEvent.this.plugin.messages.getConfig().getString("you-have-bow-subtitle").replaceAll("&", "\u00a7"));
                        var1.getPlayer().updateInventory();
                     }

                  }
               }).runTaskLater(this.plugin, 20L);
            }
         }
      }

   }
}
