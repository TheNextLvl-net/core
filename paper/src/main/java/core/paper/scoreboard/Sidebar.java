package core.paper.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

public class Sidebar {
    private final Scoreboard scoreboard;
    private final Objective objective;

    /**
     * Initializes a sidebar for the given player.
     *
     * @param player the player for whom to create the sidebar
     */
    public Sidebar(Player player) {
        var scoreboard = player.getScoreboard();
        var manager = Bukkit.getScoreboardManager();

        if (scoreboard.equals(manager.getMainScoreboard()))
            player.setScoreboard(scoreboard = manager.getNewScoreboard());

        var objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) objective.unregister();
        objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, (Component) null);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreboard = scoreboard;
        this.objective = objective;
    }

    /**
     * Unregisters the sidebar.
     * This method clears the sidebar slot and unregisters the objective.
     */
    public void unregister() {
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        objective.unregister();
    }

    /**
     * Sets the title of the sidebar.
     *
     * @param title the title component to be displayed
     * @return the Sidebar object
     */
    public Sidebar title(@Nullable Component title) {
        objective.displayName(title);
        return this;
    }

    /**
     * Sets the number format for the sidebar.
     *
     * @param format the number format to be set for the sidebar
     * @return the Sidebar object
     */
    public Sidebar numberFormat(@Nullable NumberFormat format) {
        objective.numberFormat(format);
        return this;
    }

    /**
     * Sets the number format for the specified line.
     *
     * @param line   the line number (1-15)
     * @param format the number format to be set for the specified line
     * @return the Sidebar object
     */
    public Sidebar numberFormat(int line, @Nullable NumberFormat format) {
        getScore(line).numberFormat(format);
        return this;
    }

    /**
     * Sets the content for the specified line on the sidebar.
     *
     * @param line    the line number to set the content for (1-15)
     * @param content the component to be displayed on the line
     * @return the Sidebar object
     */
    public Sidebar line(int line, @Nullable Component content) {
        getTeam(line).prefix(content);
        return showLine(line);
    }

    /**
     * Displays the specified line on the sidebar.
     *
     * @param line the line to display (1-15)
     * @return the Sidebar object
     */
    public Sidebar showLine(int line) {
        var score = getScore(line);
        if (!score.isScoreSet()) score.setScore(line);
        return this;
    }

    /**
     * Hides the specified line on the sidebar.
     *
     * @param line the line to hide (1-15)
     * @return the Sidebar object
     */
    public Sidebar hideLine(int line) {
        var value = Line.valueOf(line);
        var objective = this.objective.getScore(value.color());
        if (objective.isScoreSet()) scoreboard.resetScores(value.color());
        var team = scoreboard.getTeam(value.name());
        if (team != null) team.unregister();
        return this;
    }

    private Score getScore(int line) {
        var value = Line.valueOf(line);
        return this.objective.getScore(value.color());
    }

    private Team getTeam(int line) {
        var value = Line.valueOf(line);
        var team = scoreboard.getTeam(value.name());
        if (team != null) return team;
        team = scoreboard.registerNewTeam(value.name());
        team.addEntry(value.color());
        return team;
    }

    private enum Line {
        SCORE_1("§1", 1),
        SCORE_2("§2", 2),
        SCORE_3("§3", 3),
        SCORE_4("§4", 4),
        SCORE_5("§5", 5),
        SCORE_6("§6", 6),
        SCORE_7("§7", 7),
        SCORE_8("§8", 8),
        SCORE_9("§9", 9),
        SCORE_10("§0", 10),
        SCORE_11("§a", 11),
        SCORE_12("§b", 12),
        SCORE_13("§c", 13),
        SCORE_14("§d", 14),
        SCORE_15("§e", 15);

        private final String color;
        private final int score;

        private static Line valueOf(int line) {
            return Arrays.stream(values())
                    .filter(value -> value.score() == line)
                    .findAny()
                    .orElseThrow();
        }

        Line(String color, int score) {
            this.color = color;
            this.score = score;
        }

        public int score() {
            return score;
        }

        public String color() {
            return color;
        }
    }
}
