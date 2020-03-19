package com.freeze.pcbsc.asyncTasks;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.freeze.pcbsc.MainActivity;
import com.freeze.pcbsc.R;
import com.freeze.pcbsc.sql.DBHelper;
import com.freeze.pcbsc.sql.SqlLibrary;

import static java.lang.Thread.sleep;

//Test task for loading bar set up

public class AsyncTestTask extends AsyncTask<Void, Integer, Void> {

    private ProgressBar progressBar;


    public AsyncTestTask(ProgressBar progressBar) {
         this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i<10;i++){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(i*10);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
        System.out.println(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
