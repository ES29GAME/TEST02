package me.joseph.murder.listeners;

import java.util.Iterator;
import java.util.Set;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
   Main plugin;

   public ChatEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void Chat(AsyncPlayerChatEvent var1) {
      if (this.plugin.settings.getConfig().getBoolean("per-arena-chat") && !Arenas.isInArena(var1.getPlayer())) {
         Set var2 = var1.getRecipients();
         Iterator var4 = Bukkit.getOnlinePlayers().iterator();

         while(var4.hasNext()) {
            Player var3 = (Player)var4.next();
            if (Arenas.isInArena(var3)) {
               var2.remove(var3);
            }
         }
      }

      if (Arenas.isInArena(var1.getPlayer())) {
         Arena var8 = Arenas.getArena(var1.getPlayer());
         Iterator var5;
         Set var9;
         Player var10;
         if (this.plugin.settings.getConfig().getBoolean("per-arena-chat") && !var8.specs.contains(var1.getPlayer())) {
            var9 = var1.getRecipients();
            var5 = Bukkit.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
               var10 = (Player)var5.next();
               if (var9.contains(var10)) {
                  var9.remove(var10);
               }
            }

            var5 = var8.getPlayers().iterator();

            while(var5.hasNext()) {
               var10 = (Player)var5.next();
               if (!var9.contains(var10)) {
                  var9.add(var10);
               }
            }
         }

         if (var8.specs.contains(var1.getPlayer())) {
            var9 = var1.getRecipients();
            var5 = Bukkit.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
               var10 = (Player)var5.next();
               if (var9.contains(var10)) {
                  var9.remove(var10);
               }
            }

            Player[] var7;
            int var6 = (var7 = var8.getSpectators()).length;

            for(int var11 = 0; var11 < var6; ++var11) {
               var10 = var7[var11];
               if (!var9.contains(var10)) {
                  var9.add(var10);
               }
            }

            var1.setFormat(this.plugin.FormatText(this.plugin.messages.getConfig().getString("spec-chat-prefix") + var1.getFormat()));
         }
      }

   }
}
