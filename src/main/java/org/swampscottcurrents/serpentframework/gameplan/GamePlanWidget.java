package org.swampscottcurrents.serpentframework.gameplan;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.swampscottcurrents.serpentframework.gameplan.infiniterecharge.*;

import edu.wpi.first.shuffleboard.api.data.types.*;
import edu.wpi.first.shuffleboard.api.widget.*;
import javafx.animation.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.util.*;

@Description(dataTypes = { NoneType.class }, name = "GamePlan")
@ParametrizedController(value = "GamePlanWidget.fxml")
public class GamePlanWidget extends SimpleAnnotatedWidget<NoneType> {

    public static final double FIELD_WIDTH_FEET = 54.083;
    public static final double FIELD_HEIGHT_FEET = 26.583;

    public ArrayList<IGameActionGenerator> availableActions = new ArrayList<IGameActionGenerator>();

    private static GamePlan currentPlan;

    private HashMap<GameAction, GameActionView> planDisplayElements = new HashMap<GameAction, GameActionView>();
    private GameAction selectedAction;
    private boolean isRed = true;

    @FXML
    private AnchorPane viewingPane;
    @FXML
    private Pane fieldPane;
    @FXML
    private ImageView fieldImage;
    @FXML
    private VBox robotActionListVBox;
    @FXML
    private ChoiceBox<String> allianceChoiceBox;

    public GamePlanWidget() {
        availableActions.add(new IGameActionGenerator(){
            @Override
            public String getName() {
                return "Fire balls";
            }
        
            @Override
            public GameAction createAction() {
                return new FireBalls();
            }
        });
        availableActions.add(new IGameActionGenerator(){
            @Override
            public String getName() {
                return "Wait for time";
            }
        
            @Override
            public GameAction createAction() {
                WaitForSeconds toReturn = new WaitForSeconds();
                Double time = WaitForSecondsView.askForTimeInSeconds(toReturn.seconds);
                if(time != null) {
                    toReturn.seconds = time;
                }
                return toReturn;
            }
        });
    }

    @Override
    public Pane getView() {
        return viewingPane;
    }

    public Pane getFieldPane() {
        return fieldPane;
    }

    public double fieldWidthToFeet(double pixels) {
        return FIELD_WIDTH_FEET * pixels / fieldPane.getWidth();
    }

    public double fieldHeightToFeet(double pixels) {
        return FIELD_HEIGHT_FEET * pixels / fieldPane.getHeight();
    }

    public double fieldWidthToPixels(double feet) {
        return feet * fieldPane.getWidth() / FIELD_WIDTH_FEET;
    }

    public double fieldHeightToPixels(double feet) {
        return feet * fieldPane.getHeight() / FIELD_HEIGHT_FEET;
    }

    public void setCurrentPlan(GamePlan plan) {
        currentPlan = plan;
    }

    public GamePlan getCurrentPlan() {
        return currentPlan;
    }

