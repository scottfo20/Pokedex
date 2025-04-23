package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.adapters.PokemonAdapter;
import com.example.pokedex.models.PokemonItem;
import com.example.pokedex.models.PokemonListResponse;
import com.example.pokedex.services.PokeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<PokemonItem> pokemonList = new ArrayList<>();
    private PokeApiService apiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewPokemons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PokemonAdapter(pokemonList, this::onPokemonClicked);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(PokeApiService.class);

        fetchPokemonList();
    }

    private void fetchPokemonList() {
        apiService.getPokemonList(20).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList.clear();
                    pokemonList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                    Log.d("MainActivity", "Pokémons cargados: " + pokemonList.size());
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al cargar Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPokemonClicked(PokemonItem item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("url", item.getUrl());
        startActivity(intent);
    }
}