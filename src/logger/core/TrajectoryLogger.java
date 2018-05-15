package logger.core;

import java.util.ArrayList;

public class TrajectoryLogger {
    public ArrayList<Trajectory> trajectories = new ArrayList<>();

    public Trajectory addTrajectory() {
        Trajectory trajectory = new Trajectory();
        trajectories.add(trajectory);
        return trajectory;
    }
}
