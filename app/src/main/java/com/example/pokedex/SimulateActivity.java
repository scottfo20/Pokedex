package com.example.pokedex;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.actions.AttackAction;
import com.example.pokedex.actions.DefendAction;
import com.example.pokedex.actions.HealAction;
import com.example.pokedex.actions.RunAction;
import com.example.pokedex.interfaces.ExportStrategy;
import com.example.pokedex.interfaces.PokemonAction;
import com.example.pokedex.models.BattleStats;
import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.services.PokeApiService;
import com.example.pokedex.utilities.CombatExporter;
import com.example.pokedex.utilities.DocxExportStrategy;
import com.example.pokedex.utilities.PokemonActionFactory;
import com.example.pokedex.utilities.TxtExportStrategy;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimulateActivity extends AppCompatActivity {

    private PokemonDetail pokemon;
    private PokemonAction action;
    private TextView tvResult, tvStats;
    private Map<String, PokemonAction> actions;
    private Button btnAttack, btnDefend, btnRun, btnHeal, btnExport;
    Spinner spinnerExportFormat;
    private BattleStats battleStats = new BattleStats();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate);

        tvResult = findViewById(R.id.tvResult);
        btnAttack = findViewById(R.id.btnAttack);
        btnDefend = findViewById(R.id.btnDefend);
        btnRun = findViewById(R.id.btnRun);
        btnHeal = findViewById(R.id.btnHeal);
        tvStats = findViewById(R.id.tvStats);
        btnExport = findViewById(R.id.btnExport);
        spinnerExportFormat = findViewById(R.id.spinnerExportFormat);



        // Deshabilitar botones mientras carga el detalle
        btnAttack.setEnabled(false);
        btnDefend.setEnabled(false);
        btnRun.setEnabled(false);
        btnHeal.setEnabled(false);

        String pokemonName = getIntent().getStringExtra("pokemon_name");
        if (pokemonName == null || pokemonName.isEmpty()) {
            Toast.makeText(this, "No se especificó el Pokémon", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new String[]{"TXT", "DOCX"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExportFormat.setAdapter(adapter);


        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService api = retrofit.create(PokeApiService.class);

        // Cargar detalle Pokémon usando endpoint "pokemon/{name}"
        api.getPokemonDetail("pokemon/" + pokemonName).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemon = response.body();

                    // Ahora sí habilitamos botones
                    btnAttack.setEnabled(true);
                    btnDefend.setEnabled(true);
                    btnRun.setEnabled(true);
                    btnHeal.setEnabled(true);

                    actions = new HashMap<>();
                    actions.put("attack", new AttackAction());
                    actions.put("defend", new DefendAction());
                    actions.put("run", new RunAction());
                    actions.put("heal", new HealAction());


                    btnAttack.setOnClickListener(v -> {
                        battleStats.incrementAttack();
                        action = PokemonActionFactory.createAction("attack");
                        tvResult.setText(action.execute(pokemon));
                        updateStatsDisplay();
                    });

                    btnDefend.setOnClickListener(v -> {
                        battleStats.incrementDefense();
                        action = PokemonActionFactory.createAction("defend");
                        tvResult.setText(action.execute(pokemon));
                        updateStatsDisplay();
                    });

                    btnRun.setOnClickListener(v -> {
                        battleStats.incrementEscape();
                        action = PokemonActionFactory.createAction("run");
                        tvResult.setText(action.execute(pokemon));
                        updateStatsDisplay();
                    });
                    btnHeal.setOnClickListener(v -> {
                        battleStats.incrementHeal();
                        action = PokemonActionFactory.createAction("heal");
                        tvResult.setText(action.execute(pokemon));
                        updateStatsDisplay();
                    });

                } else {
                    Toast.makeText(SimulateActivity.this, "No se pudo cargar detalles del Pokémon", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Toast.makeText(SimulateActivity.this, "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnExport.setOnClickListener(v -> {
            if (pokemon == null) {
                Toast.makeText(this, "Datos del Pokémon no disponibles aún", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedFormat = spinnerExportFormat.getSelectedItem().toString();
            ExportStrategy strategy;

            if (selectedFormat.equals("DOCX")) {
                strategy = new DocxExportStrategy();
            } else {
                strategy = new TxtExportStrategy();
            }

            // Contenido real del combate
            String content = "Reporte de Combate Pokémon\n\n" +
                    "Nombre: " + pokemon.getName().toUpperCase() + "\n" +
                    "ID: " + pokemon.getId() + "\n" +
                    "Estadísticas de la Batalla:\n" +
                    "• Ataques realizados: " + battleStats.getAttacks() + "\n" +
                    "• Defensas realizadas: " + battleStats.getDefenses() + "\n" +
                    "• Veces que huyó: " + battleStats.getEscapes() + "\n" +
                    "• Veces que se curó: " + battleStats.getHeals() + "\n";

            CombatExporter exporter = new CombatExporter(strategy);
            exporter.export("combate_" + System.currentTimeMillis(), content, this);
        });

    }

    private void updateStatsDisplay() {
        tvStats.setText(battleStats.toString());
    }

}
