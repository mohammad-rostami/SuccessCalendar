package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by imac on 2/25/18.
 */

public class TargetActivity extends Activity {
    private Adapter_Recycler adapter_recycler;
    private String targetID;
    private String targetNAME;
    private TextView list_state;
    private ImageView btnAdd;

    public static Dialog dialog_end;
    private TextView dialog_end_header;
    private EditText dialog_end_title;
    private TextView dialog_end_cancel;
    private TextView dialog_end_register;
    private int monthNumber = 1;
    private Dialog dialog_end_edit;
    private TextView dialog_end_edit_header;
    private EditText dialog_end_edit_title;
    private TextView dialog_end_edit_cancel;
    private TextView dialog_end_edit_register;
    private ImageView dialog_end_edit_Plus;
    private ImageView dialog_end_edit_Minus;
    private EditText dialog_end_edit_Number;
    private JSONArray groupsArray;
    private String groupId;
    private String groupTitle;
    private int number = 0;
    private String targetMONTH;
    private String targetMONTH_NAME;
    private String edit_id;
    private Boolean check_state;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        try {
            targetID = getIntent().getStringExtra("ID");
            targetNAME = getIntent().getStringExtra("NAME");
            targetMONTH = getIntent().getStringExtra("MONTH");
            targetMONTH_NAME = getIntent().getStringExtra("MONTH_NAME");

        } catch (Exception e) {
        }

        // Dialog
        dialog_end = new Dialog(TargetActivity.this);
        dialog_end.setContentView(R.layout.dialog_end);
        dialog_end_header = (TextView) dialog_end.findViewById(R.id.dialog_edt_header);
        dialog_end_title = (EditText) dialog_end.findViewById(R.id.dialog_edt_title);
        dialog_end_cancel = (TextView) dialog_end.findViewById(R.id.dialog_tv_cancel);
        dialog_end_register = (TextView) dialog_end.findViewById(R.id.dialog_tv_register);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog_end.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog_end_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addInformation(dialog_end_title.getText().toString());
                dialog_end.dismiss();

//                itemChecker("END_TABLE", monthNumber, dialog_end_title.getText().toString());
//                if (canNotBeAdded == false) {
//                    dialog_end.dismiss();
//                    Random rand = new Random();
//                    int Id = rand.nextInt((100000 - 1) + 1) + 1;
//                    addToDatabase("END_TABLE", monthNumber, 0, dialog_end_title.getText().toString(), Id);
//                } else {
//                    Toast.makeText(getContext(), "این نام قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
//                }
//                canNotBeAdded = false;
                dialog_end_title.setText("");
            }
        });
        dialog_end_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_end.dismiss();
                dialog_end_title.setText("");
            }
        });


        // Dialog
        dialog_end_edit = new Dialog(TargetActivity.this);
        dialog_end_edit.setContentView(R.layout.dialog_edit_data);
        dialog_end_edit_header = (TextView) dialog_end_edit.findViewById(R.id.dialog_edt_header);
        dialog_end_edit_title = (EditText) dialog_end_edit.findViewById(R.id.dialog_edt_title);
        dialog_end_edit_cancel = (TextView) dialog_end_edit.findViewById(R.id.dialog_tv_cancel);
        dialog_end_edit_register = (TextView) dialog_end_edit.findViewById(R.id.dialog_tv_register);
        dialog_end_edit_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = dialog_end_edit_title.getText().toString();
                editInformation(title, edit_id,check_state);
                dialog_end_edit.dismiss();


