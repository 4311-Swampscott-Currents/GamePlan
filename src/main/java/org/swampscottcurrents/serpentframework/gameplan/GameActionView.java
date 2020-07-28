package org.swampscottcurrents.serpentframework.gameplan;

public class GameActionView {

    protected GamePlanWidget widget;
    private boolean hideAction;

    public GameActionView(GamePlanWidget w) {
        widget = w;
    }

    public GamePlanWidget getWidget() {
        return widget;
    }

    public double getEndPositionX() {
        return 0;
    }

    public double getEndPositionY() {
        return 0;
    }

    public double getDisplayPositionX() {
        return getEndPositionX();
    }

    public double getDisplayPositionY() {
        return getEndPositionY();
    }

    public void update(GameActionView lastAction) {}

    public void destroy() {}

    public void show() {}

    public void hide() {}

    public void edit() {}

    public void setHidden(boolean hide) {
        hideAction = hide;
        if(hideAction) {
            hide();
        }
        else {
            show();
        }
    }

    public boolean isHidden() {
        return hideAction;
    }
}