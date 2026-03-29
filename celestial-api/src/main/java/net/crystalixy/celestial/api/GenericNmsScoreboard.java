package net.crystalixy.celestial.api;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.entity.Player;

public abstract class GenericNmsScoreboard {

    private final Scoreboard scoreboard = new Scoreboard();
    private final String objectiveName;
    private Objective objective;

    private final Player player;

    protected GenericNmsScoreboard(Player player, String objectiveName, Component title) {
        this.player = player;
        this.objectiveName = objectiveName;
        this.objective = scoreboard.addObjective(
                objectiveName,
                ObjectiveCriteria.DUMMY,
                toVanillaComponent(title),
                ObjectiveCriteria.RenderType.INTEGER,
                false,
                null);
    }

    private static net.minecraft.network.chat.Component toVanillaComponent(Component component) {
        return component == null
                ? net.minecraft.network.chat.Component.empty()
                : PaperAdventure.asVanilla(component);
    }
}