//                dialog_end_edit.dismiss();
//                int percent = Integer.parseInt(dialog_end_edit_Number.getText().toString());
//
//                DataBaseEditor("END_TABLE", Integer.parseInt(itemID), percent);
//                addToDatabase("END_TABLE", monthNumber, 0, dialog_end_title.getText().toString());
                dialog_end_edit_title.setText("");
            }
        });
        dialog_end_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_end_edit.dismiss();
                dialog_end_edit_title.setText("");
            }
        });


        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        btnNavigation.setImageResource(R.mipmap.ic_back);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText(targetMONTH_NAME + " - " + targetNAME);
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd = (ImageView) findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.VISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDialog();
            }
        });


        list_state = (TextView) findViewById(R.id.list_state);


        RecyclerView menuRecycler = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {
                String id = arrayList.get(position).strId;
                String title = arrayList.get(position).strItemTitle;
                Boolean state = !arrayList.get(position).isDone;
                editInformation(title, id,state);
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", arrayList.get(position).strText, null));
//                startActivity(intent);
            }

            @Override
            public void onItemClick(int position) {

//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).strText));
//                startActivity(browserIntent);
            }

            @Override
            public void onItemDelete(int position) {
                String id = arrayList.get(position).strId;
                String title = arrayList.get(position).strItemTitle;
                delete(title, id);
            }

            @Override
            public void onCardClick(int position) {
                edit_id = arrayList.get(position).strId;
                String title = arrayList.get(position).strItemTitle;
                check_state = arrayList.get(position).isDone;
                dialog_end_edit_title.setText(title);

                dialog_end_edit.show();
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto: " + arrayList.get(position).strText));
//                startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            }

            @Override
            public void onEdit(int position) {

            }
        }, 16, false);
        menuRecycler.setAdapter(adapter_recycler);
        menuRecycler.setLayoutManager(manager);

        getInformation();

    }

    public void addInformation(String title) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerID = G.CUSTOMER_ID.getString("CUSTOMER_ID", "nothing");
        new addTargetAsync().execute(Urls.BASE_URL + Urls.ADD_TARGET, sessionId, userToken, customerID, targetID, "1396", targetMONTH, title);
    }

    public void getInformation() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerID = G.CUSTOMER_ID.getString("CUSTOMER_ID", "nothing");
        new getTargetAsync().execute(Urls.BASE_URL + Urls.GET_TARGET, sessionId, userToken, customerID, targetID, "1396", targetMONTH);
    }

    public void editInformation(String title, String id,Boolean state) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerID = G.CUSTOMER_ID.getString("CUSTOMER_ID", "nothing");
        new editTargetAsync().execute(Urls.BASE_URL + Urls.EDIT_TARGET, sessionId, userToken, customerID, targetID, "1396", targetMONTH, title, id,String.valueOf(state));
    }

    public void delete(String title, String id) {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        String customerID = G.CUSTOMER_ID.getString("CUSTOMER_ID", "nothing");
        new deleteTargetAsync().execute(Urls.BASE_URL + Urls.DELETE_TARGET, sessionId, userToken, customerID, targetID, "1396", targetMONTH, title, id);
    }

    private class getTargetAsync extends Webservice.getTarget {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            arrayList.clear();
            String x = result;

            try {
                JSONArray main = new JSONArray(result);
                for (int i = 0; i < main.length(); i++) {
                    Struct struct = new Struct();
                    JSONObject mainObject = main.getJSONObject(i);
                    String title = mainObject.getString("targetTitle");
                    String id = mainObject.getString("targetId");
                    boolean isDone = mainObject.getBoolean("isDone");

                    struct.strItemTitle = title;
                    struct.strId = id;
                    struct.isDone = isDone;
                    arrayList.add(struct);

                }
                adapter_recycler.notifyDataSetChanged();

            } catch (JSONException e) {


            }


            if (arrayList.size() < 1) {
                list_state.setVisibility(View.VISIBLE);
            } else {
                list_state.setVisibility(View.GONE);
            }
        }
    }

    private class addTargetAsync extends Webservice.addTarget {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            getInformation();
            String x = result;
        }
//
//            try {
//                JSONArray main = new JSONArray(result);
//                for (int i = 0; i < main.length(); i++) {
//                    JSONObject mainObject = main.getJSONObject(i);
//                    int type = mainObject.getInt("TypeId");
//                    String title = mainObject.getString("Title");
//                    String text = mainObject.getString("Text");
//                    int order = mainObject.getInt("Order");
//
//                    Struct struct = new Struct();
//                    struct.strItemTitle = title;
//                    struct.strText = text;
//                    if (type == 6 || type == 8) {
//                        struct.strType = "web";
//                    } else if (type == 5 || type == 2) {
//                        struct.strType = "call";
//                    } else if (type == 7) {
//                        struct.strType = "email";
//                    } else {
//                        struct.strType = "other";
//
//                    }
//                    arrayList.add(struct);
//
//                }
//                adapter_recycler.notifyDataSetChanged();
//
//            } catch (JSONException e) {
//
//
//            }
//
//
//            if (arrayList.size() < 1) {
//                list_state.setVisibility(View.VISIBLE);
//            } else {
//                list_state.setVisibility(View.GONE);
//            }
//        }
    }

    private class editTargetAsync extends Webservice.editData {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            getInformation();
            String x = result;
        }

    }

    private class deleteTargetAsync extends Webservice.deleteTarget {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            getInformation();
            String x = result;
        }

    }

    public static void runDialog() {
        dialog_end.show();
    }

}
