package org.swampscottcurrents.serpentframework.gameplan;

/** Represents the starting position of the robot, or where it has been placed on the field. */
public class RobotPosition extends GameAction {
    /** The horizontal position, in feet, from the top-left corner of the playing field where the robot has been placed. */
    public double positionX = 5;
    /** The vertical position, in feet, from the top-left corner of the playing field where the robot has been placed. */
    public double positionY = 5;

    /** Creates a GameActionView used to graphically represent this action. */
    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new RobotPositionView(widget, this);
    }
}