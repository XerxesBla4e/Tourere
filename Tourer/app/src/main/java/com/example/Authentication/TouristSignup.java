package com.example.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Api.Credentials;
import com.example.Model.Country;
import com.example.requests.Service;
import com.example.tourer.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class TouristSignup extends AppCompatActivity implements View.OnClickListener {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_signup);
        btn = findViewById(R.id.btnregister2);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnregister2:
               // double amountInUgx = getExchangeRate(500000);
               // Toast.makeText(getApplicationContext(), "Amount UGX  = " + amountInUgx + "", Toast.LENGTH_SHORT).show();
                //testMethod();
                break;
            default:
                break;
        }
    }

   /* private double getExchangeRate(double amount) {
        String urlString = "https://openexchangerates.org/api/latest.json?app_id="+Credentials.XCHANGE_API+"&base=UGX&symbols=USD";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonObject = new JSONObject(response.toString());

            double toRate = jsonObject.getJSONObject("rates").getDouble("USD");
            double usdamount = amount / toRate;
            return usdamount;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }*/

    private void testMethod() {
        String userInput = "Uganda";

        Service service = Service.getInstance();
        Call<List<Country>> call = service.getCountryApi().getCountryByName(userInput);
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.code() == 200) {
                    List<Country> countryList = response.body();
                    for (Country country : countryList) {
                        if (country.getRegion().equals("Africa") && country.getSubregion().equals("Eastern Africa")) {
                            Toast.makeText(getApplicationContext(), "East African Country", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.d("TAG", "Error:" + t.getMessage());
            }
        });
    }
}