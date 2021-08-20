# WayOfLife

L’applicazione sviluppata può essere categorizzata come un “fitness tracker” ovvero un’applicazione devota ad essere utilizzata da tutte quelle persone che hanno necessità di tracciare i propri allenamenti.
**WayOfLife** permette all’utente di tracciare a 360 gradi i propri allenamenti e di avere un recap completo di tutte le attività svolte nel tempo. In particolare, si ha la possibilità di tracciare undici allenamenti: *Camminata, Corsa, Ciclismo. Flessioni, Squat, Freestyle, Basket, Calcio, Nuoto, Scalini e Tennis*.

![Immagine 2021-08-20 194947](https://user-images.githubusercontent.com/65859032/130273797-17f85a91-a4ba-4fe3-bbe2-b47baec991e1.png)


## dialog

InfoDialog.java - *home_info_dialog.xml, dashboard_info_dialog.xml*

ProfileDialog.java - *profile_dialog.xml*

TipsDialog.java - *bilancio_energetico_info_dialog.xml, effetto_termico_info_dialog.xml, indice_attivita_info_dialog.xml*


## ui

HomeActivity.java - *activity_home.xml*

DashboardActivity.java - *activity_dashboard.xml*

ProfileActivity.java - *activity_profile.xml*

FirstAccessActivity.java - *activity_first_access.xml*

TipsActivity.java - *activity_tips.xml*



## workouts

#### trainings 

PushupActivity.java - *activity_pushup_counter.xml* <br/> 
Activity all’interno della quale è realizzato un collegamento con il **sensore di prossimità** per calcolare il numero di flessioni effettuate in modo automatico.

TrainingActivity.java - *activity_freestyle.xml*

RunningActivity.java - *activity_running.xml* <br/> 
Activity caratterizzata dall’utilizzo di una mappa per segnare il percorso dell’utente in tempo reale. 
Interfacciamento con il GPS e con delle GoogleMaps API per ottenere posizione dell’utente in tempo reale e per il calcolo dei chilometri effettuati durante l’allenamento

SquatActivity.java - *activity_squat.xml* <br/> 
Activity all’interno della quale è realizzato un collegamento con **l’accelerometro** per visualizzare lo spostamento del dispositivo e, eventualmente, contare il numero di squat effettuati, in modo automatico.


#### ui

EndWorkoutActivity.java - *activity_end_workout.xml*

WorkoutHistoryActivity.java - *activity_workout_history.xml*

WorkoutDetailActivity.java - *activity_detail_workout.xml*

MyListViewElement.java -> *viene definita la struttura di un elemento inserito nella lista di cronologia degli allenementi*


#### util

WorkoutDatabase.java -> *classe che implemenenta il database interno all'applicazione*

WorkoutModel.java -> *oggetto che viene collocato all'interno del database per memorizzare informazioni relative agli allenamenti*



## util

PermissioniRationalActivity.java -> *classe fondamentale per l'ActivityRecognitionClient API*

Calories.java -> *classe che contiene un insieme di informazioni sul dispendio calorico per ogni attività*

Constants.java -> *classe che contiene costanti utilizzate per memorizzare informazioni all'interno dell'app*

<br/>

---

### Panoramica generale WayOfLife
link - https://youtu.be/2mAGCUCmokY

### Gestione cronologia allenamenti
link - https://youtu.be/_0rAPkvotFs

### Funzionamento Activity Transition
link - https://youtu.be/i8ofrIUSbCw

### Attivazione GPS - RunningActivity
link - https://youtu.be/w_E9IZPzVuo
