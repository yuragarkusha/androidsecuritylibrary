package com.dummylabs.androidsecuritylibrary;

/**
 * Created by Ge0rge on 27.05.2016.
 */
public class ProtectedInt {
    private int value;
    private double keyNumber;
    private double fractionPart;

    public int getVariable() {
        return value/(-8);
    }

    public void setVariable(int value) {
        this.value = value*(-8);
    }

    public double partsOfValue(float value)
    {
        double src = 123.45;
        int wholePart = (int)src; //целая часть
        double fractionPart = src - wholePart; //дробная часть
        return fractionPart;
    }
}
