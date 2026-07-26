package com.data.hostedpayments.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.hostedpayments.Adapter.UpiAppAdapter;
import com.data.hostedpayments.PaymentSDK;
import com.data.hostedpayments.R;
import com.data.hostedpayments.ResponseConfig;
import com.data.hostedpayments.Theme.ButtonStyle;
import com.data.hostedpayments.Theme.PaymentTheme;
import com.data.hostedpayments.model.CardBrand;
import com.data.hostedpayments.model.Request.BrowserDetails;
import com.data.hostedpayments.model.Request.Card;
import com.data.hostedpayments.model.Request.Customer;
import com.data.hostedpayments.model.Request.PaymentDetailsRequest;
import com.data.hostedpayments.model.Request.PaymentInstrument;
import com.data.hostedpayments.model.Response.PaymentDetailsResponse;
import com.data.hostedpayments.model.UpiApp;
import com.data.hostedpayments.utils.ConstantsVar;
import com.data.hostedpayments.utils.DecryptionUtil;
import com.data.hostedpayments.utils.IntentConstants;
import com.data.hostedpayments.utils.NetworkUtils;
import com.data.hostedpayments.utils.ThemeUtils;
import com.data.hostedpayments.viewmodel.PaymentViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

public class PaymentOptionsActivity extends AppCompatActivity {

    private static final int CHECKOUT_REQUEST_CODE = 2;
    private static final int THREEDS_REQUEST_CODE = 3;
    private ActivityResultLauncher<Intent> paymentResultLauncher;
    private String weburl;
    private String payId;
    private String respData;

    private String amount;

    private RadioGroup paymentOptionGroup;
    private LinearLayout cardDetailsLayout,cvvCardLayout;
    private LinearLayout upiDetailsLayout;
    private LinearLayout otherPaymentDetailsLayout;
    private LinearLayout cardBrandStrip;
    private EditText cardNumber;
    private EditText cardHolderName;
    private EditText cardExpiry;
    private EditText cardCvv;
    private EditText upiId;
    private ImageView cardBrandImage;
    private Button btnContinue, btnCancel;
    private ProgressDialog progressDialog;
    private PaymentViewModel viewModel;
    private boolean  isFormatting;
    private final List<Integer> dynamicPaymentOptionIds = new ArrayList<>();
    private RecyclerView rvUpiApps;
    private UpiAppAdapter upiAdapter;
    private TextView tvNoUpiApps,txttransid,txttransamt,otherPaymentText;
    PaymentDetailsRequest request;
    private ScrollView paymentLayout;
    private EditText etTokenCVV;
    private LinearLayout threeDSLayout;
    BrowserDetails browser;
    private LinearLayout loadingLayout;
    private CheckBox cbSaveCard;

    private WebView webView;
    RadioButton radiobtnCard,radioupi;
    String cardTokenFlag,cardToken,transactionType;
    private ArrayList<String> supportedPaymentModes;
    private String detectedPaymentMethod = "";
    private String lastBin = "";
    private String cardoperation,curr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);


        init();
        PaymentTheme theme = PaymentSDK.getConfiguration().getTheme();
        payId = getIntent().getStringExtra(IntentConstants.EXTRA_PAY_ID);
        amount = getIntent().getStringExtra(IntentConstants.EXTRA_AMOUNT);
        transactionType = getIntent().getStringExtra(IntentConstants.EXTRA_TRANSACTION_TYPE);
        cardToken = getIntent().getStringExtra(IntentConstants.EXTRA_CARD_TOKEN);
        weburl = getIntent().getStringExtra(IntentConstants.EXTRA_WEB_URL);
        respData = getIntent().getStringExtra(IntentConstants.EXTRA_RESP_DATA);
        cardoperation = getIntent().getStringExtra(IntentConstants.EXTRA_TOKENIZATION_OPERATION);
        curr = getIntent().getStringExtra(IntentConstants.EXTRA_CURRENCY);


        paymentResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleFinalPaymentResponse(
                        result.getResultCode(),
                        result.getData()
                )
        );
        supportedPaymentModes =
                getIntent().getStringArrayListExtra(
                        IntentConstants.EXTRA_SUPPORTED_PAYMENT_MODES);

        Log.d("PAYMENT_MODES", supportedPaymentModes.toString());
        //loadSupportedPaymentMethods();
        bindAmount();
        setupCardNumberFormatting();
        setupExpiryFormatting();

        paymentOptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showSelectedPaymentDetails(checkedId);
            }
        });
        initializeTokenizedCardUI();
        // applySupportedPaymentModes();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String test3dsUrl = "https://www.mastercard.us/en-us/business/overview/safety-and-security/identity-check.html";
