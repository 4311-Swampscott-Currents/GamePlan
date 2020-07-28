package org.swampscottcurrents.serpentframework.gameplan;

public interface IGameActionGenerator {
    public String getName();
    public GameAction createAction();
}