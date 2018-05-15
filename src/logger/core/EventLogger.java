package logger.core;

public interface EventLogger {
    // this assumes it will be logged every tick
    EventLogger log(Object event);
    Trajectory addTrajectory();
}
