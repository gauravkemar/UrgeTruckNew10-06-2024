package com.android.urgetruck.UI.Utils;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class Utils {


    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wificon != null && wificon.isConnected())||(mobilecon !=null && mobilecon.isConnected())){

            return true;
        }
        else
        {
            return false;
        }


    }

    public static void showCustomDialog(Context context,String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    public static void showCustomDialogFinish(Context context,String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Activity) context).finish();

                    }
                })
                .show();

    }

    public static void postsharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",Context.MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString(key,value);


        myEdit.commit();

    }

    public static String getSharedPreferences(Context context, String key){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String s1;
        if(key.equals("apiurl")){
             s1 = sh.getString(key, "52.140.115.46");

        }else if(key.equals("antennapower")){
            s1 = sh.getString(key,"130");
        }

        else{
             s1 = sh.getString(key, "");

        }
        return s1;
    }

}
