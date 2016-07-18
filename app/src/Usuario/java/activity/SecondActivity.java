package activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matias.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Calendary.DateDialog;
import Calendary.TimeDialog;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by Matias on 24/05/2016.
 */
public class SecondActivity extends ActionBarActivity {
    private EditText txtDate;
    private EditText txtTimeDesde;
    private EditText txtTimeHasta;
    private Button btnSearch;
    private static final String TAG = SecondActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        final String name = user.get("name");
        final String email = user.get("email");


        //Calendario y Hora


        txtDate = (EditText) findViewById(R.id.etFecha);

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");

                }
            }

        });

        txtTimeDesde = (EditText) findViewById(R.id.etHoraDesde);

        txtTimeDesde.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeDialog dialog = new TimeDialog(v);
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    dialog.show(ft1, "TimePicker");
                }
            }

        });

        txtTimeHasta = (EditText) findViewById(R.id.etHoraHasta);

        txtTimeHasta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimeDialog dialog = new TimeDialog(v);
                    FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                    dialog.show(ft1, "TimePicker");
                }
            }

        });
        //End Calendario Y Hora


        //Search button Click Event
        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String date = txtDate.getText().toString().trim();
                String timedsd = txtTimeDesde.getText().toString().trim();
                String timehst = txtTimeHasta.getText().toString().trim();
                if ( !date.isEmpty() && !timedsd.isEmpty() && !timehst.isEmpty() ) {
                    /*Log.d(TAG, "date -" + date +"-"+ "timedsd -" + timedsd + "-" );
                    hideDialog();*/

                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    String today = day + "-" + (month + 1) + "-" + year;

                    SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        Date inTime = sdf.parse(timedsd);
                        Date outTime = sdf.parse(timehst);
                        Date today2 = sdf2.parse(today);
                        Date date2 = sdf2.parse(date);
                        //Log.d(TAG,"today2 : "+ today2 + " date2 : " + date2);
                        if ((inTime.compareTo(outTime) <= 0) && !(date2.before(today2))) {                 // HDesde >= Hhasta y  felegida no esta antes q hoy
                            /*Toast.makeText(getApplicationContext(),
                                    "OK" + " " + date2 + "InTime: " + " "+ inTime + " "+ "outTime: " + " "+ outTime, Toast.LENGTH_LONG)
                                   // "name:" + " " + name + " email: " + " "+ email , Toast.LENGTH_LONG)
                                    .show();*/
                            search(date, timedsd, timehst, email, name);

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Fechas Mal introducidas", Toast.LENGTH_LONG)
                                    .show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    fechasError();
                }
            }

        });



    }

    private void search (final String Fecha_elegida, final String Hdesde, final String Hhasta, final String email, final String name){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Buscando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BUSQUEDA_USUARIO,new  Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Busqueda Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String test = jObj.getString("test");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Viendo si esto anda: "+ test, Toast.LENGTH_LONG)
                                // "name:" + " " + name + " email: " + " "+ email , Toast.LENGTH_LONG)
                                .show();
                    } else {
                        fechasError();
                    }

                        /*
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }*/
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Test Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })  {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("dia", Fecha_elegida);
                params.put("horadesde", Hdesde);
                params.put("horahasta", Hdesde);
                params.put("email", email);
                params.put("name", name);
                return params;

            }

        };
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void fechasError(){
        Toast.makeText(getApplicationContext(),
                "Fechas Mal introducidas", Toast.LENGTH_LONG)
                .show();
    }


}
