package com.example.wayoflife.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.example.wayoflife.BuildConfig;
import com.example.wayoflife.Constants;
import com.example.wayoflife.NotificationActivity;
import com.example.wayoflife.PermissionRationalActivity;
import com.example.wayoflife.R;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    public static final String NOTIFICATION_CHANNEL = "notification_channel";

    /**
     * Oggetti per ActivityRecognition
     */
    // Review check for devices with Android 10 (29+).
    private boolean runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    private boolean activityTrackingEnabled;

    private List<ActivityTransition> activityTransitionList;

    // Action fired when transitions are triggered.
    private final String TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";


    private PendingIntent mActivityTransitionsPendingIntent;
    private HomeActivity.TransitionsReceiver mTransitionsReceiver;

    /**
     * Creo un canale per le notifiche
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL, "Canale di Notifiche",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Canale delle notifiche della Home");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Converto in stringa i parametri ricevuti dalla classe DetectedActivity
     * @param activity
     * @return
     */
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

    /** Stampa nella console */
    private void printToScreen(@NonNull String message) { Log.d(TAG, message); }

    /**
     * Nei dispositivi con Android 10 e precedente (29+), bisogna chiedere i permessi attraverso un
     * run-time permissions.
     */
    private boolean activityRecognitionPermissionApproved() {
        // Review permission check for 29+.
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

        //Da rimuovere
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        activityTransitionList.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());

        //Da rimuovere
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
        setContentView(R.layout.activity_home);

        /**
         * Controllore della navigation bar
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:

                    case R.id.allenamento:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;

                    case R.id.profilo:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return false;
                }
                return false;
            }
        });


        /** Se ho qualcosa da ripristinare lo rispristino da qui */
        if(savedInstanceState != null) {
            activityTrackingEnabled = savedInstanceState.getBoolean(Constants.ACTIVITY_TRACKING_STATUS);

        }else { /** Atrimenti inizializzo gli oggetti da capo */
            activityTrackingEnabled = false;
        }

        createNotificationChannel();

        // Costruisco la lista delle transazioni da riconoscere
        activityTransitionList = buildTransition();

        //Creo un PendingIntent che posso triggerare quando uno ActivityTransition accorre
        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
        mActivityTransitionsPendingIntent =
                PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);

        //Inizializzo un BroadcastReceiver per ascoltare le transazioni dell'utente
        mTransitionsReceiver = new HomeActivity.TransitionsReceiver();

        /** Recupero le informazioni che sono state memorizzate all'interno del file */
        resumeInformation();

        printToScreen("App initialized.");
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Registro il BroadcastReceiver per ascoltare le attività
        registerReceiver(mTransitionsReceiver, new IntentFilter(TRANSITIONS_RECEIVER_ACTION));
    }

    @Override
    protected void onPause() {
        // Disable activity transitions when user leaves the app.
        // Rimuovere il commento per stoppare riconoscimento quando si esce dall'activity
//        if (activityTrackingEnabled) { disableActivityTransitions(); }

        super.onPause();
    }
    @Override
    protected void onStop() {
        // Unregister activity transition receiver when user leaves the app.
        // Rimuovere il commento per stoppare riconoscimento quando si esce dall'activity
//        unregisterReceiver(mTransitionsReceiver);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(Constants.ACTIVITY_TRACKING_STATUS, activityTrackingEnabled);
    }

    /**
     * Salvo le informazioni dei bottoni che sono stati premuto oppure no
     * In questo modo anche se l'applicazione dovesse essere killara avrei lo stesso i progressi
     */
    private void saveInformation(){
        SharedPreferences sharedPref = getSharedPreferences(
                //Nome del file
                Constants.HOME_INFO_FILENAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        /**
         * Salvo se é stato attivato oppure no l'ActivityRecognitionClient
         */
        if(activityTrackingEnabled){
            //Salvo che é stato premuto l'oggetto relativo all'italia
            editor.putBoolean(Constants.ACTIVITY_TRACKING_STATUS, true);
        }else{
            editor.putBoolean(Constants.ACTIVITY_TRACKING_STATUS, false);
        }

        editor.apply();
    }
    private void resumeInformation(){
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.HOME_INFO_FILENAME,
                Context.MODE_PRIVATE);

        activityTrackingEnabled = sharedPref.getBoolean(
                Constants.ACTIVITY_TRACKING_STATUS, false);

        Switch sw = findViewById(R.id.switchAT);
        sw.setChecked(activityTrackingEnabled);

        if(activityTrackingEnabled) enableActivityTransitions();
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

        Switch myS = findViewById(R.id.switchAT);
        myS.setChecked(true);

        //Creo richiesta passando come oggetto la lista precedentemente creata
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
                        saveInformation();
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
        // Stop listening for activity changes.

        Snackbar.make(findViewById(android.R.id.content), "ActivityTracker disattivato",
                Snackbar.LENGTH_SHORT).show();

        Switch myS = findViewById(R.id.switchAT);
        myS.setChecked(false);

        //Rimuovere gli update quando non più necessari
        ActivityRecognition.getClient(this).removeActivityTransitionUpdates(mActivityTransitionsPendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activityTrackingEnabled = false;
                        saveInformation();
                        printToScreen("Transitions successfully unregistered.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        printToScreen("Transitions could not be unregistered: " + e);
                    }
                });
    }


    public void onClickEnableOrDisableActivityRecognition(View view) {
        printToScreen("onClickEnableOrDisableActivityRecognition");

        // Enable/Disable activity tracking and ask for permissions if needed.
        if (activityRecognitionPermissionApproved()) {

            if (activityTrackingEnabled) {
                disableActivityTransitions();

            } else {
                enableActivityTransitions();
                Snackbar.make(findViewById(android.R.id.content), "ActivityTracker attivato",
                        Snackbar.LENGTH_SHORT).show();
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


                    //Stampo nella console transizione di ingresso e di uscite
                    printToScreen(info);

                    //Salvo solo la transazione di INGRESSO
                    if(toTransitionType(event.getTransitionType()).equalsIgnoreCase("ENTER")){
                        enterActivity = toActivityString(event.getActivityType());
                    }

                }
            }

            if(enterActivity!=null) {
                /**
                 * Creo l'intent che mi permetterà di viaggiare da un'activity all'altra
                 */
                Intent intentOut = new Intent(HomeActivity.this, NotificationActivity.class); //Da cambiare quando voglio redirectare dalla notifica all'allenamento
                intentOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentOut.putExtra("message", enterActivity);

                PendingIntent pendingIntentOut = PendingIntent.getActivity(HomeActivity.this,
                        0, intentOut, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(HomeActivity.this);

                /**
                 * Creo la notifica e ci assegno il PendingIntent appena creato
                 */
                Notification notification = new NotificationCompat.Builder(
                        HomeActivity.this,
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
}