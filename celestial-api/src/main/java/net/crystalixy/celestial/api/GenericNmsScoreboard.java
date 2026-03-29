package net.crystalixy.celestial.api;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.FixedFormat;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class GenericNmsScoreboard {

    @SuppressWarnings("deprecation")
    protected static final String[] LEGACY_ENTRIES = Arrays.stream(ChatColor.values())
            .map(Object::toString)
            .toArray(String[]::new);

    private final Scoreboard scoreboard = new Scoreboard();
    private final String objectiveName;
    private Objective objective;

    private final Player player;
    private boolean isDeleted;

    protected GenericNmsScoreboard(Player player, String objectiveName, Component title) {
        this.player = player;
        this.objectiveName = objectiveName;
        this.objective = createObjective(title);
    }

    protected void sendObjectivePacket(ObjectiveLifecycle lifecycle, Component title) {
        if (lifecycle == ObjectiveLifecycle.CREATE) {
            this.objective = createObjective(title);
        }
        var packet = new ClientboundSetObjectivePacket(objective, lifecycle.ordinal());
        sendPacket(packet);
    }

    protected void sendDisplayObjectivePacket() {
        var packet = new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, objective);
        sendPacket(packet);
    }

    protected void sendScorePacket(List<Component> scores, int score, ScoreboardLifecycle lifecycle) {
        String entry = LEGACY_ENTRIES[score];

        if (lifecycle == ScoreboardLifecycle.REMOVE) {
            var packet = new ClientboundResetScorePacket(entry, objectiveName);
            sendPacket(packet);
            return;
        }

        Component component = lineByScore(scores, score);
        NumberFormat format = component == null
                ? BlankFormat.INSTANCE
                : new FixedFormat(toVanillaComponent(component));

        var packet = new ClientboundSetScorePacket(entry, objectiveName, score, Optional.empty(), Optional.of(format));
        sendPacket(packet);
    }

    private Component lineByScore(List<Component> lines, int score) {
        return score < lines.size() ? lines.get(lines.size() - score - 1) : null;
    }

    private void sendPacket(Packet<?> packet) {
        if (isDeleted || !player.isOnline()) return;

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);
    }

    private Objective createObjective(Component title) {
        return scoreboard.addObjective(
                objectiveName,
                ObjectiveCriteria.DUMMY,
                toVanillaComponent(title),
                ObjectiveCriteria.RenderType.INTEGER,
                false,
                null);
    }

    private net.minecraft.network.chat.Component toVanillaComponent(Component component) {
        return component == null
                ? net.minecraft.network.chat.Component.empty()
                : PaperAdventure.asVanilla(component);
    }

    public enum ObjectiveLifecycle {CREATE, REMOVE, UPDATE}

    public enum ScoreboardLifecycle {CREATE, REMOVE, UPDATE}
}
