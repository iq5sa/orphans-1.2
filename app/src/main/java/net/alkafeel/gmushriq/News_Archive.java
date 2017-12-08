package net.alkafeel.gmushriq;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import alghima.io.gmushriq.R;

public class News_Archive extends AppCompatActivity {



    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news__archive);


        layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View news_view = layoutInflater.inflate(R.layout.news_row,null);





    }
}
