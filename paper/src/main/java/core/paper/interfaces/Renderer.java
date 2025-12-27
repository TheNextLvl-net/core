package core.paper.interfaces;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.CheckReturnValue;

public interface Renderer {
    @CheckReturnValue
    ItemStack render(RenderContext context);
}
