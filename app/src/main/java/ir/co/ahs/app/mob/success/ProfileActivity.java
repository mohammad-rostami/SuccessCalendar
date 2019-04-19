package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.onesignal.OneSignal;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseHelper;
import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class ProfileActivity extends Activity {
    private Dialog dialogVerify;
    private EditText activityRegister_firstName;
    private EditText activityRegister_lastName;
    private EditText activityRegister_phone;
    private Boolean fieldsAreFilled = false;
    private TextView dialogVerify_txt_0;
    private TextView dialogVerify_txt_1;
    private TextView dialogVerify_txt_counter;
    private EditText dialogVerify_edt_code;
    private TextView dialogVerify_btn_cancel;
    private TextView dialogVerify_btn_register;
    private TextView activityRegister_btn_register;
    private EditText activityRegister_birth;
    private RadioButton activityRegister_rb_male;
    private RadioButton activityRegister_rb_female;
    private Dialog dialogDateSelector;
    private TextView dialogDateSelector_btn_register;
    private TextView dialogDateSelector_btn_cancel;
    private TextView dialogDateSelector_txt_0;
    private NumberPicker dialogDateSelector_np_year;
    private NumberPicker dialogDateSelector_np_month;
    private NumberPicker dialogDateSelector_np_day;
    private String selectedYear = "1369";
    private String selectedMonth = "11";
    private String selectedDay = "23";
    private LinearLayout activityRegister_logo;
    private ArrayList<String> stateName = new ArrayList<>();
    private ArrayList<String> cityName = new ArrayList<>();
    private Boolean isFirstTime = true;
    private String selectedCity = "8";
    private Dialog dialogSuccess;
    private TextView dialogSuccess_txt_memberCode;
    private TextView dialogSuccess_btn_register;
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private EditText activityRegister_city;
    private EditText activityRegister_code;
    private EditText activityRegister_address;
    private LinearLayout address_box;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        try{
            String value = getIntent().getStringExtra("value");
//            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
        // Views
        activityRegister_logo = (LinearLayout) findViewById(R.id.logo);
        address_box = (LinearLayout) findViewById(R.id.address_box);
        activityRegister_firstName = (EditText) findViewById(R.id.first_name);
        activityRegister_lastName = (EditText) findViewById(R.id.last_name);
        activityRegister_phone = (EditText) findViewById(R.id.phone);
        activityRegister_birth = (EditText) findViewById(R.id.birth);
        activityRegister_city = (EditText) findViewById(R.id.city);
        activityRegister_code = (EditText) findViewById(R.id.code);
        activityRegister_address = (EditText) findViewById(R.id.address);
        activityRegister_birth.setText("1369/11/23");
        activityRegister_rb_male = (RadioButton) findViewById(R.id.male);
        activityRegister_rb_female = (RadioButton) findViewById(R.id.female);
        activityRegister_btn_register = (TextView) findViewById(R.id.btn_register);



        activityRegister_firstName.setText(G.CUSTOMER_NAME.getString("CUSTOMER_NAME", "empty"));
        activityRegister_phone.setText(G.CUSTOMER_PHONE.getString("CUSTOMER_PHONE", "empty"));
        activityRegister_birth.setText(G.CUSTOMER_BIRTH.getString("CUSTOMER_BIRTH", "empty"));
        if(G.CUSTOMER_ADDRESS.getString("CUSTOMER_ADDRESS", "empty").length()<1){
            address_box.setVisibility(View.GONE);
        }else {
            address_box.setVisibility(View.VISIBLE);
            activityRegister_address.setText(G.CUSTOMER_ADDRESS.getString("CUSTOMER_ADDRESS", "empty"));
        }
        activityRegister_code.setText(G.CUSTOMER_CODE.getString("CUSTOMER_CODE", "empty"));
        int id = G.CUSTOMER_CITY.getInt("CUSTOMER_CITY", 0);
        String title = cityDataBaseChecker("srCityBig", "citname", "_id", String.valueOf(id));
        activityRegister_city.setText(title);

//        selectedCity = cityDataBaseChecker("srCityBig", "_id", "citname", id);
//        activityRegister_city.setText(selectedCity);

        activityRegister_firstName.setEnabled(false);
        activityRegister_phone.setEnabled(false);
        activityRegister_birth.setEnabled(false);
        activityRegister_code.setEnabled(false);
        activityRegister_address.setEnabled(false);
        activityRegister_city.setEnabled(false);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("پروفایل");
        TextView btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityRegister_rb_male.setChecked(true);


        // Dialog
        dialogVerify = new Dialog(ProfileActivity.this);
        dialogVerify.setContentView(R.layout.dialog_verify);
        dialogVerify.setTitle("Custom Dialog");
        dialogVerify_txt_0 = (TextView) dialogVerify.findViewById(R.id.dialog_txt);
        dialogVerify_txt_1 = (TextView) dialogVerify.findViewById(R.id.dialog_txt1);
        dialogVerify_txt_counter = (TextView) dialogVerify.findViewById(R.id.dialog_counter);
        dialogVerify_edt_code = (EditText) dialogVerify.findViewById(R.id.dialog_edt_txt);
        dialogVerify_btn_cancel = (TextView) dialogVerify.findViewById(R.id.cancel);
        dialogVerify_btn_register = (TextView) dialogVerify.findViewById(R.id.register);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                dialogVerify_txt_counter.setText(String.valueOf("00:" + millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                dialogVerify_txt_counter.setText("00:00");
            }

        }.start();
        dialogVerify_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogVerify_edt_code.getText().toString().length() < 6) {
                    dialogVerify_txt_1.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.putExtra("name", activityRegister_firstName.getText().toString() + " " + activityRegister_lastName.getText().toString());
                    ProfileActivity.this.startActivity(intent);
                    finish();
                }

            }
        });
        dialogVerify_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogVerify.dismiss();
            }
        });

        // Dialog
        dialogSuccess = new Dialog(ProfileActivity.this);
        dialogSuccess.setContentView(R.layout.dialog_credit_code);
        dialogSuccess.setCancelable(false);
