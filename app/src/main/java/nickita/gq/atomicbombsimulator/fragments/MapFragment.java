package nickita.gq.atomicbombsimulator.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.victor.loading.newton.NewtonCradleLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

import nickita.gq.atomicbombsimulator.R;
import nickita.gq.atomicbombsimulator.model.Bomb;
import nickita.gq.atomicbombsimulator.model.BombedPlace;
import nickita.gq.atomicbombsimulator.model.EventMessage;
import nickita.gq.atomicbombsimulator.utils.Navigation;
import nickita.gq.atomicbombsimulator.utils.Notifications;
import nickita.gq.atomicbombsimulator.utils.Values;

/**
 * Created by Nickita on 1/3/17.
 */
public class MapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private View mView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private Circle mShapeLevel1;
    private Circle mShapeLevel2;
    private Circle mShapeLevel3;
    private Polygon mLine;
    private GoogleApiClient mGoogleApiClient;
    private boolean mLocationReadyFlag = false;
    private Location mLastLocation;
    private LinearLayout mLoadingScreen;
    private TextView mLoadingCountdown;
    private RelativeLayout mRootLayout;
    private DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NewtonCradleLoading mLoading;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage event) {
        switch (event.getType()) {
            case Values.ASK_GPS: {
                askGps();
                break;
            }
            case Values.DROP_BOMB: {
                Bomb b = (Bomb) event.getValue();
                handleBombDropped(b);
                break;
            }
            case Values.DROP_ANOTHER: {
                activateFab(true);
                clearOldImpact();
                break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initialize(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        prepareMap();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        return mView;
    }

    private void handleBombDropped(Bomb b) {
        clearOldImpact();
        if (mMarker != null) {
            if (mLastLocation != null) {
                showLoadingBombScreen(b);
            } else {
                Notifications.showSnackbar(mRootLayout, Values.WARNING_SNACKBAR_CANNOT_DEFINE_LOCATION, Snackbar.LENGTH_LONG);
                updateLocation();
            }
        } else {
            Notifications.showSnackbar(mRootLayout, Values.WARNING_SNACKBAR_SHOULD_DROP_PIN_FIRST, Snackbar.LENGTH_LONG);
        }
    }

    private void clearOldImpact() {
        if (mMarker != null) {
            mMarker.remove();
        }
        if (mShapeLevel1 != null) {
            mShapeLevel1.remove();
        }
        if (mShapeLevel2 != null) {
            mShapeLevel2.remove();
        }
        if (mShapeLevel3 != null) {
            mShapeLevel3.remove();
        }
        if (mLine != null) {
            mLine.remove();
        }
    }

    private void activateFab(boolean status) {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.launch_bomb_fab);
        if (status) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void showLoadingBombScreen(final Bomb b) {
        mLoading.setVisibility(View.VISIBLE);
        mLoading.start();
        mLoadingScreen.setVisibility(View.VISIBLE);
        activateFab(false);
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long milliseconds) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(mLoadingCountdown);
                mLoadingCountdown.setText(String.valueOf(milliseconds / 1000));
            }

            @Override
            public void onFinish() {
                mLoadingScreen.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showImpact(b, mMarker.getPosition());
                        mLoadingScreen.setVisibility(View.GONE);
                        mLoadingScreen.setBackgroundColor(getResources().getColor(R.color.colorTintLoading));
                        EventBus.getDefault().post(new EventMessage<Bomb>(null, Values.SHOW_RESULT));
                        mLoading.setVisibility(View.GONE);
                        mLoading.stop();
                    }
                }, 1500);
            }
        }.start();
    }

    private void setUpLoadingScreen() {
        mLoadingScreen = (LinearLayout) mView.findViewById(R.id.load_map_screen);
        mLoadingCountdown = (TextView) mView.findViewById(R.id.countdown_textview);
    }

    private void initialize(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment_layout, container, false);
        mLoading = (NewtonCradleLoading) getActivity().findViewById(R.id.newton_cradle_loading);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRootLayout = (RelativeLayout) mView.findViewById(R.id.main_layout_fragment);
        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mView.getContext());
    }

    private void prepareMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                mGoogleMap = mMap;
                if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    setUpGMaps();
                } else {
                    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
            }
        });
    }

    private void setUpGMaps() {
        if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        askGps();
        setUpLongTapListener();
        setUpLocationListener();
        setUpLoadingScreen();
        updateLocation();
        loadBombedPlacesListener();
    }

    public void askGps() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(getClass().toString(), Values.ERROR_TITLE);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void setUpLocationListener() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mView.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void uploadBombingToFirebase(Bomb b, LatLng destLatLang) {
        LatLng myLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        String country = Navigation.getAddress(mView.getContext(), myLatLng.latitude, myLatLng.longitude);
        double longitude = destLatLang.longitude;
        double latitude = destLatLang.latitude;
        String power = String.valueOf(b.getPower());
        BombedPlace bombedPlace = new BombedPlace(power, latitude, longitude, country);
        mDatabase.child(Values.DB_BOMED_PLACES_LOC).child(UUID.randomUUID().toString()).setValue(bombedPlace);
    }

    private void loadBombedPlacesListener() {
        mLoading.setVisibility(View.VISIBLE);
        mLoading.start();
        mDatabase.child(Values.DB_BOMED_PLACES_LOC).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    BombedPlace bp = new BombedPlace(Values.ERROR_TITLE, 0, 0, Values.ERROR_TITLE);
                    try {
                        bp = snap.getValue(BombedPlace.class);
                    } catch (Exception e) {
                        //Log error
                        Bundle params = new Bundle();
                        params.putString(Values.ANALYTICS_CRITICAL_PARSE_OBJECT_ERROR, "true");
                        mFirebaseAnalytics.logEvent(Values.ANALYTICS_CRITICAL_PARSE_OBJECT_ERROR, params);
                        //
                    }
                    LatLng latLng = new LatLng(bp.getmLatitude(), bp.getmLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(Values.MARKER_THIS_PLACE_WAS_BOMBED_BY + bp.getmBombedByCountry()));
                }
                mLoading.setVisibility(View.GONE);
                mLoading.stop();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showImpact(Bomb bomb, LatLng latLng) {
        mLoading.setVisibility(View.VISIBLE);
        mLoading.start();
        clearOldImpact();
        uploadBombingToFirebase(bomb, latLng);
        CircleOptions optionsLevel1 = new CircleOptions()
                .strokeWidth(3)
                .center(latLng)
                .fillColor(R.color.colorImpact1)
                .strokeColor(R.color.colorPrimary)
                .radius(bomb.getImpactRadiusLevel1());
        mShapeLevel1 = mGoogleMap.addCircle(optionsLevel1);
        CircleOptions optionsLevel2 = new CircleOptions()
                .strokeWidth(3)
                .center(latLng)
                .fillColor(R.color.colorImpact2)
                .strokeColor(R.color.colorPrimary)
                .radius(bomb.getImpactRadiusLevel2());
        mShapeLevel2 = mGoogleMap.addCircle(optionsLevel2);
        CircleOptions optionsLevel3 = new CircleOptions()
                .strokeWidth(3)
                .center(latLng)
                .fillColor(R.color.colorImpact3)
                .strokeColor(R.color.colorPrimary)
                .radius(bomb.getImpactRadiusLevel3());
        mShapeLevel3 = mGoogleMap.addCircle(optionsLevel3);
        if (mLocationReadyFlag) {
            checkImpact();
            if (mMarker != null) {
                navigateSmoothOnMap(mMarker.getPosition());
            }
        } else {
            Toast.makeText(mView.getContext(), Values.WARNING_SNACKBAR_CANNOT_GET_LOCATION_NOW, Toast.LENGTH_LONG).show();
        }
        mLoading.setVisibility(View.GONE);
        mLoading.stop();
    }

    private void navigateSmoothOnMap(LatLng target) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(target).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void drawLine() {
        LatLng currentPositionLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        PolygonOptions options = new PolygonOptions()
                .add(currentPositionLatLng)
                .add(mMarker.getPosition())
                .strokeWidth(3)
                .strokeJointType(2) //Round
                .strokeColor(R.color.colorLine);
        mLine = mGoogleMap.addPolygon(options);
    }

    private void checkImpact() {
        float[] distanceLevel1 = new float[2];
        float[] distanceLevel2 = new float[2];
        float[] distanceLevel3 = new float[2];
        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                mShapeLevel1.getCenter().latitude, mShapeLevel1.getCenter().longitude, distanceLevel1);
        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                mShapeLevel2.getCenter().latitude, mShapeLevel2.getCenter().longitude, distanceLevel2);
        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                mShapeLevel3.getCenter().latitude, mShapeLevel3.getCenter().longitude, distanceLevel3);
        if (distanceLevel1[0] < mShapeLevel1.getRadius()) {
            Notifications.showSnackbar(mRootLayout, Values.RESULT_INSIDE_LEVEL_1, Snackbar.LENGTH_LONG);
        } else {
            if (distanceLevel2[0] < mShapeLevel2.getRadius()) {
                Notifications.showSnackbar(mRootLayout, Values.RESULT_INSIDE_LEVEL_2, Snackbar.LENGTH_LONG);
            } else {
                if (distanceLevel3[0] < mShapeLevel3.getRadius()) {
                    Notifications.showSnackbar(mRootLayout, Values.RESULT_INSIDE_LEVEL_3, Snackbar.LENGTH_LONG);
                } else {
                    Notifications.showSnackbar(mRootLayout, Values.RESULT_SURVIVED, Snackbar.LENGTH_LONG);
                }
            }
        }
        drawLine();
    }

    private void setUpLongTapListener() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                                 @Override
                                                 public void onMapLongClick(LatLng latLng) {
                                                     clearOldImpact();
                                                     mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.target)).title("Bomb target. Long tap anywhere on the map to choose another location."));
                                                 }
                                             }
        );
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mGoogleApiClient == null) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLocationReadyFlag = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        setUpGMaps();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocation();
        if (mLastLocation != null) {
            Notifications.showSnackbar(mRootLayout, Values.WARNING_SNACKBAR_LOCATION_DEFINED, Snackbar.LENGTH_LONG);
            navigateSmoothOnMap(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
