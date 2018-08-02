package me.joseph.murder;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerData {
   public Player p;
   int kills = 0;
   int deaths = 0;
   int wins = 0;
   int score = 0;
   int loses = 0;
   Main plugin;

   public PlayerData(Main var1, Player var2) {
      this.plugin = var1;
      this.p = var2;
      this.reset();
      this.Update();
   }

   public void addscore(Integer var1) {
      this.score = this.getscore().intValue() + var1.intValue();
   }

   public void adddeaths(Integer var1) {
      this.deaths = this.getdeaths().intValue() + var1.intValue();
   }

   public void addkill(Integer var1) {
      this.kills = this.getkill().intValue() + var1.intValue();
   }

   public void addlose(Integer var1) {
      this.loses = this.getloses().intValue() + var1.intValue();
   }

   public void addwins(Integer var1) {
      this.wins = this.getwins().intValue() + var1.intValue();
   }

   public Integer getscore() {
      return this.score;
   }

   public Integer getdeaths() {
      return this.deaths;
   }

   public Integer getkill() {
      return this.kills;
   }

   public Integer getloses() {
      return this.loses;
   }

   public Integer getwins() {
      return this.wins;
   }

   public void reset() {
      this.kills = 0;
      this.deaths = 0;
      this.wins = 0;
      this.loses = 0;
   }

   public void Update() {
      if (!this.plugin.getConfig().getBoolean("mysql")) {
         this.kills = this.plugin.api.getKills(this.p);
         this.deaths = this.plugin.api.getDeaths(this.p);
         this.wins = this.plugin.api.getWins(this.p);
         this.loses = this.plugin.api.getLoses(this.p);
         this.score = this.plugin.api.getScore(this.p);
      }

      if (this.plugin.getConfig().getBoolean("mysql")) {
         Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            public void run() {
               ResultSet var1 = PlayerData.this.plugin.getMainSQLConnection().executeQuery("SELECT * FROM `Account` WHERE playername='" + PlayerData.this.p.getName() + "'", false);
               ResultSet var2 = PlayerData.this.plugin.getMainSQLConnection().executeQuery("SELECT * FROM `Scores` WHERE playername='" + PlayerData.this.p.getName() + "'", false);

               try {
                  if (var1.next()) {
                     PlayerData.this.deaths = Integer.parseInt(var1.getString("deaths"));
                     PlayerData.this.wins = Integer.parseInt(var1.getString("wins"));
                     PlayerData.this.loses = Integer.parseInt(var1.getString("loses"));
                     PlayerData.this.kills = Integer.parseInt(var1.getString("kills"));
                     var1.close();
                  }

                  if (var2.next()) {
                     PlayerData.this.score = Integer.parseInt(var2.getString("score"));
                     var2.close();
                  }
               } catch (SQLException var4) {
                  ;
               }

            }
         });
      }

   }
}
