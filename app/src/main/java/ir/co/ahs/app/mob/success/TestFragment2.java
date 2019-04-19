package ir.co.ahs.app.mob.success;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.co.persiancalendar.core.G;


public class TestFragment2 extends Fragment {
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

    private TestFragment2.OnFragmentInteractionListener mListener;
    private LinearLayoutManager newsListManager;
    private Adapter_Recycler newsListAdapter;
    private ArrayList<Struct> newsList = new ArrayList<>();


    private Dialog dialog_verify;
    private Dialog dialog_phone;
    static ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private static Adapter_Recycler adapter_recycler;
    private EditText editText;
    public Boolean firstLoad = true;
    private String lastDate = "0000/00/00";
    private String lastTime = "00:00:00";
    private TextView list_state;
    private String headerID;
    private String headerTitle;


    public TestFragment2() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShutedDownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment2 newInstance(String param1, String param2) {
        TestFragment2 fragment = new TestFragment2();
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
        View view = inflater.inflate(R.layout.fragment_week_story, container, false);
//        RecyclerView localEventRecycler = (RecyclerView) view.findViewById(R.id.newsListRecycler);
//        list_state = (TextView) view.findViewById(R.id.list_state);

        final RecyclerView list = (RecyclerView) view.findViewById(R.id.newsListRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        adapter_recycler = new Adapter_Recycler(getContext(), arrayList, new OnItemListener() {
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

                headerID = arrayList.get(position).strId;
                headerTitle = arrayList.get(position).strText;

                getTestItem(headerID);
            }

            @Override
            public void onEdit(int position) {

            }
        }, 7, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);

//        getTests();

        getTests(1);


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
        if (context instanceof TestFragment2.OnFragmentInteractionListener) {
            mListener = (TestFragment2.OnFragmentInteractionListener) context;
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

    public static void ReloadDataForFirstTime(int position) {

        String currentString = G.WEEK_STORY_GROUPS_ARRAY.get(position);
        String[] separated = currentString.split(":");
        final String ids = separated[0];
        String names = separated[1];

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },500);



    }

    public static void ReloadData(int type) {

//        String currentString = G.WEEK_STORY_GROUPS_ARRAY.get(position);
//        String[] separated = currentString.split(":");
//        String ids = separated[0];
//        String names = separated[1];



//        getTests(ids);


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void getTests(int testType) {
        arrayList.clear();

        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new AsyncTests().execute(Urls.BASE_URL + Urls.GET_TEST_ALL, sessionId, userToken, String.valueOf(testType));
    }

    private static class AsyncTests extends Webservice.getTestGroup {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result;
            arrayList.clear();


            JSONArray array = null;
            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strId = object.getString("HeaderID");
                    struct.strText = object.getString("HeaderTitle");

                    arrayList.add(struct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter_recycler.notifyDataSetChanged();
//            getPoll();



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
            String x = result;

            Intent intent = new Intent(getActivity(),TestItemActivity.class);
            intent.putExtra("json",result);
            intent.putExtra("headerID",headerID);
            intent.putExtra("headerTitle",headerTitle);
            getActivity().startActivity(intent);

        }
    }


}