package net.crystalixs.celestial.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

/**
 * Abstraktion eines an einen Spieler gebundenen Sidebar-Scoreboards.
 */
public interface Scoreboard {

    /**
     * Liefert den aktuellen Titel des Scoreboards.
     *
     * @return Titel-Komponente
     */
    @NotNull Component title();

    /**
     * Liefert eine immutable Liste von allen aktuellen Zeilen als Kopie.
     *
     * @return Zeilen des Scoreboards
     */
    @UnmodifiableView
    @NotNull Collection<@NotNull Component> lines();

    /**
     * Liefert den Text einer Zeile anhand ihres Indexes.
     *
     * @param line 0-basierter Zeilenindex
     * @return Zeilen-Komponente
     * @throws IllegalArgumentException wenn der Index negativ oder außerhalb der vorhandenen Zeilen liegt
     */
    @NotNull Component line(int line);

    /**
     * Liefert den Score-Text einer Zeile.
     *
     * @param line 0-basierter Zeilenindex
     * @return Score-Komponente oder {@code null}, wenn kein Score gesetzt ist
     * @throws IllegalArgumentException wenn der Index negativ oder außerhalb der vorhandenen Zeilen liegt
     */
    @Nullable Component score(int line);

    /**
     * Sendet das Scoreboard an den zugeordneten Spieler.
     */
    void display();

    /**
     * Entfernt das Scoreboard beim zugeordneten Spieler.
     */
    void destroy();

    /**
     * Aktualisiert den Titel.
     *
     * @param title neuer Titel
     */
    void updateTitle(@NotNull Component title);

    /**
     * Aktualisiert oder erstellt eine Zeile ohne Score-Text.
     *
     * @param line 0-basierter Zeilenindex
     * @param text neuer Zeilentext
     */
    void updateLine(int line, @NotNull Component text);

    /**
     * Aktualisiert oder erstellt eine Zeile mit optionalem Score-Text.
     *
     * @param line  0-basierter Zeilenindex
     * @param text  neuer Zeilentext
     * @param score neuer Score-Text, {@code null} entfernt den Score
     */
    void updateLine(int line, @NotNull Component text, @Nullable Component score);

    /**
     * Ersetzt alle Zeilen. Alle Scores werden dabei entfernt.
     *
     * @param lines neue Zeilen
     */
    void updateLines(@NotNull Collection<@NotNull Component> lines);

    /**
     * Ersetzt alle Zeilen und Scores.
     *
     * @param lines  neue Zeilen
     * @param scores neue Scores; fehlende Einträge werden mit {@code null} aufgefüllt
     */
    void updateLines(@NotNull Collection<@NotNull Component> lines, @Nullable Collection<@Nullable Component> scores);

    /**
     * Entfernt eine Zeile.
     *
     * @param line 0-basierter Zeilenindex
     */
    void removeLine(int line);

    /**
     * Setzt den Score-Text einer vorhandenen Zeile.
     *
     * @param line 0-basierter Zeilenindex
     * @param text neuer Score-Text, {@code null} entfernt den Score
     */
    void updateScore(int line, @Nullable Component text);

    /**
     * Aktualisiert alle Scores anhand der vorhandenen Zeilen.
     *
     * @param scores neue Scores; fehlende Einträge werden mit {@code null} aufgefüllt
     */
    void updateScores(@Nullable Collection<@Nullable Component> scores);

    /**
     * Entfernt den Score-Text einer Zeile.
     *
     * @param line 0-basierter Zeilenindex
     */
    void removeScore(int line);

    /**
     * Basisschnittstelle für Builder von Scoreboards.
     *
     * @param <T> konkreter Builder-Typ für fluent API
     */
    interface Builder<T> {
        /**
         * Setzt den Titel.
         *
         * @param title Titel-Komponente
         * @return Builder-Instanz
         */
        T title(@NotNull Component title);

        /**
         * Fügt eine leere Zeile hinzu.
         *
         * @return Builder-Instanz
         */
        T emptyLine();

        /**
         * Fügt eine Zeile hinzu.
         *
         * @param line Zeilentext
         * @return Builder-Instanz
         */
        T line(@NotNull Component line);

        /**
         * Fügt mehrere Zeilen hinzu.
         *
         * @param lines Zeilen in Einfügereihenfolge
         * @return Builder-Instanz
         */
        T lines(@NotNull Collection<@NotNull Component> lines);
    }

    /**
     * Erzeugt einen Builder für ein Sidebar-Scoreboard.
     *
     * @return Builder fuer {@link GenericScoreboard}
     */
    static GenericScoreboard.Builder sidebar() {
        return new SidebarScoreboardBuilder();
    }
}
