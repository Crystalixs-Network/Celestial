package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static net.kyori.adventure.text.Component.empty;

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
        validateLine(line, true, false);
        return lines.get(line);
    }

    @Override
    public @NotNull Component score(int line) {
        validateLine(line, true, false);
        return scores.get(line);
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
        updateLine(line, text, empty());
    }

    @Override
    public void updateLine(int line, @NotNull Component text, @NotNull Component score) {
        validateLine(line, false, false);

        if (line < lines.size()) {
            lines.set(line, text);
            if (!scores.isEmpty()) scores.set(line, score);

            int scoreIndex = scoreByLine(line);
            sendLineChange(scoreIndex);
            sendScorePacket(name, scores, scoreIndex, ScoreboardLifecycle.UPDATE);
            return;
        }

        List<Component> newLines = new LinkedList<>(lines);
        List<Component> newScores = new LinkedList<>(scores);

        if (line > lines.size()) {
            for (int i = lines.size(); i < line; i++) {
                newLines.add(empty());
                newScores.add(empty());
            }
        }

        newLines.add(text);
        newScores.add(score);

        updateLines(newLines, newScores);
    }

    @Override
    public void updateLines(@NotNull Collection<@NotNull Component> lines) {
        updateLines(lines, Collections.emptyList());
    }

    @Override
    public synchronized void updateLines(@NotNull Collection<@NotNull Component> lines, @NotNull Collection<@NotNull Component> scores) {
        validateLine(lines.size(), false, true);

        if (scores.size() != lines.size()) {
            throw new IllegalArgumentException("Number of scores must be equal to the number of lines");
        }

        List<Component> oldLines = new LinkedList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        this.scores.clear();
        this.scores.addAll(scores);

        int lineSize = this.lines.size();
        if (oldLines.size() != lineSize) {
            List<Component> oldLinesCopy = new LinkedList<>(oldLines);
            if (oldLines.size() > lineSize) {
                for (int i = oldLinesCopy.size(); i > lineSize; i--) {
                    sendTeamPacket(name, i - 1, TeamLifecycle.REMOVE);
                    sendScorePacket(name, this.scores, i - 1, ScoreboardLifecycle.REMOVE);
                    oldLines.removeFirst();
                }
            } else {
                for (int i = oldLinesCopy.size(); i < lineSize; i++) {
                    sendScorePacket(name, this.scores, i, ScoreboardLifecycle.UPDATE);
                    sendTeamPacket(name, i, TeamLifecycle.CREATE);
                }
            }
        }

        for (int i = 0; i < lineSize; i++) {
            sendLineChange(i);
            sendScorePacket(name, this.scores, i, ScoreboardLifecycle.UPDATE);
        }
    }

    @Override
    public synchronized void removeLine(int line) {
        validateLine(line, false, false);
        if (line >= lines.size()) return;

        List<Component> newLines = new LinkedList<>(lines);
        List<Component> newScores = new LinkedList<>(scores);

        if (!scores.isEmpty()) {
            newScores.remove(line);
        }
        newLines.remove(line);

        updateLines(newLines, scores.isEmpty() ? Collections.emptyList() : newScores);
    }

    @Override
    public void updateScore(int line, @NotNull Component text) {
        validateLine(line, true, false);

        scores.set(line, text);
        sendScorePacket(name, scores, scoreByLine(line), ScoreboardLifecycle.UPDATE);
    }

    @Override
    public synchronized void updateScores(@NotNull Collection<@NotNull Component> scores) {
        if (scores.size() != lines.size()) {
            throw new IllegalArgumentException("Number of scores must be equal to the number of lines");
        }

        List<Component> newScores = new LinkedList<>(scores);
        for (int i = 0; i < lines.size(); i++) {
            if (Objects.equals(this.scores.get(i), newScores.get(i))) {
                continue;
            }
            this.scores.set(i, newScores.get(i));
            sendScorePacket(name, this.scores, scoreByLine(i), ScoreboardLifecycle.UPDATE);
        }
    }

    @Override
    public void removeScore(int line) {
        updateScore(line, empty());
    }

    private void validateLine(int line, boolean checkInRange, boolean checkMax) {
        if (line < 0) {
            throw new IllegalArgumentException("Line cannot be negative");
        }
        if (checkInRange && line >= this.lines.size()) {
            throw new IllegalArgumentException("Line cannot be greater than or equal to the number of lines (%s)".formatted(this.lines.size()));
        }
        if (checkMax && line >= LEGACY_ENTRIES.length - 1) {
            throw new IllegalArgumentException("Line number is to high: " + line);
        }
    }

    private int scoreByLine(int line) {
        return lines.size() - line - 1;
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
