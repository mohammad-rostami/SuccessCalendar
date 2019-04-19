package ir.co.ahs.app.mob.success;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by imac on 3/4/18.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
//            CreateNotification();
            Intent i = new Intent(context, NotificationService.class);
            context.stopService(i);
            context.startService(i);
        }
    }
}
