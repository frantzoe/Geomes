package com.frantzoe.geomes.listeners;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.activities.MainActivity;
import com.frantzoe.geomes.helpers.EventDatabase;
import com.frantzoe.geomes.models.Event;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MessageListener extends BroadcastReceiver {

    private static final String SMS_BODY_START = "GeoMes";
    private static final String SMS_BODY_REQ = "GeoMesReq|";
    private static final String SMS_BODY_RES = "GeoMesRes|";
    private static final String SMS_BODY_CAN = "GeoMesCan|";
    private static final String EVENT_CHANNEL_ID = "Events";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            StringBuilder smsSender = new StringBuilder();
            StringBuilder smsBody = new StringBuilder();
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody.append(smsMessage.getMessageBody());
                smsSender.append(smsMessage.getDisplayOriginatingAddress());
            }

            createNotificationChannel(context);

            if (smsBody.toString().startsWith(SMS_BODY_START)) {

                String[] bodySplit = smsBody.toString().split("\\|");
                Intent eventIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, eventIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                EventDatabase database = new EventDatabase(context);

                if (smsBody.toString().startsWith(SMS_BODY_REQ)) {
                    createNotification(123, context, EVENT_CHANNEL_ID, "Demande", "Nouvelle demande de rencontre de " + smsSender.toString(), pendingIntent);
                    database.addEvent(bodySplit[1], Double.parseDouble(bodySplit[2]), Double.parseDouble(bodySplit[3]), Event.DIR_IN, false, smsSender.toString(), bodySplit[4]);
                }
                else if (smsBody.toString().startsWith(SMS_BODY_RES)) {
                    if (Integer.parseInt(bodySplit[2]) == 0) {
                        createNotification(234, context, EVENT_CHANNEL_ID, "Réponse", smsSender.toString() + " vient de refuser votre demande de rencontre. Cette dernière a été supprimée de votre liste.", null);
                        database.deleteEvent(bodySplit[1], Event.DIR_OUT);
                    } else if (Integer.parseInt(bodySplit[2]) == 1){
                        createNotification(345, context, EVENT_CHANNEL_ID, "Réponse", smsSender.toString() + " vient d'accepter votre demande de rencontre. Cette dernière a été rajoutée à votre liste.", pendingIntent);
                        database.confirmEvent(bodySplit[1], Event.DIR_OUT);
                    }
                }
                else if (smsBody.toString().startsWith(SMS_BODY_CAN)) {
                    createNotification(456, context, EVENT_CHANNEL_ID, "Annulation", smsSender.toString() + " vient d'annuler votre rencontre. Cette dernière a été supprimée de votre liste.", null);
                    database.deleteEvent(bodySplit[1], bodySplit[2].equals(Event.DIR_IN) ? Event.DIR_OUT : Event.DIR_IN);
                }

                database.close();
            }
        }
    }

    private void createNotification(int notId, Context context, String channel, String title, String text, PendingIntent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(intent)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notId, mBuilder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence eveChnl = context.getString(R.string.event_channel_name);
            String eveChnlDesc = context.getString(R.string.event_channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(EVENT_CHANNEL_ID, eveChnl, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(eveChnlDesc);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
