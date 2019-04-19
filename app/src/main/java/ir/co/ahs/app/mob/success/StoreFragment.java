package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseWrite;
import ir.co.ahs.app.mob.success.Helper.DataBaseWriter_Product;
import ir.co.persiancalendar.core.G;

import static ir.co.persiancalendar.core.G.OrderTableFieldNames;


public class StoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager storeListManager;
    private static Adapter_Recycler storeListAdapter;
    private static ArrayList<Struct> storeList = new ArrayList<>();
    private static ArrayList<String> storeMenu = new ArrayList<>();
    private TabLayout tabLayout;

    private LinearLayout wheelSwitchOrange;

    public static Context context;
    public static Activity activity;
    public static String USER_NAME = "علی حسینی";
    private static Adapter_Recycler adapter_recycler;
    private int i2;
    public static AVLoadingIndicatorView loading;
    public static String groupId;
    private LinearLayout fadeLayer;
    private Animation fade_layer_in;
    private Animation fade_layer_out;
    private Dialog dialogAddToCart;
    private TextView dialogAddToCart_ProductName;
    private TextView dialogAddToCart_PreviousPrice;
    private TextView dialogAddToCart_CurrentPrice;
    private ImageView dialogAddToCart_ProductImage;
    private TextView dialogAddToCart_TotalPrice;
    private EditText dialogAddToCart_ProductComment;
    private TextView dialogAddToCart_BtnAdd;
    private ImageView dialogAddToCart_Plus;
    private ImageView dialogAddToCart_Minus;
    private EditText dialogAddToCart_Number;
    private int number = 1;
    private String ID;
    private String NAME;
    private String CURRENT_PRICE;
    private String PREVIOUS_PRICE;
    private String NUMBER;
    private String TEXT;
    private String IMAGE;
    private TextView dialogAddToCart_BtnCancel;
    private String COMMENT;
    private int itemPrice;
    private static String lastDate = "0000/00/00";
    private static String lastTime = "00:00:00";
    private Dialog dialog_finalize;
    private RelativeLayout btnCart;
    private TextView cartNo;
    private static String filepath;
    private int CREDIT;
    private Adapter_Recycler menuAdapter;
    private LinearLayoutManager menuManager;
    private float selected;
    private float itemSelected = 0;
    private TextView dialogAddToCart_Unit;
    private JSONArray groupsArray;
    private View view;
    private LinearLayoutManager storeMenuManager;
    private Adapter_Recycler storeMenuAdapter;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store, container, false);
        loading = (AVLoadingIndicatorView) view.findViewById(R.id.loading);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);


        // Dialog Add To Cart
        dialogAddToCart = new Dialog(getActivity());
        dialogAddToCart.setContentView(R.layout.dialog_add_product);
        dialogAddToCart.setCancelable(false);
        dialogAddToCart_ProductName = (TextView) dialogAddToCart.findViewById(R.id.product_name);
        dialogAddToCart_Unit = (TextView) dialogAddToCart.findViewById(R.id.measure);
        dialogAddToCart_PreviousPrice = (TextView) dialogAddToCart.findViewById(R.id.product_previous_price);
        dialogAddToCart_CurrentPrice = (TextView) dialogAddToCart.findViewById(R.id.product_current_price);
        dialogAddToCart_ProductImage = (ImageView) dialogAddToCart.findViewById(R.id.product_image);
        dialogAddToCart_TotalPrice = (TextView) dialogAddToCart.findViewById(R.id.price);
        dialogAddToCart_ProductComment = (EditText) dialogAddToCart.findViewById(R.id.edt_edit);
        dialogAddToCart_Plus = (ImageView) dialogAddToCart.findViewById(R.id.item_plus);
        dialogAddToCart_Minus = (ImageView) dialogAddToCart.findViewById(R.id.item_minus);
        dialogAddToCart_Number = (EditText) dialogAddToCart.findViewById(R.id.item_number);
        dialogAddToCart_BtnAdd = (TextView) dialogAddToCart.findViewById(R.id.btn_add);
        dialogAddToCart_BtnCancel = (TextView) dialogAddToCart.findViewById(R.id.btn_cancel);
        dialogAddToCart_Number.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEnteredVal = dialogAddToCart_Number.getText().toString();

                if (!strEnteredVal.equals("")) {
                    int num = Integer.parseInt(strEnteredVal);
                    if (num < 1) {
                        dialogAddToCart_Number.setText("1");
                        int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * itemPrice;
                        String convertedPrice = String.format("%,d", totalPrice);
                        dialogAddToCart_TotalPrice.setText(String.valueOf(convertedPrice));
                    } else {
                        int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * itemPrice;
                        String convertedPrice = String.format("%,d", totalPrice);
                        dialogAddToCart_TotalPrice.setText(String.valueOf(convertedPrice));
                    }
                }
            }

        });
        dialogAddToCart_BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPopUp();
                getProducts(groupId);
                dialogAddToCart.dismiss();
            }
        });
        dialogAddToCart_BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAddToCart_Number.getText().toString().equals("")) {
                    NUMBER = String.valueOf(1);
                } else {
                    NUMBER = dialogAddToCart_Number.getText().toString();
                }
                COMMENT = dialogAddToCart_ProductComment.getText().toString();
                addProductToDatabase("ORDER_TABLE", ID, NAME, CURRENT_PRICE, PREVIOUS_PRICE, NUMBER, COMMENT, IMAGE);
                getProducts(groupId);
                dialogAddToCart.dismiss();

