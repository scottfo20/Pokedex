package com.example.pokedex.interfaces;

import android.content.Context;

public interface ExportStrategy {
    void export(String fileName, String content, Context context);
}

