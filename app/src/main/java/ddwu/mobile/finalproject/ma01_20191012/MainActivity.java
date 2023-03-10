package ddwu.mobile.finalproject.ma01_20191012;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "MainActivity";
    final static int PERMISSION_REQ_CODE = 100;
    final int ADD_PLACE_CODE = 150;

    /*UI*/
    private EditText etKeyword;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;

    /*DATA*/
    private PlacesClient placesClient;

    private List<MyPlace> places = null;
    private List<Marker> markers = new ArrayList<>();

    private LocationManager locationManager;

    LatLng currentLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKeyword = findViewById(R.id.etKeyword);

        MyPlaceDBManager myPlaceDBManager = new MyPlaceDBManager(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        places = myPlaceDBManager.getAllPlace();

        mapLoad();

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        placesClient = Places.createClient(this);

        locationUpdate();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu01:
                Intent map_intent = new Intent(MainActivity.this, MyPlaceActivity.class);
                MyPlaceDBManager myPlaceDBManager = new MyPlaceDBManager(this);
                places = myPlaceDBManager.getAllPlace();
                map_intent.putExtra("places", (Serializable) places);
                startActivity(map_intent);
                break;
            case R.id.menu02:
                Intent dev_intent = new Intent(MainActivity.this, DeveloperIntro.class);
                startActivity(dev_intent);
                break;
            case R.id.menu03:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("??? ??????")
                        .setMessage("?????? ?????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("??????", null);
                builder.show();
                break;
        }
        return true;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShow:

                if (etKeyword.getText().toString().equals("??????")) {
                    searchStart(PlaceType.BOOK_STORE);
                } else if (etKeyword.getText().toString().equals("?????????")) {
                    searchStart(PlaceType.LIBRARY);
                }

                break;
            case R.id.btn_Library:
                Intent libraryIntent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(libraryIntent);
                break;
        }
    }

    private void locationUpdate() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000, 0, locationListener);
        }
    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };


    /*????????? ????????? ?????? ????????? ??????*/
    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.api_key))
                .latlng(currentLoc.latitude, currentLoc.longitude)
                .radius(1000)
                .type(type)
                .build()
                .execute();
    }


    /*Place ID ??? ????????? ?????? ???????????? ??????*/
    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
        Place.Field.PHONE_NUMBER, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();
                        Log.d(TAG, "Place found: " + place.getName());
                        Log.d(TAG, "Phone: " + place.getPhoneNumber());
                        Log.d(TAG, "Address: " + place.getAddress());
                        Log.d(TAG, "Lat: " + place.getLatLng().latitude);
                        Log.d(TAG, "type : " + place.getTypes().get(0));
                        callDetailActivity(place);
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Place not found: " + statusCode + " " + e.getMessage());
                        }
                    }
                }
        );

    }


    private void callDetailActivity(Place place) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        /*intent.putExtra("name",place.getName());
        intent.putExtra("phone",place.getPhoneNumber());
        intent.putExtra("address",place.getAddress());*/
        MyPlace mp = new MyPlace();
        mp.setPlaceId(place.getId());
        mp.setName(place.getName());
        mp.setNumber(place.getPhoneNumber());
        mp.setAddress(place.getAddress());
        mp.setLatitude(Double.valueOf(place.getLatLng().latitude));
        mp.setLongitude(Double.valueOf(place.getLatLng().longitude));
        mp.setMemo("");

        intent.putExtra("myplace", mp);

        startActivityForResult(intent, ADD_PLACE_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PLACE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    MyPlace mp = (MyPlace) data.getSerializableExtra("myplace");
                    Toast.makeText(this, mp.getName() + " ?????? ??????", Toast.LENGTH_SHORT).show();
                    getMarker(mGoogleMap);
                    mapLoad();
                    getMarker(mGoogleMap);
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    getMarker(mGoogleMap);
                    mapLoad();
                    getMarker(mGoogleMap);
                    break;
            }
        } /*else if (requestCode == UPDATE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "??? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "??? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    break;
            }
        }*/
    }


    PlacesListener placesListener = new PlacesListener() {

        @Override
        public void onPlacesFailure(PlacesException e) { }

        @Override
        public void onPlacesStart() { }

        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d(TAG, "Addomg Markers");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.snippet(place.getTypes()[0]);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                        newMarker.showInfoWindow();
                        Log.d(TAG, place.getName() + " : " + place.getPlaceId());
                    }
                }
            });

        }

        @Override
        public void onPlacesFinished() { }
    };

    public void getMarker(GoogleMap googleMap) {
        MyPlaceDBManager myPlaceDBManager = new MyPlaceDBManager(this);
        places = myPlaceDBManager.getAllPlace();
        if (places != null) {
            for (MyPlace place : places) {
                markerOptions = new MarkerOptions();
                Log.d("marker", String.valueOf(place.getLatitude()));
                markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                markerOptions.title(place.getName());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                Marker m = mGoogleMap.addMarker(markerOptions);
                m.setTag(place.getPlaceId());
                m.showInfoWindow();
                markers.add(m);
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        markerOptions = new MarkerOptions();
        Log.d(TAG, "Map ready");

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        getMarker(mGoogleMap);

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Toast.makeText(MainActivity.this,
                        String.format("????????????: (%f, %f)", location.getLatitude(), location.getLongitude()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                if (!checkPlaceExist(placeId))
                    getPlaceDetail(placeId);
                else
                    Toast.makeText(MainActivity.this, "?????? ?????? ????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Toast.makeText(MainActivity.this, "?????? : " + latLng.latitude + " ?????? : " + latLng.longitude, Toast.LENGTH_SHORT).show();
                locationManager.removeUpdates(locationListener);
            }
        });
    }


    /*???????????? ??????????????? ??????*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // ???????????? this: MainActivity ??? OnMapReadyCallback ??? ???????????????
    }

    public boolean checkPlaceExist(String placeId) {
        MyPlaceDBManager myPlaceDBManager = new MyPlaceDBManager(this);
        places = myPlaceDBManager.getAllPlace();
        if (places != null) {
            for (MyPlace place : places) {
                if (place.getPlaceId().equals(placeId))
                    return true;
            }

        }
        return false;
    }



    /* ?????? permission ?????? */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ???????????? ??????????????? ?????? ??? ?????? ??????
                mapLoad();
            } else {
                // ????????? ????????? ??? ???????????? ??????
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapLoad();
    }

    /*????????? ?????? ????????? ?????? ????????? ???????????? ?????? ?????? ?????? ?????????*/

/*
    private void startCPSearch() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.ID);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).

        if (checkPermission()) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()){
                        FindCurrentPlaceResponse response = task.getResult();
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                            Log.i(TAG, String.format("Place ID: %s", placeLikelihood.getPlace().getId()));
                            Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getLikelihood()));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        }
    }
*/

}