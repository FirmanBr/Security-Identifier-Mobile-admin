package sid.len.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String TAG = AdminActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://10.0.2.2/sid/services/android/Demographics.php";
    String Tes ;
    Integer s ;


    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);


        new GetContacts().execute();

        registerForContextMenu(lv);

/*     bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_about :
                        new AlertDialog.Builder(AdminActivity.this)
                                .setTitle("ABOUT SID MOBILE")
                                .setMessage("SID Mobile is application for surface registration.")
                                .show();
                        break;
                    case R.id.action_logout :
                        Toast toast = Toast.makeText(getApplicationContext(), "Successfull Logout", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.about){
            new AlertDialog.Builder(AdminActivity.this)
                    .setTitle("ABOUT SID MOBILE")
                    .setMessage("SID Mobile is application for surface registration.")
                    .show();

        }
        else if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Toast toast = Toast.makeText(getApplicationContext(), "Successfull Logout", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            UserActivityHttpHandler sh = new UserActivityHttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            String SessionName,SessionEmail,SessionApplicant ;
            SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            SessionName = sharedpreferences.getString("nameKey", null);
            SessionEmail = sharedpreferences.getString("emailKey", null);
            SessionApplicant = sharedpreferences.getString("applicantKey", null);



            //Toast.makeText(getApplicationContext(), SessionName, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), SessionEmail, Toast.LENGTH_SHORT).show();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("Data");

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String Status = c.getString("STATUS ID");

                        if (Status.equals("REGISTERED")) {

                            String Regis = c.getString("REGISTRATION NUMBER");
                            String Seafarer = c.getString("SEAFARER CODE");

                            JSONObject Birthday = c.getJSONObject("DOB");
                            String date = Birthday.getString("date");

                            String FirstName = c.getString("FIRST NAME");
                            String MiddleName = c.getString("MIDDLE NAME");
                            String LastName = c.getString("LAST NAME");
                            String Applicant = c.getString("APPLICANT NAME");
                            String PlaceBirth = c.getString("PLACE OF BIRTH");
                            String Negara = c.getString("NATIONALITY");
                            String Reason = c.getString("REASON FOR ISSUANCE");
                            String JK = c.getString("GENDER ID");
                            String Jalan = c.getString("P STREET");
                            String Kota = c.getString("P CITY");
                            String Negara1 = c.getString("P COUNTRY");
                            String KodePos = c.getString("P POSTAL CODE");
                            String rumah = c.getString("HOME PHONE");
                            String hp = c.getString("CELL PHONE");
                            String kantor = c.getString("WORK PHONE");
                            String JalanSurat = c.getString("M STREET");
                            String KotaSurat = c.getString("M CITY");
                            String Negara1Surat = c.getString("M COUNTRY");
                            String KodePosSurat = c.getString("M POSTAL CODE");
                            String Enrollment = c.getString("ENROLLMENT LOCATION");


                            String HasilNegara = null;
                            if (Negara.equals("1")) {
                                HasilNegara = "Indonesia";
                            }

                            String HasilReason = null;
                            if (Reason.equals("1")) {
                                HasilReason = "Pembuatan Baru";
                            } else if (Reason.equals("2")) {
                                HasilReason = "Perpanjangan";
                            } else if (Reason.equals("3")) {
                                HasilReason = "Penggantian";
                            }

                            String HasilJk = null;
                            if (JK.equals("1")) {
                                HasilJk = "PRIA";
                            } else if (Reason.equals("2")) {
                                HasilJk = "WANITA";
                            } else if (Reason.equals("3")) {
                                HasilJk = "X";
                            }

                            String HasilNegara1 = null;
                            if (Negara1.equals("1")) {
                                HasilNegara1 = "Indonesia";
                            }

                            String HasilNegara1Surat = null;
                            if (Negara1Surat.equals("1")) {
                                HasilNegara1Surat = "Indonesia";
                            }

                            String HasilEnrollment = null;
                            if (Enrollment.equals("1")) {
                                HasilEnrollment = "Jakarta";
                            } else if (Enrollment.equals("2")) {
                                HasilEnrollment = "Tanjung Benoa";
                            }


                            HashMap<String, String> contact = new HashMap<>();

                            contact.put("REGISTRATION NUMBER", Regis);
                            contact.put("SEAFARER CODE", Seafarer);
                            contact.put("date", date);
                            contact.put("FIRST NAME", FirstName);
                            contact.put("MIDDLE NAME", MiddleName);
                            contact.put("LAST NAME", LastName);
                            contact.put("APPLICANT NAME", Applicant);
                            contact.put("PLACE OF BIRTH", PlaceBirth);
                            contact.put("NATIONALITY", HasilNegara);
                            contact.put("REASON FOR ISSUANCE", HasilReason);
                            contact.put("GENDER ID", HasilJk);
                            contact.put("P STREET", Jalan);
                            contact.put("P CITY", Kota);
                            contact.put("P COUNTRY", HasilNegara1);
                            contact.put("P POSTAL CODE", KodePos);
                            contact.put("HOME PHONE", rumah);
                            contact.put("CELL PHONE", hp);
                            contact.put("WORK PHONE", kantor);
                            contact.put("M STREET", JalanSurat);
                            contact.put("M CITY", KotaSurat);
                            contact.put("M COUNTRY", HasilNegara1Surat);
                            contact.put("M POSTAL CODE", KodePosSurat);
                            contact.put("ENROLLMENT LOCATION", HasilEnrollment);
                            contact.put("STATUS ID", Status);


                            contactList.add(contact);

                        }


                        }
                  //  }

                }
                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }

            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    AdminActivity.this, contactList,
                    R.layout.list_item, new String[]{"SEAFARER CODE","REASON FOR ISSUANCE","STATUS ID","ENROLLMENT LOCATION","ENROLLMENT SCHEDULE"},
                    new int[]{R.id.Seafearer, R.id.Reason,R.id.Status,R.id.Enrollment,R.id.Schedule});


            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent verif = new Intent(AdminActivity.this, verification.class);
                    HashMap<String, String> lesson = contactList.get(i);
                    verif.putExtra("REGISTRATION NUMBER", lesson.get("REGISTRATION NUMBER"));
                    verif.putExtra("SEAFARER CODE", lesson.get("SEAFARER CODE"));
                    verif.putExtra("date", lesson.get("date"));
                    verif.putExtra("FIRST NAME", lesson.get("FIRST NAME"));
                    verif.putExtra("MIDDLE NAME", lesson.get("MIDDLE NAME"));
                    verif.putExtra("LAST NAME", lesson.get("LAST NAME"));
                    verif.putExtra("APPLICANT NAME", lesson.get("APPLICANT NAME"));
                    verif.putExtra("PLACE OF BIRTH", lesson.get("PLACE OF BIRTH"));
                    verif.putExtra("NATIONALITY", lesson.get("NATIONALITY"));
                    verif.putExtra("REASON FOR ISSUANCE", lesson.get("REASON FOR ISSUANCE"));
                    verif.putExtra("GENDER ID", lesson.get("GENDER ID"));
                    verif.putExtra("P STREET", lesson.get("P STREET"));
                    verif.putExtra("P CITY", lesson.get("P CITY"));
                    verif.putExtra("P COUNTRY", lesson.get("P COUNTRY"));
                    verif.putExtra("P POSTAL CODE", lesson.get("P POSTAL CODE"));
                    verif.putExtra("HOME PHONE", lesson.get("HOME PHONE"));
                    verif.putExtra("CELL PHONE", lesson.get("CELL PHONE"));
                    verif.putExtra("WORK PHONE", lesson.get("WORK PHONE"));
                    verif.putExtra("M STREET", lesson.get("M STREET"));
                    verif.putExtra("M CITY", lesson.get("M CITY"));
                    verif.putExtra("M COUNTRY", lesson.get("M COUNTRY"));
                    verif.putExtra("M POSTAL CODE", lesson.get("M POSTAL CODE"));
                    verif.putExtra("ENROLLMENT LOCATION", lesson.get("ENROLLMENT LOCATION"));
                    verif.putExtra("STATUS ID", lesson.get("STATUS ID"));



                    startActivity(verif);
                }
            });
//            registerForContextMenu(lv);

        }

    }

    /**    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Select The Action");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item ){
        if(item.getItemId()==R.id.Verification){
            Intent verif = new Intent(AdminActivity.this, verification.class);
            //String s = contact.get("SEAFARER CODE").toString();
            startActivity(verif);
           // Toast.makeText(getApplicationContext(), "k", Toast.LENGTH_LONG).show();

        }else{
            return false;
        }
        return true;
    }
**/


}

