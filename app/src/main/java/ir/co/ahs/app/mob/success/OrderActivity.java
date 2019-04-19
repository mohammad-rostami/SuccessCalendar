package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseWrite;
import ir.co.persiancalendar.core.G;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static ir.co.persiancalendar.core.G.OrderTableFieldNames;

/**
 * Created by imac on 2/25/18.
 */

public class OrderActivity extends Activity{

    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private String ProductId;
    private String ProductName;
    private String ProductCurrentPrice;
    private String ProductPreviousPrice;
    private Adapter_Recycler adapter_recycler;
    private TextView cartState;
    private Dialog dialogSuccess;
    private TextView dialogSuccess_txt_memberCode;
    private TextView dialogSuccess_btn_register;
    private TextView submit;
    private TextView creditPay;
    private TextView price;
    int summation = 0;
    private FrameLayout totalPrice;

    private Dialog dialogAddToCart;
    private TextView dialogAddToCart_ProductName;
    private TextView dialogAddToCart_PreviousPrice;
    private TextView dialogAddToCart_CurrentPrice;
    private ImageView dialogAddToCart_ProductImage;
    private TextView dialogAddToCart_TotalPrice;
    private EditText dialogAddToCart_ProductComment;
    private TextView dialogAddToCart_Number;
    private int number = 1;
    private String ID;
    private String NAME;
    private String CURRENT_PRICE;
    private String PREVIOUS_PRICE;
    private String NUMBER;
    private String TEXT;
    private String IMAGE;
    private String COMMENT;
    private int itemPrice;
    private String selectedId;
    private String selectedComment;
    private Dialog dialog_confirm;
    private TextView purchase_price;
    private TextView dialog_register;
    private Dialog dialog_confirm2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Toolbar
        ImageView btnNavigation = findViewById(R.id.btnNavigation);
        ImageView btnHistory = findViewById(R.id.history);
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(OrderActivity.this, ActivityHistory.class);
//                OrderActivity.this.startActivity(intent);
            }
        });

        totalPrice = findViewById(R.id.total_price);
        cartState = findViewById(R.id.cart_state);
        creditPay = findViewById(R.id.credit_pay);
        submit = findViewById(R.id.submit);
        price = findViewById(R.id.price);


        creditPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (cartState.getVisibility() == View.GONE) {
//                    dialog_credit.show();
//                    final int CREDIT = G.CREDIT.getInt("CREDIT", 0);
//                    final int Price = Integer.parseInt(priceCalculator("ORDER_TABLE",
//                            OrderTableFieldNames[0],
//                            OrderTableFieldNames[1],
//                            OrderTableFieldNames[2],
//                            OrderTableFieldNames[3],
//                            OrderTableFieldNames[4],
//                            OrderTableFieldNames[5]));
//
//                    if (Price > (CREDIT / 10)) {
//                        dialog_register.setTextColor(getResources().getColor(R.color.brightGray));
//                        dialog_register.setEnabled(false);
//                    } else {
//                        dialog_register.setTextColor(getResources().getColor(R.color.black));
//                        dialog_register.setEnabled(true);
//                    }
//                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.show();
//                if (cartState.getVisibility() == View.GONE) {
//                    makeJsonFromOrderData("ORDER_TABLE",
//                            OrderTableFieldNames[0],
//                            OrderTableFieldNames[1],
//                            OrderTableFieldNames[2],
//                            OrderTableFieldNames[3],
//                            OrderTableFieldNames[4],
//                            OrderTableFieldNames[5]);
//                }
            }
        });

        // Dialog Add To Cart
        dialogAddToCart = new Dialog(OrderActivity.this);
        dialogAddToCart.setContentView(R.layout.dialog_add_product);
        dialogAddToCart.setCancelable(true);
        dialogAddToCart_ProductName = dialogAddToCart.findViewById(R.id.product_name);
        dialogAddToCart_PreviousPrice = dialogAddToCart.findViewById(R.id.product_previous_price);
        dialogAddToCart_CurrentPrice = dialogAddToCart.findViewById(R.id.product_current_price);
        dialogAddToCart_ProductImage = dialogAddToCart.findViewById(R.id.product_image);
        dialogAddToCart_TotalPrice = dialogAddToCart.findViewById(R.id.price);
        dialogAddToCart_ProductComment = dialogAddToCart.findViewById(R.id.edt_edit);
        ImageView dialogAddToCart_Plus = dialogAddToCart.findViewById(R.id.item_plus);
        final ImageView dialogAddToCart_Minus = dialogAddToCart.findViewById(R.id.item_minus);
        dialogAddToCart_Number = dialogAddToCart.findViewById(R.id.item_number);
        TextView dialogAddToCart_BtnAdd = dialogAddToCart.findViewById(R.id.btn_add);
        dialogAddToCart_BtnAdd.setText("ویرایش");
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
        TextView dialogAddToCart_BtnCancel = dialogAddToCart.findViewById(R.id.btn_cancel);
        dialogAddToCart_BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                DataBaseEditor("ORDER_TABLE", Integer.parseInt(NUMBER), selectedId, COMMENT);
                DataBaseChecker("ORDER_TABLE",
                        OrderTableFieldNames[0],
                        OrderTableFieldNames[1],
                        OrderTableFieldNames[2],
                        OrderTableFieldNames[3],
                        OrderTableFieldNames[4],
                        OrderTableFieldNames[5],
                        OrderTableFieldNames[6]);

                dialogAddToCart.dismiss();

            }
        });
        dialogAddToCart_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number += 1;
                dialogAddToCart_Number.setText(String.valueOf(number));
                int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * itemPrice;
                dialogAddToCart_TotalPrice.setText(String.valueOf(totalPrice));

                if (number > 1) {
                    dialogAddToCart_Minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.black));
                }

            }
        });
        dialogAddToCart_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number == 1) {
                } else {
                    number -= 1;
                    dialogAddToCart_Number.setText(String.valueOf(number));
                    int totalPrice = Integer.parseInt(dialogAddToCart_Number.getText().toString()) * itemPrice;
                    dialogAddToCart_TotalPrice.setText(String.valueOf(totalPrice));
                    if (number == 1) {
                        dialogAddToCart_Minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.brightGray));
                    }
                }
            }
        });

        // Dialog
        dialogSuccess = new Dialog(OrderActivity.this);
        dialogSuccess.setContentView(R.layout.dialog_order_code);
        dialogSuccess.setCancelable(true);
