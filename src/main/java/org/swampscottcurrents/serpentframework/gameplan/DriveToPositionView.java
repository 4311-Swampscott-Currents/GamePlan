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

public class DriveToPositionView extends GameActionView {
    
    protected DriveToPosition viewData;
    protected AnchorPane arrowPane;
    protected AnchorPane arrowShaft;
    protected Polygon arrowHead;
    protected boolean isDragging = false;

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

    @Override
    public double getEndPositionX() {
        return widget.fieldWidthToPixels(viewData.positionX);
    }

    @Override
    public double getEndPositionY() {
        return widget.fieldHeightToPixels(viewData.positionY);
    }

    @Override
    public double getDisplayPositionX() {
        return getEndPositionX();
    }

    @Override
    public double getDisplayPositionY() {
        return getEndPositionY() - 20;
    }

    @Override
    public void destroy() {
        widget.getFieldPane().getChildren().remove(arrowPane);
    }

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

    @Override
    public void show() {
        arrowPane.setVisible(true);
    }

    @Override
    public void hide() {
        arrowPane.setVisible(false);
    }

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

    protected void setArrowColor(String color) {
        arrowShaft.setStyle("-fx-background-color:" + color);
        arrowHead.setFill(Paint.valueOf(color));
    }

    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}