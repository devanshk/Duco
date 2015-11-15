package carnegieart.supplyandcode.duco.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by dkukreja on 11/14/15.
 */
public class Art {
    public static ArrayList<Art> artList = new ArrayList<Art>();
    static ArrayList<Art> likedArt = new ArrayList<Art>();
    static ArrayList<Art> dislikedArt = new ArrayList<Art>();
    static Random random;
    static float maxScore = 0;
    public static Art judged;


    public static HashMap<String, Integer> medinterest = new HashMap<String, Integer>();
    public static HashMap<Integer, Integer> erainterest  = new HashMap<Integer, Integer>();
    public static HashMap<String, Integer> depinterest = new HashMap<String, Integer>();
    public static HashMap<String, Integer> artinterest = new HashMap<String, Integer>();
    public static HashMap<String, Integer> natinterest = new HashMap<String, Integer>();
    public static HashMap<String, Integer> clainterest = new HashMap<String, Integer>();

    public String title, medium, name, creation_date, id, department, artist_id, nationality, classification, url;
    ArrayList<String> mediums = new ArrayList<String>();
    public float score;

    public Art(String title, String medium, String name,
               String creation_date, String id,
               String department, String artist_id, String nationality, String classification, String url){
        this.title = title;
        this.medium = medium;
        mediums = new ArrayList<String>(Arrays.asList(medium.split("\\s*,\\s*")));
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
        return artList.remove(rand);
    }

    public static Art getRandomBadArtPiece(){
        Art next = artList.get(random.nextInt(artList.size()));
        while (next.score > 2*maxScore/3)
            next = artList.get(random.nextInt(artList.size()));
        artList.remove(next);
        return next;
    }

    public static Art getNextArt(){
        float maxscore = artList.get(0).score;
        Art.maxScore = maxscore;
        int index = 0;

        for(int i = 0; i < artList.size(); i++){
            Art a = artList.get(i);
            if(a.score > maxscore){
                maxscore = a.score;
                index = i;
            }
        }
        System.out.println("bestScore = " + maxscore);
        return artList.remove(index);

    }

    public static void updateScores(Art recent, int liked){
        ArrayList<String> meds =  recent.mediums;
        String cre = recent.creation_date;
        String dep = recent.department;
        String nam = recent.name;
        String artid = recent.artist_id;
        String nat = recent.nationality;
        String cla = recent.classification;

        if (liked > 0) likedArt.add(recent);
        else dislikedArt.add(recent);

        //update hashmap

        for (String m : meds) {
            Integer value = medinterest.get(m);
            if (value == null)
                value = 0;
            value += liked;
            medinterest.put(m, value);
        }
        System.out.println("mediums = "+medinterest);

        //department hash
        Integer val = depinterest.get(dep);
        if(val == null)
            val = 0;
        val+=liked;
        depinterest.put(dep, val);

        //artist hash
        val = artinterest.get(nam);
        if(val == null)
            val = 0;
        val+=liked;
        artinterest.put(nam, val);

        //nationality hash
        val = natinterest.get(nat);
        if(val == null)
            val = 0;
        val+=liked;
        natinterest.put(nat, val);

        //classification hashmap
        val = clainterest.get(cla);
        if(val == null)
            val = 0;
        val+=liked;
        clainterest.put(cla, val);

        boolean eragotten = false;

        //artList.remove(recent);
        for(int x = artList.size()-1; x >= 0; x--) {
            Art a = artList.get(x);

            int breaker = 0;
            for(String s1: a.mediums){
                for(String s2:meds){
                    if(s1.equals(s2) && breaker!=1){
                        breaker=1;
                        a.score+= liked*1.2;
                        break;
                    }
                }
            }

            if (cre.equals(a.creation_date))
                a.score += liked * .3;

            if (dep.equals(a.department))
                a.score += liked * .5;
            if(!a.name.toLowerCase().contains("unkown")) {
                    if (artid.equals(a.artist_id))
                        a.score += liked;
            }
            if (nat.equals(a.nationality))
                a.score += .6 * liked;

            if (cla.equals(a.classification))
                a.score += .75 * liked;

            //compare dates
            String adate = a.creation_date;
            int recint = 0;
            int currint = 0;
            int counter = 0;
            char c1 = 'a', c2 = 'b';

            for(int i = 0; i < adate.length(); i++){
                if (counter == 0){
                    c1 = adate.charAt(i);
                }
                if (counter == 1){
                    c2 = adate.charAt(i);
                }
                if(Character.isDigit(c1) && counter ==0){
                    counter++;
                }
                if(counter==1 && Character.isDigit(c2) && Character.isDigit(c1)){
                    counter++;
                    currint = Integer.parseInt(""+c1+c2);
                }
                if(counter==2) break;
            }

            counter = 0;
            c1 = 'a';
            c2 = 'b';
            for(int i = 0; i < cre.length(); i++){
                if (counter == 0) c1 = cre.charAt(i);
                if (counter == 1) c2 = cre.charAt(i);
                if(Character.isDigit(c1) && counter ==0){
                    counter++;
                }
                if(counter==1 && Character.isDigit(c2)){
                    counter++;

                    recint = Integer.parseInt(""+c1+c2);
                    if(!eragotten){
                        Integer value = erainterest.get((Integer) recint);
                        if(value == null) {
                            value = 0;
                        }
                        value+=liked;
                        erainterest.put((Integer)recint, value);
                        eragotten=true;
                    }
                }
                if(counter==2) break;
            }

            if(recint == currint){
                a.score+= liked*.3;
            }

            if (a.score < -30) {
                System.out.println("BANG! Destroyed.");
                artList.remove(a);
            }
        }
    }


    //getBest Methods
    public static String getBestMedium(){
        Integer maxval = null;
        String bestmedium = "";
        for (Map.Entry<String, Integer> entry : medinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if((maxval == null) || (value > maxval && !key.toLowerCase().equals("unknown"))){
                maxval = value;
                bestmedium = key;
            }

        }
        return bestmedium;
    }
    public static String getBestEra(){
        Integer maxval = null;
        Integer bestera = 0;
        for (Map.Entry<Integer, Integer> entry : erainterest.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(maxval == null || value > maxval){
                maxval = value;
                bestera = key;
            }

        }
        String besteraString;
        if(bestera <= 20 && bestera>=10)
            besteraString = ""+bestera+"00s";
        else
            besteraString = "no date";
        return besteraString;
    }

    public static String getBestDep(){
        Integer maxval = null;
        String bestDep = "";
        for (Map.Entry<String, Integer> entry : depinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(maxval == null || value > maxval){
                maxval = value;
                bestDep = key;
            }

        }
        return bestDep;
    }
    public static String getBestArtist(){
        Integer maxval = null;
        String bestArtist = "";
        for (Map.Entry<String, Integer> entry : artinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if((maxval == null) || (value > maxval && !key.toLowerCase().equals("unknown"))){
                maxval = value;
                bestArtist = key;
            }
        }
        return bestArtist;
    }
    public static String getBestNat(){
        Integer maxval = null;
        String bestNat = "";
        for (Map.Entry<String, Integer> entry : natinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(maxval == null || value > maxval){
                maxval = value;
                bestNat = key;
            }

        }
        return bestNat;
    }
    public static String getBestClass(){
        Integer maxval = null;
        String bestCla = "";
        for (Map.Entry<String, Integer> entry : clainterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(maxval == null || value > maxval){
                maxval = value;
                bestCla = key;
            }

        }
        return bestCla;
    }
}
