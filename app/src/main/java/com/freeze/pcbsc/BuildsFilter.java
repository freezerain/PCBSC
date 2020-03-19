package com.freeze.pcbsc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//Class finished
public class BuildsFilter extends Fragment {
    private EditText levelValue, scoreValue, priceValue, wattageValue;
    private Button resetButton, searchButton;
    private Switch isDualSwitch, percentThroughSwitch, isWatercooledSwitch;
    private CheckBox channel1, channel2, channel3, channel4;
    private Resources r;

    searchBuildsButton callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.builds_filter, container, false);
        r = getActivity().getResources();
        setViews(v);
        setButtonEvents();
        setValues();
        return v;
    }

    private void setButtonEvents(){
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultValues();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
                callback.searchButtonPressed();
            }
        });

    }

    public interface searchBuildsButton{
        void searchButtonPressed();
    }

    public void setOnSearchBuildsButton(searchBuildsButton callback) {
        this.callback = callback;
    }

    private void savePreferences(){
        SharedPreferences sp = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sp.edit();
        boolean resetNeeded = false;
        if(!TextUtils.isEmpty(levelValue.getText())) editor.putInt(getString(R.string.level), Integer.parseInt(levelValue.getText().toString()));
        else resetNeeded = true;
        if(!TextUtils.isEmpty(priceValue.getText())) editor.putInt(getString(R.string.price), Integer.parseInt(priceValue.getText().toString()));
        else resetNeeded = true;
        if(!TextUtils.isEmpty(wattageValue.getText())) editor.putInt(getString(R.string.wattage), Integer.parseInt(wattageValue.getText().toString()));
        else resetNeeded = true;
        if(!TextUtils.isEmpty(scoreValue.getText())) editor.putInt(getString(R.string.score), Integer.parseInt(scoreValue.getText().toString()));
        else resetNeeded = true;
        editor.putBoolean(getString(R.string.isDualGPU), isDualSwitch.isChecked());
        editor.putBoolean(getString(R.string.percentThrough), percentThroughSwitch.isChecked());
        editor.putBoolean(getString(R.string.isWaterCooled), isWatercooledSwitch.isChecked());
        editor.putBoolean(getString(R.string.channel1), channel1.isChecked());
        editor.putBoolean(getString(R.string.channel2), channel2.isChecked());
        editor.putBoolean(getString(R.string.channel3), channel3.isChecked());
        editor.putBoolean(getString(R.string.channel4), channel4.isChecked());
        editor.commit();
        if(resetNeeded)setValues();
    }

    private void setViews(View v){
        levelValue = v.findViewById(R.id.levelValue);
        scoreValue = v.findViewById(R.id.scoreValue);
        priceValue = v.findViewById(R.id.priceValue);
        wattageValue = v.findViewById(R.id.wattageValue);

        resetButton = v.findViewById(R.id.reset_button);
        searchButton = v.findViewById(R.id.search_button);

        isDualSwitch = v.findViewById(R.id.dualSwitch);
        percentThroughSwitch = v.findViewById(R.id.percentThroughSwitch);
        isWatercooledSwitch = v.findViewById(R.id.watercooled);

        channel1 = v.findViewById(R.id.checkBox1);
        channel2 = v.findViewById(R.id.checkBox2);
        channel3 = v.findViewById(R.id.checkBox3);
        channel4 = v.findViewById(R.id.checkBox4);
    }

    private void setValues(){
        SharedPreferences sp = getActivity().getPreferences(0);
        levelValue.setText(String.valueOf(sp.getInt(getActivity().getString(R.string.level), r.getInteger(R.integer.level_default))));
        scoreValue.setText(String.valueOf(sp.getInt(getActivity().getString(R.string.score), r.getInteger(R.integer.score_default))));
        priceValue.setText(String.valueOf(sp.getInt(getActivity().getString(R.string.price), r.getInteger(R.integer.price_default))));
        wattageValue.setText(String.valueOf(sp.getInt(getActivity().getString(R.string.wattage), r.getInteger(R.integer.wattage_default))));

        isDualSwitch.setChecked(sp.getBoolean(getActivity().getString(R.string.isDualGPU), r.getBoolean(R.bool.isDualGPU_default)));
        percentThroughSwitch.setChecked(sp.getBoolean(getActivity().getString(R.string.percentThrough), r.getBoolean(R.bool.percentThrough_default)));
        isWatercooledSwitch.setChecked(sp.getBoolean(getActivity().getString(R.string.isWaterCooled), r.getBoolean(R.bool.isWatercooled_default)));

        channel1.setChecked(sp.getBoolean(getActivity().getString(R.string.channel1), r.getBoolean(R.bool.channel1_default)));
        channel2.setChecked(sp.getBoolean(getActivity().getString(R.string.channel2), r.getBoolean(R.bool.channel2_default)));
        channel3.setChecked(sp.getBoolean(getActivity().getString(R.string.channel3), r.getBoolean(R.bool.channel3_default)));
        channel4.setChecked(sp.getBoolean(getActivity().getString(R.string.channel4), r.getBoolean(R.bool.channel4_default)));
    }

    //Set filter values to default on "RESET" button pressed
    private void setDefaultValues(){
        SharedPreferences sp = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(getString(R.string.level), r.getInteger(R.integer.level_default));
        editor.putInt(getString(R.string.price), r.getInteger(R.integer.price_default));
        editor.putInt(getString(R.string.wattage), r.getInteger(R.integer.wattage_default));
        editor.putInt(getString(R.string.score), r.getInteger(R.integer.score_default));
        editor.putBoolean(getString(R.string.isDualGPU), r.getBoolean(R.bool.isDualGPU_default));
        editor.putBoolean(getString(R.string.percentThrough), r.getBoolean(R.bool.percentThrough_default));
        editor.putBoolean(getString(R.string.isWaterCooled), r.getBoolean(R.bool.isWatercooled_default));
        editor.putBoolean(getString(R.string.channel1), r.getBoolean(R.bool.channel1_default));
        editor.putBoolean(getString(R.string.channel2), r.getBoolean(R.bool.channel2_default));
        editor.putBoolean(getString(R.string.channel3), r.getBoolean(R.bool.channel3_default));
        editor.putBoolean(getString(R.string.channel4), r.getBoolean(R.bool.channel4_default));
        editor.commit();
        setValues();
    }
}
