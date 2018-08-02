package me.joseph.murder.events;

import java.util.Iterator;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import me.joseph.murder.SignManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
   Main plugin;
   SignManager sm;

   public SignListener(Main var1) {
      this.plugin = var1;
      this.sm = var1.sm;
   }

   @EventHandler
   public void onSignBreak(BlockBreakEvent var1) {
      if (var1.getBlock().getState() instanceof Sign) {
         Sign var2 = (Sign)var1.getBlock().getState();
         if (var1.getPlayer().hasPermission("murder.sign")) {
            if (var2.getLine(0).equalsIgnoreCase(this.plugin.messages.getConfig().getString("sign-header").replaceAll("&", "\u00a7"))) {
               if (var1.getPlayer().isSneaking()) {
                  Iterator var4 = Arenas.getArenas().iterator();

                  while(var4.hasNext()) {
                     Arena var3 = (Arena)var4.next();
                     if (ChatColor.stripColor(var2.getLine(1)).equalsIgnoreCase(var3.getName())) {
                        this.sm.removeSign(var3, var1.getBlock().getLocation());
                        break;
                     }
                  }
               } else {
                  var1.setCancelled(true);
                  var2.update(true);
               }
            }
         } else if (var2.getLine(0).equalsIgnoreCase(this.plugin.messages.getConfig().getString("sign-header").replaceAll("&", "\u00a7"))) {
            var1.setCancelled(true);
            var2.update(true);
         }
      }

   }

   @EventHandler
   public void onSignCreate(SignChangeEvent var1) {
      Player var2 = var1.getPlayer();
      if (var1.getLine(0).equalsIgnoreCase("[Murder]") && var2.hasPermission("murder.admin")) {
         Iterator var4 = Arenas.getArenas().iterator();

         while(var4.hasNext()) {
            Arena var3 = (Arena)var4.next();
            if (ChatColor.stripColor(var1.getLine(1)).equalsIgnoreCase(var3.getName())) {
               var1.setLine(0, this.plugin.messages.getConfig().getString("sign-header").replaceAll("&", "\u00a7"));
               var1.setLine(1, this.plugin.messages.getConfig().getString("sign-arena").replaceAll("&", "\u00a7").replaceAll("%arena%", var3.getName()));
               if (var3.getState() == GameState.INGAME) {
                  var1.setLine(2, this.plugin.messages.getConfig().getString("sign-ingame").replaceAll("&", "\u00a7"));
               } else if (var3.getState() == GameState.LOBBY) {
                  var1.setLine(2, this.plugin.messages.getConfig().getString("sign-lobby").replaceAll("&", "\u00a7"));
               }

               this.sm.addSign(var3, var1.getBlock().getLocation());
            }
         }
      }

   }

   @EventHandler
   public void onSignInteract(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK && !var1.getPlayer().isSneaking()) {
         Block var2 = var1.getClickedBlock();
         if (var2 != null && var2.getState() instanceof Sign) {
            Sign var3 = (Sign)var2.getState();
            if (var3.getLine(0).equalsIgnoreCase(this.plugin.messages.getConfig().getString("sign-header").replaceAll("&", "\u00a7"))) {
               Iterator var5 = Arenas.getArenas().iterator();

               while(var5.hasNext()) {
                  Arena var4 = (Arena)var5.next();
                  if (ChatColor.stripColor(var3.getLine(1)).equalsIgnoreCase(var4.getName())) {
                     var4.addPlayer(var1.getPlayer());
                     break;
                  }
               }
            }
         }
      }

   }
}
