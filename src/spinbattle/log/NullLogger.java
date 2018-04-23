package spinbattle.log;

import logger.core.EventLogger;

public class NullLogger implements EventLogger {
    public EventLogger log(Object event) {
        // do nothing
        return this;
    }
}
