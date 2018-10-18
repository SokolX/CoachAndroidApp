package pl.sokolx.coach.services;

import pl.sokolx.coach.dao.UserDao;
import pl.sokolx.coach.dao.UserDaoImpl;
import pl.sokolx.coach.models.UserModel;
import pl.sokolx.coach.validator.UserValidator;

public class UserService {

    private UserValidator userValidator = new UserValidator().getInstance();

    private UserDao userDao = new UserDaoImpl();

    public UserService() {}

}
