package com.iot_projects.taas;

import android.app.Application;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adhit on 4/6/2017.
 */

public class BackgroundService extends Application implements BootstrapNotifier, BeaconConsumer {
    private static final String TAG = "BackgroundScan";
    private RegionBootstrap regionBootstrap;
    private BeaconManager mBeaconManager;
    private String postAddress = "http://192.168.0.10:8080/";
    Region regions[];

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