//        dialogSuccess.setTitle("Custom Dialog");
        dialogSuccess_txt_memberCode = (TextView) dialogSuccess.findViewById(R.id.memberCode);
        dialogSuccess_btn_register = (TextView) dialogSuccess.findViewById(R.id.register);
        dialogSuccess_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                ProfileActivity.this.startActivity(intent);
                finish();

            }
        });


        // Dialog
        dialogDateSelector = new Dialog(ProfileActivity.this);
        dialogDateSelector.setContentView(R.layout.dialog_date_pick);
        dialogDateSelector.setTitle("Custom Dialog");
        dialogDateSelector.setCancelable(false);
        dialogDateSelector_txt_0 = (TextView) dialogDateSelector.findViewById(R.id.dialog_txt);
        dialogDateSelector_np_year = (NumberPicker) dialogDateSelector.findViewById(R.id.year_picker);
        dialogDateSelector_np_month = (NumberPicker) dialogDateSelector.findViewById(R.id.month_picker);
        dialogDateSelector_np_day = (NumberPicker) dialogDateSelector.findViewById(R.id.day_picker);
        dialogDateSelector_btn_cancel = (TextView) dialogDateSelector.findViewById(R.id.cancel);
        dialogDateSelector_btn_register = (TextView) dialogDateSelector.findViewById(R.id.register);

        dialogDateSelector_np_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedYear = String.valueOf(newVal);
            }
        });
        dialogDateSelector_np_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (String.valueOf(newVal).length() == 1) {
                    selectedMonth = "0" + String.valueOf(newVal);

                } else {
                    selectedMonth = String.valueOf(newVal);
                }
            }
        });
        dialogDateSelector_np_day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (String.valueOf(newVal).length() == 1) {
                    selectedDay = "0" + String.valueOf(newVal);

                } else {
                    selectedDay = String.valueOf(newVal);
                }
            }
        });

        dialogDateSelector_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = selectedYear + "/" + selectedMonth + "/" + selectedDay;
                activityRegister_birth.setText("1369/11/23");
                dialogDateSelector.dismiss();
                activityRegister_birth.clearFocus();
            }
        });
        dialogDateSelector_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDateSelector.dismiss();
                activityRegister_birth.clearFocus();
            }
        });


        activityRegister_birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogDateSelector.show();
                }
            }
        });

        activityRegister_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortcutIcon();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            String firstName = activityRegister_firstName.getText().toString();
            String lastName = activityRegister_lastName.getText().toString();
            String phoneNumber = activityRegister_phone.getText().toString();
            String birthDate = activityRegister_birth.getText().toString();
            int gender = 1;
            if (activityRegister_rb_male.isChecked()) {
                gender = 1;
            } else if (activityRegister_rb_female.isChecked()) {
                gender = 2;
            }
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");

            new newAsync().execute(Urls.BASE_URL + Urls.REGISTER_CUSTOMER, sessionId, userToken,
                    firstName, lastName, phoneNumber, birthDate, String.valueOf(gender), selectedCity);

        }
    }

    private void fieldChecker() {
    }

    private boolean checker(final EditText editText, int limit, String text) {
        Boolean result = true;
        final String typedText = editText.getText().toString();
        if (editText.getText().toString().length() == 0) {
            editText.setText("");
            editText.setHint(text + " خود را وارد کنید");
            editText.setHintTextColor(getResources().getColor(R.color.Red));
            editText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.setText(typedText);
                    editText.setSelection(editText.getText().length());
                }
            }, 1000);
            result = false;
        } else if (editText.getText().toString().length() < limit) {
            editText.setHint(text + " وارد شده اشتباه است");
            editText.setText("");
            editText.setHintTextColor(getResources().getColor(R.color.Red));
            editText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.setText(typedText);
                    editText.setSelection(editText.getText().length());
                }
            }, 1000);
            result = false;
        }
        if (limit == 11 && editText.getText().toString().length() == 11) {
            if (String.valueOf(editText.getText().toString().charAt(0)).equals("0")
                    && String.valueOf(editText.getText().toString().charAt(1)).equals("9")) {
            } else {
                editText.setHint(text + " باید با ۰۹ آغاز شود");
                editText.setText("");
                editText.setHintTextColor(getResources().getColor(R.color.Red));
                editText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(typedText);
                        editText.setSelection(editText.getText().length());
                    }
                }, 1000);
                result = false;
            }
        }
        return result;
    }

    private boolean spinnerChecker(final MaterialSpinner spinner, final MaterialSpinner citySpinner) {
        Boolean result = true;
        try {
            if (spinner.getItems().size() == 0) {
                spinner.setHint("استان را وارد کنید");
                spinner.setHintTextColor(getResources().getColor(R.color.Red));
                spinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
                citySpinner.setHint("شهر را وارد کنید");
                citySpinner.setHintTextColor(getResources().getColor(R.color.Red));
                citySpinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setHint("استان");
                        citySpinner.setHint("شهر");
                        spinner.setHintTextColor(getResources().getColor(R.color.lightGray));
                        citySpinner.setHintTextColor(getResources().getColor(R.color.lightGray));

                    }
                }, 1000);
                result = false;
            }
        } catch (Exception e) {
            spinner.setHint("استان را وارد کنید");
            spinner.setHintTextColor(getResources().getColor(R.color.Red));
            spinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
            citySpinner.setHint("شهر را وارد کنید");
            citySpinner.setHintTextColor(getResources().getColor(R.color.Red));
            citySpinner.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner.setHint("استان");
                    citySpinner.setHint("شهر");
                    spinner.setHintTextColor(getResources().getColor(R.color.lightGray));
                    citySpinner.setHintTextColor(getResources().getColor(R.color.lightGray));

                }
            }, 1000);
            result = false;

        }

        return result;
    }

    private String stateDataBaseChecker(String TableName, String column) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " order by " + column, null);
        String title = null;
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(column));
                stateName.add(title);
            } while (cursor.moveToNext());
        }
        sqld.close();
        return title;
    }

    private class newAsync extends Webservice.postClass {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONObject mainObject = new JSONObject(result);
                String customerId = mainObject.getString("customerId");
                String customerName = mainObject.getString("firstName") + " " + mainObject.getString("lastName");
                String memberCode = mainObject.getString("memberCode");
                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();
                G.CUSTOMER_ID.edit().putString("CUSTOMER_ID", customerId).apply();
                G.CUSTOMER_NAME.edit().putString("CUSTOMER_NAME", customerName).apply();
                dialogSuccess_txt_memberCode.setText(memberCode);
                dialogSuccess.show();
//                Log.d("request result", sessionId);
//                Log.d("request result", userToken);

            } catch (JSONException e) {

                Toast.makeText(ProfileActivity.this, "ثبت نام با خطا مواجه شد!", Toast.LENGTH_SHORT).show();
                // TODO : نشان دادن پیغام خطا

            }

        }
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

                String firstName = activityRegister_firstName.getText().toString();
                String lastName = activityRegister_lastName.getText().toString();
                String phoneNumber = activityRegister_phone.getText().toString();
                String birthDate = activityRegister_birth.getText().toString();
                int gender = 1;
                if (activityRegister_rb_male.isChecked()) {
                    gender = 1;
                } else if (activityRegister_rb_female.isChecked()) {
                    gender = 2;
                }

                new newAsync().execute(Urls.BASE_URL + Urls.REGISTER_CUSTOMER, sessionId, userToken,
                        firstName, lastName, phoneNumber, birthDate, String.valueOf(gender), selectedCity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String cityDataBaseChecker(String TableName, String column, String columnName, String amount) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where " + columnName + "=\"" + amount + "\"", null);
        String title = null;
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(column));
            } while (cursor.moveToNext());
        }

        sqld.close();
        return title;
    }

    private void ShortcutIcon(){

        Intent shortcutIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        shortcutIntent.putExtra("value", "hi how are you");
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_address));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}
