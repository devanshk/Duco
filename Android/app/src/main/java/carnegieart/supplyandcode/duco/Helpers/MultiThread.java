package carnegieart.supplyandcode.duco.Helpers;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by dkukreja on 11/14/15.
 */
public class MultiThread extends AsyncTask<Void,Integer,Void> {
    private final String TAG = "Async_MultiThread";
    private Runnable runnable;

    public MultiThread(Runnable runnable){
        this.runnable = runnable;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        runnable.run();
        return null;
    }

    protected void onPostExecute(Void v){
    }
}
