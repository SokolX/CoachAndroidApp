package pl.sokolx.coach.utils.pedometer;

public class StepDetector {
    private static final int ACCEL_RING_SIZE = 50;
    private static final int STEP_DELAY_NS = 250000000;
    private static final float STEP_THRESHOLD = 50.0f;
    private static final int VEL_RING_SIZE = 10;
    private int accelRingCounter = 0;
    private float[] accelRingX = new float[50];
    private float[] accelRingY = new float[50];
    private float[] accelRingZ = new float[50];
    private long lastStepTimeNs = 0;
    private StepListener listener;
    private float oldVelocityEstimate = 0.0f;
    private float[] velRing = new float[10];
    private int velRingCounter = 0;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    public void updateAccel(long timeNs, float x, float y, float z) {
        float[] currentAccel = new float[]{x, y, z};
        this.accelRingCounter++;
        this.accelRingX[this.accelRingCounter % 50] = currentAccel[0];
        this.accelRingY[this.accelRingCounter % 50] = currentAccel[1];
        this.accelRingZ[this.accelRingCounter % 50] = currentAccel[2];
        float[] worldZ = new float[]{SensorFilter.sum(this.accelRingX) / ((float) Math.min(this.accelRingCounter, 50)), SensorFilter.sum(this.accelRingY) / ((float) Math.min(this.accelRingCounter, 50)), SensorFilter.sum(this.accelRingZ) / ((float) Math.min(this.accelRingCounter, 50))};
        float normalization_factor = SensorFilter.norm(worldZ);
        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;
        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        this.velRingCounter++;
        this.velRing[this.velRingCounter % 10] = currentZ;
        float velocityEstimate = SensorFilter.sum(this.velRing);
        if (velocityEstimate > STEP_THRESHOLD && this.oldVelocityEstimate <= STEP_THRESHOLD && timeNs - this.lastStepTimeNs > 250000000) {
            this.listener.step(timeNs);
            this.lastStepTimeNs = timeNs;
        }
        this.oldVelocityEstimate = velocityEstimate;
    }
}
