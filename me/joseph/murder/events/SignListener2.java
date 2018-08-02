package me.joseph.murder.events;

import java.util.Iterator;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener2 implements Listener {
   Main plugin;

   public SignListener2(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onSignCreate(SignChangeEvent var1) {
      Player var2 = var1.getPlayer();
      if (var1.getLine(0).equalsIgnoreCase("[MurderAuto]") && var2.hasPermission("murder.admin")) {
         var1.setLine(0, this.plugin.messages.getConfig().getString("auto-join-sign-line-1").replaceAll("&", "\u00a7"));
         var1.setLine(1, this.plugin.messages.getConfig().getString("auto-join-sign-line-2").replaceAll("&", "\u00a7"));
         var1.setLine(2, this.plugin.messages.getConfig().getString("auto-join-sign-line-3").replaceAll("&", "\u00a7"));
         var1.setLine(3, this.plugin.messages.getConfig().getString("auto-join-sign-line-4").replaceAll("&", "\u00a7"));
      }

   }

   @EventHandler
   public void onSignInteract(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK && !var1.getPlayer().isSneaking()) {
         Block var2 = var1.getClickedBlock();
         if (var2 != null && var2.getState() instanceof Sign) {
            Sign var3 = (Sign)var2.getState();
            if (var3.getLine(0).equalsIgnoreCase(this.plugin.messages.getConfig().getString("auto-join-sign-line-1").replaceAll("&", "\u00a7"))) {
               if (Arenas.getArenas() == null || Arenas.getArenas().size() == 0) {
                  var1.getPlayer().sendMessage(this.plugin.messages.getConfig().getString("no-arenas").replaceAll("&", "\u00a7"));
                  return;
               }

               if (Arenas.getArenas().size() > 0) {
                  Iterator var5 = Arenas.getArenas().iterator();

                  while(true) {
                     Arena var4;
                     do {
                        if (!var5.hasNext()) {
                           return;
                        }

                        var4 = (Arena)var5.next();
                     } while(var4.getState() != GameState.STARTING && var4.getState() != GameState.LOBBY);

                     if (!Arenas.isInArena(var1.getPlayer())) {
                        var4.addPlayer(var1.getPlayer());
                     }
                  }
               }
            }
         }
      }

   }
}
