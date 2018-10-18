package pl.sokolx.coach.models;

public class UserModel {
    private String address;
    private long dateOfBirth;
    private String email;
    private String genderType;
    private int height;
    private Double measureBMI;
    private String name;
    private int numberOfBmi;
    private int numberOfWorkout;
    private int userStepsCounterEver;
    private String userType;
    private int weight;

    public UserModel() {
    }

    public UserModel(String email, String name, String address, int height, int weight, long dateOfBirth, Double measureBMI, String genderType, String userType, int numberOfWorkout, int numberOfBmi, int userStepsCounterEver) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.measureBMI = measureBMI;
        this.genderType = genderType;
        this.userType = userType;
        this.numberOfWorkout = numberOfWorkout;
        this.numberOfBmi = numberOfBmi;
        this.userStepsCounterEver = userStepsCounterEver;
    }

    public UserModel(int numberOfWorkout, int userStepsCounterEver) {
        this.numberOfWorkout = numberOfWorkout;
        this.userStepsCounterEver = userStepsCounterEver;
    }

    public int getNumberOfBmi() {
        return this.numberOfBmi;
    }

    public void setNumberOfBmi(int numberOfBmi) {
        this.numberOfBmi = numberOfBmi;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Double getMeasureBMI() {
        return this.measureBMI;
    }

    public void setMeasureBMI(Double measureBMI) {
        this.measureBMI = measureBMI;
    }

    public String getGenderType() {
        return this.genderType;
    }

    public void setGenderType(String sexType) {
        this.genderType = sexType;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getNumberOfWorkout() {
        return this.numberOfWorkout;
    }

    public void setNumberOfWorkout(int numberOfWorkout) {
        this.numberOfWorkout = numberOfWorkout;
    }

    public int getUserStepsCounterEver() {
        return this.userStepsCounterEver;
    }

    public void setUserStepsCounterEver(int userStepsCounterEver) {
        this.userStepsCounterEver = userStepsCounterEver;
    }
}
