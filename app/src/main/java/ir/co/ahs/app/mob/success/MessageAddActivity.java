package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class MessageAddActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler adapter_recycler;
    private EditText editText;
    private EditText toolbar_main_tv_header;
    private ImageView send;
    public String conversationId = null;
    public String title = null;
    private RecyclerView list;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_add);


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        toolbar_main_tv_header = (EditText) findViewById(R.id.toolbar_main_tv_header);


        try {
            Intent intent = getIntent();
            conversationId = intent.getExtras().getString("ID");
            title = intent.getExtras().getString("TITLE");
            toolbar_main_tv_header.setText(title);
            toolbar_main_tv_header.setEnabled(false);

            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
            String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
            new AsyncConversation().execute(Urls.BASE_URL + Urls.GET_CONVERSATION, sessionId, userToken, customerId, conversationId);

        } catch (Exception e) {
        }

        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toolbar_main_tv_header.setHint("عنوان گفتگو را وارد کنید");
                toolbar_main_tv_header.setHintTextColor(getResources().getColor(R.color.Red));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        send = (ImageView) findViewById(R.id.send);
        send.setEnabled(false);
        send.setColorFilter(getResources().getColor(R.color.lightGray));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbar_main_tv_header.getText().toString().length() < 1) {
                    toolbar_main_tv_header.startAnimation(animation);
                } else {
                    toolbar_main_tv_header.setEnabled(false);
                    Toast.makeText(MessageAddActivity.this, "worked", Toast.LENGTH_SHORT).show();

                    String title;
                    String state;
                    String message = editText.getText().toString();

                    String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                    String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");

                    if (conversationId != null) {
                        state = "conversationId";
                        title = conversationId;
                    } else {
                        state = "title";
                        title = toolbar_main_tv_header.getText().toString();
                    }
                    new Async().execute(Urls.BASE_URL + Urls.SEND_MESSAGE, sessionId, userToken, customerId, title, message, state);
                    editText.setText("");
                    list.smoothScrollToPosition(arrayList.size() + 1);
                }
            }
        });


        editText = (EditText) findViewById(R.id.new_message);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    send.setColorFilter(getResources().getColor(R.color.colorPrimary));
                    send.setEnabled(true);

                } else {
                    send.setColorFilter(getResources().getColor(R.color.lightGray));
                    send.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(MessageAddActivity.this, "sadsa", Toast.LENGTH_SHORT).show();
                    Struct struct = new Struct();
                    struct.strName = editText.getText().toString();
                    struct.strType = "2";
                    arrayList.add(struct);
                    adapter_recycler.notifyItemInserted(4);

                    editText.setText("");
                    handled = true;
                }
                return handled;
            }
        });


        list = (RecyclerView) findViewById(R.id.messageList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
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
        }, 10, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);
    }

    private class Async extends Webservice.sendMessage {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                conversationId = jsonObject.getString("conversationId");

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncConversation().execute(Urls.BASE_URL + Urls.GET_CONVERSATION, sessionId, userToken, customerId, conversationId);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private class AsyncConversation extends Webservice.getConversation {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            arrayList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("messageList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String senderName = newObject.getString("senderName");
                    String receiverName = newObject.getString("receiverName");
                    String message = newObject.getString("message");
                    String sendTime = newObject.getString("sendTime");
                    String sendDate = newObject.getString("sendDate");
                    Boolean amIsender = newObject.getBoolean("amISender");

                    Struct struct = new Struct();
                    struct.strName = senderName;
                    struct.strReceiver = receiverName;
                    struct.strText = message;
                    struct.strDate = sendDate;
                    struct.strTime = sendTime;
                    struct.sender = amIsender;

                    arrayList.add(struct);
                }

                adapter_recycler.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
