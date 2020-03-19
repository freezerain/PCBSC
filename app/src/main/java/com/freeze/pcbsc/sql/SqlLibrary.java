package com.freeze.pcbsc.sql;

import android.content.SharedPreferences;

import com.freeze.pcbsc.MainActivity;
import com.freeze.pcbsc.R;

public final class SqlLibrary {
    private SqlLibrary(){}
    private static final String DROP_STRING = "DROP TABLE if exists Builds; ";
    private static final String CREATE_STRING_PREFIX = "create table if not exists Builds( ";
    private static final String CREATE_STRING_SUFIX = ");";
    private static final String ID_COLUMNS_STRING = "id INTEGER primary key, ";
    private static final String COLUMNS_STRING_CREATE = "price INTEGER, score INTEGER, cpu_price integer, gpu_price integer, ram_price integer, " +
            "wattage INTEGER, cpu_wattage INTEGER, gpu_wattage INTEGER, level INTEGER, percent_through INTEGER, cpu TEXT, gpu TEXT, ram TEXT, " +
            "is_dual INTEGER, channel INTEGER";
    private static final String COLUMNS_STRING_SELECT = "price , score , cpu_price , gpu_price , ram_price , " +
            "wattage , cpu_wattage , gpu_wattage , level , percent_through , cpu , gpu , ram , " +
            "is_dual , channel ";


    public static String getDropTableStatement(){
        return DROP_STRING;
    }
    public static String getCreateTableStatement(){
        StringBuilder sb = new StringBuilder();
        sb.append(CREATE_STRING_PREFIX).append(ID_COLUMNS_STRING).
                append(COLUMNS_STRING_CREATE).append(CREATE_STRING_SUFIX);
        return sb.toString();
    }

    public static String getSelectFromBuildsStatement(){
        return "select * from Builds order by Price, Score limit 100;";
    }

    public static String getGenerateBuildsStatement(MainActivity activity, boolean dual, int channel, SharedPreferences sp){
        StringBuilder sb = new StringBuilder();
        int priceFilter = sp.getInt(activity.getString(R.string.price), activity.getResources().getInteger(R.integer.price_default));
        int levelFilter = sp.getInt(activity.getString(R.string.level), activity.getResources().getInteger(R.integer.level_default));
        int wattageFilter = sp.getInt(activity.getString(R.string.wattage), activity.getResources().getInteger(R.integer.wattage_default));
        int scoreFilter = sp.getInt(activity.getString(R.string.score), activity.getResources().getInteger(R.integer.score_default));
        boolean waterGpuFilter = sp.getBoolean(activity.getString(R.string.isWaterCooled), activity.getResources().getBoolean(R.bool.isWatercooled_default));

        StringBuilder whereStatement = new StringBuilder(" where c.\"In Shop\" = \"Yes\" and g.\"In Shop\" =\"Yes\" and r.\"In Shop\" = \"Yes\" ");
        if(priceFilter>0) whereStatement.append(" and (c.Price + ").append(dual? "(g.Price * 2)" : "g.Price").
                append(" + ").append("(r.Price*").append(channel)
                .append(")) <= ").append(priceFilter).append(" ");

        if(levelFilter>0) whereStatement.append(" and cast(max(c.Level, g.Level, r.Level) as integer) <= ").append(levelFilter).append(" ");

        if(wattageFilter>0) whereStatement.append(" and (c.Wattage + 30 +").append(dual? "(g.Wattage * 2)": "g.Wattage").append(") <= ")
                .append(wattageFilter).append(" ");

        if(scoreFilter>0) whereStatement.append("and cast(1 / ((0.15 / (298*((c.CoreClockMultiplier*c.Frequency)+(c.MemChannelsMultiplier*").
                append(channel).append(")+(c.MemClockMultiplier*r.Frequency)+c.FinalAdjustment)))").
                append(" + (0.85 / g.\"").append(dual? "Double GPU Graphics Score" : "Single GPU Graphics Score")
                .append("\")) as integer)  >= ").append(scoreFilter).append(" ");

        if(channel>=2) whereStatement.append(" and cast(c.\"Max Memory Channels\" as integer)>=").append(channel).append(" ");

        sb.append("insert into Builds(").append(COLUMNS_STRING_SELECT).append(") ");
        sb.append(" select (c.Price + ").append(dual? "(g.Price * 2)" : "g.Price").
                append(" + ").append("(r.Price*").append(channel).append(")) as price, ");

        sb.append("cast(1 / ((0.15 / (298*((c.CoreClockMultiplier*c.Frequency)+(c.MemChannelsMultiplier*");
        sb.append(channel);
        sb.append(")+(c.MemClockMultiplier*r.Frequency)+c.FinalAdjustment)))");
        sb.append(" + (0.85 / g.\"").append(dual? "Double GPU Graphics Score" : "Single GPU Graphics Score").
                append("\")) as integer) as score, ");

        sb.append("c.Price as cpu_price, g.Price as gpu_price, r.Price as ram_price, ");

        sb.append("(c.Wattage + 30 +").append(dual? "(g.Wattage * 2)": "g.Wattage").append(") as wattage, ");

        sb.append("c.Wattage as cpu_wattage, ").append("g.Wattage as gpu_wattage, ");

        sb.append("max(c.Level, g.Level, r.Level) as level, ");

        sb.append("case when cast(c.Level as integer) > cast(g.Level as integer) " +
                "              then case when cast(c.Level as integer) > cast(r.Level as integer) " +
                "                        then case when c.\"Percent Through\" != \"1\" " +
                "                                  then 1 else 0 end " +
                "                        else case when cast(c.Level as integer) < cast(r.Level as integer) " +
                "                                  then case when r.\"Percent Through\" != \"1\" " +
                "                                            then 1 else 0 end " +
                "                                  else case when max(cast(c.\"Percent Through\" as integer), cast(r.\"Percent Through\" as integer)) > 1 " +
                "                                            then 1 else 0 end " +
                "                                  end " +
                "                        end " +
                "              else case when cast(c.Level as integer) < cast(g.Level as integer)  " +
                "                        then case when g.\"Percent Through\" != \"1\"  " +
                "                                  then 1 else 0 end " +
                "                        else case when max(cast(c.\"Percent Through\" as integer), cast(g.\"Percent Through\" as integer)) > 1  " +
                "                                  then 1 else 0 end " +
                "                        end " +
                "              end " +
                "              as percent_through, ");

        sb.append("c.\"Part Name\" as cpu, g.\"Part Name\" as gpu, r.\"Part Name\" as ram, ");

        sb.append(dual? "1" : "0").append(" as is_dual, ").append(channel).append(" as channel ");

        sb.append("from CPUsTable as c cross join ").append(waterGpuFilter? "WaterCooledGPUsTable" : "GPUsTable").append(" as g ").
                append(dual? "on cast(\"Double GPU Graphics Score\" as integer)>0 " :"").
                append( " cross join RAMsTable as r ");
        sb.append(whereStatement);
        sb.append(" order by Price, Score limit 500;");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
