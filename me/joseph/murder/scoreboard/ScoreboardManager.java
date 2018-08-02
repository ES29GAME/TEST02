package me.joseph.murder.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
   private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
   private int lastPage;
   private final String uuid;

   public ScoreboardManager(String var1) {
      this.uuid = var1;
      Objective var2 = this.getPage(0);
      var2.setDisplaySlot(DisplaySlot.SIDEBAR);
   }

   public void addTeam(String var1, OfflinePlayer[] var2) {
      if (this.scoreboard.getTeam(var1) == null) {
         Team var3 = this.scoreboard.registerNewTeam(var1);
         var3.setNameTagVisibility(NameTagVisibility.NEVER);
         var3.setPrefix("" + ChatColor.RESET);
         OfflinePlayer[] var7 = var2;
         int var6 = var2.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            OfflinePlayer var4 = var7[var5];
            if (!var3.hasPlayer(var4)) {
               var3.addPlayer(var4);
            }
         }
      }

   }

   public void changePage(int var1) {
      this.lastPage = var1;
      if (this.scoreboard.getObjective(DisplaySlot.SIDEBAR) != null) {
         this.getPage(var1).setDisplaySlot(DisplaySlot.SIDEBAR);
      }

   }

   public int getLastPage() {
      return this.lastPage;
   }

   private Objective getPage(int var1) {
      if (var1 > 15) {
         throw new IllegalArgumentException("Page number must be between 0 and 15");
      } else {
         Objective var2 = this.scoreboard.getObjective("page" + var1);
         if (var2 == null) {
            var2 = this.scoreboard.registerNewObjective("page" + var1, "dummy");

            for(int var3 = 0; var3 < 15; ++var3) {
               this.scoreboard.registerNewTeam(ChatColor.getByChar(Integer.toHexString(var1)) + ChatColor.getByChar(Integer.toHexString(var3)).toString());
            }
         }

         return var2;
      }
   }

   private Player getPlayer() {
      return Bukkit.getPlayer(this.uuid);
   }

   public Scoreboard getScoreboard() {
      return this.scoreboard;
   }

   public void setLine(int var1, int var2, String var3) {
      this.setLine(var1, var2, var3, true);
   }

   public void setLine(int var1, int var2, String var3, boolean var4) {
      if (var3.length() > 16) {
         this.setLine(var1, var2, var3.substring(0, 16), var3.substring(16), var4);
      } else {
         this.setLine(var1, var2, var3, "", var4);
      }

   }

   public void setLine(int var1, int var2, String var3, String var4) {
      this.setLine(var1, var2, var3, var4, true);
   }

   public void setLine(int var1, int var2, String var3, String var4, boolean var5) {
      if (var3.length() > 16) {
         var3 = var3.substring(0, 16);
      }

      if (var4.length() > 16) {
         var4 = var4.substring(0, 16);
      }

      if (var2 >= 0 && var2 <= 14) {
         Objective var6 = this.getPage(var1);
         String var7 = ChatColor.getByChar(Integer.toHexString(var1)) + ChatColor.getByChar(Integer.toHexString(var2)).toString();
         Score var8 = var6.getScore(var7 + ChatColor.RESET);
         Team var9 = this.scoreboard.getTeam(var7);
         if (!var8.isScoreSet()) {
            var8.setScore(var2);
            var9.addEntry(var8.getEntry());
         }

         var9.setPrefix(var3);
         if (var5) {
            var4 = ChatColor.getLastColors(var3) + var4;
            if (var4.length() > 16) {
               var4 = var4.substring(0, 16);
            }
         }

         var9.setSuffix(var4);
      } else {
         throw new IllegalArgumentException("You can only get a line from 0 - 14");
      }
   }

   public void setLineBlank(int var1, int var2) {
      this.setLine(var1, var2, "", "", false);
   }

   public void setTitle(int var1, String var2) {
      if (var2 == null) {
         var2 = "";
      }

      if (var2.length() > 32) {
         var2 = var2.substring(0, 32);
      }

      this.getPage(var1).setDisplayName(var2);
   }

   public void toggleScoreboard() {
      if (this.getPlayer() != null && !this.getPlayer().getScoreboard().equals(this.scoreboard)) {
         this.getPlayer().setScoreboard(this.scoreboard);
         if (this.scoreboard.getTeam("team") != null) {
            this.scoreboard.getTeam("team").unregister();
         }
      } else if (this.scoreboard.getObjective(DisplaySlot.SIDEBAR) == null) {
         this.getPage(this.lastPage).setDisplaySlot(DisplaySlot.SIDEBAR);
      }

   }
}
