package org.swampscottcurrents.serpentframework.gameplan;

/** A graphical element which displays information about a GameAction. */
public class GameActionView {

    /** The widget that this element is associated with. */
    protected GamePlanWidget widget;
    private boolean hideAction;

    /** Creates a new GameActionView, associating it with the specified widget. */
    public GameActionView(GamePlanWidget w) {
        widget = w;
    }

    /** Gets the widget that this element is associated with. */
    public GamePlanWidget getWidget() {
        return widget;
    }

    /** Gets the horizontal location, in pixels, of where the robot will be when it finishes this action. */
    public double getEndPositionX() {
        return 0;
    }

    /** Gets the vertical location, in pixels, of where the robot will be when it finishes this action. */
    public double getEndPositionY() {
        return 0;
    }

    /** Gets a horizontal position where the next GUI element may be rendered without overlapping this one. */
    public double getDisplayPositionX() {
        return getEndPositionX();
    }

    /** Gets a vertical position where the next GUI element may be rendered without overlapping this one. */
    public double getDisplayPositionY() {
        return getEndPositionY();
    }

    /** Updates this view with the most current information. This is called once every frame. */
    public void update(GameActionView lastAction) {}

    /** Destroys this view, causing it to remove any JavaFX objects from the GUI. */
    public void destroy() {}

    /** Makes this view visible. */
    public void show() {}

    /** Makes this view invisible. */
    public void hide() {}

    /** Called whenever the user clicks the "edit" button with this action selected. Can be used to set auxiliary parameters for the action. */
    public void edit() {}

    /** Sets whether this element should be hidden, calling the show/hide methods as necessary. */
    public void setHidden(boolean hide) {
        if(hide != isHidden()) {
            hideAction = hide;
            if(hideAction) {
                hide();
            }
            else {
                show();
            }
        }
    }

    /** Returns whether this element is currently hidden. */
    public boolean isHidden() {
        return hideAction;
    }
}