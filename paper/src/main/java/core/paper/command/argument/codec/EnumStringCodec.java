package core.paper.command.argument.codec;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Interface for encoding and decoding between {@link Enum} constants and their string representations.
 * The implementations of this interface can define custom transformations for the names of the enum constants
 * during encoding and decoding operations.
 *
 * @since 2.3.0
 */
@NullMarked
public interface EnumStringCodec {
    /**
     * Converts the specified enum constant to its string representation.
     *
     * @param value the enum constant to be converted to a string
     * @return the string representation of the given enum constant
     */
    @Contract(pure = true)
    String toString(Enum<?> value);

    /**
     * Converts a given string into its corresponding enum constant of the specified enum type.
     *
     * @param <E>       the enum type to which the string will be converted
     * @param enumClass the class object of the enum type
     * @param string    the string representation of the enum constant
     * @return the enum constant corresponding to the given string
     * @throws IllegalArgumentException if the string does not match any of the enum constants
     *                                  in the specified enum class
     */
    @Contract(pure = true)
    <E extends Enum<E>> E fromString(Class<E> enumClass, String string) throws IllegalArgumentException;


    /**
     * Returns an {@link EnumStringCodec} that performs no transformation on the names of enum constants.
     *
     * @return an identity {@link EnumStringCodec} that retains the original names of enum constants.
     */
    @Contract(pure = true)
    static EnumStringCodec identity() {
        return EnumStringCodecs.IDENTITY;
    }

    /**
     * Returns an {@link EnumStringCodec} that replaces underscores with hyphens in the names of enum constants.
     *
     * @return an {@link EnumStringCodec} that transforms enum constant names by replacing underscores with hyphens.
     */
    @Contract(pure = true)
    static EnumStringCodec hyphen() {
        return EnumStringCodecs.HYPHEN;
    }

    /**
     * Returns an {@link EnumStringCodec} that transforms the names of enum constants to uppercase.
     *
     * @return an {@link EnumStringCodec} that converts enum constant names to their uppercase representation.
     */
    @Contract(pure = true)
    static EnumStringCodec uppercase() {
        return EnumStringCodecs.UPPERCASE;
    }

    /**
     * Returns an {@link EnumStringCodec} that transforms the names of enum constants to lowercase.
     *
     * @return an {@link EnumStringCodec} that converts enum constant names to their lowercase representation.
     */
    @Contract(pure = true)
    static EnumStringCodec lowercase() {
        return EnumStringCodecs.LOWERCASE;
    }

    /**
     * Returns an {@link EnumStringCodec} that transforms the names of enum constants to uppercase
     * and replaces underscores with hyphens.
     *
     * @return an {@link EnumStringCodec} that converts enum constant names to their uppercase
     * representation and replaces underscores with hyphens.
     */
    @Contract(pure = true)
    static EnumStringCodec upperHyphen() {
        return EnumStringCodecs.UPPER_HYPHEN;
    }

    /**
     * Returns an {@link EnumStringCodec} that transforms the names of enum constants to lowercase
     * and replaces underscores with hyphens.
     *
     * @return an {@link EnumStringCodec} that converts enum constant names to their lowercase
     * representation and replaces underscores with hyphens.
     */
    @Contract(pure = true)
    static EnumStringCodec lowerHyphen() {
        return EnumStringCodecs.LOWER_HYPHEN;
    }
}