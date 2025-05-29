package com.example.pokedex.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.pokedex.interfaces.ExportStrategy;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocxExportStrategy implements ExportStrategy {
    @Override
    public void export(String fileName, String content, Context context) {
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName + ".docx");

            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(content);

            FileOutputStream out = new FileOutputStream(file);
            document.write(out);
            out.close();
            document.close();

            Toast.makeText(context, "Exportado como DOCX en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al exportar DOCX", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
