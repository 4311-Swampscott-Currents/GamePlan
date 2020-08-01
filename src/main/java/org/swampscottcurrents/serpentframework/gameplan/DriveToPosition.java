package org.swampscottcurrents.serpentframework.gameplan;

/** Represents an action where the robot turns toward and then drives to a certain position. */
public class DriveToPosition extends GameAction {
    /** The horizontal position, in feet, from the top-left corner of the playing field that the robot should drive to. */
    public double positionX;
    /** The vertical position, in feet, from the top-left corner of the playing field that the robot should drive to. */
    public double positionY;
    /** What speed the robot should drive at. If negative, the robot should use its default speed. */
    public double speed = -1;
    /** Whether the robot should drive backwards. */
    public boolean driveBackwards = false;

    /** Creates a new DriveToPosition action with default values. */
    public DriveToPosition() {}

    /** Creates a new DriveToPosition action with the specified coordinates as the target position. */
    public DriveToPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    /** Creates a GameActionView used to graphically represent this action. */
    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new DriveToPositionView(widget, this);
    }

    /** Gets a string representation of this object. */
    @Override
    public String toString() {
        if(speed > 0) {
            return "Drive " + (driveBackwards ? "backwards " : "") + speed + " ft/s to (" + toFeetAndInches(positionX) + ", " + toFeetAndInches(positionY) + ")";
        }
        else {
            return "Drive " + (driveBackwards ? "backwards " : "") + "to (" + toFeetAndInches(positionX) + ", " + toFeetAndInches(positionY) + ")";
        }
    }

    private String toFeetAndInches(double feet) {
        int totalFeet = (int)Math.floor(feet);
        return totalFeet + "\'" + Math.round(12 * (feet - totalFeet)) + "\"";
    }
}