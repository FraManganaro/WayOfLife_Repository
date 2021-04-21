package com.example.wayoflife;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wayoflife.pushup.PushupCounterActivity;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    Button btNotification;

    public static final String TAG = "ProfileActivity";
    public static final String NOTIFICATION_CHANNEL = "notification_channel";

    /**
     * Oggetti per ActivityRecognition
     */
    // TODO: Review check for devices with Android 10 (29+).
    private boolean runningQOrLater =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    private boolean activityTrackingEnabled;

    private List<ActivityTransition> activityTransitionList;

    // Action fired when transitions are triggered.
    private final String TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";

    private PendingIntent mActivityTransitionsPendingIntent;
    private TransitionsReceiver mTransitionsReceiver;


    /**
     * Devo creare un canale per le notifiche
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL, "Canale di Notifiche",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Questo é il canale delle notifiche");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            default:
                return "UNKNOWN";
        }
    }
    private static String toTransitionType(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }
    private void printToScreen(@NonNull String message) {
        Log.d(TAG, message);
    }

    /**
     * On devices Android 10 and beyond (29+), you need to ask for the ACTIVITY_RECOGNITION via the
     * run-time permissions.
     */
    private boolean activityRecognitionPermissionApproved() {
        // TODO: Review permission check for 29+.
        if (runningQOrLater) {
            return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
            );
        } else {
            return true;
        }
    }

    /**
     * Lista di transazioni e attività che voglio che vengano riconosciute
     * @return
     */
    private ArrayList buildTransition(){
        ArrayList activityTransitionList = new ArrayList<>();

        // TODO: Add activity transitions to track.

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        return activityTransitionList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        createNotificationChannel();

        btNotification = findViewById(R.id.bt_notification);
        btNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = "This is a notification";

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                        MainActivity2.this, NOTIFICATION_CHANNEL);
//
//                builder.setSmallIcon(R.drawable.ic_home_black_24dp)
//                        .setContentTitle("New Notification")
//                        .setContentText(message)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setAutoCancel(true);

                /**
                 * Creo l'intent che mi permetterà di viaggiare da un'activity all'altra
                 */
                Intent intent = new Intent(MainActivity2.this, NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message", message);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity2.this,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //builder.setContentIntent(pendingIntent);

                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(MainActivity2.this);

                /**
                 * Creo la notifica e ci assegno il PendingIntent appena creato
                 */
                Notification notification = new NotificationCompat.Builder(
                        MainActivity2.this,
                        NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.ic_home_black_24dp)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .setContentIntent(pendingIntent)
                        .build();

                notificationManagerCompat.notify(1, notification);

//                notificationManagerCompat.notify(1, builder.build());
            }
        });

        activityTrackingEnabled = false;

        // List of activity transitions to track.
        activityTransitionList = buildTransition();

        /*
        // TODO: Add activity transitions to track.
        //Creo la lista di elementi che voglio riconoscere
        //In particola considereo INIZIO e FINE camminata e lo star fermi
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        */

        // TODO: Initialize PendingIntent that will be triggered when a activity transition occurs.
        //Creo un PendingIntent che posso triggerare quando uno ActivityTransition accorre
        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION); //nota che cosa viene passato come parametro
        mActivityTransitionsPendingIntent =
                PendingIntent.getBroadcast(MainActivity2.this, 0, intent, 0);



        // TODO: Create a BroadcastReceiver to listen for activity transitions.
        // The receiver listens for the PendingIntent above that is triggered by the system when an
        // activity transition occurs.
        mTransitionsReceiver = new TransitionsReceiver();

        printToScreen("App initialized.");
    }
    @Override
    protected void onStart() {
        super.onStart();

        // TODO: Register the BroadcastReceiver to listen for activity transitions.
        registerReceiver(mTransitionsReceiver, new IntentFilter(TRANSITIONS_RECEIVER_ACTION));
    }
    @Override
    protected void onPause() {
        // TODO: Disable activity transitions when user leaves the app.

        if (activityTrackingEnabled) {
            disableActivityTransitions();
        }

        super.onPause();
    }
    @Override
    protected void onStop() {
        // TODO: Unregister activity transition receiver when user leaves the app.

        //Tolgo dall'ascolto il Receiver
        unregisterReceiver(mTransitionsReceiver);

        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Start activity recognition if the permission was approved.
        if (activityRecognitionPermissionApproved() && !activityTrackingEnabled) {
            enableActivityTransitions();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Registers callbacks for {@link ActivityTransition} events via a custom
     * {@link BroadcastReceiver}
     */
    private void enableActivityTransitions() {
        // TODO: Create request and listen for activity changes.

        Snackbar.make(findViewById(android.R.id.content), "ActivityTracker attivato",
                Snackbar.LENGTH_LONG).show();
        Switch myS = findViewById(R.id.switchAT);
        myS.setChecked(true);

        //Abbiamo creato una richiesta passando la lista precedentemente creata nel main
        ActivityTransitionRequest request = new ActivityTransitionRequest(activityTransitionList);

        // Register for Transitions Updates.
        //Chiamo il requestActivityTransitionUpdate passando la RICHIESTA appena inizializzata e l'intent dell'onCreate
        Task<Void> task =
                ActivityRecognition.getClient(this)
                        .requestActivityTransitionUpdates(request, mActivityTransitionsPendingIntent);

        //In questo modo so quando l'utente attiva e disattiva il servizio
        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        activityTrackingEnabled = true;
                        printToScreen("Transitions Api was successfully registered.");

                    }
                });
        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        printToScreen("Transitions Api could NOT be registered: " + e);
                        Log.e(TAG, "Transitions Api could NOT be registered: " + e);

                    }
                });
    }

    /**
     * Unregisters callbacks for {@link ActivityTransition} events via a custom
     * {@link BroadcastReceiver}
     */
    private void disableActivityTransitions() {
        // TODO: Stop listening for activity changes.

        Snackbar.make(findViewById(android.R.id.content), "ActivityTracker disattivato",
                Snackbar.LENGTH_LONG).show();
        Switch myS = findViewById(R.id.switchAT);
        myS.setChecked(false);

        //Rimuovere gli update quando l'applicazione e chiusa

        ActivityRecognition.getClient(this).removeActivityTransitionUpdates(mActivityTransitionsPendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activityTrackingEnabled = false;
                        printToScreen("Transitions successfully unregistered.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        printToScreen("Transitions could not be unregistered: " + e);
                        Log.e(TAG,"Transitions could not be unregistered: " + e);
                    }
                });
    }


    public void onClickEnableOrDisableActivityRecognition(View view) {
        printToScreen("onClickEnableOrDisableActivityRecognition");

        // TODO: Enable/Disable activity tracking and ask for permissions if needed.
        if (activityRecognitionPermissionApproved()) {

            if (activityTrackingEnabled) {
                disableActivityTransitions();

            } else {
                enableActivityTransitions();
            }

        } else {
            // Request permission and start activity for result. If the permission is approved, we
            // want to make sure we start activity recognition tracking.
            Intent startIntent = new Intent(this, PermissionRationalActivity.class);
            startActivityForResult(startIntent, 0);
        }
    }


    /**
     * Handles intents from from the Transitions API.
     */
    public class TransitionsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String info = "";
            String enterActivity = "";
            Log.d(TAG, "onReceive(): " + intent);

            if (!TextUtils.equals(TRANSITIONS_RECEIVER_ACTION, intent.getAction())) {
                printToScreen("Received an unsupported action in TransitionsReceiver: action = " +
                        intent.getAction());
                return;
            }

            // TODO: Extract activity transition information from listener.

            if (ActivityTransitionResult.hasResult(intent)) {

                ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);

                for (ActivityTransitionEvent event : result.getTransitionEvents()) {

                    info = "Transition: " +
                            toActivityString(event.getActivityType()) +
                            " (" + toTransitionType(event.getTransitionType()) + ")" + "   " +
                            new SimpleDateFormat("HH:mm:ss", Locale.ITALIAN).format(new Date());

                    printToScreen(info);

                    if(toTransitionType(event.getTransitionType()).equalsIgnoreCase("ENTER")){
                        enterActivity = toActivityString(event.getActivityType());
                    }

                }
            }

            if(enterActivity!=null) {
                /**
                 * Creo l'intent che mi permetterà di viaggiare da un'activity all'altra
                 */
                Intent intentOut = new Intent(MainActivity2.this, NotificationActivity.class);
                intentOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentOut.putExtra("message", enterActivity);

                PendingIntent pendingIntentOut = PendingIntent.getActivity(MainActivity2.this,
                        0, intentOut, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(MainActivity2.this);

                /**
                 * Creo la notifica e ci assegno il PendingIntent appena creato
                 */
                Notification notification = new NotificationCompat.Builder(
                        MainActivity2.this,
                        NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.ic_home_black_24dp)
                        .setContentTitle("New Notification")
                        .setContentText(enterActivity)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_EVENT)
                        .setContentIntent(pendingIntentOut)
                        .build();

                notificationManagerCompat.notify(1, notification);
            }
        }
    }


    public void goToPushupCounter(View v){
        Intent intent = new Intent(getApplicationContext(), PushupCounterActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}