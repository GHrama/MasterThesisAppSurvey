//package com.example.ramapriyasridharan.BackgroundTasks;
//
//import android.app.ActivityManager;
//import android.app.IntentService;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorListener;
//import android.hardware.SensorManager;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.PowerManager;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.example.ramapriyasridharan.SensorHelpers.*;
//
//import java.util.ArrayList;
//import java.util.EventListener;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by ramapriyasridharan on 06.06.16.
// */
//public class SensorIntentService extends Service implements SensorEventListener {
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//
//    private static final String LOG_TAG = SensorIntentService.class.getSimpleName();
//
//    //initialize this once
//    private Sensor sensorAccelerometer = null;
//
//    // need to be reset on every call
//    private SensorCollectStatus scAccelerometer = null;
//
//    private final IBinder mBinder = new SensorBinder();
//    private SensorManager sensorManager = null;
//
//    private PowerManager.WakeLock wakeLock;
//    private HandlerThread hthread;
//    private Handler handler;
//    private Lock storeMutex;
//
//    private SensorConfiguration sensorConfiguration;
//    private SensorIntentService sensorListenerClass;
//
//    private ConcurrentHashMap<Long, SensorCollectStatus> sensorCollected;
//
//    public class SensorBinder extends Binder {
//        SensorIntentService getService() {
//            return SensorIntentService.this;
//        }
//    }
//
//    public SensorIntentService(String name) {
//
//    }
//
//    public SensorIntentService() {
//
//    }
//
//
//
//    public void initSensor(long sensorId){
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_FASTEST);
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        // Prepare the wakelock
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
//        hthread = new HandlerThread("HandlerThread");
//        hthread.start();
//        // Acquire wakelock, some sensors on some phones need this
//        if (!wakeLock.isHeld()) {
//            wakeLock.acquire();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Release the wakelock here, just to be safe, in order something went wrong
//        if (wakeLock.isHeld()) {
//            wakeLock.release();
//        }
//        sensorManager.unregisterListener(this);
//        hthread.quit();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    SensorCollectStatus sensorCollectStatus = null;
//
//    public void onSensorChanged(final SensorEvent event) {
//        sensorCollectStatus = null;
//        final long startTime = System.currentTimeMillis();
//
//        handler = new Handler(hthread.getLooper());
//        final Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Log.d("sensor", "onsensorChanged");
//                ArrayList<SensorDesc> sensorDescs = new ArrayList<SensorDesc>();
//
//                long timestamp = System.currentTimeMillis();
//                Sensor sensor = event.sensor;
//                SensorDesc sensorDesc = null;
//
//                switch (sensor.getType()) {
//                    case Sensor.TYPE_ACCELEROMETER:
//
//                        Log.d("sensor", "sensorId == SensorDescAccelerometer.SENSOR_ID");
//                        //scAccelerometer.setMeasureStart(startTime);
//                        sensorCollectStatus = scAccelerometer;
//                        Log.d("sensor", "ACC onSensorChanged");
//                        sensorDesc = new SensorDescAccelerometer(timestamp, event.values[0], event.values[1], event.values[2]);
//                        Log.d(LOG_TAG, "Accelerometer data collected");
//
//                        if (sensorCollectStatus != null) {
//                            sensorCollected.put(SensorDescAccelerometer.SENSOR_ID, sensorCollectStatus);
//                            Log.d("sensor", "sensorCollectStatus != null");
//                            //long interval = sensorCollectStatus.getMeasureInterval();
//                            Log.d(LOG_TAG, "Logging sensor " + String.valueOf(SensorDescAccelerometer.SENSOR_ID) + " started with interval " + String.valueOf(interval) + " ms");
//
//                        }
//
//                        break;
//                }
//
//                sensorDescs.add(sensorDesc);
//                store(sensorDesc.getSensorId(), sensorDescs);
//            }
//        };
//        // 10 seconds initial delay
//        handler.postDelayed(run, 10000);
//
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    private synchronized void store(long sensorId, List<SensorDesc> sensorDescs) {
//        Log.d("sensor", "store");
//        storeMutex.lock();
//        SensorCollectStatus scs = sensorCollected.get(sensorId);
//        Log.d("sensor", "scs = " + scs);
//
//        if (sensorDescs != null && !sensorDescs.isEmpty()) {
//            Log.d("sensor", "sensorDescs != null && !sensorDescs.isEmpty()");
//            if (scs != null) {
//                Log.d("sensor", "scs != null");
//                if (true) {
//                    // Collected new data of this type, count it
//                    //scs.increaseCollectAmount();
//                    // Enough collected, remove from list
//                    //if (scs.isDone(System.currentTimeMillis())) {
//                        //sensorCollected.remove(scs.getSensorId());
//                        // Remove from listener list
//                        //Log.d("sensor", "unregister acc1");
//                        //unregisterSensor(scs.getSensorId());
//                    }
//                    for (SensorDesc sensorDesc : sensorDescs) {
//                        Log.d("sensor", "store Acc data");
//                        new StoreTask(getApplicationContext()).execute(sensorDesc);
//                    }
//                }
//            }
//        }
//        //storeMutex.unlock();
//    }
//
//    private void unregisterSensor(long sensorId) {
//        if (sensorId == SensorDescAccelerometer.SENSOR_ID) {
//            sensorManager.unregisterListener(this, sensorAccelerometer);
//        }
//    }
//
//    public static boolean isServiceRunning(Context context) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (SensorIntentService.class.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//
//        Log.d("sensor", "entered handleintent");
//        storeMutex = new ReentrantLock();
//        sensorListenerClass = this;
//        sensorConfiguration = SensorConfiguration.getInstance(getApplicationContext());
//
//        // Initialize sensor manager
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//
//        // Hash map to register sensor collect status references
//        sensorCollected = new ConcurrentHashMap<Long, SensorCollectStatus>();
//
//        // Initialize sensor collect status from configuration
//        scAccelerometer = sensorConfiguration.getInitialSensorCollectStatus(SensorDescAccelerometer.SENSOR_ID);
//
//        // Get references to android default sensors
//        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//        // Schedule all sensors (initially)
//        //scheduleSensor(SensorDescAccelerometer.SENSOR_ID);
//        initSensor(SensorDescAccelerometer.SENSOR_ID);
//        Log.d(LOG_TAG, "Service execution started");
//
//        return START_STICKY;
//    }
//
//    public static void startService(Context context) {
//        Intent sensorIntent = new Intent(context, SensorIntentService.class);
//        context.startService(sensorIntent);
//    }
//
//    public static void stopService(Context context) {
//        Intent sensorIntent = new Intent(context, SensorIntentService.class);
//        context.stopService(sensorIntent);
//    }
//
//
//
//}
//
//
