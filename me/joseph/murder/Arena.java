package me.joseph.murder;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.joseph.murder.events.TitleAPI;
import me.joseph.murder.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

public class Arena {
   public ArrayList data = new ArrayList();
   public String Murderer = "None";
   public String Detective = "None";
   public String Hero = "None";
   public ArrayList armor = new ArrayList();
   public ArrayList sword = new ArrayList();
   public ArrayList golds = new ArrayList();
   public ArrayList items = new ArrayList();
   public Location bowloc = null;
   public int time = 0;
   HashMap kills = new HashMap();
   HashMap score = new HashMap();
   public String name;
   public GameState state;
   public Main plugin;
   public ArrayList lists = new ArrayList();
   public ArrayList specs = new ArrayList();
   public ArrayList players = new ArrayList();
   public ArrayList murder = new ArrayList();
   public ArrayList innocents = new ArrayList();
   public ArrayList detective = new ArrayList();
   int countdown = 0;
   boolean start = false;
   public boolean isdead = false;
   public boolean wincheck = false;
   public ArrayList pic = new ArrayList();
   int spawns = 0;

   public Arena(String var1, Main var2) {
      this.name = var1;
      this.state = GameState.LOBBY;
      this.plugin = var2;
      this.spawns = 0;
   }

   public void addkill(Player var1, Integer var2) {
      if (!this.kills.containsKey(var1.getName())) {
         this.kills.put(var1.getName(), var2);
      } else {
         this.kills.put(var1.getName(), ((Integer)this.kills.get(var1.getName())).intValue() + var2.intValue());
      }
   }

