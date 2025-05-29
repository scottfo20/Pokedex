package com.example.pokedex.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.pokedex.interfaces.ExportStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TxtExportStrategy implements ExportStrategy {
    @Override
    public void export(String fileName, String content, Context context) {
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName + ".txt");

            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();

            Toast.makeText(context, "Exportado como TXT en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al exportar TXT", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}