package com.abhimanyusharma.weather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Abhimanyu Sharma on 08-01-2017.
 */

public class Forecast extends AsyncTask<String, Void, String> {
    String icon;
    int i;

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;

                result += current;

                data = reader.read();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {

            JSONObject jsonObject = new JSONObject(result);

            //List all = new ArrayList();
            JSONArray listArrayData = jsonObject.getJSONArray("list");
            for (i = 0; i < 5; i++) {
                JSONObject listData = listArrayData.getJSONObject(i);

//TEMP
                JSONObject tempData = new JSONObject(listData.getString("temp"));

                double temp_min = Double.parseDouble(tempData.getString("min"));
                int min = (int) (temp_min - 273);
                double temp_max = Double.parseDouble(tempData.getString("max"));
                int max = (int) (temp_max - 273);
//DATE

//TRY MMM dd yyyy HH:MM:ss
                String dateData = listData.getString("dt");
                long date1 = Long.parseLong(dateData);
                date1 = date1 * 1000L;
                //long d=1486477092*1000L;
                Date date = new Date(date1); // your date
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
                String date2 = formatter.format(date);

//WEATHER
                JSONArray weatherArrayData = listData.getJSONArray("weather");
                JSONObject weatherData = weatherArrayData.getJSONObject(0);
                icon = weatherData.getString("main");

                if (i == 0) {
                    MainActivity.f1.setText(date2);
                    MainActivity.mm1.setText(String.valueOf(max) + "°/" + String.valueOf(min) + "°");
                    this.setIcon(icon, i);
                }
                if (i == 1) {
                    MainActivity.f2.setText(date2);
                    MainActivity.mm2.setText(String.valueOf(max) + "°/" + String.valueOf(min) + "°");
                    this.setIcon(icon, i);
                }
                if (i == 2) {
                    MainActivity.f3.setText(date2);
                    MainActivity.mm3.setText(String.valueOf(max) + "°/" + String.valueOf(min) + "°");
                    this.setIcon(icon, i);
                }
                if (i == 3) {
                    MainActivity.f4.setText(date2);
                    MainActivity.mm4.setText(String.valueOf(max) + "°/" + String.valueOf(min) + "°");
                    this.setIcon(icon, i);
                }
                if (i == 4) {
                    MainActivity.f5.setText(date2);
                    MainActivity.mm5.setText(String.valueOf(max) + "°/" + String.valueOf(min) + "°");
                    this.setIcon(icon, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainActivity.dialog.hide();
    }

    void setIcon(String icon, int i) {

        if (i == 0) {
            switch (icon) {
                case "Clear":
                    MainActivity.i1.setImageResource(R.drawable.i_clear);
                    break;
                case "Rain":
                    MainActivity.i1.setImageResource(R.drawable.i_rain);
                    break;
                case "Cloud":
                    MainActivity.i1.setImageResource(R.drawable.i_cloud);
                    break;
                case "Snow":
                    MainActivity.i1.setImageResource(R.drawable.i_snow);
                    break;

                default:
                    MainActivity.i1.setImageResource(R.drawable.i_snow);
                    break;
            }
        } else if (i == 1) {
            switch (icon) {
                case "Clear":
                    MainActivity.i2.setImageResource(R.drawable.i_clear);
                    break;
                case "Rain":
                    MainActivity.i2.setImageResource(R.drawable.i_rain);
                    break;
                case "Cloud":
                    MainActivity.i2.setImageResource(R.drawable.i_cloud);
                    break;
                case "Snow":
                    MainActivity.i2.setImageResource(R.drawable.i_snow);
                    break;

                default:
                    MainActivity.i2.setImageResource(R.drawable.i_clear);
                    break;
            }
        } else if (i == 2) {
            switch (icon) {
                case "Clear":
                    MainActivity.i3.setImageResource(R.drawable.i_clear);
                    break;
                case "Rain":
                    MainActivity.i3.setImageResource(R.drawable.i_rain);
                    break;
                case "Cloud":
                    MainActivity.i3.setImageResource(R.drawable.i_cloud);
                    break;
                case "Snow":
                    MainActivity.i3.setImageResource(R.drawable.i_snow);
                    break;

                default:
                    MainActivity.i3.setImageResource(R.drawable.i_clear);
                    break;
            }
        } else if (i == 3) {
            switch (icon) {
                case "Clear":
                    MainActivity.i4.setImageResource(R.drawable.i_clear);
                    break;
                case "Rain":
                    MainActivity.i4.setImageResource(R.drawable.i_rain);
                    break;
                case "Cloud":
                    MainActivity.i4.setImageResource(R.drawable.i_cloud);
                    break;
                case "Snow":
                    MainActivity.i4.setImageResource(R.drawable.i_snow);
                    break;

                default:
                    MainActivity.i4.setImageResource(R.drawable.i_snow);
                    break;
            }
        } else if (i == 4) {
            switch (icon) {
                case "Clear":
                    MainActivity.i5.setImageResource(R.drawable.i_clear);
                    break;
                case "Rain":
                    MainActivity.i5.setImageResource(R.drawable.i_rain);
                    break;
                case "Cloud":
                    MainActivity.i5.setImageResource(R.drawable.i_cloud);
                    break;
                case "Snow":
                    MainActivity.i5.setImageResource(R.drawable.i_snow);
                    break;

                default:
                    MainActivity.i5.setImageResource(R.drawable.i_snow);
                    break;
            }

        }
    }
}