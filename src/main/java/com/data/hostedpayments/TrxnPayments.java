package com.data.hostedpayments;

import static android.content.Context.WIFI_SERVICE;

import static com.data.hostedpayments.utils.NetworkUtils.getIpAdd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.data.hostedpayments.Interface.SupportedPaymentCallback;
import com.data.hostedpayments.ResponseConfig;
import com.data.hostedpayments.Activity.Checkout;
import com.data.hostedpayments.Activity.PaymentOptionsActivity;
import com.data.hostedpayments.model.Response.SupportedPaymentResponse;
import com.data.hostedpayments.repository.PaymentRepository;
import com.data.hostedpayments.utils.AlertDialogManager;
import com.data.hostedpayments.utils.ConstantsVar;
import com.data.hostedpayments.utils.IntentConstants;
import com.data.hostedpayments.viewmodel.PaymentViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrxnPayments {

    static Checkout checkout = new Checkout();
    static AlertDialogManager alert = new AlertDialogManager();

    static String amt,cardToken,tokenizationOperation;


    static String IPaddress;
    static String spl;
    static String weburl, hashValue;
    static String deviceplat = "",action_Code;

    static Activity dataactivity;
    private static String currency,transID;
    private static String tokenType;
    static JSONObject generatedJson, orderdetails,customerdetails,tokenization,additionaldetails,deviceInfo,paymentInstr;
    JSONArray customPIP;
    private static ArrayList<String> supportedPaymentModes = new ArrayList<>();

    private static final PaymentRepository repository =
            new PaymentRepository();
    public static void makepaymentService(String amountd, Activity context, String action_Code, String Currency,
            String email, String address, String city, String state_code, String zip, String Country_Code, String TrackID, String cardOperation, String cardToken, String tokenType, String transID,String metadata) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, JSONException {

        dataactivity = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.e("FLOW", "1. makepaymentService()");
        Utilities.getAssetData(context);
        TrxnPayments.currency = Currency;
        TrxnPayments.tokenType = tokenType;
        TrxnPayments.transID = transID;
        Utilities.getBuildproperties(context);
        System.out.println("Expected Gradle distribution version: gradle-8.7-bin.zip | Found Gradle Version: " + ConstantsVar.buildgradleversion);
        //System.out.println("Current Gradle Version : " + ConstantsVar.buildgradleversion);
        System.out.println("Android SDK Initiated Successfully");
        System.out.println("Expected Android SDK Version: v3.0.3 ");
        //System.out.println("Current Android SDK Version installed "+ BuildConfig.SDK_VERSION_CODE);
        TrxnPayments.cardToken = cardToken;
        TrxnPayments.action_Code = action_Code;
        TrxnPayments.tokenizationOperation = cardOperation;
        if (ResponseConfig.startTrxn != ConstantsVar.appinitiateTrxn) {
            ResponseConfig.startTrxn = true;


            //---------Project Path START ----------
            String projectRoot = dataactivity.getFilesDir().getParent();
            System.out.println("Project Root File Path: "+projectRoot);


            //---------END----------
            if (isValidationSucess(context, amountd, email, action_Code, Country_Code, Currency, TrackID, cardOperation, cardToken, transID)) {
                //System.out.println("TrackID "+ TrackID);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                DecimalFormat deciformat = (DecimalFormat) nf;
                deciformat.applyPattern("##.##");
                Double db = Double.parseDouble(amountd);
                amt = deciformat.format(db);
                if (amt.contains(".")) {
                    String valnumber = amt.substring(amt.indexOf(".")).substring(1);
                    if (valnumber.length() == 1) {
                        amt = amt + "0";
                    }
                } else {
                    amt = amt + ".00";
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
             //   NetworkUtils.checkNetworkAndIP(context);
                IPaddress = getIpAdd();
                  boolean isdeviceTablet = checkIsTablet(context);
                if (isdeviceTablet) {
                    deviceplat = "Tablet";
                } else {
                    deviceplat = "Mobile";
                }

                String app_name= getAppLable(context);
                System.out.println("Android Application Name: "+app_name);
                // System.out.println("pluginVersion", BuildConfig.);
                deviceInfo = new JSONObject();
                try {
                    int verdev = Build.VERSION.SDK_INT;
                    deviceInfo.put("pluginName", "Android");
                    deviceInfo.put("pluginVersion", BuildConfig.SDK_VERSION_CODE);
                    deviceInfo.put("deviceType", deviceplat);
                    deviceInfo.put("deviceModel", Build.MANUFACTURER + " " + Build.MODEL);
                    deviceInfo.put("deviceOSVersion", "Android_" + verdev);
                    deviceInfo.put("clientPlatform", app_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //TODO for new request
                try {
                    // Parse JSON response
                    orderdetails = new JSONObject();
                    orderdetails.put("orderId", TrackID);
                    orderdetails.put("description", "");

                   // System.out.println("response Request orderobject =" + orderdetails);

                    // Print the extracted data
                    customerdetails= new JSONObject();
                    customerdetails.put("customerEmail", email);
                    customerdetails.put("billingAddressStreet", address);
                    customerdetails.put("billingAddressCity", city);
                    customerdetails.put("billingAddressState", state_code);
                    customerdetails.put("billingAddressPostalCode", zip);
                    customerdetails.put("billingAddressCountry", Country_Code);
                  //  System.out.println("response Request customerobject =" + customerdetails);

                    tokenization = new JSONObject();
                    tokenization.put("operation", cardOperation);
                    tokenization.put("cardToken", cardToken);
                  //  System.out.println("Request tokenization" + tokenization);



                    additionaldetails = new JSONObject();
                    additionaldetails.put("userData",metadata);

                    paymentInstr = new JSONObject();
                    paymentInstr.put("paymentMethod", "CCI");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadSupportedPaymentMethods(dataactivity);

             //   System.out.println("amountd = " + amountd + ", context = " + context + ", initFlag = " + ConstantsVar.appinitiateTrxn + ", action_Code = " + action_Code + ", hashValue = " + hashValue);
                Log.e("SDK_URL", ConstantsVar.appUrl);
//                if (checkConnection(context)) {
//                    // Internet Available...
//                    postData(ConstantsVar.appUrl, generatedJson, hashValue,
//                            context);
//                } else {
//                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
//                    ResponseConfig.startTrxn = false;
//                }


            }

        } else {
            Toast.makeText(context, "Transaction already Initiated", Toast.LENGTH_SHORT).show();
        }
//        return
    }
    static void loadSupportedPaymentMethods(Activity activity) {
        System.out.println("loadSupportedPaymentMethods()");
        String appUrl = ConstantsVar.appUrl;

        if (!appUrl.endsWith("/")) {
            appUrl += "/";
        }

        appUrl += "v2/payments/pay-request";
        String finalAppUrl = appUrl;
        repository.getSupportedPaymentMethods(
                ConstantsVar.termId,
                ConstantsVar.termPass,
                new SupportedPaymentCallback() {

                    @Override
                    public void onSuccess(SupportedPaymentResponse response) {

                        supportedPaymentModes =
                                new ArrayList<>(response.getSupportedPaymentModes());

                        // Call initiate transaction API here
                        if (checkConnection(activity)) {
                            try{
                                generatedJson = checkout.generateJson(ConstantsVar.termId, ConstantsVar.termPass, action_Code,
                                        currency, amt,tokenization, tokenType, orderdetails,customerdetails,transID,additionaldetails,IPaddress,deviceInfo,paymentInstr);

                                hashValue = checkout.generateHashKey(generatedJson, ConstantsVar.merchanKey);
                                // Internet Available... RUNALI CHANGES 17 July 2026
                                postData(finalAppUrl, generatedJson, hashValue,
                                    activity);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            ResponseConfig.startTrxn = false;
                        }
                    }

                    @Override
                    public void onFailure(String error) {

                        Toast.makeText(activity,
                                error,
                                Toast.LENGTH_LONG).show();

                        ResponseConfig.startTrxn = false;
                    }
                });
    }
    public static void extractData(String xmlData, Activity context) {
        JSONObject json = null;

        String merchantKey = "secret";
        try {
            json = new JSONObject(xmlData);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String targetUrl = "";

        String responsecode = "";
        String paymentid = "";
        Log.d("SDK_TOKEN", "Passing cardToken = " + cardToken);
        if (json != null) {
            try {

                if (json.getString("responseCode").equals("001")) {

             //   JSONObject paymentLinkObject = json.getJSONObject("paymentLink");

                // Retrieve the value associated with the key "linkUrl" from the "paymentLink" JSONObject
              //  targetUrl = paymentLinkObject.getString("linkUrl");

                // Output the linkUrl
               // System.out.println("Link URL: " + targetUrl);

              //  if (paymentLinkObject.has("targetUrl") && paymentLinkObject.getString("targetUrl") != null) {
              //      targetUrl = paymentLinkObject.getString("linkUrl");
              //  }

                if (json.getString("transactionId") != null) {
                    paymentid = json.getString("transactionId");

                }

                //if (targetUrl != null && !targetUrl.equalsIgnoreCase("null")) {

//                    if ((targetUrl.contains("?paymentId="))) {
//                        weburl = targetUrl + paymentid;
//                     //   System.out.println("weburl elseif= " + weburl);
//                    } else {
//
//                        weburl = targetUrl + "?" + "paymentid=" + paymentid;
//                       // System.out.println("weburl else= " + weburl);
//                    }
                    try {

                        Intent intent = new Intent(context, PaymentOptionsActivity.class);

                        intent.putStringArrayListExtra(
                                IntentConstants.EXTRA_SUPPORTED_PAYMENT_MODES,
                                supportedPaymentModes);

                        intent.putExtra(IntentConstants.EXTRA_PAY_ID, paymentid);
                        intent.putExtra(IntentConstants.EXTRA_AMOUNT, amt);
                        intent.putExtra(IntentConstants.EXTRA_TRANSACTION_TYPE, action_Code);
                        intent.putExtra(IntentConstants.EXTRA_CARD_TOKEN, cardToken);
                        intent.putExtra(IntentConstants.EXTRA_TOKENIZATION_OPERATION,tokenizationOperation );

                        intent.putExtra(IntentConstants.EXTRA_WEB_URL, weburl);
                        intent.putExtra(IntentConstants.EXTRA_RESP_DATA, "");

                        //dataactivity.startActivity(intent);
                        intent.putExtra(IntentConstants.EXTRA_CURRENCY, currency);
                        ResponseConfig.loadurl = true;
                        ResponseConfig.startTrxn = false;
                        context.startActivityForResult(intent, 2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                //}
            }
                else {
                    ResponseConfig.startTrxn = false;
                    PaymentSDK.getInstance().notifyResult(json.toString());
                }


            } catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    public String getIPAddress(Context context) {
        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI == true) {
            IPaddress = GetDeviceipWiFiData(context);
        }

        if (MOBILE == true) {
            IPaddress = GetDeviceipMobileData();
        }
        return IPaddress;
    }

    public static String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
//                      return inetAddress.getHostAddress().toString();
                        @SuppressWarnings("deprecation")
                        String ipA = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("IP Addr", "***** IP=" + ipA);
                       // System.out.println(" IP "+ipA);
                        return ipA;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public static String GetDeviceipWiFiData(Context context) {

        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        System.out.println(" IP "+ip);
        return ip;

    }


    public static boolean isValidationSucess(Context context, String amount, String email, String Action, String CountryCode, String Currency, String track, String cardOperation, String cardToken, String transid) {
        boolean d = false;
//        double am=Double.parseDouble(amount);
        boolean validcheck = isValidEmail(email);
        if (amount.isEmpty()) {
            alert.showAlertDialog(context, "Error", "Amount Should not be Empty", false);
            ResponseConfig.startTrxn = false;
        }
//        else if (email.isEmpty()) {
//            alert.showAlertDialog(context, "Error", "Email Should not be Empty", false);
//            ResponseConfig.startTrxn = false;
//        }
        else if (Action.isEmpty() || Action.length() == 0) {
            alert.showAlertDialog(context, "Error", "Action Code should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if (Currency.isEmpty() || Currency.length() == 0) {
            alert.showAlertDialog(context, "Error", "Currency should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if (CountryCode.isEmpty() || CountryCode.length() == 0) {
            alert.showAlertDialog(context, "Error", "Country Code should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if (track.isEmpty() || track.length() == 0) {
            alert.showAlertDialog(context, "Error", "Track ID should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if (Currency.length() > 3) {
            alert.showAlertDialog(context, "Error", "Currency should be proper ", false);
            ResponseConfig.startTrxn = false;
        }  else if (CountryCode.length() > 2) {
            alert.showAlertDialog(context, "Error", "CountryCode should be proper ", false);
            ResponseConfig.startTrxn = false;
        }
//        else if(!amount.isEmpty() && (am==0.0))
//        {
//
//                alert.showAlertDialog(context,"Error","Amount should not be 0",false);
//                startTrxn = false;
//
//        }
        else if (!email.isEmpty() && (validcheck == false)) {

            alert.showAlertDialog(context, "Error", "Email should be proper", false);
            ResponseConfig.startTrxn = false;

        } else if ((Action.equalsIgnoreCase("12")) && (cardOperation.equalsIgnoreCase("U")) && (cardToken.isEmpty())) {
            alert.showAlertDialog(context, "Error", "Card Token should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if ((Action.equalsIgnoreCase("12")) && (cardOperation.equalsIgnoreCase("D") && cardToken.isEmpty())) {
            alert.showAlertDialog(context, "Error", "Card Token should not be Empty", false);
            ResponseConfig.startTrxn = false;
        } else if ((Action.equalsIgnoreCase("12")) && (!(cardOperation.equalsIgnoreCase("A") || (cardOperation.equalsIgnoreCase("U")) || (cardOperation.equalsIgnoreCase("D"))))) {
            alert.showAlertDialog(context, "Invalid Tokenization", "Card Operation is not proper ", false);
            ResponseConfig.startTrxn = false;
        } else if ((Action.equalsIgnoreCase("2") && (transid.length() == 0))) {
            alert.showAlertDialog(context, "Error", "Transaction ID should be proper ", false);
            ResponseConfig.startTrxn = false;
        } else {
            d = true;
        }
        return d;
    }

    public static boolean isValidEmail(CharSequence target) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@([a-z]+\\.[a-z]+(\\.[a-z]+)?)";
//      ([a-z0-9._%+-]+@[a-z0-9.-]+(\.[a-z]+)?)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
      //  System.out.println(target + " : " + matcher.matches());

        return (!TextUtils.isEmpty(target) && matcher.matches());
    }


    public static void postData(String requesturl, JSONObject jsondata, String hashValue, final Activity context) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        try {

            HttpsTrustManager.allowAllSSL();
            jsondata.put("signature", hashValue);

            pDialog.setMessage("Loading...");
//            if(!(context).isFinishing())
            if (context != null && !context.isFinishing()) {
                pDialog.show();
            }
//        JSONObject object = new JSONObject();
//        try {
//            //input your API parameters
//            object.put("parameter","value");
//            object.put("parameter","value");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Enter the correct url for your api service site
//        String url = getResources().getString(R.string.url);

            final String mRequestBody = jsondata.toString();
            System.out.println("Request URL:"+ requesturl);
            System.out.println("Request Body: " + mRequestBody);
//            StringRequest stringRequest
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();

            MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");

            RequestBody body =
                    RequestBody.create(mRequestBody, JSON);

            Request request = new Request.Builder()
                    .url(requesturl)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                    if (context != null) {
                        context.runOnUiThread(() -> {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            ResponseConfig.startTrxn = false;

                            Log.e("PAYMENT_ERROR", e.getMessage(), e);

                            Toast.makeText(
                                    context,
                                    "Network Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }

                @Override
                public void onResponse(Call call, Response response)
                        throws IOException {

                    final String responseBody =
                            response.body() != null
                                    ? response.body().string()
                                    : "";

                    if (context != null) {
                        context.runOnUiThread(() -> {

                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            try {

                                System.out.println(
                                        "OkHttp Response: " + responseBody
                                );

                                if (response.isSuccessful()) {

                                    spl = responseBody;

                                    extractData(responseBody, context);

                                } else {

                                    ResponseConfig.startTrxn = false;

                                    Log.e(
                                            "PAYMENT_ERROR",
                                            "HTTP "
                                                    + response.code()
                                                    + " : "
                                                    + responseBody
                                    );

                                    Toast.makeText(
                                            context,
                                            "Server Error: "
                                                    + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            } catch (Exception ex) {

                                ResponseConfig.startTrxn = false;

                                Log.e(
                                        "PAYMENT_ERROR",
                                        ex.getMessage(),
                                        ex
                                );
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {

            ResponseConfig.startTrxn = false;

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            e.printStackTrace();
        }
    }



    public static boolean checkIsTablet(Context context) {
        boolean isTablet = false;
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        System.out.println("Device Screen Size in Inches: " + diagonalInches);
        if (diagonalInches >= 6.8) {
            isTablet = true;
        }

        return isTablet;
    }

    public static String getAppLable(Context context) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),0);
        } catch (final PackageManager.NameNotFoundException e) {
            Log.d("TAG", "The package with the given name cannot be found on the system.");
        }
        return (applicationInfo != null ? (String) context.getPackageManager().getApplicationLabel(applicationInfo) : "Application");
    }


    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
//            Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }


    public JSONArray createCustomPIP(String strPIP) throws JSONException {
        JSONObject pipjson = new JSONObject();

        String[] pairs = strPIP.split(",");
        int order = 1;
        JSONArray customisePaymentInstruments = new JSONArray();
        for (String pair : pairs) {
            JSONObject paymentObject = new JSONObject();

            paymentObject.put("paymentMethod", pair);
            paymentObject.put("order", String.valueOf(order++));
            customisePaymentInstruments.put(paymentObject);
        }

//        pipjson.put("customisePaymentInstruments", paymentObject);

return customisePaymentInstruments;

    }


}

//depend on merchant  token
//        if terminal is 3D secure configured then it will compulsory go to 3D sec even of tken type 0 or 1
