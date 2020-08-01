package org.swampscottcurrents.serpentframework.gameplan;

import java.io.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

/** A graphical element which displays information about an action that takes place while the robot is stationary, represented as a colored dot above the robot's location. */
public class StationaryActionView extends GameActionView {
    /** The action that this view represents. */
    protected GameAction viewData;
    /** The Pane object that reperesents the action dot. */
    protected AnchorPane mainPane;
    /** The Circle that represents the dot itself. */
    protected Circle bubble;
    /** The label used to render the emblem which should appear on the dot. */
    protected Label bubbleText;
    /** The default color of the dot. */
    protected String defaultColor = "#1E90FF";
    /** The horizontal position of the previously-rendered element. */
    protected double lastX;
    /** The vertical position of the previously-rendered element. */
    protected double lastY;
    /** The horizontal display position of the previously-rendered element. */
    protected double lastDisplayX;
    /** The vertical display position of the previously-rendered element. */
    protected double lastDisplayY;

    /** Creates a new StationaryActionView instance with the specified widget and action data. */
    public StationaryActionView(GamePlanWidget w, GameAction data) {
        super(w);
        viewData = data;

        try {
            mainPane = FXMLLoader.load(widget.getClass().getResource("actionviews/StationaryActionView.fxml"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        mainPane.setVisible(false);
        for(Node node : ((Pane)mainPane.getChildren().get(0)).getChildren()) {
            if(node instanceof Circle) {
                bubble = (Circle)node;
            }
            else {
                bubbleText = (Label)node;
            }
        }
        mainPane.getChildren().get(0).setOnMouseClicked(ev -> click(ev));
        widget.getFieldPane().getChildren().add(mainPane);
    }

    /** Creates a new StationaryActionView instance with the specified widget, action data, and text. */
    public StationaryActionView(GamePlanWidget w, GameAction data, String text) {
        this(w, data);
        setText(text);
    }

    /** Creates a new StationaryActionView instance with the specified widget, action data, text, and color. */
    public StationaryActionView(GamePlanWidget w, GameAction data, String text, String color) {
        this(w, data);
        setText(text);
        defaultColor = color;
    }

    /** Updates this view with the most current information. This is called once every frame. */
    @Override
    public void update(GameActionView lastView) {
        mainPane.setVisible(true);
        if(widget.getSelectedAction() == viewData) {
            bubble.setFill(Paint.valueOf("#ffff00"));
        }
        else {
            bubble.setFill(Paint.valueOf(defaultColor));
        }
        mainPane.setLayoutX(lastDisplayX = lastView.getDisplayPositionX());
        mainPane.setLayoutY(lastDisplayY = lastView.getDisplayPositionY());
        lastX = lastView.getEndPositionX();
        lastY = lastView.getEndPositionY();
    }

    /** Destroys this view, causing it to remove the arrow from the GUI. */
    @Override
    public void destroy() {
        widget.getFieldPane().getChildren().remove(mainPane);
    }

    /** Gets a horizontal position where the next GUI element may be rendered without overlapping this one. */
    @Override
    public double getDisplayPositionX() {
        return lastDisplayX;
    }

    /** Gets a vertical position where the next GUI element may be rendered without overlapping this one. */
    @Override
    public double getDisplayPositionY() {
        return lastDisplayY - 30;
    }

    /** Gets the horizontal location, in pixels, of where the robot will be when it finishes this action. */
    @Override
    public double getEndPositionX() {
        return lastX;
    }

    /** Gets the vertical location, in pixels, of where the robot will be when it finishes this action. */
    @Override
    public double getEndPositionY() {
        return lastY;
    }

    private void click(MouseEvent ev) {
        if(widget.getSelectedAction() == viewData) {
            widget.setSelectedAction(null);
        }
        else {
            widget.setSelectedAction(viewData);
        }
        ev.consume();
    }

    /** Makes this view visible. */
    @Override
    public void show() {
        bubble.getParent().setVisible(true);
    }

    /** Makes this view invisible. */
    @Override
    public void hide() {
        bubble.getParent().setVisible(false);
    }

    /** Sets the text that is being display on the dot. */
    public void setText(String text) {
        bubbleText.setText(text);
    }

    /** Gets the text that is being displayed on the dot. */
    public String getText() {
        return bubbleText.getText();
    }
}