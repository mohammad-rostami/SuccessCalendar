package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class VideoActivity extends Activity implements EasyVideoCallback {


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
    //    private Typeface typeFace_Bold;
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
    //    private ImageView play;
    private String media;
    private MediaPlayer mp;
    private ProgressBar progress;
    private ImageView pause;
    private LinearLayout text_layout;
    private TextView texts;
    private FrameLayout video_layout;
    private EasyVideoPlayer videoView;
    private int remainder;
    private TextView current;
    private TextView max;
    private boolean isPlaying = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

//        current = (TextView) findViewById(R.id.current);
//        max = (TextView) findViewById(R.id.maximum);
        texts = (TextView) findViewById(R.id.texts);
        item_title = (TextView) findViewById(R.id.item_title);
        item_text = (TextView) findViewById(R.id.item_text);
        item_date = (TextView) findViewById(R.id.item_date);
        item_image = (ImageView) findViewById(R.id.item_image);
//        music_layout = (LinearLayout) findViewById(R.id.music_layout);
        text_layout = (LinearLayout) findViewById(R.id.text_layout);
        video_layout = (FrameLayout) findViewById(R.id.video_layout);
//        play = (ImageView) findViewById(R.id.play);
//        pause = (ImageView) findViewById(R.id.pause);
//        progress = (ProgressBar) findViewById(R.id.progress);

//        videoView = (VideoView) findViewById(R.id.video_view);
        videoView = (EasyVideoPlayer) findViewById(R.id.video_view);
        videoView.setCallback(this);


        typeFace_Light = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
        typeFace_Medium = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_light.ttf");
//        typeFace_Bold = Typeface.createFromAsset(G.CONTEXT.getAssets(), "iran_bold.ttf");

        try {
//            String id = getIntent().getStringExtra("id");
//            String name = getIntent().getStringExtra("name");
//            String text = getIntent().getStringExtra("text");
//            String image = getIntent().getStringExtra("img");
//            String date = getIntent().getStringExtra("date");
            media = getIntent().getStringExtra("media");
//            String type = getIntent().getStringExtra("type");


//            item_title.setText(name);
//            item_text.setText(text);
//            item_date.setText(date);
//            Glide.with(G.CONTEXT).load(image).fitCenter().into(item_image);
//
//            if (type.equals("1")) {
//                text
//                text_layout.setVisibility(View.GONE);
//                music_layout.setVisibility(View.GONE);
//                video_layout.setVisibility(View.GONE);
//                texts.setText(media);
//            } else if (type.equals("2")) {
//                audio
//                music_layout.setVisibility(View.VISIBLE);
//                text_layout.setVisibility(View.GONE);
//                video_layout.setVisibility(View.GONE);
//
//            } else {
            //video
//                music_layout.setVisibility(View.GONE);
//                text_layout.setVisibility(View.GONE);
            video_layout.setVisibility(View.VISIBLE);

            videoView.setSource(Uri.parse(media));
            videoView.start();
//                MediaController mediaController = new MediaController(this);
//                mediaController.setAnchorView(videoView);
//                videoView.setMediaController(mediaController);
//                videoView.setVideoURI(Uri.parse(media));


//                int id = **"The Video's ID"**
//                ImageView iv = (ImageView ) convertView.findViewById(R.id.imagePreview);
//                ContentResolver crThumb = getContentResolver();
//                BitmapFactory.Options options=new BitmapFactory.Options();
//                options.inSampleSize = 1;
//                Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(media, MediaStore.Video.Thumbnails.MINI_KIND);
            item_image.setImageBitmap(thumbnail);
//                item_image

//            }

//            item_title.setText(name);
//            item_title.setText(name);


        } catch (Exception e) {
        }
        mp = new MediaPlayer();

//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isPlaying) {
//                    mp.start();
//                    play.setVisibility(View.GONE);
//                    pause.setVisibility(View.VISIBLE);
//                } else {
//                    try {
//                        mp.setDataSource(media);
//                        mp.prepare();
//                        int maximum = mp.getDuration() / 1000;
//                        int mins = maximum / 60;
//                        remainder = maximum - (mins * 60);
//                        int secs = remainder;
//                        String time = String.valueOf(mins) + " : " + String.valueOf(secs);
//                        max.setText(time);
//                        progress.setMax(maximum);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    mp.start();
//
//                    final Handler mHandler = new Handler();
////Make sure you update Seekbar on UI thread
//                    VideoActivity.this.runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            if (mp != null) {
//                                int mCurrentPosition = mp.getCurrentPosition() / 1000;
//                                progress.setProgress(mCurrentPosition);
//
//                                int mins = mCurrentPosition / 60;
//                                remainder = mCurrentPosition - (mins * 60);
//                                int secs = remainder + 1;
//                                String time = String.valueOf(mins) + " : " + String.valueOf(secs);
//                                current.setText(time);
//                            }
//                            mHandler.postDelayed(this, 1000);
//                        }
//                    });
//
//                    play.setVisibility(View.GONE);
//                    pause.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPlaying = true;
//                mp.pause();
////                mp.reset();
//                pause.setVisibility(View.GONE);
//                play.setVisibility(View.VISIBLE);
//            }
//        });
//
//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                play.setVisibility(View.VISIBLE);
//                pause.setVisibility(View.GONE);
//            }
//        });


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("ویدیو");
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
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    private class Async extends Webservice.getTestItem {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {

        }
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
}