//                cartNo.setText(String.valueOf(cartChecker("ORDER_TABLE")));
            }
        });
        dialogAddToCart_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number += 1;
                dialogAddToCart_Number.setText(String.valueOf(number));
                int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * Integer.parseInt(dialogAddToCart_CurrentPrice.getText().toString());
                dialogAddToCart_TotalPrice.setText(String.valueOf(totalPrice));
            }
        });
        dialogAddToCart_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number -= 1;
                dialogAddToCart_Number.setText(String.valueOf(number));
                int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * Integer.parseInt(dialogAddToCart_CurrentPrice.getText().toString());
                dialogAddToCart_TotalPrice.setText(String.valueOf(totalPrice));
            }
        });


        RecyclerView localEventRecycler = (RecyclerView) view.findViewById(R.id.storeListRecycler);
        storeListManager = new LinearLayoutManager(getContext());
        storeListAdapter = new Adapter_Recycler(getContext(), storeList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {

            }

            @Override
            public void onItemClick(int position) {
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {

                    dialogAddToCart.show();
                    dialogAddToCart_ProductComment.clearFocus();
                    dialogAddToCart_Number.clearFocus();
                    itemPrice = storeList.get(position).intCurrentPrice;
                    dialogAddToCart_ProductName.setText(storeList.get(position).strName);
                    dialogAddToCart_PreviousPrice.setText(String.valueOf(storeList.get(position).intPreviousPrice));
                    dialogAddToCart_CurrentPrice.setText(String.valueOf(storeList.get(position).intCurrentPrice));
                    dialogAddToCart_TotalPrice.setText(String.valueOf(storeList.get(position).intCurrentPrice));
                    Glide.with(G.CONTEXT).load(storeList.get(position).strImg).placeholder(R.drawable.logo).into(dialogAddToCart_ProductImage);
                    dialogAddToCart_Unit.setText(storeList.get(position).strUnitType);


                    ID = storeList.get(position).strId;
                    NAME = storeList.get(position).strName;
                    CURRENT_PRICE = String.valueOf(storeList.get(position).intCurrentPrice);
                    PREVIOUS_PRICE = String.valueOf(storeList.get(position).intPreviousPrice);
                    NUMBER = String.valueOf(1);
                    TEXT = storeList.get(position).strText;
                    COMMENT = storeList.get(position).strComment;
                    IMAGE = storeList.get(position).strImg;

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
        }, 5, false);
        localEventRecycler.setAdapter(storeListAdapter);
        localEventRecycler.setLayoutManager(storeListManager);


        // get group ids from server
        groupsArray = null;
        try {
            G.PRODUCT_GROUPS = G.GROUPS.getString("GROUPS", "x");
            groupsArray = new JSONArray(G.PRODUCT_GROUPS);
            for (int i = 0; i < groupsArray.length(); i++) {
                JSONObject productObject = groupsArray.getJSONObject(i);
                boolean tabDeleted = productObject.getBoolean("deleted");
                if (!tabDeleted) {
                    groupId = productObject.getString("groupId");
                    DataBaseChecker("PRODUCT_TABLE");
                    break;
                } else {

                }
            }

        } catch (JSONException e) {

        }

        try {
            for (int i = 0; i < groupsArray.length(); i++) {
                Struct struct = new Struct();
                JSONObject productObject = groupsArray.getJSONObject(i);
                String tabId = productObject.getString("groupId");
                String tabName = productObject.getString("groupName");
                boolean tabDeleted = productObject.getBoolean("deleted");
                if (!tabDeleted) {
                    storeMenu.add(tabId);
                    tabLayout.addTab(tabLayout.newTab().setText(tabName));
                }


            }

        } catch (JSONException e) {

        }catch (Exception e) {

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                JSONObject productObject = null;

                groupId = storeMenu.get(tab.getPosition());
                DataBaseChecker("PRODUCT_TABLE");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
//        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static void DataBaseChecker(String TableName) {
        DataBaseWriter_Product dataBaseHelper = new DataBaseWriter_Product(G.CONTEXT, "PRODUCT_DATABASE", TableName, G.ProductTableFieldNames, G.ProductTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where GROUP_ID = \"" + groupId + "\"", null);
        String date;
        String time;
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(cursor.getColumnIndex("DATE"));
                time = cursor.getString(cursor.getColumnIndex("TIME"));
//                lastDate = date;
//                lastTime = time;
            } while (cursor.moveToNext());
        }
        sqld.close();
        getProducts(groupId);

    }

    public static void getProducts(String groupId) {
        if (G.isNetworkAvailable()) {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
            new AsyncProducts().execute(Urls.BASE_URL + Urls.GET_PRODUCT, sessionId, userToken, groupId, lastDate, lastTime);

        } else {
            recyclerInitializer("PRODUCT_TABLE");
        }
    }

    public static class AsyncProducts extends Webservice.getProducts {
        private String productPhoto;

        @Override
        protected void onPreExecute() {
            loading.smoothToShow();
            loading.setVisibility(View.VISIBLE);
            storeList.clear();
        }

        @Override
        protected void onPostExecute(String result) {
            loading.smoothToHide();
            storeList.clear();
            try {
                JSONArray productArray = new JSONArray(result);
                for (int i = 0; i < productArray.length(); i++) {

                    JSONObject productObject = productArray.getJSONObject(i);
                    String productID = productObject.getString("productId");
                    String productName = productObject.getString("productName");
                    String productCode = productObject.getString("productCode");
                    String productDetail = productObject.getString("productDetail");
                    productPhoto = productObject.getString("productPhoto");
                    String groupId = productObject.getString("groupId");
                    String groupName = productObject.getString("groupName");
                    String measureUnitName = productObject.getString("measureUnitName");
                    String currencyName = productObject.getString("currencyName");
                    int priceCurrent = productObject.getInt("priceCurrent");
                    int priceBefore = productObject.getInt("priceBefore");
//                    int priceRepresentative = productObject.getInt("priceRepresentative");
                    String theDate = productObject.getString("theDate");
                    String theTime = productObject.getString("theTime");
                    Boolean isDeleted = productObject.getBoolean("deleted");

//                    saveImageToSDCard(productPhoto);
//                    DataBaseItemDelete("ORDER_TABLE", 0, productID);


//                    Struct struct = new Struct();
//                    struct.strId = productID;
//                    struct.strItemTitle = productName;
//                    struct.strImg = productPhoto;
//                    struct.strItemText = productDetail;
//                    struct.intCurrentPrice = priceCurrent;
//                    struct.intPreviousPrice = priceCurrent;
//                    struct.strCurrencyName = currencyName;
//                    struct.strUnitType = measureUnitName;
//                    storeList.add(struct);

                    if (!isDeleted) {

                        Struct struct = new Struct();
                        struct.strId = productID;
                        struct.strItemTitle = productName;
                        struct.strImg = productPhoto;
                        struct.strItemText = productDetail;
                        struct.intCurrentPrice = priceCurrent;
                        struct.intPreviousPrice = priceBefore;
                        struct.strCurrencyName = currencyName;
                        struct.strUnitType = measureUnitName;
                        storeList.add(struct);


//                struct.strSize = "10mm";
//                struct.strMaterial = "مقوا";
//                struct.strCount = "20";
//                struct.strCountInBox = "100";
                        String CURRENT_PRICE = String.valueOf(priceCurrent);
                        String PREVIOUS_PRICE = String.valueOf(priceBefore);

                        addToDatabase("PRODUCT_TABLE", groupId, productID, productName, CURRENT_PRICE, PREVIOUS_PRICE, "0", productDetail, productPhoto, theDate, theTime, measureUnitName);
                    } else {
                        DataBaseItemDelete("PRODUCT_TABLE", 0, productID);

                    }
                }

//                recyclerInitializer("PRODUCT_TABLE");
            } catch (Exception e) {

            }
            storeListAdapter.notifyDataSetChanged();
        }
    }

    private static void DataBaseItemDelete(String TableName, int quantity, String id) {
        DataBaseWriter_Product dataBaseHelper = new DataBaseWriter_Product(G.CONTEXT, "PRODUCT_DATABASE", "PRODUCT_TABLE", G.ProductTableFieldNames, G.ProductTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        db.delete(TableName, "ID =" + "\"" + id + "\"", null);
//        db.close();
        db.close();
    }

    private static void addToDatabase(String TableName, String GROUP_ID, String ID, String NAME, String CURRENT_PRICE, String PREVIOUS_PRICE, String NUMBER, String TEXT, String IMAGE, String DATE, String TIME, String measureUnitName) {
        DataBaseWriter_Product helper = new DataBaseWriter_Product(G.CONTEXT, "PRODUCT_DATABASE", TableName, G.ProductTableFieldNames, G.ProductTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    GROUP_ID,
                    ID,
                    NAME,
                    CURRENT_PRICE,
                    PREVIOUS_PRICE,
                    NUMBER,
                    TEXT,
                    IMAGE,
                    DATE,
                    TIME,
                    measureUnitName
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPopUp() {
        number = 1;
        COMMENT = "";
        dialogAddToCart_Number.setText(String.valueOf(number));
        dialogAddToCart_ProductComment.setText("");
    }

    private int cartChecker(String TableName) {
        int counter = 0;
        DataBaseWrite dataBaseHelper = new DataBaseWrite(G.CONTEXT, "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);

        if (cursor.moveToFirst()) {
            do {
                counter += 1;

            } while (cursor.moveToNext());
        }

        sqld.close();

        return counter;
    }

    private static void recyclerInitializer(String TableName) {
        storeList.clear();
        try {
            adapter_recycler.notifyDataSetChanged();
        } catch (Exception e) {
        }
        DataBaseWriter_Product dataBaseHelper = new DataBaseWriter_Product(activity.getBaseContext(), "PRODUCT_DATABASE", TableName, G.ProductTableFieldNames, G.ProductTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName + " where GROUP_ID = \"" + groupId + "\"", null);
        String id;
        String name;
        int price_1;
        int price_2;
        int quantity;
        String comment;
        String image;
        String date;
        String time;
        String unit;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex("ID"));
                name = cursor.getString(cursor.getColumnIndex("NAME"));
                price_1 = cursor.getInt(cursor.getColumnIndex("CURRENT_PRICE"));
                price_2 = cursor.getInt(cursor.getColumnIndex("PREVIOUS_PRICE"));
                quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                comment = cursor.getString(cursor.getColumnIndex("COMMENT"));
                image = cursor.getString(cursor.getColumnIndex("IMAGE"));
                date = cursor.getString(cursor.getColumnIndex("DATE"));
                time = cursor.getString(cursor.getColumnIndex("TIME"));
                unit = cursor.getString(cursor.getColumnIndex("UNIT"));


                Struct struct = new Struct();
                struct.strId = id;
                struct.strItemTitle = name;
                struct.strImg = image;
                struct.strItemText = comment;
                struct.intCurrentPrice = price_1;
                struct.intPreviousPrice = price_2;
                struct.strCurrencyName = "ریال";
                struct.strUnitType = unit;
                storeList.add(struct);
            } while (cursor.moveToNext());
            try {
                storeListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
        sqld.close();
    }

    private void addProductToDatabase(String TableName, String ID, String NAME, String CURRENT_PRICE, String PREVIOUS_PRICE, String NUMBER, String TEXT, String IMAGE) {
        DataBaseWrite helper = new DataBaseWrite(getContext(), "ORDER_DATABASE", TableName, G.OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            helper.sampleMethod(new String[]{
                    ID,
                    NAME,
                    CURRENT_PRICE,
                    PREVIOUS_PRICE,
                    NUMBER,
                    TEXT,
                    IMAGE

            });

            Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), "با موفقیت به سبد خرید اضافه شد", Snackbar.LENGTH_LONG);
            snackbar.show();

            resetPopUp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
