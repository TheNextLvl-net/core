package logger;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;

class ColoredLogger extends LegacyAbstractLogger {

    @Override
    protected @Nullable String getFullyQualifiedCallerName() {
        return null;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
        switch (level) {
            case INFO -> ColoredPrintStream.info.print(message, arguments, throwable);
            case WARN -> ColoredPrintStream.warn.print(message, arguments, throwable);
            case DEBUG -> ColoredPrintStream.debug.print(message, arguments, throwable);
            case ERROR -> ColoredPrintStream.error.print(message, arguments, throwable);
            case TRACE -> ColoredPrintStream.trace.print(message, arguments, throwable);
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return ColoredPrintStream.error.getCondition().getAsBoolean();
    }

    @Override
    public boolean isDebugEnabled() {
        return ColoredPrintStream.debug.getCondition().getAsBoolean();
    }

    @Override
    public boolean isInfoEnabled() {
        return ColoredPrintStream.info.getCondition().getAsBoolean();
    }

    @Override
    public boolean isWarnEnabled() {
        return ColoredPrintStream.warn.getCondition().getAsBoolean();
    }

    @Override
    public boolean isErrorEnabled() {
        return ColoredPrintStream.error.getCondition().getAsBoolean();
    }
}
