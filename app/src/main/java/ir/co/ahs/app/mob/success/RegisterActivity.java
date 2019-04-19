package ir.co.ahs.app.mob.success;

import android.Manifest;
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
import android.support.v13.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.onesignal.OneSignal;
import com.shawnlin.numberpicker.NumberPicker;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ir.co.ahs.app.mob.success.Helper.DataBaseHelper;
import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class RegisterActivity extends Activity {
    private static Dialog dialogVerify;
    private EditText activityRegister_firstName;
    private EditText activityRegister_lastName;
    private EditText activityRegister_phone;
    private Boolean fieldsAreFilled = false;
    private TextView dialogVerify_txt_0;
    private static TextView dialogVerify_txt_1;
    private TextView dialogVerify_txt_counter;
    private static EditText dialogVerify_edt_code;
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
    private MaterialSpinner citySpinner;
    private MaterialSpinner stateSpinner;
    private Boolean isFirstTime = true;
    private String selectedCity = "8";
    private static Dialog dialogSuccess;
    private static TextView dialogSuccess_txt_memberCode;
    private TextView dialogSuccess_btn_register;
    private String oneSignalUserID;
    private String oneSignalRegistrationID;
    private static String customerId;
    private static String memberCode;
    private CountDownTimer countDownTimer;
    private EditText activityRegister_email;
    private TextView dialogVerify_btn_resend;
    private AVLoadingIndicatorView loading;
    public static RegisterActivity activity;
    private static final int REQUEST_CODE_PERMISSION = 2;

    private String[] mPermission;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPermission = new String[]{android.Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION,};


        activity = RegisterActivity.this;
        // Views
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);

        activityRegister_logo = (LinearLayout) findViewById(R.id.logo);
        activityRegister_firstName = (EditText) findViewById(R.id.first_name);
        activityRegister_lastName = (EditText) findViewById(R.id.last_name);
        activityRegister_phone = (EditText) findViewById(R.id.phone);
        activityRegister_birth = (EditText) findViewById(R.id.birth);
        activityRegister_email = (EditText) findViewById(R.id.edt_email);
        activityRegister_birth.setText("1369/11/23");
        activityRegister_rb_male = (RadioButton) findViewById(R.id.male);
        activityRegister_rb_female = (RadioButton) findViewById(R.id.female);
        activityRegister_btn_register = (TextView) findViewById(R.id.btn_register);
        stateSpinner = (MaterialSpinner) findViewById(R.id.state_spinner);
        citySpinner = (MaterialSpinner) findViewById(R.id.city_spinner);
        stateSpinner.setVerticalScrollBarEnabled(true);
        activityRegister_rb_male.setChecked(true);
        stateSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstTime) {
                    stateDataBaseChecker("srProvince", "proname");
                    isFirstTime = false;
                }
            }
        });

        stateSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                String provinceID = cityDataBaseChecker("srProvince", "_id", "proname", item);
                cityDataBaseChecker("srCityBig", "citname", "citproidint", provinceID);
                citySpinner.setItems(cityName);
            }
        });
        citySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selectedCity = cityDataBaseCheck("srCityBig", "_id", "citname", item);
            }
        });


        // Dialog
        dialogVerify = new Dialog(RegisterActivity.this);
        dialogVerify.setContentView(R.layout.dialog_verify);
        dialogVerify.setTitle("Custom Dialog");
        dialogVerify.setCancelable(false);
        dialogVerify_txt_0 = (TextView) dialogVerify.findViewById(R.id.dialog_txt);
        dialogVerify_txt_1 = (TextView) dialogVerify.findViewById(R.id.dialog_txt1);
        dialogVerify_txt_counter = (TextView) dialogVerify.findViewById(R.id.dialog_counter);
        dialogVerify_edt_code = (EditText) dialogVerify.findViewById(R.id.dialog_edt_txt);
        dialogVerify_btn_cancel = (TextView) dialogVerify.findViewById(R.id.cancel);
        dialogVerify_btn_register = (TextView) dialogVerify.findViewById(R.id.register);
        dialogVerify_btn_resend = (TextView) dialogVerify.findViewById(R.id.resend);
        countDownTimer = new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                dialogVerify_txt_counter.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//                dialogVerify_txt_counter.setText(String.valueOf("00:" + millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                dialogVerify_txt_counter.setText("00:00");
                dialogVerify_txt_counter.setVisibility(View.GONE);
                dialogVerify_btn_resend.setVisibility(View.VISIBLE);
            }

        };
        dialogVerify_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogVerify_edt_code.getText().toString().length() < 4) {
                    dialogVerify_txt_1.setVisibility(View.VISIBLE);
                } else {

                    String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
                    String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
                    String phone = G.CUSTOMER_PHONE.getString("CUSTOMER_PHONE", "empty");
                    String code = dialogVerify_edt_code.getText().toString();


                    new verifyAsync().execute(Urls.BASE_URL + Urls.VERIFY_CUSTOMER, sessionId, userToken, customerId, phone, code);
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.putExtra("name", activityRegister_firstName.getText().toString() + " " + activityRegister_lastName.getText().toString());
//                    RegisterActivity.this.startActivity(intent);
//                    finish();
                }

            }
        });
        dialogVerify_btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        dialogVerify_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogVerify.dismiss();
            }
        });

        // Dialog
        dialogSuccess = new Dialog(RegisterActivity.this);
        dialogSuccess.setContentView(R.layout.dialog_credit_code);
        dialogSuccess.setCancelable(false);
