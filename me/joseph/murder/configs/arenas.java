package me.joseph.murder.configs;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class arenas {
   private FileConfiguration config;
   private File file;

   public arenas(File var1) {
      this.file = var1;
      this.config = YamlConfiguration.loadConfiguration(var1);
   }

   public FileConfiguration getConfig() {
      return this.config;
   }

   public void reload() {
      this.config = YamlConfiguration.loadConfiguration(this.file);
   }

   public void save() {
      try {
         this.config.save(this.file);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
