package core.file;

import org.jspecify.annotations.NullMarked;

/**
 * The Validatable interface defines a contract for objects that can be validated.
 * It declares methods for validating an object with a given scope and provides
 * a default method for validation using a predefined scope.
 *
 * @param <R> The type of the root element to be validated
 */
@NullMarked
@FunctionalInterface
public interface Validatable<R> {
    /**
     * Validate the current object providing a {@link Scope scope}
     *
     * @param scope The validation scope
     * @return the validated FileIO instance
     */
    FileIO<R> validate(Scope scope);

    /**
     * Validate the current object using {@link Scope#FILTER_AND_FILL FILTER_AND_FILL}
     *
     * @return the validated FileIO instance
     */
    default FileIO<R> validate() {
        return validate(Scope.FILTER_AND_FILL);
    }

    /**
     * Enumeration representing the scope of validation for a Validatable object.
     */
    enum Scope {
        /**
         * This scope will filter unused and fill in missing data
         */
        FILTER_AND_FILL,
        /**
         * This scope will only filter unused data
         */
        FILTER,
        /**
         * This scope will only fill in missing data
         */
        FILL;

        /**
         * Determines whether the current scope involves filtering unused data.
         *
         * @return whether this scope is filtering unused data
         */
        public boolean isFiltering() {
            return equals(FILTER) || equals(FILTER_AND_FILL);
        }

        /**
         * Determines whether the current scope involves filling in missing data.
         *
         * @return whether this scope is filling in missing data
         */
        public boolean isFilling() {
            return equals(FILL) || equals(FILTER_AND_FILL);
        }
    }
}
