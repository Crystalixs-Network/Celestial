package net.crystalixs.celestial.paper;

import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

}
