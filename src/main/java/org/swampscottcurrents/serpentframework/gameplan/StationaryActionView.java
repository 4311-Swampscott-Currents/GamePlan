package org.swampscottcurrents.serpentframework.gameplan;

import java.io.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class StationaryActionView extends GameActionView {

    protected GameAction viewData;
    protected AnchorPane mainPane;
    protected Circle bubble;
    protected Label bubbleText;
    protected String defaultColor = "#1E90FF";
    protected double lastX, lastY, lastDisplayX, lastDisplayY;

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

    public StationaryActionView(GamePlanWidget w, GameAction data, String text) {
        this(w, data);
        setText(text);
    }

    public StationaryActionView(GamePlanWidget w, GameAction data, String text, String color) {
        this(w, data);
        setText(text);
        defaultColor = color;
    }

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

    @Override
    public void destroy() {
        widget.getFieldPane().getChildren().remove(mainPane);
    }

    @Override
    public double getDisplayPositionX() {
        return lastDisplayX;
    }

    @Override
    public double getDisplayPositionY() {
        return lastDisplayY - 30;
    }

    @Override
    public double getEndPositionX() {
        return lastX;
    }

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

    @Override
    public void show() {
        bubble.getParent().setVisible(true);
    }

    @Override
    public void hide() {
        bubble.getParent().setVisible(false);
    }

    public void setText(String text) {
        bubbleText.setText(text);
    }

    public String getText() {
        return bubbleText.getText();
    }
}