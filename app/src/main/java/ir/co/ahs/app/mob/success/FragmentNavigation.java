package ir.co.ahs.app.mob.success;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.io.IOException;

import ir.co.ahs.app.mob.success.Helper.DataBaseWrite;
import ir.co.persiancalendar.core.G;


//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

public class FragmentNavigation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String mStr_packages;
    private Dialog dialog;
    private LinearLayout mLinearLayout_Main;
    private static TextView Premium;
    private TextView mTextView_ProShot;
    private String[] OrderTableFieldNames = new String[]{"ID", "NAME", "CURRENT_PRICE", "PREVIOUS_PRICE", "QUANTITY", "COMMENT"};
    private String[] OrderTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT", "TEXT"};
    private String[] HistoryTableFieldNames = new String[]{"JSON", "DATE", "Empty1", "Empty2", "Empty3", "TIME"};
    private String[] HistoryTableFieldTypes = new String[]{"TEXT", "TEXT", "INT", "INT", "INT", "TEXT"};
    private Dialog dialog_confirm;
    public static TextView S_TEXTVIEW_CREDIT;


    public FragmentNavigation() {
        // Required empty public constructor
    }

    public static FragmentNavigation newInstance(String param1, String param2) {
        FragmentNavigation fragment = new FragmentNavigation();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        // Dialog confirm
        dialog_confirm = new Dialog(getActivity());
        dialog_confirm.setContentView(R.layout.dialog_confirm);
        TextView dialog_confirm_text = (TextView) dialog_confirm.findViewById(R.id.dialog_txt);
        dialog_confirm_text.setText("آیا از خروج خود اطمینان دارد؟");
        TextView dialog_confirm_cancel = (TextView) dialog_confirm.findViewById(R.id.cancel);
        TextView dialog_confirm_register = (TextView) dialog_confirm.findViewById(R.id.register);

        dialog_confirm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                new AsyncLogout().execute(Urls.BASE_URL + "/Logout", sessionId, userToken, customerId);

                G.IS_REGISTERED.edit().putBoolean("IS_REGISTERED", false).apply();
                G.CUSTOMER_NAME.edit().remove("CUSTOMER_NAME").apply();
                G.CUSTOMER_ID.edit().remove("CUSTOMER_ID").apply();

                try {
                    clearDateBase();
                } catch (Exception e) {
                }

                G.FIRST_TIME_REQUEST.edit().putBoolean("FIRST_TIME_CONNECTION", false).apply();
                G.AUTHENTICATIONS_SESSION.edit().remove("SESSION").apply();
                G.AUTHENTICATIONS_TOKEN.edit().remove("TOKEN").apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
//                ActivityMain.finisher();
                dialog_confirm.dismiss();
            }
        });
        dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });


        Switch notification_switch = (Switch) view.findViewById(R.id.notification_switch);

        if (G.SERVICE_STATE.getBoolean("SERVICE_STATE", true)) {
        notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);

        }

        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!NotificationService.IS_SERVICE_RUNNING) {
                        Intent j = new Intent(getContext(), NotificationService.class);
                        getContext().startService(j);
                        NotificationService.IS_SERVICE_RUNNING = true;
                        G.SERVICE_STATE.edit().putBoolean("SERVICE_STATE", true).apply();

                    }
                } else {
                    Intent j = new Intent(getContext(), NotificationService.class);
                    getContext().stopService(j);
                    NotificationService.IS_SERVICE_RUNNING = false;
                    G.SERVICE_STATE.edit().putBoolean("SERVICE_STATE", false).apply();

                }
            }
        });

        LinearLayout mLinearLayout_Order = (LinearLayout) view.findViewById(R.id.fragment_nav_item_order);
        LinearLayout mLinearLayout_Credit = (LinearLayout) view.findViewById(R.id.fragment_nav_item_credit);
        LinearLayout mLinearLayout_Poll = (LinearLayout) view.findViewById(R.id.fragment_nav_item_poll);
        LinearLayout mLinearLayout_Messages = (LinearLayout) view.findViewById(R.id.fragment_nav_item_messages);
        LinearLayout mLinearLayout_ContactUs = (LinearLayout) view.findViewById(R.id.fragment_nav_item_contact_us);
        LinearLayout mLinearLayout_Profile = (LinearLayout) view.findViewById(R.id.fragment_nav_ll_item_profile);
        LinearLayout mLinearLayout_Test = (LinearLayout) view.findViewById(R.id.fragment_nav_item_test);
        LinearLayout mLinearLayout_SuccessMessage = (LinearLayout) view.findViewById(R.id.fragment_nav_item_success_message);
        LinearLayout mLinearLayout_Story = (LinearLayout) view.findViewById(R.id.fragment_nav_item_story);
        LinearLayout mLinearLayout_Meditation = (LinearLayout) view.findViewById(R.id.fragment_nav_item_meditation);
        mLinearLayout_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), CreditActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), PollActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {

                    Intent intent = new Intent(getActivity(), ConversationActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mLinearLayout_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });
        mLinearLayout_SuccessMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), SuccessMessageListActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });

        mLinearLayout_Story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Async().execute("https://mpl.pec.ir/api/EShop/GetSaleTokenNew");

