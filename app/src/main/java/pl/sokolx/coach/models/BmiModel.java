package pl.sokolx.coach.models;

import java.util.HashMap;
import java.util.Map;

public class BmiModel {
    private double bmi;
    private Long dateBmiMeasure;
    private String key;
    private int measure_id;
    private String userUID;
    private int weight;

    public BmiModel() {
    }

    public BmiModel(int measure_id, Long dateBmiMeasure, double bmi, int weight, String userUID, String key) {
        this.measure_id = measure_id;
        this.dateBmiMeasure = dateBmiMeasure;
        this.weight = weight;
        this.bmi = bmi;
        this.userUID = userUID;
        this.key = key;
    }

    public Long getDateBmiMeasure() {
        return this.dateBmiMeasure;
    }

    public void setDateBmiMeasure(Long dateBmiMeasure) {
        this.dateBmiMeasure = dateBmiMeasure;
    }

    public int getMeasure_id() {
        return this.measure_id;
    }

    public void setMeasure_id(int measure_id) {
        this.measure_id = measure_id;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return this.bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getUserUID() {
        return this.userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap();
        result.put("measure_id", Integer.valueOf(this.measure_id));
        result.put("dateBmiMeasure", this.dateBmiMeasure);
        result.put("bmi", Double.valueOf(this.bmi));
        result.put("weight", Integer.valueOf(this.weight));
        result.put("userUID", this.userUID);
        result.put("key", this.key);
        return result;
    }
}
