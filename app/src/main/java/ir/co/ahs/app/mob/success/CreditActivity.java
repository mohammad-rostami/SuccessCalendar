package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class CreditActivity extends Activity {

    private Adapter_Recycler adapter_recycler;
    ArrayList<Struct> arrayList = new ArrayList<>();
    private TextView credit;
    private TextView income;
    private TextView outcome;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("تاریخجه اعتبارات");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        credit = (TextView) findViewById(R.id.credit);
        income = (TextView) findViewById(R.id.income);
        outcome = (TextView) findViewById(R.id.outcome);

        credit.setText("0");
        income.setText("0");
        outcome.setText("0");

        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "x");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "x");
        String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
        new Async().execute(Urls.BASE_URL + Urls.GET_SCORE, sessionId, userToken, customerId);

        RecyclerView list = (RecyclerView) findViewById(R.id.rv_credit);
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
        }, 9, false);
        list.setAdapter(adapter_recycler);
        list.setLayoutManager(manager);
    }

    private class Async extends Webservice.getScore {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(final String result) {
            String x = result ;

            try {
//                JSONArray main = new JSONArray(result);
//                JSONObject mainObject = main.getJSONObject(0);
                JSONObject mainObject = new JSONObject(result);
                JSONArray scoreList = mainObject.getJSONArray("scoreItemsList");
                int Credit = mainObject.getInt("creditValue");
                int Income = mainObject.getInt("incomeValue");
                int Outcome = mainObject.getInt("outcomeValue");
                String convertedCredit = String.format("%,d", Credit);
                String convertedIncome = String.format("%,d", Income);
                String convertedOutcome = String.format("%,d", Outcome);

                credit.setText(convertedCredit);
                income.setText(convertedIncome);
                outcome.setText(convertedOutcome);
                for (int i = 0; i < scoreList.length(); i++) {
                    JSONObject soreListObjects = scoreList.getJSONObject(i);
                    Struct struct = new Struct();
                    struct.strItemTitle = soreListObjects.getString("comment");
                    struct.strText = soreListObjects.getString("scoreValue");
                    struct.strDate = soreListObjects.getString("scoreDate");
                    struct.isAdded = soreListObjects.getBoolean("Added");
                    arrayList.add(struct);
                }
                adapter_recycler.notifyDataSetChanged();
            } catch (JSONException e) {


            }
        }
    }
}
