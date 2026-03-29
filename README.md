# Celestial

Celestial ist eine Java-API für Sidebar-Scoreboards auf Paper.
Der Fokus liegt auf einer einfachen Nutzung im Plugin-Code: bauen, anzeigen, aktualisieren.

## API-Fokus

- Schneller Einstieg über `Scoreboard.sidebar()`
- Builder-Pattern für Titel, Zeilen und optionale Score-Spalte
- Laufzeit-Methoden für Updates ohne Neubau des Scoreboards
- Nutzung mit `Component` aus Adventure

## Voraussetzungen

- Java 21
- Paper API 1.21.11

## Nutzung

### 1. Scoreboard erstellen und anzeigen

```java
import net.crystalixy.celestial.api.Scoreboard;
import net.kyori.adventure.text.Component;

Scoreboard scoreboard = Scoreboard.sidebar()
        .withPlayer(player)
        .title(Component.text("Spielerprofil"))
        .line(Component.text("Kills"), Component.text("12"))
        .line(Component.text("Coins"), Component.text("530"))
        .line(Component.text("Rang"), Component.text("Gold"))
        .build();

scoreboard.display();
```

### 2. Zur Laufzeit aktualisieren

```java
scoreboard.updateTitle(Component.text("Spielerprofil (Live)"));
scoreboard.updateLine(0, Component.text("Kills"), Component.text("13"));
scoreboard.updateScore(1, Component.text("560"));
scoreboard.removeScore(2);
```

### 3. Zeilen vollständig ersetzen

```java
scoreboard.updateLines(
        List.of(
                Component.text("Kills"),
                Component.text("Coins"),
                Component.text("Rang")
        ),
        List.of(
                Component.text("13"),
                Component.text("560"),
                Component.text("Gold")
        )
);
```

### 4. Scoreboard entfernen

```java
scoreboard.destroy();
```

## Wichtige Hinweise

- Zeilenindizes sind 0-basiert.
- `title(...)` darf im Builder nur einmal gesetzt werden.
- `build()` benötigt `withPlayer(...)` und `title(...)`.
- Scores sind optional (`null` ist erlaubt).
- API-Aufrufe sollten auf dem Bukkit Main Thread erfolgen.
