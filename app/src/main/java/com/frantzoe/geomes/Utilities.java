package com.frantzoe.geomes;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Utilities {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    public static final String shortFormat = "d/M/yyyy";
    public static final String longFormat = "EEEE dd MMMM yyyy";
    private static final String CHARS_STRING = "1rt7sJFmuNjfTeI8nBv04hPQU6D5xoAZGYHbgXk3zWKlw2a9SdiMpVOyqcRCEL";
    //private static final String CHARS_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private static final String[] permissions = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    public static String formatDate(Date date, String pattern, Locale locale){
        try {return new SimpleDateFormat(pattern, locale).format(date);
        } catch(Exception e) {e.printStackTrace();
            return null;
        }
    }

    public static boolean arePermissionsGranted(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                + ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void sendSMS(String number, String message) {
        if (Patterns.PHONE.matcher(number).matches()) {
            SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
        }
    }

    public static void requestPermissions(final Activity activity){
        // Do something, when permissions not granted
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
            // If we should give explanation of requested permissions
            // Show an alert dialog here with request explanation
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Les permissions Contacts, Messages et Localisation sont requises. Veuillez lzs accepter.");
            builder.setTitle("Veuillez accepter ces permissions.");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_CODE);
                }
            });
            builder.setNeutralButton("Cancel",null);
            AlertDialog dialog = builder.create();
            dialog.show();

        } else{
            // Directly request for required permissions, without explanation
            ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_CODE);
        }
    }
}
