package com.example.pokedex;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.inappmessaging.model.Button;

public class SimulateActivity extends AppCompatActivity {

    private PokemonDetail pokemon;
    private TextView txtResult;
    private Button btnAttack, btnDefend, btnRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate);

        pokemon = (PokemonDetail) getIntent().getSerializableExtra("pokemon");




    }

    private void showResult(String result) {
        txtResult.setText(result);
    }
}