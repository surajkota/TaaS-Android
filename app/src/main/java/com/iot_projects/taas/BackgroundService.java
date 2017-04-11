package com.iot_projects.taas;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Interval;
import com.iot_projects.taas.models.Medication;
import com.iot_projects.taas.models.Subscription;
import com.iot_projects.taas.models.Treatment;
import com.iot_projects.taas.util.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adhit on 4/6/2017.
 */

public class BackgroundService extends Application implements BootstrapNotifier, BeaconConsumer, RangeNotifier {
    private static final String TAG = "BackgroundScan";
    private RegionBootstrap regionBootstrap;
    private BeaconManager mBeaconManager;
    private String postAddress = Constants.baseURL;
    Region regions[];
    public static Subscription subscription = new Subscription();
    static MqttAndroidClient mqttAndroidClient;
    private Map<String,String> uidToUrl = new HashMap<>();
    Map<String, Interval> foodNearBy;
    Map<String, Long> foodTimeStamp;

    final String serverUri = Constants.brokerURL;

    String clientId = "ExampleAndroidClient";

    @Override
    public void onCreate() {

        super.onCreate();
        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().clear();
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry Eddystone-TLM frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        //set period of scan in background and foreground. In every period 'didRangeBeaconsInRegion' callback is called
        mBeaconManager.setForegroundScanPeriod(2500);
        mBeaconManager.setForegroundBetweenScanPeriod(10000);
        mBeaconManager.setBackgroundScanPeriod(2500);
        mBeaconManager.setBackgroundBetweenScanPeriod(10000);
        BeaconManager.setAndroidLScanningDisabled(true);
        mBeaconManager.bind(this);

        uidToUrl.put("0x101010101000", "food1");
        uidToUrl.put("0x101010101001", "food2");
        uidToUrl.put("0x101010101010", "food3");
        uidToUrl.put("0x101010101011", "food4");

        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.d("Debug","Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                } else {
                    Log.d("Debug","Connected to: " + serverURI);
                    String s = "";
                    SharedPreferences settings = getSharedPreferences("myPref", 0);
                    if(settings.contains("subscription"))
                        s = settings.getString("subscription", "");
                    Log.d("Debug", s);
                    if(!s.equals(""))
                    {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Subscription subscription = null;
                        try {
                            subscription = objectMapper.readValue(s,Subscription.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Treatment treatment = subscription.getTreatment();
                        for(Medication m : treatment.getMedication())
                            subscribeToTopic(m.getMedicineId());
                    }
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("Debug","The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("Debug","Incoming message: " + new String(message.getPayload()) + " from "+ topic);
                Long timeNow = System.currentTimeMillis();
                String s = "";
                SharedPreferences settings = getSharedPreferences("myPref", 0);
                if (settings.contains("subscription"))
                    s = settings.getString("subscription", "hi");
                if(!s.equals("hi")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Subscription subscription = objectMapper.readValue(s,Subscription.class);
                        Log.d("Debug", subscription.toString());
                        int flag = 0;
                        int notProperTime = 0;
                        if(subscription.getImproperTimeMedicine().containsKey(topic))
                            notProperTime = subscription.getImproperTimeMedicine().get(topic);
                        if(subscription.getMedicineNotTakenMap().containsKey(topic))
                        {
                            Map<Long, Boolean> medicineNotTaken = subscription.getMedicineNotTakenMap().get(topic);
                            for(Long key : medicineNotTaken.keySet())
                            {
                                if(Math.abs(key-timeNow)<=3600000)
                                {
                                    Log.d("Debug", "Medicine " + topic + " taken at "+ key);
                                    medicineNotTaken.put(key,false);
                                    flag = 1;
                                }
                            }
                            if(flag==0)
                            {
                                subscription.getImproperTimeMedicine().put(topic, notProperTime+1);
                                if((notProperTime+1)>=5)
                                {
                                    //TODO Report to doctor on irregular medicine intake. Send post request to baseUrl/medicineIrregular with subscription id and medicine name as payload
                                    Toast.makeText(BackgroundService.this, "Medicine " + topic + " is taken irregularly more than 5 times!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            subscription.getMedicineNotTakenMap().put(topic, medicineNotTaken);
                            ObjectMapper objMapper = new ObjectMapper();
                            String subsStr = objMapper.writeValueAsString(subscription);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("subscription", subsStr);
                            Log.d("Debug", subsStr);
                            editor.commit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Debug","Failed to connect to: " + serverUri);
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public static void subscribeToTopic(final String subscriptionTopic){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("Debug","Subscribed to "+subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Debug","Failed to subscribe to "+subscriptionTopic);
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public static void publishMessage(String publishTopic, String publishMessage){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            Log.d("Debug","Message Published");
            if(!mqttAndroidClient.isConnected()){
                Log.d("Debug",mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d("Debug", "Enabling region here!");
        Region region = new Region("all-beacons-region", Identifier.parse("0x10101010101010101010"), null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon: beacons) {
            Log.d("Debug", beacon.getId1().toString());
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();

                Log.d("Debug", "Beacon enters with namespace id " + namespaceId + " and instance id: " + instanceId + " at a distance "+beacon.getDistance());

                String food = "";
                if(uidToUrl.containsKey(instanceId.toString()))
                        food = uidToUrl.get(instanceId.toString());
                if(!food.equals(""))
                {
                    if(foodNearBy.containsKey(food))
                    {
                        Long timeNow = System.currentTimeMillis();
                        if(timeNow-foodNearBy.get(food).getEndTime() > 300000)
                        {
                            Log.d("Debug", "Initializing new food nearby!");
                            foodNearBy.put(food,new Interval(timeNow,timeNow));
                        }
                        else
                        {
                            Interval interval = foodNearBy.get(food);
                            interval.setEndTime(timeNow);
                            if(interval.getEndTime() - interval.getStartTime() > 300000)
                            {
                                //TODO Get the ingredients of the food and check if restricted food is present in the ingredients.
                                Log.d("Debug","Patient near restricted food " + food + " for more than 5 minutes!");
                                //TODO Report to doctor! Alert the patient of wrong eating habit.
                                AsyncPost asyncPost = new AsyncPost(postAddress + "restrictedFood", instanceId.toString());
                                asyncPost.execute();
                                interval.setStartTime(timeNow);
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public void didEnterRegion(Region region) {}

    @Override
    public void didExitRegion(Region region) {}

    @Override
    public void didDetermineStateForRegion(int i, Region region) {}
}
