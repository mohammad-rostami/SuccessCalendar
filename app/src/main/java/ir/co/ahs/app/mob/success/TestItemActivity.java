package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class TestItemActivity extends Activity {


    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler_Select adapter_recycler;
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
    private TextView item_title;
    private TextView item_text;
    private TextView item_date;
    private ImageView item_image;
    private LinearLayout music_layout;
    private ImageView play;
    private String media;
    private MediaPlayer mp;
    private ProgressBar progress;
    private ImageView pause;
    private LinearLayout text_layout;
    private TextView texts;
    private LinearLayout video_layout;
    private VideoView videoView;
    private View.OnClickListener onclicklistener;
    private TextView submit;
    private String json;
    private String questionId;
    private LinearLayout test_layout;
    private LinearLayout answer_layout;
    private TextView answer;

    ArrayList<String> items = new ArrayList<>();
    private String headerTitle;
    private JSONArray main;
    private CardView submit_button;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_item);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("تست روانشناسی");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final RecyclerView list = (RecyclerView) findViewById(R.id.pollList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler_Select(getApplicationContext(), arrayList, new OnItemSelector() {
            @Override
            public void onItemSelect(int position, String string) {
                String questionId = arrayList.get(position).strItemId;
                String itemId = string;

                String answer = questionId + ":" + itemId;
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).contains(questionId)) {
                        items.remove(i);

                    }
                }
                items.add(answer);

                Log.d("size", "size" + String.valueOf(items.size()));
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemDelete(int position) {

            }

            @Override
            public void onCardClick(int position) {
//                dialogPoll.show();
                headerID = arrayList.get(position).strId;

                getTestItem(headerID);

            }

            @Override
            public void onEdit(int position) {

            }
        }, 18, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);

        dialogPoll_Title = (TextView) findViewById(R.id.title);
        state = (TextView) findViewById(R.id.state);
        submit_button = (CardView) findViewById(R.id.submit_button);

        submit = (TextView)

                findViewById(R.id.submit);

        answer = (TextView)

                findViewById(R.id.answer);

        dialogPoll_MainLayout = (LinearLayout)

                findViewById(R.id.main_layout);

        test_layout = (LinearLayout)

                findViewById(R.id.test_layout);

        answer_layout = (LinearLayout)

                findViewById(R.id.answer_layout);

        submit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                try {

//                    O.put("headerId", headerID);
                    if (items.size() == main.length()) {

                        A = new JSONArray();

                        for (int i = 0; i < items.size(); i++) {
                            String CurrentString = items.get(i);
                            String[] separated = CurrentString.split(":");
                            String questionId = separated[0];
                            String answerId = separated[1];

                            JSONArray items = new JSONArray();
                            JSONObject itemsObject = new JSONObject();
                            try {
                                itemsObject.put("answerId", answerId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            items.put(itemsObject);


                            JSONObject O = new JSONObject();
                            try {
                                O.put("questionId", questionId);
                                O.put("answerList", items);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            A.put(O);
                            Log.d("sending items", String.valueOf(A));
                        }

                        JSONArray x = A;
                        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    new AsyncTests().execute(Urls.BASE_URL + Urls.SEND_TEST_ANSWER, sessionId, userToken, customerId,headerID);
                        new AsyncTestAnswer().execute(Urls.BASE_URL + Urls.SEND_TEST_ANSWER, sessionId, userToken, customerId, headerID, String.valueOf(A));

                    } else {
                        Toast.makeText(TestItemActivity.this, "لطفا به همه سوالات پاسخ دهید", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                }
            }
        });


        onclicklistener = new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                JSONArray items = null;
                try {

                    items = new JSONArray(json);
                    JSONObject object = items.getJSONObject(0);
                    JSONArray itemsArray = object.getJSONArray("answerList");
                    JSONObject item = itemsArray.getJSONObject(v.getId());
                    itemID = item.getString("answerID");

                } catch (JSONException e) {
                }
            }
        }

        ;


        try

        {
            json = getIntent().getStringExtra("json");
            headerID = getIntent().getStringExtra("headerID");
            headerTitle = getIntent().getStringExtra("headerTitle");
            toolbar_main_tv_header.setText("تست روانشناسی - " + headerTitle);

            try {
                main = new JSONArray(json);
                for (int i = 0; i < main.length(); i++) {
                    JSONObject data = main.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strItemTitle = data.getString("questionTitle");
                    struct.strItemId = data.getString("questionID");
                    struct.jsonObject = data;
                    struct.json = json;
                    arrayList.add(struct);
                }
                adapter_recycler.notifyDataSetChanged();
            } catch (JSONException e) {

            }
            checkState();
        } catch (
                Exception e)

        {

        }
    }

    private void checkState() {
        if (arrayList.size() > 0) {
            state.setVisibility(View.GONE);
            submit_button.setVisibility(View.VISIBLE);
        } else {
            state.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.GONE);
        }
    }

    private void getTestItem(String id) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new Async().execute(Urls.BASE_URL + Urls.GET_TEST_ITEMS, sessionId, userToken, customerId, id);

    }

    private class Async extends Webservice.getTestItem {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {

        }
    }

    public void getTests() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new AsyncTests().execute(Urls.BASE_URL + Urls.أٍGET_SUCCESS_MESSAGE, sessionId, userToken);
    }

    private class AsyncTests extends Webservice.getInfo {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            arrayList.clear();


            JSONArray array = null;
            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strId = object.getString("messageId");
                    struct.strName = object.getString("messageTitle");
                    struct.strText = object.getString("messageText");
                    struct.strImg = object.getString("photoURL");
                    struct.strDate = object.getString("messageDate");
//                    struct.strVid = object.getString("mediaURL");

                    arrayList.add(struct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter_recycler.notifyDataSetChanged();
//            getPoll();

        }
    }

    private class AsyncTestAnswer extends Webservice.sendTestAnswer {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            Log.d("poll", result);
            test_layout.setVisibility(View.GONE);
            answer_layout.setVisibility(View.VISIBLE);

            try {
                JSONObject object = new JSONObject(result);
                String text = object.getString("resultText");
                answer.setText(text);
            } catch (Exception e) {

            }

        }
    }
}