package pl.sokolx.coach.models;

import java.util.HashMap;
import java.util.Map;

public class TrainingModel {
    String key;
    int stepCounter;
    int stepCounterEver;
    int step_id;
    long timeStart;
    long timeStop;
    String userUID;

    public TrainingModel() {
    }

    public TrainingModel(int step_id, int stepCounter, int stepCounterEver, long timeStart, long timeStop, String key, String userUID) {
        this.step_id = step_id;
        this.stepCounter = stepCounter;
        this.stepCounterEver = stepCounterEver;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.key = key;
        this.userUID = userUID;
    }

    public int getStep_id() {
        return this.step_id;
    }

    public int getStepCounter() {
        return this.stepCounter;
    }

    public int getStepCounterEver() {
        return this.stepCounterEver;
    }

    public long getTimeStart() {
        return this.timeStart;
    }

    public long getTimeStop() {
        return this.timeStop;
    }

    public String getKey() {
        return this.key;
    }

    public String getUserUID() {
        return this.userUID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap();
        result.put("step_id", Integer.valueOf(this.step_id));
        result.put("stepCounter", Integer.valueOf(this.stepCounter));
        result.put("stepCounterEver", Integer.valueOf(this.stepCounterEver));
        result.put("startTime", Long.valueOf(this.timeStart));
        result.put("stopTime", Long.valueOf(this.timeStop));
        result.put("key", this.key);
        result.put("userUID", this.userUID);
        return result;
    }
}
