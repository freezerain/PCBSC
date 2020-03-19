package com.freeze.pcbsc.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.freeze.pcbsc.MainActivity;
import com.freeze.pcbsc.R;
import com.freeze.pcbsc.sql.DBHelper;
import com.freeze.pcbsc.sql.SqlLibrary;

/*
This task calling sql statement to generate buidls Table
 */

public class GenerateTableAsyncTask extends AsyncTask<Void, Integer, Void> {

    private MainActivity activity;
    private ProgressBar progressBar;


    public GenerateTableAsyncTask(MainActivity activity, ProgressBar progressBar) {
        this.activity = activity; this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SharedPreferences sp = activity.getPreferences(0);
        //get filters
        boolean isDual = sp.getBoolean(activity.getString(R.string.isDualGPU), activity.getResources().getBoolean(R.bool.isDualGPU_default));
        boolean channel1 = sp.getBoolean(activity.getString(R.string.channel1), activity.getResources().getBoolean(R.bool.channel1_default));
        boolean channel2 = sp.getBoolean(activity.getString(R.string.channel2), activity.getResources().getBoolean(R.bool.channel2_default));
        boolean channel3 = sp.getBoolean(activity.getString(R.string.channel3), activity.getResources().getBoolean(R.bool.channel3_default));
        boolean channel4 = sp.getBoolean(activity.getString(R.string.channel4), activity.getResources().getBoolean(R.bool.channel4_default));

        //Count how many work should be done
        int progressMaxSteps = 0;
        if(channel1) progressMaxSteps++;
        if(channel2) progressMaxSteps++;
        if(channel3) progressMaxSteps++;
        if(channel4) progressMaxSteps++;
        if(isDual || progressMaxSteps>0) progressMaxSteps *= 2;

        int progressStep = 100/ (progressMaxSteps+2);
        int currentProgress = 0;
        publishProgress(currentProgress);

        //prepare table
        db.execSQL(SqlLibrary.getDropTableStatement());
        currentProgress+=progressStep;
        publishProgress(currentProgress);
        db.execSQL(SqlLibrary.getCreateTableStatement());
        currentProgress+=progressStep;
        publishProgress(currentProgress);

        //If no work should be done - exit method
        if(progressMaxSteps<1) {
            Log.d("GenerateAsyncTask", "Zero Progress steps, returning null");
            return null;
        }

        //fill table
        if(channel1) {
            Log.d("GenerateTableAsyncTask", "1 Channel Solo generation started");
            db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, false,1,sp));
            currentProgress+=progressStep;
            publishProgress(currentProgress);
            if(isDual){
                Log.d("GenerateTableAsyncTask", "1 Channel Dual generation started");
                db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, true,1,sp));
                currentProgress+=progressStep;
                publishProgress(currentProgress);
            }
        }

        if(channel2) {
            Log.d("GenerateTableAsyncTask", "2 Channel Solo generation started");
            db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, false,2,sp));
            currentProgress+=progressStep;
            publishProgress(currentProgress);
            if(isDual){
                Log.d("GenerateTableAsyncTask", "2 Channel Dual generation started");
                db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, true,2,sp));
                currentProgress+=progressStep;
                publishProgress(currentProgress);
            }
        }
        if(channel3) {
            Log.d("GenerateTableAsyncTask", "3 Channel Solo generation started");
            db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, false,3,sp));
            currentProgress+=progressStep;
            publishProgress(currentProgress);
            if(isDual){
                Log.d("GenerateTableAsyncTask", "3 Channel Dual generation started");
                db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, true,3,sp));
                currentProgress+=progressStep;
                publishProgress(currentProgress);
            }
        }
        if(channel4) {
            Log.d("GenerateTableAsyncTask", "4 Channel Solo generation started");
            db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, false,4,sp));
            currentProgress+=progressStep;
            publishProgress(currentProgress);
            if(isDual){
                Log.d("GenerateTableAsyncTask", "4 Channel Dual generation started");
                db.execSQL(SqlLibrary.getGenerateBuildsStatement(activity, true,4,sp));
                currentProgress+=progressStep;
                publishProgress(currentProgress);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
        Log.d("GenerateTableAsyncTask", "Progress report: " + values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.setVisibility(View.INVISIBLE);
        BuildListAsyncTask buildListAsyncTask = new BuildListAsyncTask(activity, progressBar);
        buildListAsyncTask.execute();
    }

}
