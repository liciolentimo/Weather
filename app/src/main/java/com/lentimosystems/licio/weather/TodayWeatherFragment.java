package com.lentimosystems.licio.weather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lentimosystems.licio.weather.Constants.Constants;
import com.lentimosystems.licio.weather.Constants.OpenWeatherMapInterface;
import com.lentimosystems.licio.weather.Model.WeatherResult;
import com.lentimosystems.licio.weather.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    ImageView imgWeather;
    TextView txtCityName,txtHumidity,txtSunrise,txtSunset,txtPressure,txtTemperature,txtDescription,txtDateTime,txtWind,txtGeoCoords;
    LinearLayout weatherPanel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    OpenWeatherMapInterface mService;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance() {
        if (instance == null)
            instance = new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment() {
       compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(OpenWeatherMapInterface.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView  = inflater.inflate(R.layout.fragment_today_weather, container, false);

        txtCityName = (TextView)itemView.findViewById(R.id.txtCityName);
        txtHumidity = (TextView)itemView.findViewById(R.id.txtHumidity);
        txtSunrise = (TextView)itemView.findViewById(R.id.txtSunrise);
        txtSunset = (TextView)itemView.findViewById(R.id.txtSunset);
        txtPressure = (TextView)itemView.findViewById(R.id.txtPressure);
        txtTemperature = (TextView)itemView.findViewById(R.id.txtTemperature);
        txtDescription = (TextView)itemView.findViewById(R.id.txtDescription);
        txtDateTime = (TextView)itemView.findViewById(R.id.txtDateTime);
        txtWind = (TextView)itemView.findViewById(R.id.txtWind);
        txtGeoCoords = (TextView)itemView.findViewById(R.id.txtGeoCoords);
        weatherPanel = (LinearLayout)itemView.findViewById(R.id.weatherPanel);
        loading = (ProgressBar)itemView.findViewById(R.id.loading);
        
        getWeatherInformation();

        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Constants.currentLocation.getLatitude()),
                String.valueOf(Constants.currentLocation.getLongitude()),
                Constants.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        //Load image
//                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
//                                .append(weatherResult.getWeather().get(0).getIcon())
//                        .append(".png").toString()).into(imgWeather);

                        //Display info
                        txtCityName.setText(weatherResult.getName());
                        txtDescription.setText(new StringBuilder("Weather in ")
                        .append(weatherResult.getName()).toString());
                        txtTemperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                        txtDateTime.setText(Constants.convertUnixToDate(weatherResult.getDt()));
                        txtPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txtHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
                        txtSunrise.setText(Constants.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txtSunset.setText(Constants.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txtGeoCoords.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());

                        weatherPanel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

}
