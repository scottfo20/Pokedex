package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.services.PokeApiService;
import com.example.pokedex.services.RetrofitClient;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameView, typesView;
    private Button btnAddLocation, btnViewLocations, btnSimulate;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();

        String url = getIntent().getStringExtra("url");
        PokeApiService api = RetrofitClient.getInstance().create(PokeApiService.class);

        api.getPokemonDetail(url).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail p = response.body();
                    nameView.setText(capitalize(p.getName()));
                    Picasso.get().load(p.getSprites().front_default).into(imageView);
                    typesView.setText(formatTypes(p.getTypes()));
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddLocation.setOnClickListener(v -> startActivityWithPokemonName(AddLocation.class));
        btnViewLocations.setOnClickListener(v -> startActivityWithPokemonName(MapsActivity.class));
        btnSimulate.setOnClickListener(v -> startActivityWithPokemonName(SimulateActivity.class));
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        nameView = findViewById(R.id.nameView);
        typesView = findViewById(R.id.typesView);
        btnAddLocation = findViewById(R.id.btnAddLocation);
        btnViewLocations = findViewById(R.id.btnViewLocations);
        btnSimulate = findViewById(R.id.btnSimulate);
    }

    private void startActivityWithPokemonName(Class<?> activityClass) {
        String pokemonName = nameView.getText().toString().toLowerCase();
        Intent intent = new Intent(DetailActivity.this, activityClass);
        intent.putExtra("pokemon_name", pokemonName);
        startActivity(intent);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String formatTypes(List<PokemonDetail.TypeSlot> typesList) {
        StringBuilder types = new StringBuilder("Tipo: ");
        for (PokemonDetail.TypeSlot t : typesList) {
            types.append(capitalize(t.type.name)).append(" ");
        }
        return types.toString().trim();
    }
}
