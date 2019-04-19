package ir.co.ahs.app.mob.success;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ir.co.ahs.app.mob.success.Helper.DataBaseWriter_Event;
import ir.co.persiancalendar.PersianCalendarView;
import ir.co.persiancalendar.core.G;
import ir.co.persiancalendar.core.PersianCalendarHandler;
import ir.co.persiancalendar.core.interfaces.OnDayClickedListener;
import ir.co.persiancalendar.core.interfaces.OnMonthChangedListener;
import ir.co.persiancalendar.core.models.CalendarEvent;
import ir.co.persiancalendar.core.models.PersianDate;
import ir.pec.mpl.pecpayment.view.PaymentInitiator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalendarActivity extends AppCompatActivity implements FragmentNavigation.OnFragmentInteractionListener, DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    private TextView events;
    private TextView events_header;
    String monthName;
    private FloatingActionButton add_event_fab;
    private Dialog dialog_event;
    private TextView dialog_edt_header;
    private EditText dialog_edt_title;
    private TextView dialog_tv_cancel;
    private TextView dialog_tv_register;
    private LinearLayoutManager localEventManager;
    static Adapter_Recycler localEventAdapter;
    static ArrayList<Struct> localEventList = new ArrayList<>();
    private ArrayList<Struct> officialEventList = new ArrayList<>();
    private ArrayList<String> Dates = new ArrayList<>();
    private static PersianDate persianDate;
    private LinearLayoutManager officialEventManager;
    private Adapter_Recycler officialEventAdapter;
    private DrawerLayout drawer;
    private DatePickerDialog dpd;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd;
    private TextView dialog_tv_time;
    private int intMin = 00;
    private int intHour = 00;
    private Dialog dialog_warning;
    private TextView todayPhrase;
    private CoordinatorLayout coordinatorLayout;
    private TextView phraseOwner;
    private String dayName;
    private Dialog dialog_guid;
    private TextView dialog_guid_exit;
    private TextView dialog_guid_description;
    private TextView dialog_guid_verCode;
    private TextView dialog_guid_verNo;
    private TextView dialog_guid_date;
    private TextView dialog_guid_size;
    private TextView dialog_guid_download;
    private String downloadUrl;
    private String Token;
    private Dialog dialog_event_update;
    private TextView dialog_event_update_edt_header;
    private EditText dialog_event_update_edt_title;
    private TextView dialog_event_update_tv_cancel;
    private TextView dialog_event_update_tv_register;
    private TextView dialog_event_update_tv_time;
    private String EventMonth;
    private String EventDay;
    private String EventTitle;
    private View bottomSheet;
    BottomSheetBehavior behavior;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

//        payment_getToken();

        todayPhrase = (TextView) findViewById(R.id.todayPhrase);
        phraseOwner = (TextView) findViewById(R.id.phraseOwner);
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                CalendarActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(CalendarActivity.this, now.HOUR, now.MINUTE, now.SECOND, true);
        tpd.setVersion(com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2);

        // Dialog
        dialog_guid = new Dialog(CalendarActivity.this);
        dialog_guid.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog_guid.setContentView(R.layout.dialog_update);
//        dialog_guid_title = (TextView) dialog_guid.findViewById(R.id.description);
        dialog_guid_exit = (TextView) dialog_guid.findViewById(R.id.exit);
        dialog_guid_description = (TextView) dialog_guid.findViewById(R.id.description);
        dialog_guid_verCode = (TextView) dialog_guid.findViewById(R.id.verNo);
        dialog_guid_verNo = (TextView) dialog_guid.findViewById(R.id.verCode);
        dialog_guid_date = (TextView) dialog_guid.findViewById(R.id.date);
        dialog_guid_size = (TextView) dialog_guid.findViewById(R.id.size);
        dialog_guid_download = (TextView) dialog_guid.findViewById(R.id.download);
        dialog_guid_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_guid.dismiss();
            }
        });
        dialog_guid_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = downloadUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


