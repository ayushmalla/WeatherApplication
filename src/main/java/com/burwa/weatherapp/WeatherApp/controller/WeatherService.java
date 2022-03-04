package com.burwa.weatherapp.WeatherApp.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WeatherService {
    private OkHttpClient client;
    private Response response;
    private String cityName;
    private String unit;

    public JSONObject getWeather() throws IOException {
        client = new OkHttpClient();
        Request request = new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid=09bae68abb4d30cf41ab6603e7a96302")
                                    .build();
        response = client.newCall(request).execute();

        try {
            return new JSONObject(response.body().string());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray returnWeatherArray() throws IOException, JSONException {
        return getWeather().getJSONArray("weather");

    }
    public JSONObject returnMain() throws IOException, JSONException {
        return getWeather().getJSONObject("main");
    }
    public JSONObject returnWindObject() throws IOException, JSONException {
        return getWeather().getJSONObject("wind");
    }
    public JSONObject returnSun() throws IOException, JSONException {
        return getWeather().getJSONObject("sys");
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
