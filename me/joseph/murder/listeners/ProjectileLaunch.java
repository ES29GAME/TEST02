package me.joseph.murder.listeners;

import me.joseph.murder.Arenas;
import me.joseph.murder.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileLaunch implements Listener {
   Main plugin;

   public ProjectileLaunch(Main var1) {
      this.plugin = var1;
   }

   @EventHandler
   public void onMove(ProjectileLaunchEvent var1) {
      if (this.plugin.settings.getConfig().getBoolean("bow-delay") && var1.getEntity().getShooter() instanceof Player) {
         final Player var2 = (Player)var1.getEntity().getShooter();
         if (var1.getEntity() instanceof Arrow && Arenas.isInArena(var2)) {
            if (this.plugin.cooldownTime.containsKey(var2.getName()) || this.plugin.cooldownTask.containsKey(var2.getName())) {
               var2.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("bow-cooldown")));
               var1.setCancelled(true);
               return;
            }

            if (!this.plugin.cooldownTime.containsKey(var2.getName())) {
               this.plugin.cooldownTime.put(var2.getName(), this.plugin.settings.getConfig().getInt("bow-delay-seconds"));
               int var3 = 0;
               if (this.plugin.cooldownTime.containsKey(var2.getName())) {
                  var3 = ((Integer)this.plugin.cooldownTime.get(var2.getName())).intValue();
               }

               StringBuilder var4 = new StringBuilder();

               for(int var5 = 0; var5 < this.plugin.settings.getConfig().getInt("bow-delay-seconds"); ++var5) {
                  if (var5 < var3) {
                     var4.append(this.plugin.FormatText(this.plugin.messages.getConfig().getString("progress-bar-1")));
                  } else {
                     var4.append(this.plugin.FormatText(this.plugin.messages.getConfig().getString("progress-bar-2")));
                  }
               }

               String var6 = var4.toString();
               this.plugin.api.sendActionBar(var2, this.plugin.FormatText(this.plugin.messages.getConfig().getString("bow-actionbar-cooldown").replaceAll("%progress%", var6)));
               this.plugin.cooldownTask.put(var2.getName(), new BukkitRunnable() {
                  public void run() {
                     if (!ProjectileLaunch.this.plugin.cooldownTask.containsKey(var2.getName()) || !ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                        this.cancel();
                     }

                     if (ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                        ProjectileLaunch.this.plugin.cooldownTime.put(var2.getName(), ((Integer)ProjectileLaunch.this.plugin.cooldownTime.get(var2.getName())).intValue() - 1);
                     }

                     if (ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName()) && ((Integer)ProjectileLaunch.this.plugin.cooldownTime.get(var2.getName())).intValue() <= 0) {
                        if (ProjectileLaunch.this.plugin.cooldownTask.containsKey(var2.getName())) {
                           ProjectileLaunch.this.plugin.cooldownTask.remove(var2.getName());
                        }

                        if (ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                           ProjectileLaunch.this.plugin.cooldownTime.remove(var2.getName());
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
                     if (ProjectileLaunch.this.plugin.cooldownTask.containsKey(var2.getName()) && ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                        if (!var2.isOnline() && ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName()) && ProjectileLaunch.this.plugin.cooldownTask.containsKey(var2.getName())) {
                           if (ProjectileLaunch.this.plugin.cooldownTask.containsKey(var2.getName())) {
                              ProjectileLaunch.this.plugin.cooldownTask.remove(var2.getName());
                           }

                           if (ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              ProjectileLaunch.this.plugin.cooldownTime.remove(var2.getName());
                           }

                           this.cancel();
                        } else {
                           if (ProjectileLaunch.this.plugin.cooldownTime.containsKey(var2.getName())) {
                              int var1 = ((Integer)ProjectileLaunch.this.plugin.cooldownTime.get(var2.getName())).intValue();
                              StringBuilder var2x = new StringBuilder();

                              for(int var3 = 0; var3 < ProjectileLaunch.this.plugin.settings.getConfig().getInt("bow-delay-seconds"); ++var3) {
                                 if (var3 < var1 - 1) {
                                    var2x.append(ProjectileLaunch.this.plugin.FormatText(ProjectileLaunch.this.plugin.messages.getConfig().getString("progress-bar-1")));
                                 } else {
                                    var2x.append(ProjectileLaunch.this.plugin.FormatText(ProjectileLaunch.this.plugin.messages.getConfig().getString("progress-bar-2")));
                                 }
                              }

                              String var4 = var2x.toString();
                              ProjectileLaunch.this.plugin.api.sendActionBar(var2, ProjectileLaunch.this.plugin.FormatText(ProjectileLaunch.this.plugin.messages.getConfig().getString("bow-actionbar-cooldown").replaceAll("%progress%", var4)));
                           } else {
                              this.cancel();
                              ProjectileLaunch.this.plugin.api.sendActionBar(var2, ProjectileLaunch.this.plugin.FormatText(ProjectileLaunch.this.plugin.messages.getConfig().getString("bow-can-use-again")));
                           }

                        }
                     } else {
                        this.cancel();
                     }
                  }
               }).runTaskTimer(this.plugin, 10L, 10L);
            }
         }
      }

   }
}
