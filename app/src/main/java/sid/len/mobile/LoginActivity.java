package sid.len.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText ET_Username,ET_Password;
    ProgressBar progressBar;
    TextView link_signup;
    Connection connect;
    String DBUserNameStr,DBPasswordStr,db,ip,UserNameStr,PasswordStr;


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Applicant = "applicantKey";



    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        ET_Username = (EditText) findViewById(R.id.input_email);
        ET_Password = (EditText) findViewById(R.id.input_password);
        progressBar = (ProgressBar) findViewById(R.id.determinateBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ip = "192.168.88.59";
        db = "LEN_SID_REGISTRASI_ONLINE";
        DBUserNameStr = "sa";
        DBPasswordStr = "Sa1234567890";

        progressBar.setVisibility(View.GONE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserNameStr=ET_Username.getText().toString();
                PasswordStr=ET_Password.getText().toString();

                checklogin check_Login = new checklogin();
                check_Login.execute(UserNameStr,PasswordStr);
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daftar = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(daftar);
            }
        });
    }


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
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(LoginActivity.this , "Login Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String usernam = UserNameStr;
            String passwordd =PasswordStr;
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                ConnectionResult = "Please enter Username and Password";
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
                        String query = "select * from dbo.[ACCOUNTS] where USERNAME= '" + usernam.toString() + "' and PASSWORD = '"+ passwordd.toString() +"'  ";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            String Nama     = rs.getString("NAME");
                            String Mailist  = rs.getString("EMAIL_ADDRESS");
                            int ROLE        = rs.getInt("ROLE_ID");

                            if (ROLE == 2) {

                                Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Please  Download SID User Application", Toast.LENGTH_LONG).show();
                                        }
                                    });
                            }


                            else if (ROLE == 1) {
                                ConnectionResult = "Login successful";
                                isSuccess=true;

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(Name, Nama);
                                editor.putString(Email, Mailist);
                                editor.putString(Applicant, Mailist);
                                editor.commit();

                                Intent user = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(user);
                            }
                            return ConnectionResult;
                        }
                        else
                        {
                            ConnectionResult = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    ConnectionResult = ex.getMessage();
                }
            }
            return ConnectionResult;
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();


    }


}
