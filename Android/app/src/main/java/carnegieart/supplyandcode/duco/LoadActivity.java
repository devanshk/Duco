package carnegieart.supplyandcode.duco;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
    }
}
