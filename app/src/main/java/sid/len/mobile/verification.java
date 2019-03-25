package sid.len.mobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class verification extends AppCompatActivity {

    private static final int ProsesDT = 0;
    EditText txttanggalenroll;
    Button cek;
    Connection connect;
    String ip,db,DBUserNameStr,DBPasswordStr,EnrollStr,RegisStr,StatusStr,txtperbandinganregisSTR,email;
    RequestQueue queue;
    ProgressBar ngantosan;
    String SessionName,SessionEmail;
    String getRegis,getApplicant;
    private static TextView txtregis,txtperbandinganregis;
    private static final String TAG = "MyActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        ip = "192.168.88.59";
        db = "LEN_SID_REGISTRASI_ONLINE";
        DBUserNameStr = "sa";
        DBPasswordStr = "Sa1234567890";

        //txtperbandinganregis      = (TextView) findViewById(R.id.RegisNumber);
        txtregis                    = (TextView) findViewById(R.id.RegisNumber);
        TextView txtseafarers       = (TextView) findViewById(R.id.SeafarersCode);
        TextView txtdate            = (TextView) findViewById(R.id.Birthday);
        TextView txtfirstname       = (TextView) findViewById(R.id.FirstName);
        TextView txtmiddlename      = (TextView) findViewById(R.id.MiddleName);
        TextView txtlastname        = (TextView) findViewById(R.id.LastName);
        TextView txtplaceofbirth    = (TextView) findViewById(R.id.PlaceBirth);
        TextView txtnegara          = (TextView) findViewById(R.id.Negara);
        TextView txtjenispermohonan = (TextView) findViewById(R.id.JenisPermohonan);
        TextView txtjeniskelamin    = (TextView) findViewById(R.id.JenisKelamin);
        TextView txtjalan           = (TextView) findViewById(R.id.Jalan);
        TextView kota               = (TextView) findViewById(R.id.NamaKota);
        TextView txtnegara1         = (TextView) findViewById(R.id.Negara1);
        TextView txtkodepos         = (TextView) findViewById(R.id.KodePos);
        TextView txtrumah           = (TextView) findViewById(R.id.TeleponRumah);
        TextView txtkantor          = (TextView) findViewById(R.id.TeleponKantor);
        TextView txthp              = (TextView) findViewById(R.id.HP);
        TextView txtjalansurat      = (TextView) findViewById(R.id.JalanSurat);
        TextView txtkotasurat       = (TextView) findViewById(R.id.NamaKotaSurat);
        TextView txtnegara2         = (TextView) findViewById(R.id.Negara2);
        TextView txtkodepossurat    = (TextView) findViewById(R.id.KodePosSurat);
        TextView txtlokasi          = (TextView) findViewById(R.id.LokasiRekam);

        txttanggalenroll            = (EditText) findViewById(R.id.TglBirthday);
        cek                         = (Button) findViewById(R.id.selectdate);
        Button   submit             = (Button) findViewById(R.id.Submit);
        ngantosan                   = (ProgressBar) findViewById(R.id.proses);

        queue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        SessionName = sharedpreferences.getString("nameKey", null);
        SessionEmail = sharedpreferences.getString("emailKey", null);


        ngantosan.setVisibility(View.GONE);

        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(ProsesDT);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputData();

            }
        });


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            String LabelRegis       = " Registration Number :" ;
            String LabelText        = " Seafarer's Code :" ;
            String LabelBirthday    = " Tanggal Lahir :" ;
            String LabelFirstName   = " Nama Depan :" ;
            String LabelMiddleName  = " Nama Tengah :" ;
            String LabelLastName    = " Nama Belakang :" ;
            String LabelTempatLahir = " Tempat Lahir :" ;
            String LabelNegara      = " Negara :" ;
            String LabelPermohonan  = " Jenis Permohonan :" ;
            String LabelGender      = " Jenis Kelamin :" ;
            String LabelJalan       = " Jalan :" ;
            String LabelKota        = " Kota :" ;
            String LabelNegara1     = " Negara :" ;
            String LabelKodePOS     = " Kode POS :" ;
            String LabelRumah       = " Telepon Rumah :" ;
            String LabelKantor      = " Telepon Kantor :" ;
            String LabelHP          = " HP :" ;
            String LabelJalanSurat  = " Jalan :" ;
            String LabelKotaSurat   = " Kota :" ;
            String LabelNegaraSurat = " Negara :" ;
            String LabelKodePOS1    = " Kode POS :" ;
            String LabelLokasi      = " Lokasi Rekam :" ;


            getRegis                = (String) bd.get("REGISTRATION NUMBER");
            String getSeafarer      = (String) bd.get("SEAFARER CODE");

            String getDOB = (String) bd.get("date");
            SimpleDateFormat umur = new SimpleDateFormat("yyyy-MM-dd");
            Date outDate = null;
            try {
                outDate = umur.parse(getDOB);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String getFirstName     = (String) bd.get("FIRST NAME");
            String getMiddleName    = (String) bd.get("MIDDLE NAME");
            String getLastName      = (String) bd.get("LAST NAME");
            getApplicant            = (String) bd.get("APPLICANT NAME");
            String getTempatLahir   = (String) bd.get("PLACE OF BIRTH");
            String getNegara        = (String) bd.get("NATIONALITY");
            String getPermohonan    = (String) bd.get("REASON FOR ISSUANCE");
            String getGender        = (String) bd.get("GENDER ID");
            String getJalan         = (String) bd.get("P STREET");
            String getKota          = (String) bd.get("P CITY");
            String getNegara1       = (String) bd.get("P COUNTRY");
            String getKodePOS       = (String) bd.get("P POSTAL CODE");
            String getRumah         = (String) bd.get("HOME PHONE");
            String getHP            = (String) bd.get("CELL PHONE");
            String getKantor        = (String) bd.get("WORK PHONE");
            String getJalanSurat    = (String) bd.get("M STREET");
            String getKotaSurat     = (String) bd.get("M CITY");
            String getNegara2       = (String) bd.get("M COUNTRY");
            String getKodePOS1      = (String) bd.get("M POSTAL CODE");
            String getLokasi        = (String) bd.get("ENROLLMENT LOCATION");


            txtregis.setText(LabelRegis + getRegis);
            //txtperbandinganregis.setText(getRegis);
            txtseafarers.setText(LabelText + getSeafarer);
            txtdate.setText(LabelBirthday + outDate);
            txtfirstname.setText(LabelFirstName + getFirstName);

            txtmiddlename.setText(LabelMiddleName + getMiddleName);
            txtlastname.setText(LabelLastName + getLastName);
            txtplaceofbirth.setText(LabelTempatLahir + getTempatLahir);
            txtnegara.setText(LabelNegara + getNegara);
            txtjenispermohonan.setText(LabelPermohonan + getPermohonan);
            txtjeniskelamin.setText(LabelGender + getGender);
            txtjalan.setText(LabelJalan + getJalan);
            kota.setText(LabelKota + getKota);
            txtnegara1.setText(LabelNegara1 + getNegara1);
            txtkodepos.setText(LabelKodePOS + getKodePOS);
            txtrumah.setText(LabelRumah + getRumah);
            txtkantor.setText(LabelKantor + getKantor);
            txthp.setText(LabelHP + getHP);
            txtjalansurat.setText(LabelJalanSurat + getJalanSurat);
            txtkotasurat.setText(LabelKotaSurat + getKotaSurat);
            txtnegara2.setText(LabelNegaraSurat + getNegara2);
            txtkodepossurat.setText(LabelKodePOS1 + getKodePOS1);
            txtlokasi.setText(LabelLokasi + getLokasi);

        }
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
            new AlertDialog.Builder(verification.this)
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
            Intent intent = new Intent(verification.this, LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id)  {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case ProsesDT:

                return new DatePickerDialog(verification.this, date_listener, year,
                        month, day);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date1 = String.valueOf(year) + "-" + String.valueOf(month+1)
                    + "-" + String.valueOf(day);
            txttanggalenroll.setText(date1);
        }
    };

    public void InputData() {
        txtperbandinganregisSTR  =getRegis;
        email  = getApplicant;
        RegisStr=txtregis.getText().toString();
        EnrollStr=txttanggalenroll.getText().toString();


        DaftarEnroll daftar_enroll = new DaftarEnroll();
        daftar_enroll.execute(EnrollStr,RegisStr);
    }

    public class  DaftarEnroll extends AsyncTask<String,String,String> {

        String ConnectionResult = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            ngantosan.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result)
        {
            ngantosan.setVisibility(View.GONE);
            Toast.makeText(verification.this, result, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(verification.this , "Submission Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String Username = SessionEmail;

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            String Enrolfix = EnrollStr;
            String Status = "ENROLLED";
            String RegisBaru =  RegisStr;
            String PerbandinganRegisBaru =  txtperbandinganregisSTR;
            String Patokan = "REGISTERED";

            try {
                ConnectionStr conStr = new ConnectionStr();
                connect = conStr.connectionclasss(DBUserNameStr, DBPasswordStr, db, ip);
                if (connect == null) {
                    ConnectionResult = "Check Your Internet Access!";
                }
                else
                {

                    //String query = " UPDATE dbo.DEMOGRAPHICS SET LAST_UPDATED_USERNAME = '" + Username  + "',ENROLLMENT_SCHEDULE = '" + Enrolfix + "',STATUS_ID = '" + Status  + "',LAST_UPDATED ='" + formattedDate  + "' where Registration_Number =  IsNumeric('" + RegisBaru  + "')   ";
                    String query = "UPDATE dbo.DEMOGRAPHICS SET LAST_UPDATED_USERNAME= '"+ Username +"', ENROLLMENT_SCHEDULE = '"+ Enrolfix +"', STATUS_ID ='"+ Status +"' , LAST_UPDATED ='"+ formattedDate +"' where REGISTRATION_NUMBER =  '" + PerbandinganRegisBaru  + "' AND STATUS_ID = '"+ Patokan + "'";
                    PreparedStatement preparedStatement = connect.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    //Statement stmt = connect.createStatement();
                    //stmt.executeUpdate(query);
/**                 Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "firmanbrilian@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "pemberitahuan");
                    email.putExtra(Intent.EXTRA_TEXT, "percobaan");

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose Email client :"));
 **/
                    ConnectionResult = "Submission Sucesfully";
                    NoticationEmail();

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                ConnectionResult = ex.getMessage();
            }
            return ConnectionResult;
        }

    }

    public void NoticationEmail() {

        final String username = "firmanbrilian@gmail.com";
        final String password = "sainsdanteknologi09";
        final String Notification = email;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("firmanbrilian@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Notification));
            message.setSubject("Announcement");
            message.setText("Dear Mr/Mrs,"
                    + "\n\n Please welcome our office Next Week,");
            Transport.send(message);

            System.out.println("Done");
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        Intent pindah = new Intent(verification.this, AdminActivity.class);
        startActivity(pindah);

    }

}
