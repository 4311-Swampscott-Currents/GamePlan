package org.swampscottcurrents.serpentframework.gameplan;

import edu.wpi.first.networktables.*;

public class NetworkTableBinding {
    public static boolean isConnected() {
        return NetworkTableInstance.getDefault().isConnected();
    }

    public static boolean isRobotOnRedAlliance() {
        return NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsRedAlliance").getBoolean(true);
    }

    public static double getRobotRotation() {
        return NetworkTableInstance.getDefault().getTable("GamePlan").getEntry("RobotRotation").getDouble(0);
    }

    public static void publishGamePlan(String plan) {
        NetworkTableInstance.getDefault().getTable("GamePlan").getEntry("Plan").setString(plan);
    }
}