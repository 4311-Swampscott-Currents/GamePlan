package org.swampscottcurrents.serpentframework.gameplan;

import javafx.scene.control.*;

public class NumericTextField extends TextField
    {
        public NumericTextField(double number) {
            setText(Double.valueOf(number).toString());
        }

        @Override
        public void replaceText(int start, int end, String text)
        {
            if (validate(text))
            {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text)
        {
            if (validate(text))
            {
                super.replaceSelection(text);
            }
        }

        public double getNumber() {
            if(getText().equals("")) {
                return 0;
            }
            else {
                return Double.parseDouble(getText());
            }
        }

        private boolean validate(String text)
        {
            return text.matches("[0-9.]*");
        }
    }