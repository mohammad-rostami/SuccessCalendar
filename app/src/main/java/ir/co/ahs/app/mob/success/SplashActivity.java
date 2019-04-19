package ir.co.ahs.app.mob.success;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import ir.co.ahs.app.mob.success.Helper.GPSTracker;
import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class SplashActivity extends AppCompatActivity {
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private GPSTracker gps;
    private double LATITUDE;
    private double LONGTITUDE;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        Intent service = new Intent(SplashActivity.this, NotificationService.class);
        if (!NotificationService.IS_SERVICE_RUNNING) {
            service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            NotificationService.IS_SERVICE_RUNNING = true;
//            button.setText("Stop Service");
        } else {
            service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            NotificationService.IS_SERVICE_RUNNING = false;
//            button.setText("Start Service");

        }
        startService(service);

        if (G.SERVICE_STATE.getBoolean("SERVICE_STATE", true)) {
            if (!NotificationService.IS_SERVICE_RUNNING) {
                Intent j = new Intent(getApplicationContext(), NotificationService.class);
                getApplicationContext().startService(j);
                NotificationService.IS_SERVICE_RUNNING = true;
//                G.SERVICE_STATE.edit().putBoolean("SERVICE_STATE", true).apply();

            }
        } else {
            Intent j = new Intent(getApplicationContext(), NotificationService.class);
            getApplicationContext().stopService(j);
            NotificationService.IS_SERVICE_RUNNING = false;
            G.SERVICE_STATE.edit().putBoolean("SERVICE_STATE", false).apply();
        }


        boolean firstTimeConnection = G.FIRST_TIME_REQUEST.getBoolean("FIRST_TIME_CONNECTION", false);
        if (!firstTimeConnection) {
            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                @Override
                public void idsAvailable(String userId, String registrationId) {
                    oneSignalUserID = userId;
                    if (registrationId != null)
                        oneSignalRegistrationID = registrationId;

                    Log.d("userId", oneSignalUserID);
                    Log.d("registerId", oneSignalRegistrationID);
                    String uniqueId = G.ANDROID_ID;
                    String androidVersion = G.ANDROID_VERSION;
                    String appVersion = G.APP_VERSION;
                    String deviceName = G.DEVICE_NAME;

                    new Async().execute(Urls.BASE_URL + Urls.REGISTER_SESSION, oneSignalUserID, oneSignalRegistrationID, uniqueId, androidVersion, appVersion, deviceName);

                }
            });
            G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", true).apply();
        } else {

            if (G.isNetworkAvailable()) {

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");

                Log.d("session", sessionId);
                Log.d("token", userToken);
                new getProductGroupAsync().execute(Urls.BASE_URL + Urls.GET_PRODUCT_GROUP, sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE));

            } else {
//                Toast.makeText(SplashActivity.this, "connection is lost", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, CalendarActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }
        }

        TextView ver = (TextView)findViewById(R.id.ver);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String APP_VERSION = pInfo.versionName;
            int APP_VERSION_CODE = pInfo.versionCode;
            ver.setText(APP_VERSION);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean firstTimeConnection = G.FIRST_TIME_REQUEST.getBoolean("FIRST_TIME_CONNECTION", false);
//                if (!firstTimeConnection) {
//                    new Async().execute("http://mcapi.ahscoltd.ir/mcapi/RegisterSession", oneSignalUserID, oneSignalRegistrationID);
//                    G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", true).apply();
//                }
//            }
//        },2000);


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("token",G.AUTHENTICATIONS_TOKEN.getString("TOKEN","nothing"));
//                Intent intent = new Intent(ActivitySplash.this, ActivityMain.class);
//                ActivitySplash.this.startActivity(intent);
//                finish();
//            }
//        }, 2000);


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location;

        if (network_enabled) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
//                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            }
        }

    }

    private class Async extends Webservice.sessionConnection {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONObject sessionData = new JSONObject(result);
                String sessionId = sessionData.getString("SessionId");
                String userToken = sessionData.getString("UserToken");

                G.AUTHENTICATIONS_SESSION.edit().putString("SESSION", sessionId).apply();
                G.AUTHENTICATIONS_TOKEN.edit().putString("TOKEN", userToken).apply();


                new getProductGroupAsync().execute(Urls.BASE_URL + Urls.GET_PRODUCT_GROUP, sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getProductGroupAsync extends Webservice.getProductGroup {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            G.PRODUCT_GROUPS = result;
            G.GROUPS.edit().putString("GROUPS", result).apply();

            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");

            new getTargetGroupAsync().execute(Urls.BASE_URL + Urls.GET_TARGET_GROUP, sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), "1396", "1");

        }
    }

    private class getTargetGroupAsync extends Webservice.getTargetGroup {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            G.TARGET_GROUPS = result;
            G.TARGET_GROUPS_SP.edit().putString("TARGET_GROUPS_SP", result).apply();

            getWeekMessagesGroups();


        }
    }

    public void getWeekMessagesGroups() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new AsyncWeekGroups().execute(Urls.BASE_URL + Urls.GET_WEEK_MESSAGE_GROUPS, sessionId, userToken);
    }

    private class AsyncWeekGroups extends Webservice.getWeekGroups {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
//            arrayList.clear();
//
//
            JSONArray array = null;
            G.WEEK_STORY_GROUPS_ARRAY.clear();
            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String groupId = object.getString("storyGroupId");
                    String groupTitle = object.getString("storyGroupTitle");
                    G.WEEK_STORY_GROUPS_ARRAY.add(groupId+":"+groupTitle);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getLoginType();
//            Intent intent = new Intent(SplashActivity.this, CalendarActivity.class);
//            SplashActivity.this.startActivity(intent);
//            finish();

        }
    }
    public void getLoginType() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        new AsyncLoginType().execute(Urls.BASE_URL + Urls.GET_LOGIN_TYPE, sessionId, userToken);
    }
    private class AsyncLoginType extends Webservice.getInfo {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
//            arrayList.clear();
//
//
            JSONArray array = null;
            try {
                JSONObject jsonObject = new JSONObject(result);
                G.isBySms = jsonObject.getBoolean("isBySMS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashActivity.this, CalendarActivity.class);
            SplashActivity.this.startActivity(intent);
            finish();

        }
    }

}
