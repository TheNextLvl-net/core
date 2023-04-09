package core.api.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class LoggerFactory implements ILoggerFactory {
    private static final ColoredLogger logger = new ColoredLogger();

    @Override
    public Logger getLogger(String name) {
        return logger;
    }
}
