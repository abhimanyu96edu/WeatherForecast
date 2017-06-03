package com.abhimanyusharma.weather;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.abhimanyusharma.weather.MainActivity.dialog;

/**
 * Created by Abhimanyu Sharma on 28-12-2016.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {
    char c;

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

//MAIN
            JSONObject mainData = new JSONObject(jsonObject.getString("main"));
            double temperatureData = Double.parseDouble(mainData.getString("temp"));
            int temperature = ((int) (temperatureData - 273));
            MainActivity.temperatureTextView.setText(String.valueOf(temperature) + " °C");

            double temp_min = Double.parseDouble(mainData.getString("temp_min"));
            int min = (int) (temp_min - 273);
            double temp_max = Double.parseDouble(mainData.getString("temp_max"));
            int max = (int) (temp_max - 273);


            double pressureData = Double.parseDouble(mainData.getString("pressure"));
            MainActivity.pressureTextView.setText(String.valueOf(pressureData) + "Pa");

            int humidityData = mainData.optInt("humidity");
            MainActivity.humidityTextView.setText(String.valueOf(humidityData) + "%");

//NAME
            String placeName = jsonObject.getString("name");
            //MainActivity.placeTextView.setText("City : " + placeName + country);

//VISIBILITY
            double visibilityData = jsonObject.getDouble("visibility");
            float visibility = (float) (visibilityData / 1000);
            MainActivity.visibilityTextView.setText(visibility + " km");

//WIND
            JSONObject windData = new JSONObject(jsonObject.getString("wind"));
            double speed = Double.parseDouble(windData.getString("speed"));
            double deg = Double.parseDouble(windData.getString("deg"));
            this.setDirection(deg, speed);

//WEATHER
            //List all = new ArrayList();
            JSONArray weatherArrayData = jsonObject.getJSONArray("weather");
            for (int i = 0; i < weatherArrayData.length(); i++) {
                JSONObject weatherData = weatherArrayData.getJSONObject(i);
                String description = weatherData.getString("description");
                //all.add(desc);
                //MainActivity.conditionTextView.setText("Condition : "+ all.get(i));
                MainActivity.conditionTextView.setText(description);
//DATE
                String dateData = jsonObject.getString("dt");
                long date1 = Long.parseLong(dateData);
                date1 = date1 * 1000L;
                Date date2 = new Date(date1); // your date
                SimpleDateFormat formatter1 = new SimpleDateFormat(" dd MMM , yyyy ");
                String date4 = formatter1.format(date2);
                MainActivity.dateTextView.setText(date4);

//HOURS
                SimpleDateFormat formatter = new SimpleDateFormat("HH");
                String date3 = formatter.format(date2);
                int hour = Integer.parseInt(date3);

                String icon = weatherData.getString("icon");
                //String iconId = ("R.drawable.icon_" + icon);
                this.setIconId(icon, hour);
            }
//sys
            JSONObject sysData = new JSONObject(jsonObject.getString("sys"));
            String country = sysData.getString("country");
/*location*/
            MainActivity.placeTextView.setText((placeName) + " , " + country);

            String sr = sysData.getString("sunrise");
            long sunrise = Long.parseLong(sr);
            sunrise = sunrise * 1000L;  //*1000L for millisecond
            String ss = sysData.getString("sunset");
            long sunset = Long.parseLong(ss);
            sunset = sunset * 1000L;   //*1000L for millisecond
            Date date = new Date(sunrise);
            //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String sunriseData = formatter.format(date);
            MainActivity.sunriseTextView.setText(sunriseData + " hrs");
            date = new Date(sunset);
            String sunsetData = formatter.format(date);
            MainActivity.sunsetTextView.setText(sunsetData + " hrs");


