package carnegieart.supplyandcode.duco.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Devansh Kukreja and Sam Nosenzo on 11/14/15.
 */


//Updated by Sam Nosenzo 11/21/15
public class Art {

    //Initialization of static variables:

    //artList stored all of the art objects
    public static ArrayList<Art> artList = new ArrayList<Art>();

    //likedart stored art that the user swiped right on
    static ArrayList<Art> likedArt = new ArrayList<Art>();

    //array of disliked art
    static ArrayList<Art> dislikedArt = new ArrayList<Art>();
    //random object to allow fort he grabbing of a random art every 4 or 5 selections
    static Random random;

    //initialization of maxscore variable to get the the next recommended artwork
    static float maxScore = 0;
    public static Art judged;


    //These are all hashmaps of data that the users seems to have taken interest in
    //The hashmaps use the expressed interest in a subsect of an area (i.e. woodcutting for mediums, 1900s for era, Contemporary for Dipartment)
    //as keys and have the value of an integer
    //The integer represents the score of the persons interest in that particular type of art
    //We use these to get their overall interests when they want to see it.
    public static HashMap<String, Integer> medinterest = new HashMap<String, Integer>(); //Medium interests
    public static HashMap<Integer, Integer> erainterest  = new HashMap<Integer, Integer>(); //Era
    public static HashMap<String, Integer> depinterest = new HashMap<String, Integer>(); //department
    public static HashMap<String, Integer> artinterest = new HashMap<String, Integer>(); //Artist 
    public static HashMap<String, Integer> natinterest = new HashMap<String, Integer>(); //nationality
    public static HashMap<String, Integer> clainterest = new HashMap<String, Integer>(); //classification

    //These are the variables used to store the information from each individual art piece
    public String title, medium, name, creation_date, id, department, artist_id, nationality, classification, url;
    ArrayList<String> mediums = new ArrayList<String>();
    public float score; //all artpieces have a score


