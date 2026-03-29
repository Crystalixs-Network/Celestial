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

        Builder line(@NotNull Component line, @NotNull Component score);

        @Override
        Builder lines(@NotNull Collection<@NotNull Component> lines);

        Builder lines(@NotNull Collection<@NotNull Component> lines, @NotNull Collection<@NotNull Component> scores);

        Builder withPlayer(Player player);

        @NotNull GenericScoreboard build();
    }
}
