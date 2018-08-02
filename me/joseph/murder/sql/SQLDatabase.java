package me.joseph.murder.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import org.bukkit.plugin.Plugin;

public class SQLDatabase extends Database {
   private final String user;
   private final String database;
   private final String password;
   private final String port;
   private final String hostname;

   public SQLDatabase(Plugin var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1);
      this.hostname = var2;
      this.port = var3;
      this.database = var4;
      this.user = var5;
      this.password = var6;
   }

   public Connection openConnection() {
      if (this.checkConnection()) {
         return this.connection;
      } else {
         Class.forName("com.mysql.jdbc.Driver");
         this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
         return this.connection;
      }
   }
}
