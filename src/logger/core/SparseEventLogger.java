package logger.core;

public interface SparseEventLogger {
    // these are events that occur occasionally, so note when they occur
    SparseEventLogger log(Object event, int tick);
}
