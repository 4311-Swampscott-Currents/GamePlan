package org.swampscottcurrents.serpentframework.gameplan;

public class DriveToPosition extends GameAction {
    public double positionX, positionY;
    public double speed = -1;
    public boolean driveBackwards = false;

    public DriveToPosition() {}

    public DriveToPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new DriveToPositionView(widget, this);
    }

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