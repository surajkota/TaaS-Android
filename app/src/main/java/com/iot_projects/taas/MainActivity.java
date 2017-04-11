package com.iot_projects.taas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Medication;
import com.iot_projects.taas.models.Treatment;
import com.iot_projects.taas.databinding.ActivityMainBinding;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //private TextView mTextMessage;
    private Button subscribeButton;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_COARSE_BL = 2;

    ActivityMainBinding activityMainBinding;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = activityMainBinding.toolbar;
        setSupportActionBar(toolbar);
        drawerLayout = activityMainBinding.drawerLayout;
        setupDrawerContent(activityMainBinding.navigationView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        activityMainBinding.navigationView.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, TreatmentsFragment.class.newInstance()).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //mTextMessage = (TextView) findViewById(R.id.message);
        subscribeButton = (Button) findViewById(R.id.subscribeButton);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Debug", "Subscribe Button Clicked!");
                AsyncGet asyncGet = new AsyncGet("http://192.168.0.10:8080/getTDL", MainActivity.this);
                asyncGet.start();
            }
        });

        checkLocBT();
        initializeBluetooth();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = TreatmentsFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = SubscribeFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = TreatmentsFragment.class;
                break;
            default:
                fragmentClass = TreatmentsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private BluetoothAdapter mBTAdapter;

    private void initializeBluetooth(){

        //Check if device does support BT by hardware
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLUETOOTH NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Check if device does support BT Low Energy by hardware. Else close the app(finish())!
        if (!getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast shows a message on the screen for a LENGTH_SHORT period
            Toast.makeText(this, "BLE NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            //If BLE is supported, get the BT adapter. Preparing for use!
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            //If getting the adapter returns error, close the app with error message!
            if (mBTAdapter == null) {
                Toast.makeText(this, "ERROR GETTING BLUETOOTH ADAPTER!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Check if BT is enabled! This method requires BT permissions in the manifest.
                if (!mBTAdapter.isEnabled()) {
                    //If it is not enabled, ask user to enable it with default BT enable dialog! BT enable response will be received in the onActivityResult method.
                    Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBTintent, PERMISSION_REQUEST_COARSE_BL);
                }
            }
        }
    }

    @TargetApi(23)
    private void checkLocBT(){
        //If Android version is M (6.0 API 23) or newer, check if it has Location permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //If Location permissions are not granted for the app, ask user for it! Request response will be received in the onRequestPermissionsResult.
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        //Check if permission request response is from Location
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //User granted permissions. Setup the scan settings
                    Log.d("TAG", "coarse location permission granted");
                } else {
                    //User denied Location permissions. Here you could warn the user that without
                    //Location permissions the app is not able to scan for BLE devices and eventually
                    //Close the app
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the response is from BT
        if(requestCode == PERMISSION_REQUEST_COARSE_BL){
            // User chose not to enable Bluetooth.
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void installTDL() {
        Treatment treatment = BackgroundService.subscription.getTreatment();
        int intentId = 0;
        Map<String,Map<Long, Boolean>> medicineNotTakenMap = new HashMap<>();
        Map<String,Integer> improperTimeMedicine = new HashMap<>();
        Map<String,Integer> skippedMedicine = new HashMap<>();
        for(Medication m : treatment.getMedication()) {
            Map<Long, Boolean> medicineNotTaken = new HashMap<>();
            BackgroundService.subscribeToTopic(m.getMedicineId());
            improperTimeMedicine.put(m.getMedicineId(),0);
            skippedMedicine.put(m.getMedicineId(),0);
            for(String time : m.getTimeThreshold()) {
                int hour, minute;
                String[] tokens = time.split(":");
                String ampm = tokens[1].substring(tokens[1].length()-2);
                Log.d("Debug", ampm);
                if(ampm.equals("AM"))
                {
                    hour = Integer.parseInt(tokens[0]);
                    if(hour==12)
                        hour = 0;
                }
                else
                    hour = 12+Integer.parseInt((tokens[0]));
                minute = Integer.parseInt(tokens[1].substring(0,tokens[1].length()-2).replaceAll("\\s+",""));
                Log.d("Debug", "Hour : "+hour);
                Log.d("Debug", "Minute : "+minute);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                Long time1 = calendar.getTimeInMillis();
                Log.d("Debug","Putting "+ time1 + " to true");
                medicineNotTaken.put(time1,true);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent;
                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                alarmIntent.putExtra("medicine", m.getMedicineId());
                alarmIntent.putExtra("time", time1.toString());
                Log.d("Debug", "Putting extra "+time1);
                pendingIntent = PendingIntent.getBroadcast(this, intentId, alarmIntent, 0);
                intentId++;
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time1, AlarmManager.INTERVAL_DAY, pendingIntent);
                Log.d("Debug","Alarm for medicine "+ m.getMedicineId()+ " and time "+ time1 + " is set");
            }
            medicineNotTakenMap.put(m.getMedicineId(),medicineNotTaken);
        }
        BackgroundService.subscription.setMedicineNotTakenMap(medicineNotTakenMap);
        BackgroundService.subscription.setImproperTimeMedicine(improperTimeMedicine);
        BackgroundService.subscription.setSkippedMedicine(skippedMedicine);
        ObjectMapper subscMapper = new ObjectMapper();
        String subscStr = null;
        try {
            subscStr = subscMapper.writeValueAsString(BackgroundService.subscription);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d("Debug", subscStr);
        SharedPreferences settings = getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("subscription", subscStr);
        // Commit the edits!
        editor.commit();
    }
}
