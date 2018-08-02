package me.joseph.murder.events;

import java.lang.reflect.Constructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleAPI {
   public static void clearTitle(Player var0) {
      sendTitle(var0, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), "", "");
   }

   public static Class getNMSClass(String var0) {
      String var1 = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

      try {
         return Class.forName("net.minecraft.server." + var1 + "." + var0);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   /** @deprecated */
   @Deprecated
   public static void sendFullTitle(Player var0, Integer var1, Integer var2, Integer var3, String var4, String var5) {
      sendTitle(var0, var1, var2, var3, var4, var5);
   }

   public static void sendPacket(Player var0, Object var1) {
      try {
         Object var2 = var0.getClass().getMethod("getHandle").invoke(var0);
         Object var3 = var2.getClass().getField("playerConnection").get(var2);
         var3.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(var3, var1);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   /** @deprecated */
   @Deprecated
   public static void sendSubtitle(Player var0, Integer var1, Integer var2, Integer var3, String var4) {
      sendTitle(var0, var1, var2, var3, (String)null, var4);
   }

   /** @deprecated */
   @Deprecated
   public static void sendTitle(Player var0, Integer var1, Integer var2, Integer var3, String var4) {
      sendTitle(var0, var1, var2, var3, var4, (String)null);
   }

   public static void sendTitle(Player var0, Integer var1, Integer var2, Integer var3, String var4, String var5) {
      try {
         Object var6;
         Constructor var9;
         if (var4 != null) {
            var4 = ChatColor.translateAlternateColorCodes('&', var4);
            var4 = var4.replaceAll("%player%", var0.getDisplayName());
            var6 = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)null);
            Object var7 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + var4 + "\"}");
            var9 = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object var10 = var9.newInstance(var6, var7, var1, var2, var3);
            sendPacket(var0, var10);
            var6 = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object)null);
            var7 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + var4 + "\"}");
            var9 = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
            var10 = var9.newInstance(var6, var7);
            sendPacket(var0, var10);
         }

         if (var5 != null) {
            var5 = ChatColor.translateAlternateColorCodes('&', var5);
            var5 = var5.replaceAll("%player%", var0.getDisplayName());
            var6 = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object)null);
            Object var8 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + var4 + "\"}");
            var9 = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object var11 = var9.newInstance(var6, var8, var1, var2, var3);
            sendPacket(var0, var11);
            var6 = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((Object)null);
            var8 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke((Object)null, "{\"text\":\"" + var5 + "\"}");
            var9 = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            var11 = var9.newInstance(var6, var8, var1, var2, var3);
            sendPacket(var0, var11);
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
