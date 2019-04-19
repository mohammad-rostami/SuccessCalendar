package ir.co.ahs.app.mob.success;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ir.co.persiancalendar.core.G;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static ir.co.ahs.app.mob.success.ExampleAppWidgetProvider.strMonth;
import static ir.co.ahs.app.mob.success.ExampleAppWidgetProvider.strWeekDay;

public class NotificationService extends Service {

    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    private static String strMonthGeorgian;
    private static String day;
    private NotificationManager notificationManager;
    private int smallIcon;
    BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        new getPhrase().execute(Urls.BASE_URL + Urls.GET_TODAY_PHRASE, sessionId, userToken);
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CreateNotification(String test) {
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, getCurrentShamsidate());
        contentView.setTextViewText(R.id.text, test);
        G.date = getCurrentShamsidate();
//        G.dayPhrase = test;

        switch (Integer.parseInt(day)) {
            case 0:
                smallIcon = R.drawable.ic_1;
                break;
            case 1:
                smallIcon = R.drawable.ic_1;
                break;
            case 2:
                smallIcon = R.drawable.ic_2;
                break;
            case 3:
                smallIcon = R.drawable.ic_3;
                break;
            case 4:
                smallIcon = R.drawable.ic_4;
                break;
            case 5:
                smallIcon = R.drawable.ic_5;
                break;
            case 6:
                smallIcon = R.drawable.ic_6;
                break;
            case 7:
                smallIcon = R.drawable.ic_7;
                break;
            case 8:
                smallIcon = R.drawable.ic_8;
                break;
            case 9:
                smallIcon = R.drawable.ic_9;
                break;
            case 10:
                smallIcon = R.drawable.ic_10;
                break;
            case 11:
                smallIcon = R.drawable.ic_11;
                break;
            case 12:
                smallIcon = R.drawable.ic_12;
                break;
            case 13:
                smallIcon = R.drawable.ic_13;
                break;
            case 14:
                smallIcon = R.drawable.ic_14;
                break;
            case 15:
                smallIcon = R.drawable.ic_15;
                break;
            case 16:
                smallIcon = R.drawable.ic_16;
                break;
            case 17:
                smallIcon = R.drawable.ic_17;
                break;
            case 18:
                smallIcon = R.drawable.ic_18;
                break;
            case 19:
                smallIcon = R.drawable.ic_19;
                break;
            case 20:
                smallIcon = R.drawable.ic_20;
                break;
            case 21:
                smallIcon = R.drawable.ic_21;
                break;
            case 22:
                smallIcon = R.drawable.ic_22;
                break;
            case 23:
                smallIcon = R.drawable.ic_23;
                break;
            case 24:
                smallIcon = R.drawable.ic_24;
                break;
            case 25:
                smallIcon = R.drawable.ic_25;
                break;
            case 26:
                smallIcon = R.drawable.ic_26;
                break;
            case 27:
                smallIcon = R.drawable.ic_27;
                break;
            case 28:
                smallIcon = R.drawable.ic_28;
                break;
            case 29:
                smallIcon = R.drawable.ic_29;
                break;
            case 30:
                smallIcon = R.drawable.ic_30;
                break;
            case 31:
                smallIcon = R.drawable.ic_31;
                break;

        }

        Intent notificationIntent = new Intent(this, CalendarActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContent(contentView)
                .setSmallIcon(smallIcon)
                .setOngoing(true)
                .setContentIntent(intent)
                .build();

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
//        Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case if services are bound (Bound Services).
        return null;
    }

    private static String getGeorgianDate() {
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

        return String.valueOf(dayofmonth) + " " + strMonthGeorgian + " " + String.valueOf(year);

    }

    private static class SolarCalendar {


        int date;
        int month;
        int year;

        public SolarCalendar() {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }

        public SolarCalendar(Date MiladiDate) {
            calcSolarCalendar(MiladiDate);
        }

        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = "فروردين";
                    break;
                case 2:
                    strMonth = "ارديبهشت";
                    break;
                case 3:
                    strMonth = "خرداد";
                    break;
                case 4:
                    strMonth = "تير";
                    break;
                case 5:
                    strMonth = "مرداد";
                    break;
                case 6:
                    strMonth = "شهريور";
                    break;
                case 7:
                    strMonth = "مهر";
                    break;
                case 8:
                    strMonth = "آبان";
                    break;
                case 9:
                    strMonth = "آذر";
                    break;
                case 10:
                    strMonth = "دي";
                    break;
                case 11:
                    strMonth = "بهمن";
                    break;
                case 12:
                    strMonth = "اسفند";
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = "يکشنبه";
                    break;
                case 1:
                    strWeekDay = "دوشنبه";
                    break;
                case 2:
                    strWeekDay = "سه شنبه";
                    break;
                case 3:
                    strWeekDay = "چهارشنبه";
                    break;
                case 4:
                    strWeekDay = "پنج شنبه";
                    break;
                case 5:
                    strWeekDay = "جمعه";
                    break;
                case 6:
                    strWeekDay = "شنبه";
                    break;
            }
            G.month = month;
            G.year = year;

        }

    }

    public static String getCurrentShamsidate() {
        Locale loc = new Locale("en_US");
//        Utilities util = new Utilities();
        NotificationService.SolarCalendar sc = new NotificationService.SolarCalendar();
        day = String.format(loc, "%02d", sc.date);
        G.monthName = strMonth;
        return strWeekDay + " " + String.format(loc, "%02d", sc.date) + " " + strMonth + " " + String.valueOf(sc.year);
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private class getPhrase extends Webservice.getInfo {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
            try {
                JSONObject jsonObject = new JSONObject(result);
                String phrase = jsonObject.getString("phraseTitle");
                String phraseOwner = jsonObject.getString("phraseOwner");
                G.dayPhrase.edit().putString("dayPhrase", phrase).apply();
                G.PHRASE_OWNER.edit().putString("PHRASE_OWNER", phraseOwner).apply();
                CreateNotification(phrase);
                if (phrase.equals("")) {
//                    G.PHRASE_OWNER = "لوئيز ال.هی";
                    String savedPhrase = G.CUSTOMER_NAME.getString("dayPhrase", "احساس ارزشمندی و عزت نفس ، مهمترين داشته های يک زن به شمار می آيند");
                    CreateNotification(savedPhrase);
                }

            } catch (JSONException e) {
//                G.PHRASE_OWNER = "لوئيز ال.هی";
                String savedPhrase = G.CUSTOMER_NAME.getString("dayPhrase", "احساس ارزشمندی و عزت نفس ، مهمترين داشته های يک زن به شمار می آيند");
                CreateNotification(savedPhrase);
            }

        }
    }


    // use this as an inner class like here or as a top-level class

}