//FORECAST CALLING
            Forecast forecast = new Forecast();
            forecast.execute("http://api.openweathermap.org/data/2.5/forecast/daily?lat="+String.valueOf(LocListener.lat)+"&lon="+String.valueOf(LocListener.lon)+"&cnt=10&appid=78ae06131c470ee9928e4f1eb87ffe43");
            //forecast.execute("http://api.openweathermap.org/data/2.5/forecast/daily?lat=51&lon=0&cnt=10&appid=aabd7edd6ed65a8eb1c5cfd90fbec64d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.hide();
    }

    void setDirection(double deg, double speed) {
        if (deg > 337.50 && deg <= 22.50) {
            MainActivity.windTextView.setText("N at " + speed);
        } else if (deg > 22.50 && deg <= 67.50) {
            MainActivity.windTextView.setText("NE at " + speed);
        } else if (deg > 67.50 && deg <= 112.50) {
            MainActivity.windTextView.setText("E at " + speed);
        } else if (deg > 112.50 && deg <= 157.50) {
            MainActivity.windTextView.setText("SE at " + speed);
        } else if (deg > 157.50 && deg <= 202.50) {
            MainActivity.windTextView.setText("S at " + speed);
        } else if (deg > 202.50 && deg <= 247.50) {
            MainActivity.windTextView.setText("SW at " + speed);
        } else if (deg > 247.50 && deg <= 292.50) {
            MainActivity.windTextView.setText("W at " + speed);
        } else if (deg > 292.50 && deg <= 337.50) {
            MainActivity.windTextView.setText("NW at " + speed);
        }

    }

    void setIconId(String icon, int hour) {
        if (hour <= 19 && hour >= 6) {
            /*day*/
            switch (icon) {
                case "1n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_1d);
                    break;
                case "2n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_2d);
                    break;
                case "3n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_3d);
                    break;
                case "4n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_4d);
                    break;
                case "9n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_9d);
                    break;
                case "01n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_1d);
                    break;
                case "02n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_2d);
                    break;
                case "03n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_3d);
                    break;
                case "04n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_4d);
                    break;
                case "09n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_9d);
                    break;
                case "10n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_10d);
                    break;
                case "11n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_11d);
                    break;
                case "13n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_13d);
                    break;
                case "50n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_50d);
                    break;
                case "1d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_1d);
                    break;
                case "2d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_2d);
                    break;
                case "3d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_3d);
                    break;
                case "4d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_4d);
                    break;
                case "9d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_9d);
                    break;
                case "01d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_01d);
                    break;
                case "02d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_02d);
                    break;
                case "03d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_03d);
                    break;
                case "04d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_04d);
                    break;
                case "09d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_09d);
                    break;
                case "10d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_10d);
                    break;
                case "11d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_11d);
                    break;
                case "13d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_13d);
                    break;
                case "50d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_50d);
                    break;

                default:
                    MainActivity.cloudImageView.setImageResource(R.drawable.na);
                    break;
            }
        }//if
        else {
            /*night*/
            switch (icon) {
                case "1n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_1n);
                    break;
                case "2n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_2n);
                    break;
                case "3n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_3n);
                    break;
                case "4n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_4n);
                    break;
                case "9n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_9n);
                    break;
                case "01n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_01n);
                    break;
                case "02n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_02n);
                    break;
                case "03n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_03n);
                    break;
                case "04n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_04n);
                    break;
                case "09n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_09n);
                    break;
                case "10n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_10n);
                    break;
                case "11n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_11n);
                    break;
                case "13n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_13n);
                    break;
                case "50n":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_50n);
                    break;
                case "1d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_1d);
                    break;
                case "2d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_2d);
                    break;
                case "3d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_3d);
                    break;
                case "4d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_4d);
                    break;
                case "9d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_9d);
                    break;
                case "01d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_01d);
                    break;
                case "02d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_02d);
                    break;
                case "03d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_03d);
                    break;
                case "04d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_04d);
                    break;
                case "09d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_09d);
                    break;
                case "10d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_10d);
                    break;
                case "11d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_11d);
                    break;
                case "13d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_13d);
                    break;
                case "50d":
                    MainActivity.cloudImageView.setImageResource(R.drawable.icon_50d);
                    break;

                default:
                    MainActivity.cloudImageView.setImageResource(R.drawable.na);
                    break;
            }
        }//else

    }

}
