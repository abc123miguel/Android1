package micromaster.galileo.edu.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import micromaster.galileo.edu.weatherapp.API.WeatherInterface;
import micromaster.galileo.edu.weatherapp.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final static String BASE_URL = "http://api.wunderground.com/api/";
    private final static String API_KEY = "PASTE HERE YOUR API KEY";

    private TextView countryNameTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryNameTextView = findViewById(R.id.countryName);
        temperatureTextView = findViewById(R.id.temperature);
        pressureTextView = findViewById(R.id.pressure);
        humidityTextView = findViewById(R.id.humidity);
        weather = findViewById(R.id.weather);

        //Execute AsyncTask
        new GetWeatherInformation().execute();
    }


    private class GetWeatherInformation extends AsyncTask<Void, Void, WeatherResponse> {

        @Override
        protected WeatherResponse doInBackground(Void... params) {
            //Now this code is working since it is executed on background
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            int y=1;
            Log.d("test","Testing "+y);
            WeatherInterface weatherInterface = retrofit.create(WeatherInterface.class);
            Call<WeatherResponse> call = weatherInterface.getWeatherFromSanFrancisco(API_KEY);
            WeatherResponse weatherResponse = null;
            try {
                weatherResponse = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherResponse;
        }


        // Test
        @Override
        protected void onPostExecute(WeatherResponse weatherResponse) {
            super.onPostExecute(weatherResponse);

            if (weatherResponse != null) {
                //Update textViews with server response
                countryNameTextView.setText(weatherResponse.getWeatherData().getDisplayLocation().getCityName());
                temperatureTextView.setText(weatherResponse.getWeatherData().getTemp());
                weather.setText(weatherResponse.getWeatherData().getWeather());
                pressureTextView.setText(String.valueOf(weatherResponse.getWeatherData().getPressure()));
                humidityTextView.setText(weatherResponse.getWeatherData().getHumidity());
            }

        }
    }
}
