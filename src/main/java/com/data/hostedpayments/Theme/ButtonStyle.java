package com.data.hostedpayments.Theme;

public class ButtonStyle {

    private int backgroundColor;
    private int textColor;
    private int borderColor;

    private float cornerRadius = 12;
    private float borderWidth = 1;
    private float fontsize ;
    private float height ;


    //private float elevation = 4;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

//    public float getElevation() {
//        return elevation;
//    }
//
//    public void setElevation(float elevation) {
//        this.elevation = elevation;
//    }
}