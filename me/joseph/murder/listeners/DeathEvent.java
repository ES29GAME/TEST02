package me.joseph.murder.listeners;

import java.util.Iterator;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import me.joseph.murder.PlayerType;
import me.joseph.murder.events.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

public class DeathEvent implements Listener {
   Main plugin;

   public DeathEvent(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onDeath2(PlayerDeathEvent var1) {
      Player var2 = var1.getEntity().getPlayer();
      if (Arenas.isInArena(var2)) {
         Arena var3 = Arenas.getArena(var2);
         if (Bukkit.getPluginManager().isPluginEnabled("CorpseReborn")) {
            CorpseData var4 = CorpseAPI.spawnCorpse(var2, var2.getLocation());
            var3.data.add(var4);
         }

         var1.setDroppedExp(0);
         var1.getDrops().clear();
         var1.setDeathMessage("");
         var2.setHealth(var2.getMaxHealth());
         Player var8;
         if (!(var1.getEntity().getKiller() instanceof Player)) {
            if (this.plugin.settings.getConfig().getBoolean("death-messages")) {
               Iterator var9 = var3.getPlayers().iterator();

               while(var9.hasNext()) {
                  var8 = (Player)var9.next();
                  var8.sendMessage(this.plugin.messages.getConfig().getString("death-message").replaceAll("&", "\u00a7").replaceAll("%player%", var1.getEntity().getPlayer().getName()));
               }
            }

            if (this.plugin.getPlayerData(var2) != null) {
               this.plugin.getPlayerData(var2).adddeaths(Integer.valueOf(1));
               this.plugin.getPlayerData(var2).addlose(Integer.valueOf(1));
            }

            if (Bukkit.getPlayer(var3.Murderer) != null) {
               var3.addscore(Bukkit.getPlayer(var3.Murderer), this.plugin.settings.getConfig().getInt("score-on-kill"), this.plugin.FormatText(this.plugin.messages.getConfig().getString("kill-reason")));
               if (this.plugin.getPlayerData(Bukkit.getPlayer(var3.Murderer)) != null) {
                  this.plugin.getPlayerData(Bukkit.getPlayer(var3.Murderer)).addkill(Integer.valueOf(1));
               }

               var3.addkill(Bukkit.getPlayer(var3.Murderer), Integer.valueOf(1));
            }

            if (Arenas.isInArena(var2)) {
               var3.removePlayer(var2, "death");
            }

            return;
         }

         if (var1.getEntity().getKiller() instanceof Player) {
            var8 = var1.getEntity();
            Player var5 = var1.getEntity().getKiller();
            if (this.plugin.getPlayerData(var2) != null) {
               this.plugin.getPlayerData(var2).adddeaths(Integer.valueOf(1));
               this.plugin.getPlayerData(var2).addlose(Integer.valueOf(1));
            }

            if (this.plugin.getPlayerData(var5) != null) {
               this.plugin.getPlayerData(var5).addkill(Integer.valueOf(1));
            }

            var3.addkill(var5, Integer.valueOf(1));
            TitleAPI.sendTitle(var8, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("death-title").replaceAll("&", "\u00a7").replaceAll("%killer%", var5.getName()));
            TitleAPI.sendSubtitle(var8, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("death-subtitle").replaceAll("&", "\u00a7").replaceAll("%killer%", var5.getName()));
            if (this.plugin.settings.getConfig().getBoolean("enable-sounds")) {
               var5.playSound(var2.getLocation(), Sound.valueOf(this.plugin.settings.getConfig().getString("KILL_SOUND")), 1.0F, 1.0F);
            }

            if (var3.getType(var5) != PlayerType.Murderer && var3.getType(var2) == PlayerType.Murderer && var3.getType(var2) != PlayerType.None && var3.getType(var5) != PlayerType.None) {
               if (Arenas.isInArena(var2)) {
                  var3.removePlayer(var2, "death");
               }

               var3.addscore(var5, this.plugin.settings.getConfig().getInt("score-on-kill"), this.plugin.FormatText(this.plugin.messages.getConfig().getString("kill-reason")));
            }

            if (var3.getType(var5) != PlayerType.Murderer && var3.getType(var2) != PlayerType.Murderer && var3.getType(var2) != PlayerType.None && var3.getType(var5) != PlayerType.None) {
               if (Arenas.isInArena(var2)) {
                  var3.removePlayer(var2, "death");
               }

               if (Arenas.isInArena(var5)) {
                  var3.removePlayer(var5, "death");
               }
            }

            if (var3.getType(var5) == PlayerType.Murderer && var3.getType(var8) == PlayerType.Detective && var3.getType(var2) != PlayerType.None && var3.getType(var5) != PlayerType.None && var5.getItemInHand().getType() == Material.getMaterial(this.plugin.settings.getConfig().getInt("murderer-weapon.item-id"))) {
               if (Arenas.isInArena(var2)) {
                  var3.removePlayer(var2, "death");
               }

               var3.addscore(var5, this.plugin.settings.getConfig().getInt("score-on-kill"), this.plugin.FormatText(this.plugin.messages.getConfig().getString("kill-reason")));
            }

            if (var3.getType(var5) == PlayerType.Murderer && var3.getType(var8) == PlayerType.Innocents && var3.getType(var2) != PlayerType.None && var3.getType(var5) != PlayerType.None && var5.getItemInHand().getType() == Material.getMaterial(this.plugin.settings.getConfig().getInt("murderer-weapon.item-id"))) {
               if (Arenas.isInArena(var2)) {
                  var3.removePlayer(var2, "death");
               }

               var3.addscore(var5, this.plugin.settings.getConfig().getInt("score-on-kill"), this.plugin.FormatText(this.plugin.messages.getConfig().getString("kill-reason")));
            }

            if (this.plugin.settings.getConfig().getBoolean("death-messages")) {
               Iterator var7 = var3.getPlayers().iterator();

               while(var7.hasNext()) {
                  Player var6 = (Player)var7.next();
                  var6.sendMessage(this.plugin.messages.getConfig().getString("death-message").replaceAll("&", "\u00a7").replaceAll("%player%", var1.getEntity().getPlayer().getName()));
               }
            }

            if (Arenas.isInArena(var2)) {
               var3.removePlayer(var2, "death");
            }

            return;
         }
      }

   }
}
