package me.joseph.murder.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.Iterator;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveItem implements Listener {
   Main plugin;

   public LeaveItem(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   private void ClickItem(PlayerInteractEvent var1) {
      if (var1.getPlayer().getItemInHand() != null) {
         final Player var2 = var1.getPlayer();
         if (var1.getAction().name().toLowerCase().contains("right")) {
            if (var2.getItemInHand().getType() == Material.AIR) {
               return;
            }

            ItemStack var3;
            ItemMeta var4;
            String var5;
            if ((var3 = var2.getItemInHand()).hasItemMeta() && (var4 = var3.getItemMeta()).hasDisplayName() && (var5 = var4.getDisplayName()) != null) {
               Arena var6;
               if (var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("quit.item-name")))) {
                  var1.setCancelled(true);
                  if (Arenas.isInArena(var2)) {
                     var6 = Arenas.getArena(var2);
                     if (var6.specs.contains(var2)) {
                        this.plugin.setup(var2);
                        var6.specs.remove(var2);
                        this.plugin.restoreInventory(var2);
                        Arenas.removeArena(var2);
                        if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                           var2.teleport(this.plugin.getLobby());
                        }

                        if (this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                           ByteArrayDataOutput var7 = ByteStreams.newDataOutput();
                           var7.writeUTF("Connect");
                           var7.writeUTF(this.plugin.getConfig().getString("lobby-server"));
                           var2.sendPluginMessage(this.plugin, "BungeeCord", var7.toByteArray());
                        }

                        var2.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                        return;
                     }
                  }
               }

               if (var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("rejoin.item-name")))) {
                  var1.setCancelled(true);
                  if (Arenas.isInArena(var2)) {
                     var6 = Arenas.getArena(var2);
                     if (var6.specs.contains(var2)) {
                        this.plugin.setup(var2);
                        var6.specs.remove(var2);
                        this.plugin.restoreInventory(var2);
                        Arenas.removeArena(var2);
                        var2.teleport(this.plugin.getLobby());
                        (new BukkitRunnable() {
                           public void run() {
                              if (Arenas.getArenas().size() > 0) {
                                 Iterator var2x = Arenas.getArenas().iterator();

                                 while(true) {
                                    Arena var1;
                                    do {
                                       if (!var2x.hasNext()) {
                                          return;
                                       }

                                       var1 = (Arena)var2x.next();
                                    } while(var1.getState() != GameState.STARTING && var1.getState() != GameState.LOBBY);

                                    if (!Arenas.isInArena(var2)) {
                                       var1.addPlayer(var2);
                                    }
                                 }
                              }
                           }
                        }).runTaskLater(this.plugin, (long)(20 * this.plugin.settings.getConfig().getInt("rejoin-interval")));
                        return;
                     }
                  }
               }

               if (var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("quit3.item-name")))) {
                  var1.setCancelled(true);
                  if (!Arenas.isInArena(var1.getPlayer()) && this.plugin.getConfig().getBoolean("bungee")) {
                     if (this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                        ByteArrayDataOutput var8 = ByteStreams.newDataOutput();
                        var8.writeUTF("Connect");
                        var8.writeUTF(this.plugin.getConfig().getString("lobby-server"));
                        if (this.plugin.isEnabled()) {
                           var2.sendPluginMessage(this.plugin, "BungeeCord", var8.toByteArray());
                        }
                     }

                     if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                        var2.kickPlayer(this.plugin.FormatText(this.plugin.messages.getConfig().getString("kick-message")));
                     }

                     return;
                  }
               }

               if (var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("quit2.item-name")))) {
                  var1.setCancelled(true);
                  this.plugin.leave2(var2);
               }
            }
         }
      }

   }
}
