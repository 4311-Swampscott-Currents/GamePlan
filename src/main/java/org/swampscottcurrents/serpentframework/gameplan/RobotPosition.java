package org.swampscottcurrents.serpentframework.gameplan;

public class RobotPosition extends GameAction {
    public double positionX = 5, positionY = 5;

    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new RobotPositionView(widget, this);
    }
}