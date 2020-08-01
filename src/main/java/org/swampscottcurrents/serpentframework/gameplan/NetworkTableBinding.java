package org.swampscottcurrents.serpentframework.gameplan;

import edu.wpi.first.networktables.*;

/** Provides static methods for robot communication over NetworkTables. */
public class NetworkTableBinding {
    /** Returns whether Shuffleboard is currently connected to the robot. */
    public static boolean isConnected() {
        return NetworkTableInstance.getDefault().isConnected();
    }

    /** Returns whether the robot is on red alliance. */
    public static boolean isRobotOnRedAlliance() {
        return NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);
    }

    /** Retrieves the current robot yaw rotation, as reported by the robot gyroscope. */
    public static double getRobotRotation() {
        return NetworkTableInstance.getDefault().getTable("GamePlan").getEntry("RobotRotation").getDouble(0);
    }

    /** Pushes a GamePlan to NetworkTables so that the robot can use it. */
    public static void publishGamePlan(String plan) {
        NetworkTableInstance.getDefault().getTable("GamePlan").getEntry("Plan").setString(plan);
    }
}