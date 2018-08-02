package me.joseph.murder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;

public class Arenas {
   private static HashMap arenas = new HashMap();
   private static HashMap playerArena = new HashMap();
   private static List list = new ArrayList();
   static Main plugin;

   public static void addArena(Arena var0) {
      if (!arenas.containsKey(var0.getName())) {
         arenas.put(var0.getName(), var0);
         if (!list.contains(var0)) {
            list.add(var0);
         }
      }

   }

   public static void addArena(Player var0, Arena var1) {
      if (!playerArena.containsKey(var0.getName())) {
         playerArena.put(var0.getName(), var1);
      }

   }

   public static boolean arenaExists(String var0) {
      return arenas.containsKey(var0);
   }

   public static Arena getArena(Player var0) {
      String var1 = var0.getName();
      if (playerArena.containsKey(var1)) {
         Arena var2 = (Arena)playerArena.get(var1);
         return var2;
      } else {
         return null;
      }
   }

   public static Arena getArena(String var0) {
      if (arenas.containsKey(var0)) {
         Arena var1 = (Arena)arenas.get(var0);
         return var1;
      } else {
         return null;
      }
   }

   public static List getArenas() {
      return list;
   }

   public static boolean isInArena(Player var0) {
      return playerArena.containsKey(var0.getName());
   }

   public static void removeArena(Player var0) {
      if (playerArena.containsKey(var0.getName())) {
         playerArena.remove(var0.getName());
      }

   }

   public Arenas(Main var1) {
      plugin = var1;
   }
}
