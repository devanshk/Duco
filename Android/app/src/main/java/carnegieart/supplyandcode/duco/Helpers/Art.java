package carnegieart.supplyandcode.duco.Helpers;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dkukreja on 11/14/15.
 */
public class Art {
    public static ArrayList<Art> artList = new ArrayList<Art>();
    static ArrayList<Art> likedArt = new ArrayList<Art>();
    static ArrayList<Art> dislikedArt = new ArrayList<Art>();
    static Random random;

    public String title, medium, name, creation_date, id, department, artist_id, nationality, classification, url;

    public float score;

    public Art(String title, String medium, String name,
               String creation_date, String id,
               String department, String artist_id, String nationality, String classification, String url){
        this.title = title;
        this.medium = medium;
        this.name = name;
        this.creation_date = creation_date;
        this.id = id;
        this.department = department;
        this.artist_id = artist_id;
        this.nationality = nationality;
        this.classification = classification;
        this.url = url;

        this.random = new Random();
    }

    public static void generateArt(){
        artList.add(new Art("Untitled", null, "Van Goh", null,""+1,null,null,null,null,"http://www.cmoa.org/CollectionDetail.aspx?item=2"));
        artList.add(new Art("Pill Lamp", null, "Picasso", null,""+2,null,null,null,null,"http://www.cmoa.org/CollectionImage.aspx?irn=70387&size=Large"));
        artList.add(new Art("Table Knife", null, "Da Vinci", null,""+3,null,null,null,null,"http://www.cmoa.org/CollectionDetail.aspx?item=52"));
    }

    public static void addArt(Art art){
        artList.add(art);
    }

    public static ArrayList<Art> getArtList(){
        return artList;
    }

    public static Art getArtById(String id){
        for (Art a : artList)
            if (a.id.equals(id))
                return a;
        return null;
    }

    public static Art getRandomArtPiece(){
        int rand = random.nextInt(artList.size());
        System.out.println("rand = " + rand);
        return artList.get(rand);
    }

    public static Art getNextArt(){
        float maxscore = artList.get(0).score;
        int index = 0;

        for(int i = 0; i < artList.size(); i++){1
            Art a = artList.get(i);
            if(a.score > maxscore){
                maxscore = a.score;
                index = i;
            }
        }
        return artList.get(index);

    }

    public static void updateScores(Art recent, int liked){
        String med =  recent.medium;
        String cre = recent.creation_date;
        String dep = recent.department;
        String artid = recent.artist_id;
        String nat = recent.nationality;
        String cla = recent.classification;

        if (liked > 0) likedArt.add(recent);
        else dislikedArt.add(recent);

        artList.remove(recent);
        for(int i = artList.size()-1; i >= 0; i--) {
            Art a = artList.get(i);

            if (med.equals(a.medium))
                a.score += liked * 2;

            if (cre.equals(a.creation_date))
                a.score += liked * .3;

            if (dep.equals(a.department))
                a.score += liked * .5;

            if (artid.equals(a.artist_id))
                a.score += liked;

            if (nat.equals(a.nationality))
                a.score += .6 * liked;

            if (cla.equals(a.classification))
                a.score += .75 * liked;

            if (a.score < -10)
                artList.remove(a);
        }
    }
}
