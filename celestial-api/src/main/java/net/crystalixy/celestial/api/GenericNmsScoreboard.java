package net.crystalixy.celestial.api;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class GenericNmsScoreboard {

    public enum ObjectiveLifecycle {CREATE, REMOVE, UPDATE}

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
}