//        dialogSuccess.setTitle("Custom Dialog");
        dialogSuccess_txt_memberCode = (TextView) dialogSuccess.findViewById(R.id.memberCode);
        dialogSuccess_btn_register = (TextView) dialogSuccess.findViewById(R.id.register);
        dialogSuccess_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.dismiss();

            }
        });

        // Dialog confirm
        dialog_confirm = new Dialog(OrderActivity.this);
        dialog_confirm.setContentView(R.layout.dialog_confirm);
        TextView dialog_confirm_cancel = (TextView) dialog_confirm.findViewById(R.id.cancel);
        TextView dialog_confirm_register = (TextView) dialog_confirm.findViewById(R.id.register);

        dialog_confirm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (cartState.getVisibility() == View.GONE) {
//                    makeJsonFromOrderData("ORDER_TABLE",
//                            OrderTableFieldNames[0],
//                            OrderTableFieldNames[1],
//                            OrderTableFieldNames[2],
//                            OrderTableFieldNames[3],
//                            OrderTableFieldNames[4],
//                            OrderTableFieldNames[5]);
//                }
//                dialog_confirm.dismiss();
            }
        });
        dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });


        // Dialog confirm
        dialog_confirm2 = new Dialog(OrderActivity.this);
        dialog_confirm2.setContentView(R.layout.dialog_confirm2);
        TextView dialog_confirm_cancel2 = (TextView) dialog_confirm2.findViewById(R.id.cancel);
        TextView dialog_confirm_register2 = (TextView) dialog_confirm2.findViewById(R.id.register);

        dialog_confirm_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new OrderActivity.AsyncBuy().execute(Urls.BASE_URL + Urls.BUY, sessionId, userToken, customerId, "1", "1", "1");


                Toast.makeText(OrderActivity.this, "پرداخت انجام شد", Toast.LENGTH_SHORT).show();
                dialog_confirm2.dismiss();
            }
        });
        dialog_confirm_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm2.dismiss();
            }
        });


        // Dialog
        dialog_credit = new Dialog(OrderActivity.this);
        dialog_credit.setContentView(R.layout.dialog_credit);
        dialog_credit.setTitle("Custom Dialog");
        TextView cancel = (TextView) dialog_credit.findViewById(R.id.cancel);
        dialog_register = (TextView) dialog_credit.findViewById(R.id.register);
        purchase_price = (TextView) dialog_credit.findViewById(R.id.purchase_price);
        TextView your_credit = (TextView) dialog_credit.findViewById(R.id.your_credit);
        int CREDITs = G.CREDIT.getInt("CREDIT", 0);
        String convertedCredit = String.format("%,d", CREDITs / 10);
        your_credit.setText(convertedCredit);

        dialog_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_credit.dismiss();
                dialog_confirm2.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_credit.dismiss();
            }
        });

        DataBaseChecker("ORDER_TABLE",
                OrderTableFieldNames[0],
                OrderTableFieldNames[1],
                OrderTableFieldNames[2],
                OrderTableFieldNames[3],
                OrderTableFieldNames[4],
                OrderTableFieldNames[5],
                OrderTableFieldNames[6]);

        RecyclerView list = (RecyclerView) findViewById(R.id.orderList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {
                arrayList.get(position).intQuantity += 1;
                int quantity = arrayList.get(position).intQuantity;
                String id = arrayList.get(position).strId;
                String comment = arrayList.get(position).strComment;
                DataBaseEditor("ORDER_TABLE", quantity, id, comment);
                String convertedPrice = String.format("%,d", Integer.parseInt(priceCalculator
                        ("ORDER_TABLE",
                                OrderTableFieldNames[0],
                                OrderTableFieldNames[1],
                                OrderTableFieldNames[2],
                                OrderTableFieldNames[3],
                                OrderTableFieldNames[4],
                                OrderTableFieldNames[5])));

                price.setText(convertedPrice);
                purchase_price.setText(convertedPrice);

            }

            @Override
            public void onItemClick(int position) {
                arrayList.get(position).intQuantity -= 1;
                int quantity = arrayList.get(position).intQuantity;
                String id = arrayList.get(position).strId;
                String comment = arrayList.get(position).strComment;
                DataBaseEditor("ORDER_TABLE", quantity, id, comment);
                String convertedPrice = String.format("%,d", Integer.parseInt(priceCalculator
                        ("ORDER_TABLE",
                                OrderTableFieldNames[0],
                                OrderTableFieldNames[1],
                                OrderTableFieldNames[2],
                                OrderTableFieldNames[3],
                                OrderTableFieldNames[4],
                                OrderTableFieldNames[5])));

                price.setText(convertedPrice);
                purchase_price.setText(convertedPrice);

            }

            @Override
            public void onItemDelete(int position) {
                arrayList.get(position).intQuantity -= 1;
                int quantity = arrayList.get(position).intQuantity;
                String id = arrayList.get(position).strId;
                DataBaseItemDelete("ORDER_TABLE", quantity, id);
            }

            @Override
            public void onCardClick(int position) {
                dialogAddToCart.show();
                dialogAddToCart_ProductComment.setText(arrayList.get(position).strComment);
                dialogAddToCart_ProductName.setText(arrayList.get(position).strName);
                itemPrice = arrayList.get(position).intCurrentPrice;
                String convertedPrice = String.format("%,d", itemPrice);
                String convertedPrice1 = String.format("%,d", arrayList.get(position).intPreviousPrice);
                dialogAddToCart_CurrentPrice.setText(convertedPrice);
                dialogAddToCart_PreviousPrice.setText(convertedPrice1);
                dialogAddToCart_Number.setText(String.valueOf(arrayList.get(position).intQuantity));
                String totalPrice = String.format("%,d", itemPrice * arrayList.get(position).intQuantity);
                dialogAddToCart_TotalPrice.setText(totalPrice);
                number = arrayList.get(position).intQuantity;
                Glide.with(G.CONTEXT).load(arrayList.get(position).strImg).into(dialogAddToCart_ProductImage);

                selectedId = arrayList.get(position).strId;
                selectedComment = arrayList.get(position).strComment;

                if (arrayList.get(position).intQuantity == 1) {
                    dialogAddToCart_Minus.setColorFilter(G.CONTEXT.getResources().getColor(R.color.brightGray));
                }
            }

            @Override
            public void onEdit(int position) {
                int quantity = Integer.parseInt(dialogAddToCart_Number.getText().toString());
                String id = arrayList.get(position).strId;
                String comment = arrayList.get(position).strComment;
                DataBaseEditor("ORDER_TABLE", quantity, id, comment);
                String convertedPrice = String.format("%,d", Integer.parseInt(priceCalculator
                        ("ORDER_TABLE",
                                OrderTableFieldNames[0],
                                OrderTableFieldNames[1],
                                OrderTableFieldNames[2],
                                OrderTableFieldNames[3],
                                OrderTableFieldNames[4],
                                OrderTableFieldNames[5])));

                price.setText(convertedPrice);
                purchase_price.setText(convertedPrice);

            }
        }, 8, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);
    }

    private String DataBaseChecker(String TableName, String ID, String NAME, String CURRENT_PRICE, String PREVIOUS_PRICE, String QUANTITY, String COMMENT, String IMAGE) {
        arrayList.clear();
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        String id = null;
        summation = 0;
        String name = null;
        int price_1;
        int price_2;
        int quantity;
        String comment = null;
        String image = null;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(ID));
                name = cursor.getString(cursor.getColumnIndex(NAME));
                price_1 = cursor.getInt(cursor.getColumnIndex(CURRENT_PRICE));
                price_2 = cursor.getInt(cursor.getColumnIndex(PREVIOUS_PRICE));
                quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                comment = cursor.getString(cursor.getColumnIndex(COMMENT));
                image = cursor.getString(cursor.getColumnIndex(IMAGE));

                Log.d("table", id + " " + name + " " + String.valueOf(price_1) + " " +
                        String.valueOf(price_2) + " " + String.valueOf(quantity) + "" + comment + image);

                Struct struct = new Struct();
                struct.strId = id;
                struct.strName = name;
                struct.strImg = image;
                struct.intCurrentPrice = price_1;
                struct.intPreviousPrice = price_2;
                struct.intQuantity = quantity;
                struct.strComment = comment;
                summation += price_1 * quantity;
                arrayList.add(struct);
            } while (cursor.moveToNext());
        }
        String convertedPrice = String.format("%,d", summation);
        try {
            adapter_recycler.notifyDataSetChanged();
        } catch (Exception e) {
        }
        price.setText(convertedPrice);
        purchase_price.setText(convertedPrice);
        cartStateCheck();
        sqld.close();

        return id;
    }

    private void DataBaseEditor(String TableName, int quantity, String id, String comment) {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderTableFieldNames[4], quantity);
        values.put(OrderTableFieldNames[5], comment);

        db.update(TableName, values, "ID =" + "\"" + id + "\"", null);
        db.close();
    }

    private void DataBaseItemDelete(String TableName, int quantity, String id) {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        db.delete(TableName, "ID =" + "\"" + id + "\"", null);
//        db.close();
//

        DataBaseChecker("ORDER_TABLE",
                OrderTableFieldNames[0],
                OrderTableFieldNames[1],
                OrderTableFieldNames[2],
                OrderTableFieldNames[3],
                OrderTableFieldNames[4],
                OrderTableFieldNames[5],
                OrderTableFieldNames[6]);

//        getProducts(groupId);

        db.close();
    }

    private void makeJsonFromOrderData(String TableName, String TITLE, String TEXT, String PHOTO, String DATE, String TIME, String ID) {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        String id = null;
        String name = null;
        int price_1;
        int price_2;
        int quantity;
        String comment = null;
        if (cursor.moveToFirst()) {
            JSONArray all = new JSONArray();
            do {
                id = cursor.getString(cursor.getColumnIndex(TITLE));
                name = cursor.getString(cursor.getColumnIndex(TEXT));
                price_1 = cursor.getInt(cursor.getColumnIndex(PHOTO));
                price_2 = cursor.getInt(cursor.getColumnIndex(DATE));
                quantity = cursor.getInt(cursor.getColumnIndex(TIME));
                comment = cursor.getString(cursor.getColumnIndex(ID));

                try {
                    JSONObject x = new JSONObject();
                    x.put("productID", id);
                    x.put("quantity", quantity);
                    x.put("price", price_1);
//                    x.put("comment", comment);
                    all.put(x);

                } catch (JSONException e) {

                }

            } while (cursor.moveToNext());
            Log.d("x", String.valueOf(all));

            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
            String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
            new OrderActivity.AsyncOrders().execute(Urls.BASE_URL + Urls.SEND_ORDER, sessionId, userToken, customerId, String.valueOf(all), "no comment");

        }
        sqld.close();

    }

    private class AsyncOrders extends Webservice.sendOrders {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
//            Log.d("result", result);
            try {
                JSONObject mainData = new JSONObject(result);
                String HeaderID = mainData.getString("HeaderID");
                String OrderNumber = mainData.getString("orderNumber");
                String OrderTime = mainData.getString("theTime");
                String OrderDate = mainData.getString("theDate");
                int Statuse = mainData.getInt("theStatus");
                JSONArray products = mainData.getJSONArray("orderItemList");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    String ItemID = product.getString("ItemID");
                    String ProductID = product.getString("ProductID");
                    String ProductName = product.getString("ProductName");
                    int ProductPrice = product.getInt("ProductPrice");
                    int ProductCount = product.getInt("ProductCount");
                }

//                DataBaseWrite helper = new DataBaseWrite(getBaseContext(), "HISTORY_DATABASE", "HISTORY_TABLE", G.HistoryTableFieldNames, G.HistoryTableFieldTypes);
//                try {
//                    helper.sampleMethod(new String[]{
//                            result,
//                            OrderDate,
//                            "0",
//                            "0",
//                            "0",
//                            OrderTime,
//                            "0"
//                    });


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearDateBase("ORDER_TABLE");

                    }
                }, 500);

                dialogSuccess_txt_memberCode.setText(OrderNumber);
                dialogSuccess.show();


