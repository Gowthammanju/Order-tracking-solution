scan("15 seconds")

def logLevel = INFO

def appAppender = "app-appender"

appender(appAppender, RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyyMMddHHmmss.SSS};%C{0};%mdc{req_id};%.-1level;%msg;%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = 'target/%d{yyyyMMdd}_demo_test.log.gz'
            maxHistory = 30
    }
}

logger("org.springframework", logLevel, [appAppender], false)
logger("de.telekom.pd", logLevel, [appAppender], false)

root(logLevel, [appAppender])
