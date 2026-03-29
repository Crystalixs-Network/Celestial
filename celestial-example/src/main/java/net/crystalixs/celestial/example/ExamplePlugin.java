package net.crystalixs.celestial.example;

import net.crystalixy.celestial.api.Scoreboard;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static net.kyori.adventure.text.Component.translatable;

public final class ExamplePlugin extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        MiniMessageTranslationStore store = MiniMessageTranslationStore.create(Key.key("celestial:example_translation"));
        store.defaultLocale(Locale.GERMAN);

        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.GERMAN);

        store.registerAll(Locale.GERMAN, bundle, true);
        GlobalTranslator.translator().addSource(store);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Scoreboard scoreboard = Scoreboard.sidebar()
                .withPlayer(player)
                .title(translatable("title"))
                .lines(lines(), scores())
                .build();
        scoreboard.display();
    }

    private List<Component> scores() {
        List<Component> scores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String key = "score_" + (i + 1);
            scores.add(translatable(key));
        }
        return scores;
    }

    private List<Component> lines() {
        List<Component> lines = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String key = "line_" + (i + 1);
            lines.add(translatable(key));
        }
        return lines;
    }
}
