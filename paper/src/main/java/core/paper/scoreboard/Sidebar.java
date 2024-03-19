package core.paper.scoreboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Sidebar {
    private final @Getter Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;

    public Sidebar(Player player) {
        var scoreboard = player.getScoreboard();
        var manager = Bukkit.getScoreboardManager();

        if (scoreboard.equals(manager.getMainScoreboard()))
            player.setScoreboard(scoreboard = manager.getNewScoreboard());

        var objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) objective.unregister();
        objective = scoreboard.registerNewObjective("display", Criteria.DUMMY, (Component) null);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.player = player;
        this.scoreboard = scoreboard;
        this.objective = objective;
    }

    public void remove() {
        IntStream.rangeClosed(1, 15).forEach(this::unsetScore);
        title(null);
    }

    public Sidebar title(@Nullable Component title) {
        objective.displayName(title);
        return this;
    }

    public Sidebar setScore(@Nullable Component content, int score) {
        getTeam(score).prefix(content);
        return showScore(score);
    }

    public Sidebar unsetScore(int score) {
        return hideScore(score);
    }

    private Sidebar showScore(int score) {
        var value = Score.valueOf(score);
        var objective = this.objective.getScore(value.color());
        if (!objective.isScoreSet()) objective.setScore(score);
        return this;
    }

    private Sidebar hideScore(int score) {
        var value = Score.valueOf(score);
        var objective = this.objective.getScore(value.color());
        if (objective.isScoreSet()) scoreboard.resetScores(value.color());
        var team = scoreboard.getTeam(value.name());
        if (team != null) team.unregister();
        return this;
    }

    private Team getTeam(int score) {
        var value = Score.valueOf(score);
        var team = scoreboard.getTeam(value.name());
        if (team != null) return team;
        team = scoreboard.registerNewTeam(value.name());
        team.addEntry(value.color());
        return team;
    }

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    public enum Score {
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

        public static Score valueOf(int score) {
            return Arrays.stream(values())
                    .filter(score1 -> score1.score() == score)
                    .findFirst()
                    .orElseThrow();
        }
    }
}
