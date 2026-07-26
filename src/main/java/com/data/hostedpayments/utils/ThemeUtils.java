package com.data.hostedpayments.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.CompoundButtonCompat;

import com.data.hostedpayments.Theme.ButtonStyle;
import com.data.hostedpayments.Theme.PaymentTheme;

public class ThemeUtils {

    private ThemeUtils() {
    }

    // ---------------- BUTTON ----------------

    public static void applyButton(Button button, PaymentTheme theme) {

        if (theme == null || theme.getPrimaryButtonStyle() == null) {
            return;
        }

        ButtonStyle style = theme.getPrimaryButtonStyle();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(style.getBackgroundColor());
        drawable.setCornerRadius(style.getCornerRadius());
        drawable.setStroke(
                (int) style.getBorderWidth(),
                style.getBorderColor());

        button.setBackground(drawable);

        button.setTextColor(style.getTextColor());



        button.setText(theme.getPayButtonText());

        if (theme.getBoldFont() != null) {
            button.setTypeface(theme.getBoldFont());
        }
    }

    // ---------------- TEXT ----------------

    public static void applyText(TextView textView,
                                 PaymentTheme theme) {

        if (theme == null) return;

        textView.setTextColor(theme.getTextColor());

        if (theme.getRegularFont() != null) {
            textView.setTypeface(theme.getRegularFont());
        }
    }

    // ---------------- EDITTEXT ----------------

    public static void applyEditText(EditText editText,
                                     PaymentTheme theme) {

        if (theme == null) return;

        editText.setTextColor(theme.getTextColor());

        editText.setHintTextColor(theme.getHintColor());

        if (theme.getRegularFont() != null) {
            editText.setTypeface(theme.getRegularFont());
        }

        GradientDrawable drawable = new GradientDrawable();

        drawable.setCornerRadius(theme.getTextFieldCornerRadius());

        drawable.setStroke(
                2,
                theme.getTextFieldBorderColor());

        drawable.setColor(theme.getBackgroundColor());


        editText.setBackground(drawable);
    }

    // ---------------- RADIO BUTTON ----------------

    public static void applyRadioButton(RadioButton radioButton,
                                        PaymentTheme theme) {

        if (theme == null) return;

        CompoundButtonCompat.setButtonTintList(
                radioButton,
                ColorStateList.valueOf(
                        theme.getPrimaryColor()));

        radioButton.setTextColor(theme.getTextColor());

        if (theme.getRegularFont() != null) {
            radioButton.setTypeface(theme.getRegularFont());
        }
    }

    // ---------------- TOOLBAR ----------------

    public static void applyToolbar(Toolbar toolbar,
                                    PaymentTheme theme) {

        if (theme == null) return;

        toolbar.setBackgroundColor(theme.getToolbarColor());

        toolbar.setTitleTextColor(theme.getTextColor());
    }

    // ---------------- CARD ----------------

    public static void applyCard(CardView cardView,
                                 PaymentTheme theme) {

        if (theme == null) return;

        cardView.setCardBackgroundColor(
                theme.getBackgroundColor());

        cardView.setRadius(
                theme.getCardCornerRadius());

        cardView.setCardElevation(
                theme.getCardElevation());
    }

    // ---------------- LOGO ----------------

    public static void applyMerchantLogo(ImageView imageView,
                                         PaymentTheme theme) {

        if (theme == null) return;

        if (theme.getMerchantLogo() != null) {

            imageView.setImageDrawable(
                    theme.getMerchantLogo());

        }
    }

    // ---------------- MERCHANT NAME ----------------

    public static void applyMerchantName(TextView textView,
                                         PaymentTheme theme) {

        if (theme == null) return;

        textView.setText(
                theme.getMerchantName());

        textView.setTextColor(
                theme.getTextColor());

        if (theme.getBoldFont() != null) {
            textView.setTypeface(theme.getBoldFont());
        }
    }
}