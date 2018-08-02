package me.joseph.murder.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.joseph.murder.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SQLConnection {
   public static Connection c;
   public SQLDatabase MySQL;
   Main plugin;

   public SQLConnection(Main var1, Plugin var2, String var3, String var4, String var5, String var6, String var7) {
      this.plugin = var1;
      this.MySQL = new SQLDatabase(var2, var3, var4, var5, var6, var7);
   }

   public void closeConnection() {
      if (this.isConnected()) {
         try {
            c.close();
         } catch (SQLException var2) {
            ;
         }
      }

   }

   public ResultSet executeQuery(String var1, boolean var2) {
      if (this.isConnected()) {
         try {
            Statement var3 = c.createStatement();
            ResultSet var4 = var3.executeQuery(var1);
            if (var2) {
               var4.next();
            }

            return var4;
         } catch (SQLException var5) {
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean executeUpdate(String var1) {
      if (this.isConnected()) {
         try {
            Statement var2 = c.createStatement();
            var2.executeUpdate(var1);
            return true;
         } catch (SQLException var3) {
            var3.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isConnected() {
      try {
         return !c.isClosed();
      } catch (Exception var2) {
         return false;
      }
   }

   public void openConnection() {
      if (this.isConnected()) {
         this.closeConnection();
      }

      try {
         c = this.MySQL.openConnection();
         Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            public void run() {
               SQLConnection.this.executeUpdate("CREATE TABLE IF NOT EXISTS Account (playername VARCHAR(16), wins INT(10), loses INT(10), deaths INT(10), kills INT(10));");
               SQLConnection.this.executeUpdate("CREATE TABLE IF NOT EXISTS Scores (playername VARCHAR(16), score INT(10));");
            }
         });
         (new BukkitRunnable() {
            public void run() {
               SQLConnection.this.openConnection();
            }
         }).runTaskLater(this.plugin, 100L);
      } catch (ClassNotFoundException var2) {
         var2.printStackTrace();
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

   }
}
