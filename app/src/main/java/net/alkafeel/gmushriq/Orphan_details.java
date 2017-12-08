package net.alkafeel.gmushriq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import alghima.io.gmushriq.R;

public class Orphan_details extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    TextView tv_name, tv_birth_date, tv_acadmy_level, tv_school_name, tv_disses, tv_info;
    Typeface new_font;
    ImageView kid_image;
    Toolbar custom_toolbar;
    Button btn_join;

    String domain_name = "http://aljiachi.net/";
    String images_dir = domain_name + "apps/orphans/assets/files/images/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orphan_details);


        Bundle get_data = getIntent().getExtras();


        new get_orphan_details().execute("getKidInfo.php?id=" + get_data.getString("id"));
        kid_image = (ImageView) findViewById(R.id.imageView2);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_birth_date = (TextView) findViewById(R.id.tv_birth_date);
        tv_acadmy_level = (TextView) findViewById(R.id.tv_acadmy_level);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        tv_disses = (TextView) findViewById(R.id.tv_disease);
        tv_info = (TextView) findViewById(R.id._tv_info);

        new_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");

        tv_name.setTypeface(new_font);
        tv_birth_date.setTypeface(new_font);
        tv_acadmy_level.setTypeface(new_font);
        tv_school_name.setTypeface(new_font);
        tv_disses.setTypeface(new_font);
        tv_info.setTypeface(new_font);


        btn_join = (Button) findViewById(R.id.btn_join);
        btn_join.setTypeface(new_font);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MakeCall = new Intent(Intent.ACTION_CALL);

                MakeCall.setData(Uri.parse("tel:07706731370"));
                int hasPermission = ContextCompat.checkSelfPermission(Orphan_details.this,Manifest.permission.CALL_PHONE);
                if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                    startActivity(MakeCall);
                }else {

                    ActivityCompat.requestPermissions(Orphan_details.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }

            }
        });



    }

    public class get_orphan_details extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Utiles utiles = new Utiles();

            String date_send = utiles.GET(params[0]);


            return date_send;


        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject parentJson = new JSONObject(s);

                String name =  parentJson.getString("name");

                tv_name.setText(tv_name.getText().toString() + "\n" + parentJson.getString("name"));
                tv_birth_date.setText(tv_birth_date.getText().toString() + "\n" + parentJson.getString("birth_date"));
                tv_acadmy_level.setText(tv_acadmy_level.getText().toString() + "\n" + parentJson.getString("edc"));
                tv_school_name.setText(tv_school_name.getText().toString() + "\n" + parentJson.getString("school"));
                tv_disses.setText(tv_disses.getText().toString() + "\n" + parentJson.getString("disease"));
                tv_info.setText(tv_info.getText().toString() + "\n" + parentJson.getString("about"));

                Picasso.with(getApplicationContext()).load(images_dir + parentJson.getString("image")).into(kid_image);

                String[] split = name.split(" ");
                btn_join.setText(getString(R.string.sponsor)+ " " + split[0]);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


}