//        dialogSuccess.setTitle("Custom Dialog");
        dialogSuccess_txt_memberCode = (TextView) dialogSuccess.findViewById(R.id.memberCode);
        dialogSuccess_btn_register = (TextView) dialogSuccess.findViewById(R.id.register);
        dialogSuccess_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();

            }
        });


        // Dialog
        dialogDateSelector = new Dialog(RegisterActivity.this);
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
                fieldChecker();
                if (fieldsAreFilled) {
                    try {
                        if (ActivityCompat.checkSelfPermission(RegisterActivity.this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(RegisterActivity.this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(RegisterActivity.this, mPermission[2]) != MockPackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, mPermission, REQUEST_CODE_PERMISSION);

                            // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                        } else {
                            runSessionService();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    String firstName = activityRegister_firstName.getText().toString();
//                    String lastName = activityRegister_lastName.getText().toString();
//                    String phoneNumber = activityRegister_phone.getText().toString();
//                    String birthDate = activityRegister_birth.getText().toString();
//                    int gender = 1;
//                    if (activityRegister_rb_male.isChecked()) {
//                        gender = 1;
//                    } else if (activityRegister_rb_female.isChecked()) {
//                        gender = 2;
//                    }
//
//                    String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
//                    String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
//                    new newAsync().execute("http://mcapi.ahscoltd.ir/mcapi/RegisterCustomer", sessionId, userToken,
//                            firstName, lastName, phoneNumber, birthDate, String.valueOf(gender), selectedCity);
//                    dialogVerify.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(intent);
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

            String mail = activityRegister_email.getText().toString();
//            if (activityRegister_email.getText().toString().length() < 2) {
//                Random r = new Random();
//                int i1 = r.nextInt(100000 - 1) + 1;
//                mail = String.valueOf("mail place Holder " + i1);
//            }

            new newAsync().execute(Urls.BASE_URL + Urls.REGISTER_CUSTOMER, sessionId, userToken, firstName, lastName, phoneNumber, birthDate, String.valueOf(gender), selectedCity, mail);

        }
    }

    private void fieldChecker() {
        checker(activityRegister_firstName, 3, "نام");
        checker(activityRegister_lastName, 3, "نام خانوادگی");
        checker(activityRegister_phone, 11, "شماره موبایل");
        checker(activityRegister_birth, 6, "تاریخ تولد");
        spinnerChecker(stateSpinner, citySpinner);

        if (!checker(activityRegister_firstName, 3, "نام") ||
                !checker(activityRegister_lastName, 3, "نام خانوادگی") ||
                !checker(activityRegister_phone, 11, "شماره موبایل") ||
                !checker(activityRegister_birth, 6, "تاریخ تولد") ||
                !spinnerChecker(stateSpinner, citySpinner)) {
            fieldsAreFilled = false;
        } else {
            fieldsAreFilled = true;

        }
    }

    private boolean checker(final EditText editText, int limit, String text) {
        Boolean result = true;
        final String typedText = editText.getText().toString();
        if (editText.getText().toString().length() == 0) {
            editText.setText("");
            editText.setHint(text + " خود را وارد کنید");
            editText.setHintTextColor(getResources().getColor(R.color.white));
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
            editText.setHintTextColor(getResources().getColor(R.color.white));
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
                editText.setHintTextColor(getResources().getColor(R.color.white));
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stateSpinner.setItems(stateName);
                cityDataBaseChecker("srCityBig", "citname", "citproidint", String.valueOf(8));
                citySpinner.setItems(cityName);

            }
        }, 500);
        sqld.close();
        return title;
    }

    private String cityDataBaseChecker(String TableName, String column, String columnName, String amount) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where " + columnName + "=\"" + amount + "\" order by " + column, null);
        String title = null;
        cityName.clear();
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(column));
                cityName.add(title);
            } while (cursor.moveToNext());
        }

        sqld.close();
        return title;
    }

    private String cityDataBaseCheck(String TableName, String column, String columnName, String amount) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where " + columnName + "=\"" + amount + "\" order by " + column, null);
        String title = null;
