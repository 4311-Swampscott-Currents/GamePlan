package org.swampscottcurrents.serpentframework.gameplan.infiniterecharge;

import org.swampscottcurrents.serpentframework.gameplan.*;

public class FireBalls extends GameAction {
    @Override
    public GameActionView createView(GamePlanWidget widget) {
        return new StationaryActionView(widget, this, new String(Character.toChars(0x1f525)), "#fc3d44");
    }

    @Override
    public String toString() {
        return "Fire balls";
    }
}