package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static net.kyori.adventure.text.Component.empty;

public final class SidebarScoreboardBuilder implements GenericScoreboard.Builder {

    private final LinkedList<Component> lines = new LinkedList<>();
    private final LinkedList<Component> scores = new LinkedList<>();
    private Component title;
    private Player player;

    @Override
    public GenericScoreboard.Builder title(@NotNull Component title) {
        if (this.title != null) {
            throw new IllegalStateException("Title is already set");
        }

        this.title = title;
        return this;
    }

    @Override
    public GenericScoreboard.Builder emptyLine() {
        line(empty());
        return this;
    }

    @Override
    public GenericScoreboard.Builder line(@NotNull Component line) {
        line(line, empty());
        return this;
    }

    @Override
    public GenericScoreboard.Builder line(@NotNull Component line, @NotNull Component score) {
        lines.add(line);
        scores.add(score);
        return this;
    }

    @Override
    public GenericScoreboard.Builder lines(@NotNull Collection<@NotNull Component> lines) {
        lines(lines, Collections.emptyList());
        return this;
    }

    @Override
    public GenericScoreboard.Builder lines(@NotNull Collection<@NotNull Component> lines, @NotNull Collection<@NotNull Component> scores) {
        if (lines.contains(null)) {
            throw new IllegalArgumentException("Lines cannot contain null");
        }

        this.lines.addAll(lines);
        if (scores.isEmpty()) {
            for (int i = 0; i < lines.size(); i++) {
                this.scores.add(empty());
            }
        } else {
            this.scores.addAll(scores);
        }

        return this;
    }

    @Override
    public GenericScoreboard.Builder withPlayer(Player player) {
        this.player = player;
        return this;
    }

    @Override
    public @NotNull GenericScoreboard build() {
        if (player == null) throw new IllegalStateException("Player is not set");
        if (title == null) throw new IllegalStateException("Title is not set");

        return new SidebarScoreboard(player, title, lines, scores);
    }
}
