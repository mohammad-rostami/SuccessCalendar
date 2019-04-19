package ir.co.ahs.app.mob.success;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ir.co.ahs.app.mob.success.Helper.DataBaseWriter;
import ir.co.persiancalendar.core.G;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
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
    private LinearLayoutManager newsListManager;
    private Adapter_Recycler newsListAdapter;
    private ArrayList<Struct> newsList = new ArrayList<>();


    private Dialog dialog_verify;
    private Dialog dialog_phone;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private Dialog dialog_credit;
    private Adapter_Recycler adapter_recycler;
    private EditText editText;
    public Boolean firstLoad = true;
    private String lastDate = "0000/00/00";
    private String lastTime = "00:00:00";


    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
//        for (int i = 0; i < 6; i++) {
//            Struct struct = new Struct();
//            struct.strItemTitle = "لورم اپیسوم";
//            struct.strItemText = "متن تستی";
//            struct.strItemDate = "۹۶/۲/۲۳";
//            newsList.add(struct);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView localEventRecycler = (RecyclerView) view.findViewById(R.id.newsListRecycler);
        newsListManager = new LinearLayoutManager(getContext());
        newsListAdapter = new Adapter_Recycler(getContext(), newsList, new OnItemListener() {
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
        }, 4, false);
        localEventRecycler.setAdapter(newsListAdapter);
        localEventRecycler.setLayoutManager(newsListManager);
//        DataBaseChecker("news_table", "title", "text", "photo", "date", "time", "id","deleted");

        if (firstLoad) {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
            new newAsync().execute(Urls.BASE_URL + Urls.GET_NEWS, sessionId, userToken, lastDate, lastTime);
            firstLoad = false;
        }

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

    private String DataBaseChecker(String TableName, String TITLE, String TEXT, String PHOTO, String DATE, String TIME, String ID, String DELETED) {
        DataBaseWriter dataBaseHelper = new DataBaseWriter(getContext());
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase sqld = dataBaseHelper.getWritableDatabase();

        Cursor cursor = sqld.rawQuery("select * from " + TableName, null);
        String title = null;
        String text = null;
        String photo = null;
        String date = null;
        String time = null;
        String id = null;
        String deleted = null;
        if (cursor.moveToFirst()) {
            boolean firstCycle = true;
            do {
                title = cursor.getString(cursor.getColumnIndex(TITLE));
                text = cursor.getString(cursor.getColumnIndex(TEXT));
                photo = cursor.getString(cursor.getColumnIndex(PHOTO));
                date = cursor.getString(cursor.getColumnIndex(DATE));
                time = cursor.getString(cursor.getColumnIndex(TIME));
                id = cursor.getString(cursor.getColumnIndex(ID));
                deleted = cursor.getString(cursor.getColumnIndex(DELETED));

                if (firstCycle) {
                    lastDate = date;
                    lastTime = time;
                }

                if (title != null) {
                    if (deleted.equals("false")) {
                        Struct struct = new Struct();
                        struct.strName = title;
                        struct.strText = text;
                        struct.strImg = PHOTO;
                        struct.strDate = date;
                        newsList.add(struct);
                    }
                }
                firstCycle = false;
            } while (cursor.moveToNext());
        }
        newsListAdapter.notifyDataSetChanged();
        sqld.close();

        if (firstLoad) {
            String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
            String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
            new newAsync().execute(Urls.BASE_URL + Urls.GET_NEWS, sessionId, userToken, lastDate, lastTime);
            firstLoad = false;
        }
        return title;
    }

    private class newAsync extends Webservice.getNews {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
//            DataBaseWriter helper = new DataBaseWriter(getContext());
            try {
                JSONArray main = new JSONArray(result);
                if (main.length()>0){
//                    helper.delete();
                }
                for (int i = 0; i < main.length(); i++) {
                    JSONObject object = main.getJSONObject(i);
//                    helper.sampleMethod(new String[]{
//                            object.getString("newsTitle"),
//                            object.getString("newsText"),
//                            object.getString("newsPhoto"),
//                            object.getString("newsDate"),
//                            object.getString("newsTime"),
//                            object.getString("newsId"),
//                            String.valueOf(object.getBoolean("deleted"))});

                    if (String.valueOf(object.getBoolean("deleted")).equals("false")) {
                        Struct struct = new Struct();
                        struct.strName = object.getString("newsTitle");
                        struct.strText = object.getString("newsText");
                        struct.strImg = object.getString("newsPhoto");
                        struct.strDate = object.getString("newsDate");
                        newsList.add(struct);
                    }
                }
                arrayList.clear();
//                DataBaseChecker("news_table", "title", "text", "photo", "date", "time", "id", "deleted");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            newsListAdapter.notifyDataSetChanged();

        }
    }
    private static void DataBaseItemDelete(String TableName, int quantity, String id) {
        DataBaseWriter helper = new DataBaseWriter(G.CONTEXT);
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

}
