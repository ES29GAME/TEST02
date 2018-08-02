package me.joseph.murder.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bukkit.plugin.Plugin;

public abstract class Database {
   protected Connection connection;
   protected Plugin plugin;

   protected Database(Plugin var1) {
      this.plugin = var1;
      this.connection = null;
   }

   public boolean checkConnection() {
      return this.connection != null && !this.connection.isClosed();
   }

   public boolean closeConnection() {
      if (this.connection == null) {
         return false;
      } else {
         this.connection.close();
         return true;
      }
   }

   public Connection getConnection() {
      return this.connection;
   }

   public abstract Connection openConnection();

   public ResultSet querySQL(String var1) {
      if (!this.checkConnection()) {
         this.openConnection();
      }

      Statement var2 = this.connection.createStatement();
      ResultSet var3 = var2.executeQuery(var1);
      return var3;
   }

   public int updateSQL(String var1) {
      if (!this.checkConnection()) {
         this.openConnection();
      }

      Statement var2 = this.connection.createStatement();
      int var3 = var2.executeUpdate(var1);
      return var3;
   }
}