//        cityName.clear();
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(column));
//                cityName.add(title);
            } while (cursor.moveToNext());
        }

        sqld.close();
        return title;
    }

    private class newAsync extends Webservice.postClass {
        @Override
        protected void onPreExecute() {
            activityRegister_btn_register.setVisibility(View.GONE);
            loading.smoothToShow();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            loading.smoothToHide();
            activityRegister_btn_register.setVisibility(View.VISIBLE);

            try {
                JSONObject mainObject = new JSONObject(result);
                customerId = mainObject.getString("customerId");
                String customerName = mainObject.getString("firstName") + " " + mainObject.getString("lastName");
                memberCode = mainObject.getString("memberCode");
                String customerPhone = mainObject.getString("cellPhone");
                String customerAddress = mainObject.getString("address");
                String customerBirth = mainObject.getString("birthDate");
                String email = mainObject.getString("email");
                Boolean activeBySms = mainObject.getBoolean("activateBySMS");
                int customerCity = mainObject.getInt("cityId");
                int customerGender = mainObject.getInt("gender");
                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();
                G.CUSTOMER_ID.edit().putString("CUSTOMER_ID", customerId).apply();
                G.CUSTOMER_NAME.edit().putString("CUSTOMER_NAME", customerName).apply();
                G.CUSTOMER_PHONE.edit().putString("CUSTOMER_PHONE", customerPhone).apply();
                G.CUSTOMER_GENDER.edit().putInt("CUSTOMER_GENDER", customerGender).apply();
                G.CUSTOMER_ADDRESS.edit().putString("CUSTOMER_ADDRESS", customerAddress).apply();
                G.CUSTOMER_CITY.edit().putInt("CUSTOMER_CITY", customerCity).apply();
                G.CUSTOMER_EMAIL.edit().putString("CUSTOMER_EMAIL", email).apply();
                G.CUSTOMER_BIRTH.edit().putString("CUSTOMER_BIRTH", customerBirth).apply();
                G.MEMBER_CODE.edit().putString("MEMBER_CODE", memberCode).apply();

                if (activeBySms) {
                    dialogVerify_txt_counter.setVisibility(View.VISIBLE);
                    dialogVerify_btn_resend.setVisibility(View.GONE);

                    countDownTimer.start();
                    dialogVerify.show();
                } else {
                    dialogSuccess_txt_memberCode.setText(memberCode);
                    dialogSuccess.show();
                }
//                Log.d("request result", sessionId);
//                Log.d("request result", userToken);

            } catch (JSONException e) {
//                Log.d("error", "Response: " + result);
//                Log.d("error", "Response: " + e);
//
//
//                try {
//                    JSONObject object = new JSONObject(result);
//                    Toast.makeText(RegisterActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
//
//                } catch (JSONException e1) {
//
//                    Toast.makeText(RegisterActivity.this,"خطا در روند ثبت نام.", Toast.LENGTH_SHORT).show();
//
//                }
                // TODO : نشان دادن پیغام خطا

            }

        }
    }

    private class AsyncSesssion extends Webservice.sessionConnection {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
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
                String mail = activityRegister_email.getText().toString();
                if (activityRegister_email.getText().toString().length() < 2) {
                    mail = "placeHolderMail";
                }
                int gender = 1;
                if (activityRegister_rb_male.isChecked()) {
                    gender = 1;
                } else if (activityRegister_rb_female.isChecked()) {
                    gender = 2;
                }

                new newAsync().execute(Urls.BASE_URL + Urls.REGISTER_CUSTOMER, sessionId, userToken, firstName, lastName, phoneNumber, birthDate, String.valueOf(gender), selectedCity, mail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class verifyAsync extends Webservice.verifyClass {
        @Override
        protected void onPreExecute() {
            dialogVerify.dismiss();
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                Boolean verifyResult = jsonObject.getBoolean("isActivationCodeValid");
                if (verifyResult) {
                    dialogSuccess_txt_memberCode.setText(memberCode);
                    dialogSuccess.show();
                } else {
                    Toast.makeText(G.CONTEXT, "خطا در روند ثبت نام", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequest() {

        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        String cellPhone = G.CUSTOMER_PHONE.getString("CUSTOMER_PHONE", "x");

        new resendAsync().execute(Urls.BASE_URL + Urls.RESEND_CODE, sessionId, userToken, customerId, cellPhone);


    }

    private class resendAsync extends Webservice.resend {
        @Override
        protected void onPreExecute() {
            activityRegister_btn_register.setVisibility(View.GONE);
            loading.smoothToShow();
            loading.setVisibility(View.VISIBLE);

            dialogVerify_txt_counter.setVisibility(View.VISIBLE);
            dialogVerify_btn_resend.setVisibility(View.GONE);
            countDownTimer.start();

        }

        @Override
        protected void onPostExecute(final String result) {
            loading.smoothToHide();
            activityRegister_btn_register.setVisibility(View.VISIBLE);

            try {
                JSONObject mainObject = new JSONObject(result);
                customerId = mainObject.getString("customerId");
                String customerName = mainObject.getString("firstName") + " " + mainObject.getString("lastName");
                memberCode = mainObject.getString("memberCode");
                String customerPhone = mainObject.getString("cellPhone");
                String customerAddress = mainObject.getString("address");
                String customerBirth = mainObject.getString("birthDate");
                String email = mainObject.getString("email");
                Boolean activeBySms = mainObject.getBoolean("activateBySMS");
                int customerCity = mainObject.getInt("cityId");
                int customerGender = mainObject.getInt("gender");
                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", true).apply();
                G.CUSTOMER_ID.edit().putString("CUSTOMER_ID", customerId).apply();
                G.CUSTOMER_NAME.edit().putString("CUSTOMER_NAME", customerName).apply();
                G.CUSTOMER_PHONE.edit().putString("CUSTOMER_PHONE", customerPhone).apply();
                G.CUSTOMER_GENDER.edit().putInt("CUSTOMER_GENDER", customerGender).apply();
                G.CUSTOMER_ADDRESS.edit().putString("CUSTOMER_ADDRESS", customerAddress).apply();
                G.CUSTOMER_CITY.edit().putInt("CUSTOMER_CITY", customerCity).apply();
                G.CUSTOMER_EMAIL.edit().putString("CUSTOMER_EMAIL", email).apply();
                G.CUSTOMER_BIRTH.edit().putString("CUSTOMER_BIRTH", customerBirth).apply();

            } catch (JSONException e) {

                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                // TODO : نشان دادن پیغام خطا

            }

        }
    }

    public static void showToast(final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                JSONObject object = null;
                try {
                    object = new JSONObject(message);
                    String result = object.getString("Message");
                    Toast.makeText(G.CONTEXT, result, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {


                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 4 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED) {

                // Success Stuff here

            }
        }

    }
    public static void verificationCodeSetter(String string){
        dialogVerify_edt_code.setText("");
        dialogVerify_edt_code.setText(string);

        if (dialogVerify_edt_code.getText().toString().length() < 4) {
            dialogVerify_txt_1.setVisibility(View.VISIBLE);
        } else {

            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
            String phone = G.CUSTOMER_PHONE.getString("CUSTOMER_PHONE", "empty");
            String code = dialogVerify_edt_code.getText().toString();


            new verifyAsync().execute(Urls.BASE_URL + Urls.VERIFY_CUSTOMER, sessionId, userToken, customerId, phone, code);
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.putExtra("name", activityRegister_firstName.getText().toString() + " " + activityRegister_lastName.getText().toString());
//                    RegisterActivity.this.startActivity(intent);
//                    finish();
        }

    }
}
