package me.joseph.murder.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import me.joseph.murder.sql.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MurderAPI {
   Main plugin;

   public MurderAPI(Main var1) {
      this.plugin = var1;
   }

   public void createAccount(final Player var1) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
         public void run() {
            MurderAPI.this.plugin.sqlConnection.executeUpdate("INSERT INTO `Account` (playername, wins, deaths, loses, kills) VALUES ('" + var1.getName() + "', '0', '0', '0', '0');");
         }
      });
   }

   public void createAccount2(final Player var1) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
         public void run() {
            MurderAPI.this.plugin.sqlConnection.executeUpdate("INSERT INTO `Scores` (playername, score) VALUES ('" + var1.getName() + "', '0');");
         }
      });
   }

   public boolean existsInDatabase(Player var1) {
      ResultSet var2 = this.getMainSQLConnection().executeQuery("SELECT * FROM `Account` WHERE playername='" + var1.getName() + "'", false);

      try {
         if (var2.next()) {
            var2.close();
            return true;
         } else {
            var2.close();
            return false;
         }
      } catch (SQLException var4) {
         return false;
      }
   }

   public boolean existsInDatabase2(Player var1) {
      ResultSet var2 = this.getMainSQLConnection().executeQuery("SELECT * FROM `Scores` WHERE playername='" + var1.getName() + "'", false);

      try {
         if (var2.next()) {
            var2.close();
            return true;
         } else {
            var2.close();
            return false;
         }
      } catch (SQLException var4) {
         return false;
      }
   }

   public int getScore(Player var1) {
      return this.plugin.data.getConfig().getInt("Score." + var1.getUniqueId() + ".score");
   }

   public int getDeaths(Player var1) {
      return this.plugin.data.getConfig().getInt("Deaths." + var1.getUniqueId() + ".death");
   }

   public int getKills(Player var1) {
      return this.plugin.data.getConfig().getInt("Kills." + var1.getUniqueId() + ".kill");
   }

   public int getLoses(Player var1) {
      return this.plugin.data.getConfig().getInt("Loses." + var1.getUniqueId() + ".lose");
   }

   public SQLConnection getMainSQLConnection() {
      return this.plugin.sqlConnection;
   }

   public int getWins(Player var1) {
      return this.plugin.data.getConfig().getInt("Wins." + var1.getUniqueId() + ".win");
   }

   public void heroreward(Player var1) {
      if (this.plugin.settings.getConfig().getBoolean("hero-rewards")) {
         if (Arenas.isInArena(var1)) {
            Arena var2 = Arenas.getArena(var1);
            int var3;
            int var4;
            List var5;
            String var6;
            Iterator var7;
            if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".hero-rewards") && this.plugin.rewards.getConfig().contains("hero-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection("hero-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains("hero-rewards." + var4)) {
                  this.heroreward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList("hero-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }

            if (this.plugin.rewards.getConfig().contains(var2.getName() + ".hero-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection(var2.getName() + ".hero-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".hero-rewards." + var4)) {
                  this.heroreward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList(var2.getName() + ".hero-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }
         }

      }
   }

   public void losereward(Player var1) {
      if (this.plugin.settings.getConfig().getBoolean("lose-rewards")) {
         if (Arenas.isInArena(var1)) {
            Arena var2 = Arenas.getArena(var1);
            int var3;
            int var4;
            List var5;
            String var6;
            Iterator var7;
            if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".lose-rewards") && this.plugin.rewards.getConfig().contains("lose-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection("lose-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains("lose-rewards." + var4)) {
                  this.losereward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList("lose-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }

            if (this.plugin.rewards.getConfig().contains(var2.getName() + ".lose-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection(var2.getName() + ".lose-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".lose-rewards." + var4)) {
                  this.losereward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList(var2.getName() + ".lose-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }
         }

      }
   }

   public void sendActionBar(Player var1, String var2) {
      if (var1.isOnline()) {
         if (this.plugin.nmsver.startsWith("v1_12_")) {
            this.sendActionBarPost112(var1, var2);
         } else {
            this.sendActionBarPre112(var1, var2);
         }

      }
   }

   private void sendActionBarPost112(Player var1, String var2) {
      if (var1.isOnline()) {
         try {
            Class var3 = Class.forName("org.bukkit.craftbukkit." + this.plugin.nmsver + ".entity.CraftPlayer");
            Object var4 = var3.cast(var1);
            Class var6 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".PacketPlayOutChat");
            Class var7 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".Packet");
            Class var8 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".ChatComponentText");
            Class var9 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".IChatBaseComponent");
            Class var10 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".ChatMessageType");
            Object[] var11 = var10.getEnumConstants();
            Object var12 = null;
            Object[] var16 = var11;
            int var15 = var11.length;

            Object var13;
            for(int var14 = 0; var14 < var15; ++var14) {
               var13 = var16[var14];
               if (var13.toString().equals("GAME_INFO")) {
                  var12 = var13;
               }
            }

            var13 = var8.getConstructor(String.class).newInstance(var2);
            Object var5 = var6.getConstructor(var9, var10).newInstance(var13, var12);
            Method var20 = var3.getDeclaredMethod("getHandle");
            Object var21 = var20.invoke(var4);
            Field var22 = var21.getClass().getDeclaredField("playerConnection");
            Object var17 = var22.get(var21);
            Method var18 = var17.getClass().getDeclaredMethod("sendPacket", var7);
            var18.invoke(var17, var5);
         } catch (Exception var19) {
            var19.printStackTrace();
            this.plugin.works = false;
         }

      }
   }

   private void sendActionBarPre112(Player var1, String var2) {
      if (var1.isOnline()) {
         try {
            Class var3 = Class.forName("org.bukkit.craftbukkit." + this.plugin.nmsver + ".entity.CraftPlayer");
            Object var4 = var3.cast(var1);
            Class var6 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".PacketPlayOutChat");
            Class var7 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".Packet");
            Object var5;
            Class var8;
            Class var9;
            Object var11;
            if (this.plugin.useOldMethods) {
               var8 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".ChatSerializer");
               var9 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".IChatBaseComponent");
               Method var10 = var8.getDeclaredMethod("a", String.class);
               var11 = var9.cast(var10.invoke(var8, "{\"text\": \"" + var2 + "\"}"));
               var5 = var6.getConstructor(var9, Byte.TYPE).newInstance(var11, 2);
            } else {
               var8 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".ChatComponentText");
               var9 = Class.forName("net.minecraft.server." + this.plugin.nmsver + ".IChatBaseComponent");
               Object var16 = var8.getConstructor(String.class).newInstance(var2);
               var5 = var6.getConstructor(var9, Byte.TYPE).newInstance(var16, 2);
            }

            Method var14 = var3.getDeclaredMethod("getHandle");
            Object var15 = var14.invoke(var4);
            Field var17 = var15.getClass().getDeclaredField("playerConnection");
            var11 = var17.get(var15);
            Method var12 = var11.getClass().getDeclaredMethod("sendPacket", var7);
            var12.invoke(var11, var5);
         } catch (Exception var13) {
            var13.printStackTrace();
            this.plugin.works = false;
         }

      }
   }

   public void setNonSQLData(Player var1, int var2, int var3, int var4, int var5, int var6) {
      this.plugin.data.getConfig().set("Deaths." + var1.getUniqueId() + ".death", var3);
      this.plugin.data.getConfig().set("Loses." + var1.getUniqueId() + ".lose", var4);
      this.plugin.data.getConfig().set("Wins." + var1.getUniqueId() + ".win", var5);
      this.plugin.data.getConfig().set("Kills." + var1.getUniqueId() + ".kill", var2);
      this.plugin.data.getConfig().set("Score." + var1.getUniqueId() + ".score", var6);
      this.plugin.data.save();
   }

   public void setSQLData(final Player var1, final int var2, final int var3, final int var4, final int var5, final int var6) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
         public void run() {
            MurderAPI.this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET kills='" + var2 + "' WHERE playername='" + var1.getName() + "'");
            MurderAPI.this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET deaths='" + var3 + "' WHERE playername='" + var1.getName() + "'");
            MurderAPI.this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET loses='" + var4 + "' WHERE playername='" + var1.getName() + "'");
            MurderAPI.this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET wins='" + var5 + "' WHERE playername='" + var1.getName() + "'");
            MurderAPI.this.plugin.sqlConnection.executeUpdate("UPDATE `Scores` SET score='" + var6 + "' WHERE playername='" + var1.getName() + "'");
         }
      });
   }

   public void setSQLDataInstantly(Player var1, int var2, int var3, int var4, int var5, int var6) {
      this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET kills='" + var2 + "' WHERE playername='" + var1.getName() + "'");
      this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET deaths='" + var3 + "' WHERE playername='" + var1.getName() + "'");
      this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET loses='" + var4 + "' WHERE playername='" + var1.getName() + "'");
      this.plugin.sqlConnection.executeUpdate("UPDATE `Account` SET wins='" + var5 + "' WHERE playername='" + var1.getName() + "'");
      this.plugin.sqlConnection.executeUpdate("UPDATE `Scores` SET score='" + var6 + "' WHERE playername='" + var1.getName() + "'");
   }

   public void winreward(Player var1) {
      if (this.plugin.settings.getConfig().getBoolean("win-rewards")) {
         if (Arenas.isInArena(var1)) {
            Arena var2 = Arenas.getArena(var1);
            int var3;
            int var4;
            List var5;
            String var6;
            Iterator var7;
            if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".win-rewards") && this.plugin.rewards.getConfig().contains("win-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection("win-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains("win-rewards." + var4)) {
                  this.winreward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList("win-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }

            if (this.plugin.rewards.getConfig().contains(var2.getName() + ".win-rewards")) {
               var3 = this.plugin.rewards.getConfig().getConfigurationSection(var2.getName() + ".win-rewards").getKeys(true).size();
               var4 = var2.getRandom(0, var3);
               if (!this.plugin.rewards.getConfig().contains(var2.getName() + ".win-rewards." + var4)) {
                  this.winreward(var1);
                  return;
               }

               var5 = this.plugin.rewards.getConfig().getStringList(var2.getName() + ".win-rewards." + var4);
               var7 = var5.iterator();

               while(var7.hasNext()) {
                  var6 = (String)var7.next();
                  Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), var6.replaceAll("%player%", var1.getName()));
               }

               return;
            }
         }

      }
   }
}
