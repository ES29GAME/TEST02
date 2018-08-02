package me.joseph.murder.listeners;

import java.util.Iterator;
import me.joseph.murder.Main;
import me.joseph.murder.events.TitleAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionConsumEvent implements Listener {
   Main plugin;

   public PotionConsumEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void itemconsume(final PlayerItemConsumeEvent var1) {
      if (var1.getItem().hasItemMeta() && var1.getItem().getItemMeta().hasDisplayName() && var1.getItem() != null && var1.getItem().getType() != Material.AIR && var1.getItem().getType() == Material.POTION) {
         Iterator var3 = var1.getPlayer().getActivePotionEffects().iterator();

         while(var3.hasNext()) {
            PotionEffect var2 = (PotionEffect)var3.next();
            var1.getPlayer().removePotionEffect(var2.getType());
         }

         var1.setCancelled(true);
         var1.getPlayer().setItemInHand((ItemStack)null);
         (new BukkitRunnable() {
            public void run() {
               var1.getPlayer().getInventory().remove(Material.GLASS_BOTTLE);
               var1.getPlayer().setItemInHand((ItemStack)null);
               var1.getPlayer().updateInventory();
            }
         }).runTaskLater(this.plugin, 1L);
         ItemStack var4 = var1.getItem();
         PotionMeta var5 = (PotionMeta)var4.getItemMeta();
         TitleAPI.sendTitle(var1.getPlayer(), Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("potion-title").replaceAll("%time%", String.valueOf(((PotionEffect)var5.getCustomEffects().get(0)).getDuration() / 20)).replaceAll("%type%", this.plugin.capitalizeFirstLetter(((PotionEffect)var5.getCustomEffects().get(0)).getType().getName().toLowerCase())).replaceAll("&", "\u00a7"));
         TitleAPI.sendSubtitle(var1.getPlayer(), Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("potion-subtitle").replaceAll("%time%", String.valueOf(((PotionEffect)var5.getCustomEffects().get(0)).getDuration() / 20)).replaceAll("%type%", this.plugin.capitalizeFirstLetter(((PotionEffect)var5.getCustomEffects().get(0)).getType().getName().toLowerCase())).replaceAll("&", "\u00a7"));
         var1.getPlayer().addPotionEffect(new PotionEffect(((PotionEffect)var5.getCustomEffects().get(0)).getType(), ((PotionEffect)var5.getCustomEffects().get(0)).getDuration(), ((PotionEffect)var5.getCustomEffects().get(0)).getAmplifier() - 1));
      }

   }
}
