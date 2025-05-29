package com.example.pokedex.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.models.PokemonItem;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    //SRP-DIP-OCP-DRY
    //Inversión de dependencias: Al usar una interfaz para el click (OnPokemonClickListener) el adapter depende de abstracciones, no de implementaciones concretas.
    private List<PokemonItem> list;                    // Guarda la lista de Pokémons que se muestran.
    private View.OnClickListener onClick;              // Listener genérico para clicks (no se usa en el código final).

    public interface OnPokemonClickListener {          // Interfaz para manejar clicks con detalle (Item clickeado).
        void onClick(PokemonItem item);
    }

    private final OnPokemonClickListener listener;      // Listener definido por la interfaz para delegar clicks.

    public PokemonAdapter(List<PokemonItem> list, OnPokemonClickListener listener) {
        this.list = list;                               // Inyección de dependencia: recibe lista y listener desde fuera.
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista de cada item del RecyclerView.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PokemonItem item = list.get(position);
        holder.textView.setText(capitalize(item.getName()));    // Pone el nombre capitalizado.
        holder.itemView.setOnClickListener(v -> listener.onClick(item)); // Maneja click delegándolo al listener.
    }

    @Override
    public int getItemCount() {
        return list.size();                              // Retorna cantidad de items.
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1); // Capitaliza primer letra.
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewPokemonName);    // Referencia a TextView del layout.
        }
    }
}
