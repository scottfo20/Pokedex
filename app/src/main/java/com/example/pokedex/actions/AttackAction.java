package com.example.pokedex.actions;

import com.example.pokedex.interfaces.PokemonAction;
import com.example.pokedex.models.PokemonDetail;

public class AttackAction implements PokemonAction {
    @Override
    public String execute(PokemonDetail pokemon) {
        return pokemon.getName() + " lanza un ataque poderoso!";
    }
}

