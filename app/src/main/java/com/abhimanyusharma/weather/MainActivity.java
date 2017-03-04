package com.abhimanyusharma.weather;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    static ImageView cloudImageView, ImageView1;
    static TextView f1, f2, f3, f4, f5, mm1, mm2, mm3, mm4, mm5;
    static ImageView i1, i2, i3, i4, i5;
    static TextView placeTextView;
    static TextView dateTextView;
    static TextView temperatureTextView;
    static TextView pressureTextView;
    static TextView humidityTextView;
    static TextView conditionTextView;
    static TextView visibilityTextView;
    static TextView windTextView;
    static TextView sunriseTextView;
    static TextView sunsetTextView;
    public static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//use the code below to make activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setFlags(WindowManager.LayoutParams.TYPE_WALLPAPER,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        cloudImageView = (ImageView) findViewById(R.id.cloudImageView);
        ImageView1 = (ImageView) findViewById(R.id.ImageView1);
        i1 = (ImageView) findViewById(R.id.i1);
        i2 = (ImageView) findViewById(R.id.i2);
        i3 = (ImageView) findViewById(R.id.i3);
        i4 = (ImageView) findViewById(R.id.i4);
        i5 = (ImageView) findViewById(R.id.i5);
        placeTextView = (TextView) findViewById(R.id.placeTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        pressureTextView = (TextView) findViewById(R.id.pressureTextView);
        humidityTextView = (TextView) findViewById(R.id.humidityTextView);
        visibilityTextView = (TextView) findViewById(R.id.visibilityTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        windTextView = (TextView) findViewById(R.id.windTextView);
        sunriseTextView = (TextView) findViewById(R.id.sunriseTextView);
        sunsetTextView = (TextView) findViewById(R.id.sunsetTextView);
        f1 = (TextView) findViewById(R.id.f1);
        f2 = (TextView) findViewById(R.id.f2);
        f3 = (TextView) findViewById(R.id.f3);
        f4 = (TextView) findViewById(R.id.f4);
        f5 = (TextView) findViewById(R.id.f5);
        mm5 = (TextView) findViewById(R.id.mm5);
        mm4 = (TextView) findViewById(R.id.mm4);
        mm3 = (TextView) findViewById(R.id.mm3);
        mm2 = (TextView) findViewById(R.id.mm2);
        mm1 = (TextView) findViewById(R.id.mm1);
        MainActivity.ImageView1.setImageResource(R.drawable.location2);


        MainActivity.dialog = new ProgressDialog(this);
        //dialog.setIcon(R.drawable.i_snow);
        dialog.setCancelable(false);
        //dialog.setOnCancelListener(null);
        dialog.setMessage("Loading...");
        dialog.show();
        //dialog.hide();

        refresh();
    }

    public void refresh() {
        Toast.makeText(this, "Fetching Data", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocListener loc = new LocListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        DownloadTask task = new DownloadTask();
        //task.execute("http://api.openweathermap.org/data/2.5/weather?lat=51&lon=0&appid=aabd7edd6ed65a8eb1c5cfd90fbec64d");
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(LocListener.lat)+"&lon="+String.valueOf(LocListener.lon)+"&appid=aabd7edd6ed65a8eb1c5cfd90fbec64d");

    }

    /*   @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.menu_main, menu);
           return true;
       }

       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           // Handle action bar item clicks here. The action bar will
           // automatically handle clicks on the Home/Up button, so long
           // as you specify a parent activity in AndroidManifest.xml.
           int id = item.getItemId();

           //noinspection SimplifiableIfStatement
           if (id == R.id.action_settings) {

               refresh();
               return true;
           }
           if (id == R.id.refresh) {

               refresh();
               return true;
           }
           return super.onOptionsItemSelected(item);
       }
   */
    public void addLocation(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(this.getString(R.string.search_title));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        alert.setView(input);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    try {
                        ///////          function calling       ////////////////
                        userInput(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        userInput(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //  saveLocation(result);
                }
            }
        });
        alert.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        alert.show();
    }

    /*
    private String saveLocation(String result) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //city = preferences.getString("city", SyncStateContract.Constants.C);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city", result);
        editor.commit();
        String city=result;

        //  if (!city.equals(result)) {
        // New location, update weather
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        // }
        return result;
    }
    */
    public void userInput(String result) throws IllegalArgumentException, IOException {

        Geocoder geocoder = new Geocoder(this);
        if (geocoder.isPresent()) {
            List<Address> list = geocoder.getFromLocationName(result, 1);
            Address address = list.get(0);
            LocListener.lat = address.getLatitude();
            LocListener.lon = address.getLongitude();
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(LocListener.lat) + "&lon=" + String.valueOf(LocListener.lon) + "&appid=aabd7edd6ed65a8eb1c5cfd90fbec64d");

        }
    }
}
