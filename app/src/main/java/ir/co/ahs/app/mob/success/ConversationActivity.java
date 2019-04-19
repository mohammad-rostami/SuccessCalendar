package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseWriter_Conversation;
import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class ConversationActivity extends Activity {

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    static ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private static Adapter_Recycler adapter_recycler;
    private EditText editText;
    private static TextView state;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConversations();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("پیامها");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConversationActivity.this, MessageAddActivity.class);
                ConversationActivity.this.startActivity(intent);
            }
        });

        getConversations();


        state = (TextView) findViewById(R.id.state);
        editText = (EditText) findViewById(R.id.new_message);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
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


        RecyclerView list = (RecyclerView) findViewById(R.id.messageList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {

                String id = arrayList.get(position).strId;
                String title = arrayList.get(position).strItemTitle;
                Intent intent = new Intent(ConversationActivity.this, MessageAddActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("TITLE", title);
                ConversationActivity.this.startActivity(intent);
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
        }, 14, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);
    }

    private class Async extends Webservice.getConversationList {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            clearDateBase("CONVERSATION_TABLE");
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newObject = jsonArray.getJSONObject(i);
                    String conversationId = newObject.getString("conversationId");
                    String title = newObject.getString("title");
                    int unread = newObject.getInt("unReadMessageCount");
                    String senderName = newObject.getString("senderName");
                    String receiverName = newObject.getString("receiverName");
                    String message = newObject.getString("lastMessage");
                    String sendTime = newObject.getString("theTime");
                    String sendDate = newObject.getString("theDate");

                    addToDatabase("CONVERSATION_TABLE", conversationId, title, String.valueOf(unread), senderName, receiverName, message, sendDate, sendTime);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            recyclerInitializer("CONVERSATION_TABLE");

        }
    }

    public static void cartStateCheck() {
        System.out.println(String.valueOf(arrayList.size()));
        if (arrayList.size() == 0) {
            state.setVisibility(View.VISIBLE);

        } else {
            state.setVisibility(View.GONE);


        }
    }

    private static void addToDatabase(String TableName, String conversationId, String title, String unReadMessageCount, String senderName, String receiverName, String lastMessage, String theDate, String theTime) {
        DataBaseWriter_Conversation helper = new DataBaseWriter_Conversation(G.CONTEXT, "CONVERSATION_DATABASE", TableName, G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    conversationId,
                    title,
                    unReadMessageCount,
                    senderName,
                    receiverName,
                    lastMessage,
                    theDate,
                    theTime
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void recyclerInitializer(String TableName) {
        arrayList.clear();
        try {
            adapter_recycler.notifyDataSetChanged();
        } catch (Exception e) {
        }
        DataBaseWriter_Conversation helper = new DataBaseWriter_Conversation(G.CONTEXT, "CONVERSATION_DATABASE", TableName, G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        String conversationId;
        String title;
        int unread;
        String senderName;
        String receiverName;
        String message;
        String sendDate;
        String sendTime;
        if (cursor.moveToFirst()) {
            do {
                conversationId = cursor.getString(cursor.getColumnIndex("conversationId"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                unread = cursor.getInt(cursor.getColumnIndex("unReadMessageCount"));
                senderName = cursor.getString(cursor.getColumnIndex("senderName"));
                receiverName = cursor.getString(cursor.getColumnIndex("receiverName"));
                message = cursor.getString(cursor.getColumnIndex("lastMessage"));
                sendDate = cursor.getString(cursor.getColumnIndex("theDate"));
                sendTime = cursor.getString(cursor.getColumnIndex("theTime"));

                Struct struct = new Struct();
                struct.strId = conversationId;
                struct.strItemTitle = title;
                struct.intQuantity = unread;
                struct.strName = senderName;
                struct.strReceiver = receiverName;
                struct.strText = message;
                struct.strDate = sendDate;
                struct.strTime = sendTime;
                arrayList.add(struct);
            } while (cursor.moveToNext());
            try {
                adapter_recycler.notifyDataSetChanged();
                cartStateCheck();

            } catch (Exception e) {
            }
        }
        sqld.close();
    }

    public void clearDateBase(String TableName) {
        DataBaseWriter_Conversation dataBaseHelper = new DataBaseWriter_Conversation(getBaseContext(), "CONVERSATION_DATABASE", "CONVERSATION_TABLE", G.ConversationTableFieldNames, G.ConversationTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        sqld.execSQL("delete from " + TableName);


        cartStateCheck();

//        Cursor cursor = sqld.rawQuery("TRUNCATE TABLE " + TableName, null);
    }

    public void getConversations() {
        try {
            if (G.isNetworkAvailable()) {

                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new Async().execute(Urls.BASE_URL + Urls.GET_CONVERSATION_LIST, sessionId, userToken, customerId);

            } else {
                recyclerInitializer("CONVERSATION_TABLE");
            }
        } catch (Exception e) {

        }
    }
}
