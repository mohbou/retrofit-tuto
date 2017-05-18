package com.mohbou.retrofit_tuto.retrofit_tuto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mohbou.retrofit_tuto.retrofit_tuto.model.Row;
import com.mohbou.retrofit_tuto.retrofit_tuto.model.Stocks;
import com.mohbou.retrofit_tuto.retrofit_tuto.network.YhooServiceAPI;

import java.io.File;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
import okhttp3.Cache;

public class MainActivity extends AppCompatActivity {


    private static final String BASE_URL = "https://query.yahooapis.com/v1/public/";
    private Retrofit mRetrofit;
    private YhooServiceAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mService.stocks().enqueue(new Callback<Stocks>() {
            @Override
            public void onResponse(Call<Stocks> call, Response<Stocks> response) {
                Iterator<Row> iterator = response.body().getQuery().getResults().getRow().iterator();

                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Log.d("result", row.getSymbol() + " Change " + row.getChange() + " Price " + row.getPrice());
                }

            }

            @Override
            public void onFailure(Call<Stocks> call, Throwable t) {
                Log.d("result", "Something wrong happened " + t.getMessage());
            }
        });
    }

    private void init() {
        Timber.plant(new Timber.DebugTree());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });

        File cacheFile = new File(this.getCacheDir(), "okhttp_cache");
        cacheFile.mkdir();

        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        mService = mRetrofit.create(YhooServiceAPI.class);
    }
}
