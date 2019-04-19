package ir.co.ahs.app.mob.success;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ir.co.ahs.app.mob.success.Helper.DataBaseWriter_End;
import ir.co.ahs.app.mob.success.Helper.DataBaseWriter_Event;
import ir.co.persiancalendar.core.G;

public class EndFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private static boolean canNotBeAdded = false;
    private final String PLUS_ONE_URL = "http://developer.android.com";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static ArrayList<Struct> endList = new ArrayList<>();
    private LinearLayoutManager endListManager;
    private static Adapter_Recycler endListAdapter;
    public static Dialog dialog_end;
    private TextView dialog_end_header;
    private EditText dialog_end_title;
    private TextView dialog_end_cancel;
    private TextView dialog_end_register;
    private int monthNumber = 1;
    private static TextView list_state;
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
    private String itemID;
    private String itemNAME;
    private MaterialSpinner month_spinner;
    private String[] months;
    public int monthPosition = 1;
    private String groupPercent;
    private String targetCount;

    public EndFragment() {
        // Required empty public constructor
    }

    public static EndFragment newInstance(String param1, String param2) {
        EndFragment fragment = new EndFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end, container, false);
        list_state = (TextView) view.findViewById(R.id.list_state);

        // Dialog
        dialog_end = new Dialog(getActivity());
        dialog_end.setContentView(R.layout.dialog_end);
        dialog_end_header = (TextView) dialog_end.findViewById(R.id.dialog_edt_header);
        dialog_end_title = (EditText) dialog_end.findViewById(R.id.dialog_edt_title);
        dialog_end_cancel = (TextView) dialog_end.findViewById(R.id.dialog_tv_cancel);
        dialog_end_register = (TextView) dialog_end.findViewById(R.id.dialog_tv_register);
        dialog_end_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemChecker("END_TABLE", monthNumber, dialog_end_title.getText().toString());
                if (canNotBeAdded == false) {
                    dialog_end.dismiss();
                    Random rand = new Random();
                    int Id = rand.nextInt((100000 - 1) + 1) + 1;
                    addToDatabase("END_TABLE", monthNumber, 0, dialog_end_title.getText().toString(), Id);
                } else {
                    Toast.makeText(getContext(), "این نام قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
                }
                canNotBeAdded = false;
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
        dialog_end_edit = new Dialog(getActivity());
        dialog_end_edit.setContentView(R.layout.dialog_end_edit);
        dialog_end_edit_header = (TextView) dialog_end_edit.findViewById(R.id.dialog_edt_header);
        dialog_end_edit_title = (EditText) dialog_end_edit.findViewById(R.id.dialog_edt_title);
        dialog_end_edit_cancel = (TextView) dialog_end_edit.findViewById(R.id.dialog_tv_cancel);
        dialog_end_edit_register = (TextView) dialog_end_edit.findViewById(R.id.dialog_tv_register);
        dialog_end_edit_Plus = (ImageView) dialog_end_edit.findViewById(R.id.item_plus);
        dialog_end_edit_Minus = (ImageView) dialog_end_edit.findViewById(R.id.item_minus);
        dialog_end_edit_Number = (EditText) dialog_end_edit.findViewById(R.id.item_number);
        dialog_end_edit_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number += 10;
                if (number > 99) {
                    number = 100;
                    dialog_end_edit_Number.setText(String.valueOf(number));
                } else {
                    dialog_end_edit_Number.setText(String.valueOf(number));
                }
            }
        });
        dialog_end_edit_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number -= 10;
                if (number < 1) {
                    number = 0;
                    dialog_end_edit_Number.setText(String.valueOf(number));
                } else {
                    dialog_end_edit_Number.setText(String.valueOf(number));
                }
            }
        });

        dialog_end_edit_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_end_edit.dismiss();
                int percent = Integer.parseInt(dialog_end_edit_Number.getText().toString());

                DataBaseEditor("END_TABLE", Integer.parseInt(itemID), percent);
//                addToDatabase("END_TABLE", monthNumber, 0, dialog_end_title.getText().toString());
                dialog_end_title.setText("");
            }
        });
        dialog_end_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_end_edit.dismiss();
                dialog_end_title.setText("");
            }
        });

        month_spinner = (MaterialSpinner) view.findViewById(R.id.month_spinner);
        months = new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
        month_spinner.setItems(months);

        month_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                monthPosition = position + 1;
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                new getTargetGroupAsync().execute(Urls.BASE_URL + Urls.GET_TARGET_GROUP,
                        sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), "1396", String.valueOf(monthPosition));

            }
        });

        RecyclerView localEventRecycler = (RecyclerView) view.findViewById(R.id.endListRecycler);
        endListManager = new GridLayoutManager(getContext(), 3);
        endListAdapter = new Adapter_Recycler(getContext(), endList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    itemID = endList.get(position).strId;
                    itemNAME = endList.get(position).strItemTitle;

                    Intent intent = new Intent(getActivity(), TargetActivity.class);
                    intent.putExtra("ID", itemID);
                    intent.putExtra("NAME", itemNAME);
                    intent.putExtra("MONTH", String.valueOf(monthPosition));
                    intent.putExtra("MONTH_NAME", months[monthPosition - 1]);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
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
        }, 6, false);
        localEventRecycler.setAdapter(endListAdapter);
        localEventRecycler.setLayoutManager(endListManager);
//        recyclerInitializer("END_TABLE", monthNumber);