//                openCheckout(test3dsUrl, "");

                if (isTokenizedPurchase()) {
                    submitTokenizedCardPayment();
                } else {
                    if (!isInputValid()) {
                        return;
                    }
                    cardTokenFlag = cbSaveCard.isChecked() ? "Y" : "N";
                    submitPaymentDetails();
                }
            }
        });



        ButtonStyle style = theme.getSecondaryButtonStyle();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.TRANSPARENT);
        drawable.setStroke(2, style.getBorderColor());
        drawable.setCornerRadius(style.getCornerRadius());

        btnCancel.setBackground(drawable);
        btnCancel.setTextColor(style.getTextColor());


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponseConfig.startTrxn = false;
                finish();
            }
        });
        theme = PaymentSDK.getConfiguration().getTheme();

        if (theme != null) {

//            paymentRoot.setBackgroundColor(theme.getBackgroundColor());

//            ThemeUtils.applyToolbar(toolbar, theme);

//            ThemeUtils.applyMerchantLogo(imgMerchantLogo, theme);

//            ThemeUtils.applyMerchantName(tvMerchantName, theme);

            //ThemeUtils.applyText(totalAmountValue, theme);

            //ThemeUtils.applyButton(btnContinue, theme);

            ThemeUtils.applyRadioButton(radiobtnCard, theme);
            ThemeUtils.applyRadioButton(radioupi, theme);

//            ThemeUtils.applyEditText(cardNumber, theme);
//            ThemeUtils.applyEditText(cardHolderName, theme);
//            ThemeUtils.applyEditText(cardExpiry, theme);
//            ThemeUtils.applyEditText(cardCvv, theme);

//            ThemeUtils.applyCard(cardContainer, theme);
//            ThemeUtils.applyCard(upiContainer, theme);
        }

    }
    private  void init(){
        etTokenCVV = findViewById(R.id.etTokenCVV);
        paymentOptionGroup = findViewById(R.id.paymentOptionGroup);
        cardDetailsLayout = findViewById(R.id.cardDetailsLayout);
        cvvCardLayout = findViewById(R.id.cvvCardLayout);
        upiDetailsLayout = findViewById(R.id.upiDetailsLayout);
        radiobtnCard = findViewById(R.id.radioCardDetails);
        radioupi = findViewById(R.id.radioUpi);
        otherPaymentDetailsLayout = findViewById(R.id.otherPaymentDetailsLayout);
        cardBrandStrip = findViewById(R.id.cardBrandStrip);
        cardNumber = findViewById(R.id.cardNumber);
        cardHolderName = findViewById(R.id.cardHolderName);
        cardExpiry = findViewById(R.id.cardExpiry);
        cardCvv = findViewById(R.id.cardCvv);
        rvUpiApps = findViewById(R.id.rvUpiApps);
        tvNoUpiApps = findViewById(R.id.tvNoUpiApps);
        txttransid = findViewById(R.id.tvTransactionId);
        txttransamt = findViewById(R.id.trxnAmount);
        rvUpiApps.setLayoutManager(
                new LinearLayoutManager(this));
        cardBrandImage = findViewById(R.id.cardBrandImage);
        otherPaymentText = findViewById(R.id.otherPaymentText);
        //totalAmountValue = findViewById(R.id.totalAmountValue);
        btnContinue = findViewById(R.id.btnContinuePayment);
        btnCancel = findViewById(R.id.btnCancelPayment);
        paymentLayout = findViewById(R.id.paymentLayout);

        threeDSLayout = findViewById(R.id.threeDSLayout);

        loadingLayout = findViewById(R.id.loadingLayout);
        cbSaveCard = findViewById(R.id.cbSaveCard);
        webView = findViewById(R.id.webView3ds);
    }
    private void submitTokenizedCardPayment() {

        showProgress();

        request = new PaymentDetailsRequest();

        request.setReferenceId(payId);

        request.setCustomerIp(NetworkUtils.getIpAdd());

        Card card = new Card();

//        card.setCardToken(cardToken);

        card.setCvv(etTokenCVV.getText().toString());

        request.setCard(card);

//        callPaymentAPI(request);

            browser = new BrowserDetails();
            browser.setTransactionUuid(UUID.randomUUID().toString());
            browser.setBrowserLanguage(Locale.getDefault().toLanguageTag());
            browser.setBrowserScreenWidth("1080");
            browser.setBrowserScreenHeight("2400");
            browser.setBrowserColorDepth("24");
            browser.setBrowserJavascriptEnabled(true);
            browser.setBrowserJavaEnabled(false);
            browser.setBrowserAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            browser.setBrowserTZ(String.valueOf(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 60000));
            browser.setBrowserUserAgent(System.getProperty("http.agent"));
            request.setBrowserDetails(browser);

            viewModel.submitPayment(request,isTokenizedPurchase())
                    .observe(this, json -> {

                        dismissProgress();

                        if (json == null) {

                            PaymentSDK.getInstance().notifyResult(
                                    "{\"result\":\"FAILURE\",\"message\":\"No Response\"}");

                            finish();
                            return;
                        }

                        try {

                            JSONObject root = new JSONObject(json);

                            String status = root.optString("status");

                            if ("CHALLENGE".equalsIgnoreCase(status)) {

                                JSONObject challenge =
                                        root.optJSONObject("3dsChanllengeResponse");

                                if (challenge != null) {

                                    String acsUrl = challenge.optString("acsUrl");
                                    String redirectHtml = challenge.optString("redirectHtml");

                                    Log.d("3DS", "ACS URL = " + acsUrl);
                                    Log.d("3DS", "Redirect HTML = " + redirectHtml);

                                    openThreeDS(acsUrl, redirectHtml);
                                    return;
                                }
                            } else {
                                // Direct Success / Failure
                                Log.e("FINAL_RESPONSE", json);
                                PaymentSDK.getInstance().notifyResult(json);
                                finish();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            PaymentSDK.getInstance().notifyResult(json);

                            finish();
                        }

                    });

    }
    private void initializeTokenizedCardUI() {

        boolean isPurchase =
                "1".equals(transactionType)
                        || "4".equals(transactionType);

        boolean hasCardToken =
                !TextUtils.isEmpty(cardToken);

        if (isPurchase && hasCardToken) {

            // Hide payment methods
            paymentOptionGroup.setVisibility(View.GONE);

            // Hide full card entry
            cardDetailsLayout.setVisibility(View.GONE);

            // Hide UPI
            upiDetailsLayout.setVisibility(View.GONE);

            // Hide other methods
            otherPaymentDetailsLayout.setVisibility(View.GONE);

            // Hide card brand strip
            cardBrandStrip.setVisibility(View.GONE);

            // Show only CVV
            cvvCardLayout.setVisibility(View.VISIBLE);

            // Card already saved
            cbSaveCard.setVisibility(View.GONE);

        } else {

            paymentOptionGroup.setVisibility(View.VISIBLE);
            applySupportedPaymentModes();
        }
    }
    private void showSelectedPaymentDetails(int checkedId) {
        if (isTokenizedPurchase()) {

            cardDetailsLayout.setVisibility(View.GONE);

            upiDetailsLayout.setVisibility(View.GONE);

            otherPaymentDetailsLayout.setVisibility(View.GONE);

            cardBrandStrip.setVisibility(View.GONE);

            cvvCardLayout.setVisibility(View.VISIBLE);

            return;
        }
        cardDetailsLayout.setVisibility(View.GONE);
        upiDetailsLayout.setVisibility(View.GONE);
        otherPaymentDetailsLayout.setVisibility(View.GONE);

        boolean isCardSelected = checkedId == R.id.radioCardDetails;
        cardBrandStrip.setVisibility(isCardSelected ? View.VISIBLE : View.GONE);

        if (isCardSelected) {
            movePaymentDetailsBelow(cardDetailsLayout, R.id.cardBrandStrip);
            cardDetailsLayout.setVisibility(View.VISIBLE);
            return;
        }

        if (checkedId == R.id.radioUpi) {

            movePaymentDetailsBelow(upiDetailsLayout,
                    R.id.radioUpi);

            upiDetailsLayout.setVisibility(View.VISIBLE);

            List<UpiApp> apps =
                    getInstalledUpiApps(this);

            if (apps.isEmpty()) {
                rvUpiApps.setVisibility(View.GONE);
                tvNoUpiApps.setVisibility(View.VISIBLE);
            } else {
                rvUpiApps.setVisibility(View.VISIBLE);
                tvNoUpiApps.setVisibility(View.GONE);
                upiAdapter = new UpiAppAdapter(apps);
                rvUpiApps.setAdapter(upiAdapter);
            }
        }
    }

    private void movePaymentDetailsBelow(View detailsView, int anchorViewId) {
        View anchorView = findViewById(anchorViewId);
        if (anchorView == null) {
            return;
        }

        ViewParent currentParent = detailsView.getParent();
        if (currentParent instanceof ViewGroup) {
            ((ViewGroup) currentParent).removeView(detailsView);
        }

        int anchorIndex = paymentOptionGroup.indexOfChild(anchorView);
        if (anchorIndex < 0) {
            return;
        }

        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, dpToPx(8), 0, dpToPx(8));
        paymentOptionGroup.addView(detailsView, anchorIndex + 1, layoutParams);
    }
    private void bindAmount() {
        String displayAmount = amount == null || amount.trim().isEmpty()
                ? curr+ " 0.00"
                : curr + amount;

        if ("A".equalsIgnoreCase(cardoperation) && "12".equals(transactionType)) {
            btnContinue.setText("Add Card");
        } else {
            btnContinue.setText("Pay Now  " + displayAmount);
        }
        //totalAmountValue.setText(displayAmount);
        txttransid.setText(payId);
        txttransamt.setText(displayAmount);
    }

    private boolean isInputValid() {
        int checkedId = paymentOptionGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.radioCardDetails) {
            if (isBlank(cardNumber)
                    || isBlank(cardHolderName)
                    || isBlank(cardExpiry)
                    || isBlank(cardCvv)) {

                Toast.makeText(this,
                        "Please enter card details",
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            String expiry = cardExpiry.getText().toString().trim();

            if (!expiry.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {

                Toast.makeText(this,
                        "Please enter a valid expiry date",
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            return true;
        }

        if (checkedId == R.id.radioUpi) {
            if (upiAdapter == null || upiAdapter.getSelectedApp() == null) {
                Toast.makeText(this, "Please select a UPI app", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return true;
    }

    private void applySupportedPaymentModes() {

        // Saved Card Purchase Flow
        if (("1".equals(transactionType) || "4".equals(transactionType))
                && !TextUtils.isEmpty(cardToken)) {

            paymentOptionGroup.setVisibility(View.GONE);

            cardBrandStrip.setVisibility(View.GONE);

            cardDetailsLayout.setVisibility(View.GONE);

            upiDetailsLayout.setVisibility(View.GONE);

            otherPaymentDetailsLayout.setVisibility(View.GONE);

            cbSaveCard.setVisibility(View.GONE);

            cvvCardLayout.setVisibility(View.VISIBLE);

            return;
        }

        // Existing Flow
        resetPaymentModes();
        paymentOptionGroup.setVisibility(View.VISIBLE);

        if (supportedPaymentModes == null || supportedPaymentModes.isEmpty()) {
            paymentOptionGroup.setVisibility(View.GONE);
            Toast.makeText(this, "No supported payment methods available", Toast.LENGTH_LONG).show();
            return;
        }

        boolean anyModeShown = false;
        JSONArray modes = new JSONArray(supportedPaymentModes);
        for (int i = 0; i < modes.length(); i++) {
            String mode = getPaymentModeValue(modes.opt(i));
            if (!mode.isEmpty() && showPaymentMode(mode)) {
                anyModeShown = true;
            }
        }

        if (!anyModeShown) {
            paymentOptionGroup.setVisibility(View.GONE);
            cardDetailsLayout.setVisibility(View.GONE);
            upiDetailsLayout.setVisibility(View.GONE);
            otherPaymentDetailsLayout.setVisibility(View.GONE);
            Toast.makeText(this, "No supported payment methods available", Toast.LENGTH_LONG).show();
            return;
        }

        selectFirstVisibleMode();
    }
    private boolean isTokenizedPurchase() {
        return ("1".equals(transactionType)
                || "4".equals(transactionType))
                && !TextUtils.isEmpty(cardToken);
    }
    private void resetPaymentModes() {
        paymentOptionGroup.clearCheck();
        setModeVisibility(R.id.radioCardDetails, false);
        setModeVisibility(R.id.radioUpi, false);
        setModeVisibility(R.id.radioNetbanking, false);
        setModeVisibility(R.id.radiosamsungpay, false);
        cardDetailsLayout.setVisibility(View.GONE);
        cardBrandStrip.setVisibility(View.GONE);
        upiDetailsLayout.setVisibility(View.GONE);
        otherPaymentDetailsLayout.setVisibility(View.GONE);
        for (int radioButtonId : dynamicPaymentOptionIds) {
            View view = findViewById(radioButtonId);
            if (view != null) {
                paymentOptionGroup.removeView(view);
            }
        }
        dynamicPaymentOptionIds.clear();
    }

    private boolean showPaymentMode(String mode) {

        String normalizedMode = normalizePaymentMode(mode);

        switch (normalizedMode) {

            case "CCI":
            case "DCI":
                setModeVisibility(R.id.radioCardDetails, true);
                return true;

            case "UPI":
                setModeVisibility(R.id.radioUpi, true);
                return true;

            case "APPLEPAY":
            case "STCPAY":
            case "TABBY":
                addDynamicPaymentMode(mode, normalizedMode);
                return true;

            default:
                return false;
        }
    }

    private String getPaymentModeValue(Object modeObject) {
        if (modeObject == null || JSONObject.NULL.equals(modeObject)) {
            return "";
        }

        if (modeObject instanceof JSONObject) {
            JSONObject modeJson = (JSONObject) modeObject;
            String[] modeKeys = {
                    "paymentMethod",
                    "paymentMode",
                    "paymentType",
                    "method",
                    "code",
                    "name"
            };
            for (String key : modeKeys) {
                String value = modeJson.optString(key, "").trim();
                if (!value.isEmpty()) {
                    return value;
                }
            }
            return "";
        }

        return String.valueOf(modeObject).trim();
    }
    private void addDynamicPaymentMode(String displayMode, String normalizedMode) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setId(View.generateViewId());
        radioButton.setTag(normalizedMode);
        radioButton.setText(formatPaymentModeLabel(displayMode));
        radioButton.setTextColor(0xFF202124);
        radioButton.setTextSize(13);
        radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                dpToPx(40)
        ));
        paymentOptionGroup.addView(radioButton);
        dynamicPaymentOptionIds.add(radioButton.getId());
    }

    private String normalizePaymentMode(String mode) {
        return mode == null ? "" : mode.trim().replace(" ", "").replace("_", "").toUpperCase();
    }

    private String formatPaymentModeLabel(String mode) {
        String normalizedMode = normalizePaymentMode(mode);
        if ("STCPAY".equals(normalizedMode)) {
            return "STC Pay";
        }
        return mode == null ? "" : mode.trim();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setModeVisibility(int radioButtonId, boolean visible) {
        RadioButton radioButton = findViewById(radioButtonId);
        if (radioButton == null) {
            return;
        }
        radioButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (radioButtonId == R.id.radioCardDetails) {
            cardBrandStrip.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void selectFirstVisibleMode() {
        int[] radioButtonIds = {
                R.id.radioCardDetails,
                R.id.radioUpi,
                R.id.radioNetbanking,
                R.id.radiosamsungpay
        };

        for (int radioButtonId : radioButtonIds) {
            RadioButton radioButton = findViewById(radioButtonId);
            if (radioButton.getVisibility() == View.VISIBLE) {
                paymentOptionGroup.check(radioButtonId);
                return;
            }
        }
        for (int radioButtonId : dynamicPaymentOptionIds) {
            RadioButton radioButton = findViewById(radioButtonId);
            if (radioButton.getVisibility() == View.VISIBLE) {
                paymentOptionGroup.check(radioButtonId);
                return;
            }
        }
    }
    private void setupCardNumberFormatting() {
        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) return;
                isFormatting = true;
                String digits  = s.toString().replace(" ", "");

                if (digits.length() > 16) digits = digits.substring(0, 16);
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    if (i > 0 && i % 4 == 0) formatted.append(' ');
                    formatted.append(digits.charAt(i));
                }
                cardNumber.setText(formatted);
                cardNumber.setSelection(formatted.length());
                updateCardBrandIcon(digits);
                if (digits.length() >= 10) {

                    String cardBin = digits.substring(0,10);

                    if (!cardBin.equals(lastBin)) {

                        lastBin = cardBin;

                        fetchCardType(cardBin);
                    }
                }
                isFormatting = false;
            }
        });
    }
    private void updateCardBrandIcon(String digits) {

        if (digits.equals("0")) {
            cardBrandImage.setVisibility(View.GONE);
            cardBrandImage.setImageDrawable(null);
            return;
        }

        cardBrandImage.setVisibility(View.VISIBLE);
        if (digits.startsWith("4")) {
            cardBrandImage.setVisibility(View.VISIBLE);
            cardBrandImage.setImageResource(R.drawable.ic_visa);
        } else if (digits.startsWith("5") || digits.startsWith("2")) {
            cardBrandImage.setImageResource(R.drawable.ic_mastercard);
        } else if (digits.startsWith("6")) {
            cardBrandImage.setImageResource(R.drawable.ic_rupay);
        } else if (digits.startsWith("3")) {
            cardBrandImage.setImageResource(R.drawable.ic_amex);
        } else {
            cardBrandImage.setImageResource(0);
            cardBrandImage.setVisibility(View.GONE);
            cardBrandImage.setImageDrawable(null);
        }
    }
    // ── Expiry — auto-inserts '/' after MM ───────────────────────────────────

    private void setupExpiryFormatting() {
        cardExpiry.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) return;
                isFormatting = true;
                String digits = s.toString().replace("/", "");
                if (digits.length() > 4) digits = digits.substring(0, 4);
                String formatted = digits.length() >= 3
                        ? digits.substring(0, 2) + "/" + digits.substring(2)
                        : digits;
                cardExpiry.setText(formatted);
                cardExpiry.setSelection(formatted.length());
                isFormatting = false;
            }
        });
    }


    private boolean isBlank(EditText editText) {
        return editText.getText() == null || editText.getText().toString().trim().isEmpty();
    }


    private int getCardBrandImage(CardBrand cardBrand) {
        switch (cardBrand) {
            case VISA:
                return R.drawable.ic_visa;
            case MASTERCARD:
                return R.drawable.ic_mastercard;
            case AMEX:
                return R.drawable.bg_input;
            default:
                return 0;
        }
    }

    private String getSelectedPaymentOption() {
        RadioButton selected = findViewById(paymentOptionGroup.getCheckedRadioButtonId());
        return selected == null ? "" : selected.getText().toString();
    }

    private String getSelectedPaymentMethodCode() {
        int checkedId = paymentOptionGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.radioCardDetails) {
            return "CARD";
        }
        if (checkedId == R.id.radioUpi) {
            return "UPI";
        }
        if (checkedId == R.id.radioNetbanking) {
            return "NETBANKING";
        }
        if (checkedId == R.id.radiosamsungpay) {
            return "SAMSUNGPAY";
        }
        RadioButton selected = findViewById(checkedId);
        if (selected != null && selected.getTag() != null) {
            return selected.getTag().toString();
        }
        return "";
    }

    private void submitPaymentDetails() {
        showProgress();   // Show loader
        String paymentMethod = getSelectedPaymentMethodCode();


        if ("CARD".equals(paymentMethod)) {

            request = new PaymentDetailsRequest();

            request.setReferenceId(payId);
            request.setCustomerIp(NetworkUtils.getIpAdd());
            request.setAmount(amount);
            request.setCurrency(curr);


            PaymentInstrument instrument = new PaymentInstrument();
            instrument.setPaymentMethod(detectedPaymentMethod);
            request.setPaymentInstrument(instrument);

            String[] expiryParts = cardExpiry.getText().toString().trim().split("/");

            Card card = new Card();
            card.setNumber(cardNumber.getText().toString().replace(" ", ""));
            card.setCvv(cardCvv.getText().toString());
            card.setExpiryMonth(expiryParts[0]);
            card.setExpiryYear("20" + expiryParts[1]);

            request.setCard(card);
            request.setCardTokenFlag(cardTokenFlag);

            Customer customer = new Customer();
            customer.setCardHolderName(cardHolderName.getText().toString());
            customer.setCustomerEmail("test@example.com");
            request.setCustomer(customer);


            browser = new BrowserDetails();
            browser.setTransactionUuid(UUID.randomUUID().toString());
            browser.setBrowserLanguage(Locale.getDefault().toLanguageTag());
            browser.setBrowserScreenWidth("1080");
            browser.setBrowserScreenHeight("2400");
            browser.setBrowserColorDepth("24");
            browser.setBrowserJavascriptEnabled(true);
            browser.setBrowserJavaEnabled(false);
            browser.setBrowserAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            browser.setBrowserTZ(String.valueOf(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 60000));
            browser.setBrowserUserAgent(System.getProperty("http.agent"));
            request.setBrowserDetails(browser);

            viewModel.submitPaymentDetails(request)
                    .observe(this, json -> {

                       dismissProgress();

                        if (json == null) {

                            PaymentSDK.getInstance().notifyResult(
                                    "{\"result\":\"FAILURE\",\"message\":\"No Response\"}");

                            finish();
                            return;
                        }

                        try {

                            JSONObject root = new JSONObject(json);

                            String status = root.optString("status");

                            if ("CHALLENGE".equalsIgnoreCase(status)) {

                                JSONObject challenge =
                                        root.optJSONObject("3dsChanllengeResponse");

                                if (challenge != null) {

                                    String acsUrl = challenge.optString("acsUrl");
                                    String redirectHtml = challenge.optString("redirectHtml");

                                    Log.d("3DS", "ACS URL = " + acsUrl);
                                    Log.d("3DS", "Redirect HTML = " + redirectHtml);

                                    openThreeDS(acsUrl, redirectHtml);
                                    return;
                                }
                            } else {

                                // Direct Success / Failure
                                Log.e("FINAL_RESPONSE", json);

                                PaymentSDK.getInstance().notifyResult(json);

                                finish();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            PaymentSDK.getInstance().notifyResult(json);

                            finish();
                        }

                    });
        } else {
            launchUpiApp();

//            UpiApp selectedApp = upiAdapter.getSelectedApp();
//
//            if (selectedApp == null) {
//                Toast.makeText(this, "Please select a UPI App", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Log.d("UPI", "Selected App = " + selectedApp.getAppName());
//            Log.d("UPI", "Package = " + selectedApp.getPackageName());
        }


    }
    private void fetchCardType(String cardBin) {

        Log.d("CARD_BRAND_API", "Request URL : " +
                ConstantsVar.appUrl +
                "getCardBrandDetails.htm?cardBin=" + cardBin);

        Log.d("CARD_BRAND_API", "Request BIN : " + cardBin);

        viewModel.getCardBrandDetails(cardBin)
                .observe(this, response -> {

                    if (response == null) {
                        Log.e("CARD_BRAND_API", "Response : NULL");
                        return;
                    }

                    detectedPaymentMethod = response.getPaymentMethod();

                    Log.d("CARD_BRAND_API",
                            "Detected Payment Method : " + detectedPaymentMethod);

                    Log.d("CARD_BRAND_API",
                            "Response Object : " + new Gson().toJson(response));

                });
    }
    private void openThreeDS(String acsUrl,
                             String redirectHtml){

        paymentLayout.setVisibility(View.GONE);

        threeDSLayout.setVisibility(View.VISIBLE);

        WebSettings settings =
                webView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view,
                                      String url,
                                      Bitmap favicon) {

                Log.e("3DS","Started : "+url);

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view,
                                       String url) {

                Log.e("3DS","Finished : "+url);

                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view,
                                        WebResourceRequest request,
                                        WebResourceError error) {

                Log.e("3DS",
                        "ERROR : "+error.getDescription());
            }

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                Log.d("3DS", url);

                if (url.contains ("?data")){

                    handleGatewayCallback(url);

                    return true;
                }

                return false;
            }

        });

        String html =
                redirectHtml +

                        "<script>" +

                        "document.getElementById('threedsChallengeRedirectForm').submit();" +

                        "</script>";

        webView.loadDataWithBaseURL(

                acsUrl,

                html,

                "text/html",

                "UTF-8",

                null
        );

    }

    private void handleGatewayCallback(String url) {


        try {
            URI uri = new URI(url);
            // Get query from URI
            String query = uri.getQuery();
            // Parse query string to get parameters
            Map<String, String> queryParams = parseQuery(query);
            // Extract 'data' parameter value
            String data = queryParams.get("data");
            // Log or use the data

            // URL decode and replace spaces with '+'
            String decodedData = java.net.URLDecoder.decode(data, "UTF-8");
            decodedData = decodedData.replace(' ', '+');
            // Decrypt the data
            String encryptedResponse = decodedData;
            String merKey = ConstantsVar.merchanKey; // Ensure this key is set correctly
            String decryptedData = DecryptionUtil.decodeAndDecryptV2(encryptedResponse, merKey);
            System.out.println("payment Response Body: "+ decryptedData );



            Intent intent = new Intent();
            intent.putExtra("MESSAGE", decryptedData);
            Log.e("SDK_FLOW", "Before notifyResult");
            PaymentSDK.getInstance().notifyResult(decryptedData);
            Log.e("SDK_FLOW", "After notifyResult");
            finish();

        } catch (Exception e) {
            e.printStackTrace();

            Intent intent = new Intent();
            intent.putExtra("MESSAGE", "{\"status\":\"FAILED\",\"message\":\"3DS response processing failed\"}");
            handleFinalPaymentResponse(RESULT_CANCELED, intent);
        }
    }
    private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name()),
                                URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params;
    }
    private void launchUpiApp() {

        Intent launchIntent = getPackageManager()
                .getLaunchIntentForPackage("com.phonepe.app");

        if (launchIntent != null) {

            startActivity(launchIntent);

        } else {

            Toast.makeText(this,
                    "PhonePe is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private JSONObject buildPaymentDetailsRequest() {
        JSONObject requestJson = new JSONObject();
        try {
            String paymentMethod = getSelectedPaymentMethodCode();
            requestJson.put("transactionId", payId);
            requestJson.put("paymentMethod", paymentMethod);

            if ("CARD".equals(paymentMethod)) {
                String[] expiryParts = cardExpiry.getText().toString().trim().split("/");
                requestJson.put("cardNumber", cardNumber.getText().toString().replace(" ", "").trim());
                requestJson.put("cardholderName", cardHolderName.getText().toString().trim());
                requestJson.put("expiryMonth", expiryParts.length > 0 ? expiryParts[0] : "");
                requestJson.put("expiryYear", expiryParts.length > 1 ? normalizeExpiryYear(expiryParts[1]) : "");
                requestJson.put("cvv", cardCvv.getText().toString().trim());
            } else if ("UPI".equals(paymentMethod)) {
                requestJson.put("upiId", upiId.getText().toString().trim());
            }
        } catch (JSONException e) {
            Log.e("PAYMENT_DETAILS", "Unable to build request", e);
        }
        return requestJson;
    }

    private String normalizeExpiryYear(String year) {
        String trimmedYear = year.trim();
        if (trimmedYear.length() == 2) {
            return "20" + trimmedYear;
        }
        return trimmedYear;
    }





    private void returnFinalResponse(String responseBody) {
        ResponseConfig.startTrxn = false;
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", responseBody);
        handleFinalPaymentResponse(RESULT_OK, intent);
    }
    public List<UpiApp> getInstalledUpiApps(Context context) {

        List<UpiApp> upiApps = new ArrayList<>();
        Set<String> addedPackages = new HashSet<>();

        PackageManager pm = context.getPackageManager();

        Intent upiIntent = new Intent(Intent.ACTION_VIEW);
        upiIntent.setData(Uri.parse("upi://pay?pa=test@upi&pn=Test&am=1&cu=INR"));

        List<ResolveInfo> resolveInfos =
                pm.queryIntentActivities(
                        upiIntent,
                        PackageManager.MATCH_DEFAULT_ONLY
                );

        Log.d("UPI", "Apps Found = " + resolveInfos.size());

        for (ResolveInfo info : resolveInfos) {
            Log.d("UPI", "Package = " + info.activityInfo.packageName);
            Log.d("UPI", "Name = " + info.loadLabel(pm));
        }

        for (ResolveInfo info : resolveInfos) {

            String packageName = info.activityInfo.packageName;
            if (!addedPackages.add(packageName)) {
                continue;
            }

            String appName =
                    info.loadLabel(pm).toString();

            Drawable icon =
                    info.loadIcon(pm);

            upiApps.add(
                    new UpiApp(
                            appName,
                            packageName,
                            icon
                    )
            );
        }

        addInstalledUpiAppIfPresent(pm, upiApps, addedPackages, "com.google.android.apps.nbu.paisa.user");
        addInstalledUpiAppIfPresent(pm, upiApps, addedPackages, "com.phonepe.app");
        addInstalledUpiAppIfPresent(pm, upiApps, addedPackages, "in.org.npci.upiapp");
        addInstalledUpiAppIfPresent(pm, upiApps, addedPackages, "net.one97.paytm");

        return upiApps;
    }

    private void addInstalledUpiAppIfPresent(PackageManager pm,
                                             List<UpiApp> upiApps,
                                             Set<String> addedPackages,
                                             String packageName) {
        if (!addedPackages.add(packageName)) {
            return;
        }

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            upiApps.add(new UpiApp(
                    pm.getApplicationLabel(applicationInfo).toString(),
                    packageName,
                    pm.getApplicationIcon(applicationInfo)
            ));
        } catch (PackageManager.NameNotFoundException e) {
            addedPackages.remove(packageName);
            Log.d("UPI", "Package not installed = " + packageName);
        }
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    @Override
    public void onBackPressed() {
        ResponseConfig.startTrxn = false;
        super.onBackPressed();
    }
    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
    }
    private void handleFinalPaymentResponse(int resultCode, Intent data) {
        String response = data != null ? data.getStringExtra("MESSAGE") : "";
        Log.e("FINAL_RESPONSE", response);
        if (resultCode == RESULT_OK) {
            PaymentSDK.getInstance().notifyResult(response);

            finish();
        }
//        else if (resultCode == RESULT_CANCELED) {
//            PaymentSDK.getInstance().notifyCancel(response);
//        } else {
//
//            PaymentSDK.getInstance().notifyFailure(response);
//        }

        finish();
    }

}












