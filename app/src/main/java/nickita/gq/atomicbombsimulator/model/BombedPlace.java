package nickita.gq.atomicbombsimulator.model;

/**
 * Created by Nickita on 1/3/17.
 */
public class BombedPlace {
    private String mPower;
    private double mLatitude;
    private double mLongitude;
    private String mBombedByCountry;

    public BombedPlace() {

    }

    public BombedPlace(String mPower, double mLatitude, double mLongitude, String mBombedByCountry) {
        this.mPower = mPower;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mBombedByCountry = mBombedByCountry;
    }

    public String getmPower() {
        return mPower;
    }

    public void setmPower(String mPower) {
        this.mPower = mPower;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmBombedByCountry() {
        return mBombedByCountry;
    }

    public void setmBombedByCountry(String mBombedByCountry) {
        this.mBombedByCountry = mBombedByCountry;
    }
}
