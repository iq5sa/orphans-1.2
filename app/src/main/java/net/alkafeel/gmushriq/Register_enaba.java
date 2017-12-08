package net.alkafeel.gmushriq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import alghima.io.gmushriq.R;

public class Register_enaba extends AppCompatActivity {

    Button btn_enaba_signin;
    Typeface new_font;
    EditText email_EditText,password_EditText;
    Context context;
    ProgressDialog NewsPD;

    String domain_name = "http://aljiachi.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_enaba);
        new_font = Typeface.createFromAsset(getAssets(),"fonts/font.ttf");

        context = getApplicationContext();

        btn_enaba_signin = (Button)findViewById(R.id.btn_enaba_signin);
        btn_enaba_signin.setTypeface(new_font);


        email_EditText = (EditText)findViewById(R.id.email_editText);
        password_EditText = (EditText)findViewById(R.id.password_editText);


        btn_enaba_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_EditText.getText().toString().equals("") || password_EditText.getText().toString().equals("")){

                    Toast.makeText(Register_enaba.this, getString(R.string.Please_fill_in_the_fields), Toast.LENGTH_SHORT).show();
                }else {


                    NewsPD = new ProgressDialog(Register_enaba.this);
                    NewsPD.setMessage("جاري التحميل  ...");
                    NewsPD.show();
                    new Signin().execute(email_EditText.getText().toString(),password_EditText.getText().toString());
                }
            }
        });





        


    }

    public class Signin extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {


            Utiles utiles = new Utiles();

            String Data = utiles.POST("doLogin.php","email="+params[0]+"&logpass="+params[1]);


            return Data;
        }

        @Override
        protected void onPostExecute(String s) {

            NewsPD.dismiss();
            JSONObject json = null;
            try {
                json = new JSONObject(s);
                String status = json.getString("status");


                if(status.equals("logged")){
                    String uid = json.getString("id");
                    String name = json.getString("name");

                    SharedPreferences sharedpref = getSharedPreferences("sponsor_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedpref.edit();

                    edit.putString("uid",uid);
                    edit.putString("name",name);
                    edit.apply();


                    Intent intent = new Intent(Register_enaba.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), getString(R.string.has_been_logged), Toast.LENGTH_SHORT).show();


                }else {


                    Toast.makeText(getApplicationContext(), getString(R.string.problem_happened), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
