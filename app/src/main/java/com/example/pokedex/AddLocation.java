package com.example.pokedex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pokedex.models.PokemonLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLocation extends AppCompatActivity {

    //SRP: Maneja la UI para añadir ubicación y guarda datos en Firebase.
    //DIP: Aquí se depende directamente de FirebaseDatabase (puede mejorarse para inyectar una abstracción, mejorando DIP).
    //DRY: Validación clara y manejo de errores evita duplicación.
    private EditText editLatitude, editLongitude;
    private Button btnSaveLocation;
    private DatabaseReference databaseRef;
    private String pokemonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        editLatitude = findViewById(R.id.editLatitude);
        editLongitude = findViewById(R.id.editLongitude);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);

        pokemonName = getIntent().getStringExtra("pokemon_name").toLowerCase();
        databaseRef = FirebaseDatabase.getInstance().getReference("pokemon_locations");

        btnSaveLocation.setOnClickListener(v -> {
            try {
                double lat = Double.parseDouble(editLatitude.getText().toString());
                double lon = Double.parseDouble(editLongitude.getText().toString());

                String locationId = databaseRef.push().getKey();
                if (locationId != null) {
                    PokemonLocation location = new PokemonLocation(lat, lon);
                    databaseRef.child(pokemonName).child(locationId).setValue(location)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show();
                                finish(); // ← vuelve a DetailActivity
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
