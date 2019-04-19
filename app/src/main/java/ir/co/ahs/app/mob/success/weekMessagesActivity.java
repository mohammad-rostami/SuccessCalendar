package ir.co.ahs.app.mob.success;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ir.co.ahs.app.mob.success.MainActivity.headerTitle;

/**
 * Created by imac on 2/25/18.
 */

public class weekMessagesActivity extends AppCompatActivity implements WeekStoryFragment.OnFragmentInteractionListener {

    private String[] mStr_TabNames;
    private ArrayList<String> mArray_TabNames;
    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler adapter_recycler;
    private EditText editText;
    private LinearLayout mainLayout;
    private Dialog dialogPoll;
    private LinearLayout dialogPoll_MainLayout;
    private TextView dialogPoll_Title;
    private String headerID;
    private Typeface typeFace_Light;
    private Typeface typeFace_Medium;
    private Typeface typeFace_Bold;
    private TextView dialogPoll_Confirm;
    private TextView dialogPoll_Cancel;
    private int size;
    private String itemsJSON;
    private Boolean isSelected;
    private String itemID = null;
    private ArrayList<String> itemIDs = new ArrayList<>();
    private JSONArray A;
    private TextView state;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_messages);

        typeFace_Light = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
        typeFace_Medium = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_medium.ttf");
        typeFace_Bold = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_bold.ttf");

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("مطالب هفته");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        state = (TextView) findViewById(R.id.state);
        TextView btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(weekMessagesActivity.this, "نظر شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
            }
        });


        // Dialog Add To Cart

        dialogPoll = new Dialog(weekMessagesActivity.this);
        dialogPoll.setContentView(R.layout.dialog_poll);
        dialogPoll.setCancelable(false);
        dialogPoll_Title = (TextView) dialogPoll.findViewById(R.id.title);
        dialogPoll_MainLayout = (LinearLayout) dialogPoll.findViewById(R.id.main_layout);
        dialogPoll_Confirm = (TextView) dialogPoll.findViewById(R.id.confirm);
        dialogPoll_Cancel = (TextView) dialogPoll.findViewById(R.id.cancel);
        dialogPoll_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                A = new JSONArray();
                if (isSelected) {
                    if (itemIDs.size() > 0) {


                        JSONArray items = new JSONArray();
                        for (int i = 0; i < itemIDs.size(); i++) {
                            JSONObject itemsObject = new JSONObject();
                            try {
                                itemsObject.put("itemId", itemIDs.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            items.put(itemsObject);
                        }


                        JSONObject O = new JSONObject();
                        try {
                            O.put("headerId", headerID);
                            O.put("itemIdList", items);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        A.put(O);
                        Log.d("sending items", String.valueOf(A));

                        dialogPoll.dismiss();
                        dialogPoll_MainLayout.removeAllViews();
                    } else {
                        Toast.makeText(weekMessagesActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (itemID != null) {
                        JSONArray items = new JSONArray();
                        JSONObject itemsObject = new JSONObject();
                        try {
                            itemsObject.put("itemId", itemID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        items.put(itemsObject);


                        JSONObject O = new JSONObject();
                        try {
                            O.put("headerId", headerID);
                            O.put("itemIdList", items);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        A.put(O);
                        Log.d("sending items", String.valueOf(A));

                        dialogPoll.dismiss();
                        dialogPoll_MainLayout.removeAllViews();
                    } else {
                        Toast.makeText(weekMessagesActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();

                    }
                }
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncTests().execute(Urls.BASE_URL + Urls.GET_TEST_ALL, sessionId, userToken, customerId);
            }
        });
        dialogPoll_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPoll.dismiss();
                dialogPoll_MainLayout.removeAllViews();
            }
        });

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
//        getPoll();
//        getTests();

        final View.OnClickListener onclicklistener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(ActivityPoll.this, String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();

                JSONArray items = null;
                try {
                    if (isSelected) {
                        items = new JSONArray(itemsJSON);
                        JSONObject item = items.getJSONObject(v.getId());
                        itemID = item.getString("ItemID");
                        if (!itemIDs.contains(itemID)) {
                            itemIDs.add(itemID);
                        } else {
                            itemIDs.remove(itemID);
                        }
//                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
                    } else {
                        items = new JSONArray(itemsJSON);
                        JSONObject item = items.getJSONObject(v.getId());
                        itemID = item.getString("ItemID");
//                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                }
            }
        };

        // View pager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.activity_main_vp_container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.setCurrentItem(0);
//        mViewPager.setOffscreenPageLimit(0);
//        WeekStoryFragment.ReloadDataForFirstTime(0);


        mViewPager.setPageMargin(50);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                WeekStoryFragment.ReloadData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        try{
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(2);

            }
        },500);
        }catch (Exception e){}


    }

    private void checkState() {
        if (arrayList.size() > 0) {
            state.setVisibility(View.GONE);
        } else {
            state.setVisibility(View.VISIBLE);
        }
    }

    private void getTestItem(String id) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new Async().execute(Urls.BASE_URL + Urls.GET_TEST_ITEMS, sessionId, userToken, customerId, id);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class Async extends Webservice.getTestItem {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;

        }
    }

    public void getTests() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new AsyncTests().execute(Urls.BASE_URL + Urls.أٍGET_WEEK_MESSAGE, sessionId, userToken);
    }

    private class AsyncTests extends Webservice.getInfo {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
            arrayList.clear();


            JSONArray array = null;
            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Struct struct = new Struct();
//                    struct.strId = object.getString("messageId");
                    struct.strName = object.getString("storyGroupTitle");
                    struct.strItemTitle = object.getString("storyTitle");
                    struct.strText = object.getString("storyText");
                    struct.strImg = object.getString("storyPhoto");
                    struct.strDate = object.getString("storyDate");

                    arrayList.add(struct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter_recycler.notifyDataSetChanged();
////            getPoll();

        }
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
//            mStr_TabNames = new String[]{"لغو شده", "مختومه", "در حال پیگیری"};
        }

        // sets tab content (fragment)
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                    return new WeekStoryFragment();
//                case 1:
//                    return new WeekStoryFragment();

            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
//            return 3;
            return G.WEEK_STORY_GROUPS_ARRAY.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mStr_TabNames[position];
            return getTabNames(position);
        }

    }

    public String getTabNames(int position) {
        String names = null;
//        for (int i = 0; i < G.WEEK_STORY_GROUPS_ARRAY.size(); i++) {

        String currentString = G.WEEK_STORY_GROUPS_ARRAY.get(position);
        String[] separated = currentString.split(":");
        String ids = separated[0];
        names = separated[1];
//        }
        return names;
    }
}