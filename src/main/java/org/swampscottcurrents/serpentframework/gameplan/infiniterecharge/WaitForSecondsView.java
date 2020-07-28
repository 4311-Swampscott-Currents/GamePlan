package org.swampscottcurrents.serpentframework.gameplan.infiniterecharge;

import org.swampscottcurrents.serpentframework.gameplan.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class WaitForSecondsView extends StationaryActionView {
    public WaitForSecondsView(GamePlanWidget w, WaitForSeconds data) {
        super(w, data, new String(Character.toChars(0x1f552)), "#fc3ddf");
    }

    @Override
    public void edit() {
        Double time = askForTimeInSeconds(((WaitForSeconds)viewData).seconds);
        if(time != null) {
            ((WaitForSeconds)viewData).seconds = time;
        }
    }

    public static Double askForTimeInSeconds(double original) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setHeaderText("At what speed should the robot drive (in feet per second)?");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        NumericTextField number = new NumericTextField(original);
        alert.getDialogPane().expandableContentProperty().set(number);
        alert.getDialogPane().setExpanded(true);
        ButtonType button = alert.showAndWait().get();
        if(button == ButtonType.OK) {
            try {
                return number.getNumber();
            }
            catch(Exception e) {
                new Alert(AlertType.ERROR, "Input number was not in a valid format.");
            }
        }
        return null;
    }
}