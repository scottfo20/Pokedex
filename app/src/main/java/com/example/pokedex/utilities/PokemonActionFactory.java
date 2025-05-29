package com.example.pokedex.utilities;

import com.example.pokedex.actions.AttackAction;
import com.example.pokedex.actions.DefendAction;
import com.example.pokedex.actions.HealAction;
import com.example.pokedex.actions.RunAction;
import com.example.pokedex.actions.SpecialattackAction;
import com.example.pokedex.interfaces.PokemonAction;

public class PokemonActionFactory {
    public static PokemonAction createAction(String actionType) {
        switch (actionType.toLowerCase()) {
            case "attack":
                return new AttackAction();
            case "defend":
                return new DefendAction();
            case "run":
                return new RunAction();
            case "heal":
                return new HealAction();
            case "special_attack":
                return new SpecialattackAction();
            default:
                throw new IllegalArgumentException("Acción no válida: " + actionType);
        }
    }
}

