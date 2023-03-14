package com.example.swingolf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        Button buttonSwinGolfAnlageBearbeiten = findViewById(R.id.swinGolfAnlageBearbeiten);
        buttonSwinGolfAnlageBearbeiten.setOnClickListener(view -> openActivitySwinGolfAnlageBearbeiten());

        Button buttonSpielerInnenBearbeiten = findViewById(R.id.spielerinnenBearbeiten);
        buttonSpielerInnenBearbeiten.setOnClickListener(view -> openActivitySpielerInnenBearbeiten());

        Button buttonNeuesSpielStarten = findViewById(R.id.neuesSpielStarten);
        buttonNeuesSpielStarten.setOnClickListener(view -> openActivityNeuesSpielStarten());
    }

    public void openActivitySwinGolfAnlageBearbeiten() {
        Intent intent = new Intent(this, SwinGolfAnlageBearbeiten.class);
        startActivity(intent);
    }

    public void openActivitySpielerInnenBearbeiten() {
        Intent intent = new Intent(this, SpielerInnenBearbeiten.class);
        startActivity(intent);
    }

    public void openActivityNeuesSpielStarten() {
        Intent intent = new Intent(this, NeuesSpielStarten.class);
        startActivity(intent);
    }

}