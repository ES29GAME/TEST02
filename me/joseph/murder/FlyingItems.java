package me.joseph.murder;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyingItems {
   String name;
   Player p;
   Location loc;
   ItemStack m;
   Main plugin;
   ArmorStand a;
   Item i;

   public FlyingItems(Player var1, String var2, ItemStack var3, Location var4, Main var5) {
      this.p = var1;
      this.name = var2;
      this.loc = var4;
      this.plugin = var5;
      this.m = var3;
      this.spawn();
   }

   public void remove() {
      (new BukkitRunnable() {
         public void run() {
            if (FlyingItems.this.a.getPassenger() != null && !FlyingItems.this.a.getPassenger().isDead()) {
               FlyingItems.this.a.getPassenger().remove();
            }

            if (!FlyingItems.this.a.isDead() && FlyingItems.this.a != null) {
               FlyingItems.this.a.remove();
            }

            if (!FlyingItems.this.i.isDead() && FlyingItems.this.i != null) {
               FlyingItems.this.i.remove();
            }

            FlyingItems.this.p = null;
            FlyingItems.this.loc = null;
            FlyingItems.this.m = null;
            FlyingItems.this.a = null;
            FlyingItems.this.i = null;
         }
      }).runTaskLater(this.plugin, 20L);
   }

   public void spawn() {
      if (this.a == null) {
         this.a = (ArmorStand)this.loc.getWorld().spawn(this.loc, ArmorStand.class);
         this.a.setGravity(false);
         this.a.setVisible(false);
         this.a.setArms(false);
         this.a.setBasePlate(false);
      }

      if (this.i == null) {
         this.i = this.loc.getWorld().dropItem(this.loc, this.m);
         this.i.setCustomName(this.name);
         this.i.setCustomNameVisible(true);
         this.i.setPickupDelay(Integer.MAX_VALUE);
         this.a.setPassenger(this.i);
      }

   }
}
