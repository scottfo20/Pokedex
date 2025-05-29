package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView r; A a; List<PokemonItem> l = new ArrayList<>();
    PokeApiService s; int o = 0, m = 20; boolean b = false;

    @Override
    protected void onCreate(Bundle z) {
        super.onCreate(z); EdgeToEdge.enable(this); setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, in) -> {
            Insets i = in.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(i.left, i.top, i.right, i.bottom); return in;
        });
        r = findViewById(R.id.recyclerViewPokemons);
        r.setLayoutManager(new LinearLayoutManager(this));
        a = new A(l, this::q);
        r.setAdapter(a);
        s = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build().create(PokeApiService.class);
        z();
        r.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                int t = lm.getItemCount(), p = lm.findLastVisibleItemPosition();
                if (!b && p == t - 1 && o < 300) z();
            }
        });
    }

    private void z() {
        b = true;
        s.getPokemonList(m, o).enqueue(new Callback<PokemonListResponse>() {
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> res) {
                if (res.isSuccessful() && res.body() != null) {
                    l.addAll(res.body().getResults());
                    a.notifyDataSetChanged(); o += m;
                }
                b = false;
            }
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                b = false;
                Toast.makeText(MainActivity.this, "Error al cargar Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void q(PokemonItem i) {
        Intent in = new Intent(this, DetailActivity.class); in.putExtra("url", i.getUrl()); startActivity(in);
    }
}


