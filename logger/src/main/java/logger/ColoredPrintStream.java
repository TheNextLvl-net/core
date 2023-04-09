package logger;

import core.api.placeholder.Placeholder;
import core.api.placeholder.SystemMessageKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Matcher;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ColoredPrintStream extends PrintStream {
    public static final ColoredPrintStream info = new ColoredPrintStream("info", SystemMessageKey.LOG_INFO::message, FileDescriptor.out).colorize(Color.LIME, Color.GOLD);
    public static final ColoredPrintStream warn = new ColoredPrintStream("warn", SystemMessageKey.LOG_WARN::message, FileDescriptor.out).colorize(Color.YELLOW, Color.WHITE);
    public static final ColoredPrintStream error = new ColoredPrintStream("error", SystemMessageKey.LOG_ERROR::message, FileDescriptor.err).colorize(Color.RED, Color.DARK_RED);
    public static final ColoredPrintStream debug = new ColoredPrintStream("debug", SystemMessageKey.LOG_DEBUG::message, FileDescriptor.out).colorize(Color.YELLOW, Color.GOLD).setCondition(() -> false);
    public static final ColoredPrintStream trace = new ColoredPrintStream("trace", SystemMessageKey.LOG_TRACE::message, FileDescriptor.err).colorize(Color.RED, Color.DARK_RED).setCondition(() -> false);

    private final String name;
    private final Supplier<String> prefix;
    private Color mainColor = Color.RESET;
    private Color secondaryColor = Color.RESET;
    private BooleanSupplier condition = () -> true;
    private final FileDescriptor descriptor;

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> error.printStackTrace(throwable));
    }

    public ColoredPrintStream(String name, Supplier<String> prefix, FileDescriptor descriptor) {
        super(new FileOutputStream(descriptor), true);
        this.name = name;
        this.prefix = prefix;
        this.descriptor = descriptor;
    }

    @SuppressWarnings("resource")
    public ColoredPrintStream colorize(Color mainColor, Color secondaryColor) {
        return setMainColor(mainColor).setSecondaryColor(secondaryColor);
    }

    private synchronized void printStackTrace(Throwable throwable) {
        println(throwable.toString());
        List<StackTraceElement> trace = Arrays.asList(throwable.getStackTrace());
        for (Throwable t : throwable.getSuppressed()) {
            for (StackTraceElement element : t.getStackTrace()) trace.removeIf(element::equals);
        }
        for (StackTraceElement element : trace) println("\tat " + element);
        Throwable cause = throwable.getCause();
        if (cause != null) printCause(cause);
    }

    private synchronized void printCause(Throwable cause) {
        println("Caused by: " + cause);
        StackTraceElement[] trace = cause.getStackTrace();
        for (StackTraceElement element : trace) println("\tat " + element);
        Throwable[] suppressed = cause.getSuppressed();
        for (Throwable element : suppressed) println("Suppressed: \t" + element);
        cause = cause.getCause();
        if (cause != null) printCause(cause);
    }

    @Override
    public synchronized void println() {
        if (getCondition().getAsBoolean()) super.println();
    }

    @Override
    public void println(boolean x) {
        _println(x);
    }

    @Override
    public void println(char x) {
        _println(x);
    }

    @Override
    public void println(int x) {
        _println(x);
    }

    @Override
    public void println(long x) {
        _println(x);
    }

    @Override
    public void println(float x) {
        _println(x);
    }

    @Override
    public void println(double x) {
        _println(x);
    }

    @Override
    public void println(char[] x) {
        _println(Arrays.toString(x));
    }

    @Override
    public void println(@Nullable String x) {
        _println(x);
    }

    @Override
    public void println(@Nullable Object o) {
        _println(o);
    }

    public void println(Object... values) {
        _println(values);
    }

    @Override
    public ColoredPrintStream printf(String format, @Nullable Object... args) {
        if (!getCondition().getAsBoolean()) return this;
        super.printf(this.format((Object) format), args);
        return this;
    }

    @Override
    public ColoredPrintStream printf(@Nullable Locale locale, String format, @Nullable Object... args) {
        if (!getCondition().getAsBoolean()) return this;
        super.printf(locale, this.format((Object) format), args);
        return this;
    }

    @Override
    public ColoredPrintStream format(String format, @Nullable Object... args) {
        if (!getCondition().getAsBoolean()) return this;
        return (ColoredPrintStream) super.format(format, args);
    }

    @Override
    public ColoredPrintStream format(@Nullable Locale locale, String format, @Nullable Object... args) {
        if (!getCondition().getAsBoolean()) return this;
        return (ColoredPrintStream) super.format(locale, format, args);
    }

    @Override
    public ColoredPrintStream append(char c) {
        if (!getCondition().getAsBoolean()) return this;
        println(c);
        return this;
    }

    @Override
    public ColoredPrintStream append(@Nullable CharSequence csq) {
        if (!getCondition().getAsBoolean()) return this;
        return (ColoredPrintStream) super.append(csq);
    }

    @Override
    public ColoredPrintStream append(@Nullable CharSequence csq, int start, int end) {
        if (!getCondition().getAsBoolean()) return this;
        return (ColoredPrintStream) super.append(csq, start, end);
    }

    private String format(Object... values) {
        StringBuilder message = new StringBuilder();
        for (@Nullable Object value : values) {
            String string = Color.colorize(String.valueOf(value), '§', '\u007F');
            String text = getMainColor().getCode() + string.replace(".", "§8.%1%").
                    replace(",", "§8,%1%").replace("<'", "§8'%2%").replace("'>", "§8'%1%").
                    replace(":", "§8:%2%").replace("[", "§8[%2%").replace("]", "§8]%1%").
                    replace("(", "§8(%2%").replace(")", "§8)%1%").replace("{", "§8{%2%").
                    replace("}", "§8}%1%").replace("\"", "§8\"%1%").replace("/", "§8/%2%").
                    replace("\\", "§8\\%2%").replace("|", "§8|%2%").replace(">", "§8>%1%").
                    replace("<", "§8<%1%").replace("»", "§8»%1%").replace("«", "§8«%1%").
                    replace("%1%", getMainColor().getCode()).replace("%2%", getSecondaryColor().getCode());
            String prefix = Color.replace(getPrefix().get());
            if (prefix.isBlank()) message.append(Placeholder.Formatter.DEFAULT.format(Color.replace(text + "§r")));
            else message.append(Placeholder.Formatter.DEFAULT.format(Color.replace(prefix + " " + text + "§r")));
        }
        return message.toString();
    }

    private synchronized void _println(Object... values) {
        if (!getCondition().getAsBoolean()) return;
        for (Object value : values) {
            if (value instanceof Throwable t) printStackTrace(t);
            else super.println(format(value));
        }
    }

    synchronized void print(@Nullable String message, Object[] arguments, @Nullable Throwable throwable) {
        if (message != null) print(message, arguments);
        if (throwable != null) printStackTrace(throwable);
    }

    synchronized void print(String message, Object[] arguments) {
        Iterator<Object> iterator = Arrays.stream(arguments).iterator();
        while (message.contains("{}") && iterator.hasNext()) {
            String replacement = Matcher.quoteReplacement(String.valueOf(iterator.next()));
            message = message.replaceFirst("\\{}", replacement);
        }
        println(message);
    }
}
