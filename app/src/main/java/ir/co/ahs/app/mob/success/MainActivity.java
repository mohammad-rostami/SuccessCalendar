package ir.co.ahs.app.mob.success;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.ArrayList;

import ir.co.persiancalendar.core.models.PersianDate;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements FragmentNavigation.OnFragmentInteractionListener, FalFragment.OnFragmentInteractionListener, StoreFragment.OnFragmentInteractionListener,
        EndFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener {

    private String[] mStr_TabNames;
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
    private Adapter_Recycler localEventAdapter;
    private ArrayList<Struct> localEventList = new ArrayList<>();
    private ArrayList<Struct> officialEventList = new ArrayList<>();
    private ArrayList<String> Dates = new ArrayList<>();
    private PersianDate persianDate;
    private LinearLayoutManager officialEventManager;
    private Adapter_Recycler officialEventAdapter;
    private DrawerLayout drawer;
    private static ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static TextView toolbar_main_tv_header;
    private static ImageView btnAdd;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Dates = Utility.readCalendarEvent(getApplicationContext());
//
//        for (int i = 0; i < Dates.size(); i++) {
//            Struct struct = new Struct();
//            if (String.valueOf(Dates.get(i)).equals("02/03/2017"))
//            struct.strItemTitle = String.valueOf(Dates.get(i)) +" " + Utility.nameOfEvent;
//            localEventList.add(struct);
//        }

        // Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Fragment squadFragment = new FragmentNavigation();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, squadFragment, null);
        fragmentTransaction.commit();


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        btnAdd = (ImageView) findViewById(R.id.btnAdd);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndFragment.runDialog();
            }
        });

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });


        final ImageView fragmentNews = (ImageView) findViewById(R.id.btnNews);
        final ImageView fragmentCart = (ImageView) findViewById(R.id.btnCart);
        final ImageView fragmentCalendar = (ImageView) findViewById(R.id.btnCalendar);
        final ImageView fragmentFal = (ImageView) findViewById(R.id.btnFal);
        final ImageView fragmentEnd = (ImageView) findViewById(R.id.btnEnd);

        fragmentNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);

            }
        });
        fragmentCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);

            }
        });
        fragmentCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragmentFal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);

            }
        });
        fragmentEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);

            }
        });

        // View pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.activity_main_vp_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        headerTitle("اخبار");
        fragmentNews.setAlpha(1f);
        fragmentCart.setAlpha(.5f);
        fragmentCalendar.setAlpha(.5f);
        fragmentFal.setAlpha(.5f);
        fragmentEnd.setAlpha(.5f);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(50);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fragmentNews.setAlpha(1f);
                        fragmentCart.setAlpha(.5f);
                        fragmentCalendar.setAlpha(.5f);
                        fragmentFal.setAlpha(.5f);
                        fragmentEnd.setAlpha(.5f);
                        headerTitle("اخبار");
                        addButton(false);
                        break;
                    case 1:
                        fragmentNews.setAlpha(.5f);
                        fragmentCart.setAlpha(1f);
                        fragmentCalendar.setAlpha(.5f);
                        fragmentFal.setAlpha(.5f);
                        fragmentEnd.setAlpha(.5f);
                        headerTitle("فروشگاه");
                        addButton(false);
                        break;
                    case 2:
                        fragmentNews.setAlpha(.5f);
                        fragmentCart.setAlpha(.5f);
                        fragmentCalendar.setAlpha(.5f);
                        fragmentFal.setAlpha(1f);
                        fragmentEnd.setAlpha(.5f);
                        headerTitle("پیام موفقیتِ متولدین ماهها");
                        addButton(false);
                        break;
                    case 3:
                        fragmentNews.setAlpha(.5f);
                        fragmentCart.setAlpha(.5f);
                        fragmentCalendar.setAlpha(.5f);
                        fragmentFal.setAlpha(.5f);
                        fragmentEnd.setAlpha(1f);
                        headerTitle("اهداف");
                        addButton(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        //Returns a new instance of this fragment for the given section number.

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    // Image slider Adapter
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        //sets tab names
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mStr_TabNames = new String[]{"دانلودها", "بانک رینگو", "برش", "آرشیو"};
        }

        // sets tab content (fragment)
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NewsFragment();
                case 1:
                    return new StoreFragment();
                case 2:
                    return new FalFragment();
                case 3:
                    return new EndFragment();

            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStr_TabNames[position];
        }

    }

    public static void pageSelector(final int number) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(number);

            }
        }, 100);

    }

    public static void headerTitle(String text) {
        toolbar_main_tv_header.setText(text);
    }

    public static void addButton(Boolean isAvailable) {
        if (isAvailable) {
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
        }
    }
}
