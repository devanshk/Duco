package carnegieart.supplyandcode.duco;

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

import carnegieart.supplyandcode.duco.Helpers.MultiThread;
import carnegieart.supplyandcode.duco.Helpers.Spanner;
import me.fichardu.circleprogress.CircleProgress;

public class LoadActivity extends AppCompatActivity {
    Spanner spanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        getSupportActionBar().hide();

        spanner = (Spanner)findViewById(R.id.cp_progress);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Avenir-Medium.ttf");
        TextView myTextView = (TextView)findViewById(R.id.loading_data);
        myTextView.setTypeface(myTypeface);

        new MultiThread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("cmoa.json");
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

    void loadData() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("things");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                System.out.println("i="+i);
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                /*"title": "Keith Haring",
              "creation_date": "1984",
              "creation_date_earliest": "1984-01-01",
              "creation_date_latest": "1984-01-01",
              "medium": "gelatin silver print",
              "accession_number": "2002.17",
              "id": "692a68c5-af1e-4124-80f1-cbf38be51abe",
              "credit_line": "Milton Fine Fund",
              "date_acquired": "2002-06-06",
              "department": "Contemporary Art",
              "physical_location": "Not on View",
              "item_width": 23.25,
              "item_height": 29.25,
              "item_depth": 1.25,
              "item_diameter": 0.0,
              "web_url": "http://www.cmoa.org/CollectionDetail.aspx?item=1",
              "provenance_text": "The Robert Mapplethorpe Foundation, 2002",
              "classification": "photographs",*/
                Log.d("Details-->", jo_inside.getString("formule"));
                String formula_value = jo_inside.getString("formule");
                String url_value = jo_inside.getString("url");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("formule", formula_value);
                m_li.put("url", url_value);

                formList.add(m_li);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
