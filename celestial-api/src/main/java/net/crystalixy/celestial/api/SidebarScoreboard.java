package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public final class SidebarScoreboard extends GenericNmsScoreboard implements GenericScoreboard {

    private final LinkedList<Component> lines;
    private final LinkedList<Component> scores;
    private final String name;
    private Component title;

    public SidebarScoreboard(Player player, Component title, LinkedList<Component> lines, LinkedList<Component> scores) {
        super(player);
        this.title = title;
        this.lines = lines;
        this.scores = scores;
        this.name = "celestial-" + Integer.toHexString(ThreadLocalRandom.current().nextInt());
    }

    @Override
    public @NotNull Component title() {
        return title;
    }

    @Override
    public @UnmodifiableView @NotNull Collection<@NotNull Component> lines() {
        return Collections.unmodifiableCollection(lines);
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
        sendObjectivePacket(ObjectiveLifecycle.CREATE, name, title);
        sendDisplayObjectivePacket();
        sendInitialLines();
    }

    @Override
    public void destroy() {
        for (int i = 0; i < lines.size(); i++) {
            sendTeamPacket(name, i, TeamLifecycle.REMOVE);
        }
        sendObjectivePacket(ObjectiveLifecycle.REMOVE, name, title);
    }

    @Override
    public void updateTitle(@NotNull Component title) {
        this.title = title;
        sendObjectivePacket(ObjectiveLifecycle.UPDATE, name, title);
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

    private void sendLineChange(int score) {
        Component line = lineByScore(this.lines, score);
        sendTeamPacket(name, score, TeamLifecycle.UPDATE, line, null);
    }

    private void sendInitialLines() {
        for (int i = 0; i < lines.size(); i++) {
            sendScorePacket(name, scores, i, ScoreboardLifecycle.UPDATE);
            sendTeamPacket(name, i, TeamLifecycle.CREATE);
            sendLineChange(i);
        }
    }
}
