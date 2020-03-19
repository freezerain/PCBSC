package com.freeze.pcbsc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.freeze.pcbsc.sql.BuildDTO;
import com.freeze.pcbsc.sql.DBHelper;
import com.freeze.pcbsc.sql.FilterLib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BuildTableFragment extends Fragment {
    private  TableLayout table;
    private ArrayList<Integer> gravityRigthList = new ArrayList<>(Arrays.asList(3));

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.build_table_view, container, false);
        table = v.findViewById(R.id.table);
        ArrayList<String> headerList = new ArrayList<>(Arrays.asList(
                "№", "Price", "Score", "Level", "GPU", "CPU", "RAM"));
        buildHeader(table, headerList);
        return v;
    }

    public void loadData() {
        DBHelper dbHelper = new DBHelper(getContext());
        dbHelper.generateBuildsTable();
        buildAllRows(dbHelper.getBuilds(getActivity()));
    }

    public void buildAllRows(ArrayList<BuildDTO> buildList){
        int i = 0;
        for(BuildDTO build : buildList){
            TableRow tr = buildTableRow(i);
            tr.addView(buildTableCell(0 ,String.valueOf(build.getID())));
            tr.addView(buildTableCell(1 , new ArrayList<>(Arrays.asList(
                    "Total: " + String.valueOf(build.getPrice() + "$"),
                    "Wattage: " + String.valueOf(build.getWattage())
            ))));
            tr.addView(buildTableCell(2 , new ArrayList<>(Arrays.asList(
                    "Score: " + String.valueOf(build.getScore()),
                    "GPU mode: " + (build.isDualGPU()? "Dual" : "Solo"),
                    "Ram №: " + String.valueOf(build.getRamChannels())
            ))));
            tr.addView(buildTableCell(3 , new ArrayList<>(Arrays.asList(
                    "Level: " + String.valueOf(build.getLevel()),
                    "Percent: " + (build.isPercentThrough()? "Yes" : "No")
            ))));

            tr.addView(buildTableCell(4 , new ArrayList<>(Arrays.asList(
                    "CPU: " + build.getCPU(),
                    "Price: " + String.valueOf(build.getWattage() + "$"),
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

            table.addView(tr);

            TableRow trSep = new TableRow(getContext());
            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParamsSep.setMargins(0, 0, 0, 0);
            trSep.setLayoutParams(trParamsSep);
            TextView tvSep = new TextView(getContext());
            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            //Amount of Columns
            tvSepLay.span = 7;
            tvSep.setLayoutParams(tvSepLay);
            tvSep.setBackgroundColor(getResources().getColor(R.color.divider));
            tvSep.setHeight(1);
            trSep.addView(tvSep);
            table.addView(trSep, trParamsSep);
            i++;
        }
    }

    private TextView buildTableCell(int column, String text){
        TextView tv = buildTableRowText(text, true,
                !gravityRigthList.contains(column), false);
        if (column%2==0)
            tv.setBackgroundColor(getResources().getColor(R.color.table_element_dark));
        else tv.setBackgroundColor(getResources().getColor(R.color.table_element));
        return tv;
    }

    private LinearLayout buildTableCell(int column, ArrayList<String> list){
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.setPadding(0, 10, 0, 10);
        if (column%2==0)
            layout.setBackgroundColor(getResources().getColor(R.color.table_element_dark));
        else layout.setBackgroundColor(getResources().getColor(R.color.table_element));
        for (int i = 0; i<list.size(); i++){
            if(i == 0) layout.addView(buildTableRowText(list.get(i), true,
                    !gravityRigthList.contains(column), true));
            else layout.addView(buildTableRowText(list.get(i), false,
                    !gravityRigthList.contains(column), true));
        }
        return layout;
    }

    private TableRow buildTableRow(int id){
        TableRow tr = new TableRow(getContext());
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
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setLayoutParams(new TableRow.LayoutParams(
                wrapConent? TableRow.LayoutParams.WRAP_CONTENT : TableRow.LayoutParams.MATCH_PARENT,
                wrapConent? TableRow.LayoutParams.WRAP_CONTENT : TableRow.LayoutParams.MATCH_PARENT));
        tv.setGravity(isGravityLeft ? Gravity.LEFT : Gravity.RIGHT);
        if(isPrimaryText)tv.setPadding((int) getResources().getDimension(R.dimen.table_padding_left),
                (int) getResources().getDimension(R.dimen.table_padding_top),
                (int) getResources().getDimension(R.dimen.table_padding_right),0);
        else tv.setPadding((int)getResources().getDimension(R.dimen.table_padding_left),
                0,
                (int) getResources().getDimension(R.dimen.table_padding_right),
                0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX , getResources().getDimension(isPrimaryText? R.dimen.table_text_size : R.dimen.table_secondary_text_size));
        tv.setTextColor(getResources().getColor(isPrimaryText? R.color.primary_text : R.color.secondary_text));
        return  tv;
    }

    private void buildHeader(TableLayout table, ArrayList<String> headerList) {
        Context context = getContext();
        TableRow row = new TableRow(context);
        int i =0;
        for (String key : headerList) {
            TextView tv = new TextView(context);
            tv.setText(key);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding((int) getResources().getDimension(R.dimen.table_padding_left),
                    (int) getResources().getDimension(R.dimen.table_padding_top),
                    (int) getResources().getDimension(R.dimen.table_padding_right),
                    (int) getResources().getDimension(R.dimen.table_padding_bottom));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.table_text_size));
            if (i % 2 == 0)
                tv.setBackgroundColor(getResources().getColor(R.color.table_header_dark));
            else tv.setBackgroundColor(getResources().getColor(R.color.table_header));
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
