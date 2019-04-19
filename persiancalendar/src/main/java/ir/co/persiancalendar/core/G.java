package ir.co.persiancalendar.core;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.onesignal.OneSignal;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ir.co.persiancalendar.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.ContentValues.TAG;

/**
 * Created by PC4 on 11/29/2017.
 */

public class G extends Application {
    public static Context CONTEXT;
    public static SharedPreferences EVENTS;
    public static SharedPreferences FIRST_TIME_REQUEST;
    public static SharedPreferences AUTHENTICATIONS_SESSION;
    public static SharedPreferences AUTHENTICATIONS_TOKEN;
    public static SharedPreferences CUSTOMER_ID;
    public static SharedPreferences IS_REGISTERED;
    public static SharedPreferences GROUPS;
    public static SharedPreferences CREDIT;
    public static SharedPreferences CUSTOMER_NAME;
    public static SharedPreferences CUSTOMER_PHONE;
    public static SharedPreferences CUSTOMER_GENDER;
    public static SharedPreferences CUSTOMER_ADDRESS;
    public static SharedPreferences CUSTOMER_CITY;
    public static SharedPreferences CUSTOMER_BIRTH;
    public static SharedPreferences CUSTOMER_CODE;
    public static SharedPreferences CUSTOMER_EMAIL;
    public static SharedPreferences SERVICE_STATE;
    public static String oneSignalUserID;
    public static String oneSignalRegistrationID;
    public static Boolean isRegistered = false;
    public static String PRODUCT_GROUPS;
    public static String TARGET_GROUPS;
    public static String ANDROID_ID;
    public static String APP_VERSION;
    public static String ANDROID_VERSION;
    public static String DEVICE_NAME;
    public static String[] ENDTableFieldNames = new String[]{"MONTH", "PERCENT", "TOPIC", "ID"};
    public static String[] ENDTableFieldTypes = new String[]{"INT", "INT", "TEXT", "INT"};
    public static String[] EVENTTableFieldNames = new String[]{"MONTH", "DAY", "TIME", "TOPIC"};
    public static String[] EVENTTableFieldTypes = new String[]{"INT", "INT", "TEXT", "TEXT"};
    public static String[] ProductTableFieldNames = new String[]{"GROUP_ID", "ID", "NAME", "CURRENT_PRICE", "PREVIOUS_PRICE", "QUANTITY", "COMMENT", "IMAGE", "DATE", "TIME", "UNIT"};
    public static String[] ProductTableFieldTypes = new String[]{"TEXT", "TEXT", "TEXT", "INT", "INT", "INT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
    public static String[] OrderTableFieldNames = new String[]{"ID", "NAME", "CURRENT_PRICE", "PREVIOUS_PRICE", "QUANTITY", "COMMENT", "IMAGE"};
    public static String[] OrderTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT", "TEXT"};
    public static String[] HistoryTableFieldNames = new String[]{"JSON", "DATE", "Empty1", "Empty2", "Empty3", "TIME", "IMAGE"};
    public static String[] HistoryTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT", "TEXT"};
    public static String[] ConversationTableFieldNames = new String[]{"conversationId", "title", "unReadMessageCount", "senderName", "receiverName", "lastMessage", "theDate", "theTime"};
    public static String[] ConversationTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
    public static double LATITUDE = 0, LONGTITUDE = 0;
    public static SharedPreferences TARGET_GROUPS_SP;
    public static SharedPreferences MEMBER_CODE;
    public static int month = 1;
    public static int year = 1397;
    public static SharedPreferences dayPhrase ;
    public static String date;
    public static SharedPreferences PHRASE_OWNER;
    public static String monthName;
    public static ArrayList<String> WEEK_STORY_GROUPS_ARRAY= new ArrayList<>();
    public static boolean isBySms;
    public static boolean isRunning=false;
    private GPSTracker gps;
    public static String georgianDate;
    private String strMonthGeorgian;
    public static int APP_VERSION_CODE;


    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();

        EVENTS = getSharedPreferences("EVENTS", MODE_PRIVATE);

        //define font library
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("iran_medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()


        );

        //initialise one signal
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        //get one signal user and registration id
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                oneSignalUserID = userId;
                if (registrationId != null)
                    oneSignalRegistrationID = registrationId;
            }
        });

        ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ANDROID_VERSION = Build.VERSION.RELEASE;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            APP_VERSION = pInfo.versionName;
            APP_VERSION_CODE = pInfo.versionCode;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DEVICE_NAME = getDeviceName();

        Log.d("android_id", ANDROID_ID);
        Log.d("android_version", String.valueOf(ANDROID_VERSION));
        Log.d("app_version", APP_VERSION);
        Log.d("device_name", DEVICE_NAME);


        //defining shared preferences
        FIRST_TIME_REQUEST = getSharedPreferences("FIRST_TIME_CONNECTION", MODE_PRIVATE);
        AUTHENTICATIONS_SESSION = getSharedPreferences("SESSION", MODE_PRIVATE);
        AUTHENTICATIONS_TOKEN = getSharedPreferences("TOKEN", MODE_PRIVATE);
        CUSTOMER_ID = getSharedPreferences("CUSTOMER_ID", MODE_PRIVATE);
        IS_REGISTERED = getSharedPreferences("IS_REGISTERED", MODE_PRIVATE);
        GROUPS = getSharedPreferences("GROUPS", MODE_PRIVATE);
        TARGET_GROUPS_SP = getSharedPreferences("TARGET_GROUPS_SP", MODE_PRIVATE);
        CREDIT = getSharedPreferences("CREDIT", MODE_PRIVATE);
        CUSTOMER_NAME = getSharedPreferences("CUSTOMER_NAME", MODE_PRIVATE);
        CUSTOMER_PHONE = getSharedPreferences("CUSTOMER_PHONE", MODE_PRIVATE);
        CUSTOMER_GENDER = getSharedPreferences("CUSTOMER_GENDER", MODE_PRIVATE);
        CUSTOMER_ADDRESS = getSharedPreferences("CUSTOMER_ADDRESS", MODE_PRIVATE);
        CUSTOMER_EMAIL = getSharedPreferences("CUSTOMER_EMAIL", MODE_PRIVATE);
        CUSTOMER_CITY = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE);
        CUSTOMER_BIRTH = getSharedPreferences("CUSTOMER_BIRTH", MODE_PRIVATE);
        CUSTOMER_CODE = getSharedPreferences("CUSTOMER_CODE", MODE_PRIVATE);
        MEMBER_CODE = getSharedPreferences("MEMBER_CODE", MODE_PRIVATE);
        SERVICE_STATE = getSharedPreferences("SERVICE_STATE", MODE_PRIVATE);
        PHRASE_OWNER = getSharedPreferences("PHRASE_OWNER", MODE_PRIVATE);
        dayPhrase = getSharedPreferences("dayPhrase", MODE_PRIVATE);

        gpsData();


        Calendar cal = Calendar.getInstance();

        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        //12 hour format
        int hour = cal.get(Calendar.HOUR);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);


        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);

        switch (month) {
            case 1:
                strMonthGeorgian = "ژانویه";
                break;
            case 2:
                strMonthGeorgian = "فوریه";
                break;
            case 3:
                strMonthGeorgian = "مارس";
                break;
            case 4:
                strMonthGeorgian = "آپریل";
                break;
            case 5:
                strMonthGeorgian = "می";
                break;
            case 6:
                strMonthGeorgian = "ژوئن";
                break;
            case 7:
                strMonthGeorgian = "جولای";
                break;
            case 8:
                strMonthGeorgian = "آگوست";
                break;
            case 9:
                strMonthGeorgian = "سپتامبر";
                break;
            case 10:
                strMonthGeorgian = "اکتبر";
                break;
            case 11:
                strMonthGeorgian = "نوامبر";
                break;
            case 12:
                strMonthGeorgian = "دسامبر";
                break;
        }

        georgianDate =  String.valueOf(dayofmonth) + " " + strMonthGeorgian + " " + String.valueOf(year);

    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void gpsData() {
        GPSTracker gps;
        double latitude, longitude;
        latitude = 0;
        longitude = 0;
        gps = new GPSTracker();
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        String LocationAddress = latitude + "/" + longitude;
//        Toast.makeText(CONTEXT, LocationAddress, Toast.LENGTH_SHORT).show();

    }

}