    @FXML
    public void initialize() {
        setCurrentPlan(new GamePlan());
        fieldPane.setOnMouseClicked(ev -> onFieldPaneClick(ev));
        
        allianceChoiceBox.getSelectionModel().select("Red");
        allianceChoiceBox.getSelectionModel()
        .selectedItemProperty()
        .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> allianceChoiceBoxChange(newValue));

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(33), event -> {
            if(Components.getDefault().getActiveWidgets().contains(this)) {
                update();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public GameAction getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(GameAction action) {
        selectedAction = action;
    }

    public boolean isRedAlliance() {
        return isRed;
    }

    public double getRobotRotation() {
        return NetworkTableBinding.getRobotRotation();
    }

    public void showAlert(String text) {
        new Alert(AlertType.INFORMATION, text).show();
    }

    protected void onFieldPaneClick(MouseEvent ev) {
        if(getSelectedAction() == null) {
            getCurrentPlan().actions.add(new DriveToPosition(fieldWidthToFeet(ev.getX()), fieldHeightToFeet(ev.getY())));
        }
        else {
            setSelectedAction(null);
        }
        ev.consume();
    }

    private void update() {
        if(NetworkTableBinding.isConnected()) {
            allianceChoiceBox.setDisable(true);
            allianceChoiceBox.getSelectionModel().select(NetworkTableBinding.isRobotOnRedAlliance() ? "Red" : "Blue");
            NetworkTableBinding.publishGamePlan(getCurrentPlan().serialize());
        }
        else {
            allianceChoiceBox.setDisable(false);
        }

        HashMap<GameAction, GameActionView> elements = new HashMap<GameAction, GameActionView>();
        GameActionView lastView = null;
        for(GameAction action : getCurrentPlan().actions) {
            GameActionView view = planDisplayElements.containsKey(action) ? planDisplayElements.get(action) : action.createView(this);
            elements.put(action, view);
            view.update(lastView);
            lastView = view;
        }
        for(GameAction key : planDisplayElements.keySet()) {
            if(!elements.containsKey(key)) {
                GameActionView view = planDisplayElements.get(key);
                view.destroy();
                if(selectedAction == key) {
                    setSelectedAction(null);
                }
            }
        }
        planDisplayElements = elements;
        fieldImage.setRotate(isRedAlliance() ? 0 : 180);
        updateActionList();
    }

    private void updateActionList() {
        ArrayList<RadioButton> buttons = convertNodeListToRadioButtons(robotActionListVBox.getChildren());
        ArrayList<GameAction> actions = getCurrentPlan().actions;
        for(int x = 1; x < actions.size(); x++) {
            RadioButton button;
            if(buttons.size() > 0) {
                button = buttons.get(0);
                buttons.remove(button);
            }
            else {
                button = new RadioButton();
                robotActionListVBox.getChildren().add(button);
            }
            GameAction act = actions.get(x);
            GameActionView view = planDisplayElements.get(act);
            button.setText(act.toString());
            button.setStyle("-fx-text-fill:" + (view.isHidden() ? "gray" : "black"));
            button.setOnMouseClicked(ev -> {
                if(getSelectedAction() == act) {
                    setSelectedAction(null);
                }
                else {
                    setSelectedAction(act);
                }
            });
            button.setSelected(getSelectedAction() == act);
        }
        robotActionListVBox.getChildren().removeAll(buttons);
    }

    private void allianceChoiceBoxChange(String value) {
        isRed = value.toUpperCase().equals("RED");
    }

    private ArrayList<RadioButton> convertNodeListToRadioButtons(ObservableList<Node> list) {
        ArrayList<RadioButton> toReturn = new ArrayList<RadioButton>();
        for(Node node : list) {
            toReturn.add((RadioButton)node);
        }
        return toReturn;
    }

    @FXML
    private void addAction() {
        int insertPosition = 1;
        if(selectedAction == null) {
            insertPosition = getCurrentPlan().actions.size();
        }
        else {
            for(GameAction act : getCurrentPlan().actions) {
                if(act == selectedAction) {
                    break;
                }
                insertPosition++;
            }
        }

        HashMap<ButtonType,IGameActionGenerator> generators = new HashMap<ButtonType,IGameActionGenerator>();
        for(IGameActionGenerator gen : availableActions) {
            generators.put(new ButtonType(gen.getName()), gen);
        }
        generators.put(ButtonType.CANCEL, null);
        Alert alert = new Alert(AlertType.INFORMATION, "Which action should the robot perform?", generators.keySet().toArray(new ButtonType[0]));
        IGameActionGenerator gen = generators.get(alert.showAndWait().get());
        if(gen != null) {
            GameAction act = gen.createAction();
            getCurrentPlan().actions.add(insertPosition, act);
            setSelectedAction(act);
        }
    }

    @FXML
    private void removeAction() {
        if(selectedAction != null) {
            int x = 0;
            for(GameAction act : getCurrentPlan().actions.toArray(new GameAction[0])) {
                if(selectedAction == act) {
                    getCurrentPlan().actions.remove(act);
                    setSelectedAction(null);
                    break;
                }
                x++;
            }
            if(x >= getCurrentPlan().actions.size()) {
                if(x > 1) {
                    setSelectedAction(getCurrentPlan().actions.get(x - 1));
                }
            }
            else if(x > 0) {
                setSelectedAction(getCurrentPlan().actions.get(x));
            }
        }
    }

    @FXML
    private void showHideAction() {
        if(selectedAction != null) {
            GameActionView view = planDisplayElements.get(selectedAction);
            if(view != null) {
                view.setHidden(!view.isHidden());
            }
        }
    }

    @FXML
    private void moveUpAction() {
        if(selectedAction != null) {
            for(int x = 2; x < getCurrentPlan().actions.size(); x++) {
                GameAction act = getCurrentPlan().actions.get(x);
                if(act == selectedAction) {
                    getCurrentPlan().actions.remove(act);
                    getCurrentPlan().actions.add(x - 1, act);
                    return;
                }
            }
        }
    }

    @FXML
    private void moveDownAction() {
        if(selectedAction != null) {
            for(int x = 1; x < getCurrentPlan().actions.size() - 1; x++) {
                GameAction act = getCurrentPlan().actions.get(x);
                if(act == selectedAction) {
                    getCurrentPlan().actions.remove(act);
                    getCurrentPlan().actions.add(x + 1, act);
                    return;
                }
            }
        }
    }

    @FXML
    private void editAction() {
        if(selectedAction != null) {
            GameActionView view = planDisplayElements.get(selectedAction);
            if(view != null) {
                view.edit();
            }
        }
    }

    @FXML
    private void saveGamePlan() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save strategy");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("GamePlan Strategy files (*.gps)", "*.gps"));
        fileChooser.setInitialFileName("*.gps");
        File dest = fileChooser.showSaveDialog(viewingPane.getScene().getWindow());
        if(dest != null) {
            try {
                FileWriter writer = new FileWriter(dest); 
                writer.write(getCurrentPlan().serialize());
                writer.flush();
                writer.close();
            } catch(Exception e) {}
        }
    }

    @FXML
    private void loadGamePlan() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load strategy");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("GamePlan Strategy files (*.gps)", "*.gps"));
        fileChooser.setInitialFileName("*.gps");
        File dest = fileChooser.showOpenDialog(viewingPane.getScene().getWindow());
        if(dest != null) {
            try {
                setCurrentPlan(GamePlan.deserialize(Files.readString(Paths.get(dest.getPath()))));
            } catch(Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("An unknown error occured while attempting to load the GamePlan strategy.");
                alert.getDialogPane().expandableContentProperty().set(new TextArea(e.getMessage()));
                e.printStackTrace();
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            }
        }
    }
}