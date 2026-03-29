package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface GenericScoreboard extends Scoreboard {

    interface Builder extends Scoreboard.Builder<Builder> {
        @Override
        Builder title(@NotNull Component title);

        @Override
        Builder emptyLine();

        @Override
        Builder line(@NotNull Component line);

        @Override
        Builder lines(@NotNull Collection<@NotNull Component> lines);

        Builder usePlayer(Player player);

        @NotNull GenericScoreboard build();
    }
}
