package core.paper.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public class PaperBrigadierCommand extends BukkitCommand implements PluginIdentifiableCommand {
    private static final CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();

    /**
     * Returns the literal node for this command.
     */
    private final LiteralCommandNode<CommandSender> node;
    private final @Accessors(fluent = false) Plugin plugin;
    private @Nullable Function<CommandSender, Component> usage;

    /**
     * Constructs a {@link PaperBrigadierCommand} from the node returned by the given builder.
     *
     * @param builder the {@link LiteralCommandNode} builder
     */
    public PaperBrigadierCommand(LiteralArgumentBuilder<CommandSender> builder, Plugin plugin) {
        this(builder.build(), plugin);
    }

    /**
     * Constructs a {@link PaperBrigadierCommand} from the given command node.
     *
     * @param node the command node
     */
    public PaperBrigadierCommand(LiteralCommandNode<CommandSender> node, Plugin plugin) {
        super(node.getName());
        this.plugin = plugin;
        this.node = node;
    }

    /**
     * Register aliases to access this command
     *
     * @param aliases the aliases
     * @return the paper brigadier command
     */
    public PaperBrigadierCommand aliases(String... aliases) {
        setAliases(Arrays.asList(aliases));
        return this;
    }

    /**
     * Define a string permission for this command
     *
     * @param permission the permission
     * @return the paper brigadier command
     */
    public PaperBrigadierCommand permission(String permission) {
        setPermission(permission);
        return this;
    }

    /**
     * Define a description for this command
     *
     * @param description the description
     * @return the paper brigadier command
     */
    public PaperBrigadierCommand description(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Define a usage function taking in a {@link CommandSender} returning a {@link Component}
     *
     * @param usage the usage function
     * @return the paper brigadier command
     */
    public PaperBrigadierCommand usage(Function<CommandSender, Component> usage) {
        var component = (this.usage = usage).apply(Bukkit.getConsoleSender());
        setUsage(PlainTextComponentSerializer.plainText().serialize(component));
        return this;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            dispatcher.execute(getName() + " " + String.join(" ", args), sender);
        } catch (CommandSyntaxException e) {
            if (usage() != null) sender.sendMessage(usage().apply(sender));
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        // var parse = dispatcher.parse(getName() + " " + String.join(" ", args), sender);
        // return dispatcher.getCompletionSuggestions(parse).join();
        return super.tabComplete(sender, alias, args);
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return super.testPermissionSilent(target) && node().canUse(target);
    }

    public void register() {
        dispatcher.getRoot().addChild(node());
        Bukkit.getCommandMap().register(getName(), plugin.getName(), this);
    }
}
