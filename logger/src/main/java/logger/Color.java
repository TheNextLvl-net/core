package logger;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum Color {
    BOLD_BLACK("\033[1;30m", "§o§l"),
    BOLD_RED("\033[1;31m", "§c§l"),
    BOLD_GREEN("\033[1;32m", "§2§l"),
    BOLD_GOLD("\033[1;33m", "§6§l"),
    BOLD_AQUA("\033[1;34m", "§b§l"),
    BOLD_DARK_PURPLE("\033[1;35m", "§5§l"),
    BOLD_DARK_CYAN("\033[1;36m", "§9§l"),
    BOLD_GRAY("\033[1;90m", "§8§l"),
    BOLD_DARK_RED("\033[1;91m", "§4§l"),
    BOLD_LIME("\033[1;92m", "§a§l"),
    BOLD_YELLOW("\033[1;93m", "§e§l"),
    BOLD_BLUE("\033[1;94m", "§1§l"),
    BOLD_PURPLE("\033[1;95m", "§d§l"),
    BOLD_CYAN("\033[1;96m", "§3§l"),
    BOLD_WHITE("\033[1;97m", "§f§l"),

    UNDERLINED_BLACK("\033[4;30m", "§0§n"),
    UNDERLINED_RED("\033[4;31m", "§c§n"),
    UNDERLINED_DARK_GREEN("\033[4;32m", "§2§n"),
    UNDERLINED_GOLD("\033[4;33m", "§6§n"),
    UNDERLINED_AQUA("\033[4;34m", "§b§n"),
    UNDERLINED_DARK_PURPLE("\033[4;35m", "§5§n"),
    UNDERLINED_DARK_CYAN("\033[4;36m", "§9§n"),

    BACKGROUND_BLACK("\033[40m", "§0§k"),
    BACKGROUND_RED("\033[41m", "§c§k"),
    BACKGROUND_DARK_GREEN("\033[42m", "§2§k"),
    BACKGROUND_GOLD("\033[43m", "§6§k"),
    BACKGROUND_AQUA("\033[44m", "§b§k"),
    BACKGROUND_DARK_PURPLE("\033[45m", "§5§k"),
    BACKGROUND_DARK_CYAN("\033[46m", "§9§k"),
    BACKGROUND_GRAY("\033[0;100m", "§8§k"),
    BACKGROUND_DARK_RED("\033[0;101m", "§4§k"),
    BACKGROUND_LIME("\033[0;102m", "§a§k"),
    BACKGROUND_YELLOW("\033[0;103m", "§e§k"),
    BACKGROUND_BLUE("\033[0;104m", "§1§k"),
    BACKGROUND_PURPLE("\033[0;105m", "§d§k"),
    BACKGROUND_CYAN("\033[0;106m", "§3§k"),
    BACKGROUND_WHITE("\033[0;107m", "§f§k"),

    RESET("\033[0m", "§r"),
    BLACK("\033[0;30m", "§0"),
    RED("\033[0;91m", "§c"),
    LIME("\033[0;92m", "§a"),
    GREEN("\033[0;32m", "§2"),
    GOLD("\033[0;33m", "§6"),
    AQUA("\033[0;34m", "§b"),
    DARK_PURPLE("\033[0;35m", "§5"),
    DARK_CYAN("\033[0;96m", "§9"),
    DARK_GRAY("\033[0;90m", "§8"),
    GRAY("\033[0;37m", "§7"),
    DARK_RED("\033[0;31m", "§4"),
    YELLOW("\033[0;93m", "§e"),
    BLUE("\033[0;94m", "§1"),
    PURPLE("\033[0;95m", "§d"),
    CYAN("\033[0;36m", "§3"),
    WHITE("\033[0;97m", "§f"),

    BOLD_IGNORED("", "§l"),
    UNDERLINE_IGNORED("", "§n"),
    STRIKETHROUGH_IGNORED("", "§m"),
    MATRIX_IGNORED("", "§k"),
    ITALIC_IGNORED("", "§o"),
    HEX_IGNORED("", "§x");

    private final String ansi;
    private final String code;

    Color(String ansi, String code) {
        this.ansi = ansi;
        this.code = code;
    }

    @Override
    public String toString() {
        return getAnsi();
    }

    public static String replace(String string) {
        if ((string.contains("<") && string.contains(">")) || string.contains("§")) {
            for (Color color : Color.values()) {
                string = string.replace("<" + color.name().toLowerCase() + ">", color.getAnsi());
                string = string.replace(color.getCode(), color.getAnsi());
            }
        }
        return string;
    }

    public static String unColorize(String text, char... prefix) {
        return Minecraft.unColorize(Hex.unColorize(text), prefix);
    }

    public static String colorize(String text, char... prefix) {
        return Minecraft.colorize(Hex.colorize(text), prefix);
    }

    public static final class Hex {

        private static final Pattern PATTERN_1 = Pattern.compile("<color:#[a-fA-F\\d]{6}>");
        private static final Pattern PATTERN_2 = Pattern.compile("<#[a-fA-F\\d]{6}>");

        public static String colorize(String message) {
            Matcher match = PATTERN_1.matcher(message);
            while (match.find()) {
                String hex = message.substring(match.start() + 7, match.end() - 1);
                String code = message.substring(match.start(), match.end());
                message = message.replace(code, Minecraft.getColor(hex));
                match = PATTERN_1.matcher(message);
            }
            match = PATTERN_2.matcher(message);
            while (match.find()) {
                String hex = message.substring(match.start() + 1, match.end() - 1);
                String code = message.substring(match.start(), match.end());
                message = message.replace(code, Minecraft.getColor(hex));
                match = PATTERN_2.matcher(message);
            }
            return message;
        }

        public static String unColorize(String message) {
            Matcher match = PATTERN_1.matcher(message);
            while (match.find()) {
                message = message.replace(message.substring(match.start(), match.end()), "");
                match = PATTERN_1.matcher(message);
            }
            match = PATTERN_2.matcher(message);
            while (match.find()) {
                message = message.replace(message.substring(match.start(), match.end()), "");
                match = PATTERN_2.matcher(message);
            }
            return message;
        }
    }

    @Getter
    public enum Minecraft {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        MATRIX('k', true),
        BOLD('l', true),
        STRIKETHROUGH('m', true),
        UNDERLINE('n', true),
        ITALIC('o', true),
        RESET('r', true);

        private final String name;
        private final char identifier;
        private final boolean formatter;

        Minecraft(char identifier) {
            this(identifier, false);
        }

        Minecraft(char identifier, boolean formatter) {
            this.identifier = identifier;
            this.name = name().toLowerCase();
            this.formatter = formatter;
        }

        public static String unColorize(String string, char... prefix) {
            if (prefix.length == 0) prefix = new char[]{'&', '§'};
            for (char c : prefix) {
                for (Minecraft minecraft : Minecraft.values()) {
                    string = string.replace(String.valueOf(c) + minecraft.getIdentifier(), "");
                }
            }
            return string;
        }

        public static String colorize(String string, char... prefix) {
            if (prefix.length == 0) prefix = new char[]{'&', '§'};
            for (char c : prefix) for (Minecraft minecraft : Minecraft.values()) {
                string = string.replace(String.valueOf(c) + minecraft.getIdentifier(), "§" + minecraft.getIdentifier());
            }
            return string;
        }

        public static String getColor(String hex) {
            if (hex.length() >= 7) {
                StringBuilder s = new StringBuilder("§x");
                for (String s1 : hex.substring(1, 7).split("")) s.append("§").append(s1);
                return s.toString();
            }
            return hex;
        }
    }
}