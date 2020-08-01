package org.swampscottcurrents.serpentframework.gameplan;

import java.util.ArrayList;
import douglasdwyer.royaljson.*;

/** Represents a unique autonomous sequence that the robot should perform at the beginning of a match. */
public class GamePlan {
    /** A list of game actions that the robot should perform. */
    public ArrayList<GameAction> actions = new ArrayList<GameAction>();

    /** Creates a new GamePlan instance, with no information other than a default robot position. */
    public GamePlan() {
        actions.add(new RobotPosition());
    }

    /** Serializes this GamePlan instance to a JSON string so that it may be stored or transferred across the network. */
    public String serialize() {
        return new RoyalJsonSerializer().serialize(this);
    }

    /** Deserializes a GamePlan instance from a JSON string so that it may be loaded into memory. */
    public static GamePlan deserialize(String json) {
        return new RoyalJsonSerializer().deserializeTo(json);
    }
}