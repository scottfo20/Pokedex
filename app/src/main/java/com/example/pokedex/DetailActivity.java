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
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.firebase.database.DatabaseReference;


public class DetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameView, typesView;
    private Button btnAddLocation, btnViewLocations, btnSimulate;
    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);
        nameView = findViewById(R.id.nameView);
        typesView = findViewById(R.id.typesView);

        btnAddLocation = findViewById(R.id.btnAddLocation);
        btnViewLocations = findViewById(R.id.btnViewLocations);
        btnSimulate = findViewById(R.id.btnSimulate);



        String url = getIntent().getStringExtra("url");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService api = retrofit.create(PokeApiService.class);

        api.getPokemonDetail(url).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail p = response.body();
                    nameView.setText(capitalize(p.getName()));
                    Picasso.get().load(p.getSprites().front_default).into(imageView);

                    StringBuilder types = new StringBuilder("Tipo: ");
                    for (PokemonDetail.TypeSlot t : p.getTypes()) {
                        types.append(capitalize(t.type.name)).append(" ");
                    }
                    typesView.setText(types.toString().trim());
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddLocation.setOnClickListener(v -> {
            String pokemonName = nameView.getText().toString().toLowerCase();
            Intent intent = new Intent(DetailActivity.this, AddLocation.class);
            intent.putExtra("pokemon_name", pokemonName);
            startActivity(intent);
        });

        btnViewLocations.setOnClickListener(v -> {
            String pokemonName = nameView.getText().toString().toLowerCase();
            Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
            intent.putExtra("pokemon_name", pokemonName);
            startActivity(intent);
        });

        btnSimulate.setOnClickListener(v -> {
            String pokemonName = nameView.getText().toString().toLowerCase();
            Intent intent = new Intent(DetailActivity.this, SimulateActivity.class);
            intent.putExtra("pokemon_name", pokemonName);
            startActivity(intent);
        });



    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}