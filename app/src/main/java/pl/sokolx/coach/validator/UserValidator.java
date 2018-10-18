package pl.sokolx.coach.validator;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import pl.sokolx.coach.R;
import pl.sokolx.coach.exception.BmiIsNotSetException;
import pl.sokolx.coach.exception.UserDateOfBirthIsNotSetException;
import pl.sokolx.coach.exception.UserEmailIsEmptyException;
import pl.sokolx.coach.exception.UserNameIsEmptyException;
import pl.sokolx.coach.exception.UserPasswordIsEmptyException;
import pl.sokolx.coach.exception.UserPasswordShortLengthException;

public class UserValidator {

    public static final int MIN_LENGTH_PASSWORD = 8;
    public static final int MIN_LENGTH_LOGIN = 4;

    private static UserValidator instance = null;

    public UserValidator() {
    }

    public static UserValidator getInstance() {
        if (instance == null) {
            instance = new UserValidator();
        }

        return instance;
    }

    public boolean isValidate(Activity activity, String email, String password) throws UserPasswordIsEmptyException, UserEmailIsEmptyException {

        if(isEmailEmpty(email)) {
            throw new UserEmailIsEmptyException(activity.getResources().getString(R.string.please_enter_email));
        } else if(isPasswordEmpty(password)) {
            throw new UserPasswordIsEmptyException(activity.getResources().getString(R.string.enter_password));
        }
        return true;
    }

    public boolean isValidate(Activity activity, String email, String password, String name, String address, String dateOfBirth, String bmi) throws UserPasswordIsEmptyException, UserEmailIsEmptyException, UserPasswordShortLengthException, UserDateOfBirthIsNotSetException, BmiIsNotSetException, UserNameIsEmptyException {

        if(isEmailEmpty(email)) {
            throw new UserEmailIsEmptyException(activity.getResources().getString(R.string.please_enter_email));
        } else if(isPasswordEmpty(password)) {
            throw new UserPasswordIsEmptyException(activity.getResources().getString(R.string.enter_password));
        } else if(isPasswordLengthNoEnough(password)) {
            throw new UserPasswordShortLengthException(activity.getResources().getString(R.string.password_is_not_valid));
        } else if(isNameEmpty(name)) {
            throw new UserNameIsEmptyException(activity.getResources().getString(R.string.name_is_empty));
        } else if(isAddresEmpty(address)) {
            throw new UserNameIsEmptyException(activity.getResources().getString(R.string.address_is_empty));
        } else if (isDateOfBirthNotSet(dateOfBirth)) {
            throw new UserDateOfBirthIsNotSetException(activity.getResources().getString(R.string.date_of_birth_is_not_set));
        } else if(isBmiNotSet(bmi)) {
            throw new BmiIsNotSetException(activity.getResources().getString(R.string.bmi_is_not_set));
        }
        return true;
    }

    private boolean isPasswordEmpty(String password) {

        return TextUtils.isEmpty(password);
    }

    private boolean isPasswordLengthNoEnough(String password) {

        return password.length() < MIN_LENGTH_PASSWORD;

    }

    private boolean isNameEmpty(String name) {

        return TextUtils.isEmpty(name);

    }
    private boolean isAddresEmpty(String address) {

        return TextUtils.isEmpty(address);

    }
    private boolean isDateOfBirthNotSet(String dateOfBirth) {

        if(dateOfBirth == null) {
            return false;
        }

       if(String.valueOf(dateOfBirth.charAt(0)).matches("\\d")){
           return false;
       }
        return true;
    }

    private boolean isBmiNotSet(String bmi) {

        if(bmi.length() == 0) {
            return true;
        }

        return false;
    }

    private boolean isEmailEmpty(String email) {

        return TextUtils.isEmpty(email);
    }
}