//        dpd.show(getFragmentManager(), "Datepickerdialog");

        checkForNewVersion();

        // Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Fragment squadFragment = new FragmentNavigation();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, squadFragment, null);
        fragmentTransaction.commit();


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        ImageView fragmentNews = (ImageView) findViewById(R.id.btnNews);
        ImageView fragmentCart = (ImageView) findViewById(R.id.btnCart);
        ImageView fragmentFal = (ImageView) findViewById(R.id.btnFal);
        ImageView fragmentEnd = (ImageView) findViewById(R.id.btnEnd);

        fragmentNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                CalendarActivity.this.startActivity(intent);
                MainActivity.pageSelector(0);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                },1000);            }
        });
        fragmentCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                CalendarActivity.this.startActivity(intent);
                MainActivity.pageSelector(1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                },1000);            }
        });
        fragmentFal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                CalendarActivity.this.startActivity(intent);
                MainActivity.pageSelector(2);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                },1000);
            }
        });
        fragmentEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                CalendarActivity.this.startActivity(intent);
                MainActivity.pageSelector(3);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                },1000);
            }
        });

        // Dialog
        dialog_warning = new Dialog(CalendarActivity.this);
        dialog_warning.setContentView(R.layout.dialog_warning);
        dialog_tv_register = (TextView) dialog_warning.findViewById(R.id.dialog_tv_register);
        dialog_tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_warning.dismiss();
            }
        });


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (!prefs.getBoolean("firstTimeRunning", false)) {
//            // <---- run your one time code here
//            dialog_warning.show();
//            // mark first time has runned.
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstTimeRunning", true);
//            editor.commit();
//        }

        // Dialog Edit Event
        dialog_event_update = new Dialog(CalendarActivity.this);
        dialog_event_update.setContentView(R.layout.dialog_event_update);
        dialog_event_update_edt_header = (TextView) dialog_event_update.findViewById(R.id.dialog_edt_header);
        dialog_event_update_edt_title = (EditText) dialog_event_update.findViewById(R.id.dialog_edt_title);
        dialog_event_update_tv_cancel = (TextView) dialog_event_update.findViewById(R.id.dialog_tv_cancel);
        dialog_event_update_tv_register = (TextView) dialog_event_update.findViewById(R.id.dialog_tv_register);
        dialog_event_update_tv_time = (TextView) dialog_event_update.findViewById(R.id.time);
        WindowManager.LayoutParams update_lp = new WindowManager.LayoutParams();
        Window update_window = dialog_event_update.getWindow();
        update_lp.copyFrom(update_window.getAttributes());
//This makes the dialog take up the full width
        update_lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        update_lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        update_window.setAttributes(update_lp);

        dialog_event_update_tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_event_update.dismiss();
                String title = dialog_event_update_edt_title.getText().toString();
                String time = dialog_event_update_tv_time.getText().toString();
                databaseUpdate("EVENT_TABLE", title, time, EventMonth, EventDay, EventTitle);
                dialog_event_update_edt_title.setText("");

            }
        });
        dialog_event_update_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_event_update.dismiss();
                dialog_event_update_edt_title.setText("");
            }
        });


        // Dialog Event
        dialog_event = new Dialog(CalendarActivity.this);
        dialog_event.setContentView(R.layout.dialog_event);
        dialog_edt_header = (TextView) dialog_event.findViewById(R.id.dialog_edt_header);
        dialog_edt_title = (EditText) dialog_event.findViewById(R.id.dialog_edt_title);
        dialog_tv_cancel = (TextView) dialog_event.findViewById(R.id.dialog_tv_cancel);
        dialog_tv_register = (TextView) dialog_event.findViewById(R.id.dialog_tv_register);
        dialog_tv_time = (TextView) dialog_event.findViewById(R.id.time);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_event.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog_tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd.show(getFragmentManager(), "TimePickerdialog");
            }
        });

        events_header = (TextView) findViewById(R.id.events_header);
        add_event_fab = (FloatingActionButton) findViewById(R.id.add);
        final PersianCalendarView persianCalendarView = (PersianCalendarView) findViewById(R.id.persian_calendar);
        final PersianCalendarHandler calendar = persianCalendarView.getCalendar();
        final PersianDate today = calendar.getToday();


        dialog_tv_register.setOnClickListener(new View.OnClickListener() {
            int GeorgianYear;
            int GeorgianMonth;
            int GeorgianDay;

            @Override
            public void onClick(View v) {
                dialog_event.dismiss();

                if (persianDate != null) {
                    GeorgianYear = persianDate.getYear();
                    GeorgianMonth = persianDate.getMonth();
                    GeorgianDay = persianDate.getDayOfMonth();
                } else {
                    GeorgianYear = today.getYear();
                    GeorgianMonth = today.getMonth();
                    GeorgianDay = today.getDayOfMonth();
                }
                saman.zamani.persiandate.PersianDate persianDate = new saman.zamani.persiandate.PersianDate();
                int[] date = persianDate.toGregorian(GeorgianYear, GeorgianMonth, GeorgianDay);

                setRecurringAlarm(getApplicationContext(), intHour, intMin, date[1] - 1, date[2], GeorgianMonth - 1, GeorgianDay);


//                setRecurringAlarm(intHour,intMin,Calendar.get);
//                String title = dialog_edt_title.getText().toString();
//                calendar.addLocalEvent(new CalendarEvent(persianDate, title, false));
//                persianCalendarView.update();

                dialog_edt_title.setText("");

            }
        });
        dialog_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_event.dismiss();
                dialog_edt_title.setText("");
            }
        });

        RecyclerView officialEventRecycler = (RecyclerView) findViewById(R.id.officialEventRecycler);
        officialEventManager = new LinearLayoutManager(getApplicationContext());
        officialEventAdapter = new Adapter_Recycler(getApplicationContext(), officialEventList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemDelete(int position) {

            }

            @Override
            public void onCardClick(int position) {


            }

            @Override
            public void onEdit(int position) {

            }
        }, 1, false);
        officialEventRecycler.setAdapter(officialEventAdapter);
        officialEventRecycler.setLayoutManager(officialEventManager);


        RecyclerView localEventRecycler = (RecyclerView) findViewById(R.id.localEventRecycler);
        localEventManager = new LinearLayoutManager(getApplicationContext());
        localEventAdapter = new Adapter_Recycler(getApplicationContext(), localEventList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemDelete(int position) {

            }

            @Override
            public void onCardClick(int position) {
                dialog_event_update.show();
                EventTitle = localEventList.get(position).strItemTitle;
                dialog_event_update_edt_title.setText(localEventList.get(position).strItemTitle);
                dialog_event_update_tv_time.setText(localEventList.get(position).strItemTime);
                EventMonth = localEventList.get(position).strItemMonth;
                EventDay = localEventList.get(position).strItemDay;

            }

            @Override
            public void onEdit(int position) {

            }
        }, 2, false);
        localEventRecycler.setAdapter(localEventAdapter);
        localEventRecycler.setLayoutManager(localEventManager);

        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onChanged(PersianDate date) {
                Toast.makeText(CalendarActivity.this, calendar.getMonthName(date), Toast.LENGTH_SHORT).show();
//                String x = calendar.getMonthName(date);
//                if (calendar.getMonthName(date)==G.monthName){
//                    TextView yearName = (TextView) findViewById(R.id.year_name);
//                    TextView monthName = (TextView) findViewById(R.id.month_name);
//                    monthName.setText(G.date);
//                    yearName.setText(G.georgianDate);
//                }else {
//                    TextView yearName = (TextView) findViewById(R.id.year_name);
//                    TextView monthName = (TextView) findViewById(R.id.month_name);
//                    monthName.setText(calendar.getMonthName(date));
//                    yearName.setText("");
//                }
            }
        });
        persianCalendarView.setOnDayClickedListener(new OnDayClickedListener() {
            @Override
            public void onClick(PersianDate date) {

                persianDate = date;
                officialEventList.clear();
                officialEventAdapter.notifyDataSetChanged();
                localEventList.clear();
                localEventAdapter.notifyDataSetChanged();

                switch (date.getMonth()) {
                    case 1:
                        monthName = "فروردین";
                        break;
                    case 2:
                        monthName = "اردیبهشت";
                        break;
                    case 3:
                        monthName = "خرداد";
                        break;
                    case 4:
                        monthName = "تیر";
                        break;
                    case 5:
                        monthName = "مرداد";
                        break;
                    case 6:
                        monthName = "شهریور";
                        break;
                    case 7:
                        monthName = "مهر";
                        break;
                    case 8:
                        monthName = "آبان";
                        break;
                    case 9:
                        monthName = "آذر";
                        break;
                    case 10:
                        monthName = "دی";
                        break;
                    case 11:
                        monthName = "بهمن";
                        break;
                    case 12:
                        monthName = "اسفند";
                        break;
                }

                switch (date.getDayOfWeek()) {
                    case 5:
                        dayName = "شنبه";
                        break;
                    case 6:
                        dayName = "یکشنبه";
                        break;
                    case 7:
                        dayName = "دوشنبه";
                        break;
                    case 1:
                        dayName = "سه شنبه";
                        break;
                    case 2:
                        dayName = "چهارشنبه";
                        break;
                    case 3:
                        dayName = "پنج شنبه";
                        break;
                    case 4:
                        dayName = "جمعه";
                        break;

                }

//                events_header.setText(dayName +" " + String.valueOf(date.getDayOfMonth()) + " " + monthName);
                events_header.setText(" " + String.valueOf(date.getDayOfMonth()) + " " + monthName);

                for (CalendarEvent e : calendar.getOfficialEventsForDay(date)) {
                    Struct struct = new Struct();
                    struct.strItemTitle = e.getTitle();
                    struct.strItemDate = String.valueOf(String.valueOf(e.getDate().getYear() + "/" + String.valueOf(e.getDate().getMonth()) + "/" + e.getDate().getDayOfMonth()));
                    officialEventList.add(struct);
                }
                officialEventAdapter.notifyDataSetChanged();


//                for (int i = 0; i < calendar.getLocalEventsForDay(date).size(); i++) {
//                    Struct struct = new Struct();
//                    struct.strItemTitle = calendar.getLocalEventsForDay(date).get(i).getTitle();
//                    localEventList.add(struct);
//                }
//                localEventAdapter.notifyDataSetChanged();

                recyclerInitializer("EVENT_TABLE", date.getMonth(), date.getDayOfMonth());

//                calendar.addLocalEvent(new CalendarEvent(today.clone().rollDay(2, false), "Some event that will be added in runtime", false));
//                persianCalendarView.update();
            }
        });
        add_event_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_event.show();
            }
        });

        calendar.setHighlightOfficialEvents(false);
        TextView txtDayMonth = (TextView) findViewById(R.id.txt_day_month);
        TextView txtYear = (TextView) findViewById(R.id.txt_year);

        String dayAndMonth = calendar.getWeekDayName(today) + calendar.formatNumber(today.getDayOfMonth()) + calendar.getMonthName(today);
        txtDayMonth.setText(dayAndMonth);
        txtYear.setText(calendar.formatNumber(today.getYear()));

        calendar.setColorBackground(getResources().getColor(R.color.Red));
        try {
            persianCalendarView.update();
        } catch (Exception e) {

        }
        String savedOwner = G.PHRASE_OWNER.getString("PHRASE_OWNER", "لوئيز ال.هی");
        String savedPhrase = G.dayPhrase.getString("dayPhrase", "احساس ارزشمندی و عزت نفس ، مهمترين داشته های يک زن به شمار می آيند");
        phraseOwner.setText(savedOwner);
        todayPhrase.setText(savedPhrase);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = 168 * (metrics.densityDpi / 160f);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight((int) px);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


        CardView bottomNav = (CardView) findViewById(R.id.bottom_nav);
        bottomNav.bringToFront();

        TextView yearName = (TextView) findViewById(R.id.year_name);
        TextView monthName = (TextView) findViewById(R.id.month_name);
        monthName.setText(G.date);
        yearName.setText(G.georgianDate);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = "You picked the following time: " + hourOfDay + "h" + minute + "m" + second;
        dialog_tv_time.setText(hourOfDay + ":" + minute);
