import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import java.nio.charset.StandardCharsets

String localhost = InetAddress.getLocalHost().getHostName()

static String getProp(String key) {
    return System.getProperty(key) ?: System.getenv(key) ?: ''
}


String level = getProp("logback_level")
String path = getProp("logback_path")
path = !path || path.endsWith('/') ? path : path + '/'
logPattern = "%date $localhost[%thread] %.-5level %logger{32} - [%mdc{sage.correlationId}] %message%n%exception{32}"

appender('CONSOLE', ConsoleAppender) {
    filter(ThresholdFilter) {
        level = Level.toLevel(level, Level.WARN)
    }
    encoder(PatternLayoutEncoder) {
        charset = StandardCharsets.UTF_8
        pattern = logPattern
    }
}

logger("springfox.documentation.schema", Level.WARN)
logger("springfox.documentation.spring", Level.WARN)
if (!level) {
    appender('FILE', RollingFileAppender) {
        file = path + "sage.log"
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = path + "sage.%d{yyyy-MM-dd}.log"
            maxHistory = 30
        }
        filter(ThresholdFilter) {
            level = Level.INFO
        }
        encoder(PatternLayoutEncoder) {
            charset = StandardCharsets.UTF_8
            pattern = logPattern
        }
    }
    logger("org.springframework", Level.WARN)
    root(Level.toLevel(level, Level.INFO), ['CONSOLE', 'FILE'])
} else {
    root(Level.toLevel(level, Level.INFO), ['CONSOLE'])
}
