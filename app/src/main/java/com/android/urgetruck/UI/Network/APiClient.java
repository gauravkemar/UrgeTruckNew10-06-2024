package com.android.urgetruck.UI.Network;

import com.android.urgetruck.UI.Utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        if(BASE_URL.equals("192.168.1.235"))
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+BASE_URL+":5000/Ut/api/MobileApp/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        else
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL+"/Service/api/MobileApp/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
