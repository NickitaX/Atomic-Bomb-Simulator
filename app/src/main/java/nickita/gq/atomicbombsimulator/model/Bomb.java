package nickita.gq.atomicbombsimulator.model;

/**
 * Created by Nickita on 1/3/17.
 */
public class Bomb {
    private int mPower;//Kilotons
    private int mImpactRadiusLevel1;
    private int mImpactRadiusLevel2;
    private int  mImpactRadiusLevel3;
    private final int BLAST_PER_KILOTON = 200;

    public Bomb(int power) {
        this.mPower = power;
        //https://en.wikipedia.org/wiki/Effects_of_nuclear_explosions
        this.mImpactRadiusLevel1 = ((Double) Math.sqrt(calculateRadius(power) / Math.PI)).intValue();
        this.mImpactRadiusLevel2 = mImpactRadiusLevel1*3;
        this.mImpactRadiusLevel3 = mImpactRadiusLevel1*8;
    }

    private int calculateRadius(int kilotons) {
        return kilotons * BLAST_PER_KILOTON;
    }

    public int getPower() {
        return mPower;
    }

    public void setPower(int power) {
        this.mPower = power;
    }

    public int getImpactRadiusLevel1() {
        return mImpactRadiusLevel1;
    }

    public int getImpactRadiusLevel2() {
        return mImpactRadiusLevel2;
    }

    public int getImpactRadiusLevel3() {
        return mImpactRadiusLevel3;
    }

}
