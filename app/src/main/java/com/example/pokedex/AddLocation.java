package com.example.pokedex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLocation extends AppCompatActivity {
    private EditText a, b; private Button c; private DatabaseReference d; private String e;

    @Override
    protected void onCreate(Bundle f) {
        super.onCreate(f); setContentView(R.layout.activity_add_location);
        a = findViewById(R.id.editLatitude); b = findViewById(R.id.editLongitude); c = findViewById(R.id.btnSaveLocation);
        e = getIntent().getStringExtra("pokemon_name").toLowerCase(); d = FirebaseDatabase.getInstance().getReference("pokemon_locations");
        c.setOnClickListener(v -> {
            try {
                double x = Double.parseDouble(a.getText().toString());
                double y = Double.parseDouble(b.getText().toString());
                String z = d.push().getKey();
                if (z != null) {
                    PokemonLocation p = new PokemonLocation(x, y);
                    d.child(e).child(z).setValue(p).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(er -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception ee) {
                Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

