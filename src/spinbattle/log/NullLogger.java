package spinbattle.log;

import logger.core.EventLogger;
import logger.core.Trajectory;

public class NullLogger implements EventLogger {
    public EventLogger log(Object event) {
        // do nothing
        return this;
    }

    @Override
    public Trajectory addTrajectory() {
        return null;
    }
}
