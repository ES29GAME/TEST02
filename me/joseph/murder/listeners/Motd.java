package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener {
   Main plugin;

   public Motd(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void MotdEvent(ServerListPingEvent var1) {
      if (this.plugin.getConfig().getBoolean("bungee")) {
         Arena var2;
         if (this.plugin.bungee != null) {
            var2 = this.plugin.bungee;
            if (var2.getState() == GameState.INGAME) {
               var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("ingame-motd")));
            }

            if (var2.getState() == GameState.LOBBY) {
               var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("lobby-motd")));
            }

            if (var2.getState() == GameState.STARTING) {
               var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("starting-motd")));
            }

            return;
         }

         if (this.plugin.bungee == null) {
            if (Arenas.getArenas().size() > 1) {
               var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("voting-motd")));
            }

            if (Arenas.getArenas().size() == 1) {
               var2 = (Arena)Arenas.getArenas().get(0);
               if (var2.getState() == GameState.INGAME) {
                  var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("ingame-motd")));
               }

               if (var2.getState() == GameState.LOBBY) {
                  var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("lobby-motd")));
               }

               if (var2.getState() == GameState.STARTING) {
                  var1.setMotd(this.plugin.FormatText(this.plugin.messages.getConfig().getString("starting-motd")));
               }
            }
         }
      }

   }
}
