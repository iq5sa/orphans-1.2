package net.alkafeel.gmushriq;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import alghima.io.gmushriq.R;

public class Internal_news extends AppCompatActivity {

    String domain_name = "http://aljiachi.net/";
    String images_dir = domain_name + "apps/orphans/assets/files/images/";

    ImageView full_img;
    TextView full_title, full_text;
    Typeface new_font;
    ProgressDialog NewsPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internal_news);





        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        full_title = (TextView)findViewById(R.id.tv_full_title) ;
        full_text = (TextView)findViewById(R.id.tv_full_text) ;

        full_img = (ImageView)findViewById(R.id.full_img);

        new_font = Typeface.createFromAsset(getAssets(),"fonts/font.ttf");

        Bundle get_date_intent = getIntent().getExtras();

        if(!isNetworkAvailable()){
            finish();
            Toast.makeText(this,"تحقق من الإتصال بالإنترنت" , Toast.LENGTH_SHORT).show();
        }else {


            NewsPD = new ProgressDialog(Internal_news.this);
            NewsPD.setMessage("جاري التحميل  ...");
            NewsPD.show();
            new getNewsDetails().execute(get_date_intent.getString("id"));

        }
        full_title.setTypeface(new_font);

        full_text.setTypeface(new_font);





    }

    public class getNewsDetails extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {


            Utiles utiles = new Utiles();

            String con = utiles.GET("getNewsDetails.php?id="+params[0]);

            return con;

        }

        @Override
        protected void onPostExecute(String s) {

            NewsPD.dismiss();

            try {
                JSONObject parentJson = new JSONObject(s);
            
                String title = parentJson.getString("title");
                String text = parentJson.getString("text");
                String image = parentJson.getString("image");

                full_title.setText(title);
                full_text.setText(text);
                Picasso.with(getApplicationContext()).load(images_dir + image).into(full_img);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
