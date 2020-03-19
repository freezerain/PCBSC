package com.freeze.pcbsc.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.freeze.pcbsc.MainActivity;
import com.freeze.pcbsc.R;
import com.freeze.pcbsc.sql.BuildDTO;
import java.util.ArrayList;
import java.util.Arrays;
/* This task building visual table based on BuildDTO array
 */

public class BuildTableAsyncTask extends AsyncTask<ArrayList<BuildDTO>, TableRow, Void> {

    private MainActivity activity;
    private TableLayout table;
    private ArrayList<Integer> gravityRigthList = new ArrayList<>(Arrays.asList(3));

    public BuildTableAsyncTask(MainActivity activity, TableLayout table) {
        this.activity = activity; this.table = table;
    }

    @Override
    protected void onPreExecute() {
        table.removeAllViews();
        buildHeader(table, new ArrayList<String>(Arrays.asList(
                "№", "Price", "Score", "Level", "GPU", "CPU", "RAM")));
    }

    @Override
    protected Void doInBackground(ArrayList<BuildDTO>... lists) {
        ArrayList<BuildDTO> buildList = lists[0];
        if(buildList == null) {
            Log.d("BuildTableAsyncTask", "Build list is null, returning null");
            return null;
        } else if(buildList.isEmpty()) {
            Log.d("BuildTableAsyncTask", "Empty Build list, returning null");
            return null;
        }
        int i = 0;
        for(BuildDTO build : buildList){
            TableRow tr = buildTableRow(i);
            composeRow(build, tr);
            publishProgress(tr);
            i++;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(TableRow... rows) {
        table.addView(rows[0]);
        TableRow trSep = new TableRow(activity.getApplicationContext());
        TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParamsSep.setMargins(0, 0, 0, 0);
        trSep.setLayoutParams(trParamsSep);
        TextView tvSep = new TextView(activity.getApplicationContext());
        TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        //Amount of Columns
        tvSepLay.span = 7;
        tvSep.setLayoutParams(tvSepLay);
        tvSep.setBackgroundColor(activity.getResources().getColor(R.color.divider));
        tvSep.setHeight(1);
        trSep.addView(tvSep);
        table.addView(trSep);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(table.getChildCount()<2) Toast.makeText(activity.getApplicationContext(), "No result found", Toast.LENGTH_LONG).show();
        else Toast.makeText(activity.getApplicationContext(), ((table.getChildCount()-1)/2) + " results found", Toast.LENGTH_LONG).show();
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void composeRow(BuildDTO build, TableRow tr){
        tr.addView(buildTableCell(0 ,String.valueOf(tr.getId())));
        tr.addView(buildTableCell(1 , new ArrayList<>(Arrays.asList(
                "Total: " + String.valueOf(build.getPrice() + "$"),
                "Wattage: " + String.valueOf(build.getWattage())
        ))));
        tr.addView(buildTableCell(2 , new ArrayList<>(Arrays.asList(
                "Score: " + String.valueOf(build.getScore()),
                "GPU mode: " + (build.isDualGPU()? "Dual" : "Solo"),
                "Ram slots: " + String.valueOf(build.getRamChannels())
        ))));
        tr.addView(buildTableCell(3 , new ArrayList<>(Arrays.asList(
                "Level: " + String.valueOf(build.getLevel()),
                "Percent: " + (build.isPercentThrough()? "Yes" : "No")
        ))));

        tr.addView(buildTableCell(4 , new ArrayList<>(Arrays.asList(
                "CPU: " + build.getCPU(),
                "Price: " + String.valueOf(build.getCpuPrice() + "$"),
                "Wattage: " + String.valueOf(build.getCpuWattage()) + "w"
        ))));

        tr.addView(buildTableCell(5 , new ArrayList<>(Arrays.asList(
                "GPU: " + (build.isDualGPU()? "2x ":"") + build.getGPU(),
                "Price: " + (build.isDualGPU()? "2x ":"") + build.getGpuPrice() + "$",
                "Wattage: " + (build.isDualGPU()? "2x ":"") + String.valueOf(build.getGpuWattage()) + "w"
        ))));

        tr.addView(buildTableCell(6 , new ArrayList<>(Arrays.asList(
                "RAM: " + (build.getRamChannels()>1? build.getRamChannels() + "x " : "") + build.getRAM(),
                "Price: " + (build.getRamChannels()>1? build.getRamChannels() + "x " : "") + build.getRamPrice() + "$"
        ))));
    }

    private TextView buildTableCell(int column, String text){
        TextView tv = buildTableRowText(text, true,
                !gravityRigthList.contains(column), false);
        if (column%2==0)
            tv.setBackgroundColor(activity.getResources().getColor(R.color.table_element_dark));
        else tv.setBackgroundColor(activity.getResources().getColor(R.color.table_element));
        return tv;
    }

    private LinearLayout buildTableCell(int column, ArrayList<String> list){
        LinearLayout layout = new LinearLayout(activity.getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.setPadding(0, 10, 0, 10);
        if (column%2==0)
            layout.setBackgroundColor(activity.getResources().getColor(R.color.table_element_dark));
        else layout.setBackgroundColor(activity.getResources().getColor(R.color.table_element));

        for (int i = 0; i<list.size(); i++){
            if(i == 0) layout.addView(buildTableRowText(list.get(i), true,
                    !gravityRigthList.contains(column), true));
            else layout.addView(buildTableRowText(list.get(i), false,
                    !gravityRigthList.contains(column), true));
        }
        return layout;
    }


    private TableRow buildTableRow(int id){
        TableRow tr = new TableRow(activity.getApplicationContext());
        tr.setId(id);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(0, 0, 0, 0);
        tr.setPadding(0, 0, 0, 0);
        tr.setLayoutParams(trParams);
        //Set on click event
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TableRow tr = (TableRow) v;
                //do whatever action is needed
            }
        });
        return tr;
    }

    private TextView buildTableRowText(String text, boolean isPrimaryText,
                                       boolean isGravityLeft, boolean wrapConent){
        TextView tv = new TextView(activity.getApplicationContext());
        tv.setText(text);
        tv.setLayoutParams(new TableRow.LayoutParams(
                wrapConent? TableRow.LayoutParams.WRAP_CONTENT : TableRow.LayoutParams.MATCH_PARENT,
                wrapConent? TableRow.LayoutParams.WRAP_CONTENT : TableRow.LayoutParams.MATCH_PARENT));
        tv.setGravity(isGravityLeft ? Gravity.LEFT : Gravity.RIGHT);
        if(isPrimaryText)tv.setPadding((int) activity.getResources().getDimension(R.dimen.table_padding_left),
                (int) activity.getResources().getDimension(R.dimen.table_padding_top),
                (int) activity.getResources().getDimension(R.dimen.table_padding_right),0);
        else tv.setPadding((int)activity.getResources().getDimension(R.dimen.table_padding_left),
                0,
                (int) activity.getResources().getDimension(R.dimen.table_padding_right),
                0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX , activity.getResources().getDimension(isPrimaryText? R.dimen.table_text_size : R.dimen.table_secondary_text_size));
        tv.setTextColor(activity.getResources().getColor(isPrimaryText? R.color.primary_text : R.color.secondary_text));
        return  tv;
    }


    private void buildHeader(TableLayout table, ArrayList<String> headerList) {
        TableRow row = new TableRow(activity.getApplicationContext());
        int i =0;
        for (String key : headerList) {
            TextView tv = new TextView(activity.getApplicationContext());
            tv.setText(key);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding((int) activity.getResources().getDimension(R.dimen.table_padding_left),
                    (int) activity.getResources().getDimension(R.dimen.table_padding_top),
                    (int) activity.getResources().getDimension(R.dimen.table_padding_right),
                    (int) activity.getResources().getDimension(R.dimen.table_padding_bottom));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.table_text_size));
            if (i % 2 == 0)
                tv.setBackgroundColor(activity.getResources().getColor(R.color.table_header_dark));
            else tv.setBackgroundColor(activity.getResources().getColor(R.color.table_header));
            row.addView(tv);
            i++;
        }
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(0, 0, 0, 0);
        row.setPadding(0, 0, 0, 0);
        row.setLayoutParams(trParams);
        table.addView(row);
    }
}
