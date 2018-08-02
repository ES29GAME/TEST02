package me.joseph.murder.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.FlyingItems;
import me.joseph.murder.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickPotionEffectBlock implements Listener {
   Main plugin;
   ArrayList cooldown = new ArrayList();

   public ClickPotionEffectBlock(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onClickPotion(final PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         Block var2 = var1.getClickedBlock();
         if (var2.getType() != Material.AIR && Arenas.isInArena(var1.getPlayer())) {
            if (var2.getType() == Material.CHEST || var2.getType() == Material.TRAPPED_CHEST || var2.getType() == Material.DISPENSER || var2.getType() == Material.ENDER_CHEST || var2.getType() == Material.DROPPER || var2.getType() == Material.WORKBENCH || var2.getType() == Material.BED_BLOCK || var2.getType() == Material.FURNACE) {
               var1.setCancelled(true);
            }

            if (this.plugin.hasPotion(var2, Arenas.getArena(var1.getPlayer()))) {
               var1.setCancelled(true);
               if (this.cooldown.contains(var1.getPlayer().getName())) {
                  return;
               }

               this.cooldown.add(var1.getPlayer().getName());
               (new BukkitRunnable() {
                  public void run() {
                     if (ClickPotionEffectBlock.this.cooldown.contains(var1.getPlayer().getName())) {
                        ClickPotionEffectBlock.this.cooldown.remove(var1.getPlayer().getName());
                     }

                  }
               }).runTaskLater(this.plugin, 20L);
               if (this.plugin.blocks.contains(var2)) {
                  var1.getPlayer().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("potion-use-error")));
                  return;
               }

               if (!var1.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("dropped-item-id"))), this.plugin.settings.getConfig().getInt("gold-amount-to-get-potion"))) {
                  var1.getPlayer().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("potion-use-error-2")));
                  return;
               }

               if (var1.getPlayer().getInventory().containsAtLeast(new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("dropped-item-id"))), this.plugin.settings.getConfig().getInt("gold-amount-to-get-potion"))) {
                  Arena var3 = Arenas.getArena(var1.getPlayer());
                  var1.getPlayer().getInventory().removeItem(new ItemStack[]{new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("dropped-item-id")), this.plugin.settings.getConfig().getInt("gold-amount-to-get-potion"))});
                  this.plugin.blocks.add(var2);
                  var1.getPlayer().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("potion-use-message")));
                  ArrayList var4 = new ArrayList();
                  Iterator var6 = this.plugin.potions.getConfig().getConfigurationSection("Potions").getKeys(false).iterator();

                  while(var6.hasNext()) {
                     String var5 = (String)var6.next();
                     var4.add(var5);
                  }

                  int var11 = var4.size();
                  int var12 = this.plugin.getRandom(0, var11 - 1);
                  if (var12 > var11 - 1) {
                     var12 = 0;
                  }

                  String var7 = (String)var4.get(var12);
                  ItemStack var8 = new ItemStack(Material.POTION);
                  PotionMeta var9 = (PotionMeta)var8.getItemMeta();
                  var9.setDisplayName(this.plugin.FormatText(this.plugin.potions.getConfig().getString("Potions." + var7 + ".name")));
                  var9.addCustomEffect(new PotionEffect(PotionEffectType.getByName(this.plugin.potions.getConfig().getString("Potions." + var7 + ".type").toUpperCase()), this.plugin.potions.getConfig().getInt("Potions." + var7 + ".time") * 20, this.plugin.potions.getConfig().getInt("Potions." + var7 + ".level")), true);
                  var9.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
                  var8.setItemMeta(var9);
                  FlyingItems var10 = new FlyingItems(var1.getPlayer(), var8.getItemMeta().getDisplayName(), var8, var1.getClickedBlock().getLocation().add(0.5D, 0.0D, 0.5D), this.plugin);
                  if (var3.items.contains(var10)) {
                     return;
                  }

                  var10.spawn();
                  var3.items.add(var10);
                  this.plugin.canceltask(Integer.valueOf(2), var10, var3, var1.getClickedBlock(), var1.getPlayer(), var8);
               }
            }
         }
      }

   }
}
