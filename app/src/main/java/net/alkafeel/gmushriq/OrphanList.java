package net.alkafeel.gmushriq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import alghima.io.gmushriq.R;

public class OrphanList extends AppCompatActivity {

    LayoutInflater inflater;
    TextView title_new;
    ImageView img_new;
    LinearLayout orphan_list;
    Button btn_signout;

    String domain_name = "http://aljiachi.net/";
    String images_dir = domain_name + "apps/orphans/assets/files/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orphan_list);

        btn_signout = (Button)findViewById(R.id.btn_signout);

        orphan_list = (LinearLayout)findViewById(R.id.orphan_list);

        final SharedPreferences sharedPref = getSharedPreferences("sponsor_data", Context.MODE_PRIVATE);

        Bundle data_rec = getIntent().getExtras();

        String uid = data_rec.getString("uid");


       inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new GetOrphansList().execute(uid);

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sharedPref.edit();

                edit.putString("uid","");
                edit.putString("name","");
                edit.apply();

                Toast.makeText(OrphanList.this, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrphanList.this,HomeActivity.class);
                startActivity(intent);


            }
        });

    }

    public class GetOrphansList extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Utiles utiles = new Utiles();

            String data = utiles.GET("orphansList.php?id="+params[0]);



            return data;


        }



        @Override
        protected void onPostExecute(String s) {



            try {
                JSONObject json = new JSONObject(s);
                JSONArray jsonArray = json.getJSONArray("results");

                for (int i = 0;i < jsonArray.length();i++){
                    JSONArray foo = jsonArray.getJSONArray(i);
                    final String name = foo.getString(1);
                    final String image = foo.getString(2);
                    final String birth_date = foo.getString(3);

                    View view = inflater.inflate(R.layout.news_row,null);
                    title_new = (TextView)view.findViewById(R.id.title_new);
                    img_new  = (ImageView)view.findViewById(R.id.img_new);
                    //box_article = (LinearLayout)view.findViewById(box_article);

                    Picasso.with(getApplicationContext()).load(images_dir + image).resize(110,120).into(img_new);

                    title_new.setText(name + "\n" + birth_date);
                    orphan_list.addView(view);
                    //title_new.setTypeface(new_font);


                    /*
                    box_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent move = new Intent(HomeActivity.this,Internal_news.class);

                            Bundle data_send = new Bundle();
                            data_send.putString("text",text);
                            data_send.putString("title",title);
                            data_send.putString("image",image);

                            move.putExtras(data_send);

                            startActivity(move);

                        }
                    });

                    */
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
