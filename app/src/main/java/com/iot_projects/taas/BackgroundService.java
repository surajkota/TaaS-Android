package com.iot_projects.taas;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Medication;
import com.iot_projects.taas.models.Subscription;
import com.iot_projects.taas.models.Treatment;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adhit on 4/6/2017.
 */

public class BackgroundService extends Application implements BootstrapNotifier, BeaconConsumer {
    private static final String TAG = "BackgroundScan";
    private RegionBootstrap regionBootstrap;
    private BeaconManager mBeaconManager;
    private String postAddress = "http://192.168.0.10:8080/";
    Region regions[];
    public static Subscription subscription = new Subscription();
    static MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://192.168.0.10:1883";

    String clientId = "ExampleAndroidClient";

    public void onCreate() {

        super.onCreate();
        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().clear();

        //set Beacon Layout for iBeacon packet
        /*mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));*/
        //set Beacon Layout for Eddystone-UID packet
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));

        //set period of scan in background and foreground. In every period 'didRangeBeaconsInRegion' callback is called
        mBeaconManager.setForegroundScanPeriod(3000);
        mBeaconManager.setForegroundBetweenScanPeriod(500);
        mBeaconManager.setBackgroundScanPeriod(3000);
        mBeaconManager.setBackgroundBetweenScanPeriod(1000);
        BeaconManager.setAndroidLScanningDisabled(true);


        regions = new Region[4];
        //With "new Region" you are adding the beacon identifier to the list that will be checked in every Scan Period
        //To add iBeacon region it's necessary to pass as parameters --> (uniqueId = region name, id1=uuid, id2 = major, id3 = minor)
        regions[0] = new Region("area1", Identifier.parse("0x10101010101010101010"), Identifier.parse("0x101010101000"), null);
        //To add Eddystone-UID region it's necessary to pass as parameters --> (uniqueId = region name, id1=namespace, id2 = instance, id3 = null)
        regions[1] = new Region("area2", Identifier.parse("0x10101010101010101010"), Identifier.parse("0x101010101001"), null);
        regions[2] = new Region("area3", Identifier.parse("0x10101010101010101010"), Identifier.parse("0x101010101010"), null);
        regions[3] = new Region("area4", Identifier.parse("0x10101010101010101010"), Identifier.parse("0x101010101011"), null);

        mBeaconManager.bind(this);

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
                        s = settings.getString("subscription", "hi");
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

    public void enableRegions() {
        try {

            if (regionBootstrap == null) {
                List<Region> list = new ArrayList<>();
                for (int i = 0; i < regions.length; i++) {
                    if (regions[i] != null) {
                        list.add(regions[i]);
                    }
                }
                regionBootstrap = new RegionBootstrap(this, list);
            }

            for (int i = 0; i < regions.length; i++) {
                if (regions[i] != null) {
                    mBeaconManager.startRangingBeaconsInRegion(regions[i]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableRegions() {
        try {
            if (regionBootstrap != null)
                regionBootstrap.disable();
            regionBootstrap = null;

            for (int i = 0; i < regions.length; i++) {
                if (regions[i] != null) {
                    mBeaconManager.stopRangingBeaconsInRegion(regions[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        enableRegions();
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "Beacon enters with namespace id " + region.getId1() +" and instance id: " + region.getId2().toString());
        Log.d(TAG, "Sending post request to node!");
        AsyncPost asyncPost = new AsyncPost(postAddress+"incrementCounter", region.getId2().toString());
        asyncPost.execute();
    }

    @Override
    public void didExitRegion(Region region) {

        Log.d(TAG, "Beacon out of region with namespace id " + region.getId1() +" and instance id: " + region.getId2());
        AsyncPost asyncPost = new AsyncPost(postAddress+"decrementCounter", region.getId2().toString());
        asyncPost.execute();
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        //Ignore
    }
}
