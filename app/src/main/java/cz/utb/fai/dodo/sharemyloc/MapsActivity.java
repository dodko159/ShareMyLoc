package cz.utb.fai.dodo.sharemyloc;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Shared shared;

    LocationManager locationManager;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;
    private SharedPreferences sharedPref;
    private DatabaseReference positionsRef;
    private DatabaseReference namesRef;
    private DatabaseReference dRef;
    private String name;
    private MarkerOptions markerOption;
    private Marker m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setMenuVisibility(true);

        sharedPref = getSharedPreferences(shared.SHARED_FILE, MODE_PRIVATE);

        sharedListener();

        dRef = FirebaseDatabase.getInstance().getReference();
        positionsRef = dRef.child("positions");

        String uid = sharedPref.getString(Shared.SHARED_WATCHING,"");
        name = sharedPref.getString(Shared.SHARED_NAME,"");

        markerOption = new MarkerOptions().position(new LatLng(0,0)).title(name);

        positionsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LatLng latLng = new LatLng(
                        dataSnapshot.child("latitude").getValue(Double.class),
                        dataSnapshot.child("longitude").getValue(Double.class)
                );
                m.setPosition(latLng);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.5f));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }

    private void sharedListener() {
        spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        if(key.equals(Shared.SHARED_POSITIONS)){

                        }
                    }
                };
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        m = mMap.addMarker(markerOption);

        // Add a marker in Sydney and move the camera
       /* LatLng position = shared.loadPosition();
        String name = sharedPref.getString(Shared.SHARED_NAME,"");
        mMap.addMarker(new MarkerOptions().position(position).title(name));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,13f));*/
    }
}