//                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
//                intent.putExtra("Type", "1");
//                intent.putExtra("Token", token);
//                intent.putExtra("OrderID", int_something);
//                intent.putExtra("TSPEnabled", 1);
//
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), weekMessagesActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });

        mLinearLayout_Meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Async().execute("https://mpl.pec.ir/api/EShop/GetSaleTokenNew");

//                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
//                intent.putExtra("Type", "1");
//                intent.putExtra("Token", token);
//                intent.putExtra("OrderID", int_something);
//                intent.putExtra("TSPEnabled", 1);
//
                Boolean isRegistered = G.IS_REGISTERED.getBoolean("IS_REGISTERED", false);
                if (isRegistered) {
                    Intent intent = new Intent(getActivity(), MeditationActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });

        final TextView Name = (TextView) view.findViewById(R.id.name);
        final LinearLayout exit = (LinearLayout) view.findViewById(R.id.fragment_nav_item_exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_confirm.show();
            }
        });
        Name.setText("ورود / ثبت نام");

        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exit.getVisibility() == View.VISIBLE) {
//                    dialog_confirm.show();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
//                    ActivityMain.finisher();
                }

            }
        });

        if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
            String name = G.CUSTOMER_NAME.getString("CUSTOMER_NAME", "empty");
            if (!name.equals("empty")) {
                Name.setText(name);
                exit.setVisibility(View.VISIBLE);
                refreshCredit();
            } else {
                exit.setVisibility(View.GONE);
                Name.setText("ورود / ثبت نام");
            }
        } else {
            exit.setVisibility(View.GONE);
            Name.setText("ورود / ثبت نام");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        void onFragmentInteraction(Uri uri);

        void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second);

        void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth);
    }

    private void activityFinisher() {

    }


    private class AsyncLogout extends Webservice.logOutSession {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            Toast.makeText(G.CONTEXT, result, Toast.LENGTH_LONG).show();

        }
    }

    //
    public static void refreshCredit() {
        int CREDIT = G.CREDIT.getInt("CREDIT", 0);
        String convertedCredit = String.format("%,d", CREDIT);
//        S_TEXTVIEW_CREDIT.setText(convertedCredit + " ریال");

    }

    public void clearDateBase() {
        DataBaseWrite dataBaseHelper = new DataBaseWrite(getContext(), "ORDER_DATABASE", "ORDER_TABLE", OrderTableFieldNames, OrderTableFieldTypes);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();
        sqld.execSQL("delete from " + "ORDER_TABLE");

        DataBaseWrite db = new DataBaseWrite(getContext(), "HISTORY_DATABASE", "HISTORY_TABLE", HistoryTableFieldNames, HistoryTableFieldTypes);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("delete from " + "HISTORY_TABLE");

    }


    private class Async extends Webservice.getToken {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {


        }
    }

}
