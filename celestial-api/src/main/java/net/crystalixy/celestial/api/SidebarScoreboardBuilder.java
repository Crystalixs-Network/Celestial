package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class SidebarScoreboardBuilder implements GenericScoreboard.Builder {

    @Override
    public GenericScoreboard.Builder title(@NotNull Component title) {
        return null;
    }

    @Override
    public GenericScoreboard.Builder emptyLine() {
        return null;
    }

    @Override
    public GenericScoreboard.Builder line(@NotNull Component line) {
        return null;
    }

    @Override
    public GenericScoreboard.Builder lines(@NotNull Collection<@NotNull Component> lines) {
        return null;
    }

    @Override
    public GenericScoreboard.Builder usePlayer(Player player) {
        return null;
    }

    @Override
    public @NotNull GenericScoreboard build() {
        return null;
    }
}
