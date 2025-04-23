package com.example.pokedex.models;

import java.util.List;

public class PokemonDetail {
    private int id;
    private String name;
    private Sprites sprites;
    private List<TypeSlot> types;

    public String getName() { return name; }
    public Sprites getSprites() { return sprites; }
    public List<TypeSlot> getTypes() { return types; }

    public class Sprites {
        public String front_default;
    }

    public class TypeSlot {
        public int slot;
        public Type type;

        public class Type {
            public String name;
        }
    }
}
