package ir.co.ahs.app.mob.success;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {

            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String x = messages[0].getOriginatingAddress();
            Toast.makeText(context, x, Toast.LENGTH_SHORT).show();

            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();

                try {
                    Pattern pattern = Pattern.compile("(\\d{4})");
                    Matcher matcher = pattern.matcher(messageBody);
                    String val = "";
                    if (matcher.find()) {
                        val = matcher.group(1);  // 4 digit number
                    }
//                    Toast.makeText(context, val, Toast.LENGTH_SHORT).show();
                    Log.d("code", "'" + val + "'");
                    try {
                        RegisterActivity.verificationCodeSetter(val);
                    } catch (Exception e) {

                    }

                } catch (Exception e) {

                }

            }
        }
    }
}