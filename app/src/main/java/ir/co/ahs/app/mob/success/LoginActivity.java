package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class LoginActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private TextView dialog_phone_txt1;
    private TextView dialog_phone_counter;
    private EditText dialog_phone_edt_txt;
    private EditText dialog_phone_edt_txt_code;
    private TextView dialog_phone_resendCode;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Dialog
        dialog_phone = new Dialog(LoginActivity.this);
        dialog_phone.setContentView(R.layout.dialog_phone);
        dialog_phone_resendCode = (TextView) dialog_phone.findViewById(R.id.resendCode);
        TextView dialog_phone_txt = (TextView) dialog_phone.findViewById(R.id.dialog_txt);
        dialog_phone_txt1 = (TextView) dialog_phone.findViewById(R.id.dialog_txt1);
        dialog_phone_counter = (TextView) dialog_phone.findViewById(R.id.dialog_counter);
        dialog_phone_edt_txt = (EditText) dialog_phone.findViewById(R.id.dialog_edt_txt);
        dialog_phone_edt_txt_code = (EditText) dialog_phone.findViewById(R.id.dialog_edt_txt_code);
        TextView dialog_phone_cancel = (TextView) dialog_phone.findViewById(R.id.cancel);
        TextView dialog_phone_register = (TextView) dialog_phone.findViewById(R.id.register);

        dialog_phone_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog_phone_edt_txt.getText().toString().length() == 11) {

                    String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                    String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");

                    Log.d("session", sessionId);
                    Log.d("token", userToken);
                    new getActivationCode().execute(Urls.BASE_URL + Urls.SEND_ACTIVATION_CODE, sessionId, userToken, dialog_phone_edt_txt.getText().toString());
                } else {
                    dialog_phone_edt_txt.setText("");
                    dialog_phone_edt_txt.setHint("شماره تلفن خود را وارد کنید");
                }
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog_phone_txt1.setTextColor(getResources().getColor(R.color.Red));
                dialog_phone_txt1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dialog_phone_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog_phone_edt_txt.getText().toString().length() == 11 && dialog_phone_edt_txt_code.getText().toString().length() != 0) {
                    runSessionService();

                } else {
                    dialog_phone_txt1.setText("شماره تلفن یا کد عضویت اشتباه است");
                    dialog_phone_txt1.startAnimation(animation);
                }

            }
        });
        dialog_phone_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_phone.dismiss();
            }
        });

        TextView login = (TextView) findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_phone.show();
                if (G.isBySms) {
                    dialog_phone_edt_txt_code.setHint("کدفعالسازی");
                    dialog_phone_resendCode.setVisibility(View.VISIBLE);
                } else {
                    dialog_phone_edt_txt_code.setHint("کد عضویت");
                    dialog_phone_resendCode.setVisibility(View.GONE);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    private class getCustomerAsync extends Webservice.login {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                String x = result;
//                JSONArray mainArray = new JSONArray(result);
                JSONObject mainObject = new JSONObject(result);
                String customerId = mainObject.getString("customerId");
                String customerName = mainObject.getString("firstName") + " " + mainObject.getString("lastName");
                String customerPhone = mainObject.getString("cellPhone");
                String customerAddress = mainObject.getString("address");
                String customerBirth = mainObject.getString("birthDate");
                String customerCode = mainObject.getString("memberCode");
                int customerCity = mainObject.getInt("cityId");
                int customerGender = mainObject.getInt("gender");
                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();
                G.CUSTOMER_ID.edit().putString("CUSTOMER_ID", customerId).apply();
                G.CUSTOMER_NAME.edit().putString("CUSTOMER_NAME", customerName).apply();
                G.CUSTOMER_PHONE.edit().putString("CUSTOMER_PHONE", customerPhone).apply();
                G.CUSTOMER_GENDER.edit().putInt("CUSTOMER_GENDER", customerGender).apply();
                G.CUSTOMER_ADDRESS.edit().putString("CUSTOMER_ADDRESS", customerAddress).apply();
                G.CUSTOMER_CITY.edit().putInt("CUSTOMER_CITY", customerCity).apply();
                G.CUSTOMER_BIRTH.edit().putString("CUSTOMER_BIRTH", customerBirth).apply();
                G.CUSTOMER_CODE.edit().putString("CUSTOMER_CODE", customerCode).apply();
//                Log.d("request result", sessionId);
//                Log.d("request result", userToken);
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
                new CreditAsync().execute(Urls.BASE_URL + Urls.GET_SCORE, sessionId, userToken, customerId);
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "مشخصات وارد شده اشتباه است!", Toast.LENGTH_SHORT).show();
                // TODO : نشان دادن پیغام خطا
            }
        }
    }

    public void runSessionService() {
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

                    new AsyncSesssion().execute(Urls.BASE_URL + Urls.REGISTER_SESSION, oneSignalUserID, oneSignalRegistrationID, uniqueId, androidVersion, appVersion, deviceName);
                    G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", true).apply();
                }
            });
        } else {
            getCustomer();
        }
    }

    public void getCustomer() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String phone = dialog_phone_edt_txt.getText().toString();
        String memberCode = dialog_phone_edt_txt_code.getText().toString();
//        new getCustomerAsync().execute(Urls.BASE_URL + Urls.GET_CUSTOMER, sessionId, userToken, phone, memberCode);
        new getCustomerAsync().execute(Urls.BASE_URL + Urls.LOGIN, sessionId, userToken, phone, memberCode, String.valueOf(G.isBySms));
    }

    private class AsyncSesssion extends Webservice.sessionConnection {
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

                getCustomer();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class CreditAsync extends Webservice.getScore {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
//                JSONArray main = new JSONArray(result);
                JSONObject mainObject = new JSONObject(result);
                JSONArray scoreList = mainObject.getJSONArray("scoreItemsList");
                int Credit = mainObject.getInt("creditValue");
                int Income = mainObject.getInt("incomeValue");
                int Outcome = mainObject.getInt("outcomeValue");
                String convertedCredit = String.format("%,d", Credit);
                String convertedIncome = String.format("%,d", Income);
                String convertedOutcome = String.format("%,d", Outcome);
                G.CREDIT.edit().putInt("CREDIT", Credit).apply();


                Toast.makeText(LoginActivity.this, "ورود با موفقیت انجام شد!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();

            } catch (JSONException e) {


            }


        }
    }


    private class getActivationCode extends Webservice.getCode {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {

            String x = result;
            try {
                JSONObject main = new JSONObject(result);
                String response = main.getString("resendResult");
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {


            }


        }
    }
}