//        timeTextView.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//        dateTextView.setText(date);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog, int i, int i1, int i2) {
        dialog_tv_time.setText(i + " : " + i1);
        intHour = i;
        intMin = i1;

    }


//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void setRecurringAlarm(Context context, int Hour, int Min, int Month, int Day, int persianMonth, int persianDay) {
//
//        Calendar now = Calendar.getInstance();
//        Calendar updateTime = (Calendar) now.clone();
//        updateTime.setTimeZone(TimeZone.getTimeZone("GMT+3:30"));
//        updateTime.set(Calendar.MONTH, Month);
//        updateTime.set(Calendar.DAY_OF_MONTH, Day);
//        updateTime.set(Calendar.HOUR_OF_DAY, Hour);
//        updateTime.set(Calendar.MINUTE, Min);
//
//
//        Intent intent = new Intent(context, Tasks.class);
//        intent.putExtra("event", dialog_edt_title.getText().toString());
//
//        PendingIntent recurringDownload = PendingIntent.getBroadcast(context, 1, intent, 0);
//
//        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarms.set(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), recurringDownload);
//
////
////        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
////            // Do something for lollipop and above versions
////        } else {
////            alarms.getNextAlarmClock();
////        }
//
//        addToDatabase("EVENT_TABLE", persianMonth, persianDay, Hour + " : " + Min, dialog_edt_title.getText().toString());
//
//    }

    private void setRecurringAlarm(Context context, int Hour, int Min, int Month, int Day, int persianMonth, int persianDay) {
//        Calendar cal=Calendar.getInstance();
//        cal.set(Calendar.MONTH,5);
//        cal.set(Calendar.YEAR,2018);
//        cal.set(Calendar.DAY_OF_MONTH,29);
//        cal.set(Calendar.HOUR_OF_DAY,12);
//        cal.set(Calendar.MINUTE,57);
//
//        Calendar now=Calendar.getInstance();
//        now.get(Calendar.MONTH);
//        now.get(Calendar.YEAR);
//        now.get(Calendar.DAY_OF_MONTH);
//        now.get(Calendar.HOUR_OF_DAY);
//        now.get(Calendar.MINUTE);

        Calendar set = Calendar.getInstance();
        set.set(Calendar.MONTH, Month);
        set.set(Calendar.YEAR, 2018);
        set.set(Calendar.DAY_OF_MONTH, Day);
        set.set(Calendar.HOUR_OF_DAY, Hour);
        set.set(Calendar.MINUTE, Min);
        set.set(Calendar.SECOND, 00);


        //String[] dude=new String[] {"nitin","avi","aman","rahul","pattrick","minkle","manmohan","nitin","nitin"};

        //setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,dude));
        //getListView().setTextFilterEnabled(true);

        //String[] dude1=new String[] {"nitin","avi","aman","rahul","pattrick","minkle","manmohan"};

        Intent intent = new Intent(this, Tasks.class);
        intent.putExtra("event", dialog_edt_title.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, set.getTimeInMillis(), pendingIntent);
//        Toast.makeText(this, "Alarm worked.", Toast.LENGTH_LONG).show();
        addToDatabase("EVENT_TABLE", persianMonth, persianDay, Hour + " : " + Min, dialog_edt_title.getText().toString());


    }

    private static void addToDatabase(String TableName, int MONTH, int DAY, String TIME, String TOPIC) {
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "EVENT_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    String.valueOf(MONTH),
                    String.valueOf(DAY),
                    TIME,
                    TOPIC
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerInitializer("EVENT_TABLE", MONTH, DAY);

    }

    private static void DataBaseChecker(String TableName, int monthNumber, int dayNumber) {
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "EVENT_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();
        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"" + " and" + " DAY = " + dayNumber + "\"", null);
        String month;
        String day;
        String time;
        String topic;
        if (cursor.moveToFirst()) {
            do {
                month = cursor.getString(cursor.getColumnIndex("MONTH"));
                day = cursor.getString(cursor.getColumnIndex("DAY"));
                time = cursor.getString(cursor.getColumnIndex("TIME"));
                topic = cursor.getString(cursor.getColumnIndex("TOPIC"));

            } while (cursor.moveToNext());
        }
        sqld.close();
