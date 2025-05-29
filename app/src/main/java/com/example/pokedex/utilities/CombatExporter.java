package com.example.pokedex.utilities;

import android.content.Context;

import com.example.pokedex.interfaces.ExportStrategy;

public class CombatExporter {
    private ExportStrategy strategy;

    public CombatExporter(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void export(String fileName, String content, Context context) {
        strategy.export(fileName, content, context);
    }
}