//        endList.clear();
//        groupsArray = null;
//        try {
//            G.TARGET_GROUPS = G.TARGET_GROUPS_SP.getString("TARGET_GROUPS_SP", "x");
//            groupsArray = new JSONArray(G.TARGET_GROUPS);
//            for (int i = 0; i < groupsArray.length(); i++) {
//                JSONObject productObject = groupsArray.getJSONObject(i);
//                groupId = productObject.getString("groupId");
//                groupTitle = productObject.getString("groupTitle");
//                groupPercent = productObject.getString("completedPercent");
//
//                Struct struct = new Struct();
//                struct.strItemTitle = groupTitle;
//                struct.intPercent = Integer.parseInt(groupPercent);
//                struct.strItemText = String.valueOf(struct.intPercent) + " ٪ ";
//                struct.strId = groupId;
//
//                endList.add(struct);
//            }
//            endListAdapter.notifyDataSetChanged();
////            DataBaseChecker("PRODUCT_TABLE");
//        } catch (JSONException e) {
//
//        }
        month_spinner.setSelectedIndex(G.month - 1);
        monthPosition = G.month ;
        firstRequest();
        return view;
    }
    public void firstRequest() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        new getTargetGroupAsync().execute(Urls.BASE_URL + Urls.GET_TARGET_GROUP,
                sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), "1396", String.valueOf(monthPosition));
    }
    @Override
    public void onResume() {
        super.onResume();

        try {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
            new getTargetGroupAsync().execute(Urls.BASE_URL + Urls.GET_TARGET_GROUP,
                    sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), "1396", String.valueOf(monthPosition));
        }catch (Exception e){}

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static void addToDatabase(String TableName, int MONTH, int PERCENT, String TOPIC, int ID) {
        DataBaseWriter_End helper = new DataBaseWriter_End(G.CONTEXT, "END_DATABASE", TableName, G.ENDTableFieldNames, G.ENDTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    String.valueOf(MONTH),
                    String.valueOf(PERCENT),
                    TOPIC,
                    String.valueOf(ID)
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerInitializer("END_TABLE", MONTH);

    }

    private static void DataBaseChecker(String TableName, int monthNumber, int dayNumber) {
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "END_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();
        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"", null);
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
        DataBaseWriter_Event helper = new DataBaseWriter_Event(G.CONTEXT, "END_DATABASE", TableName, G.EVENTTableFieldNames, G.EVENTTableFieldTypes);
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

    private static void recyclerInitializer(String TableName, int monthNumber) {
        endList.clear();
        try {
            endListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
        DataBaseWriter_End helper = new DataBaseWriter_End(G.CONTEXT, "END_DATABASE", TableName, G.ENDTableFieldNames, G.ENDTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"", null);
        String month;
        String percent;
        String topic;
        String id;
        if (cursor.moveToFirst()) {
            do {
                month = cursor.getString(cursor.getColumnIndex("MONTH"));
                percent = cursor.getString(cursor.getColumnIndex("PERCENT"));
                topic = cursor.getString(cursor.getColumnIndex("TOPIC"));
                id = cursor.getString(cursor.getColumnIndex("ID"));


                Struct struct = new Struct();
                struct.strItemTitle = topic;
                struct.intPercent = Integer.parseInt(percent);
                struct.strItemText = String.valueOf(struct.intPercent) + " ٪ ";
                struct.strId = id;

                endList.add(struct);
            } while (cursor.moveToNext());
            try {
                endListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
        sqld.close();
        if (endList.size() < 1) {
            list_state.setVisibility(View.VISIBLE);
        } else {
            list_state.setVisibility(View.GONE);

        }
    }

    private static void itemChecker(String TableName, int monthNumber, String name) {

        DataBaseWriter_End helper = new DataBaseWriter_End(G.CONTEXT, "END_DATABASE", TableName, G.ENDTableFieldNames, G.ENDTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = helper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where MONTH = \"" + monthNumber + "\"", null);
        String month;
        String percent;
        String topic;
        String id;
        if (cursor.moveToFirst()) {
            do {
                month = cursor.getString(cursor.getColumnIndex("MONTH"));
                percent = cursor.getString(cursor.getColumnIndex("PERCENT"));
                topic = cursor.getString(cursor.getColumnIndex("TOPIC"));
                id = cursor.getString(cursor.getColumnIndex("ID"));


                if (topic.equals(name)) {
                    canNotBeAdded = true;
                }

            } while (cursor.moveToNext());
        }
        sqld.close();
    }

    public static void runDialog() {
        dialog_end.show();
    }

    private void DataBaseEditor(String TableName, int id, int percent) {
        DataBaseWriter_End helper = new DataBaseWriter_End(G.CONTEXT, "END_DATABASE", TableName, G.ENDTableFieldNames, G.ENDTableFieldTypes);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(G.ENDTableFieldNames[1], percent);

        db.update(TableName, values, "ID =" + "\"" + id + "\"", null);
        db.close();

        recyclerInitializer("END_TABLE", monthNumber);

    }

    private class getTargetGroupAsync extends Webservice.getTargetGroup {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
            endList.clear();
            groupsArray = null;
            try {
                groupsArray = new JSONArray(result);
                for (int i = 0; i < groupsArray.length(); i++) {
                    JSONObject productObject = groupsArray.getJSONObject(i);
                    groupId = productObject.getString("groupId");
                    groupTitle = productObject.getString("groupTitle");
                    groupPercent = productObject.getString("completedPercent");
                    targetCount = productObject.getString("targetCount");

                    Struct struct = new Struct();
                    struct.strItemTitle = groupTitle;
                    struct.intPercent = Integer.parseInt(groupPercent);
                    struct.strItemText = String.valueOf(struct.intPercent) + " ٪ ";
                    struct.intQuantity = Integer.parseInt(targetCount);
                    struct.strId = groupId;

                    endList.add(struct);
                }
                endListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {

            }

        }
    }

}
