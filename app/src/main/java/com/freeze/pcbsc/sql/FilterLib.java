package com.freeze.pcbsc.sql;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.freeze.pcbsc.R;

public final class FilterLib {

    private FilterLib(){}

    public static String getWhereStatement(Activity activity){
        Resources r = activity.getResources();
        StringBuilder sb = new StringBuilder("where ");
        SharedPreferences sp = activity.getPreferences(0);
        boolean isDual = sp.getBoolean(activity.getString(R.string.isDualGPU), r.getBoolean(R.bool.isDualGPU_default));
        String priceName;
        String wattageName;
        if(isDual){priceName = "Dual Price"; wattageName = "Dual Wattage";}
        else {priceName = "Build Price"; wattageName = "Wattage";}

        if(isDual)sb.append("b.\"Double GPU Ready\" = 1 and ");

        if(!sp.getBoolean(activity.getString(R.string.percentThrough), r.getBoolean(R.bool.percentThrough_default)))sb.append("b.\"Percent Through\" = 1 and ");

        boolean isWatercooled = sp.getBoolean(activity.getString(R.string.isWaterCooled), r.getBoolean(R.bool.isWatercooled_default));
        sb.append("b.\"Water Cooled\" = ").append(isWatercooled ? 1 : 0).append(" and ");

        int wattage = sp.getInt(activity.getString(R.string.wattage), r.getInteger(R.integer.wattage_default));
        if(wattage>0) sb.append("b.\"").append(wattageName).append("\" <= ").append(wattage).append(" and ");

        int price = sp.getInt(activity.getString(R.string.price), r.getInteger(R.integer.price_default));
        if(price>0) sb.append("b.\"").append(priceName).append("\" <= ").append(price).append(" and ");

        int level = sp.getInt(activity.getString(R.string.level), r.getInteger(R.integer.level_default));
        if(level>0) sb.append("b.\"Level\" <= ").append(level).append(" and ");

        boolean channel1 = sp.getBoolean(activity.getString(R.string.channel1), r.getBoolean(R.bool.channel1_default));
        boolean channel2 = sp.getBoolean(activity.getString(R.string.channel2), r.getBoolean(R.bool.channel2_default));
        boolean channel3 = sp.getBoolean(activity.getString(R.string.channel3), r.getBoolean(R.bool.channel3_default));
        boolean channel4 = sp.getBoolean(activity.getString(R.string.channel4), r.getBoolean(R.bool.channel4_default));
        int score = sp.getInt(activity.getString(R.string.score), r.getInteger(R.integer.score_default));
        if(score>0){
            if(channel1 || channel2 || channel3 || channel4){
                sb.append("(");
                if (channel1) sb.append("b.\"").append(isDual ? "Score Dual 1R" : "Score Solo 1R").append("\" >= ").append(score).append(" or ");
                if (channel2) sb.append("b.\"").append(isDual ? "Score Dual 2R" : "Score Solo 2R").append("\" >= ").append(score).append(" or ");
                if (channel2) sb.append("b.\"").append(isDual ? "Score Dual 3R" : "Score Solo 3R").append("\" >= ").append(score).append(" or ");
                if (channel2) sb.append("b.\"").append(isDual ? "Score Dual 4R" : "Score Solo 4R").append("\" >= ").append(score).append(" or ");
                sb.setLength(sb.length()-4);
                sb.append(") and ");
            }else sb.append("b.\"").append(isDual ? "Score Dual 1R" : "Score Solo 1R").append("\" >= ").append(score).append(" and ");
        }
        sb.setLength(sb.length()-5);
        return sb.toString();
    }

    public static boolean getIsDualFilter(Activity activity){
        return activity.getPreferences(0).
                getBoolean(activity.getString(R.string.isDualGPU), activity.getResources().getBoolean(R.bool.isDualGPU_default));
    }

    public static boolean getChannel1Filter(Activity activity){
        return activity.getPreferences(0).
                getBoolean(activity.getString(R.string.channel1),
                        activity.getResources().getBoolean(R.bool.channel1_default));
    }
    public static boolean getChannel2Filter(Activity activity){
        return activity.getPreferences(0).
                getBoolean(activity.getString(R.string.channel2),
                        activity.getResources().getBoolean(R.bool.channel2_default));
    }
    public static boolean getChannel3Filter(Activity activity){
        return activity.getPreferences(0).
                getBoolean(activity.getString(R.string.channel3),
                        activity.getResources().getBoolean(R.bool.channel3_default));
    }
    public static boolean getChannel4Filter(Activity activity){
        return activity.getPreferences(0).
                getBoolean(activity.getString(R.string.channel4),
                        activity.getResources().getBoolean(R.bool.channel4_default));
    }

    public static int calculateBuildScore(int cpu, int gpu){
        return Math.round(1f / ((0.15f / cpu) + (0.85f / gpu)));
    }

    public static int getMinScore(Activity activity){
        return activity.getPreferences(0).
                getInt(activity.getString(R.string.score),
                        activity.getResources().getInteger(R.integer.score_default));
    }


}
