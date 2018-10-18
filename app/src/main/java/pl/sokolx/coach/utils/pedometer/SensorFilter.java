package pl.sokolx.coach.utils.pedometer;

public class SensorFilter {
    private SensorFilter() {
    }

    public static float sum(float[] array) {
        float retval = 0.0f;
        for (float f : array) {
            retval += f;
        }
        return retval;
    }

    public static float[] cross(float[] arrayA, float[] arrayB) {
        return new float[]{(arrayA[1] * arrayB[2]) - (arrayA[2] * arrayB[1]), (arrayA[2] * arrayB[0]) - (arrayA[0] * arrayB[2]), (arrayA[0] * arrayB[1]) - (arrayA[1] * arrayB[0])};
    }

    public static float norm(float[] array) {
        float retval = 0.0f;
        for (int i = 0; i < array.length; i++) {
            retval += array[i] * array[i];
        }
        return (float) Math.sqrt((double) retval);
    }

    public static float dot(float[] a, float[] b) {
        return ((a[0] * b[0]) + (a[1] * b[1])) + (a[2] * b[2]);
    }

    public static float[] normalize(float[] a) {
        float[] retval = new float[a.length];
        float norm = norm(a);
        for (int i = 0; i < a.length; i++) {
            retval[i] = a[i] / norm;
        }
        return retval;
    }
}
