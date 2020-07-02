package com.majazeh.risloo.Utils;

import android.widget.EditText;

public class InputChecker {

    public void oneInput(EditText firstEditText, boolean firstTouch, boolean firstError, int defaultResId, int errorResID) {
        firstEditText.setCursorVisible(false);

        firstTouch = false;

        if (firstEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            firstError = true;
        } else {
            firstEditText.setBackgroundResource(defaultResId);
            firstError = false;
        }
    }

    public void twoInput(EditText firstEditText, EditText secondEditText, boolean firstTouch, boolean secondTouch, boolean firstError, boolean secondError, int defaultResId, int errorResID) {
        firstEditText.setCursorVisible(false);
        secondEditText.setCursorVisible(false);

        firstTouch = false;
        secondTouch = false;

        if (firstEditText.length() == 0 && secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = true;
        } else if (firstEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = false;
        } else if (secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = true;
        } else {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = false;
        }
    }

    public void threeInput(EditText firstEditText, EditText secondEditText, EditText thirdEditText, boolean firstTouch, boolean secondTouch, boolean thirdTouch, boolean firstError, boolean secondError, boolean thirdError, int defaultResId, int errorResID) {
        firstEditText.setCursorVisible(false);
        secondEditText.setCursorVisible(false);
        thirdEditText.setCursorVisible(false);

        firstTouch = false;
        secondTouch = false;
        thirdTouch = false;

        if (firstEditText.length() == 0 && secondEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = true;
            thirdError = true;
        } else if (secondEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = true;
            thirdError = true;
        } else if (firstEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = false;
            thirdError = true;
        } else if (firstEditText.length() == 0 && secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = true;
            thirdError = false;
        } else if (thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = false;
            thirdError = true;
        } else if (secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = true;
            thirdError = false;
        } else if (firstEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = false;
            thirdError = false;
        } else {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = false;
            thirdError = false;
        }
    }

    public void fourInput(EditText firstEditText, EditText secondEditText, EditText thirdEditText, EditText fourthEditText, boolean firstTouch, boolean secondTouch, boolean thirdTouch, boolean fourthTouch, boolean firstError, boolean secondError, boolean thirdError, boolean fourthError, int defaultResId, int errorResID) {
        firstEditText.setCursorVisible(false);
        secondEditText.setCursorVisible(false);
        thirdEditText.setCursorVisible(false);
        fourthEditText.setCursorVisible(false);

        firstTouch = false;
        secondTouch = false;
        thirdTouch = false;
        fourthTouch = false;

        if (firstEditText.length() == 0 && secondEditText.length() == 0 && thirdEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = true;
            thirdError = true;
            fourthError = true;
        } else if (secondEditText.length() == 0 && thirdEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = true;
            thirdError = true;
            fourthError = true;
        } else if (firstEditText.length() == 0 && thirdEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = false;
            thirdError = true;
            fourthError = true;
        } else if (firstEditText.length() == 0 && secondEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = true;
            thirdError = false;
            fourthError = true;
        } else if (secondEditText.length() == 0 && secondEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = true;
            thirdError = true;
            fourthError = false;
        } else if (thirdEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = false;
            thirdError = true;
            fourthError = true;
        } else if (secondEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = true;
            thirdError = false;
            fourthError = true;
        } else if (secondEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = true;
            thirdError = true;
            fourthError = false;
        } else if (firstEditText.length() == 0 && fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = true;
            secondError = false;
            thirdError = false;
            fourthError = true;
        } else if (firstEditText.length() == 0 && thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = false;
            thirdError = true;
            fourthError = false;
        } else if (firstEditText.length() == 0 && secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = true;
            thirdError = false;
            fourthError = false;
        } else if (fourthEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(errorResID);
            firstError = false;
            secondError = false;
            thirdError = false;
            fourthError = true;
        } else if (thirdEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(errorResID);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = false;
            thirdError = true;
            fourthError = false;
        } else if (secondEditText.length() == 0) {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(errorResID);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = true;
            thirdError = false;
            fourthError = false;
        } else if (firstEditText.length() == 0) {
            firstEditText.setBackgroundResource(errorResID);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = true;
            secondError = false;
            thirdError = false;
            fourthError = false;
        } else {
            firstEditText.setBackgroundResource(defaultResId);
            secondEditText.setBackgroundResource(defaultResId);
            thirdEditText.setBackgroundResource(defaultResId);
            fourthEditText.setBackgroundResource(defaultResId);
            firstError = false;
            secondError = false;
            thirdError = false;
            fourthError = false;
        }
    }

}