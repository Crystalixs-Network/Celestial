package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;

public final class SidebarScoreboard extends GenericNmsScoreboard implements GenericScoreboard {

    @Override
    public @NotNull Component title() {
        return Component.empty();
    }

    @Override
    public @UnmodifiableView @NotNull Collection<@NotNull Component> lines() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Component line(int line) {
        return Component.empty();
    }

    @Override
    public @NotNull Component score(int line) {
        return Component.empty();
    }

    @Override
    public void display() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void updateTitle(@NotNull Component title) {
    }

    @Override
    public void updateLine(int line, @NotNull Component text) {
    }

    @Override
    public void updateLine(int line, @NotNull Component text, @NotNull Component score) {
    }

    @Override
    public void updateLines(@NotNull Collection<@NotNull Component> lines) {
    }

    @Override
    public void updateLines(@NotNull Collection<@NotNull Component> lines, @NotNull Collection<@NotNull Component> scores) {
    }

    @Override
    public void removeLine(int line) {
    }

    @Override
    public void updateScore(int line, @NotNull Component text) {
    }

    @Override
    public void updateScores(@NotNull Collection<@NotNull Component> scores) {
    }

    @Override
    public void removeScore(int line) {
    }
}
