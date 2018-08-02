package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
   Main plugin;

   public JoinEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent var1) {
      if (this.plugin.settings.getConfig().getBoolean("tp-lobby-on-join") && this.plugin.arenas.getConfig().contains("Lobby.main.lobby") && this.plugin.getLobby() != null) {
         var1.getPlayer().teleport(this.plugin.getLobby());
      }

      this.plugin.Regiser(var1.getPlayer());
      this.plugin.registerNewData(var1.getPlayer());
      this.plugin.setScoreboard(var1.getPlayer());
      if (this.plugin.getConfig().getBoolean("bungee")) {
         var1.setJoinMessage("");
         if (Arenas.getArenas().size() == 1) {
            Arena var2 = (Arena)Arenas.getArenas().get(0);
            if (var2.getState() != GameState.INGAME && !Arenas.isInArena(var1.getPlayer())) {
               var2.addPlayer(var1.getPlayer());
            }
         }
      }

      if (this.plugin.getConfig().getBoolean("bungee")) {
         if (this.plugin.bungee != null) {
            this.plugin.bungee.addPlayer(var1.getPlayer());
            return;
         }

         if (Arenas.getArenas().size() > 1) {
            this.plugin.setUpForMultiMaps(var1.getPlayer());
         }
      }

   }
}
