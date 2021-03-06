package org.swampscottcurrents.serpentframework.gameplan;

import java.io.*;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.robot.*;
import javafx.scene.transform.Rotate;

/** A graphical element which displays information about a RobotPosition. */
public class RobotPositionView extends GameActionView {

    /** The length of the robot in feet. */
    public static final double ROBOT_LENGTH_FEET = 2.692;
    /** The width of the robot in feet. */
    public static final double ROBOT_GIRTH_FEET = 2.25;
    /** The action that this view represents. */
    protected RobotPosition viewData;
    /** The Pane object that represents the robot. */
    protected AnchorPane robotPane;
    /** The Pane object that represents the vertical position bar. */
    protected AnchorPane verticalRobotPositionBar;
    /** The Pane object that represents the horizontal position bar. */
    protected AnchorPane horizontalRobotPositionBar;

    /** Creates a new RobotPositionView instance with the specified widget and action data. */
    public RobotPositionView(GamePlanWidget widget, RobotPosition data) {
        super(widget);
        viewData = data;
        initialize();
    }

    private void initialize() {
        try {
            robotPane = FXMLLoader.load(widget.getClass().getResource("actionviews/RobotPositionView.fxml"));
            robotPane.setPrefWidth(widget.fieldWidthToPixels(ROBOT_LENGTH_FEET));
            robotPane.setPrefHeight(widget.fieldHeightToPixels(ROBOT_GIRTH_FEET));
            robotPane.setMinWidth(robotPane.getPrefWidth());
            robotPane.setMinHeight(robotPane.getPrefHeight());
            robotPane.setOnMouseClicked(ev -> click(ev));
            widget.getFieldPane().getChildren().add(robotPane);

            verticalRobotPositionBar = FXMLLoader.load(widget.getClass().getResource("actionviews/VerticalRobotPositionBar.fxml"));
            verticalRobotPositionBar.setLayoutY(0);
            widget.getFieldPane().getChildren().add(verticalRobotPositionBar);

            horizontalRobotPositionBar = FXMLLoader.load(widget.getClass().getResource("actionviews/HorizontalRobotPositionBar.fxml"));
            horizontalRobotPositionBar.setLayoutX(0);
            widget.getFieldPane().getChildren().add(horizontalRobotPositionBar);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void click(MouseEvent event) {
        GameAction selected = widget.getSelectedAction();
        if(selected == viewData) {
            widget.setSelectedAction(null);
        }
        else {
            widget.setSelectedAction(viewData);
        }
        event.consume();
    }

    /** Updates this view with the most current information. This is called once every frame. */
    @Override
    public void update(GameActionView lastView) {
        robotPane.toFront();
        robotPane.setStyle("-fx-background-color:" + (widget.isRedAlliance() ? "#ff0000" : "#0000ff"));
        robotPane.getTransforms().clear();
        robotPane.getTransforms().add(new Rotate(widget.getRobotRotation(), robotPane.getWidth() / 2, robotPane.getHeight() / 2));
        if(widget.getSelectedAction() == viewData) {
            double l = ROBOT_LENGTH_FEET / 2, g = ROBOT_GIRTH_FEET / 2;
            Point2D position = widget.getFieldPane().screenToLocal(new Robot().getMousePosition());
            position = new Point2D(
                clamp(widget.fieldWidthToFeet(position.getX()), l, GamePlanWidget.FIELD_WIDTH_FEET - l),
                clamp(widget.fieldHeightToFeet(position.getY()), g, GamePlanWidget.FIELD_HEIGHT_FEET - g));
            viewData.positionX = position.getX();
            viewData.positionY = position.getY();
        }
        robotPane.setLayoutX(widget.fieldWidthToPixels(viewData.positionX) - robotPane.getWidth() / 2);
        robotPane.setLayoutY(widget.fieldHeightToPixels(viewData.positionY) - robotPane.getHeight() / 2);
        updatePositionBars();
    }

    private void updatePositionBars() {
        Point2D chosenPoint = getVertexClosestToTopLeft();

        verticalRobotPositionBar.setLayoutX(chosenPoint.getX());
        verticalRobotPositionBar.setPrefHeight(chosenPoint.getY());
        double measurement = widget.fieldHeightToFeet(chosenPoint.getY());
        int feet = (int)Math.floor(measurement);
        String measurementText = feet + "'" + Math.round(12 * (measurement - feet)) + "\"";
        measurement = GamePlanWidget.FIELD_HEIGHT_FEET - measurement;
        feet = (int)Math.floor(measurement);
        measurementText += " (op. " + feet + "'" + Math.round(12 * (measurement - feet)) + "\")";
        ((Label)verticalRobotPositionBar.getChildren().get(0)).setText(measurementText);

        horizontalRobotPositionBar.setLayoutY(chosenPoint.getY());
        horizontalRobotPositionBar.setPrefWidth(chosenPoint.getX());
        measurement = widget.fieldWidthToFeet(chosenPoint.getX());
        feet = (int)Math.floor(measurement);
        measurementText = feet + "'" + Math.round(12 * (measurement - feet)) + "\"";
        ((Label)horizontalRobotPositionBar.getChildren().get(0)).setText(measurementText);
    }

    private Point2D getVertexClosestToTopLeft() {
        double rx = robotPane.getLayoutX(), ry = robotPane.getLayoutY(), rl = robotPane.getWidth(), rh = robotPane.getHeight();
        Rotate rotator = new Rotate(widget.getRobotRotation(), rx + rl / 2, ry + rh / 2);
        Point2D closestPoint = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        for(int x = 0; x < 2; x++) {
            for(int y = 0; y < 2; y++) {
                Point2D rotatedPoint = rotator.transform(rx + x * rl, ry + y * rh);
                if(rotatedPoint.magnitude() < closestPoint.magnitude()) {
                    closestPoint = rotatedPoint;
                }
            }
        }
        return closestPoint;
    }

    /** Destroys this view, causing it to remove the arrow from the GUI. */
    @Override
    public void destroy() {
        widget.getFieldPane().getChildren().remove(robotPane);
        widget.getFieldPane().getChildren().remove(verticalRobotPositionBar);
        widget.getFieldPane().getChildren().remove(horizontalRobotPositionBar);
    }

    /** Gets the horizontal location, in pixels, of where the robot will be when it finishes this action. */
    @Override
    public double getEndPositionX() {
        return widget.fieldWidthToPixels(viewData.positionX);
    }

    /** Gets the vertical location, in pixels, of where the robot will be when it finishes this action. */
    @Override
    public double getEndPositionY() {
        return widget.fieldHeightToPixels(viewData.positionY);
    }

    /** Gets a horizontal position where the next GUI element may be rendered without overlapping this one. */
    @Override
    public double getDisplayPositionX() {
        return getEndPositionX();
    }

    /** Gets a vertical position where the next GUI element may be rendered without overlapping this one. */
    @Override
    public double getDisplayPositionY() {
        return getEndPositionY() - robotPane.getHeight() - 5;
    }

    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}