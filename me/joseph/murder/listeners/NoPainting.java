package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NoPainting implements Listener {
   Main plugin;

   public NoPainting(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onArrow(ProjectileHitEvent var1) {
      if (var1.getEntity() instanceof Arrow) {
         Arrow var2 = (Arrow)var1.getEntity();
         if (var2.getShooter() instanceof Player) {
            Player var3 = (Player)var2.getShooter();
            if (Arenas.isInArena(var3)) {
               var1.getEntity().remove();
            }
         }
      }

   }

   @EventHandler
   public void onFrameBrake(HangingBreakByEntityEvent var1) {
      Player var2;
      if (var1.getEntity().getType() == EntityType.ITEM_FRAME && var1.getRemover() instanceof Player) {
         var2 = (Player)var1.getRemover();
         if (Arenas.isInArena(var2) || this.plugin.getConfig().getBoolean("bungee")) {
            var1.setCancelled(true);
         }
      }

      if (var1.getEntity().getType() == EntityType.PAINTING && var1.getRemover() instanceof Player) {
         var2 = (Player)var1.getRemover();
         if (Arenas.isInArena(var2) || this.plugin.getConfig().getBoolean("bungee")) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void Target2(EntityTargetEvent var1) {
      if (var1.getTarget() instanceof Player) {
         Player var2 = (Player)var1.getTarget();
         if (Arenas.isInArena(var2)) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void Target3(PlayerInteractAtEntityEvent var1) {
      Player var2 = var1.getPlayer();
      if ((Arenas.isInArena(var2) || this.plugin.getConfig().getBoolean("bungee")) && (var1.getRightClicked().getType() == EntityType.PAINTING || var1.getRightClicked().getType() == EntityType.ARMOR_STAND || var1.getRightClicked().getType() == EntityType.ITEM_FRAME)) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void Target4(PlayerInteractEntityEvent var1) {
      Player var2 = var1.getPlayer();
      if ((Arenas.isInArena(var2) || this.plugin.getConfig().getBoolean("bungee")) && (var1.getRightClicked().getType() == EntityType.PAINTING || var1.getRightClicked().getType() == EntityType.ARMOR_STAND || var1.getRightClicked().getType() == EntityType.ITEM_FRAME)) {
         var1.setCancelled(true);
      }

   }
}
