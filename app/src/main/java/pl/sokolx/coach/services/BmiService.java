package pl.sokolx.coach.services;

public class BmiService {

    public static double setBmi(int height, int weight) {

        Double currentBmi = weight/ ((double) height * height / 10000); //waga / wzrost * wzrost [6 / 1.65 * 1.65]
        return currentBmi;

    }
}
