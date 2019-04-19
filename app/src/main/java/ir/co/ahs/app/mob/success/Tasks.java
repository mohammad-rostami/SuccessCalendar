package ir.co.ahs.app.mob.success;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Tasks extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "Do somthing", Toast.LENGTH_SHORT).show();

        try {
        String name = intent.getStringExtra("event");

        Intent intent1 = new Intent(context.getApplicationContext(), AlarmActivity.class);
        intent1.putExtra("event", name);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent1);
        } catch (Exception e) {
        }


    }
}