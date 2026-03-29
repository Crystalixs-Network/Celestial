package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;
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

        this.title = requireNonNull(title, "Title cannot be null");
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
        lines.add(requireNonNull(line, "Line cannot be null"));
        scores.add(requireNonNull(score, "Score cannot be null"));
        return this;
    }

    @Override
    public GenericScoreboard.Builder lines(@NotNull Collection<@NotNull Component> lines) {
        lines(lines, Collections.emptyList());
        return this;
    }

    @Override
    public GenericScoreboard.Builder lines(@NotNull Collection<@NotNull Component> lines, @NotNull Collection<@NotNull Component> scores) {
        requireNonNull(lines, "Lines cannot be null");
        requireNonNull(scores, "Scores cannot be null");
        requireNoNullElements(lines, "Lines");
        requireNoNullElements(scores, "Scores");

        this.lines.addAll(lines);
        if (scores.isEmpty()) {
            for (int i = 0; i < lines.size(); i++) {
                this.scores.add(empty());
            }
        } else {
            if (scores.size() != lines.size()) {
                throw new IllegalArgumentException("Scores size must match lines size");
            }
            this.scores.addAll(scores);
        }

        return this;
    }

    @Override
    public GenericScoreboard.Builder withPlayer(Player player) {
        this.player = requireNonNull(player, "Player cannot be null");
        return this;
    }

    @Override
    public @NotNull GenericScoreboard build() {
        if (player == null) throw new IllegalStateException("Player is not set");
        if (title == null) throw new IllegalStateException("Title is not set");

        return new SidebarScoreboard(player, title, lines, scores);
    }

    private static void requireNoNullElements(Collection<Component> components, String name) {
        for (Component component : components) {
            if (component == null) {
                throw new IllegalArgumentException(name + " cannot contain null");
            }
        }
    }
}
