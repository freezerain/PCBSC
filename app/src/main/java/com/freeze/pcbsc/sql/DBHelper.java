package com.freeze.pcbsc.sql;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.freeze.pcbsc.R;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "myfin.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //setForcedUpgrade();
    }

    public ArrayList<BuildDTO> getBuilds(Activity activity){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(SqlLibrary.getSelectFromBuildsStatement(), null );
        return parseResToBuild(res);
    }

    public ArrayList<BuildDTO> parseResToBuild(Cursor res){
        ArrayList<BuildDTO> buildList = new ArrayList<>();
        res.moveToFirst();
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
        }
        return buildList;
    }

    public void generateBuildsTable(){
        /*SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(SqlLibrary.getDropTableStatement());
        db.execSQL(SqlLibrary.getCreateTableStatement());
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(false,1));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(false,2));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(false,3));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(false,4));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(true,1));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(true,2));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(true,3));
        db.execSQL(SqlLibrary.getGenerateBuildsStatement(true,4));*/
    }

}