//        getProducts(groupId);

    }

    private static void DataBaseItemDelete(String TableName, int quantity, String id) {
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "EVENT_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TableName, "ID =" + "\"" + id + "\"", null);
//        db.close();
        db.close();
    }

    private static void recyclerInitializer(String TableName, int monthNumber, int dayNumber) {
        localEventList.clear();
        try {
            localEventAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "EVENT_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"" + " and" + " DAY = \"" + dayNumber + "\"", null);
        String month;
        String day;
        String time;
        String topic;
        if (cursor.moveToFirst()) {
            do {
                month = cursor.getString(cursor.getColumnIndex("MONTH"));
                day = cursor.getString(cursor.getColumnIndex("DAY"));
                time = cursor.getString(cursor.getColumnIndex("TIME"));
                topic = cursor.getString(cursor.getColumnIndex("TOPIC"));


                Struct struct = new Struct();
                struct.strItemTitle = topic;
                struct.strItemDate = month + " : " + day;
                struct.strItemTime = time;
                struct.strItemMonth = month;
                struct.strItemDay = day;

                localEventList.add(struct);
            } while (cursor.moveToNext());
            try {
                localEventAdapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
        sqld.close();
    }

    private static void databaseUpdate(String TableName, String title, String time, String month, String day, String lastTitle) {
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "EVENT_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

//        ContentValues cv = new ContentValues();
//        cv.put("MONTH", month); //These Fields should be your String values of actual column names
//        cv.put("DAY", day);
//        cv.put("TIME", time);
//        cv.put("TOPIC", title);

//        sqld.update(TableName, cv, "DAY=\"" + month + "\" and " + "TIME=\"" + time + "\"", null);

        String strSQL = "UPDATE " + TableName + " SET" + " MONTH = \"" + month + "\"" + ", DAY = \"" + day + "\"" + ", TIME = \"" + time + "\"" + ", TOPIC = \"" + title + "\"" + " where MONTH = \"" + month + "\"" + " and" + " TIME = \"" + time + "\"";

        sqld.execSQL(strSQL);


//        Cursor cursor = sqld.rawQuery("UPDATE " + TableName + " SET" + " MONTH = \"" + month + "\"" + " DAY = \"" + day + "\"" + " TIME = \"" + time + "\"" + " TOPIC = \"" + title + "\"" + " where MONTH = \"" + month + "\"" + " and" + " TIME = \"" + time + "\"", null);
//        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"" + " and" + " DAY = \"" + dayNumber + "\"", null);
//        String month;
//        String day;
//        String time;
//        String topic;
//        if (cursor.moveToFirst()) {
//            do {
//                month = cursor.getString(cursor.getColumnIndex("MONTH"));
//                day = cursor.getString(cursor.getColumnIndex("DAY"));
//                time = cursor.getString(cursor.getColumnIndex("TIME"));
//                topic = cursor.getString(cursor.getColumnIndex("TOPIC"));
//
//
//                Struct struct = new Struct();
//                struct.strItemTitle = topic;
//                struct.strItemDate = month + " : " + day;
//                struct.strItemTime = time;
//
//                localEventList.add(struct);
//            } while (cursor.moveToNext());
//            try {
//                localEventAdapter.notifyDataSetChanged();
//            } catch (Exception e) {
//            }
//        }
        sqld.close();
        recyclerInitializer("EVENT_TABLE", persianDate.getMonth(), persianDate.getDayOfMonth());

    }

    private void checkForNewVersion() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        Log.d("session", sessionId);
        Log.d("token", userToken);
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");

        new getUpdate().execute(Urls.BASE_URL + Urls.HasNewVersionReleased, sessionId, userToken, G.APP_VERSION, String.valueOf(G.APP_VERSION_CODE), "1");
    }

    private class getUpdate extends Webservice.getUpdates {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {
            String x = result;

            try {
                JSONObject data = new JSONObject(result);
                Boolean hasNewVersionReleased = data.getBoolean("hasNewVersionReleased");

                if (hasNewVersionReleased) {


                    String versionName = data.getString("versionName");
                    String versionCode = data.getString("versionCode");
                    String releaseDate = data.getString("releaseDate");
                    downloadUrl = data.getString("downloadUrl");
                    String description = data.getString("description");
                    String appFileSize = data.getString("appFileSize");

                    dialog_guid_description.setText(description);
                    dialog_guid_verNo.setText(versionName);
                    dialog_guid_verCode.setText(versionCode);
                    dialog_guid_date.setText(releaseDate);
                    dialog_guid_size.setText(appFileSize);

                    dialog_guid.show();

                } else {

                }


            } catch (JSONException e) {


            }

        }

    }

    public void payment_getToken() {

        int Amount = 10000;
        long MobileNo = 9355890011L;
        int PaymentType = 1;
        long OrderId = 9084908902849023L;
        boolean Force4Factor = false;


        String appVersion = "1.0.0";
        String authorization = "Basic TW92YWZhZ2h5YVQ6VktoRjZNSTZYTnkxMnljRzM0MHY=";
//        new getOmen().execute(Urls.BASE_URL + Urls.GET_MONTH_OMEN, sessionId, userToken, String.valueOf(Amount), String.valueOf(MobileNo), String.valueOf(PaymentType), String.valueOf(OrderId), String.valueOf(Force4Factor));
        new getToken().execute("https://testmpl.pec.ir/api/EShop/GetSaleTokenNew", appVersion, authorization, String.valueOf(Amount), String.valueOf(MobileNo), String.valueOf(PaymentType), String.valueOf(OrderId), String.valueOf(Force4Factor));
    }

    private class getToken extends Webservice.getTokenPayment {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject data = jsonObject.getJSONObject("Data");
                Token = data.getString("Token");
                String ExpireDate = data.getString("ExpireDate");
                String Message = data.getString("Message");
                Toast.makeText(CalendarActivity.this, Message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CalendarActivity.this, PaymentInitiator.class);
                intent.putExtra("Type", "1");
                intent.putExtra("Token", Token);
                intent.putExtra("OrderID", 123);
                startActivityForResult(intent, 0);

            } catch (Exception e) {
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                data.getStringExtra("enData");
                data.getStringExtra("message");
                String.valueOf(data.getIntExtra("status", 0));
                break;
            case 2:
                data.getIntExtra(" errorType ", 0);
                String.valueOf(data.getIntExtra("OrderID", 0));
                break;
            case 3:
                data.getStringExtra("enData");
                data.getStringExtra("message ");
                String.valueOf(data.getIntExtra("status", 0));
                break;
            case 4:
                String.valueOf(data.getIntExtra("errorType", 0));
                break;
            case 5:
                String.valueOf(data.getIntExtra("errorType", 0));
                String.valueOf(data.getIntExtra("OrderID", 0));
                break;
            case 6:
                String.valueOf(data.getIntExtra("errorType", 0));
                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;


        }
    }
}
