package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.pokedex.models.PokemonLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pokedex.databinding.ActivityMapsBinding;
import com.google.common.collect.Maps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseRef;
    private String pokemonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pokemonName = getIntent().getStringExtra("pokemon_name").toLowerCase();
        databaseRef = FirebaseDatabase.getInstance().getReference("pokemon_locations").child(pokemonName);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Button btnZoomIn = findViewById(R.id.btnZoomIn);
        Button btnZoomOut = findViewById(R.id.btnZoomOut);

        btnZoomIn.setOnClickListener(v -> {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        btnZoomOut.setOnClickListener(v -> {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        });


        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LatLng last = null;
                for (DataSnapshot locationSnap : snapshot.getChildren()) {
                    PokemonLocation loc = locationSnap.getValue(PokemonLocation.class);
                    if (loc != null) {
                        LatLng point = new LatLng(loc.latitude, loc.longitude);
                        mMap.addMarker(new MarkerOptions().position(point).title("Ubicación"));
                        last = point;
                    }
                }
                if (last != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(last, 10));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MapsActivity.this, "Error al cargar ubicaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}