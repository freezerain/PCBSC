package com.freeze.pcbsc.asyncTasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.freeze.pcbsc.MainActivity;
import com.freeze.pcbsc.R;
import com.freeze.pcbsc.sql.BuildDTO;
import com.freeze.pcbsc.sql.DBHelper;
import com.freeze.pcbsc.sql.SqlLibrary;

import java.util.ArrayList;
//Class Finished
public class BuildListAsyncTask extends AsyncTask<Void, Integer, ArrayList<BuildDTO>> {

    /*This task is parsing sql data to BuildsDTO array
    */

    private MainActivity activity;
    private ProgressBar progressBar;

    public BuildListAsyncTask(MainActivity activity, ProgressBar progressBar) {
        this.activity = activity; this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    @Override
    protected ArrayList<BuildDTO> doInBackground(Void... voids) {
        DBHelper dbHelper = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery(SqlLibrary.getSelectFromBuildsStatement(), null );
        ArrayList<BuildDTO> buildList = new ArrayList<>();
        int size = res.getCount();
        if(size < 1) {
            Log.d("BuildListAsyncTask", "Empty sql table, returning null");
            return null;
        }
        int i = 0;
        Log.d("BuildListAsyncTask", "Sql to BuildDTO task started");
        if(res.moveToFirst()){
            while(!res.isAfterLast()){
                BuildDTO build = new BuildDTO();
                build.setID(res.getInt(res.getColumnIndex("id")));
                build.setPrice(res.getInt(res.getColumnIndex("price")));
                build.setScore(res.getInt(res.getColumnIndex("score")));
                build.setCpuPrice(res.getInt(res.getColumnIndex("cpu_price")));
                build.setGpuPrice(res.getInt(res.getColumnIndex("gpu_price")));
                build.setRamPrice(res.getInt(res.getColumnIndex("ram_price")));
                build.setWattage(res.getInt(res.getColumnIndex("wattage")));
                build.setCpuWattage(res.getInt(res.getColumnIndex("cpu_wattage")));
                build.setGpuWattage(res.getInt(res.getColumnIndex("gpu_wattage")));
                build.setLevel(res.getInt(res.getColumnIndex("level")));
                build.setPercentThrough(res.getInt(res.getColumnIndex("percent_through"))!=0);
                build.setCPU(res.getString(res.getColumnIndex("cpu")));
                build.setGPU(res.getString(res.getColumnIndex("gpu")));
                build.setRAM(res.getString(res.getColumnIndex("ram")));
                build.setDualGPU(res.getInt(res.getColumnIndex("is_dual"))!=0);
                build.setRamChannels(res.getInt(res.getColumnIndex("channel")));
                buildList.add(build);
                res.moveToNext();
                i++;
                publishProgress(Math.round(((float)i / (float)size)*100));
            }
        }
        res.close();
        return buildList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<BuildDTO> buildList) {
        progressBar.setVisibility(View.INVISIBLE);
        BuildTableAsyncTask buildTableAsyncTask = new BuildTableAsyncTask(activity, (TableLayout)activity.findViewById(R.id.table));
        try {
            buildTableAsyncTask.execute(buildList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
