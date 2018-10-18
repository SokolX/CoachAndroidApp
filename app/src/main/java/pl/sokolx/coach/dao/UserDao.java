package pl.sokolx.coach.dao;

import android.app.Activity;

import pl.sokolx.coach.models.UserModel;

public interface UserDao {

    void loginUser(Activity activity, String email, String password);
    void createAccount(Activity activity, UserModel userModel, String password);
    UserModel getUser();

}