//                getProducts(groupId);


//                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "سفارش با موفقیت ثبت شد!", Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "خطا در ثبت سفارش", Snackbar.LENGTH_LONG);
                snackbar.show();

            }


        }
    }

    private class AsyncBuy extends Webservice.Buy {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {

        }
    }

    public void clearDateBase(String TableName) {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        sqld.execSQL("delete from " + TableName);

        arrayList.clear();
        adapter_recycler.notifyDataSetChanged();
        cartStateCheck();

//        Cursor cursor = sqld.rawQuery("TRUNCATE TABLE " + TableName, null);
    }

    private String priceCalculator(String TableName, String TITLE, String TEXT, String PHOTO, String DATE, String TIME, String ID) {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getBaseContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, G.OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        int price_1;
        int price_2;
        int quantity;
        summation = 0;
        if (cursor.moveToFirst()) {
            do {
                price_1 = cursor.getInt(cursor.getColumnIndex(PHOTO));
                price_2 = cursor.getInt(cursor.getColumnIndex(DATE));
                quantity = cursor.getInt(cursor.getColumnIndex(TIME));

                summation += price_1 * quantity;
            } while (cursor.moveToNext());

        }
        sqld.close();

        return String.valueOf(summation);
    }

    public void cartStateCheck() {
        System.out.println(String.valueOf(arrayList.size()));
        if (arrayList.size() == 0) {
            cartState.setVisibility(View.VISIBLE);
            submit.setBackground(getResources().getDrawable(R.drawable.bg_gray_round_button));
            creditPay.setBackground(getResources().getDrawable(R.drawable.bg_gray_round_button));
            totalPrice.setVisibility(View.GONE);
        } else {
            cartState.setVisibility(View.GONE);
            submit.setBackground(getResources().getDrawable(R.drawable.bg_pink_round_button));
            creditPay.setBackground(getResources().getDrawable(R.drawable.bg_dark_round_button));
            totalPrice.setVisibility(View.VISIBLE);

        }
    }
}