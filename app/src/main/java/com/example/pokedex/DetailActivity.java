package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity {
    ImageView a; TextView b, c; Button d, e;

    @Override
    protected void onCreate(Bundle f) {
        super.onCreate(f); setContentView(R.layout.activity_detail);
        a = findViewById(R.id.imageView); b = findViewById(R.id.nameView); c = findViewById(R.id.typesView);
        d = findViewById(R.id.btnAddLocation); e = findViewById(R.id.btnViewLocations);
        String g = getIntent().getStringExtra("url");
        new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build()
                .create(PokeApiService.class).getPokemonDetail(g).enqueue(new Callback<PokemonDetail>() {
                    @Override
                    public void onResponse(Call<PokemonDetail> h, Response<PokemonDetail> i) {
                        if (i.isSuccessful() && i.body() != null) {
                            PokemonDetail p = i.body(); b.setText(cap(p.getName()));
                            Picasso.get().load(p.getSprites().front_default).into(a);
                            StringBuilder sb = new StringBuilder("Tipo: ");
                            for (PokemonDetail.TypeSlot t : p.getTypes()) sb.append(cap(t.type.name)).append(" ");
                            c.setText(sb.toString().trim());
                        }
                    }
                    @Override
                    public void onFailure(Call<PokemonDetail> h, Throwable t) {
                        Toast.makeText(DetailActivity.this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
                    }
                });
        d.setOnClickListener(v -> {
            Intent i = new Intent(this, AddLocation.class);
            i.putExtra("pokemon_name", b.getText().toString().toLowerCase()); startActivity(i);
        });
        e.setOnClickListener(v -> {
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("pokemon_name", b.getText().toString().toLowerCase()); startActivity(i);
        });
    }

    private String cap(String s) { return s.substring(0,1).toUpperCase() + s.substring(1); }
}
