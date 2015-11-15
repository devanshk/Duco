package carnegieart.supplyandcode.duco;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;

import carnegieart.supplyandcode.duco.Helpers.Art;
import carnegieart.supplyandcode.duco.Helpers.MultiThread;
import carnegieart.supplyandcode.duco.Helpers.Spanner;
import me.fichardu.circleprogress.CircleProgress;

public class LoadActivity extends AppCompatActivity {
    static LoadActivity instance;
    Spanner spanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        getSupportActionBar().hide();
        instance = this;

        spanner = (Spanner)findViewById(R.id.cp_progress);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Avenir-Medium.ttf");
        TextView myTextView = (TextView)findViewById(R.id.loading_data);
        myTextView.setTypeface(myTypeface);

        new MultiThread(new Runnable() {
            @Override
            public void run() {
                loadData();
                //Art.generateArt();
                LoadActivity.doneLoading();
            }
        }).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("filterData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void doneLoading(){
        Intent i = new Intent(instance, MainActivity.class);
        instance.startActivity(i);
    }

    void loadData() {
        try {
            JSONArray data = new JSONArray(loadJSONFromAsset());
            //JSONArray data = obj.getJSONArray("things");

            for (int i = 0; i < data.length(); i++) {
                JSONObject p = data.getJSONObject(i);
                Art a;
                String iD = p.getString("id");
                String ti = p.getString("title");
                String med = p.getString("medium");
                String nam = p.getString("full_name");
                String cre = p.getString("creation_date");
                String dep = p.getString("department");
                String artid = p.getString("artist_id");
                String nat = p.getString("nationality");
                String cla = p.getString("classification");
                String wurl = p.getString("image_url");
                try{ ti = ti.substring(0,15);} catch (Exception e){}

                //Account for multiple urls
                int nextIndex = wurl.indexOf("http",4);
                if (nextIndex > 0)
                    wurl = wurl.substring(0, nextIndex);

                nextIndex = wurl.indexOf("|");
                if (nextIndex > 0) {
                    wurl = wurl.substring(0, nextIndex);
                    System.out.println("found. new wurl = "+wurl);
                }

                if (!wurl.equals("")) {
                    a = new Art(ti, med, nam, cre, iD, dep, artid, nat, cla, wurl);
                    Art.addArt(a);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i < 2; i++) {
            MainActivity.artBuffer.add(Art.getRandomArtPiece());
        }
    }
}
