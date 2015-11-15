package carnegieart.supplyandcode.duco;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;

import carnegieart.supplyandcode.duco.Helpers.Art;
import carnegieart.supplyandcode.duco.Helpers.MultiThread;

public class MainActivity extends Activity {
    SwipeFlingAdapterView flingContainer;
    ImageLoader imageLoader = null;
    public static ArrayList<Art> artBuffer = new ArrayList<Art>();
    public static HashMap<Art, View> viewSet = new HashMap<Art, View>();
    int added = 0;
    View prefsView, prefsShadow, starButton, infoButton;
    TextView prefsTextView;
    static boolean isInfo = false;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        //getActionBar().hide();
        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        prefsView = findViewById(R.id.prefs_layout);
        prefsShadow = findViewById(R.id.prefs_back_shadow);
        prefsTextView = (TextView)findViewById(R.id.prefs_text);
        starButton = findViewById(R.id.star_btn);
        infoButton = findViewById(R.id.m_btn);

        YoYo.with(Techniques.FadeOut).duration(1).playOn(prefsView);
        YoYo.with(Techniques.FadeOut).duration(1).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                prefsShadow.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(prefsShadow);

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
                Art.judged = (Art) o;
                new MultiThread(new Runnable() {
                    @Override
                    public void run() {
                        Art.updateScores(Art.judged, -1);
                    }
                }).executeOnExecutor(Executors.newSingleThreadExecutor());
                isInfo = false;
            }

            @Override
            public void onRightCardExit(Object o) {
                Art.judged = (Art) o;
                new MultiThread(new Runnable() {
                    @Override
                    public void run() {
                        Art.updateScores(Art.judged, 1);
                    }
                }).executeOnExecutor(Executors.newSingleThreadExecutor());
                isInfo = false;
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                added += 1;
                Art nextPiece;
                if (added % 5 == 0)
                    nextPiece = Art.getRandomBadArtPiece();
                else
                    nextPiece = Art.getNextArt();
                System.out.println("nextPiece = " + nextPiece);
                artBuffer.add(nextPiece);

                while (artBuffer.size() < 3)
                    artBuffer.add(Art.getNextArt());
                while (artBuffer.size() > 3)
                    artBuffer.remove(artBuffer.size() - 1);

                artAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float v) {

            }
        });

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefOutput = "Era:\n" + Art.getBestEra() + "\n\nDepartment:\n" + Art.getBestDep() + "\n\nNationality:\n" + Art.getBestNat() + "\n\nMedium:\n" + Art.getBestMedium() + "\n\nArtist:\n" + Art.getBestArtist();
                System.out.println("prefOutput= " + prefOutput);
                prefsTextView.setText(prefOutput);
                prefsShadow.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(300).playOn(prefsShadow);
                YoYo.with(Techniques.SlideInLeft).duration(300).playOn(prefsView);
            }
        });

        prefsShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefsShadow.getVisibility() == View.VISIBLE) {
                    YoYo.with(Techniques.FadeOut).duration(300).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            prefsShadow.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(prefsShadow);
                    YoYo.with(Techniques.SlideOutLeft).duration(300).playOn(prefsView);
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Art a = artAdapter.getItem(0);

                Animation FlipOutLeft = AnimationUtils.loadAnimation(getBaseContext(), R.animator.card_flip_left_out);
                Animation FlipInLeft = AnimationUtils.loadAnimation(getBaseContext(), R.animator.card_flip_left_in);
                FlipOutLeft.setFillAfter(true);
                FlipInLeft.setFillAfter(true);

                View main = flingContainer.getSelectedView();
                View art = (View)main.getTag(R.id.art_view);
                View info = (View)main.getTag(R.id.info_view);
                TextView infoText = (TextView)main.getTag(R.id.info_text);

                String infoStream = "" + a.title + "\n\n" + a.creation_date + "\n\n" + a.department + "\n\n" + a.nationality + "\n\n" + a.name+"\n\n"+a.medium;
                infoText.setText(infoStream);

                //art.startAnimation(FlipOutLeft);
                //info.startAnimation(FlipInLeft);

                if (!isInfo) {
                    info.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInRight).duration(300).playOn(info);
                } else{
                    YoYo.with(Techniques.SlideOutRight).duration(300).playOn(info);
                }

                isInfo = !isInfo;
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
            ImageView infoImage = (ImageView) convertView.findViewById(R.id.info_image_view);
            TextView artScore = (TextView) convertView.findViewById(R.id.art_score);
            TextView artMedium = (TextView) convertView.findViewById(R.id.art_medium);

            String newTitle = art.title;
            try{ newTitle = newTitle.substring(0, 15);} catch (Exception e){}
            String newMedium = art.medium;
            try{ newMedium = newMedium.substring(0,45);} catch (Exception e){}
            // Populate the data into the template view using the data object
            artTitle.setText(newTitle);
            artistName.setText(art.name);
            artScore.setText("" + art.score);
            artMedium.setText(newMedium);
            artMedium.setVisibility(View.GONE);

            //Seperate the views we need as tags
            convertView.setTag(R.id.info_view, convertView.findViewById(R.id.info_view));
            convertView.setTag(R.id.info_text,convertView.findViewById(R.id.info_text));
            convertView.setTag(R.id.art_view, convertView.findViewById(R.id.art_view));

            YoYo.with(Techniques.FadeOut).duration(1).playOn(convertView.findViewById(R.id.info_view));

            imageLoader.displayImage(art.url, artImage);
            imageLoader.displayImage(art.url, infoImage);

            System.out.println("art.url = "+art.url);
            viewSet.put(art, convertView);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
