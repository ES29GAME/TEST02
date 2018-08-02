package me.joseph.murder;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import me.joseph.murder.api.MurderAPI;
import me.joseph.murder.configs.arenas;
import me.joseph.murder.configs.data;
import me.joseph.murder.configs.messages;
import me.joseph.murder.configs.potions;
import me.joseph.murder.configs.rewards;
import me.joseph.murder.configs.settings;
import me.joseph.murder.events.SignListener;
import me.joseph.murder.events.SignListener2;
import me.joseph.murder.listeners.BlockEvents;
import me.joseph.murder.listeners.ChatEvent;
import me.joseph.murder.listeners.ClickPotionEffectBlock;
import me.joseph.murder.listeners.DamageEvent;
import me.joseph.murder.listeners.DeathEvent;
import me.joseph.murder.listeners.DropItem;
import me.joseph.murder.listeners.EntityDamageByEntityEvent;
import me.joseph.murder.listeners.FoodLevel;
import me.joseph.murder.listeners.JoinEvent;
import me.joseph.murder.listeners.LeaveItem;
import me.joseph.murder.listeners.LoginEvent;
import me.joseph.murder.listeners.Motd;
import me.joseph.murder.listeners.NoPainting;
import me.joseph.murder.listeners.NoSpecDamage;
import me.joseph.murder.listeners.OpenVoteGUI;
import me.joseph.murder.listeners.PickUpEvent;
import me.joseph.murder.listeners.PotionConsumEvent;
import me.joseph.murder.listeners.ProjectileLaunch;
import me.joseph.murder.listeners.QuitEvent;
import me.joseph.murder.listeners.SpectateEvent;
import me.joseph.murder.listeners.SpectatorItem;
import me.joseph.murder.listeners.SwordEvent;
import me.joseph.murder.listeners.VoteEvent;
import me.joseph.murder.scoreboard.ScoreboardManager;
import me.joseph.murder.scoreboard.ScoreboardType;
import me.joseph.murder.sql.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {
   public static Main instance;
   public ArrayList datalist = new ArrayList();
   public HashMap pdata = new HashMap();
   public HashMap scoreboards = new HashMap();
   public HashMap scorestate = new HashMap();
   public HashMap cooldownTime;
   public HashMap cooldownTask;
   public MurderAPI api;
   public SignManager sm;
   public String user = "%%__USER__%%";
   public arenas arenas;
   public rewards rewards;
   public data data;
   public potions potions;
   public messages messages;
   public settings settings;
   public boolean works = true;
   public String nmsver;
   public boolean useOldMethods = false;
   public ArrayList bungeepl = new ArrayList();
   public ArrayList passable = new ArrayList();
   public SQLConnection sqlConnection = null;
   public ArrayList blocks = new ArrayList();
   public HashMap Pitch = new HashMap();
   public HashMap Yaw = new HashMap();
   private HashMap armourContents = new HashMap();
   private HashMap inventoryContents = new HashMap();
   private HashMap gamemode = new HashMap();
   private HashMap level = new HashMap();
   private HashMap xp = new HashMap();
   boolean startmap = false;
   public Arena bungee = null;
   public HashMap votes = new HashMap();
   public HashMap point = new HashMap();
   boolean disabled = false;

   public static Main getInstance() {
      return instance;
   }

   public static boolean isInt(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public void addGold(Player var1, Arena var2, int var3) {
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".World", var1.getWorld().getName());
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".x", var1.getLocation().getX());
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".y", var1.getLocation().getY());
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".z", var1.getLocation().getZ());
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".yaw", (double)var1.getLocation().getYaw());
      this.arenas.getConfig().set("Gold." + var2.getName() + "." + var3 + ".pitch", (double)var1.getLocation().getPitch());
      this.arenas.save();
      var1.sendMessage(this.messages.getConfig().getString("add-gold-message").replaceAll("&", "\u00a7"));
   }

   public void addPotion(Block var1, Arena var2) {
      List var3 = this.arenas.getConfig().getStringList("Potions." + var2.getName());
      if (!var3.contains(this.getStringFromLocation(var1.getLocation()))) {
         var3.add(this.getStringFromLocation(var1.getLocation()));
         this.arenas.getConfig().set("Potions." + var2.getName(), var3);
         this.arenas.save();
      }
   }

   public void addSpawn(Player var1, Arena var2, int var3) {
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".World", var1.getWorld().getName());
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".x", var1.getLocation().getX());
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".y", var1.getLocation().getY());
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".z", var1.getLocation().getZ());
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".yaw", (double)var1.getLocation().getYaw());
      this.arenas.getConfig().set("Spawns." + var2.getName() + "." + var3 + ".pitch", (double)var1.getLocation().getPitch());
      this.arenas.save();
      var1.sendMessage(this.messages.getConfig().getString("add-spawn-message").replaceAll("&", "\u00a7"));
   }

   public void canceltask(Integer var1, final FlyingItems var2, final Arena var3, final Block var4, final Player var5, final ItemStack var6) {
      (new BukkitRunnable() {
         public void run() {
            if (var3.items.contains(var2)) {
               if (var3.items.contains(var2)) {
                  var3.items.remove(var2);
               }

               if (Main.this.blocks.contains(var4)) {
                  Main.this.blocks.remove(var4);
               }

               var2.remove();
               var5.getInventory().addItem(new ItemStack[]{var6});
            }
         }
      }).runTaskLater(this, (long)(20 * var1.intValue()));
   }

   public String capitalizeFirstLetter(String var1) {
      return var1 != null && var1.length() != 0 ? var1.substring(0, 1).toUpperCase() + var1.substring(1) : var1;
   }

   public void createarena(String var1, Player var2) {
      List var3 = this.arenas.getConfig().getStringList("arena-list");
      if (var3.contains(var1)) {
         var2.sendMessage(this.messages.getConfig().getString("arena-already-exits").replaceAll("&", "\u00a7"));
      } else {
         var3.add(var1);
         this.arenas.getConfig().set("arena-list", var3);
         this.arenas.save();
         Arena var4 = new Arena(var1, this);
         Arenas.addArena(var4);
         var2.sendMessage(this.messages.getConfig().getString("arena-created-message").replaceAll("&", "\u00a7"));
      }
   }

   public void DropGold(Arena var1) {
      if (this.getRandomGold(var1) != null) {
         Location var2 = this.getRandomGold(var1);
         Entity[] var6;
         int var5 = (var6 = this.getNearbyEntities(var2, 2)).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            Entity var3 = var6[var4];
            if (!var3.getWorld().getName().equalsIgnoreCase(var2.getWorld().getName())) {
               return;
            }

            if (var3.getType() == EntityType.DROPPED_ITEM) {
               return;
            }

            if (var3.getType() == EntityType.PLAYER) {
               return;
            }
         }

         Item var7 = this.getSpawn(var1, 0).getWorld().dropItemNaturally(var2, new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("dropped-item-id")), 1));
         var7.setVelocity(new Vector(0, 0, 0));
         var1.golds.add(var7);
      }

   }

   public String FormatText(String var1) {
      return var1.replaceAll("&", "\u00a7");
   }

   public String formattominutes(int var1) {
      int var2 = var1 % 3600;
      int var3 = var2 / 60;
      int var4 = var2 % 60;
      return var3 + ":" + (var4 < 10 ? "0" : "") + var4;
   }

   public Color getColor(String var1) {
      if (var1.equalsIgnoreCase("AQUA")) {
         return Color.AQUA;
      } else if (var1.equalsIgnoreCase("BLACK")) {
         return Color.BLACK;
      } else if (var1.equalsIgnoreCase("BLUE")) {
         return Color.BLUE;
      } else if (var1.equalsIgnoreCase("FUCHSIA")) {
         return Color.FUCHSIA;
      } else if (var1.equalsIgnoreCase("GRAY")) {
         return Color.GRAY;
      } else if (var1.equalsIgnoreCase("GREEN")) {
         return Color.GREEN;
      } else if (var1.equalsIgnoreCase("LIME")) {
         return Color.LIME;
      } else if (var1.equalsIgnoreCase("MAROON")) {
         return Color.MAROON;
      } else if (var1.equalsIgnoreCase("NAVY")) {
         return Color.NAVY;
      } else if (var1.equalsIgnoreCase("OLIVE")) {
         return Color.OLIVE;
      } else if (var1.equalsIgnoreCase("ORANGE")) {
         return Color.ORANGE;
      } else if (var1.equalsIgnoreCase("PURPLE")) {
         return Color.PURPLE;
      } else if (var1.equalsIgnoreCase("RED")) {
         return Color.RED;
      } else if (var1.equalsIgnoreCase("SILVER")) {
         return Color.SILVER;
      } else if (var1.equalsIgnoreCase("TEAL")) {
         return Color.TEAL;
      } else if (var1.equalsIgnoreCase("WHITE")) {
         return Color.WHITE;
      } else {
         return var1.equalsIgnoreCase("YELLOW") ? Color.YELLOW : null;
      }
   }

   public int getDeaths(Player var1) {
      return this.getPlayerData(var1) != null ? this.getPlayerData(var1).getdeaths().intValue() : 0;
   }

   public Location getGold(Arena var1, int var2) {
      if (this.arenas.getConfig().contains("Gold." + var1.getName() + "." + var2)) {
         World var3 = Bukkit.getWorld(this.arenas.getConfig().getString("Gold." + var1.getName() + "." + var2 + ".World"));
         double var4 = this.arenas.getConfig().getDouble("Gold." + var1.getName() + "." + var2 + ".x");
         double var6 = this.arenas.getConfig().getDouble("Gold." + var1.getName() + "." + var2 + ".y");
         double var8 = this.arenas.getConfig().getDouble("Gold." + var1.getName() + "." + var2 + ".z");
         float var10 = (float)this.arenas.getConfig().getInt("Gold." + var1.getName() + "." + var2 + ".yaw");
         float var11 = (float)this.arenas.getConfig().getInt("Gold." + var1.getName() + "." + var2 + ".pitch");
         return new Location(var3, var4, var6, var8, var10, var11);
      } else {
         return this.getGold(var1, 0);
      }
   }

   public String getHighestVote() {
      String var1 = null;
      int var2 = 0;
      Iterator var4 = this.point.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var3 = (Entry)var4.next();
         if (((Integer)var3.getValue()).intValue() > var2) {
            var1 = (String)var3.getKey();
            var2 = ((Integer)var3.getValue()).intValue();
         }
      }

      return var1;
   }

   public int getKills(Player var1) {
      return this.getPlayerData(var1) != null ? this.getPlayerData(var1).getkill().intValue() : 0;
   }

   public int getScore(Player var1) {
      return this.getPlayerData(var1) != null ? this.getPlayerData(var1).getscore().intValue() : 0;
   }

   public Vector getLeftHeadDirection(ArmorStand var1) {
      Vector var2 = var1.getLocation().getDirection().normalize();
      return (new Vector(var2.getZ(), 0.0D, -var2.getX())).normalize();
   }

   public Vector getLeftHeadDirection(Player var1) {
      Vector var2 = var1.getLocation().getDirection().normalize();
      return (new Vector(var2.getZ(), 0.0D, -var2.getX())).normalize();
   }

   public Location getLobby() {
      World var1 = Bukkit.getWorld(this.arenas.getConfig().getString("Lobby.main.lobby.world"));
      double var2 = this.arenas.getConfig().getDouble("Lobby.main.lobby.x");
      double var4 = this.arenas.getConfig().getDouble("Lobby.main.lobby.y");
      double var6 = this.arenas.getConfig().getDouble("Lobby.main.lobby.z");
      float var8 = (float)this.arenas.getConfig().getInt("Lobby.main.lobby.yaw");
      float var9 = (float)this.arenas.getConfig().getInt("Lobby.main.lobby.pitch");
      return new Location(var1, var2, var4, var6, var8, var9);
   }

   public Location getLocationFromString(String var1) {
      if (var1 != null && var1.trim() != "") {
         String[] var2 = var1.split(":");
         if (var2.length == 4) {
            World var3 = Bukkit.getServer().getWorld(var2[0]);
            double var4 = Double.parseDouble(var2[1]);
            double var6 = Double.parseDouble(var2[2]);
            double var8 = Double.parseDouble(var2[3]);
            return new Location(var3, var4, var6, var8);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public int getLoses(Player var1) {
      return this.getPlayerData(var1) != null ? this.getPlayerData(var1).getloses().intValue() : 0;
   }

   public SQLConnection getMainSQLConnection() {
      return this.sqlConnection;
   }

   public Entity[] getNearbyEntities(Location var1, int var2) {
      int var3 = var2 < 16 ? 1 : (var2 - var2 % 16) / 16;
      HashSet var4 = new HashSet();

      for(int var5 = 0 - var3; var5 <= var3; ++var5) {
         for(int var6 = 0 - var3; var6 <= var3; ++var6) {
            int var7 = (int)var1.getX();
            int var8 = (int)var1.getY();
            int var9 = (int)var1.getZ();
            Entity[] var13;
            int var12 = (var13 = (new Location(var1.getWorld(), (double)(var7 + var5 * 16), (double)var8, (double)(var9 + var6 * 16))).getChunk().getEntities()).length;

            for(int var11 = 0; var11 < var12; ++var11) {
               Entity var10 = var13[var11];
               if (var1.getWorld().getName().equalsIgnoreCase(var10.getWorld().getName()) && var10.getLocation().distance(var1) <= (double)var2 && var10.getLocation().getBlock() != var1.getBlock()) {
                  var4.add(var10);
               }
            }
         }
      }

      return (Entity[])var4.toArray(new Entity[var4.size()]);
   }

   public Double getNearestDouble(Player var1, Double var2) {
      double var3 = Double.POSITIVE_INFINITY;
      Player var5 = null;
      Iterator var7 = var1.getNearbyEntities(var2.doubleValue(), var2.doubleValue(), var2.doubleValue()).iterator();

      while(var7.hasNext()) {
         Entity var6 = (Entity)var7.next();
         if (var6.getType() == EntityType.PLAYER && (Player)var6 != var1) {
            double var8 = var1.getLocation().distance(var6.getLocation());
            if (var8 <= var3) {
               var3 = var8;
               var5 = (Player)var6;
            }
         }
      }

      return var3;
   }

   public Player getNearestName(Player var1, Double var2) {
      double var3 = Double.POSITIVE_INFINITY;
      Player var5 = null;
      Iterator var7 = var1.getNearbyEntities(var2.doubleValue(), var2.doubleValue(), var2.doubleValue()).iterator();

      while(var7.hasNext()) {
         Entity var6 = (Entity)var7.next();
         if (var6.getType() == EntityType.PLAYER && (Player)var6 != var1) {
            double var8 = var1.getLocation().distance(var6.getLocation());
            if (var8 <= var3) {
               var3 = var8;
               var5 = (Player)var6;
            }
         }
      }

      return var5;
   }

   public PlayerData getPlayerData(Player var1) {
      if (this.pdata.containsKey(var1.getName())) {
         PlayerData var2 = (PlayerData)this.pdata.get(var1.getName());
         return var2;
      } else {
         return null;
      }
   }

   public PlayerData[] getPlayersData() {
      return (PlayerData[])this.datalist.toArray(new PlayerData[this.datalist.size()]);
   }

   public int getRandom(int var1, int var2) {
      Random var3 = new Random();
      return var3.nextInt(var2 - var1 + 1) + var1;
   }

   public Location getRandomGold(Arena var1) {
      if (this.GoldSize(var1) <= 0) {
         return null;
      } else {
         int var2 = this.getRandom(0, this.GoldSize(var1));
         return this.arenas.getConfig().contains("Spawns." + var1.getName() + "." + var2) ? this.getGold(var1, var2) : this.getGold(var1, 0);
      }
   }

   public Vector getRightHeadDirection(ArmorStand var1) {
      Vector var2 = var1.getLocation().getDirection().normalize();
      return (new Vector(-var2.getZ(), 0.0D, var2.getX())).normalize();
   }

   public Location getSpawn(Arena var1, int var2) {
      if (this.arenas.getConfig().contains("Spawns." + var1.getName() + "." + var2)) {
         World var3 = Bukkit.getWorld(this.arenas.getConfig().getString("Spawns." + var1.getName() + "." + var2 + ".World"));
         double var4 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".x");
         double var6 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".y");
         double var8 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".z");
         float var10 = (float)this.arenas.getConfig().getInt("Spawns." + var1.getName() + "." + var2 + ".yaw");
         float var11 = (float)this.arenas.getConfig().getInt("Spawns." + var1.getName() + "." + var2 + ".pitch");
         return new Location(var3, var4, var6, var8, var10, var11);
      } else {
         return this.getSpawn(var1, 0);
      }
   }

   public Location getSpawn2(Arena var1, int var2) {
      if (this.arenas.getConfig().contains("Spawns." + var1.getName() + "." + var2)) {
         World var3 = Bukkit.getWorld(this.arenas.getConfig().getString("Spawns." + var1.getName() + "." + var2 + ".World"));
         double var4 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".x");
         double var6 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".y");
         double var8 = this.arenas.getConfig().getDouble("Spawns." + var1.getName() + "." + var2 + ".z");
         float var10 = (float)this.arenas.getConfig().getInt("Spawns." + var1.getName() + "." + var2 + ".yaw");
         float var11 = (float)this.arenas.getConfig().getInt("Spawns." + var1.getName() + "." + var2 + ".pitch");
         return new Location(var3, var4, var6, var8, var10, var11);
      } else {
         return this.getSpawn(var1, 0);
      }
   }

   public Location getSpec(Arena var1) {
      World var2 = Bukkit.getWorld(this.arenas.getConfig().getString("Spectator." + var1.getName() + ".main.lobby.world"));
      double var3 = this.arenas.getConfig().getDouble("Spectator." + var1.getName() + ".main.lobby.x");
      double var5 = this.arenas.getConfig().getDouble("Spectator." + var1.getName() + ".main.lobby.y");
      double var7 = this.arenas.getConfig().getDouble("Spectator." + var1.getName() + ".main.lobby.z");
      float var9 = (float)this.arenas.getConfig().getInt("Spectator." + var1.getName() + ".main.lobby.yaw");
      float var10 = (float)this.arenas.getConfig().getInt("Spectator." + var1.getName() + ".main.lobby.pitch");
      return new Location(var2, var3, var5, var7, var9, var10);
   }

   public String getStringFromLocation(Location var1) {
      return var1 == null ? "" : var1.getWorld().getName() + ":" + var1.getX() + ":" + var1.getY() + ":" + var1.getZ();
   }

   public Location getWait(Arena var1) {
      World var2 = Bukkit.getWorld(this.arenas.getConfig().getString("Wait." + var1.getName() + ".main.lobby.world"));
      double var3 = this.arenas.getConfig().getDouble("Wait." + var1.getName() + ".main.lobby.x");
      double var5 = this.arenas.getConfig().getDouble("Wait." + var1.getName() + ".main.lobby.y");
      double var7 = this.arenas.getConfig().getDouble("Wait." + var1.getName() + ".main.lobby.z");
      float var9 = (float)this.arenas.getConfig().getInt("Wait." + var1.getName() + ".main.lobby.yaw");
      float var10 = (float)this.arenas.getConfig().getInt("Wait." + var1.getName() + ".main.lobby.pitch");
      return new Location(var2, var3, var5, var7, var9, var10);
   }

   public int getWins(Player var1) {
      return this.getPlayerData(var1) != null ? this.getPlayerData(var1).getwins().intValue() : 0;
   }

   public int GoldSize(Arena var1) {
      if (!this.arenas.getConfig().contains("Gold." + var1.getName())) {
         return 0;
      } else {
         return !this.arenas.getConfig().contains("Gold." + var1.getName() + ".0") ? 0 : this.arenas.getConfig().getConfigurationSection("Gold." + var1.getName()).getKeys(false).size();
      }
   }

   public boolean hasPotion(Block var1, Arena var2) {
      List var3 = this.arenas.getConfig().getStringList("Potions." + var2.getName());
      return var3.contains(this.getStringFromLocation(var1.getLocation()));
   }

   public void LaunchFirework(Location var1) {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      List var4 = this.getConfig().getStringList("firework.colors");
      List var5 = this.getConfig().getStringList("firework.fade");
      Iterator var7 = var4.iterator();

      String var6;
      while(var7.hasNext()) {
         var6 = (String)var7.next();
         var2.add(this.getColor(var6));
      }

      var7 = var5.iterator();

      while(var7.hasNext()) {
         var6 = (String)var7.next();
         var3.add(this.getColor(var6));
      }

      Firework var8 = (Firework)var1.getWorld().spawn(var1.add(0.5D, (double)this.getConfig().getInt("firework.height"), 0.5D), Firework.class);
      FireworkMeta var9 = var8.getFireworkMeta();
      var9.addEffect(FireworkEffect.builder().flicker(this.getConfig().getBoolean("firework.flicker")).trail(this.getConfig().getBoolean("firework.trail")).with(Type.valueOf(this.getConfig().getString("firework.type"))).withColor(var2).withFade(var3).build());
      var9.setPower(this.getConfig().getInt("firework.power"));
      var8.setFireworkMeta(var9);
   }

   public void leave2(Player var1) {
      if (Arenas.isInArena(var1)) {
         Arena var2 = Arenas.getArena(var1);
         if (!var2.specs.contains(var1)) {
            var2.removePlayer(var1, "leave");
         }

         if (var2.specs.contains(var1)) {
            this.setup(var1);
            var2.specs.remove(var1);
            this.restoreInventory(var1);
            Arenas.removeArena(var1);
            if (!this.getConfig().getBoolean("send-to-server-on-leave")) {
               var1.teleport(this.getLobby());
            }

            if (this.getConfig().getBoolean("send-to-server-on-leave")) {
               ByteArrayDataOutput var3 = ByteStreams.newDataOutput();
               var3.writeUTF("Connect");
               var3.writeUTF(this.getConfig().getString("lobby-server"));
               var1.sendPluginMessage(this, "BungeeCord", var3.toByteArray());
            }

            var1.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
         }

      }
   }

   public void loadarenas() {
      if (this.arenas.getConfig().contains("arena-list")) {
         Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading arenas..");
         Iterator var2 = this.arenas.getConfig().getStringList("arena-list").iterator();

         while(var2.hasNext()) {
            String var1 = (String)var2.next();
            Arena var3 = new Arena(var1, this);
            Arenas.addArena(var3);
         }

         Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Arenas has been loaded!");
      }

   }

   public void LoadConfigFiles() {
      Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading config files..");
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      this.getConfig().addDefault("do-not-edit", false);
      this.getConfig().addDefault("bungee", false);
      this.getConfig().addDefault("send-to-server-on-leave", false);
      this.getConfig().addDefault("update-data-on-game-end", false);
      this.getConfig().addDefault("update-data-on-server-stop", true);
      this.getConfig().addDefault("update-data-on-player-quit", false);
      this.getConfig().addDefault("mysql", false);
      this.getConfig().addDefault("lobby-server", "lobby-server");
      this.getConfig().addDefault("host", "localhost");
      this.getConfig().addDefault("port", "3306");
      this.getConfig().addDefault("database", "testdb");
      this.getConfig().addDefault("username", "root");
      this.getConfig().addDefault("password", "");
      this.getConfig().addDefault("remove-sword-after-time", Integer.valueOf(5));
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      this.arenas = new arenas(new File(this.getDataFolder() + "/arenas/data.yml"));
      this.arenas.save();
      this.arenas.getConfig().options().copyDefaults(true);
      this.arenas.save();
      this.rewards = new rewards(new File(this.getDataFolder() + "/rewards/data.yml"));
      this.rewards.save();
      this.rewards.getConfig().options().copyDefaults(true);
      List var1 = this.rewards.getConfig().getStringList("test.win-rewards.0");
      var1.add("say %player% test reward");
      var1.add("say %player% test reward2");
      this.rewards.getConfig().addDefault("test.win-rewards.0", var1);
      List var2 = this.rewards.getConfig().getStringList("test.lose-rewards.0");
      var2.add("say %player% test reward");
      var2.add("say %player% test reward2");
      this.rewards.getConfig().addDefault("test.lose-rewards.0", var2);
      List var3 = this.rewards.getConfig().getStringList("test.hero-rewards.0");
      var3.add("say %player% test reward");
      var3.add("say %player% test reward2");
      this.rewards.getConfig().addDefault("test.hero-rewards.0", var3);
      this.rewards.save();
      this.data = new data(new File(this.getDataFolder() + "/data/data.yml"));
      this.data.save();
      this.data.getConfig().options().copyDefaults(true);
      this.data.save();
      this.potions = new potions(new File(this.getDataFolder() + "/potions/data.yml"));
      this.potions.save();
      this.potions.getConfig().addDefault("Potions.Speed-Potion.type", "SPEED");
      this.potions.getConfig().addDefault("Potions.Speed-Potion.time", Integer.valueOf(10));
      this.potions.getConfig().addDefault("Potions.Speed-Potion.name", "&bSpeed");
      this.potions.getConfig().addDefault("Potions.Speed-Potion.level", Integer.valueOf(1));
      this.potions.getConfig().options().copyDefaults(true);
      this.potions.save();
      this.settings = new settings(new File(this.getDataFolder() + "/settings/data.yml"));
      this.settings.save();
      this.settings.getConfig().addDefault("rejoin-interval", Integer.valueOf(3));
      this.settings.getConfig().addDefault("murder-sword-remove-height", Integer.valueOf(2));
      this.settings.getConfig().addDefault("throwing-sword-angle-rotation", Integer.valueOf(351));
      this.settings.getConfig().addDefault("death-messages", true);
      List var4 = this.settings.getConfig().getStringList("whitelisted-commands");
      var4.add("/test command");
      this.settings.getConfig().addDefault("whitelisted-commands", var4);
      List var5 = this.settings.getConfig().getStringList("stats-board-world-whitelist");
      var5.add("world");
      this.settings.getConfig().addDefault("stats-board-world-whitelist", var5);
      List var6 = this.settings.getConfig().getStringList("commands-world-whitelist");
      var6.add("world");
      this.settings.getConfig().addDefault("commands-world-whitelist", var6);
      this.settings.getConfig().addDefault("commands-whitelist", false);
      this.settings.getConfig().addDefault("board-whitelist", false);
      this.settings.getConfig().addDefault("arenas-inventory-size", Integer.valueOf(18));
      this.settings.getConfig().addDefault("arenas-refresh-item-id", Integer.valueOf(381));
      this.settings.getConfig().addDefault("arenas-refresh-item-data", Integer.valueOf(0));
      this.settings.getConfig().addDefault("arenas-refresh-item-name", "&f&lREFRESH");
      this.settings.getConfig().addDefault("arenas-inventory-title", "Arena List");
      this.settings.getConfig().addDefault("arenas-lobby-state-item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("arenas-lobby-state-item-data", Integer.valueOf(10));
      this.settings.getConfig().addDefault("arenas-lobby-state-item-name", "&a%arena%");
      ArrayList var7 = new ArrayList();
      var7.add("&bPlayers: &e%players%/%max%");
      var7.add("&bState: &e%state%");
      var7.add("&bMap: &e%map%");
      this.settings.getConfig().addDefault("arenas-lobby-state-item-lore", var7);
      this.settings.getConfig().addDefault("arenas-starting-state-item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("arenas-starting-state-item-data", Integer.valueOf(9));
      this.settings.getConfig().addDefault("arenas-starting-state-item-name", "&6%arena%");
      ArrayList var8 = new ArrayList();
      var8.add("&bPlayers: &e%players%/%max%");
      var8.add("&bState: &e%state%");
      var8.add("&bMap: &e%map%");
      this.settings.getConfig().addDefault("arenas-starting-state-item-lore", var8);
      this.settings.getConfig().addDefault("arenas-ingame-state-item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("arenas-ingame-state-item-data", Integer.valueOf(8));
      this.settings.getConfig().addDefault("arenas-ingame-state-item-name", "&c%arena%");
      ArrayList var9 = new ArrayList();
      var9.add("&ePlayers: &7%players%/%max%");
      var9.add("&eState: &7%state%");
      var9.add("&eMap: &7%map%");
      this.settings.getConfig().addDefault("arenas-ingame-state-item-lore", var9);
      this.settings.getConfig().addDefault("stats-board", true);
      this.settings.getConfig().addDefault("no-fall-damage", true);
      this.settings.getConfig().addDefault("tp-lobby-on-join", false);
      this.settings.getConfig().addDefault("per-arena-chat", true);
      this.settings.getConfig().addDefault("send-stats-message-on-leave", true);
      this.settings.getConfig().addDefault("win-rewards", false);
      this.settings.getConfig().addDefault("hero-rewards", false);
      this.settings.getConfig().addDefault("lose-rewards", false);
      this.settings.getConfig().addDefault("start-fireworks-on-players-location", true);
      this.settings.getConfig().addDefault("show-potion-name-in-floating-item", true);
      this.settings.getConfig().addDefault("score-on-kill", Integer.valueOf(10));
      this.settings.getConfig().addDefault("score-on-gold", Integer.valueOf(5));
      this.settings.getConfig().addDefault("time-until-game-start", Integer.valueOf(5));
      this.settings.getConfig().addDefault("give-spectate-item-after-ticks", Integer.valueOf(5));
      this.settings.getConfig().addDefault("stop-arena-after-win-time", Integer.valueOf(5));
      this.settings.getConfig().addDefault("fireworks-ticks", Integer.valueOf(5));
      this.settings.getConfig().addDefault("fireworks-time-in-ticks", Integer.valueOf(10));
      this.settings.getConfig().addDefault("vote-time", Integer.valueOf(15));
      this.settings.getConfig().addDefault("vote-inventory.size", Integer.valueOf(36));
      this.settings.getConfig().addDefault("vote-inventory.name", "Voting");
      this.settings.getConfig().addDefault("map-displayname-in-gui-color", "GREEN");
      this.settings.getConfig().addDefault("map-item-lore", "&eVotes: &b%votes%");
      this.settings.getConfig().addDefault("vote-message", "&a[Murder] &eYou have voted for &b%map% &emap!");
      this.settings.getConfig().addDefault("bow-delay", true);
      this.settings.getConfig().addDefault("bow-delay-seconds", Integer.valueOf(3));
      this.settings.getConfig().addDefault("bow-pickup-radius", Integer.valueOf(2));
      this.settings.getConfig().addDefault("map.item-id", Integer.valueOf(339));
      this.settings.getConfig().addDefault("map.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("map.item-name", "&d&lMAP SELECTOR");
      this.settings.getConfig().addDefault("map.item-lore", "&fClick here to select a map");
      this.settings.getConfig().addDefault("tracking-compass", true);
      this.settings.getConfig().addDefault("time-to-give-tracker", Integer.valueOf(60));
      this.settings.getConfig().addDefault("murder-track.item-id", Integer.valueOf(345));
      this.settings.getConfig().addDefault("murder-track.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("murder-track.item-name", "&b&lPLAYER TRACKER");
      this.settings.getConfig().addDefault("murder-track.item-lore", "&fTracks players");
      this.settings.getConfig().addDefault("murder-track.range", Integer.valueOf(100));
      this.settings.getConfig().addDefault("track.item-id", Integer.valueOf(345));
      this.settings.getConfig().addDefault("track.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("track.item-name", "&a&lBOW TRACKER");
      this.settings.getConfig().addDefault("track.item-lore", "&fTrack bow location");
      this.settings.getConfig().addDefault("quit.item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("quit.item-subid", Integer.valueOf(1));
      this.settings.getConfig().addDefault("quit.item-name", "&c&lQUIT");
      this.settings.getConfig().addDefault("quit.item-lore", "&fClick here to leave from the arena!");
      this.settings.getConfig().addDefault("quit2.item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("quit2.item-subid", Integer.valueOf(1));
      this.settings.getConfig().addDefault("quit2.item-name", "&c&lQUIT");
      this.settings.getConfig().addDefault("quit2.item-lore", "&fClick here to leave from the lobby!");
      this.settings.getConfig().addDefault("quit3.item-id", Integer.valueOf(351));
      this.settings.getConfig().addDefault("quit3.item-subid", Integer.valueOf(1));
      this.settings.getConfig().addDefault("quit3.item-name", "&c&lQUIT");
      this.settings.getConfig().addDefault("quit3.item-lore", "&fClick here to leave from the server!");
      this.settings.getConfig().addDefault("spectate-inventory-size", Integer.valueOf(18));
      this.settings.getConfig().addDefault("spectate-inventory-title", "Spectate Players");
      this.settings.getConfig().addDefault("Spectate-Display-Name-Color", "RED");
      this.settings.getConfig().addDefault("Spectate-Display-Lore", "&fClick to teleport to that player!");
      this.settings.getConfig().addDefault("spec.item-name", "&a&lSPECTATE");
      this.settings.getConfig().addDefault("spec.item-id", Integer.valueOf(345));
      this.settings.getConfig().addDefault("spec.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("rejoin.item-name", "&b&lREJOIN");
      this.settings.getConfig().addDefault("rejoin.item-id", Integer.valueOf(339));
      this.settings.getConfig().addDefault("rejoin.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("enable-sword-throw", true);
      this.settings.getConfig().addDefault("throw-sword-damage-radius", Integer.valueOf(1));
      this.settings.getConfig().addDefault("throw-sword-cooldown", Integer.valueOf(3));
      this.settings.getConfig().addDefault("sword-throw-speed", Integer.valueOf(1));
      this.settings.getConfig().addDefault("receive-sword-after", Integer.valueOf(10));
      this.settings.getConfig().addDefault("murderer-weapon.item-id", Integer.valueOf(267));
      this.settings.getConfig().addDefault("murderer-weapon.item-subid", Integer.valueOf(0));
      this.settings.getConfig().addDefault("murderer-weapon.item-name", "&c&lKNIFE");
      this.settings.getConfig().addDefault("murderer-weapon.item-lore", "&fKill em all");
      this.settings.getConfig().addDefault("min-players-to-start-bungee", Integer.valueOf(3));
      this.settings.getConfig().addDefault("countdown", Integer.valueOf(15));
      this.settings.getConfig().addDefault("gold-drop-interval", Integer.valueOf(20));
      this.settings.getConfig().addDefault("gold-amount-to-get-bow", Integer.valueOf(10));
      this.settings.getConfig().addDefault("gold-amount-to-get-potion", Integer.valueOf(2));
      this.settings.getConfig().addDefault("dropped-item-id", Integer.valueOf(266));
      this.settings.getConfig().addDefault("enable-sounds", false);
      this.settings.getConfig().addDefault("COUNT_DOWN_SOUND", "CLICK");
      this.settings.getConfig().addDefault("KILL_SOUND", "ORB_PICKUP");
      this.settings.getConfig().addDefault("PICK_UP", "CHICKEN_EGG_POP");
      this.settings.getConfig().addDefault("scoreboard-update-interval", Integer.valueOf(10));
      this.settings.getConfig().options().copyDefaults(true);
      this.settings.save();
      this.messages = new messages(new File(this.getDataFolder() + "/messages/data.yml"));
      this.messages.save();
      this.messages.getConfig().addDefault("potion-title", "&b%type% Potion!");
      this.messages.getConfig().addDefault("potion-subtitle", "&eFor %time% seconds!");
      this.messages.getConfig().addDefault("progress-bar-1", "&a\u2586");
      this.messages.getConfig().addDefault("progress-bar-2", "&c\u2586");
      this.messages.getConfig().addDefault("sword-actionbar-cooldown", "&a&lCooldown: &f[%progress%&f]");
      this.messages.getConfig().addDefault("sword-can-use-again", "&a&lYou can use your throwing sword again!");
      this.messages.getConfig().addDefault("throw-cooldown", "&7[&cMurder&7]: &7Wait for sword cooldown!");
      this.messages.getConfig().addDefault("bow-cooldown", "&7[&cMurder&7]: &7Wait for bow cooldown!");
      this.messages.getConfig().addDefault("bow-actionbar-cooldown", "&a&lCooldown: &f[%progress%&f]");
      this.messages.getConfig().addDefault("bow-can-use-again", "&a&lYou can use your bow again!");
      this.messages.getConfig().addDefault("stats-reset-error", "&7[&cMurder&7]: &7Could not reset stats, play games then you will be able to reset stats!");
      this.messages.getConfig().addDefault("stats-reset-message", "&7[&cMurder&7]: &eStats was reset successfully!");
      this.messages.getConfig().addDefault("bow-dropped", "&cThe bow is dropped");
      this.messages.getConfig().addDefault("bow-not-dropped", "&aThe bow is not dropped");
      this.messages.getConfig().addDefault("kick-message", "&cYou left the game.");
      this.messages.getConfig().addDefault("vote-scoreboard-title", "&aMap Voting");
      this.messages.getConfig().addDefault("scoreboard-map", "&e%map% &b(%votes%)");
      this.messages.getConfig().addDefault("voting-time-started", "&7[&cMurder&7]: &eVoting time has been started, click the item in your inventory and choose your favourite map!");
      this.messages.getConfig().addDefault("vote-win", "&7[&cMurder&7]: &eMap &b%map% &ehas won the voting starting game soon...");
      this.messages.getConfig().addDefault("vote-error-perm", "&7[&cMurder&7]: &7You don't have enough permissions to vote!");
      this.messages.getConfig().addDefault("vote-error", "&7[&cMurder&7]: &7The arena has been selected you are not allowed to vote anymore!");
      this.messages.getConfig().addDefault("gold-reason", "Pick Up Gold");
      this.messages.getConfig().addDefault("kill-reason", "Killed A Player");
      this.messages.getConfig().addDefault("receive-score-message", "&6+&e%score% &6scores (%reason%)!");
      this.messages.getConfig().addDefault("spec-chat-prefix", "&eSPEC: &f");
      this.messages.getConfig().addDefault("murder-role", "Murderer");
      this.messages.getConfig().addDefault("detective-role", "Detective");
      this.messages.getConfig().addDefault("innocent-role", "Innocent");
      this.messages.getConfig().addDefault("dead-role", "Dead");
      this.messages.getConfig().addDefault("death-title", "&cYou died");
      this.messages.getConfig().addDefault("death-subtitle", "&eKilled by: &c%killer%");
      this.messages.getConfig().addDefault("murder-receive-sword-message", "&7[&cMurder&7]: &7The murderer has received his sword!");
      this.messages.getConfig().addDefault("bow-location-message", "&eBow location is on &b(%loc%) &efeet!");
      this.messages.getConfig().addDefault("near-player-location-message", "&bNearest Player &f%player% &bdistance &e(%distance%) &bfeet!");
      this.messages.getConfig().addDefault("title-countdown", "&cStarting game in");
      this.messages.getConfig().addDefault("subtitle-countdown", "&e%time% &eseconds!");
      this.messages.getConfig().addDefault("you-are-murderer-title", "&cYou are the murderer!");
      this.messages.getConfig().addDefault("you-are-murderer-subtitle", "&fKill all the players!");
      this.messages.getConfig().addDefault("you-are-detective-title", "&bYou are the detective!");
      this.messages.getConfig().addDefault("you-are-detective-subtitle", "&fFind the murderer and kill him!");
      this.messages.getConfig().addDefault("you-have-bow-title", "&aYou have got a bow!");
      this.messages.getConfig().addDefault("you-have-bow-subtitle", "&fYou have +1 shot to kill the murderer!");
      this.messages.getConfig().addDefault("you-are-innocent-title", "&aYou are an innocent!");
      this.messages.getConfig().addDefault("you-are-innocent-subtitle", "&fCollect gold to have a chance to kill the murderer!");
      this.messages.getConfig().addDefault("countdown", "&7[&cMurder&7]: &eThe game will starts in %time% seconds!");
      this.messages.getConfig().addDefault("join-error", "&7[&cMurder&7]: &7You can't join that arena because its already started!");
      this.messages.getConfig().addDefault("already-in-arena", "&7[&cMurder&7]: &7You are already in an arena!");
      this.messages.getConfig().addDefault("arena-full", "&7[&cMurder&7]: &7That arena is full!");
      this.messages.getConfig().addDefault("no-arenas", "&7[&cMurder&7]: &7No arenas available at this time :/!");
      this.messages.getConfig().addDefault("player-join-arena-message", "&7[&cMurder&7]: &e%player% &7has joined the game &b(%min%/%max%)&e!");
      this.messages.getConfig().addDefault("player-leave-arena-message", "&7[&cMurder&7]: &7%player% &ehas left the game&7!");
      this.messages.getConfig().addDefault("ingame-motd", "&cIngame");
      this.messages.getConfig().addDefault("lobby-motd", "&aLobby");
      this.messages.getConfig().addDefault("starting-motd", "&cStarting");
      this.messages.getConfig().addDefault("voting-motd", "&dVoting");
      this.messages.getConfig().addDefault("game-soon-start-message", "&7[&cMurder&7]: &eThe game will starts soon!");
      this.messages.getConfig().addDefault("cancel", "&7[&cMurder&7]: &7Starting has been cancelled due not enough players!");
      this.messages.getConfig().addDefault("game-start-message", "&7[&cMurder&7]: &eThe game has been started!");
      this.messages.getConfig().addDefault("not-in-arena", "&7[&cMurder&7]: &7You are not in arena!");
      this.messages.getConfig().addDefault("spectate-message", "&7[&cMurder&7]: &eYou are a spectator now!");
      this.messages.getConfig().addDefault("lobby-set-message", "&7[&cMurder&7]: &eLobby has been set!");
      this.messages.getConfig().addDefault("death-message", "&7[&cMurder&7]: &7%player% &ehas died!");
      this.messages.getConfig().addDefault("detective-die", "&7[&cMurder&7]: &eThe detective has died pick up his bow!");
      this.messages.getConfig().addDefault("arena-created-message", "&7[&cMurder&7]: &eArena has been created!");
      this.messages.getConfig().addDefault("arena-already-exits", "&7[&cMurder&7]: &eArena already exits!");
      this.messages.getConfig().addDefault("arena-remove-message", "&7[&cMurder&7]: &eArena has been created!");
      this.messages.getConfig().addDefault("arena-not-exits", "&7[&cMurder&7]: &7Arena not exits!");
      this.messages.getConfig().addDefault("wait-lobby-set-message", "&7[&cMurder&7]: &eWait lobby has been set!");
      this.messages.getConfig().addDefault("add-potion", "&7[&cMurder&7]: &eAdded mystery potion block!");
      this.messages.getConfig().addDefault("remove-potion", "&7[&cMurder&7]: &eRemoved mystery potion block!");
      this.messages.getConfig().addDefault("potion-use-error", "&7[&cMurder&7]: &7MysteryPotion is in use already!");
      this.messages.getConfig().addDefault("potion-use-error-2", "&7[&cMurder&7]: &7You don't have enough gold to use mystery potions!");
      this.messages.getConfig().addDefault("potion-use-message", "&7[&cMurder&7]: &eYou have used a mystery potions!");
      this.messages.getConfig().addDefault("spectate-set-message", "&7[&cMurder&7]: &eSpectator position has been set!");
      this.messages.getConfig().addDefault("not-online", "&7[&cMurder&7]: &7That player is not online!");
      this.messages.getConfig().addDefault("add-spawn-message", "&7[&cMurder&7]: &ePlayer spawn added!");
      this.messages.getConfig().addDefault("add-gold-message", "&7[&cMurder&7]: &eGold spawn added!");
      this.messages.getConfig().addDefault("lobby-scoreboard-title", "&c&lMurder Mystery");
      this.messages.getConfig().addDefault("wait-scoreboard-title", "&c&lMurder Mystery");
      this.messages.getConfig().addDefault("countdown-scoreboard-title", "&c&lMurder Mystery");
      this.messages.getConfig().addDefault("ingame-scoreboard-title", "&c&lMurder Mystery");
      this.messages.getConfig().addDefault("stats-scoreboard-title", "&aMurder Stats");
      this.messages.getConfig().addDefault("sign-header", "&aMurder");
      this.messages.getConfig().addDefault("sign-ingame", "&eIngame");
      this.messages.getConfig().addDefault("sign-lobby", "&eLobby");
      this.messages.getConfig().addDefault("sign-starting", "&eStarting");
      this.messages.getConfig().addDefault("sign-arena", "&e%arena%");
      this.messages.getConfig().addDefault("players", "&emin/max");
      this.messages.getConfig().addDefault("auto-join-sign-line-1", "&aMurder");
      this.messages.getConfig().addDefault("auto-join-sign-line-2", "&7Click to join");
      this.messages.getConfig().addDefault("auto-join-sign-line-3", "&7Random Arena");
      this.messages.getConfig().addDefault("auto-join-sign-line-4", "");
      this.messages.getConfig().addDefault("pickup-bow", "&7[&cMurder&7]: &eSomeone has picked up the bow!");
      List var10 = this.messages.getConfig().getStringList("murder-help-message");
      var10.add("&f/murder &7| &eMain command");
      var10.add("&f/murder join <arena> &7| &eTo join an arena");
      var10.add("&f/murder leave &7| &eTo leave a game");
      var10.add("&f/murder stats &7| &eTo view your stats");
      var10.add("&f/murder stats <player> &7| &eTo see other players stats");
      var10.add("&f/murder randomjoin &7| &eTo join random arena");
      var10.add("&f/murder reset &7| &eTo reset your stats");
      var10.add("&f/murder admin &7| &eAdmin commands");
      this.messages.getConfig().addDefault("murder-help-message", var10);
      List var11 = this.messages.getConfig().getStringList("self-info-message");
      var11.add("&aYour Game Stats");
      var11.add("&b&m------------------");
      var11.add("&aName: &e%player%");
      var11.add("&aWins: &e%wins%");
      var11.add("&aLoses: &e%loses%");
      var11.add("&aKills: &e%kills%");
      var11.add("&aDeaths: &e%deaths%");
      var11.add("&aTotal score: &e%score%");
      var11.add("&b&m------------------");
      this.messages.getConfig().addDefault("self-info-message", var11);
      List var12 = this.messages.getConfig().getStringList("other-info-message");
      var12.add("&a%player% Game Stats");
      var12.add("&b&m------------------");
      var12.add("&aName: &e%player%");
      var12.add("&aWins: &e%wins%");
      var12.add("&aLoses: &e%loses%");
      var12.add("&aKills: &e%kills%");
      var12.add("&aDeaths: &e%deaths%");
      var12.add("&aTotal score: &e%score%");
      var12.add("&b&m------------------");
      this.messages.getConfig().addDefault("other-info-message", var12);
      List var13 = this.messages.getConfig().getStringList("wait-scoreboard-lines");
      var13.add("&r");
      var13.add("&cPlayers: &7%size%/%max%");
      var13.add("&r&r");
      var13.add("&cWaiting for players");
      var13.add("&r&r&r");
      var13.add("&cwww.server.net");
      this.messages.getConfig().addDefault("wait-scoreboard-lines", var13);
      List var14 = this.messages.getConfig().getStringList("countdown-scoreboard-lines");
      var14.add("&r");
      var14.add("&cPlayers: &7%size%/%max%");
      var14.add("&r&r");
      var14.add("&cStarting: &7%countdown%s");
      var14.add("&r&r&r");
      var14.add("&cwww.server.net");
      this.messages.getConfig().addDefault("countdown-scoreboard-lines", var14);
      List var15 = this.messages.getConfig().getStringList("ingame-scoreboard-lines");
      var15.add("&cDate: &7%date%");
      var15.add("&r&r");
      var15.add("&cTime: &7%time%");
      var15.add("&cInnocents left: &7%innocents%");
      var15.add("&cRole: &7%role%");
      var15.add("&cMap: &7%map%");
      var15.add("&r&r&r&r");
      var15.add("%bow-state%");
      var15.add("&cKills: &7%kills%");
      var15.add("&cScore: &7%score%");
      var15.add("&r&r&r&r&r");
      var15.add("&cspigotmc.org");
      this.messages.getConfig().addDefault("ingame-scoreboard-lines", var15);
      List var16 = this.messages.getConfig().getStringList("stats-scoreboard-lines");
      var16.add("");
      var16.add("&6Kills: &e%kills%");
      var16.add("&6Deaths: &e%deaths%");
      var16.add("&6Loses: &e%loses%");
      var16.add("&6Wins: &e%wins%");
      var16.add("&6Score: &e%score%");
      var16.add("");
      var16.add("&cspigotmc.org");
      this.messages.getConfig().addDefault("stats-scoreboard-lines", var16);
      List var17 = this.messages.getConfig().getStringList("innocents-won-message");
      var17.add("&a&m---------------------------------------");
      var17.add("&fMurder Mystery");
      var17.add("");
      var17.add("&fWinner: &aPLAYERS");
      var17.add("");
      var17.add("&eMurderer: &e%murderer% &6(%mscore%)");
      var17.add("&eDetective: &e%detective% &6(%dscore%)");
      var17.add("&eHero: &e%hero% &6(%hscore%)");
      var17.add("&a&m---------------------------------------");
      this.messages.getConfig().addDefault("innocents-won-message", var17);
      List var18 = this.messages.getConfig().getStringList("murderer-won-message");
      var18.add("&a&m---------------------------------------");
      var18.add("&fMurder Mystery");
      var18.add("");
      var18.add("&fWinner: &cMURDERER");
      var18.add("");
      var18.add("&eMurderer: &e%murderer% &6(%mscore%)");
      var18.add("&eDetective: &e%detective% &6(%dscore%)");
      var18.add("&eHero: &e%hero%");
      var18.add("&a&m---------------------------------------");
      this.messages.getConfig().addDefault("murderer-won-message", var18);
      this.messages.getConfig().options().copyDefaults(true);
      this.messages.save();
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Config files has been loaded!");
   }

   public void log(String var1) {
      System.out.println(var1);
   }

   @EventHandler
   public void onChange(PlayerChangedWorldEvent var1) {
      if (this.settings.getConfig().getBoolean("board-whitelist")) {
         List var2 = this.settings.getConfig().getStringList("stats-board-world-whitelist");
         if (!var2.contains(var1.getPlayer().getWorld().getName()) && this.scoreboards.containsKey(var1.getPlayer().getName())) {
            this.scoreboards.remove(var1.getPlayer().getName());
            var1.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
         }

         if (var2.contains(var1.getPlayer().getWorld().getName()) && !this.scoreboards.containsKey(var1.getPlayer().getName())) {
            this.setScoreboard(var1.getPlayer());
         }
      }

   }

   @EventHandler
   public void oncommand(PlayerCommandPreprocessEvent var1) {
      if (Arenas.isInArena(var1.getPlayer())) {
         List var2 = this.settings.getConfig().getStringList("whitelisted-commands");
         if (var1.getMessage().contains("/murder") || var1.getMessage().contains("/mm")) {
            return;
         }

         if (var2.contains(var1.getMessage())) {
            return;
         }

         var1.setCancelled(true);
      }

   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      if ((var3.equalsIgnoreCase("murder") || var3.equalsIgnoreCase("mm")) && var1 instanceof Player) {
         Player var5 = (Player)var1;
         Player var6 = (Player)var1;
         if (this.disabled) {
            return true;
         }

         List var7;
         if (this.settings.getConfig().getBoolean("commands-whitelist")) {
            var7 = this.settings.getConfig().getStringList("commands-world-whitelist");
            if (!var7.contains(var6.getWorld().getName())) {
               return true;
            }
         }

         String var8;
         Iterator var9;
         int var11;
         if (var4.length == 0) {
            for(var11 = 1; var11 < 15; ++var11) {
               var5.sendMessage("");
            }

            var5.sendMessage(ChatColor.GREEN + "[Murder]: " + ChatColor.YELLOW + "Created By JosephGP");
            var5.sendMessage("");
            var7 = this.messages.getConfig().getStringList("murder-help-message");
            var9 = var7.iterator();

            while(var9.hasNext()) {
               var8 = (String)var9.next();
               var5.sendMessage(var8.replaceAll("&", "\u00a7"));
            }
         }

         Arena var14;
         Player var15;
         if (var4.length == 1) {
            if (var4[0].equalsIgnoreCase("reset") && this.getPlayerData(var6) != null) {
               if (this.getPlayerData(var6).getdeaths().intValue() == 0 && this.getPlayerData(var6).getkill().intValue() == 0 && this.getPlayerData(var6).getloses().intValue() == 0 && this.getPlayerData(var6).getwins().intValue() == 0) {
                  var6.sendMessage(this.FormatText(this.messages.getConfig().getString("stats-reset-error")));
                  return true;
               }

               if (this.getPlayerData(var6) != null) {
                  this.getPlayerData(var6).reset();
                  var6.sendMessage(this.FormatText(this.messages.getConfig().getString("stats-reset-message")));
               }
            }

            if (var4[0].equalsIgnoreCase("stats") && this.getPlayerData(var6) != null) {
               for(var11 = 1; var11 < 15; ++var11) {
                  var5.sendMessage("");
               }

               var7 = this.messages.getConfig().getStringList("self-info-message");
               var9 = var7.iterator();

               while(var9.hasNext()) {
                  var8 = (String)var9.next();
                  var5.sendMessage(var8.replaceAll("&", "\u00a7").replaceAll("%player%", var6.getName()).replaceAll("%loses%", String.valueOf(this.getPlayerData(var5).getloses())).replaceAll("%wins%", String.valueOf(this.getPlayerData(var5).getwins())).replaceAll("%kills%", String.valueOf(this.getPlayerData(var5).getkill())).replaceAll("%score%", String.valueOf(this.getPlayerData(var5).getscore())).replaceAll("%deaths%", String.valueOf(this.getPlayerData(var5).getdeaths())));
               }
            }

            if (var4[0].equalsIgnoreCase("arenas") && var6.hasPermission("murder.arenas")) {
               this.openInventory(var6);
            }

            Iterator var12;
            if (var4[0].equalsIgnoreCase("randomjoin")) {
               if (Arenas.getArenas() == null || Arenas.getArenas().size() == 0) {
                  var6.sendMessage(this.messages.getConfig().getString("no-arenas").replaceAll("&", "\u00a7"));
                  return true;
               }

               if (var6.hasPermission("murder.randomjoin") && Arenas.getArenas().size() > 0) {
                  var12 = Arenas.getArenas().iterator();

                  label364:
                  while(true) {
                     do {
                        if (!var12.hasNext()) {
                           break label364;
                        }

                        var14 = (Arena)var12.next();
                     } while(var14.getState() != GameState.STARTING && var14.getState() != GameState.LOBBY);

                     if (!Arenas.isInArena(var6)) {
                        var14.addPlayer(var6);
                     }
                  }
               }
            }

            if (var4[0].equalsIgnoreCase("reload")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               this.reloadConfig();
               this.potions.reload();
               this.arenas.reload();
               this.messages.reload();
               this.rewards.reload();
               this.settings.reload();
               this.data.reload();
               var5.sendMessage(ChatColor.GREEN + "Reloaded murder config!");
               var12 = Bukkit.getOnlinePlayers().iterator();

               while(var12.hasNext()) {
                  var15 = (Player)var12.next();
                  this.Regiser(var15);
               }
            }

            if (var4[0].equalsIgnoreCase("admin")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               for(var11 = 1; var11 < 15; ++var11) {
                  var5.sendMessage("");
               }

               var5.sendMessage(ChatColor.GREEN + "[Murder]: " + ChatColor.YELLOW + "Created By JosephGP");
               var5.sendMessage("");
               var5.sendMessage(ChatColor.GRAY + "/murder admin " + ChatColor.WHITE + "To view help page for ops.");
               var5.sendMessage(ChatColor.GRAY + "/murder create <arena> " + ChatColor.WHITE + "Creates an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder settime <arena> <seconds> " + ChatColor.WHITE + "Set time of an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder setmin <arena> <amount> " + ChatColor.WHITE + "Set min amount of players of an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder remove <arena> " + ChatColor.WHITE + "Removes an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder setlobby " + ChatColor.WHITE + "Set main lobby.");
               var5.sendMessage(ChatColor.GRAY + "/murder setwait <arena> " + ChatColor.WHITE + "Set wait lobby.");
               var5.sendMessage(ChatColor.GRAY + "/murder setspectate <arena> " + ChatColor.WHITE + "Set spectator location for an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder addspawn <arena> <number> " + ChatColor.WHITE + "Adds spawns for arena <spawn number should start from 0).");
               var5.sendMessage(ChatColor.GRAY + "/murder addgold <arena> <number> " + ChatColor.WHITE + "Adds gold spawn for arena <gold number should start from 0).");
               var5.sendMessage(ChatColor.GRAY + "/murder start <name> " + ChatColor.WHITE + "Starts an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder stop <name> " + ChatColor.WHITE + "Stop an arena.");
               var5.sendMessage(ChatColor.GRAY + "/murder reload " + ChatColor.WHITE + "Reloaded config files.");
               var5.sendMessage(ChatColor.GRAY + "/murder addpotion <arena> " + ChatColor.WHITE + "Add potion to a specific block.");
               var5.sendMessage(ChatColor.GRAY + "/murder removepotion <arena> " + ChatColor.WHITE + "Remove potion to a specific block.");
            }
         }

         Arena var13;
         if (var4.length == 3) {
            if (var4[0].equalsIgnoreCase("settime")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               this.arenas.getConfig().set("Time." + var4[1], Integer.parseInt(var4[2]));
               this.arenas.save();
               var6.sendMessage(ChatColor.GREEN + "Arena time has been set to " + this.formattominutes(Integer.parseInt(var4[2])) + "!");
            }

            if (var4[0].equalsIgnoreCase("setmin")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               this.arenas.getConfig().set("MinPlayers." + var4[1], Integer.parseInt(var4[2]));
               this.arenas.save();
               var6.sendMessage(ChatColor.GREEN + "Arena time has been set to " + this.arenas.getConfig().getInt("MinPlayers." + var4[1]) + "!");
            }

            if (var4[0].equalsIgnoreCase("addspawn")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               this.addSpawn(var6, var13, Integer.parseInt(var4[2]));
            }

            if (var4[0].equalsIgnoreCase("addgold")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               this.addGold(var6, var13, Integer.parseInt(var4[2]));
            }
         }

         if (var4.length == 2) {
            if (var4[0].equalsIgnoreCase("stats")) {
               if (Bukkit.getPlayer(var4[1]) == null) {
                  var6.sendMessage(this.messages.getConfig().getString("not-online").replaceAll("&", "\u00a7"));
                  return true;
               }

               var15 = Bukkit.getPlayer(var4[1]);
               if (this.getPlayerData(var15) != null) {
                  for(int var17 = 1; var17 < 15; ++var17) {
                     var5.sendMessage("");
                  }

                  List var19 = this.messages.getConfig().getStringList("other-info-message");
                  Iterator var10 = var19.iterator();

                  while(var10.hasNext()) {
                     String var16 = (String)var10.next();
                     var5.sendMessage(var16.replaceAll("&", "\u00a7").replaceAll("%player%", var15.getName()).replaceAll("%loses%", String.valueOf(this.getPlayerData(var15).getloses())).replaceAll("%wins%", String.valueOf(this.getPlayerData(var15).getwins())).replaceAll("%kills%", String.valueOf(this.getPlayerData(var15).getkill())).replaceAll("%score%", String.valueOf(this.getPlayerData(var15).getscore())).replaceAll("%deaths%", String.valueOf(this.getPlayerData(var15).getdeaths())));
                  }
               }
            }

            if (var4[0].equalsIgnoreCase("join")) {
               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               var13.addPlayer(var6);
            }

            Block var18;
            if (var4[0].equalsIgnoreCase("addpotion")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               var18 = var5.getTargetBlock((HashSet)null, 20);
               if (var18.getType() != Material.AIR) {
                  this.addPotion(var18, var13);
                  var6.sendMessage(this.messages.getConfig().getString("add-potion").replaceAll("&", "\u00a7"));
               }
            }

            if (var4[0].equalsIgnoreCase("removepotion")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               var18 = var5.getTargetBlock((HashSet)null, 20);
               if (var18.getType() != Material.AIR) {
                  this.removePotion(var18, var13);
                  var6.sendMessage(this.messages.getConfig().getString("remove-potion").replaceAll("&", "\u00a7"));
               }
            }

            if (var4[0].equalsIgnoreCase("setwait")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               this.setWait(var6, var13);
               var6.sendMessage(this.messages.getConfig().getString("wait-lobby-set-message").replaceAll("&", "\u00a7"));
            }

            if (var4[0].equalsIgnoreCase("setspectate")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               this.setSpec(var6, var13);
               var6.sendMessage(this.messages.getConfig().getString("spectate-set-message").replaceAll("&", "\u00a7"));
            }

            if (var4[0].equalsIgnoreCase("start")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               if (var13.players.size() > 0) {
                  var13.start();
               }
            }

            if (var4[0].equalsIgnoreCase("stop")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               var7 = this.arenas.getConfig().getStringList("arena-list");
               if (!var7.contains(var4[1])) {
                  var6.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
                  return true;
               }

               var13 = Arenas.getArena(var4[1]);
               if (var13.getState() != GameState.LOBBY) {
                  var13.stop("reload");
               }
            }

            if (var4[0].equalsIgnoreCase("create")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               this.createarena(var4[1], var6);
            }

            if (var4[0].equalsIgnoreCase("remove")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               this.removearena(var4[1], var6);
            }
         }

         if (var4.length == 1) {
            if (var4[0].equalsIgnoreCase("leave")) {
               if (!Arenas.isInArena(var6)) {
                  var6.sendMessage(this.FormatText(this.messages.getConfig().getString("not-in-arena")));
                  return true;
               }

               var14 = Arenas.getArena(var6);
               if (!var14.specs.contains(var6)) {
                  var14.removePlayer(var6, "leave");
               }

               if (var14.specs.contains(var6)) {
                  this.setup(var6);
                  var14.specs.remove(var6);
                  this.restoreInventory(var6);
                  Arenas.removeArena(var6);
                  if (!this.getConfig().getBoolean("send-to-server-on-leave")) {
                     var6.teleport(this.getLobby());
                  }

                  if (this.getConfig().getBoolean("send-to-server-on-leave")) {
                     ByteArrayDataOutput var20 = ByteStreams.newDataOutput();
                     var20.writeUTF("Connect");
                     var20.writeUTF(this.getConfig().getString("lobby-server"));
                     var6.sendPluginMessage(this, "BungeeCord", var20.toByteArray());
                  }

                  var6.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
               }
            }

            if (var4[0].equalsIgnoreCase("setlobby")) {
               if (!var6.isOp() && !var6.hasPermission("murder.admin")) {
                  return true;
               }

               this.setLobby(var6);
               var6.sendMessage(this.messages.getConfig().getString("lobby-set-message").replaceAll("&", "\u00a7"));
            }
         }
      }

      return true;
   }

   public void onDisable() {
      if (this.getConfig().getBoolean("update-data-on-server-stop") && this.datalist.size() > 0) {
         PlayerData[] var4;
         int var3 = (var4 = this.getPlayersData()).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            PlayerData var1 = var4[var2];
            if (var1 != null) {
               if (!this.getConfig().getBoolean("mysql")) {
                  this.api.setNonSQLData(var1.p, var1.getkill().intValue(), var1.getdeaths().intValue(), var1.getloses().intValue(), var1.getwins().intValue(), var1.getscore().intValue());
               }

               if (this.getConfig().getBoolean("mysql")) {
                  this.api.setSQLDataInstantly(var1.p, var1.getkill().intValue(), var1.getdeaths().intValue(), var1.getloses().intValue(), var1.getwins().intValue(), var1.getscore().intValue());
               }
            }
         }
      }

      Iterator var8;
      if (Arenas.getArenas() != null && Arenas.getArenas().size() > 0) {
         var8 = Arenas.getArenas().iterator();

         while(var8.hasNext()) {
            Arena var6 = (Arena)var8.next();
            if (var6 != null) {
               var6.stop("stop");
            }
         }
      }

      if (this.getConfig().getBoolean("mysql")) {
         this.sqlConnection.closeConnection();
      }

      var8 = Bukkit.getWorlds().iterator();

      while(var8.hasNext()) {
         World var7 = (World)var8.next();
         Iterator var10 = var7.getEntities().iterator();

         while(var10.hasNext()) {
            Entity var9 = (Entity)var10.next();
            if (var9.getType() == EntityType.DROPPED_ITEM) {
               Item var5 = (Item)var9;
               if (var5.getItemStack().getType() == Material.getMaterial(this.settings.getConfig().getInt("dropped-item-id"))) {
                  var9.remove();
               }
            }

            ArmorStand var11;
            if (var9.getType() == EntityType.ARMOR_STAND) {
               var11 = (ArmorStand)var9;
               if (var11.getItemInHand().getType() == Material.getMaterial(this.settings.getConfig().getInt("murderer-weapon.item-id"))) {
                  var9.remove();
               }
            }

            if (var9.getType() == EntityType.ARMOR_STAND) {
               var11 = (ArmorStand)var9;
               if (var11.getItemInHand().getType() == Material.BOW) {
                  var9.remove();
               }
            }
         }
      }

   }

   public void onEnable() {
      instance = this;
      this.cooldownTime = new HashMap();
      this.cooldownTask = new HashMap();
      this.nmsver = Bukkit.getServer().getClass().getPackage().getName();
      this.nmsver = this.nmsver.substring(this.nmsver.lastIndexOf(".") + 1);
      if (this.nmsver.equalsIgnoreCase("v1_8_R1") || this.nmsver.startsWith("v1_7_")) {
         this.useOldMethods = true;
      }

      this.api = new MurderAPI(this);
      this.sm = new SignManager(this);
      Bukkit.getServer().getPluginManager().registerEvents(this, this);
      Bukkit.getServer().getPluginManager().registerEvents(new SignListener(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new SignListener2(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new BlockEvents(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new ClickPotionEffectBlock(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new DamageEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new DropItem(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageByEntityEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new FoodLevel(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new LeaveItem(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new LoginEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new Motd(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new NoPainting(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new NoSpecDamage(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new OpenVoteGUI(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new PickUpEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new PotionConsumEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new ProjectileLaunch(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new SpectateEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new SpectatorItem(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new SwordEvent(this), this);
      Bukkit.getServer().getPluginManager().registerEvents(new VoteEvent(this), this);
      Bukkit.getConsoleSender().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Murder Mystery 2");
      Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Plugin has been enabled");
      Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Was created by JosephGP");
      Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + "Version: " + this.getDescription().getVersion());
      this.LoadConfigFiles();
      this.loadarenas();
      if (this.getConfig().getBoolean("mysql")) {
         String var1 = this.getConfig().getString("host");
         String var2 = this.getConfig().getString("port");
         String var3 = this.getConfig().getString("database");
         String var4 = this.getConfig().getString("username");
         String var5 = this.getConfig().getString("password");
         this.sqlConnection = new SQLConnection(this, this, var1, var2, var3, var4, var5);
         this.sqlConnection.openConnection();
      }

      Iterator var8 = Bukkit.getOnlinePlayers().iterator();

      Player var6;
      while(var8.hasNext()) {
         var6 = (Player)var8.next();
         this.Regiser(var6);
         this.registerNewData(var6);
         this.setScoreboard(var6);
      }

      if (this.getConfig().getBoolean("bungee")) {
         Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
      }

      (new BukkitRunnable() {
         public void run() {
            Iterator var2 = Arenas.getArenas().iterator();

            while(var2.hasNext()) {
               Arena var1 = (Arena)var2.next();
               Main.this.sm.updateSigns(var1);
            }

            var2 = Bukkit.getOnlinePlayers().iterator();

            Arena var3;
            Player var7;
            while(var2.hasNext()) {
               var7 = (Player)var2.next();
               if (Main.this.getPlayerData(var7) == null) {
                  Main.this.Regiser(var7);
                  Main.this.registerNewData(var7);
               }

               if (Arenas.isInArena(var7)) {
                  var3 = Arenas.getArena(var7);
                  if (var3.getState() == GameState.INGAME) {
                     if (!var3.specs.contains(var7) && var3.innocents.contains(var7) && var7.getItemInHand().getType() == Material.getMaterial(Main.this.settings.getConfig().getInt("track.item-id")) && var3.bowloc != null) {
                        Location var4 = var3.bowloc;
                        if (var4.getWorld().getName().equalsIgnoreCase(var7.getWorld().getName())) {
                           double var5 = var7.getLocation().distance(var4);
                           if (var5 != 0.0D && var7.getLocation().distance(var4) > 0.0D) {
                              if (var7.getItemInHand().getType() == Material.COMPASS) {
                                 var7.setCompassTarget(var4);
                              }

                              Main.this.api.sendActionBar(var7, Main.this.FormatText(Main.this.messages.getConfig().getString("bow-location-message").replaceAll("%loc%", String.valueOf(Math.round(var5)))));
                           }
                        }
                     }

                     if (var3.murder.contains(var7) && !var3.specs.contains(var7) && var7.getItemInHand().getType() == Material.getMaterial(Main.this.settings.getConfig().getInt("murder-track.item-id"))) {
                        Player var9 = Main.this.getNearestName(var7, Main.this.settings.getConfig().getDouble("murder-track.range"));
                        if (var9 != null && var9.isOnline() && !var3.specs.contains(var9) && var9.getWorld().getName().equalsIgnoreCase(var7.getWorld().getName())) {
                           Main.this.api.sendActionBar(var7, Main.this.FormatText(Main.this.messages.getConfig().getString("near-player-location-message").replaceAll("%player%", var9.getName()).replaceAll("%distance%", String.valueOf(Math.round(Main.this.getNearestDouble(var7, Main.this.settings.getConfig().getDouble("murder-track.range")).doubleValue())))));
                        }
                     }
                  }
               }
            }

            Player var8;
            Iterator var10;
            if (Main.this.getConfig().getBoolean("do-not-edit")) {
               var2 = Bukkit.getOnlinePlayers().iterator();

               while(var2.hasNext()) {
                  var7 = (Player)var2.next();
                  var10 = Bukkit.getOnlinePlayers().iterator();

                  while(var10.hasNext()) {
                     var8 = (Player)var10.next();
                     if (var8.getWorld().getName().equalsIgnoreCase(var7.getWorld().getName()) && Arenas.isInArena(var8) && Arenas.isInArena(var7)) {
                        if (var8.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                           var7.hidePlayer(var8);
                        }

                        if (!var8.hasPotionEffect(PotionEffectType.INVISIBILITY) && var7.getWorld().getName().equalsIgnoreCase(var8.getWorld().getName())) {
                           var7.showPlayer(var8);
                        }
                     }
                  }
               }
            }

            if (!Main.this.getConfig().getBoolean("do-not-edit")) {
               var2 = Bukkit.getOnlinePlayers().iterator();

               while(var2.hasNext()) {
                  var7 = (Player)var2.next();
                  var10 = Bukkit.getOnlinePlayers().iterator();

                  while(var10.hasNext()) {
                     var8 = (Player)var10.next();
                     if (var8.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        var7.hidePlayer(var8);
                     }

                     if (!var8.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        var7.showPlayer(var8);
                     }
                  }
               }
            }

            var2 = Bukkit.getOnlinePlayers().iterator();

            while(true) {
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var7 = (Player)var2.next();
                  if (Main.this.getConfig().getBoolean("bungee") && !Arenas.isInArena(var7) && var7.getLocation().getBlockY() <= 0) {
                     var7.teleport(Main.this.getLobby());
                  }
               } while(!Arenas.isInArena(var7));

               var3 = Arenas.getArena(var7);
               if ((var3.getState() == GameState.LOBBY || var3.getState() == GameState.STARTING) && var7.getLocation().getBlockY() <= 0) {
                  var7.teleport(Main.this.getWait(var3));
               }

               if (var3.getState() == GameState.INGAME && var7.getLocation().getBlockY() <= 0 && !var7.getAllowFlight()) {
                  var7.setAllowFlight(true);
                  var7.setFlying(true);
                  var7.teleport(Main.this.getSpec(var3));
                  var3.removePlayer(var7, "death");
                  if (Main.this.getPlayerData(var7) != null) {
                     Main.this.getPlayerData(var7).adddeaths(Integer.valueOf(1));
                     Main.this.getPlayerData(var7).addlose(Integer.valueOf(1));
                  }
               }
            }
         }
      }).runTaskTimer(this, 20L, 20L);
      if (this.getConfig().getBoolean("bungee")) {
         (new BukkitRunnable() {
            public void run() {
               if (Arenas.getArenas().size() == 1 && Arenas.getArenas() != null) {
                  Arena var1 = (Arena)Arenas.getArenas().get(0);
                  if (Bukkit.getOnlinePlayers().size() > 0) {
                     Iterator var3 = Bukkit.getOnlinePlayers().iterator();

                     while(var3.hasNext()) {
                        Player var2 = (Player)var3.next();
                        if (!Arenas.isInArena(var2)) {
                           if (var1.getState() == GameState.INGAME) {
                              if (!Main.this.getConfig().getBoolean("send-to-server-on-leave")) {
                                 var2.teleport(Main.this.getLobby());
                              }

                              if (Main.this.getConfig().getBoolean("send-to-server-on-leave")) {
                                 ByteArrayDataOutput var4 = ByteStreams.newDataOutput();
                                 var4.writeUTF("Connect");
                                 var4.writeUTF(Main.this.getConfig().getString("lobby-server"));
                                 var2.sendPluginMessage(Main.this.plugin(), "BungeeCord", var4.toByteArray());
                              }

                              return;
                           }

                           var1.addPlayer(var2);
                        }
                     }
                  }
               }

            }
         }).runTaskLater(this, 20L);
      }

      if (this.getConfig().getBoolean("bungee") && Arenas.getArenas().size() > 1) {
         if (Bukkit.getOnlinePlayers().size() > 0) {
            var8 = Bukkit.getOnlinePlayers().iterator();

            while(var8.hasNext()) {
               var6 = (Player)var8.next();
               this.setUpForMultiMaps(var6);
            }
         }

         var8 = Arenas.getArenas().iterator();

         while(var8.hasNext()) {
            Arena var7 = (Arena)var8.next();
            this.point.put(var7.getName(), Integer.valueOf(0));
         }

         (new BukkitRunnable() {
            public void run() {
               if (Bukkit.getOnlinePlayers().size() >= Main.this.settings.getConfig().getInt("min-players-to-start-bungee")) {
                  Main.this.StartMap();
                  this.cancel();
               }
            }
         }).runTaskTimer(this, 20L, 20L);
      }

      this.passable.add(Material.AIR);
      this.passable.add(Material.WATER);
      this.passable.add(Material.STATIONARY_WATER);
      this.passable.add(Material.WALL_BANNER);
      this.passable.add(Material.WALL_SIGN);
      this.passable.add(Material.CARPET);
      this.passable.add(Material.CARROT_ITEM);
      this.passable.add(Material.CROPS);
      this.passable.add(Material.DEAD_BUSH);
      this.passable.add(Material.DIODE);
      this.passable.add(Material.DIODE_BLOCK_OFF);
      this.passable.add(Material.DIODE_BLOCK_ON);
      this.passable.add(Material.REDSTONE_TORCH_OFF);
      this.passable.add(Material.REDSTONE_TORCH_OFF);
      this.passable.add(Material.TORCH);
      this.passable.add(Material.DOUBLE_PLANT);
      this.passable.add(Material.LONG_GRASS);
      if (this.user.equalsIgnoreCase("10045") || this.user.equalsIgnoreCase("1700")) {
         this.disabled = true;
      }

      if (!isInt(this.user)) {
         this.disabled = true;
      }

      Bukkit.getConsoleSender().sendMessage("\u00a7cCracked By yeongpin\u00a7f/\u00a7eBought By \u00a76YossiMan");
   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent var1) {
      if (Arenas.isInArena((Player)var1.getWhoClicked()) && var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         var1.setCancelled(true);
      }

      if (var1.getInventory().getTitle().equalsIgnoreCase(this.settings.getConfig().getString("arenas-inventory-title"))) {
         if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
            var1.setCancelled(true);
         }

         if (var1.getInventory().contains(var1.getCurrentItem())) {
            if (var1.getSlot() == var1.getInventory().getSize() - 1) {
               this.openInventory((Player)var1.getWhoClicked());
            } else {
               if (Arenas.getArena(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName())) != null) {
                  Arena var2 = Arenas.getArena(ChatColor.stripColor(var1.getCurrentItem().getItemMeta().getDisplayName()));
                  var2.addPlayer((Player)var1.getWhoClicked());
                  var1.getWhoClicked().closeInventory();
               }

            }
         }
      }
   }

   public void openInventory(Player var1) {
      if (!this.getConfig().getBoolean("bungee")) {
         if (!Arenas.isInArena(var1)) {
            if (Arenas.getArenas() != null && Arenas.getArenas().size() != 0) {
               Inventory var2 = Bukkit.createInventory((InventoryHolder)null, this.settings.getConfig().getInt("arenas-inventory-size"), this.FormatText(this.settings.getConfig().getString("arenas-inventory-title")));
               Iterator var4 = Arenas.getArenas().iterator();

               while(true) {
                  Arena var3;
                  ItemStack var5;
                  ItemMeta var6;
                  List var7;
                  ArrayList var8;
                  String var9;
                  String var10;
                  Iterator var11;
                  do {
                     if (!var4.hasNext()) {
                        ItemStack var12 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("arenas-refresh-item-id")), 1, (short)this.settings.getConfig().getInt("arenas-refresh-item-data"));
                        ItemMeta var13 = var12.getItemMeta();
                        var13.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
                        var13.setDisplayName(this.FormatText(this.settings.getConfig().getString("arenas-refresh-item-name")));
                        var12.setItemMeta(var13);
                        var2.setItem(var2.getSize() - 1, var12);
                        var1.openInventory(var2);
                        return;
                     }

                     var3 = (Arena)var4.next();
                     if (var3.getState() == GameState.LOBBY) {
                        var5 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("arenas-lobby-state-item-id")), 1, (short)this.settings.getConfig().getInt("arenas-lobby-state-item-data"));
                        var6 = var5.getItemMeta();
                        var6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
                        var6.setDisplayName(this.FormatText(this.settings.getConfig().getString("arenas-lobby-state-item-name").replaceAll("%arena%", var3.getName())));
                        var7 = this.settings.getConfig().getStringList("arenas-lobby-state-item-lore");
                        var8 = new ArrayList();
                        var9 = var3.getState().toString().toLowerCase();
                        var11 = var7.iterator();

                        while(var11.hasNext()) {
                           var10 = (String)var11.next();
                           var8.add(var10.replaceAll("&", "\u00a7").replaceAll("%state%", this.capitalizeFirstLetter(var9)).replaceAll("%map%", this.capitalizeFirstLetter(var3.getName())).replaceAll("%max%", String.valueOf(this.SpawnSize(var3))).replaceAll("%players%", String.valueOf(var3.players.size())));
                        }

                        var6.setLore(var8);
                        var5.setItemMeta(var6);
                        var2.addItem(new ItemStack[]{var5});
                     }

                     if (var3.getState() == GameState.STARTING) {
                        var5 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("arenas-starting-state-item-id")), 1, (short)this.settings.getConfig().getInt("arenas-starting-state-item-data"));
                        var6 = var5.getItemMeta();
                        var6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
                        var6.setDisplayName(this.FormatText(this.settings.getConfig().getString("arenas-starting-state-item-name").replaceAll("%arena%", var3.getName())));
                        var7 = this.settings.getConfig().getStringList("arenas-starting-state-item-lore");
                        var8 = new ArrayList();
                        var9 = var3.getState().toString().toLowerCase();
                        var11 = var7.iterator();

                        while(var11.hasNext()) {
                           var10 = (String)var11.next();
                           var8.add(var10.replaceAll("&", "\u00a7").replaceAll("%state%", this.capitalizeFirstLetter(var9)).replaceAll("%map%", this.capitalizeFirstLetter(var3.getName())).replaceAll("%max%", String.valueOf(this.SpawnSize(var3))).replaceAll("%players%", String.valueOf(var3.players.size())));
                        }

                        var6.setLore(var8);
                        var5.setItemMeta(var6);
                        var2.addItem(new ItemStack[]{var5});
                     }
                  } while(var3.getState() != GameState.INGAME);

                  var5 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("arenas-ingame-state-item-id")), 1, (short)this.settings.getConfig().getInt("arenas-ingame-state-item-data"));
                  var6 = var5.getItemMeta();
                  var6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE});
                  var6.setDisplayName(this.FormatText(this.settings.getConfig().getString("arenas-ingame-state-item-name").replaceAll("%arena%", var3.getName())));
                  var7 = this.settings.getConfig().getStringList("arenas-ingame-state-item-lore");
                  var8 = new ArrayList();
                  var9 = var3.getState().toString().toLowerCase();
                  var11 = var7.iterator();

                  while(var11.hasNext()) {
                     var10 = (String)var11.next();
                     var8.add(var10.replaceAll("&", "\u00a7").replaceAll("%state%", this.capitalizeFirstLetter(var9)).replaceAll("%map%", this.capitalizeFirstLetter(var3.getName())).replaceAll("%max%", String.valueOf(this.SpawnSize(var3))).replaceAll("%players%", String.valueOf(var3.players.size())));
                  }

                  var6.setLore(var8);
                  var5.setItemMeta(var6);
                  var2.addItem(new ItemStack[]{var5});
               }
            } else {
               var1.sendMessage(this.messages.getConfig().getString("no-arenas").replaceAll("&", "\u00a7"));
            }
         }
      }
   }

   public void OpenSpec(Player var1) {
      if (Arenas.isInArena(var1)) {
         Arena var2 = Arenas.getArena(var1);
         if (var2.specs.contains(var1)) {
            Inventory var3 = Bukkit.createInventory((InventoryHolder)null, this.settings.getConfig().getInt("spectate-inventory-size"), this.FormatText(this.settings.getConfig().getString("spectate-inventory-title")));
            Player[] var7;
            int var6 = (var7 = var2.getPlayers2()).length;

            for(int var5 = 0; var5 < var6; ++var5) {
               Player var4 = var7[var5];
               ItemStack var8 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
               SkullMeta var9 = (SkullMeta)var8.getItemMeta();
               var9.setOwner(var4.getName());
               var9.setDisplayName(ChatColor.valueOf(this.settings.getConfig().getString("Spectate-Display-Name-Color").toUpperCase()) + var4.getName());
               var9.setLore(Arrays.asList(this.FormatText(this.settings.getConfig().getString("Spectate-Display-Lore"))));
               var8.setItemMeta(var9);
               var3.addItem(new ItemStack[]{var8});
            }

            var1.openInventory(var3);
         }
      }

   }

   public void OpenVote(Player var1) {
      if (Arenas.getArenas().size() > 1) {
         Inventory var2 = Bukkit.createInventory((InventoryHolder)null, this.settings.getConfig().getInt("vote-inventory.size"), this.FormatText(this.settings.getConfig().getString("vote-inventory.name")));
         var2.clear();
         Iterator var4 = Arenas.getArenas().iterator();

         while(var4.hasNext()) {
            Arena var3 = (Arena)var4.next();
            if (!this.getConfig().contains(var3.getName())) {
               ItemStack var5 = new ItemStack(Material.PAPER);
               ItemMeta var6 = var5.getItemMeta();
               var6.setDisplayName(ChatColor.valueOf(this.settings.getConfig().getString("map-displayname-in-gui-color").toUpperCase()) + var3.getName());
               var6.setLore(Arrays.asList(this.FormatText(this.settings.getConfig().getString("map-item-lore").replaceAll("%votes%", String.valueOf(this.point.get(var3.getName()))))));
               var5.setItemMeta(var6);
               var2.addItem(new ItemStack[]{var5});
            }

            if (this.getConfig().contains(var3.getName())) {
               String var9 = this.getConfig().getString(var3.getName());
               String[] var10 = var9.split(";");
               ItemStack var7 = new ItemStack(Material.getMaterial(Integer.parseInt(var10[0])), 1, (short)Integer.parseInt(var10[1]));
               ItemMeta var8 = var7.getItemMeta();
               var8.setDisplayName(ChatColor.valueOf(this.settings.getConfig().getString("map-displayname-in-gui-color").toUpperCase()) + var3.getName());
               var8.setLore(Arrays.asList(this.FormatText(this.settings.getConfig().getString("map-item-lore").replaceAll("%votes%", String.valueOf(this.point.get(var3.getName()))))));
               var7.setItemMeta(var8);
               var2.addItem(new ItemStack[]{var7});
            }
         }

         var1.openInventory(var2);
      }
   }

   public Plugin plugin() {
      return this;
   }

   public void Regiser(Player var1) {
      if (this.getConfig().getBoolean("mysql")) {
         if (!this.api.existsInDatabase(var1)) {
            this.api.createAccount(var1);
         }

         if (!this.api.existsInDatabase2(var1)) {
            this.api.createAccount2(var1);
         }
      }

   }

   public void registerNewData(Player var1) {
      if (!this.pdata.containsKey(var1.getName())) {
         PlayerData var2 = new PlayerData(this, var1);
         this.pdata.put(var1.getName(), var2);
         this.datalist.add(var2);
      }

   }

   public void removearena(String var1, Player var2) {
      List var3 = this.arenas.getConfig().getStringList("arena-list");
      if (!var3.contains(var1)) {
         var2.sendMessage(this.messages.getConfig().getString("arena-not-exits").replaceAll("&", "\u00a7"));
      } else {
         var3.remove(var1);
         this.arenas.getConfig().set("arena-list", var3);
         this.arenas.save();
         if (this.arenas.getConfig().contains("Spawns." + var1)) {
            this.arenas.getConfig().set("Spawns." + var1, (Object)null);
         }

         if (this.arenas.getConfig().contains("Wait." + var1)) {
            this.arenas.getConfig().set("Wait." + var1, (Object)null);
         }

         if (this.arenas.getConfig().contains("Spectator." + var1)) {
            this.arenas.getConfig().set("Spectator." + var1, (Object)null);
         }

         if (this.arenas.getConfig().contains("Signs." + var1)) {
            this.arenas.getConfig().set("Signs." + var1, (Object)null);
         }

         if (this.arenas.getConfig().contains("Gold." + var1)) {
            this.arenas.getConfig().set("Gold." + var1, (Object)null);
         }

         if (this.arenas.getConfig().contains(var1)) {
            this.arenas.getConfig().set(var1, (Object)null);
         }

         this.arenas.save();
         var2.sendMessage(this.messages.getConfig().getString("arena-remove-message").replaceAll("&", "\u00a7"));
      }
   }

   public void removeCompass(Player var1) {
      var1.getInventory().setItem(4, (ItemStack)null);
      var1.updateInventory();
   }

   public void removeitem(Player var1) {
      if (var1.getItemInHand().getAmount() == 1) {
         var1.setItemInHand((ItemStack)null);
         var1.updateInventory();
      } else {
         var1.getItemInHand().setAmount(var1.getItemInHand().getAmount() - 1);
         var1.updateInventory();
      }
   }

   public void removePlayerData(Player var1) {
      if (this.pdata.containsKey(var1.getName())) {
         PlayerData var2 = (PlayerData)this.pdata.get(var1.getName());
         if (!this.getConfig().getBoolean("mysql")) {
            this.api.setNonSQLData(var2.p, var2.getkill().intValue(), var2.getdeaths().intValue(), var2.getloses().intValue(), var2.getwins().intValue(), var2.getscore().intValue());
         }

         if (this.getConfig().getBoolean("mysql")) {
            this.api.setSQLData(var2.p, var2.getkill().intValue(), var2.getdeaths().intValue(), var2.getloses().intValue(), var2.getwins().intValue(), var2.getscore().intValue());
         }

         var2.reset();
         if (this.datalist.contains(var2)) {
            this.datalist.remove(var2);
         }

         this.pdata.remove(var1.getName());
      }

   }

   public void removePotion(Block var1, Arena var2) {
      List var3 = this.arenas.getConfig().getStringList("Potions." + var2.getName());
      if (var3.contains(this.getStringFromLocation(var1.getLocation()))) {
         var3.remove(this.getStringFromLocation(var1.getLocation()));
         this.arenas.getConfig().set("Potions." + var2.getName(), var3);
         this.arenas.save();
      }
   }

   public void restoreInventory(Player var1) {
      if (var1.isOnline() && var1 != null) {
         if (this.level.containsKey(var1.getName())) {
            var1.setLevel(((Integer)this.level.get(var1.getName())).intValue());
            this.level.remove(var1.getName());
         }

         if (this.xp.containsKey(var1.getName())) {
            var1.setExp(((Float)this.xp.get(var1.getName())).floatValue());
            this.xp.remove(var1.getName());
         }

         if (this.inventoryContents.containsKey(var1.getName())) {
            var1.getInventory().clear();
            var1.getInventory().setContents((ItemStack[])this.inventoryContents.get(var1.getName()));
            this.inventoryContents.remove(var1.getName());
         }

         if (this.armourContents.containsKey(var1.getName())) {
            var1.getInventory().setArmorContents((ItemStack[])null);
            var1.getInventory().setArmorContents((ItemStack[])this.armourContents.get(var1.getName()));
            this.armourContents.remove(var1.getName());
         }

         if (this.gamemode.containsKey(var1.getName())) {
            var1.setGameMode((GameMode)this.gamemode.get(var1.getName()));
            this.gamemode.remove(var1.getName());
         }

         var1.updateInventory();
      }

   }

   public void saveInventory(Player var1) {
      this.level.put(var1.getName(), var1.getLevel());
      this.xp.put(var1.getName(), var1.getExp());
      this.armourContents.put(var1.getName(), var1.getInventory().getArmorContents());
      this.inventoryContents.put(var1.getName(), var1.getInventory().getContents());
      this.gamemode.put(var1.getName(), var1.getGameMode());
      var1.getInventory().clear();
      var1.getInventory().setArmorContents((ItemStack[])null);
      var1.updateInventory();
   }

   public void sendPlayers() {
      (new BukkitRunnable() {
         public void run() {
            Iterator var2 = Bukkit.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
               Player var1 = (Player)var2.next();
               Main.this.bungee.addPlayer(var1);
            }

         }
      }).runTaskLater(this, (long)(20 * this.settings.getConfig().getInt("time-until-game-start")));
   }

   public void setCompass(Player var1) {
      if (Arenas.isInArena(var1)) {
         Arena var2 = Arenas.getArena(var1);
         if (!var2.specs.contains(var1)) {
            ItemStack var3 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("track.item-id")));
            var3.setDurability((short)this.settings.getConfig().getInt("track.item-subid"));
            ItemMeta var4 = var3.getItemMeta();
            var4.setDisplayName(this.settings.getConfig().getString("track.item-name").replaceAll("&", "\u00a7"));
            ArrayList var5 = new ArrayList();
            var5.add(this.settings.getConfig().getString("track.item-lore").replaceAll("&", "\u00a7"));
            var4.setLore(var5);
            var3.setItemMeta(var4);
            if (!var1.getInventory().contains(var3)) {
               var1.getInventory().setItem(4, var3);
               var1.updateInventory();
            }
         }
      }

   }

   public void setLobby(Player var1) {
      this.arenas.getConfig().set("Lobby.main.lobby.world", var1.getLocation().getWorld().getName());
      this.arenas.getConfig().set("Lobby.main.lobby.x", var1.getLocation().getX());
      this.arenas.getConfig().set("Lobby.main.lobby.y", var1.getLocation().getY());
      this.arenas.getConfig().set("Lobby.main.lobby.z", var1.getLocation().getZ());
      this.arenas.getConfig().set("Lobby.main.lobby.yaw", (double)var1.getLocation().getYaw());
      this.arenas.getConfig().set("Lobby.main.lobby.pitch", (double)var1.getLocation().getPitch());
      this.arenas.save();
   }

   public void setMap(Player var1) {
      ItemStack var2 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("map.item-id")));
      var2.setDurability((short)this.settings.getConfig().getInt("map.item-subid"));
      ItemMeta var3 = var2.getItemMeta();
      var3.setDisplayName(this.settings.getConfig().getString("map.item-name").replaceAll("&", "\u00a7"));
      ArrayList var4 = new ArrayList();
      var4.add(this.settings.getConfig().getString("map.item-lore").replaceAll("&", "\u00a7"));
      var3.setLore(var4);
      var2.setItemMeta(var3);
      var1.getInventory().setItem(0, var2);
      var1.updateInventory();
   }

   public void setScoreboard(final Player var1) {
      if (!this.scoreboards.containsKey(var1.getName())) {
         (new BukkitRunnable() {
            public void run() {
               if (!var1.isOnline()) {
                  if (Main.this.scoreboards.containsKey(var1.getName())) {
                     Main.this.scoreboards.remove(var1.getName());
                  }

                  if (Main.this.scorestate.containsKey(var1.getName())) {
                     Main.this.scorestate.remove(var1.getName());
                  }

                  this.cancel();
               } else {
                  ScoreboardType var1x = null;
                  if (!Arenas.isInArena(var1) && Main.this.getConfig().getBoolean("bungee") && Arenas.getArenas().size() > 1) {
                     var1x = ScoreboardType.VOTING;
                  }

                  if (!Arenas.isInArena(var1) && Main.this.settings.getConfig().getBoolean("stats-board") && !Main.this.getConfig().getBoolean("bungee")) {
                     if (!Main.this.settings.getConfig().getBoolean("board-whitelist")) {
                        var1x = ScoreboardType.STATS;
                     }

                     if (Main.this.settings.getConfig().getBoolean("board-whitelist")) {
                        List var2 = Main.this.settings.getConfig().getStringList("stats-board-world-whitelist");
                        if (var2.contains(var1.getWorld().getName())) {
                           var1x = ScoreboardType.STATS;
                        }
                     }
                  }

                  if (Arenas.isInArena(var1) && Arenas.getArena(var1).getState() == GameState.LOBBY) {
                     var1x = ScoreboardType.WAITING;
                  }

                  if (Arenas.isInArena(var1) && Arenas.getArena(var1).getState() == GameState.STARTING) {
                     var1x = ScoreboardType.STARTING;
                  }

                  if (Arenas.isInArena(var1) && Arenas.getArena(var1).getState() == GameState.INGAME) {
                     var1x = ScoreboardType.INGAME;
                  }

                  if (var1x != null) {
                     ScoreboardManager var10 = null;
                     if (!Main.this.scoreboards.containsKey(var1.getName())) {
                        var10 = new ScoreboardManager(var1.getName());
                        Main.this.scoreboards.put(var1.getName(), var10);
                     }

                     if (Main.this.scoreboards.containsKey(var1.getName()) && !Main.this.scorestate.containsKey(var1.getName())) {
                        Main.this.scorestate.put(var1.getName(), var1x);
                     }

                     if (Main.this.scoreboards.containsKey(var1.getName()) && var1x != Main.this.scorestate.get(var1.getName())) {
                        var10 = new ScoreboardManager(var1.getName());
                        Main.this.scoreboards.put(var1.getName(), var10);
                        Main.this.scorestate.put(var1.getName(), var1x);
                     }

                     if (var10 == null) {
                        var10 = (ScoreboardManager)Main.this.scoreboards.get(var1.getName());
                     }

                     if (var10 != null) {
                        if (var1x == ScoreboardType.VOTING) {
                           var10.setTitle(0, Main.this.FormatText(Main.this.messages.getConfig().getString("vote-scoreboard-title")));
                           int var3 = Arenas.getArenas().size();

                           for(Iterator var5 = Arenas.getArenas().iterator(); var5.hasNext(); --var3) {
                              Arena var4 = (Arena)var5.next();
                              int var6 = 0;
                              if (Main.this.point.containsKey(var4.getName())) {
                                 var6 = ((Integer)Main.this.point.get(var4.getName())).intValue();
                              }

                              var10.setLine(0, var3, Main.this.FormatText(Main.this.messages.getConfig().getString("scoreboard-map").replaceAll("%votes%", String.valueOf(var6)).replaceAll("%map%", var4.getName())));
                           }
                        }

                        if (var1x == ScoreboardType.STATS && Main.this.getPlayerData(var1) != null) {
                           var10.setTitle(0, Main.this.FormatText(Main.this.messages.getConfig().getString("stats-scoreboard-title")));
                           List var11 = Main.this.messages.getConfig().getStringList("stats-scoreboard-lines");
                           int var13 = Main.this.messages.getConfig().getStringList("stats-scoreboard-lines").size();

                           for(Iterator var19 = var11.iterator(); var19.hasNext(); --var13) {
                              String var16 = (String)var19.next();
                              var10.setLine(0, var13, var16.replaceAll("&", "\u00a7").replaceAll("%player%", var1.getName()).replaceAll("%loses%", String.valueOf(Main.this.getPlayerData(var1).getloses())).replaceAll("%wins%", String.valueOf(Main.this.getPlayerData(var1).getwins())).replaceAll("%kills%", String.valueOf(Main.this.getPlayerData(var1).getkill())).replaceAll("%score%", String.valueOf(Main.this.getPlayerData(var1).getscore())).replaceAll("%deaths%", String.valueOf(Main.this.getPlayerData(var1).getdeaths())));
                           }
                        }

                        Iterator var7;
                        Arena var12;
                        List var14;
                        int var17;
                        String var21;
                        if (var1x == ScoreboardType.WAITING) {
                           var12 = Arenas.getArena(var1);
                           var10.setTitle(0, Main.this.FormatText(Main.this.messages.getConfig().getString("wait-scoreboard-title")));
                           var14 = Main.this.messages.getConfig().getStringList("wait-scoreboard-lines");
                           var17 = Main.this.messages.getConfig().getStringList("wait-scoreboard-lines").size();

                           for(var7 = var14.iterator(); var7.hasNext(); --var17) {
                              var21 = (String)var7.next();
                              var10.setLine(0, var17, var21.replaceAll("%max%", String.valueOf(Main.this.SpawnSize(var12))).replaceAll("%map%", var12.getName()).replaceAll("%size%", String.valueOf(var12.players.size())).replaceAll("&", "\u00a7"));
                           }
                        }

                        if (var1x == ScoreboardType.STARTING) {
                           var12 = Arenas.getArena(var1);
                           var10.setTitle(0, Main.this.FormatText(Main.this.messages.getConfig().getString("countdown-scoreboard-title")));
                           var14 = Main.this.messages.getConfig().getStringList("countdown-scoreboard-lines");
                           var17 = Main.this.messages.getConfig().getStringList("countdown-scoreboard-lines").size();

                           for(var7 = var14.iterator(); var7.hasNext(); --var17) {
                              var21 = (String)var7.next();
                              var10.setLine(0, var17, var21.replaceAll("%countdown%", String.valueOf(var12.countdown)).replaceAll("%max%", String.valueOf(Main.this.SpawnSize(var12))).replaceAll("%size%", String.valueOf(var12.players.size())).replaceAll("%map%", var12.getName()).replaceAll("&", "\u00a7"));
                           }
                        }

                        if (var1x == ScoreboardType.INGAME) {
                           var12 = Arenas.getArena(var1);
                           var10.addTeam("team", var12.getPlayers2());
                           Date var18 = new Date();
                           SimpleDateFormat var20 = new SimpleDateFormat("dd-MM-yy");
                           var10.setTitle(0, Main.this.FormatText(Main.this.messages.getConfig().getString("ingame-scoreboard-title").replaceAll("%time%", Main.this.formattominutes(Arenas.getArena(var1).time))));
                           List var23 = Main.this.messages.getConfig().getStringList("ingame-scoreboard-lines");
                           int var22 = Main.this.messages.getConfig().getStringList("ingame-scoreboard-lines").size();

                           for(Iterator var9 = var23.iterator(); var9.hasNext(); --var22) {
                              String var8 = (String)var9.next();
                              var10.setLine(0, var22, var8.replaceAll("%spectators%", String.valueOf(var12.specs.size())).replaceAll("%innocents%", String.valueOf(var12.innocents.size())).replaceAll("%kills%", String.valueOf(var12.getkill(var1))).replaceAll("%bow-state%", var12.getBowState()).replaceAll("%score%", String.valueOf(var12.getscore(var1))).replaceAll("%role%", String.valueOf(var12.getRole(var1))).replaceAll("%date%", String.valueOf(var20.format(var18)).replaceAll("-", "/")).replaceAll("%map%", var12.getName()).replaceAll("%time%", Main.this.formattominutes(var12.time)).replaceAll("&", "\u00a7"));
                           }
                        }

                        if (Main.this.scoreboards.containsKey(var1.getName())) {
                           ScoreboardManager var15 = (ScoreboardManager)Main.this.scoreboards.get(var1.getName());
                           if (var15.getScoreboard() != var1.getScoreboard()) {
                              var15.toggleScoreboard();
                           }
                        }
                     }
                  }

               }
            }
         }).runTaskTimer(this, (long)this.settings.getConfig().getInt("scoreboard-update-interval"), (long)this.settings.getConfig().getInt("scoreboard-update-interval"));
      }
   }

   public void setSpec(Player var1, Arena var2) {
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.world", var1.getLocation().getWorld().getName());
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.x", var1.getLocation().getX());
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.y", var1.getLocation().getY());
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.z", var1.getLocation().getZ());
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.yaw", (double)var1.getLocation().getYaw());
      this.arenas.getConfig().set("Spectator." + var2.getName() + ".main.lobby.pitch", (double)var1.getLocation().getPitch());
      this.arenas.save();
   }

   public void setup(Player var1) {
      var1.getInventory().setHeldItemSlot(1);
      var1.getInventory().clear();
      var1.getInventory().setArmorContents((ItemStack[])null);
      var1.updateInventory();
      var1.setHealth(20.0D);
      var1.setFoodLevel(20);
      var1.setGameMode(GameMode.ADVENTURE);
      var1.setExp(0.0F);
      var1.setLevel(0);
      var1.setAllowFlight(false);
      var1.setFlying(false);
      var1.setCanPickupItems(true);
      var1.setFireTicks(0);
      Iterator var3 = var1.getActivePotionEffects().iterator();

      while(var3.hasNext()) {
         PotionEffect var2 = (PotionEffect)var3.next();
         var1.removePotionEffect(var2.getType());
      }

      var1.setFallDistance(0.0F);
   }

   public void setUpForMultiMaps(Player var1) {
      if (Arenas.isInArena(var1)) {
         Arena var2 = Arenas.getArena(var1);
         var2.removePlayer(var1, "leave");
      }

      if (Arenas.getArenas().size() > 1) {
         this.setup(var1);
         this.setMap(var1);
         var1.teleport(this.getLobby());
         ItemStack var4 = new ItemStack(Material.getMaterial(this.settings.getConfig().getInt("quit3.item-id")), 1, (short)this.settings.getConfig().getInt("quit3.item-subid"));
         ItemMeta var3 = var4.getItemMeta();
         var3.setDisplayName(this.FormatText(this.settings.getConfig().getString("quit3.item-name")));
         var3.setLore(Arrays.asList(this.FormatText(this.settings.getConfig().getString("quit3.item-lore"))));
         var4.setItemMeta(var3);
         var1.getInventory().setItem(8, var4);
      }

   }

   public void setWait(Player var1, Arena var2) {
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.world", var1.getLocation().getWorld().getName());
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.x", var1.getLocation().getX());
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.y", var1.getLocation().getY());
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.z", var1.getLocation().getZ());
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.yaw", (double)var1.getLocation().getYaw());
      this.arenas.getConfig().set("Wait." + var2.getName() + ".main.lobby.pitch", (double)var1.getLocation().getPitch());
      this.arenas.save();
   }

   public void spawnarmorstand(final Arena var1, final Location var2) {
      if (var1.armor.size() <= 0) {
         final ArmorStand var3 = (ArmorStand)var2.getWorld().spawnEntity(var2, EntityType.ARMOR_STAND);
         var3.setVisible(false);
         var3.setBasePlate(false);
         var3.setGravity(false);
         var3.setArms(true);
         var3.setItemInHand(new ItemStack(Material.BOW));
         var1.bowloc = var2;
         if (!var1.armor.contains(var1)) {
            var1.armor.add(var3);
         }

         (new BukkitRunnable() {
            public void run() {
               if (!var3.isDead() && var3 != null) {
                  Location var1x = var2;
                  var1x.setYaw(var1x.getYaw() + 3.0F);
                  var1x.setPitch(var1x.getPitch() + 3.0F);
                  var3.teleport(var1x);
               } else {
                  if (var1.armor.contains(var3)) {
                     var1.armor.remove(var3);
                  }

                  this.cancel();
               }
            }
         }).runTaskTimer(this, 1L, 1L);
      }
   }

   public int SpawnSize(Arena var1) {
      return this.arenas.getConfig().getConfigurationSection("Spawns." + var1.getName()).getKeys(false).size();
   }

   public int SpawnSize2(Arena var1) {
      return this.arenas.getConfig().getConfigurationSection("Spawns." + var1.getName()).getKeys(false).size();
   }

   public void StartFireworksMurder(final Arena var1) {
      (new BukkitRunnable() {
         int time;

         {
            this.time = Main.this.settings.getConfig().getInt("fireworks-time-in-ticks");
         }

         public void run() {
            --this.time;
            if (this.time <= 0) {
               this.cancel();
            } else {
               if (!Main.this.settings.getConfig().getBoolean("start-fireworks-on-players-location")) {
                  Location var1x = Main.this.getSpawn2(var1, Main.this.getRandom(0, Main.this.SpawnSize2(var1)));
                  Main.this.LaunchFirework(var1x);
               }

               if (Main.this.settings.getConfig().getBoolean("start-fireworks-on-players-location") && var1.murder.size() > 0) {
                  Player var2 = (Player)var1.murder.get(0);
                  if (var2 != null) {
                     Main.this.LaunchFirework(var2.getLocation());
                  }
               }

            }
         }
      }).runTaskTimer(this, (long)this.settings.getConfig().getInt("fireworks-ticks"), (long)this.settings.getConfig().getInt("fireworks-ticks"));
   }

   public void StartFireworksPlayers(final Arena var1) {
      (new BukkitRunnable() {
         int time;

         {
            this.time = Main.this.settings.getConfig().getInt("fireworks-time-in-ticks");
         }

         public void run() {
            --this.time;
            if (this.time <= 0) {
               this.cancel();
            } else {
               if (!Main.this.settings.getConfig().getBoolean("start-fireworks-on-players-location")) {
                  Location var1x = Main.this.getSpawn2(var1, Main.this.getRandom(0, Main.this.SpawnSize2(var1)));
                  Main.this.LaunchFirework(var1x);
               }

               if (Main.this.settings.getConfig().getBoolean("start-fireworks-on-players-location")) {
                  Iterator var2 = var1.getPlayers().iterator();

                  while(var2.hasNext()) {
                     Player var3 = (Player)var2.next();
                     Main.this.LaunchFirework(var3.getLocation());
                  }
               }

            }
         }
      }).runTaskTimer(this, (long)this.settings.getConfig().getInt("fireworks-ticks"), (long)this.settings.getConfig().getInt("fireworks-ticks"));
   }

   public void StartMap() {
      if (!this.startmap) {
         this.startmap = true;
         if (Arenas.getArenas().size() > 1) {
            Iterator var2 = Bukkit.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
               Player var1 = (Player)var2.next();
               var1.sendMessage(this.FormatText(this.messages.getConfig().getString("voting-time-started")));
            }

            (new BukkitRunnable() {
               public void run() {
                  Player var1;
                  Iterator var2;
                  if (Main.this.getHighestVote() != null) {
                     Main.this.bungee = Arenas.getArena(Main.this.getHighestVote());
                     var2 = Bukkit.getOnlinePlayers().iterator();

                     while(var2.hasNext()) {
                        var1 = (Player)var2.next();
                        var1.sendMessage(Main.this.FormatText(Main.this.messages.getConfig().getString("vote-win").replaceAll("%map%", Main.this.getHighestVote())));
                     }
                  }

                  if (Main.this.getHighestVote() == null) {
                     Main.this.bungee = (Arena)Arenas.getArenas().get(0);
                     var2 = Bukkit.getOnlinePlayers().iterator();

                     while(var2.hasNext()) {
                        var1 = (Player)var2.next();
                        var1.sendMessage(Main.this.FormatText(Main.this.messages.getConfig().getString("vote-win").replaceAll("%map%", Main.this.bungee.getName())));
                     }
                  }

                  Main.this.sendPlayers();
               }
            }).runTaskLater(this, (long)(20 * this.settings.getConfig().getInt("vote-time")));
         }

      }
   }
}
