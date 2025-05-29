package com.example.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class A extends RecyclerView.Adapter<A.B> {
    private List<PokemonItem> x;
    private C d;

    public interface C {
        void o(PokemonItem i);
    }

    public A(List<PokemonItem> y, C z) {
        x = y; d = z;
    }

    @NonNull
    @Override
    public B onCreateViewHolder(@NonNull ViewGroup a, int b) {
        return new B(LayoutInflater.from(a.getContext()).inflate(R.layout.item_pokemon, a, false));
    }

    @Override
    public void onBindViewHolder(@NonNull B b, int i) {
        PokemonItem p = x.get(i);
        String name = p.getName();
        if (name != null && !name.isEmpty()) {
            b.t.setText(name.substring(0,1).toUpperCase() + name.substring(1));
        } else {
            b.t.setText(""); // O un texto por defecto
        }
        b.itemView.setOnClickListener(v -> d.o(p));
    }


    @Override
    public int getItemCount() { return x.size(); }

    static class B extends RecyclerView.ViewHolder {
        TextView t;
        B(View v) { super(v); t = v.findViewById(R.id.textViewPokemonName); }
    }
}
