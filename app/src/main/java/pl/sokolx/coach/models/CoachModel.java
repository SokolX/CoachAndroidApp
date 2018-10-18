package pl.sokolx.coach.models;

import java.util.HashMap;
import java.util.Map;

public class CoachModel {
    int coachDateOfBirth;
    String coachDesc;
    String coachEmail;
    int coachHeight;
    int coachId;
    String coachName;
    int coachWeight;
    String key;

    public CoachModel() {
    }

    public CoachModel(int coachId, int coachWeight, int coachHeight, int coachDateOfBirth, String coachName, String coachEmail, String coachDesc, String key) {
        this.coachId = coachId;
        this.coachWeight = coachWeight;
        this.coachHeight = coachHeight;
        this.coachDateOfBirth = coachDateOfBirth;
        this.coachName = coachName;
        this.coachEmail = coachEmail;
        this.coachDesc = coachDesc;
        this.key = key;
    }

    public int getCoachId() {
        return this.coachId;
    }

    public int getCoachDateOfBirth() {
        return this.coachDateOfBirth;
    }

    public String getCoachName() {
        return this.coachName;
    }

    public String getCoachEmail() {
        return this.coachEmail;
    }

    public String getCoachDesc() {
        return this.coachDesc;
    }

    public String getKey() {
        return this.key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap();
        result.put("coachId", Integer.valueOf(this.coachId));
        result.put("coachWeight", Integer.valueOf(this.coachWeight));
        result.put("coachHeight", Integer.valueOf(this.coachHeight));
        result.put("coachDateOfBirth", Integer.valueOf(this.coachDateOfBirth));
        result.put("coachName", this.coachName);
        result.put("coachEmail", this.coachEmail);
        result.put("coachDesc", this.coachDesc);
        result.put("key", this.key);
        return result;
    }
}
