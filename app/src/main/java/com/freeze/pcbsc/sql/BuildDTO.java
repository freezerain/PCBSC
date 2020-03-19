package com.freeze.pcbsc.sql;

import lombok.Data;

@Data
public class BuildDTO {
    //Build fields
    private int ID;
    private int price;
    private int score;
    private int cpuPrice;
    private int gpuPrice;
    private int ramPrice;
    private int wattage;
    private int cpuWattage;
    private int gpuWattage;
    private int level;
    private boolean percentThrough;
    private String CPU;
    private String GPU;
    private String RAM;
    private boolean isDualGPU;
    private int ramChannels;

    public BuildDTO() {
    }

}
