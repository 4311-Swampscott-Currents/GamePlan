package org.swampscottcurrents.serpentframework.gameplan;

/** Represents an object that has the ability to generate new GameActions. */
public interface IGameActionGenerator {
    /** The name of the GameAction that this generator creates. */
    public String getName();
    /** Generates a new GameAction instance. */
    public GameAction createAction();
}