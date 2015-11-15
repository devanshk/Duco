package carnegieart.supplyandcode.duco;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import carnegieart.supplyandcode.duco.Helpers.Art;
import carnegieart.supplyandcode.duco.Helpers.Spanner;

public class MainActivity extends AppCompatActivity {
    SwipeFlingAdapterView flingContainer;
    ImageLoader imageLoader = null;
    public static ArrayList<Art> artBuffer = new ArrayList<Art>();
    public static HashMap<Art, View> viewSet = new HashMap<Art, View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));

        final ArtAdapter artAdapter = new ArtAdapter(this, artBuffer);
        flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        flingContainer.setAdapter(artAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                artBuffer.remove(0);
                artAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Art judged = (Art)o;
                Art.updateScores(judged, -1);
            }

            @Override
            public void onRightCardExit(Object o) {
                Art judged = (Art)o;
                Art.updateScores(judged, 1);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                Art nextPiece = Art.getRandomArtPiece();
                artBuffer.add(nextPiece);

                while (artBuffer.size() < 3)
                    artBuffer.add(Art.getNextArt());
                while (artBuffer.size() > 3)
                    artBuffer.remove(artBuffer.size()-1);

                artAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float v) {

            }
        });

    }

    public class ArtAdapter extends ArrayAdapter<Art> {
        public ArtAdapter(Context context, ArrayList<Art> artList) {
            super(context, 0, artList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Art art = getItem(position);
            convertView = viewSet.get(art);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                System.out.println("convertView for "+art.title+" = null");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_art, parent, false);
            }

            // Lookup view for data population
            TextView artTitle = (TextView) convertView.findViewById(R.id.art_title);
            TextView artistName = (TextView) convertView.findViewById(R.id.art_artist_name);
            ImageView artImage = (ImageView) convertView.findViewById(R.id.art_image);
            TextView artScore = (TextView) convertView.findViewById(R.id.art_score);
            TextView artMedium = (TextView) convertView.findViewById(R.id.art_medium);
            // Populate the data into the template view using the data object
            artTitle.setText(art.title);
            artistName.setText(art.name);
            artScore.setText("" + art.score);
            artMedium.setText("" + art.medium);

            imageLoader.displayImage(art.url, artImage);

            System.out.println("art.url = "+art.url);
            viewSet.put(art, convertView);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