    //Constructor for art
    public Art(String title, String medium, String name,
               String creation_date, String id,
               String department, String artist_id, String nationality, String classification, String url){
        this.title = title;
        this.medium = medium;
        //the medium is split up by commas because we found that some had multiple mediums
        //This allows for greater accuracy we believe
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

    //function used to read in art manually--used as a test
    public static void generateArt(){
        artList.add(new Art("Untitled", null, "Van Goh", null,""+1,null,null,null,null,"http://www.cmoa.org/CollectionDetail.aspx?item=2"));
        artList.add(new Art("Pill Lamp", null, "Picasso", null,""+2,null,null,null,null,"http://www.cmoa.org/CollectionImage.aspx?irn=70387&size=Large"));
        artList.add(new Art("Table Knife", null, "Da Vinci", null,""+3,null,null,null,null,"http://www.cmoa.org/CollectionDetail.aspx?item=52"));
    }

    //adds an artpiece to the ArrayList
    public static void addArt(Art art){
        artList.add(art);
    }

    //Gets the entire artlist
    public static ArrayList<Art> getArtList(){
        return artList;
    }

    //Allows for a search of the artlist by the id of the artpiece
    public static Art getArtById(String id){
        for (Art a : artList)
            if (a.id.equals(id))
                return a;
        return null;
    }

    //returns a random artpiece
    public static Art getRandomArtPiece(){
        int rand = random.nextInt(artList.size());
        System.out.println("rand = " + rand);
        return artList.remove(rand);
    }

    //returns a random artpiece with a score that is lower than a certain point
    //point is proportional to the current max score
    public static Art getRandomBadArtPiece(){
        Art next = artList.get(random.nextInt(artList.size()));
        while (next.score > 2*maxScore/3)
            next = artList.get(random.nextInt(artList.size()));
        artList.remove(next);
        return next;
    }

    //gets the next recommended art for the user
    //cycles throught the array and pulls out the one with the highest score
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

    //This function updates the scores of all of the artworks in the arraylist
    //based on the most recent artwork the user has voted on and what he voted on it
    public static void updateScores(Art recent, int liked){ //liked variable is a 1 or -1

        //Takes all necessary info from the most recent art piece that the user voted on
        ArrayList<String> meds =  recent.mediums;
        String cre = recent.creation_date;
        String dep = recent.department;
        String nam = recent.name;
        String artid = recent.artist_id;
        String nat = recent.nationality;
        String cla = recent.classification;

        //adds the piece to the liked or disliked art array depending on the vote
        if (liked > 0) likedArt.add(recent);
        else dislikedArt.add(recent);

        //update hashmaps scores

        //medium hashmap
        //cycles throught the mediums of the most recent artpiece
        for (String m : meds) {
            Integer value = medinterest.get(m);
            //if the medium is not in the hashmap already then it is added and the initial score is zero
            if (value == null)
                value = 0;
            //the value of the mediums that were voted on are increased or decreased by 1 depending on the vote
            value += liked;
            medinterest.put(m, value); //value set back into the hashmap
        }
        System.out.println("mediums = "+medinterest);


        //similar concepts for the following interest hashmaps
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

        //This for loop updates the scores of each artwork
        //cycles through entire artlist
        for(int x = artList.size()-1; x >= 0; x--) {
            Art a = artList.get(x);

            //updates score if any mediums are the same
            //only adds to score for one medium
            int breaker = 0;
            for(String s1: a.mediums){
                for(String s2:meds){
                    if(s1.equals(s2) && breaker!=1){
                        breaker=1;
                        a.score+= liked*1.2;//weighted 1.2x
                        break;
                    }
                }
            }

            //updates score if creation date matches
            if (cre.equals(a.creation_date))
                a.score += liked * .3;//weighted .3x


            //if department matches
            if (dep.equals(a.department))
                a.score += liked * .5;

            //updates score if the artist matches
            //checks for "unkown" artists
            if(!a.name.toLowerCase().contains("unkown")) {
                    if (artid.equals(a.artist_id))
                        a.score += liked;
            }

            //updates score for nationality
            if (nat.equals(a.nationality))
                a.score += .6 * liked;


            //updates score for classification
            if (cla.equals(a.classification))
                a.score += .75 * liked;



            //the following section compares the dates
            //can definitely be improved upon
            //The creation date data was widely varied in format, so we just took the first 2 numbers and called it an era
            String adate = a.creation_date; //date for current artpiece in the array
            int recint = 0; //will be 1st two digits of the era of the most recently voted on artpiece
            int currint = 0; //1st two digits of the current artpiece in the array
            int counter = 0; //used to count the digits
            char c1 = 'a', c2 = 'b'; //initialization of variables

            //finds first two digits in the date of the current artpiece in the array
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
            //finds first two digits in the date of the most recint artpieces
            //also updates hashmap of the era interests (only for first comparison) --still, sloppy
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

            //officially updates the score of the artwork based on the era
            if(recint == currint){
                a.score+= liked*.3;
            }

            //removes any artworks with a score less than 30
            if (a.score < -30) {
                System.out.println("BANG! Destroyed.");
                artList.remove(a);
            }
        }
    }


    //getBest Methods

    //this gets the users favortie medium based on his votes
    public static String getBestMedium(){
        Integer maxval = null;
        String bestmedium = "";
        //cycles through hashmap
        for (Map.Entry<String, Integer> entry : medinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            //makes sure that the medium is not unkown
            if((maxval == null) || (value > maxval && !key.toLowerCase().equals("unknown"))){
                maxval = value;
                bestmedium = key;
            }

        }
        return bestmedium;
    }

    //gets the users favorite era
    public static String getBestEra(){
        //finds maxvalue by going through hashmap
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
        //transforms the int into a string
        String besteraString;
        //if the int is between 10 and 20, then it makes it an era by slapping two 00s on the end
        if(bestera <= 20 && bestera>=10)
            besteraString = ""+bestera+"00s";
        else //otherwise it's just no date. Or methods are pretty flawed and could use improvement
            besteraString = "no date";
        return besteraString;
    }

    //gets the users favorite department 
    public static String getBestDep(){
        Integer maxval = null;
        String bestDep = "";
        //cycles through and returns string with top score
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

    //gets the users favorite artist
    public static String getBestArtist(){
        Integer maxval = null;
        String bestArtist = "";
        //cycles through and returns artist with top score
        for (Map.Entry<String, Integer> entry : artinterest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            //makes sure the artist is not unkown
            if((maxval == null) || (value > maxval && !key.toLowerCase().equals("unknown"))){
                maxval = value;
                bestArtist = key;
            }
        }
        return bestArtist;
    }

    //gets the users favorite artist nationality
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

    //gets the users favorite classification of art
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
    
}//end of art class
