package core.paper.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BelowNameScoreboard {
    private final Scoreboard scoreboard;
    private final Objective objective;

    /**
     * Initializes a BelowNameScoreboard for the given player.
     *
     * @param player the player for whom to create the BelowNameScoreboard
     */
    public BelowNameScoreboard(Player player) {
        var scoreboard = player.getScoreboard();
        var manager = Bukkit.getScoreboardManager();

        if (scoreboard.equals(manager.getMainScoreboard()))
            player.setScoreboard(scoreboard = manager.getNewScoreboard());

        var objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);
        if (objective != null) objective.unregister();
        objective = scoreboard.registerNewObjective("below_name", Criteria.DUMMY, (Component) null);
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        this.scoreboard = scoreboard;
        this.objective = objective;
    }

    /**
     * Unregisters the BelowNameScoreboard.
     * This method clears the slot below the player's name and unregisters the objective.
     */
    public void unregister() {
        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
        objective.unregister();
    }

    /**
     * Sets the text of the scoreboard objective.
     *
     * @param title the title component to be displayed
     * @return the BelowNameScoreboard object
     */
    public BelowNameScoreboard text(@Nullable Component title) {
        objective.displayName(title);
        return this;
    }

    /**
     * Sets the number format for the scoreboard objective.
     *
     * @param format the number format to be set for the scoreboard objective
     * @return the BelowNameScoreboard object
     */
    public BelowNameScoreboard numberFormat(@Nullable NumberFormat format) {
        objective.numberFormat(format);
        return this;
    }
}
