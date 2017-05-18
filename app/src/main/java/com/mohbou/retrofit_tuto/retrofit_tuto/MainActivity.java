package com.mohbou.retrofit_tuto.retrofit_tuto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mohbou.retrofit_tuto.retrofit_tuto.model.Row;
import com.mohbou.retrofit_tuto.retrofit_tuto.model.Stocks;
import com.mohbou.retrofit_tuto.retrofit_tuto.network.YhooServiceAPI;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String BASE_URL ="https://query.yahooapis.com/v1/public/";
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
                    Log.d("result",row.getSymbol()+" Change "+row.getChange()+" Price "+ row.getPrice());
                }

            }

            @Override
            public void onFailure(Call<Stocks> call, Throwable t) {
                Log.d("result","Something wrong happened "+t.getMessage());
            }
        });
    }

    private void init() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(YhooServiceAPI.class);
    }
}
