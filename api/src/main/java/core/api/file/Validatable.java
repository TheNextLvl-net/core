package core.api.file;

@FunctionalInterface
public interface Validatable<T extends FileIO<?, T>> {
    /**
     * Validate the current object providing a {@link Scope scope}
     *
     * @param scope The validation scope
     * @return the validated FileIO instance
     */
    T validate(Scope scope);

    /**
     * Validate the current object using {@link Scope#FILTER_AND_FILL FILTER_AND_FILL}
     *
     * @return the validated FileIO instance
     */
    default T validate() {
        return validate(Scope.FILTER_AND_FILL);
    }

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
         * @return whether this scope is filtering unused data
         */
        public boolean isFiltering() {
            return equals(FILTER) || equals(FILTER_AND_FILL);
        }

        /**
         * @return whether this scope is filling in missing data
         */
        public boolean isFilling() {
            return equals(FILL) || equals(FILTER_AND_FILL);
        }
    }
}
