package ir.co.ahs.app.mob.success;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ContactUsActivity extends Activity {
    private Adapter_Recycler adapter_recycler;

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
        setContentView(R.layout.activity_about);

        //Toolbar
        ImageView btnNavigation = (ImageView) findViewById(R.id.btnNavigation);
        TextView toolbar_main_tv_header = (TextView) findViewById(R.id.toolbar_main_tv_header);
        toolbar_main_tv_header.setText("ارتباط با ما");
        btnNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getInformation();


        RecyclerView menuRecycler = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter_recycler = new Adapter_Recycler(getApplicationContext(), arrayList, new OnItemListener() {
            @Override
            public void onItemSelect(int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", arrayList.get(position).strText, null));
                startActivity(intent);
            }

            @Override
            public void onItemClick(int position) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(position).strText));
                startActivity(browserIntent);
            }

            @Override
            public void onItemDelete(int position) {

            }

            @Override
            public void onCardClick(int position) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + arrayList.get(position).strText));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));

            }

            @Override
            public void onEdit(int position) {

            }
        }, 12, false);
        menuRecycler.setAdapter(adapter_recycler);
        menuRecycler.setLayoutManager(manager);


    }


    public void getInformation() {
        String sessionId = G.AUTHENTICATIONS_SESSION.getString("SESSION", "nothing");
        String userToken = G.AUTHENTICATIONS_TOKEN.getString("TOKEN", "nothing");
        new getInformationAsync().execute(Urls.BASE_URL + Urls.GET_INFO, sessionId, userToken);
    }

    private class getInformationAsync extends Webservice.getInfo {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                JSONArray main = new JSONArray(result);
                for (int i = 0; i < main.length(); i++) {
                    JSONObject mainObject = main.getJSONObject(i);
                    int type = mainObject.getInt("TypeId");
                    String title = mainObject.getString("Title");
                    String text = mainObject.getString("Text");
                    int order = mainObject.getInt("Order");

                    Struct struct = new Struct();
                    struct.strItemTitle = title;
                    struct.strText = text;
                    if (type == 6 || type == 8) {
                        struct.strType = "web";
                    } else if (type == 5 || type == 2) {
                        struct.strType = "call";
                    } else if (type == 7) {
                        struct.strType = "email";
                    } else {
                        struct.strType = "other";

                    }
                    arrayList.add(struct);

                }
                adapter_recycler.notifyDataSetChanged();

            } catch (JSONException e) {


            }


        }
    }
}
