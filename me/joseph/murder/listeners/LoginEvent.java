package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginEvent implements Listener {
   Main plugin;

   public LoginEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onLogin(PlayerLoginEvent var1) {
      if (this.plugin.getConfig().getBoolean("bungee") && Arenas.getArenas().size() == 1) {
         Arena var2 = (Arena)Arenas.getArenas().get(0);
         if (var2.getState() == GameState.INGAME) {
            var1.setKickMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("join-error")));
            var1.setResult(Result.KICK_FULL);
         }
      }

      if (this.plugin.getConfig().getBoolean("bungee") && Arenas.getArenas().size() > 1 && this.plugin.bungee != null && this.plugin.bungee.getState() == GameState.INGAME) {
         var1.setKickMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("join-error")));
         var1.setResult(Result.KICK_FULL);
      }

   }
}