   public void addPlayer(Player var1) {
      if (!this.specs.contains(var1)) {
         if (this.getState() != GameState.LOBBY && this.getState() != GameState.STARTING) {
            var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("join-error")));
         } else if (this.players.contains(var1)) {
            var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("already-in-arena")));
         } else if (Arenas.isInArena(var1)) {
            var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("already-in-arena")));
         } else if (this.players.size() >= this.plugin.SpawnSize(this)) {
            var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("arena-full")));
         } else {
            if (this.players.size() < this.plugin.SpawnSize(this)) {
               this.plugin.saveInventory(var1);
               this.players.add(var1);
               Arenas.addArena(var1, this);
               this.plugin.setup(var1);
               var1.teleport(this.plugin.getWait(this));
               ItemStack var2 = new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit2.item-id")), 1, (short)this.plugin.settings.getConfig().getInt("quit2.item-subid"));
               ItemMeta var3 = var2.getItemMeta();
               var3.setDisplayName(this.plugin.FormatText(this.plugin.settings.getConfig().getString("quit2.item-name")));
               var3.setLore(Arrays.asList(this.plugin.FormatText(this.plugin.settings.getConfig().getString("quit2.item-lore"))));
               var2.setItemMeta(var3);
               var1.getInventory().setItem(8, var2);
               var1.updateInventory();
               Iterator var5 = this.getPlayers().iterator();

               Player var4;
               while(var5.hasNext()) {
                  var4 = (Player)var5.next();
                  var4.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("player-join-arena-message").replaceAll("%max%", String.valueOf(this.plugin.SpawnSize(this))).replaceAll("%min%", String.valueOf(this.players.size())).replaceAll("%player%", var1.getName())));
               }

               if (!this.start && this.players.size() >= this.getMinPlayersToStartGame()) {
                  this.countdown = this.plugin.settings.getConfig().getInt("countdown");
                  var5 = this.getPlayers().iterator();

                  while(var5.hasNext()) {
                     var4 = (Player)var5.next();
                     var4.sendMessage(this.plugin.messages.getConfig().getString("game-soon-start-message").replaceAll("&", "\u00a7"));
                  }

                  this.state = GameState.STARTING;
                  (new BukkitRunnable() {
                     public void run() {
                        if (Arena.this.state == GameState.INGAME) {
                           this.cancel();
                        } else {
                           Player var1;
                           Iterator var2;
                           if (Arena.this.players.size() < Arena.this.getMinPlayersToStartGame()) {
                              Arena.this.start = false;
                              Arena.this.countdown = Arena.this.plugin.settings.getConfig().getInt("countdown");
                              var2 = Arena.this.getPlayers().iterator();

                              while(var2.hasNext()) {
                                 var1 = (Player)var2.next();
                                 var1.sendMessage(Arena.this.plugin.messages.getConfig().getString("cancel").replaceAll("&", "\u00a7"));
                              }

                              Arena.this.state = GameState.LOBBY;
                              this.cancel();
                           } else {
                              --Arena.this.countdown;
                              if (Arena.this.countdown <= 5 && Arena.this.countdown > 0) {
                                 for(var2 = Arena.this.getPlayers().iterator(); var2.hasNext(); var1.sendMessage(Arena.this.plugin.messages.getConfig().getString("countdown").replaceAll("&", "\u00a7").replaceAll("%time%", String.valueOf(Arena.this.countdown)))) {
                                    var1 = (Player)var2.next();
                                    TitleAPI.sendTitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), Arena.this.plugin.messages.getConfig().getString("title-countdown").replaceAll("&", "\u00a7").replaceAll("%time%", String.valueOf(Arena.this.countdown)));
                                    TitleAPI.sendSubtitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), Arena.this.plugin.messages.getConfig().getString("subtitle-countdown").replaceAll("&", "\u00a7").replaceAll("%time%", String.valueOf(Arena.this.countdown)));
                                    if (Arena.this.plugin.settings.getConfig().getBoolean("enable-sounds")) {
                                       var1.playSound(var1.getLocation(), Sound.valueOf(Arena.this.plugin.settings.getConfig().getString("COUNT_DOWN_SOUND")), 1.0F, 1.0F);
                                    }
                                 }
                              }

                              if (Arena.this.countdown <= 0) {
                                 Arena.this.start();
                                 this.cancel();
                              }
                           }
                        }
                     }
                  }).runTaskTimer(this.plugin, 20L, 20L);
                  this.start = true;
               }
            }

         }
      }
   }

   public void addscore(Player var1, Integer var2, String var3) {
      if (!this.score.containsKey(var1.getName())) {
         this.score.put(var1.getName(), var2);
         var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("receive-score-message").replaceAll("%score%", "" + var2).replaceAll("%reason%", var3)));
         if (this.plugin.getPlayerData(var1) != null) {
            this.plugin.getPlayerData(var1).addscore(var2);
         }

      } else {
         this.score.put(var1.getName(), ((Integer)this.score.get(var1.getName())).intValue() + var2.intValue());
         var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("receive-score-message").replaceAll("%score%", "" + var2).replaceAll("%reason%", var3)));
         this.plugin.getPlayerData(var1).addscore(var2);
      }
   }

   public void clearplayer(Player var1) {
      if (this.murder.contains(var1)) {
         this.murder.remove(var1);
      }

      if (this.innocents.contains(var1)) {
         this.innocents.remove(var1);
      }

      if (this.detective.contains(var1)) {
         this.detective.remove(var1);
      }

   }

   public Arena getArena() {
      return this;
   }

   public ArmorStand[] getArmor() {
      return (ArmorStand[])this.armor.toArray(new ArmorStand[this.armor.size()]);
   }

   public String getBowState() {
      return this.IsBowDropped() ? this.plugin.FormatText(this.plugin.messages.getConfig().getString("bow-dropped")) : this.plugin.FormatText(this.plugin.messages.getConfig().getString("bow-not-dropped"));
   }

   public Player[] getDetectives() {
      return (Player[])this.detective.toArray(new Player[this.detective.size()]);
   }

   public Entity[] getGolds() {
      return (Entity[])this.golds.toArray(new Entity[this.golds.size()]);
   }

   public Player[] getInnocents() {
      return (Player[])this.innocents.toArray(new Player[this.innocents.size()]);
   }

   public FlyingItems[] getItems() {
      return (FlyingItems[])this.items.toArray(new FlyingItems[this.items.size()]);
   }

   public Integer getkill(Player var1) {
      return !this.kills.containsKey(var1.getName()) ? Integer.valueOf(0) : (Integer)this.kills.get(var1.getName());
   }

   public int getMinPlayersToStartGame() {
      return this.plugin.arenas.getConfig().contains("MinPlayers." + this.getName()) ? this.plugin.arenas.getConfig().getInt("MinPlayers." + this.getName()) : 3;
   }

   public Player[] getMurderers() {
      return (Player[])this.murder.toArray(new Player[this.murder.size()]);
   }

   public String getName() {
      return this.name;
   }

   public ArrayList getPlayers() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = this.players.iterator();

      Player var2;
      while(var3.hasNext()) {
         var2 = (Player)var3.next();
         var1.add(var2);
      }

      var3 = this.specs.iterator();

      while(var3.hasNext()) {
         var2 = (Player)var3.next();
         var1.add(var2);
      }

      return var1;
   }

   public Player[] getPlayers2() {
      return (Player[])this.players.toArray(new Player[this.players.size()]);
   }

   public int getRandom(int var1, int var2) {
      Random var3 = new Random();
      return var3.nextInt(var2 - var1 + 1) + var1;
   }

   public String getRole(Player var1) {
      if (this.getType(var1) == PlayerType.Murderer) {
         return this.plugin.messages.getConfig().getString("murder-role");
      } else if (this.getType(var1) == PlayerType.Detective) {
         return this.plugin.messages.getConfig().getString("detective-role");
      } else if (this.getType(var1) == PlayerType.Innocents) {
         return this.plugin.messages.getConfig().getString("innocent-role");
      } else {
         return this.getType(var1) == PlayerType.None ? this.plugin.messages.getConfig().getString("dead-role") : this.plugin.messages.getConfig().getString("dead-role");
      }
   }

   public Integer getscore(Player var1) {
      return !this.score.containsKey(var1.getName()) ? Integer.valueOf(0) : (Integer)this.score.get(var1.getName());
   }

   public Player[] getSpectators() {
      return (Player[])this.specs.toArray(new Player[this.specs.size()]);
   }

   public GameState getState() {
      return this.state;
   }

   public ArmorStand[] getSwords() {
      return (ArmorStand[])this.sword.toArray(new ArmorStand[this.sword.size()]);
   }

   public PlayerType getType(Player var1) {
      if (this.innocents.contains(var1)) {
         return PlayerType.Innocents;
      } else if (this.murder.contains(var1)) {
         return PlayerType.Murderer;
      } else if (this.detective.contains(var1)) {
         return PlayerType.Detective;
      } else {
         return this.specs.contains(var1) ? PlayerType.None : PlayerType.None;
      }
   }

   public boolean IsBowDropped() {
      return this.isdead;
   }

   public boolean isin(Player var1) {
      return this.innocents.contains(var1);
   }

   public void RandomDetective() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = this.getPlayers().iterator();

      Player var2;
      while(var3.hasNext()) {
         var2 = (Player)var3.next();
         var1.add(var2);
      }

      var2 = (Player)var1.get((new Random()).nextInt(var1.size()));
      if (!this.isin(var2)) {
         this.setDetective(var2);
      }

   }

   public void RandomMurderer() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = this.getPlayers().iterator();

      Player var2;
      while(var3.hasNext()) {
         var2 = (Player)var3.next();
         var1.add(var2);
      }

      var2 = (Player)var1.get((new Random()).nextInt(var1.size()));
      if (!this.isin(var2)) {
         this.setMurder(var2);
      }

   }

   public void removePlayer(final Player var1, String var2) {
      if (!Arenas.isInArena(var1)) {
         var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("not-in-arena")));
      } else if (!this.players.contains(var1)) {
         var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("not-in-arena")));
      } else {
         Player var3;
         Iterator var4;
         int var5;
         Player[] var6;
         int var7;
         if (var2.equalsIgnoreCase("leave")) {
            if (this.getState() == GameState.LOBBY || this.getState() == GameState.STARTING) {
               var4 = this.getPlayers().iterator();

               while(var4.hasNext()) {
                  var3 = (Player)var4.next();
                  var5 = this.players.size() - 1;
                  var3.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("player-leave-arena-message").replaceAll("%max%", String.valueOf(this.plugin.SpawnSize(this))).replaceAll("%min%", String.valueOf(var5)).replaceAll("%player%", var1.getName())));
               }
            }

            this.players.remove(var1);
            Arenas.removeArena(var1);
            this.plugin.setup(var1);
            this.plugin.restoreInventory(var1);
            if ((this.getState() == GameState.INGAME || this.getState() == GameState.STARTING) && (this.players.size() <= 0 || this.getPlayers().size() <= 0)) {
               this.stop("reload");
            }

            if (!this.isdead) {
               if (this.getType(var1) != PlayerType.Detective && this.lists.contains(var1.getName())) {
                  this.plugin.spawnarmorstand(this, var1.getLocation());
                  this.isdead = true;
                  var5 = (var6 = this.getInnocents()).length;

                  for(var7 = 0; var7 < var5; ++var7) {
                     var3 = var6[var7];
                     this.plugin.setCompass(var3);
                  }

                  var4 = this.getPlayers().iterator();

                  while(var4.hasNext()) {
                     var3 = (Player)var4.next();
                     var3.sendMessage(this.plugin.messages.getConfig().getString("bow-dropped").replaceAll("&", "\u00a7"));
                  }
               }

               if (this.getType(var1) == PlayerType.Detective) {
                  this.plugin.spawnarmorstand(this, var1.getLocation());
                  this.isdead = true;
                  var5 = (var6 = this.getInnocents()).length;

                  for(var7 = 0; var7 < var5; ++var7) {
                     var3 = var6[var7];
                     this.plugin.setCompass(var3);
                  }

                  var4 = this.getPlayers().iterator();

                  while(var4.hasNext()) {
                     var3 = (Player)var4.next();
                     var3.sendMessage(this.plugin.messages.getConfig().getString("detective-die").replaceAll("&", "\u00a7"));
                  }
               }
            }

            if (this.getType(var1) == PlayerType.Murderer && !this.wincheck) {
               this.stop("reload");
            }

            if (this.players.size() == 1 && this.getType((Player)this.players.get(0)) == PlayerType.Murderer) {
               this.win("m");
            }

            this.clearplayer(var1);
            if (this.plugin.settings.getConfig().getBoolean("send-stats-message-on-leave")) {
               var1.chat("/murder stats");
            }

            if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
               var1.teleport(this.plugin.getLobby());
            }

            if (this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
               ByteArrayDataOutput var8 = ByteStreams.newDataOutput();
               var8.writeUTF("Connect");
               var8.writeUTF(this.plugin.getConfig().getString("lobby-server"));
               var1.sendPluginMessage(this.plugin, "BungeeCord", var8.toByteArray());
            }
         }

         if (var2.equalsIgnoreCase("death")) {
            if (!this.isdead) {
               if (this.getType(var1) != PlayerType.Detective && this.lists.contains(var1.getName())) {
                  this.plugin.spawnarmorstand(this, var1.getLocation());
                  this.isdead = true;
                  var5 = (var6 = this.getInnocents()).length;

                  for(var7 = 0; var7 < var5; ++var7) {
                     var3 = var6[var7];
                     this.plugin.setCompass(var3);
                  }

                  var4 = this.getPlayers().iterator();

                  while(var4.hasNext()) {
                     var3 = (Player)var4.next();
                     var3.sendMessage(this.plugin.messages.getConfig().getString("bow-dropped").replaceAll("&", "\u00a7"));
                  }
               }

               if (this.getType(var1) == PlayerType.Detective) {
                  this.plugin.spawnarmorstand(this, var1.getLocation());
                  this.isdead = true;
                  var5 = (var6 = this.getInnocents()).length;

                  for(var7 = 0; var7 < var5; ++var7) {
                     var3 = var6[var7];
                     this.plugin.setCompass(var3);
                  }

                  var4 = this.getPlayers().iterator();

                  while(var4.hasNext()) {
                     var3 = (Player)var4.next();
                     var3.sendMessage(this.plugin.messages.getConfig().getString("detective-die").replaceAll("&", "\u00a7"));
                  }
               }
            }

            this.plugin.setup(var1);
            var1.setHealth(20.0D);
            this.players.remove(var1);
            this.specs.add(var1);
            var1.setGameMode(GameMode.ADVENTURE);
            var1.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
               public void run() {
                  var1.teleport(Arena.this.plugin.getSpec(Arenas.getArena(Arena.this.name)));
                  ItemStack var1x = new ItemStack(Material.getMaterial(Arena.this.plugin.settings.getConfig().getInt("quit.item-id")), 1, (short)Arena.this.plugin.settings.getConfig().getInt("quit.item-subid"));
                  ItemMeta var2 = var1x.getItemMeta();
                  var2.setDisplayName(Arena.this.plugin.FormatText(Arena.this.plugin.settings.getConfig().getString("quit.item-name")));
                  var2.setLore(Arrays.asList(Arena.this.plugin.FormatText(Arena.this.plugin.settings.getConfig().getString("quit.item-lore"))));
                  var1x.setItemMeta(var2);
                  var1.getInventory().setItem(8, var1x);
                  ItemStack var3 = new ItemStack(Material.getMaterial(Arena.this.plugin.settings.getConfig().getInt("spec.item-id")), 1, (short)Arena.this.plugin.settings.getConfig().getInt("spec.item-subid"));
                  ItemMeta var4 = var3.getItemMeta();
                  var4.setDisplayName(Arena.this.plugin.FormatText(Arena.this.plugin.settings.getConfig().getString("spec.item-name")));
                  var3.setItemMeta(var4);
                  var1.getInventory().setItem(0, var3);
                  if (!Arena.this.plugin.getConfig().getBoolean("bungee")) {
                     ItemStack var5 = new ItemStack(Material.getMaterial(Arena.this.plugin.settings.getConfig().getInt("rejoin.item-id")), 1, (short)Arena.this.plugin.settings.getConfig().getInt("rejoin.item-subid"));
                     ItemMeta var6 = var5.getItemMeta();
                     var6.setDisplayName(Arena.this.plugin.FormatText(Arena.this.plugin.settings.getConfig().getString("rejoin.item-name")));
                     var5.setItemMeta(var6);
                     var1.getInventory().setItem(7, var5);
                  }

                  var1.updateInventory();
                  Iterator var9 = Arena.this.getPlayers().iterator();

                  while(var9.hasNext()) {
                     Player var8 = (Player)var9.next();
                     ScoreboardManager var7 = (ScoreboardManager)Arena.this.plugin.scoreboards.get(var8.getName());
                     if (var7 != null && var7.getScoreboard().getTeam("team") != null && var7.getScoreboard().getTeam("team").hasPlayer(var1)) {
                        var7.getScoreboard().getTeam("team").removePlayer(var1);
                     }
                  }

               }
            }, (long)this.plugin.settings.getConfig().getInt("give-spectate-item-after-ticks"));
            var1.setAllowFlight(true);
            var1.setFlying(true);
            var1.sendMessage(this.plugin.FormatText(this.plugin.messages.getConfig().getString("spectate-message")));
            if (this.players.size() == 0 || this.getPlayers().size() == 0) {
               Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                  public void run() {
                     Arena.this.stop("reload");
                  }
               }, 60L);
               return;
            }

            if (this.getType(var1) == PlayerType.Murderer) {
               this.clearplayer(var1);
               if (this.players.size() > 0) {
                  this.win("p");
               }

               if (this.players.size() == 0 || this.getPlayers().size() == 0) {
                  this.stop("reload");
               }

               return;
            }

            if (this.players.size() == 1 && this.getType((Player)this.players.get(0)) == PlayerType.Murderer) {
               this.win("m");
            }
         }

         this.clearplayer(var1);
      }
   }

   public void reset() {
      this.state = GameState.LOBBY;
      this.countdown = 0;
      this.spawns = 0;
      this.isdead = false;
      this.start = false;
      this.wincheck = false;
      this.Murderer = "None";
      this.Detective = "None";
      this.Hero = "None";
      this.score.clear();
      this.kills.clear();
      this.lists.clear();
      this.pic.clear();
      this.bowloc = null;
   }

   public void setDetective(Player var1) {
      if (!this.murder.contains(var1) && !this.detective.contains(var1) && !this.innocents.contains(var1)) {
         this.clearplayer(var1);
         var1.getInventory().setArmorContents((ItemStack[])null);
         var1.getInventory().clear();
         this.detective.add(var1);
         TitleAPI.sendTitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-detective-title").replaceAll("&", "\u00a7"));
         TitleAPI.sendSubtitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-detective-subtitle").replaceAll("&", "\u00a7"));
         if (!var1.getInventory().contains(new ItemStack(Material.BOW))) {
            var1.getInventory().addItem(new ItemStack[]{new ItemStack(Material.BOW)});
         }

         ItemStack var2 = new ItemStack(Material.ARROW, 64);
         var1.getInventory().setItem(9, var2);
         var1.updateInventory();
      } else {
         this.RandomDetective();
      }
   }

   public void setInnocent(Player var1) {
      if (!this.murder.contains(var1) && !this.detective.contains(var1) && !this.innocents.contains(var1)) {
         this.clearplayer(var1);
         var1.getInventory().setArmorContents((ItemStack[])null);
         var1.getInventory().clear();
         this.innocents.add(var1);
         TitleAPI.sendTitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-innocent-title").replaceAll("&", "\u00a7"));
         TitleAPI.sendSubtitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-innocent-subtitle").replaceAll("&", "\u00a7"));
         var1.updateInventory();
      }
   }

   public void SetInnocents() {
      Iterator var2 = this.getPlayers().iterator();

      while(var2.hasNext()) {
         Player var1 = (Player)var2.next();
         if (!this.isin(var1)) {
            this.setInnocent(var1);
         }
      }

   }

   public void setMurder(final Player var1) {
      if (!this.murder.contains(var1) && !this.detective.contains(var1) && !this.innocents.contains(var1)) {
         this.clearplayer(var1);
         var1.getInventory().setArmorContents((ItemStack[])null);
         var1.getInventory().clear();
         this.murder.add(var1);
         TitleAPI.sendTitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-murderer-title").replaceAll("&", "\u00a7"));
         TitleAPI.sendSubtitle(var1, Integer.valueOf(0), Integer.valueOf(60), Integer.valueOf(0), this.plugin.messages.getConfig().getString("you-are-murderer-subtitle").replaceAll("&", "\u00a7"));
         final ItemStack var2 = new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("murderer-weapon.item-id")));
         var2.setDurability((short)this.plugin.settings.getConfig().getInt("murderer-weapon.item-subid"));
         ItemMeta var3 = var2.getItemMeta();
         var3.setDisplayName(this.plugin.settings.getConfig().getString("murderer-weapon.item-name").replaceAll("&", "\u00a7"));
         ArrayList var4 = new ArrayList();
         var4.add(this.plugin.settings.getConfig().getString("murderer-weapon.item-lore").replaceAll("&", "\u00a7"));
         var3.setLore(var4);
         var2.setItemMeta(var3);
         (new BukkitRunnable() {
            public void run() {
               if (Arena.this.getState() == GameState.INGAME && !var1.isDead() && var1 != null && !Arena.this.specs.contains(var1)) {
                  var1.getInventory().setItem(0, var2);
                  var1.getInventory().setHeldItemSlot(8);
                  Iterator var2x = Arena.this.getPlayers().iterator();

                  while(var2x.hasNext()) {
                     Player var1x = (Player)var2x.next();
                     var1x.sendMessage(Arena.this.plugin.messages.getConfig().getString("murder-receive-sword-message").replaceAll("&", "\u00a7"));
                  }
               }

            }
         }).runTaskLater(this.plugin, (long)(this.plugin.settings.getConfig().getInt("receive-sword-after") * 20));
         var1.updateInventory();
      } else {
         this.RandomMurderer();
      }
   }

   public void SetUp() {
      this.RandomMurderer();
      if (this.players.size() > 1) {
         this.RandomDetective();
         this.SetInnocents();
      }

      if (this.murder.size() > 0) {
         this.Murderer = ((Player)this.murder.get(0)).getName();
      }

      if (this.detective.size() > 0) {
         this.Detective = ((Player)this.detective.get(0)).getName();
      }

      (new BukkitRunnable() {
         public void run() {
            if (Arena.this.murder.size() > 0) {
               Arena.this.Murderer = ((Player)Arena.this.murder.get(0)).getName();
            }

            if (Arena.this.detective.size() > 0) {
               Arena.this.Detective = ((Player)Arena.this.detective.get(0)).getName();
            }

         }
      }).runTaskLater(this.plugin, 60L);
   }

   public void start() {
      if (this.state == GameState.STARTING || this.state == GameState.LOBBY) {
         if (this.players.size() != 0 && this.getPlayers().size() != 0) {
            this.plugin.getSpec(this).getWorld().setThundering(false);
            this.plugin.getSpec(this).getWorld().setStorm(false);
            this.state = GameState.INGAME;
            (new BukkitRunnable() {
               public void run() {
                  if (Arena.this.getState() != GameState.INGAME) {
                     this.cancel();
                  } else {
                     Iterator var2 = Arena.this.getPlayers().iterator();

                     while(true) {
                        Player var1;
                        do {
                           do {
                              if (!var2.hasNext()) {
                                 if (Arena.this.time > 0) {
                                    --Arena.this.time;
                                 }

                                 if (Arena.this.plugin.settings.getConfig().getBoolean("tracking-compass") && Arena.this.time <= Arena.this.plugin.settings.getConfig().getInt("time-to-give-tracker") && Arena.this.murder.size() > 0) {
                                    var1 = (Player)Arena.this.murder.get(0);
                                    if (var1 != null) {
                                       ItemStack var13 = new ItemStack(Material.getMaterial(Arena.this.plugin.settings.getConfig().getInt("murder-track.item-id")));
                                       var13.setDurability((short)Arena.this.plugin.settings.getConfig().getInt("murder-track.item-subid"));
                                       ItemMeta var14 = var13.getItemMeta();
                                       var14.setDisplayName(Arena.this.plugin.settings.getConfig().getString("murder-track.item-name").replaceAll("&", "\u00a7"));
                                       ArrayList var15 = new ArrayList();
                                       var15.add(Arena.this.plugin.settings.getConfig().getString("murder-track.item-lore").replaceAll("&", "\u00a7"));
                                       var14.setLore(var15);
                                       var13.setItemMeta(var14);
                                       if (!var1.getInventory().contains(var13)) {
                                          var1.getInventory().setItem(4, var13);
                                          var1.updateInventory();
                                       }
                                    }
                                 }

                                 if (Arena.this.time <= 0 && (Arena.this.murder.size() > 0 || Arena.this.detective.size() > 0)) {
                                    Arena.this.win("p");
                                 }

                                 return;
                              }

                              var1 = (Player)var2.next();
                           } while(Arena.this.getType(var1) != PlayerType.Innocents);
                        } while(!Arena.this.IsBowDropped());

                        Entity[] var6;
                        int var5 = (var6 = Arena.this.plugin.getNearbyEntities(var1.getLocation(), Arena.this.plugin.settings.getConfig().getInt("bow-pickup-radius"))).length;

                        for(int var4 = 0; var4 < var5; ++var4) {
                           Entity var3 = var6[var4];
                           if (!var1.getWorld().getName().equalsIgnoreCase(var3.getWorld().getName())) {
                              return;
                           }

                           if (var3 instanceof ArmorStand) {
                              ArmorStand var7 = (ArmorStand)var3;
                              if (var7.getItemInHand().getType() == Material.BOW) {
                                 if (Arena.this.specs.contains(var1)) {
                                    return;
                                 }

                                 if (Arena.this.pic.contains(var1.getName())) {
                                    return;
                                 }

                                 Arena.this.pic.add(var1.getName());
                                 var1.getInventory().clear();
                                 if (!var1.getInventory().contains(new ItemStack(Material.BOW))) {
                                    var1.getInventory().addItem(new ItemStack[]{new ItemStack(Material.BOW)});
                                 }

                                 ItemStack var8 = new ItemStack(Material.ARROW, 64);
                                 var1.getInventory().setItem(9, var8);
                                 var1.updateInventory();
                                 if (Arena.this.plugin.settings.getConfig().getBoolean("enable-sounds")) {
                                    var1.playSound(var1.getLocation(), Sound.valueOf(Arena.this.plugin.settings.getConfig().getString("PICK_UP")), 1.0F, 1.0F);
                                 }

                                 Player[] var12;
                                 int var11 = (var12 = Arena.this.getInnocents()).length;

                                 Player var9;
                                 for(int var10 = 0; var10 < var11; ++var10) {
                                    var9 = var12[var10];
                                    Arena.this.plugin.removeCompass(var9);
                                 }

                                 Iterator var16 = Arena.this.getPlayers().iterator();

                                 while(var16.hasNext()) {
                                    var9 = (Player)var16.next();
                                    var9.sendMessage(Arena.this.plugin.messages.getConfig().getString("pickup-bow").replaceAll("&", "\u00a7"));
                                 }

                                 if (Arena.this.isdead) {
                                    Arena.this.isdead = false;
                                 }

                                 if (Arena.this.pic.contains(var1.getName())) {
                                    Arena.this.pic.remove(var1.getName());
                                 }

                                 if (!Arena.this.lists.contains(var1.getName())) {
                                    Arena.this.lists.add(var1.getName());
                                 }

                                 if (Arena.this.bowloc != null) {
                                    Arena.this.bowloc = null;
                                 }

                                 if (Arena.this.armor.contains(var7)) {
                                    Arena.this.armor.remove(var7);
                                 }

                                 var7.remove();
                              }
                           }
                        }
                     }
                  }
               }
            }).runTaskTimer(this.plugin, 20L, 20L);
            (new BukkitRunnable() {
               public void run() {
                  if (Arena.this.getState() != GameState.INGAME) {
                     this.cancel();
                  } else {
                     Arena.this.plugin.DropGold(Arena.this.getArena());
                     ArmorStand[] var4;
                     int var3 = (var4 = Arena.this.getArmor()).length;

                     ArmorStand var1;
                     int var2;
                     for(var2 = 0; var2 < var3; ++var2) {
                        var1 = var4[var2];
                        if ((var1.isDead() || var1 == null) && Arena.this.armor.contains(var1)) {
                           Arena.this.armor.remove(var1);
                        }
                     }

                     Entity[] var6;
                     var3 = (var6 = Arena.this.getGolds()).length;

                     for(var2 = 0; var2 < var3; ++var2) {
                        Entity var5 = var6[var2];
                        if ((var5.isDead() || var5 == null) && Arena.this.golds.contains(var5)) {
                           Arena.this.golds.remove(var5);
                        }
                     }

                     var3 = (var4 = Arena.this.getSwords()).length;

                     for(var2 = 0; var2 < var3; ++var2) {
                        var1 = var4[var2];
                        if ((var1.isDead() || var1 == null) && Arena.this.sword.contains(var1)) {
                           Arena.this.sword.remove(var1);
                        }
                     }

                  }
               }
            }).runTaskTimer(this.plugin, (long)this.plugin.settings.getConfig().getInt("gold-drop-interval"), (long)this.plugin.settings.getConfig().getInt("gold-drop-interval"));
            this.SetUp();
            if (!this.plugin.arenas.getConfig().contains("Time." + this.getName())) {
               this.time = 300;
            }

            if (this.plugin.arenas.getConfig().contains("Time." + this.getName())) {
               this.time = this.plugin.arenas.getConfig().getInt("Time." + this.getName());
            }

            Player var1;
            for(Iterator var2 = this.getPlayers().iterator(); var2.hasNext(); var1.updateInventory()) {
               var1 = (Player)var2.next();
               var1.sendMessage(this.plugin.messages.getConfig().getString("game-start-message").replaceAll("&", "\u00a7"));
               var1.teleport(this.plugin.getSpawn(this.getArena(), this.spawns));
               if (!this.plugin.scoreboards.containsKey(var1)) {
                  this.plugin.setScoreboard(var1);
               }

               ++this.spawns;
               if (var1.getInventory().contains(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit2.item-id")))) {
                  var1.getInventory().remove(new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit2.item-id"))));
               }

               if (var1.getInventory().contains(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit.item-id")))) {
                  var1.getInventory().remove(new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit.item-id"))));
               }

               if (var1.getInventory().contains(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit3.item-id")))) {
                  var1.getInventory().remove(new ItemStack(Material.getMaterial(this.plugin.settings.getConfig().getInt("quit3.item-id"))));
               }
            }

         }
      }
   }

   public void stop(String var1) {
      CorpseData var2;
      Iterator var3;
      Entity[] var5;
      Player var6;
      Entity var7;
      FlyingItems var8;
      ArmorStand var9;
      int var10;
      int var12;
      FlyingItems[] var13;
      ArmorStand[] var14;
      if (var1.equalsIgnoreCase("stop")) {
         if (this.data.size() > 0) {
            var3 = this.data.iterator();

            while(var3.hasNext()) {
               var2 = (CorpseData)var3.next();
               CorpseAPI.removeCorpse(var2);
            }
         }

         if (this.getPlayers().size() > 0) {
            var3 = this.getPlayers().iterator();

            while(var3.hasNext()) {
               var6 = (Player)var3.next();
               if (this.plugin.getConfig().getBoolean("update-data-on-game-end") && this.plugin.getPlayerData(var6) != null) {
                  PlayerData var4 = this.plugin.getPlayerData(var6);
                  if (!this.plugin.getConfig().getBoolean("mysql")) {
                     this.plugin.api.setNonSQLData(var4.p, var4.getkill().intValue(), var4.getdeaths().intValue(), var4.getloses().intValue(), var4.getwins().intValue(), var4.getscore().intValue());
                  }

                  if (this.plugin.getConfig().getBoolean("mysql")) {
                     this.plugin.api.setSQLData(var4.p, var4.getkill().intValue(), var4.getdeaths().intValue(), var4.getloses().intValue(), var4.getwins().intValue(), var4.getscore().intValue());
                  }
               }

               this.plugin.setup(var6);
               if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                  var6.teleport(this.plugin.getLobby());
               }

               if (this.players.contains(var6)) {
                  this.players.remove(var6);
               }

               if (Arenas.isInArena(var6)) {
                  Arenas.removeArena(var6);
               }

               this.plugin.restoreInventory(var6);
               if (this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                  ByteArrayDataOutput var11 = ByteStreams.newDataOutput();
                  var11.writeUTF("Connect");
                  var11.writeUTF(this.plugin.getConfig().getString("lobby-server"));
                  if (this.plugin.isEnabled()) {
                     var6.sendPluginMessage(this.plugin, "BungeeCord", var11.toByteArray());
                  }
               }
            }
         }

         this.innocents.clear();
         this.murder.clear();
         this.detective.clear();
         if (this.golds.size() > 0) {
            var12 = (var5 = this.getGolds()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var7 = var5[var10];
               if (!var7.isDead() && var7 != null) {
                  var7.remove();
               }

               this.golds.remove(var7);
            }
         }

         if (this.items.size() > 0) {
            var12 = (var13 = this.getItems()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var8 = var13[var10];
               if (var8 != null) {
                  var8.remove();
               }

               this.items.remove(var8);
            }
         }

         if (this.sword.size() > 0) {
            var12 = (var14 = this.getSwords()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var9 = var14[var10];
               if (!var9.isDead() && var9 != null) {
                  var9.remove();
               }

               this.sword.remove(var9);
            }
         }

         if (this.armor.size() > 0) {
            var12 = (var14 = this.getArmor()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var9 = var14[var10];
               if (!var9.isDead() && var9 != null) {
                  var9.remove();
               }

               this.armor.remove(var9);
            }
         }

         this.reset();
         this.players.clear();
         this.specs.clear();
      }

      if (var1.equalsIgnoreCase("reload")) {
         if (this.data.size() > 0) {
            var3 = this.data.iterator();

            while(var3.hasNext()) {
               var2 = (CorpseData)var3.next();
               CorpseAPI.removeCorpse(var2);
            }
         }

         if (this.getPlayers().size() > 0) {
            var3 = this.getPlayers().iterator();

            while(var3.hasNext()) {
               var6 = (Player)var3.next();
               var6.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
               this.plugin.setup(var6);
               if (!this.plugin.getConfig().getBoolean("send-to-server-on-leave")) {
                  var6.teleport(this.plugin.getLobby());
               }

               if (this.players.contains(var6)) {
                  this.players.remove(var6);
               }

               this.plugin.restoreInventory(var6);
               if (this.plugin.settings.getConfig().getBoolean("send-stats-message-on-leave")) {
                  var6.chat("/murder stats");
               }

               if (Arenas.isInArena(var6)) {
                  Arenas.removeArena(var6);
               }
            }
         }

         this.innocents.clear();
         this.murder.clear();
         this.detective.clear();
         if (this.golds.size() > 0) {
            var12 = (var5 = this.getGolds()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var7 = var5[var10];
               if (!var7.isDead() && var7 != null) {
                  var7.remove();
               }

               this.golds.remove(var7);
            }
         }

         if (this.items.size() > 0) {
            var12 = (var13 = this.getItems()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var8 = var13[var10];
               if (var8 != null) {
                  var8.remove();
               }

               this.items.remove(var8);
            }
         }

         if (this.sword.size() > 0) {
            var12 = (var14 = this.getSwords()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var9 = var14[var10];
               if (!var9.isDead() && var9 != null) {
                  var9.remove();
               }

               this.sword.remove(var9);
            }
         }

         if (this.armor.size() > 0) {
            var12 = (var14 = this.getArmor()).length;

            for(var10 = 0; var10 < var12; ++var10) {
               var9 = var14[var10];
               if (!var9.isDead() && var9 != null) {
                  var9.remove();
               }

               this.armor.remove(var9);
            }
         }

         (new BukkitRunnable() {
            public void run() {
               if (Arena.this.plugin.getConfig().getBoolean("bungee")) {
                  if (Arenas.getArenas().size() == 1) {
                     Arena var5 = (Arena)Arenas.getArenas().get(0);
                     Player var6;
                     if (Bukkit.getOnlinePlayers().size() > 0) {
                        for(Iterator var7 = Bukkit.getOnlinePlayers().iterator(); var7.hasNext(); var5.addPlayer(var6)) {
                           var6 = (Player)var7.next();
                           if (var6.isDead() || var6.getHealth() == 0.0D) {
                              var6.setHealth(20.0D);
                           }
                        }
                     }

                     return;
                  }

                  if (Arenas.getArenas().size() > 1) {
                     if (Bukkit.getOnlinePlayers().size() > 0) {
                        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

                        while(var2.hasNext()) {
                           Player var1 = (Player)var2.next();
                           if (var1.isDead() || var1.getHealth() == 0.0D) {
                              var1.setHealth(20.0D);
                           }

                           Arena.this.plugin.setUpForMultiMaps(var1);
                           Arena.this.plugin.bungee = null;
                           Arena.this.plugin.point.clear();
                           Arena.this.plugin.votes.clear();
                           Iterator var4 = Arenas.getArenas().iterator();

                           while(var4.hasNext()) {
                              Arena var3 = (Arena)var4.next();
                              Arena.this.plugin.point.put(var3.getName(), Integer.valueOf(0));
                           }
                        }
                     }

                     (new BukkitRunnable() {
                        public void run() {
                           if (Bukkit.getOnlinePlayers().size() >= Arena.this.plugin.settings.getConfig().getInt("min-players-to-start-bungee")) {
                              Arena.this.plugin.startmap = false;
                              Arena.this.plugin.StartMap();
                              this.cancel();
                           }
                        }
                     }).runTaskTimer(Arena.this.plugin, 20L, 20L);
                  }
               }

            }
         }).runTaskLater(this.plugin, 60L);
         this.players.clear();
         this.specs.clear();
         this.reset();
      }

   }

   public void win(final String var1) {
      if (!this.wincheck) {
         this.wincheck = true;
         (new BukkitRunnable() {
            public void run() {
               Player var1x;
               int var2;
               int var3;
               Player[] var4;
               int var7;
               int var8;
               if (!var1.equalsIgnoreCase("m")) {
                  Arena.this.plugin.StartFireworksPlayers(Arena.this.getArena());
                  var3 = (var4 = Arena.this.getInnocents()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addwins(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.winreward(var1x);
                  }

                  var3 = (var4 = Arena.this.getDetectives()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addwins(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.winreward(var1x);
                  }

                  var3 = (var4 = Arena.this.getMurderers()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addlose(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.losereward(var1x);
                  }

                  if (Bukkit.getPlayer(Arena.this.Hero) != null) {
                     Arena.this.plugin.api.heroreward(Bukkit.getPlayer(Arena.this.Hero));
                  }

                  Iterator var17 = Arena.this.getPlayers().iterator();

                  while(var17.hasNext()) {
                     var1x = (Player)var17.next();
                     List var14 = Arena.this.plugin.messages.getConfig().getStringList("innocents-won-message");

                     String var15;
                     int var16;
                     for(Iterator var13 = var14.iterator(); var13.hasNext(); var1x.sendMessage(var15.replaceAll("%mscore%", String.valueOf(var7)).replaceAll("%dscore%", String.valueOf(var16)).replaceAll("%hscore%", String.valueOf(var8)).replaceAll("&", "\u00a7").replaceAll("%murderer%", Arena.this.Murderer).replaceAll("%detective%", Arena.this.Detective).replaceAll("%hero%", Arena.this.Hero))) {
                        var15 = (String)var13.next();
                        var16 = 0;
                        if (Bukkit.getPlayer(Arena.this.Detective) != null) {
                           var16 = Arena.this.getscore(Bukkit.getPlayer(Arena.this.Detective)).intValue();
                        }

                        var7 = 0;
                        if (Bukkit.getPlayer(Arena.this.Murderer) != null) {
                           var7 = Arena.this.getscore(Bukkit.getPlayer(Arena.this.Murderer)).intValue();
                        }

                        var8 = 0;
                        if (Bukkit.getPlayer(Arena.this.Hero) != null) {
                           var8 = Arena.this.getscore(Bukkit.getPlayer(Arena.this.Hero)).intValue();
                        }
                     }
                  }

                  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Arena.this.plugin, new Runnable() {
                     public void run() {
                        Arena.this.stop("reload");
                     }
                  }, (long)(Arena.this.plugin.settings.getConfig().getInt("stop-arena-after-win-time") * 20));
               } else if (var1.equalsIgnoreCase("m")) {
                  Arena.this.plugin.StartFireworksMurder(Arena.this.getArena());
                  var3 = (var4 = Arena.this.getInnocents()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addlose(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.losereward(var1x);
                  }

                  var3 = (var4 = Arena.this.getDetectives()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addlose(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.losereward(var1x);
                  }

                  var3 = (var4 = Arena.this.getMurderers()).length;

                  for(var2 = 0; var2 < var3; ++var2) {
                     var1x = var4[var2];
                     if (Arena.this.plugin.getPlayerData(var1x) != null) {
                        Arena.this.plugin.getPlayerData(var1x).addwins(Integer.valueOf(1));
                     }

                     Arena.this.plugin.api.winreward(var1x);
                  }

                  if (Bukkit.getPlayer(Arena.this.Hero) != null) {
                     Arena.this.plugin.api.heroreward(Bukkit.getPlayer(Arena.this.Hero));
                  }

                  ArrayList var9 = new ArrayList();
                  Iterator var10 = Arena.this.getPlayers().iterator();

                  Player var11;
                  while(var10.hasNext()) {
                     var11 = (Player)var10.next();
                     var9.add(var11);
                  }

                  var10 = var9.iterator();

                  while(var10.hasNext()) {
                     var11 = (Player)var10.next();
                     List var12 = Arena.this.plugin.messages.getConfig().getStringList("murderer-won-message");

                     String var5;
                     for(Iterator var6 = var12.iterator(); var6.hasNext(); var11.sendMessage(var5.replaceAll("%mscore%", String.valueOf(var8)).replaceAll("%dscore%", String.valueOf(var7)).replaceAll("&", "\u00a7").replaceAll("%murderer%", Arena.this.Murderer).replaceAll("%detective%", Arena.this.Detective).replaceAll("%hero%", Arena.this.Hero))) {
                        var5 = (String)var6.next();
                        var7 = 0;
                        if (Bukkit.getPlayer(Arena.this.Detective) != null) {
                           var7 = Arena.this.getscore(Bukkit.getPlayer(Arena.this.Detective)).intValue();
                        }

                        var8 = 0;
                        if (Bukkit.getPlayer(Arena.this.Murderer) != null) {
                           var8 = Arena.this.getscore(Bukkit.getPlayer(Arena.this.Murderer)).intValue();
                        }
                     }
                  }

                  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Arena.this.plugin, new Runnable() {
                     public void run() {
                        Arena.this.stop("reload");
                     }
                  }, (long)(Arena.this.plugin.settings.getConfig().getInt("stop-arena-after-win-time") * 20));
               }
            }
         }).runTaskLater(this.plugin, 20L);
      }

   }
}
