package net.crystalixs.celestial.api;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.FixedFormat;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.*;
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
    private Objective objective;

    private final Player player;
    private boolean isDeleted;

    protected GenericNmsScoreboard(Player player) {
        this.player = player;
    }

    protected Component lineByScore(List<Component> lines, int score) {
        return score < lines.size() ? lines.get(lines.size() - score - 1) : null;
    }

    protected void sendObjectivePacket(ObjectiveLifecycle lifecycle, String uniqueName, Component title) {
        if (lifecycle == ObjectiveLifecycle.CREATE) {
            this.objective = createObjective(title, uniqueName);
        }
        var packet = new ClientboundSetObjectivePacket(objective, lifecycle.ordinal());
        sendPacket(packet);
    }

    protected void sendDisplayObjectivePacket() {
        var packet = new ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, objective);
        sendPacket(packet);
    }

    protected void sendScorePacket(String uniqueName, List<Component> scores, int score, ScoreboardLifecycle lifecycle) {
        String entry = LEGACY_ENTRIES[score];

        if (lifecycle == ScoreboardLifecycle.REMOVE) {
            var packet = new ClientboundResetScorePacket(entry, uniqueName);
            sendPacket(packet);
            return;
        }

        Component component = lineByScore(scores, score);
        boolean hasDisplayScore = component != null && !component.equals(Component.empty());

        Optional<net.minecraft.network.chat.Component> display = hasDisplayScore
                ? Optional.of(toVanillaComponent(component))
                : Optional.empty();

        Optional<NumberFormat> numberFormat = hasDisplayScore
                ? Optional.of(BlankFormat.INSTANCE)
                : Optional.of(new FixedFormat(toVanillaComponent(component)));

        var packet = new ClientboundSetScorePacket(entry, uniqueName, score, display, numberFormat);
        sendPacket(packet);
    }

    protected void sendTeamPacket(String uniqueName, int score, TeamLifecycle lifecycle, Component prefix, Component suffix) {
        if (lifecycle == TeamLifecycle.ADD_PLAYERS || lifecycle == TeamLifecycle.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }

        String teamName = uniqueName + ":" + score;
        String entry = LEGACY_ENTRIES[score];

        PlayerTeam team = new PlayerTeam(scoreboard, teamName);
        team.setDisplayName(net.minecraft.network.chat.Component.empty());
        team.setNameTagVisibility(Team.Visibility.ALWAYS);
        team.setCollisionRule(Team.CollisionRule.ALWAYS);
        team.setColor(ChatFormatting.RESET);
        team.setPlayerPrefix(toVanillaComponent(prefix));
        team.setPlayerSuffix(toVanillaComponent(suffix));

        if (lifecycle == TeamLifecycle.REMOVE) {
            var packet = ClientboundSetPlayerTeamPacket.createRemovePacket(team);
            sendPacket(packet);
            return;
        }

        boolean shouldCreate = lifecycle == TeamLifecycle.CREATE;
        if (shouldCreate) {
            team.getPlayers().add(entry);
        }
        var packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, shouldCreate);
        sendPacket(packet);
    }

    protected void sendTeamPacket(String uniqueName, int score, TeamLifecycle lifecycle) {
        sendTeamPacket(uniqueName, score, lifecycle, null, null);
    }

    private void sendPacket(Packet<?> packet) {
        if (isDeleted || !player.isOnline()) return;

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(packet);
    }

    private Objective createObjective(Component title, String uniqueName) {
        return scoreboard.addObjective(
                uniqueName,
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

    public enum TeamLifecycle {CREATE, REMOVE, UPDATE, ADD_PLAYERS, REMOVE_PLAYERS}
}
