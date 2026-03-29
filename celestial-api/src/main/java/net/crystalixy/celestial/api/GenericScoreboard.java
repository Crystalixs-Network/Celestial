package net.crystalixy.celestial.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Scoreboard-Erweiterung mit typspezifischem Builder.
 */
public interface GenericScoreboard extends Scoreboard {

    /**
     * Builder für ein {@link GenericScoreboard}.
     */
    interface Builder extends Scoreboard.Builder<Builder> {
        /**
         * {@inheritDoc}
         */
        @Override
        Builder title(@NotNull Component title);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder emptyLine();

        /**
         * {@inheritDoc}
         */
        @Override
        Builder line(@NotNull Component line);

        /**
         * Fügt eine Zeile mit optionalem Score-Text hinzu.
         *
         * @param line  Zeilentext
         * @param score Score-Text oder {@code null}
         * @return Builder-Instanz
         */
        Builder line(@NotNull Component line, @Nullable Component score);

        /**
         * {@inheritDoc}
         */
        @Override
        Builder lines(@NotNull Collection<@NotNull Component> lines);

        /**
         * Fügt mehrere Zeilen mit optionalen Scores hinzu.
         *
         * @param lines  Zeilen in Einfügereihenfolge
         * @param scores zugehörige Scores oder {@code null}
         * @return Builder-Instanz
         */
        Builder lines(@NotNull Collection<@NotNull Component> lines, @Nullable Collection<@Nullable Component> scores);

        /**
         * Setzt den Zielspieler des Scoreboards.
         *
         * @param player Zielspieler
         * @return Builder-Instanz
         */
        Builder withPlayer(Player player);

        /**
         * Baut das Scoreboard.
         *
         * @return neues {@link GenericScoreboard}
         * @throws IllegalStateException wenn Pflichtwerte (Titel/Spieler) fehlen
         */
        @NotNull GenericScoreboard build();
    }
}
