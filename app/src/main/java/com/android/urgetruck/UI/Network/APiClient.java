package com.android.urgetruck.UI.Network;

import com.android.urgetruck.UI.Utils.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APiClient {
    private static Retrofit retrofit;



    public static Retrofit getClient(String BASE_URL){
//        if(retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://"+BASE_URL+"/Service/api/MobileApp/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
        if(BASE_URL.equals("192.168.1.107"))
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+BASE_URL+":8001/Ut/api/MobileApp/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        else
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+BASE_URL+"/Service/api/MobileApp/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
