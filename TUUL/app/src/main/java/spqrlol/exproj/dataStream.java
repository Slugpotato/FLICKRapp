package spqrlol.exproj;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by ilaforces1 on 4/29/15.
 */

// Download status, used primarily for debugging
enum Status{IDLE, PROCESSING, NOT_INITIALISED,FAILED_OR_EMPTY, OK}

public class dataStream {

    private String LOG_TAG = dataStream.class.getSimpleName();
    private String RawURL;
    private String Data;
    private Status status;

    public dataStream(String rawURL) {
        this.RawURL = rawURL;
        this.status = Status.IDLE;

    }

    public void resetStatus() {
        this.status = Status.IDLE;
        this.RawURL = null;
        this.Data = null;

    }

    public String getData() {
        return Data;

    }

    public Status getStatus() {
        return status;

    }

    public void setRawURL(String rawURL) {
        RawURL = rawURL;
    }

    public void execute() {
        this.status = Status.PROCESSING;
        RawData rawData = new RawData();
        rawData.execute(RawURL);

    }

    public class RawData extends AsyncTask<String,Void, String >{

        protected void onPostExecute(String webData){
            Data = webData;
            Log.v(LOG_TAG, "Data returned was: " + Data);

            if(Data == null){
                if(RawURL==null){
                    status = spqrlol.exproj.Status.NOT_INITIALISED;

                }else{
                    status = spqrlol.exproj.Status.FAILED_OR_EMPTY;
                }

            }else{
                // Successfully received data from URL
                status = spqrlol.exproj.Status.OK;
            }

        }

        protected String doInBackground(String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if (params == null)
                return null;

            try{

                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine() ) != null ){
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;

            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();

                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                        return null;
                    }

                }

            }

        }

    }

}
