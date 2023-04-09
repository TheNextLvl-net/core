package logger;

import core.annotation.FieldsAreNullableByDefault;
import lombok.Getter;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

@Getter
@FieldsAreNullableByDefault
public class LoggerService implements SLF4JServiceProvider {
    private ILoggerFactory loggerFactory;
    private IMarkerFactory markerFactory;
    private MDCAdapter mDCAdapter;

    @Override
    public String getRequestedApiVersion() {
        return "2.0.6";
    }

    @Override
    public void initialize() {
        loggerFactory = new LoggerFactory();
        markerFactory = new BasicMarkerFactory();
        mDCAdapter = new NOPMDCAdapter();
        System.setErr(ColoredPrintStream.error);
        System.setOut(ColoredPrintStream.info);
    }
}
