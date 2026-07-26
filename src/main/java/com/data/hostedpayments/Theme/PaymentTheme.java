package com.data.hostedpayments.Theme;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class PaymentTheme {

    public Drawable getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(Drawable merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }

    public int getSuccessColor() {
        return successColor;
    }

    public void setSuccessColor(int successColor) {
        this.successColor = successColor;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public int getWarningColor() {
        return warningColor;
    }

    public void setWarningColor(int warningColor) {
        this.warningColor = warningColor;
    }

    public Typeface getRegularFont() {
        return regularFont;
    }

    public void setRegularFont(Typeface regularFont) {
        this.regularFont = regularFont;
    }

    public Typeface getMediumFont() {
        return mediumFont;
    }

    public void setMediumFont(Typeface mediumFont) {
        this.mediumFont = mediumFont;
    }

    public Typeface getBoldFont() {
        return boldFont;
    }

    public void setBoldFont(Typeface boldFont) {
        this.boldFont = boldFont;
    }

    public ButtonStyle getPrimaryButtonStyle() {
        return primaryButtonStyle;
    }

    public void setPrimaryButtonStyle(ButtonStyle primaryButtonStyle) {
        this.primaryButtonStyle = primaryButtonStyle;
    }

    public ButtonStyle getSecondaryButtonStyle() {
        return secondaryButtonStyle;
    }

    public void setSecondaryButtonStyle(ButtonStyle secondaryButtonStyle) {
        this.secondaryButtonStyle = secondaryButtonStyle;
    }

    public float getTextFieldCornerRadius() {
        return textFieldCornerRadius;
    }

    public void setTextFieldCornerRadius(float textFieldCornerRadius) {
        this.textFieldCornerRadius = textFieldCornerRadius;
    }

    public int getTextFieldBorderColor() {
        return textFieldBorderColor;
    }

    public void setTextFieldBorderColor(int textFieldBorderColor) {
        this.textFieldBorderColor = textFieldBorderColor;
    }

    public float getCardCornerRadius() {
        return cardCornerRadius;
    }

    public void setCardCornerRadius(float cardCornerRadius) {
        this.cardCornerRadius = cardCornerRadius;
    }

    public float getCardElevation() {
        return cardElevation;
    }

    public void setCardElevation(float cardElevation) {
        this.cardElevation = cardElevation;
    }

    public ThemeMode getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(ThemeMode themeMode) {
        this.themeMode = themeMode;
    }

    public String getPayButtonText() {
        return payButtonText;
    }

    public void setPayButtonText(String payButtonText) {
        this.payButtonText = payButtonText;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    // Merchant Branding
    private Drawable merchantLogo;
    private String merchantName;

    // Colors
    private int primaryColor;
    private int secondaryColor;
    private int backgroundColor;
    private int toolbarColor;

    private int textColor;
    private int hintColor;

    private int successColor;
    private int errorColor;
    private int warningColor;

    // Fonts
    private Typeface regularFont;
    private Typeface mediumFont;
    private Typeface boldFont;

    // Buttons
    private ButtonStyle primaryButtonStyle;
    private ButtonStyle secondaryButtonStyle;

    // TextFields
    private float textFieldCornerRadius = 12;
    private int textFieldBorderColor;

    // Cards
    private float cardCornerRadius = 16;
    private float cardElevation = 4;

    // Theme
    private ThemeMode themeMode = ThemeMode.AUTO;

    // Labels
    private String payButtonText = "Pay";
    private String cancelButtonText = "Cancel";

    // getters and setters
}