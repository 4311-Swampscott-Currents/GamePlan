package org.swampscottcurrents.serpentframework.gameplan.infiniterecharge;

import org.swampscottcurrents.serpentframework.gameplan.*;

public class WaitForSeconds extends GameAction {
    public double seconds = 1;

    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new WaitForSecondsView(widget, this);
    }

    @Override
    public String toString() {
        return "Wait for " + seconds + " second(s)";
    }
}