package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
   Main plugin;

   public QuitEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void entitydamage(PlayerQuitEvent var1) {
      Player var2 = var1.getPlayer();
      if (this.plugin.getConfig().getBoolean("update-data-on-player-quit") && this.plugin.getPlayerData(var2) != null) {
         this.plugin.removePlayerData(var2);
      }

      if (Arenas.isInArena(var2)) {
         Arena var3 = Arenas.getArena(var2);
         if (var3.pic.contains(var2.getName())) {
            var3.pic.remove(var2.getName());
         }

         if (var3.specs.contains(var2)) {
            this.plugin.setup(var2);
            var3.specs.remove(var2);
            this.plugin.restoreInventory(var2);
            Arenas.removeArena(var2);
            if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
               var2.teleport(this.plugin.getLobby());
            }

            return;
         }

         var3.removePlayer(var2, "leave");
      }

      if (this.plugin.getConfig().getBoolean("bungee")) {
         var1.setQuitMessage("");
         if (this.plugin.votes.containsKey(var2.getName()) && ((Integer)this.plugin.point.get(this.plugin.votes.get(var2.getName()))).intValue() > 0) {
            this.plugin.point.put((String)this.plugin.votes.get(var2.getName()), ((Integer)this.plugin.point.get(this.plugin.votes.get(var2.getName()))).intValue() - 1);
         }
      }

   }
}
