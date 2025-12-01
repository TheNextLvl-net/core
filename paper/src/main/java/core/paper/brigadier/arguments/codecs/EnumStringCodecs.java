package core.paper.brigadier.arguments.codecs;

import java.util.Locale;
import java.util.function.Function;

final class EnumStringCodecs {
    static final EnumStringCodec IDENTITY = new MatchingCodec(Function.identity());
    static final EnumStringCodec HYPHEN = new MatchingCodec(name -> name.replace('_', '-'));
    static final EnumStringCodec LOWERCASE = new MatchingCodec(name -> name.toLowerCase(Locale.ROOT));
    static final EnumStringCodec UPPERCASE = new MatchingCodec(name -> name.toUpperCase(Locale.ROOT));
    static final EnumStringCodec LOWER_HYPHEN = new MatchingCodec(name -> name.toLowerCase(Locale.ROOT).replace('_', '-'));
    static final EnumStringCodec UPPER_HYPHEN = new MatchingCodec(name -> name.toUpperCase(Locale.ROOT).replace('_', '-'));

    private record MatchingCodec(Function<String, String> formatter) implements EnumStringCodec {
        @Override
        public String toString(Enum<?> value) {
            return formatter.apply(value.name());
        }

        @Override
        public <E extends Enum<E>> E fromString(Class<E> enumClass, String string) throws IllegalArgumentException {
            for (var constant : enumClass.getEnumConstants()) {
                if (string.equals(formatter.apply(constant.name()))) return constant;
            }
            throw new IllegalArgumentException("No enum constant for '" + string + "' in " + enumClass.getName());
        }
    }
}