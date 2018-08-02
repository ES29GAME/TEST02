package me.joseph.murder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;

public class SignManager {
   Main plugin;

   public SignManager(Main var1) {
      this.plugin = var1;
   }

   public void addSign(Arena var1, Location var2) {
      String var3 = var1.getName();
      this.plugin.arenas.getConfig().set("Signs." + var3 + ".X", var2.getX());
      this.plugin.arenas.getConfig().set("Signs." + var3 + ".Y", var2.getY());
      this.plugin.arenas.getConfig().set("Signs." + var3 + ".Z", var2.getZ());
      this.plugin.arenas.getConfig().set("Signs." + var3 + ".World", var2.getWorld().getName());
      this.plugin.arenas.save();
   }

   public List getSigns(Arena var1) {
      String var2 = var1.getName();
      if (!this.plugin.arenas.getConfig().contains("Signs." + var2)) {
         return null;
      } else {
         ArrayList var3 = new ArrayList();
         double var4 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".X");
         double var6 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".Y");
         double var8 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".Z");
         World var10 = Bukkit.getWorld(this.plugin.arenas.getConfig().getString("Signs." + var2 + ".World"));
         Location var11 = new Location(var10, var4, var6, var8);
         var3.add(var11);
         return var3;
      }
   }

   public void removeSign(Arena var1, Location var2) {
      String var3 = var1.getName();
      this.plugin.arenas.getConfig().set("Signs." + var3, (Object)null);
      this.resetSigns(var1);
      this.plugin.arenas.save();
   }

   private void resetSigns(Arena var1) {
      String var2 = var1.getName();
      if (this.plugin.arenas.getConfig().contains("Signs." + var2 + ".X.")) {
         double var3 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".X");
         double var5 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".Y");
         double var7 = this.plugin.arenas.getConfig().getDouble("Signs." + var2 + ".Z");
         String var9 = this.plugin.arenas.getConfig().getString("Signs." + var2 + ".World");
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".X", (Object)null);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".Y", (Object)null);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".Z", (Object)null);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".World", (Object)null);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".X", var3);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".Y", var5);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".Z", var7);
         this.plugin.arenas.getConfig().set("Signs." + var2 + ".World", var9);
         this.plugin.arenas.save();
      }

   }

   public void updateSigns(Arena var1) {
      if (this.plugin.arenas.getConfig().contains("Signs." + var1.getName())) {
         Iterator var3 = this.getSigns(var1).iterator();

         while(var3.hasNext()) {
            Location var2 = (Location)var3.next();
            if (var2.getBlock().getState() instanceof Sign) {
               Sign var4 = (Sign)var2.getBlock().getState();
               var4.setLine(3, this.plugin.messages.getConfig().getString("players").replaceAll("&", "\u00a7").replaceAll("max", String.valueOf(this.plugin.SpawnSize(var1))).replaceAll("min", String.valueOf(var1.players.size())));
               var4.setLine(0, this.plugin.messages.getConfig().getString("sign-header").replaceAll("&", "\u00a7"));
               var4.setLine(1, this.plugin.messages.getConfig().getString("sign-arena").replaceAll("&", "\u00a7").replaceAll("%arena%", var1.getName()));
               if (var1.getState() == GameState.INGAME) {
                  var4.setLine(2, this.plugin.messages.getConfig().getString("sign-ingame").replaceAll("&", "\u00a7"));
               } else if (var1.getState() == GameState.LOBBY) {
                  var4.setLine(2, this.plugin.messages.getConfig().getString("sign-lobby").replaceAll("&", "\u00a7"));
               } else if (var1.getState() == GameState.STARTING) {
                  var4.setLine(2, this.plugin.messages.getConfig().getString("sign-starting").replaceAll("&", "\u00a7"));
               }

               var4.update();
            }
         }

      }
   }
}
