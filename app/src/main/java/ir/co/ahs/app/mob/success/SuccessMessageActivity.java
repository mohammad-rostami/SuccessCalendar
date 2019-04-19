package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class SuccessMessageActivity extends Activity {


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
    private ImageView play_video;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_message);

        texts = (TextView) findViewById(R.id.texts);
        item_title = (TextView) findViewById(R.id.item_title);
        item_text = (TextView) findViewById(R.id.item_text);
        item_date = (TextView) findViewById(R.id.item_date);
        item_image = (ImageView) findViewById(R.id.item_image);
        music_layout = (LinearLayout) findViewById(R.id.music_layout);
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);
        progress = (ProgressBar) findViewById(R.id.progress);

        videoView = (VideoView) findViewById(R.id.video_view);
        play_video = (ImageView) findViewById(R.id.play_video);


        typeFace_Light = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
        typeFace_Medium = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_medium.ttf");
        typeFace_Bold = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_bold.ttf");

        try {
            String id = getIntent().getStringExtra("id");
            String name = getIntent().getStringExtra("name");
            String text = getIntent().getStringExtra("text");
            String image = getIntent().getStringExtra("img");
            String date = getIntent().getStringExtra("date");
            media = getIntent().getStringExtra("media");
            String type = getIntent().getStringExtra("type");


            item_title.setText(name);
            item_text.setText(text);
            item_date.setText(date);
            Glide.with(G.CONTEXT).load(image).placeholder(R.drawable.news).into(item_image);

            if (type.equals("1")) {
                //text
                text_layout.setVisibility(View.VISIBLE);
                music_layout.setVisibility(View.GONE);
                video_layout.setVisibility(View.GONE);
                texts.setText(media);
            } else if (type.equals("2")) {
                //audio
                music_layout.setVisibility(View.VISIBLE);
                text_layout.setVisibility(View.GONE);
                video_layout.setVisibility(View.GONE);

            } else {
                //video
                music_layout.setVisibility(View.GONE);
                text_layout.setVisibility(View.GONE);
                video_layout.setVisibility(View.GONE);
//                MediaController mediaController = new MediaController(this);
//                mediaController.setAnchorView(videoView);
//                videoView.setMediaController(mediaController);
//                videoView.setVideoURI(Uri.parse(media));

                play_video.setVisibility(View.VISIBLE);
                play_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SuccessMessageActivity.this, VideoActivity.class);
                        intent.putExtra("media", media);
                        SuccessMessageActivity.this.startActivity(intent);
                    }
                });

            }

            item_title.setText(name);
            item_title.setText(name);


        } catch (Exception e) {
        }
        mp = new MediaPlayer();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (G.isRunning) {
                    mp.start();

                } else {
                    try {
                        mp.setDataSource(media);
                        mp.prepare();
                        int maximum = mp.getDuration() / 1000;
                        progress.setMax(maximum);
                    } catch (Exception v) {
                        v.printStackTrace();
                    }

                    mp.start();
                }


                final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
                SuccessMessageActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mp != null) {
                            int mCurrentPosition = mp.getCurrentPosition() / 1000;
                            progress.setProgress(mCurrentPosition);
                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);


            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.isRunning = true;
                mp.pause();
//                mp.reset();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("پیام موفقیت");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.pause();
                    mp.reset();
                } catch (Exception e) {

                }
                finish();
            }
        });


//        state = (TextView) findViewById(R.id.state);
//        TextView btnSubmit = (TextView) findViewById(R.id.btnSubmit);
//        btnSubmit.setVisibility(View.GONE);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SuccessMessageActivity.this, "نظر شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Dialog Add To Cart

//        dialogPoll = new Dialog(SuccessMessageActivity.this);
//        dialogPoll.setContentView(R.layout.dialog_poll);
//        dialogPoll.setCancelable(false);
//        dialogPoll_Title = (TextView) dialogPoll.findViewById(R.id.title);
//        dialogPoll_MainLayout = (LinearLayout) dialogPoll.findViewById(R.id.main_layout);
//        dialogPoll_Confirm = (TextView) dialogPoll.findViewById(R.id.confirm);
//        dialogPoll_Cancel = (TextView) dialogPoll.findViewById(R.id.cancel);
//        dialogPoll_Confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                A = new JSONArray();
//                if (isSelected) {
//                    if (itemIDs.size() > 0) {
//
//
//                        JSONArray items = new JSONArray();
//                        for (int i = 0; i < itemIDs.size(); i++) {
//                            JSONObject itemsObject = new JSONObject();
//                            try {
//                                itemsObject.put("itemId", itemIDs.get(i));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            items.put(itemsObject);
//                        }
//
//
//                        JSONObject O = new JSONObject();
//                        try {
//                            O.put("headerId", headerID);
//                            O.put("itemIdList", items);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        A.put(O);
//                        Log.d("sending items", String.valueOf(A));
//
//                        dialogPoll.dismiss();
//                        dialogPoll_MainLayout.removeAllViews();
//                    } else {
//                        Toast.makeText(SuccessMessageActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();
//
//                    }
//                } else {
//                    if (itemID != null) {
//                        JSONArray items = new JSONArray();
//                        JSONObject itemsObject = new JSONObject();
//                        try {
//                            itemsObject.put("itemId", itemID);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        items.put(itemsObject);
//
//
//                        JSONObject O = new JSONObject();
//                        try {
//                            O.put("headerId", headerID);
//                            O.put("itemIdList", items);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        A.put(O);
//                        Log.d("sending items", String.valueOf(A));
//
//                        dialogPoll.dismiss();
//                        dialogPoll_MainLayout.removeAllViews();
//                    } else {
//                        Toast.makeText(SuccessMessageActivity.this, "لطفا یک مورد را انتخاب کنید", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
//                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
//                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                new AsyncTests().execute(Urls.BASE_URL + Urls.GET_TEST_ALL, sessionId, userToken, customerId);
//            }
//        });
//        dialogPoll_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogPoll.dismiss();
//                dialogPoll_MainLayout.removeAllViews();
//            }
//        });
//
//        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
////        getPoll();
//        getTests();
//
//        final View.OnClickListener onclicklistener = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(ActivityPoll.this, String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
//
//                JSONArray items = null;
//                try {
//                    if (isSelected) {
//                        items = new JSONArray(itemsJSON);
//                        JSONObject item = items.getJSONObject(v.getId());
//                        itemID = item.getString("ItemID");
//                        if (!itemIDs.contains(itemID)) {
//                            itemIDs.add(itemID);
//                        } else {
//                            itemIDs.remove(itemID);
//                        }
////                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
//                    } else {
//                        items = new JSONArray(itemsJSON);
//                        JSONObject item = items.getJSONObject(v.getId());
//                        itemID = item.getString("ItemID");
////                        Toast.makeText(TestActivity.this, item.getString("ItemID"), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                }
//            }
//        };
//
//        final RecyclerView list = (RecyclerView) findViewById(R.id.pollList);
//        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
//        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
//            @Override
//            public void onItemSelect(int position) {
//
//            }
//
//            @Override
//            public void onItemClick(int position) {
//
//            }
//
//            @Override
//            public void onItemDelete(int position) {
//
//            }
//
//            @Override
//            public void onCardClick(int position) {
//
//
//            }
//
//            @Override
//            public void onEdit(int position) {
//
//            }
//        }, 17, false);
//        list.setAdapter(adapter_recycler);
//        list.setLayoutManager(manager);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mp.pause();
            mp.reset();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        G.isRunning = false;
    }
}