package net.alkafeel.gmushriq;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String refreshedToken;
    SharedPreferences prefs;

    @Override
    public void onTokenRefresh() {

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("registed", "Refreshed token: " + refreshedToken);

        sendToken();
    }

    public void sendToken(){

        // https://alkafeel.net/withYou/api/register_token.php?ios=%@&uid=%@

        if(prefs.getString("appID", "").equals("")) {

            UpdateToken updater = new UpdateToken();
            updater.execute();

        }

    }

    class UpdateToken extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... params) {

            Utiles utiles = new Utiles();

            String AppKey = utiles.GET(params[0]);

            return AppKey;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result.replaceAll(" ", "").equals("done")) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("appID", refreshedToken);
                edit.apply();
            }


        }

    }
}
