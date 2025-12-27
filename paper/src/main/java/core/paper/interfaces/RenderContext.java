package core.paper.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

public sealed interface RenderContext permits SimpleRenderContext {
    @Contract(pure = true)
    Player player();

    @Contract(pure = true)
    int index();

    @Contract(pure = true)
    int row();

    @Contract(pure = true)
    int column();

    @Contract(pure = true)
    int slot();
    
    // todo: remove with old gui api
    @Deprecated(forRemoval = true)
    static RenderContext of(Player player, int index, int row, int column, int slot) {
        return new SimpleRenderContext(player, index, row, column, slot);
    }
}
