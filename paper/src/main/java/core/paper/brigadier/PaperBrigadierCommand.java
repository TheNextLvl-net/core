package core.paper.brigadier;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import core.paper.exception.PaperCommandException;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public class PaperBrigadierCommand extends Command implements PluginIdentifiableCommand, Listener {
    private static final CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();

    /**
     * Returns the literal node for this command.
     */
    private final LiteralCommandNode<CommandSender> node;
    private final @Accessors(fluent = false) Plugin plugin;
    private final Map<Class<?>, ExceptionHandler<?>> exceptionHandlers = new HashMap<>();

    @FunctionalInterface
    public interface ExceptionHandler<T extends Throwable> {
        /**
         * Handle an exception
         *
         * @param sender    the command sender who ran the command
         * @param throwable the exception that occurred
         */
        void thrown(CommandSender sender, T throwable);
    }

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
        applyDefaults();
    }

    private void applyDefaults() {
        handle(PaperCommandException.class, (sender, exception) -> sender.sendMessage(exception.getComponent()));
    }

    /**
     * Get the fallback prefix for this command
     *
     * @return the fallback prefix
     */
    public String getFallbackPrefix() {
        return getPlugin().getName().toLowerCase();
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
     * Define a usage for this command
     *
     * @param usage the usage
     * @return the paper brigadier command
     */
    public PaperBrigadierCommand usage(String usage) {
        setUsage(usage);
        return this;
    }

    public <T extends Throwable> PaperBrigadierCommand handle(Class<T> type, ExceptionHandler<T> handler) {
        exceptionHandlers().put(type, handler);
        return this;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            if (args.length == 0) dispatcher.execute(getName(), sender);
            else dispatcher.execute(getName() + " " + String.join(" ", args), sender);
        } catch (Throwable e) {
            var exceptionHandler = (ExceptionHandler<Throwable>) exceptionHandlers().get(e.getClass());
            if (exceptionHandler == null) {
                if (sender.isOp()) e.printStackTrace();
                if (sender instanceof ConsoleCommandSender) return true;
                sender.sendMessage(Component.translatable("command.failed").color(NamedTextColor.RED));
            } else exceptionHandler.thrown(sender, e);
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncTabComplete(AsyncTabCompleteEvent event) {
        if (!event.isCommand()) return;

        var split = (event.getBuffer().charAt(0) == '/'
                ? event.getBuffer().substring(1)
                : event.getBuffer())
                .split(" ", 2);

        var aliases = new ArrayList<>(getAliases());
        aliases.addAll(getAliases().stream().map(alias -> getFallbackPrefix() + ":" + alias).toList());
        aliases.add(getFallbackPrefix() + ":" + getName());
        aliases.add(getName());
        if (aliases.stream().noneMatch(split[0]::equals)) return;

        var parse = dispatcher.parse(getName() + " " + split[1], event.getSender());
        event.completions(dispatcher.getCompletionSuggestions(parse).join().getList().stream()
                .map(suggestion -> AsyncTabCompleteEvent.Completion.completion(suggestion.getText(),
                        suggestion.getTooltip() instanceof ComponentMessage message
                                ? message.asComponent()
                                : suggestion.getTooltip() != null
                                ? Component.text(suggestion.getTooltip().getString())
                                : null))
                .toList());
        event.setHandled(true);
    }

    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return super.testPermissionSilent(target) && node().canUse(target);
    }

    public void register() {
        dispatcher.getRoot().addChild(node());
        Bukkit.getCommandMap().register(getName(), getFallbackPrefix(), this);
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }
}
