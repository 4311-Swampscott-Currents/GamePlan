package org.swampscottcurrents.serpentframework.gameplan;

import java.util.ArrayList;
import douglasdwyer.royaljson.*;

public class GamePlan {
    public ArrayList<GameAction> actions = new ArrayList<GameAction>();

    public GamePlan() {
        actions.add(new RobotPosition());
    }

    public String serialize() {
        return new RoyalJsonSerializer().serialize(this);
    }

    public static GamePlan deserialize(String json) {
        return new RoyalJsonSerializer().deserializeTo(json);
    }
}