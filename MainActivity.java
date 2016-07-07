package proveb.gk.com.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private float currentZoom = -1;
    ArrayList<LatLng> mMarkerPoints;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private GoogleMap mGoogleMap;
    LatLng currentPosition;

    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 0;

    public LocationManager locationManager;
    public MyLocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available

        setContentView(R.layout.activity_main);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            mMarkerPoints = new ArrayList<LatLng>();
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
            mGoogleMap = fm.getMap();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            listener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        /*LocationChanged Listener Methods Implementation End*/
            mGoogleMap.setMapType(MAP_TYPES[1 ]);
            mGoogleMap.setMyLocationEnabled(true);
        }
        initListeners();
    }

    private void initListeners() {
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setOnMapClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /*LocationChanged Listener Methods Implementation Start*/
    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.e("Location changed:", loc.getLatitude() + " , " + loc.getLongitude());
            currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 18));



            MarkerOptions options = new MarkerOptions().position( currentPosition );

            options.icon( BitmapDescriptorFactory.defaultMarker() );
            mGoogleMap.addMarker(options);
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
    /*LocationChanged Listener Methods Implementation End*/

}