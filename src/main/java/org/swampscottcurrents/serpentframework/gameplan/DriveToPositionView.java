package org.swampscottcurrents.serpentframework.gameplan;

import java.io.IOException;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.robot.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;

/** A graphical element which displays information about a DriveToPositionAction. */
public class DriveToPositionView extends GameActionView {
    
    /** The action that this view represents. */
    protected DriveToPosition viewData;
    /** The Pane object that represents the position arrow, centered at the previous location and pointing towards the target position. */
    protected AnchorPane arrowPane;
    /** The shaft of the arrow. */
    protected AnchorPane arrowShaft;
    /** The triangular head of the arrow. */
    protected Polygon arrowHead;
    /** Whether the user is currently dragging the arrow. */
    protected boolean isDragging = false;

    /** Creates a new DriveToPositionView instance with the specified widget and action data. */
    public DriveToPositionView(GamePlanWidget w, DriveToPosition data) {
        super(w);
        viewData = data;
        try {
            arrowPane = FXMLLoader.load(widget.getClass().getResource("actionviews/DriveToPositionView.fxml"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        arrowPane.setOnMouseClicked(ev -> click(ev));
        arrowPane.setVisible(false);
        widget.getFieldPane().getChildren().add(arrowPane);

        arrowShaft = (AnchorPane)arrowPane.getChildren().get(0);
        arrowHead = (Polygon)((AnchorPane)arrowShaft.getChildren().get(0)).getChildren().get(0);
    }

    /** Updates this view with the most current information. This is called once every frame. */
    @Override
    public void update(GameActionView lastView) {
        if(widget.getSelectedAction() == viewData) {
            setArrowColor("#ffff00");
            if(isDragging) {
                Point2D position = widget.getFieldPane().screenToLocal(new Robot().getMousePosition());
                viewData.positionX = clamp(widget.fieldWidthToFeet(position.getX()), 0, GamePlanWidget.FIELD_WIDTH_FEET);
                viewData.positionY = clamp(widget.fieldHeightToFeet(position.getY()), 0, GamePlanWidget.FIELD_HEIGHT_FEET);
            }
        }
        else {
            isDragging = false;
            setArrowColor(viewData.driveBackwards ? "#ff8800" : "#00ffff");
        }

        arrowPane.setLayoutX(lastView.getEndPositionX());
        arrowPane.setLayoutY(lastView.getEndPositionY());
        Point2D translate = new Point2D(widget.fieldWidthToPixels(viewData.positionX), widget.fieldHeightToPixels(viewData.positionY)).subtract(arrowPane.getLayoutX(), arrowPane.getLayoutY());
        arrowPane.getTransforms().clear();
        arrowPane.getTransforms().add(new Rotate(Math.toDegrees(Math.atan2(translate.getY(), translate.getX()))));
        arrowPane.setPrefWidth(translate.magnitude());
        arrowPane.setVisible(!isHidden());
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
        return getEndPositionY() - 20;
    }

    /** Destroys this view, causing it to remove the arrow from the GUI. */
    @Override
    public void destroy() {
        widget.getFieldPane().getChildren().remove(arrowPane);
    }

    /** Called whenever the user clicks on this view. */
    protected void click(MouseEvent ev) {
        if(ev.isAltDown()) {
            widget.setSelectedAction(viewData);
            isDragging = true;
        }
        else if(widget.getSelectedAction() == viewData) {
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
        arrowPane.setVisible(true);
    }

    /** Makes this view invisible. */
    @Override
    public void hide() {
        arrowPane.setVisible(false);
    }

    /** This method is called when a user clicks the "edit" button. It allows them to change the speed and driving direction of the robot while it performs this action. */
    @Override
    public void edit() {
        ButtonType changeSpeed = new ButtonType("Edit speed");
        ButtonType changeDirection = new ButtonType("Drive " + (viewData.driveBackwards ? "forwards" : "backwards"));
        Alert alert = new Alert(AlertType.INFORMATION, viewData.toString(), changeSpeed, changeDirection, ButtonType.CANCEL);
        ButtonType button = alert.showAndWait().get();
        if(button == changeSpeed) {
            ButtonType defaultSpeed = new ButtonType("Default");
            alert = new Alert(Alert.AlertType.NONE);
            alert.setHeaderText("At what speed should the robot drive (in feet per second)?");
            alert.getButtonTypes().add(ButtonType.OK);
            alert.getButtonTypes().add(defaultSpeed);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            NumericTextField number = new NumericTextField(viewData.speed > 0 ? viewData.speed : 0);
            alert.getDialogPane().expandableContentProperty().set(number);
            alert.getDialogPane().setExpanded(true);
            button = alert.showAndWait().get();
            if(button == ButtonType.OK) {
                try {
                    viewData.speed = number.getNumber();
                }
                catch(Exception e) {
                    new Alert(AlertType.ERROR, "Input number was not in a valid format.");
                }
            }
            else if(button == defaultSpeed) {
                viewData.speed = -1;
            }
        }
        else if(button == changeDirection) {
            viewData.driveBackwards = !viewData.driveBackwards;
        }
    }

    /** Sets the color of the GUI arrow. */
    protected void setArrowColor(String color) {
        arrowShaft.setStyle("-fx-background-color:" + color);
        arrowHead.setFill(Paint.valueOf(color));
    }

    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}