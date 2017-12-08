package net.alkafeel.gmushriq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alghima.io.gmushriq.R;

public class HomeActivity extends AppCompatActivity {

    Toolbar custom_toolbar;
    TextView see_you_orphans_txt,signin_txt,orphan_name,txt_nosponsor,title_new,news_section;
    RelativeLayout top_app;
    HorizontalScrollView orphans_view;
    ScrollView news_view;
    LayoutInflater layoutInflater;
    ImageView orphan_img,img_new;
    LinearLayout parent_orphan,parent_news,box_article;
    LayoutInflater inflater;
    Typeface new_font;
    Button btn_signout;

    Context context;
    String domain_name = "http://aljiachi.net/";
    String images_dir = domain_name + "apps/orphans/assets/files/images/";
    String token = FirebaseInstanceId.getInstance().getToken();

    ProgressDialog NewsPD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        context = getApplicationContext();
        if (getIntent().getExtras() != null) {
            Bundle notification_data = getIntent().getExtras();

            String IdND = notification_data.getString("id");

            Intent intent = new Intent(HomeActivity.this,Internal_news.class);
            Bundle bdatan = new Bundle();
            bdatan.putString("id",IdND);
            intent.putExtras(bdatan);

            startActivity(intent);

        }



        if(!isNetworkAvailable()){
            finish();
            Toast.makeText(this,"تحقق من الإتصال بالإنترنت" , Toast.LENGTH_SHORT).show();
        }else {

            final SharedPreferences sharedPref = getSharedPreferences("sponsor_data", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPref.edit();

            edit.putString("token",token);
            edit.apply();

            if (!sharedPref.getString("token", "").equals("")) {
                new SaveToken().execute("add_android_token.php");
            }



            NewsPD = new ProgressDialog(HomeActivity.this);
            NewsPD.setMessage("جاري التحميل  ...");
            NewsPD.show();

            btn_signout = (Button)findViewById(R.id.btn_signout);
            if (!sharedPref.getString("name", "").equals("")) {
               btn_signout.setVisibility(View.VISIBLE);
            }
            btn_signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor edit = sharedPref.edit();

                    edit.putString("uid","");
                    edit.putString("name","");
                    edit.apply();

                    Toast.makeText(HomeActivity.this, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    startActivity(intent);


                }
            });

            see_you_orphans_txt = (TextView) findViewById(R.id.see_ur_orphans);
            txt_nosponsor = (TextView) findViewById(R.id.orphan_nosponsor);
            signin_txt = (TextView) findViewById(R.id.signin);
            news_section = (TextView) findViewById(R.id.news_section);
            //orphans_view = (HorizontalScrollView) findViewById(R.id.orphan_scroll);
            parent_orphan = (LinearLayout) findViewById(R.id.parent_orphan);
            parent_news = (LinearLayout) findViewById(R.id.parent_news);


            get_orphans get_orphans = new get_orphans();
            get_orphans.execute();

            get_home_news get_news = new get_home_news();
            get_news.execute();
            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (!sharedPref.getString("name", "").equals("")) {
                see_you_orphans_txt.setText(getString(R.string.Hello) + " , " + sharedPref.getString("name", ""));
                signin_txt.setText(getString(R.string.see_your_orphans));

            }


            //Move To News Archive By Click this Button

            news_section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent news_archive= new Intent(HomeActivity.this,News_Archive.class);
                    startActivity(news_archive);
                }
            });
            top_app = (RelativeLayout) findViewById(R.id.top_app);

            top_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sharedPref.getString("name", "").equals("")) {
                        Intent register_enaba = new Intent(HomeActivity.this, Register_enaba.class);
                        startActivity(register_enaba);

                    } else {
                        Intent register_enaba = new Intent(HomeActivity.this, OrphanList.class);
                        Bundle data_send = new Bundle();
                        data_send.putString("uid", sharedPref.getString("uid", ""));
                        register_enaba.putExtras(data_send);
                        startActivity(register_enaba);

                    }


                }
            });

            new_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
            // Typeface jflat_reg = Typeface.createFromAsset(getAssets(),"fonts/JFLatReg.otf");
            see_you_orphans_txt.setTypeface(new_font);
            signin_txt.setTypeface(new_font);
            txt_nosponsor.setTypeface(new_font);
            news_section.setTypeface(new_font);


        }
        //see_you_orphans_txt.setTypeface(dorid_bold);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class get_orphans extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Utiles utiles = new Utiles();

            String data = utiles.GET("home.php");

            publishProgress(data);


            return data;


        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(String s) {

            NewsPD.dismiss();

            try {
                JSONObject json = new JSONObject(s);
                JSONArray jsonArray = json.getJSONArray("nosponsers");

                for (int i = 0;i < jsonArray.length();i++){
                     JSONArray foo = jsonArray.getJSONArray(i);
                     final String id = foo.getString(0);
                     final String name = foo.getString(1);
                     String image = foo.getString(2);

                    View view = inflater.inflate(R.layout.orphan_row,null);
                    orphan_name = (TextView)view.findViewById(R.id.orphans_name);
                    orphan_img  = (ImageView)view.findViewById(R.id.orphans_image);
                    Picasso.with(getApplicationContext()).load(images_dir + image).resize(150,150).into(orphan_img);

                    orphan_name.setText(name);
                    parent_orphan.addView(view);

                    orphan_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent move = new Intent(HomeActivity.this,Orphan_details.class);
                            Bundle data_send = new Bundle();
                            data_send.putString("id",id);
                            move.putExtras(data_send);
                            startActivity(move);
                        }
                    });
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class get_home_news extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Utiles utiles = new Utiles();

            String data = utiles.GET("home.php");

            publishProgress(data);


            return data;


        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(String s) {



            try {
                JSONObject json = new JSONObject(s);
                JSONArray jsonArray = json.getJSONArray("news");

                for (int i = 0;i < jsonArray.length();i++){
                    JSONArray foo = jsonArray.getJSONArray(i);
                    final String id = foo.getString(0);
                    final String title = foo.getString(1);
                    final String text = foo.getString(2);
                    final String image = foo.getString(3);

                    View view = inflater.inflate(R.layout.news_row,null);
                    title_new = (TextView)view.findViewById(R.id.title_new);
                    img_new  = (ImageView)view.findViewById(R.id.img_new);
                    box_article = (LinearLayout)view.findViewById(R.id.box_article);

                    Picasso.with(getApplicationContext()).load(images_dir + image).resize(110,130).into(img_new);

                    title_new.setText(title);
                    parent_news.addView(view);
                    title_new.setTypeface(new_font);


                    box_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent move = new Intent(HomeActivity.this,Internal_news.class);

                            Bundle data_send = new Bundle();
                            data_send.putString("id",id);

                            move.putExtras(data_send);

                            startActivity(move);

                        }
                    });
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class SaveToken extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {


            Utiles utiles = new Utiles();

            String TokenValue = utiles.GET(params[0]+"?token=" + token);
            return TokenValue;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }



}
