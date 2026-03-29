package net.crystalixs.celestial.example;

import net.crystalixy.celestial.api.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public final class ExamplePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Scoreboard scoreboard = Scoreboard.sidebar()
                .withPlayer(player)
                .title(text("Example Sidebar"))
                .lines(List.of(
                                empty(),
                                text("Test"),
                                empty(),
                                empty(),
                                text("Test2"),
                                empty()
                        ),
                        List.of(
                                empty(),
                                text("Affe"),
                                empty(),
                                empty(),
                                text("lol"),
                                empty()
                        )
                )
                .build();
        scoreboard.display();
    }
}
