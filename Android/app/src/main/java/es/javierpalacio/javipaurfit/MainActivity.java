package es.javierpalacio.javipaurfit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private static final String TAG = "javipaurfit";
    private boolean authInProgress = false;
    private static TextView tNombre, tFecha, tPasos;
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;
    private int previousValue;
    private int pasos=0;
    private Button bEnviar;
    private Button bSalir;
    private Button bRefrescar;
    private GoogleApiClient client;
    private String mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        setContentView(R.layout.activity_main);
        tNombre = (TextView) findViewById(R.id.tNombre);
        tNombre.setText("Javi");
        tFecha = (TextView) findViewById(R.id.tFecha);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fechaDia = sdf.format(new Date());
        tFecha.setText(fechaDia);
        tPasos = (TextView) findViewById(R.id.tPasos);
        tPasos.setText(String.valueOf(pasos));
        bEnviar=(Button) findViewById(R.id.bEnviar);
        bRefrescar=(Button) findViewById(R.id.bRefrescar);
        bSalir=(Button) findViewById(R.id.bSalir);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        buildFitnessClient();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        bEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //enviarDatos();
                //Borramos todos los objetos existentes para la fecha del dia del Usuario

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ObjetoRegistroPasos");
                query.whereEqualTo("Usuario", tNombre.getText());
                query.whereEqualTo("Fecha", tFecha.getText());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (list.size() != 0)
                            list.get(0).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getBaseContext(), "Registros Borrados", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), "Error Delete!" + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                   /*
                if (list.size() != 0){
                    ParseObject testObject = new ParseObject("ObjetoRegistroPasos");
                    Toast.makeText(getBaseContext(), "Leido!"+ list.get(0).get("Usuario")+list.get(0).get("Fecha")+list.get(0).get("Pasos"), Toast.LENGTH_LONG).show();
                    }
                    */
                    }

                });


                ParseObject testObject = new ParseObject("ObjetoRegistroPasos");
                testObject.put("Pasos", tPasos.getText());
                testObject.put("Fecha", tFecha.getText());
                testObject.put("Usuario", tNombre.getText());
                testObject.saveInBackground();
                Toast.makeText(getApplicationContext(), "Enviando datos.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        bRefrescar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pasos == 0) pasos = 999;
                tPasos.setText(String.valueOf(pasos));
                Toast.makeText(getApplicationContext(), "Refrescar datos." + tPasos.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        bSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Salir de aplicacion.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }



    private void enviarDatos() {
        //Borramos todos los objetos existentes para la fecha del dia del Usuario

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ObjetoRegistroPasos");
        query.whereEqualTo("Usuario", tNombre.getText());
        query.whereEqualTo("Fecha", tFecha.getText());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list.size() != 0)
                    list.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getBaseContext(), "Registros Borrados", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Error Delete!" + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                   /*
                if (list.size() != 0){
                    ParseObject testObject = new ParseObject("ObjetoRegistroPasos");
                    Toast.makeText(getBaseContext(), "Leido!"+ list.get(0).get("Usuario")+list.get(0).get("Fecha")+list.get(0).get("Pasos"), Toast.LENGTH_LONG).show();
                    }
                    */
            }

        });

        //Insertamos Pasos
        ParseObject testObject = new ParseObject("ObjetoRegistroPasos");
        testObject.put("Pasos","888");
        testObject.put("Fecha", tFecha.getText());
        testObject.put("Usuario", tNombre.getText());
        testObject.saveInBackground();

    }



    private void buildFitnessClient() {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .addScope(Fitness.SCOPE_LOCATION_READ_WRITE)
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.i(TAG, "Conectado!!!");
                                // Now you can make calls to the Fitness APIs.
                                // Put application specific code here.
                                pasos = findFitnessDataSources();
                                tPasos.setText(String.valueOf(pasos));
                                Toast.makeText(getApplicationContext(), "N pasos."+pasos,
                                Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost, red perdida");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost, servicio desconectado");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Conexion fallida. Causa: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                            MainActivity.this, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        Log.i(TAG, "Intentando resolver el fallo de conexion");
                                        authInProgress = true;
                                        result.startResolutionForResult(MainActivity.this,REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG, "Exception mientras arranca la actividad!", e);
                                    }
                                }
                            }
                        }
                )
                .build();
    }



    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // Connect to the Fitness API
        Log.i(TAG, "Connectando...");
        mClient.connect();
        // findFitnessDataSources();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://es.javierpalacio.javipaurfit/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://es.javierpalacio.javipaurfit/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        if (mClient.isConnected()) {

            mClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (!mClient.isConnecting() && !mClient.isConnected()) {
                    mClient.connect();

                }
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }
    private int findFitnessDataSources() {
        // [START find_data_sources]

        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        //.setDataTypes(DataType.AGGREGATE_DISTANCE_DELTA)
                        // Can specify whether data type is raw or derived.
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.i(TAG, "Resultado: " + dataSourcesResult.getStatus().toString());

                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.i(TAG, "Data source found: " + dataSource.toString());
                            Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_CUMULATIVE) && mListener == null) {
                                Log.i(TAG, "Data source for TYPE_STEP_COUNT_CUMULATIVE found!  Registering.");

                                pasos = registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);

                            }
                        }
                    }
                });
        return pasos;
    }
    private int registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        // [START register_data_listener]

        mListener = new OnDataPointListener() {

            @Override
            public void onDataPoint(DataPoint dataPoint) {
                //int pasosTotales=dataPoint.getDataType().getFields().get(0).getClass().getFields();
                Value val=null;
                for (Field field : dataPoint.getDataType().getFields()) {
                    //pasos++;
                    val = dataPoint.getValue(field);
                    Log.i(TAG, "Detected DataPoint field: " + field.getName());
                    Log.i(TAG, "Detected DataPoint value: " + val);
                    //Log.i(TAG, "Difference in steps: " + (val.asInt() - previousValue));
                    //previousValue = val.asInt();

                    pasos=val.asInt();
                    //tPasos.setText(String.valueOf(pasos));
                }

            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(1, TimeUnit.MILLISECONDS)
                        .build(),
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener Registrado!");

                        } else {
                            Log.i(TAG, "Listener no registrado.");
                        }
                    }
                });
        return pasos;
    }
    private void unregisterFitnessDataListener() {
        if (mListener == null) {
            // This code only activates one listener at a time.  If there's no listener, there's
            // nothing to unregister.
            return;
        }

        // [START unregister_data_listener]
        // Waiting isn't actually necessary as the unregister call will complete regardless,
        // even if called from within onStop, but a callback can still be added in order to
        // inspect the results.
        Fitness.SensorsApi.remove(
                mClient,
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "escuchador borrado!");
                        } else {
                            Log.i(TAG, "escuchador no borrado.");
                        }
                    }
                });
        // [END unregister_data_listener]
    }


}


