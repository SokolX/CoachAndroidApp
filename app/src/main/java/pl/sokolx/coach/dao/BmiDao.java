package pl.sokolx.coach.dao;

import android.app.Activity;

import pl.sokolx.coach.models.BmiModel;
import pl.sokolx.coach.models.UserModel;

public interface BmiDao {

    void addBmi(Activity activity, BmiModel bmiModel);

}
