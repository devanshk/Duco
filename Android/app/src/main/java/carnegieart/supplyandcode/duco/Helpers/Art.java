package carnegieart.supplyandcode.duco.Helpers;

import java.util.ArrayList;

/**
 * Created by dkukreja on 11/14/15.
 */
public class Art {
    private static ArrayList<Art> artList = new ArrayList<Art>();

    String title, medium, name,
            creation_date, id,
            department, artistid, nationality, classification, url;
    float score;

    static void addArt(String id){
        artList.add(getArtById(id));
    }

    static ArrayList<Art> getArtList(){
        return artList;
    }

    static Art getArtById(String id){
        for (Art a : artList)
            if (a.id.equals(id))
                return a;
        return null;
    }
}
