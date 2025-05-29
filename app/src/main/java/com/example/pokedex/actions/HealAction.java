package com.example.pokedex.actions;

import com.example.pokedex.interfaces.PokemonAction;
import com.example.pokedex.models.PokemonDetail;

public class HealAction implements PokemonAction {
    @Override
    public String execute(PokemonDetail pokemon) {
        return pokemon.getName() + " usa un movimiento de curación para recuperar energías!";
    }
}