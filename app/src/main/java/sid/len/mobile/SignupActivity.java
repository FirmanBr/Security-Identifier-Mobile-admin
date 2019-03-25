package sid.len.mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.sql.Connection;
import java.sql.Statement;


public class SignupActivity extends AppCompatActivity {

    Button daftar;
    TextView masuk;
    EditText Name,Email,password,confirmpassword;
    ProgressBar progressBar;
    Connection connect;
    String DBUserNameStr,DBPasswordStr,db,ip,UserNameStr,UserEmailStr,PasswordStr,ConfirmPasswordStr;
    String TAG = SignupActivity.class.getSimpleName();
    RequestQueue queue;
    String SITE_KEY = "6LcNb34UAAAAADphtUZkRhgsQVU61D1_Jb4_x6In";
    String SECRET_KEY = "6LcNb34UAAAAAHeCHO_b5lwT0fbjsmZmpd0_EhNH";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = (EditText) findViewById(R.id.input_name);
        Email = (EditText) findViewById(R.id.input_email1);
        password = (EditText) findViewById(R.id.input_password1);
        confirmpassword = (EditText) findViewById(R.id.input_reEnterPassword);
        daftar = (Button) findViewById(R.id.daftar);
        masuk = (TextView) findViewById(R.id.link_login);
        progressBar = (ProgressBar) findViewById(R.id.Progress);

        ip = "192.168.88.59";
        db = "LEN_SID_REGISTRASI_ONLINE";
        DBUserNameStr = "sa";
        DBPasswordStr = "Sa1234567890";


        progressBar.setVisibility(View.GONE);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserNameStr=Name.getText().toString();
                UserEmailStr=Email.getText().toString();
                PasswordStr=password.getText().toString();
                ConfirmPasswordStr=confirmpassword.getText().toString();

                checklogin check_Login = new checklogin();
                check_Login.execute(UserNameStr,UserEmailStr,PasswordStr,ConfirmPasswordStr);
            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daftar = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(daftar);
            }
        });


    }

/*    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.Capcay1:
                if (checked)
                {
                    SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                            .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                @Override
                                public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                    if (!response.getTokenResult().isEmpty()) {
                                        handleSiteVerify(response.getTokenResult());
                                    }
                                }
                            })
                            .addOnFailureListener(this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof ApiException) {
                                        ApiException apiException = (ApiException) e;
                                        Log.d(TAG, "Error message: " +
                                                CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                                    } else {
                                        Log.d(TAG, "Unknown type of error: " + e.getMessage());
                                    }
                                }
                            });
                }
                else
                    break;
        }
    }

    protected  void handleSiteVerify(final String responseToken){
        String url = "https://www.google.com/recaptcha/api/siteverify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getBoolean("success")),Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getString("error-codes")),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "JSON exception: " + ex.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error message: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
*/


    public class checklogin extends AsyncTask<String,String,String>
    {
        String ConnectionResult = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SignupActivity.this, result, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(SignupActivity.this , "Sign UP Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String usernam = UserNameStr;
            String mail = UserEmailStr;
            String passwordd =PasswordStr;
            String confirmpasswordd =ConfirmPasswordStr;
            String role ="1";


            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                ConnectionResult = "Please enter Username and Password";
            else if (!passwordd.equals(confirmpasswordd) )
                ConnectionResult = "Password not Match";
            else
            {
                try
                {
                    ConnectionStr conStr=new ConnectionStr();

                    connect =conStr.connectionclasss(DBUserNameStr, DBPasswordStr, db, ip);        // Connect to database
                    if (connect == null)
                    {
                        ConnectionResult = "Check Your Internet Access!";
                    }
                    else
                    {
                        // Change below query according to your own database.
                        String query = "INSERT INTO dbo.ACCOUNTS(USERNAME, NAME, PASSWORD, EMAIL_ADDRESS, ROLE_ID) VALUES ('" + mail.toString()  + "', '" + usernam.toString()  + "',cast ('" + passwordd.toString()  + "'AS Varbinary (MAX)),'" + mail.toString()  + "','" + role + "') " ;
                        Statement stmt = connect.createStatement();
                        stmt.executeUpdate(query);
                        ConnectionResult = "SignUp successful";
                        Intent pindah = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(pindah);


/*                        if(rs.next())
                        {
                            ConnectionResult = "SignUp successful";
                            isSuccess=true;
                            Intent pindah = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(pindah);
                        }
                        else
                        {
                            ConnectionResult = "Invalid Credentials!";
                            isSuccess = false;
                        }
*/                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    ConnectionResult = ex.getMessage();
                }
            }
            return ConnectionResult;
        }
    }


}
