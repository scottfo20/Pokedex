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

    private int offset = 0;
    private final int limit = 20;
    private boolean isLoading = false;
    private final int totalCount = 150;

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


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem == totalItemCount - 1 && offset < totalCount) {
                    fetchPokemonList();
                }
            }
        });
    }

    private void fetchPokemonList() {
        isLoading = true;
        apiService.getPokemonList(limit, offset).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PokemonItem> newPokemons = response.body().getResults();
                    pokemonList.addAll(newPokemons);
                    adapter.notifyDataSetChanged();
                    offset += limit;
                    Log.d("MainActivity", "Pokémons cargados: " + pokemonList.size());
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                isLoading = false;
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
