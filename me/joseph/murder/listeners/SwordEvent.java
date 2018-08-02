package me.joseph.murder.listeners;

import me.joseph.murder.Arena;
import me.joseph.murder.Arenas;
import me.joseph.murder.GameState;
import me.joseph.murder.Main;
import me.joseph.murder.PlayerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class SwordEvent implements Listener {
   Main plugin;

   public SwordEvent(Main var1) {
      this.plugin = var1;
   }

   public void removesword(final Arena var1, final ArmorStand var2) {
      (new BukkitRunnable() {
         public void run() {
            if (var1.sword.contains(var2) && !var2.isDead() && var2 != null) {
               var1.sword.remove(var2);
               var2.remove();
            }

         }
      }).runTaskLater(this.plugin, (long)(20 * this.plugin.getConfig().getInt("remove-sword-after-time")));
   }

   @EventHandler
   public void Sword(final PlayerInteractEvent var1) {
      if (var1.getPlayer().getItemInHand() != null) {
         final Player var2 = var1.getPlayer();
         if (var1.getAction().name().toLowerCase().contains("right")) {
            if (var2.getItemInHand().getType() == Material.AIR) {
               return;
            }

            ItemStack var3;
            ItemMeta var4;
            String var5;
            if ((var3 = var2.getItemInHand()).hasItemMeta() && (var4 = var3.getItemMeta()).hasDisplayName() && (var5 = var4.getDisplayName()) != null && var5.equalsIgnoreCase(this.plugin.FormatText(this.plugin.settings.getConfig().getString("murderer-weapon.item-name"))) && Arenas.isInArena(var1.getPlayer())) {
               Arena var6 = Arenas.getArena(var1.getPlayer());
               if (var6.specs.contains(var1.getPlayer())) {
                  return;
               }

               if (var6.getState() != GameState.INGAME || !this.plugin.settings.getConfig().getBoolean("enable-sword-throw")) {
                  return;
               }

               if (var6.getType(var1.getPlayer()) == PlayerType.Murderer) {
                  if (this.plugin.cooldownTime.containsKey(var2.getName()) || this.plugin.cooldownTask.containsKey(var2.getName())) {
                     var1.getPlayer().sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("throw-cooldown")));
                     return;
                  }

                  if (!this.plugin.cooldownTime.containsKey(var2.getName())) {
                     this.plugin.cooldownTime.put(var2.getName(), this.plugin.settings.getConfig().getInt("throw-sword-cooldown"));
                     int var7 = 0;
                     if (this.plugin.cooldownTime.containsKey(var2.getName())) {
                        var7 = ((Integer)this.plugin.cooldownTime.get(var2.getName())).intValue();
                     }

                     StringBuilder var8 = new StringBuilder();

                     for(int var9 = 0; var9 < this.plugin.settings.getConfig().getInt("throw-sword-cooldown"); ++var9) {
                        if (var9 < var7) {
                           var8.append(this.plugin.FormatText(this.plugin.messages.getConfig().getString("progress-bar-1")));
                        } else {
                           var8.append(this.plugin.FormatText(this.plugin.messages.getConfig().getString("progress-bar-2")));
                        }
                     }

                     String var63 = var8.toString();
                     this.plugin.api.sendActionBar(var2, this.plugin.FormatText(this.plugin.messages.getConfig().getString("sword-actionbar-cooldown").replaceAll("%progress%", var63)));
                     this.plugin.cooldownTask.put(var2.getName(), new BukkitRunnable() {
                        public void run() {
                           if (!SwordEvent.this.plugin.cooldownTask.containsKey(var2.getName()) || !SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              this.cancel();
                           }

                           if (SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              SwordEvent.this.plugin.cooldownTime.put(var2.getName(), ((Integer)SwordEvent.this.plugin.cooldownTime.get(var2.getName())).intValue() - 1);
                           }

                           if (SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName()) && ((Integer)SwordEvent.this.plugin.cooldownTime.get(var2.getName())).intValue() <= 0) {
                              if (SwordEvent.this.plugin.cooldownTask.containsKey(var2.getName())) {
                                 SwordEvent.this.plugin.cooldownTask.remove(var2.getName());
                              }

                              if (SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                                 SwordEvent.this.plugin.cooldownTime.remove(var2.getName());
                              }

                              this.cancel();
                           }

                        }
                     });
                     if (this.plugin.cooldownTask.containsKey(var2.getName())) {
                        ((BukkitRunnable)this.plugin.cooldownTask.get(var2.getName())).runTaskTimer(this.plugin, 20L, 20L);
                     }

                     (new BukkitRunnable() {
                        public void run() {
                           if (!SwordEvent.this.plugin.cooldownTask.containsKey(var2.getName()) || !SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              this.cancel();
                           }

                           if (!var2.isOnline() && SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName()) && SwordEvent.this.plugin.cooldownTask.containsKey(var2.getName())) {
                              if (SwordEvent.this.plugin.cooldownTask.containsKey(var2.getName())) {
                                 SwordEvent.this.plugin.cooldownTask.remove(var2.getName());
                              }

                              if (SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                                 SwordEvent.this.plugin.cooldownTime.remove(var2.getName());
                              }

                              this.cancel();
                           }

                           if (SwordEvent.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              int var1 = ((Integer)SwordEvent.this.plugin.cooldownTime.get(var2.getName())).intValue();
                              StringBuilder var2x = new StringBuilder();

                              for(int var3 = 0; var3 < SwordEvent.this.plugin.settings.getConfig().getInt("throw-sword-cooldown"); ++var3) {
                                 if (var3 < var1 - 1) {
                                    var2x.append(SwordEvent.this.plugin.FormatText(SwordEvent.this.plugin.messages.getConfig().getString("progress-bar-1")));
                                 } else {
                                    var2x.append(SwordEvent.this.plugin.FormatText(SwordEvent.this.plugin.messages.getConfig().getString("progress-bar-2")));
                                 }
                              }

                              String var4 = var2x.toString();
                              SwordEvent.this.plugin.api.sendActionBar(var2, SwordEvent.this.plugin.FormatText(SwordEvent.this.plugin.messages.getConfig().getString("sword-actionbar-cooldown").replaceAll("%progress%", var4)));
                           } else {
                              this.cancel();
                              SwordEvent.this.plugin.api.sendActionBar(var2, SwordEvent.this.plugin.FormatText(SwordEvent.this.plugin.messages.getConfig().getString("sword-can-use-again")));
                           }

                        }
                     }).runTaskTimer(this.plugin, 10L, 10L);
                     Location var10 = var2.getLocation();
                     final ArmorStand var11 = (ArmorStand)var1.getPlayer().getWorld().spawn(var10.add(this.plugin.getLeftHeadDirection(var2).multiply(1)), ArmorStand.class);
                     this.removesword(var6, var11);
                     var11.setArms(true);
                     var11.setBasePlate(false);
                     var11.setVisible(false);
                     var11.setGravity(false);
                     var11.setRightArmPose(new EulerAngle(Math.toRadians((double)this.plugin.settings.getConfig().getInt("throwing-sword-angle-rotation")), Math.toRadians((double)(-var2.getLocation().getPitch())), Math.toRadians(90.0D)));
                     var11.setItemInHand(var1.getPlayer().getItemInHand());
                     var6.sword.add(var11);
                     Location var12 = var11.getLocation();
                     Location var13 = var11.getLocation().add(this.plugin.getRightHeadDirection(var11).multiply(1));
                     Location var14 = var11.getLocation().add(0.5D, (double)this.plugin.settings.getConfig().getInt("murder-sword-remove-height"), 0.5D);
                     final double var15 = var12.getX();
                     final double var17 = var12.getY();
                     final double var19 = var12.getZ();
                     double var21 = Math.toRadians((double)(var12.getYaw() + 90.0F));
                     double var23 = Math.toRadians((double)(var12.getPitch() + 90.0F));
                     final double var25 = Math.sin(var23) * Math.cos(var21);
                     final double var27 = Math.sin(var23) * Math.sin(var21);
                     final double var29 = Math.cos(var23);
                     final double var31 = var14.getX();
                     final double var33 = var14.getY();
                     final double var35 = var14.getZ();
                     double var37 = Math.toRadians((double)(var14.getYaw() + 90.0F));
                     double var39 = Math.toRadians((double)(var14.getPitch() + 90.0F));
                     final double var41 = Math.sin(var39) * Math.cos(var37);
                     final double var43 = Math.sin(var39) * Math.sin(var37);
                     final double var45 = Math.cos(var39);
                     final double var47 = var13.getX();
                     final double var49 = var13.getY();
                     final double var51 = var13.getZ();
                     double var53 = Math.toRadians((double)(var13.getYaw() + 90.0F));
                     double var55 = Math.toRadians((double)(var13.getPitch() + 90.0F));
                     final double var57 = Math.sin(var55) * Math.cos(var53);
                     final double var59 = Math.sin(var55) * Math.sin(var53);
                     final double var61 = Math.cos(var55);
                     this.plugin.Yaw.put(var2.getName(), var2.getLocation().getYaw());
                     this.plugin.Pitch.put(var2.getName(), var2.getLocation().getPitch());
                     (new BukkitRunnable() {
                        int a = 0;

                        public void run() {
                           Location var1x = new Location(var2.getWorld(), var15 + (double)this.a * var25, var17 + (double)this.a * var29, var19 + (double)this.a * var27);
                           Location var2x = new Location(var2.getWorld(), var31 + (double)this.a * var41, var33 + (double)this.a * var45, var35 + (double)this.a * var43);
                           Location var3 = new Location(var2.getWorld(), var47 + (double)this.a * var57, var49 + (double)this.a * var61, var51 + (double)this.a * var59);
                           Arena var4 = Arenas.getArena(var2);
                           if (!var11.isDead() && var11 != null) {
                              if (Arenas.isInArena(var2) && !var11.isDead() && var11 != null && var4.sword.contains(var11) && var4.getState() == GameState.INGAME && var11.getWorld().getName().equalsIgnoreCase(SwordEvent.this.plugin.getSpawn(var4, 0).getWorld().getName())) {
                                 var1x.setYaw(((Float)SwordEvent.this.plugin.Yaw.get(var2.getName())).floatValue());
                                 var1x.setPitch(((Float)SwordEvent.this.plugin.Pitch.get(var2.getName())).floatValue());
                                 Block var5 = var2x.getBlock();
                                 if (!SwordEvent.this.plugin.passable.contains(var5.getType())) {
                                    if (var4.sword.contains(var11) && var11 != null && !var11.isDead()) {
                                       var11.remove();
                                    }

                                    this.cancel();
                                 } else {
                                    Entity[] var9;
                                    int var8 = (var9 = SwordEvent.this.plugin.getNearbyEntities(var3, (int)SwordEvent.this.plugin.settings.getConfig().getDouble("throw-sword-damage-radius"))).length;

                                    for(int var7 = 0; var7 < var8; ++var7) {
                                       Entity var6 = var9[var7];
                                       if (var6 instanceof LivingEntity && !(var6 instanceof ArmorStand) && var6 instanceof Player) {
                                          Player var10 = (Player)var6;
                                          if (var10 != var1.getPlayer() && Arenas.isInArena(var10) && Arenas.getArena(var10).getType(var10) != PlayerType.Murderer && !Arenas.getArena(var10).specs.contains(var10)) {
                                             var10.damage(1000.0D);
                                             if (var4.sword.contains(var11) && var11 != null && !var11.isDead()) {
                                                var11.remove();
                                             }

                                             this.cancel();
                                             return;
                                          }
                                       }
                                    }

                                    var11.teleport(var1x);
                                    this.a += SwordEvent.this.plugin.settings.getConfig().getInt("sword-throw-speed");
                                 }
                              } else {
                                 this.cancel();
                              }
                           } else {
                              this.cancel();
                           }
                        }
                     }).runTaskTimer(this.plugin, 2L, 2L);
                  }
               }
            }
         }
      }

   }
}
