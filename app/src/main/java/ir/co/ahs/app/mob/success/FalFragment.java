package ir.co.ahs.app.mob.success;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;

public class FalFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    private final String PLUS_ONE_URL = "http://developer.android.com";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Struct> falList = new ArrayList<>();
    private LinearLayoutManager falListManager;
    private Adapter_Recycler falListAdapter;
    private String strMonth;

    private JSONArray groupsArray;
    private String groupId;
    private String groupTitle;
    private int number = 0;
    private MaterialSpinner month_spinner;
    private String[] months;
    public int monthPosition = 1;
    private MaterialSpinner year_spinner;
    private String[] years;
    public int yearPosition = 0;
    private TextView list_state;

    public FalFragment() {
    }

    public static FalFragment newInstance(String param1, String param2) {
        FalFragment fragment = new FalFragment();
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


//        for (int i = 1; i < 5; i++) {
//            Struct struct = new Struct();
//            switch (i) {
//                case 1:
//                    strMonth = "فروردين";
//                    break;
//                case 2:
//                    strMonth = "ارديبهشت";
//                    break;
//                case 3:
//                    strMonth = "خرداد";
//                    break;
//                case 4:
//                    strMonth = "تير";
//                    break;
//                case 5:
//                    strMonth = "مرداد";
//                    break;
//                case 6:
//                    strMonth = "شهريور";
//                    break;
//                case 7:
//                    strMonth = "مهر";
//                    break;
//                case 8:
//                    strMonth = "آبان";
//                    break;
//                case 9:
//                    strMonth = "آذر";
//                    break;
//                case 10:
//                    strMonth = "دي";
//                    break;
//                case 11:
//                    strMonth = "بهمن";
//                    break;
//                case 12:
//                    strMonth = "اسفند";
//                    break;
//            }
//            struct.strItemTitle = strMonth;
//            struct.strItemText = getResources().getString(R.string.fal);
//            struct.strItemDate = "۹۶/۲/۲۳";
//            falList.add(struct);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fal, container, false);
        RecyclerView localEventRecycler = (RecyclerView) view.findViewById(R.id.falListRecycler);
        list_state = (TextView) view.findViewById(R.id.list_state);

        falListManager = new LinearLayoutManager(getContext());
        falListAdapter = new Adapter_Recycler(getContext(), falList, new OnItemListener() {
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
        }, 3, false);
        localEventRecycler.setAdapter(falListAdapter);
        localEventRecycler.setLayoutManager(falListManager);


        month_spinner = (MaterialSpinner) view.findViewById(R.id.month_spinner);
        months = new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
        month_spinner.setItems(months);

        month_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                monthPosition = position + 1;
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                new getOmen().execute(Urls.BASE_URL + Urls.GET_MONTH_OMEN,
                        sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), years[yearPosition], String.valueOf(monthPosition));

            }
        });

        year_spinner = (MaterialSpinner) view.findViewById(R.id.year_spinner);
        years = new String[]{"1396", "1397", "1398", "1399", "1400"};
        year_spinner.setItems(years);

        year_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                yearPosition = position;
                String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
                String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
                new getOmen().execute(Urls.BASE_URL + Urls.GET_MONTH_OMEN,
                        sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), years[yearPosition], String.valueOf(monthPosition));

            }
        });

        month_spinner.setSelectedIndex(G.month - 1);
        monthPosition = G.month;
        for (int i = 0; i < 5; i++) {
            if (String.valueOf(G.year).equals(years[i])) {
                yearPosition = i;
            }
        }
        year_spinner.setSelectedIndex(yearPosition);
        firstRequest();
        return view;
    }

    public void firstRequest() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        new getOmen().execute(Urls.BASE_URL + Urls.GET_MONTH_OMEN,
                sessionId, userToken, String.valueOf(G.LATITUDE), String.valueOf(G.LONGTITUDE), years[yearPosition], String.valueOf(monthPosition));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class getOmen extends Webservice.getOmen {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;

            falList.clear();
            groupsArray = null;
            try {
                groupsArray = new JSONArray(result);
                for (int i = 0; i < groupsArray.length(); i++) {
                    Struct struct = new Struct();
                    JSONObject productObject = groupsArray.getJSONObject(i);
                    groupId = productObject.getString("OmenID");
                    struct.strItemTitle = productObject.getString("BirthMonth") + " - " + " نیمه " + productObject.getString("OmenMonthHalf");
                    struct.strItemText = productObject.getString("OmenDiscription");
                    struct.strImg = productObject.getString("OmenPhoto");
                    struct.strItemDate = "۹۶/۲/۲۳";
                    falList.add(struct);
                }
                falListAdapter.notifyDataSetChanged();

                if (falList.size() < 1) {
                    list_state.setVisibility(View.VISIBLE);
                } else {
                    list_state.setVisibility(View.GONE);

                }
            } catch (JSONException e) {

            }

        }
    }

}
