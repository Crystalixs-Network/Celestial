package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface Scoreboard {

    @NotNull Component title();

    @UnmodifiableView
    @NotNull Collection<@NotNull Component> lines();

    @NotNull Component line(int line);

    @Nullable Component score(int line);

    void display();

    void destroy();

    void updateTitle(@NotNull Component title);

    void updateLine(int line, @NotNull Component text);

    void updateLine(int line, @NotNull Component text, @Nullable Component score);

    void updateLines(@NotNull Collection<@NotNull Component> lines);

    void updateLines(@NotNull Collection<@NotNull Component> lines, @Nullable Collection<@Nullable Component> scores);

    void removeLine(int line);

    void updateScore(int line, @Nullable Component text);

    void updateScores(@Nullable Collection<@Nullable Component> scores);

    void removeScore(int line);

    interface Builder<T> {
        T title(@NotNull Component title);

        T emptyLine();

        T line(@NotNull Component line);

        T lines(@NotNull Collection<@NotNull Component> lines);
    }

    static GenericScoreboard.Builder sidebar() {
        return new SidebarScoreboardBuilder();
    }
}
