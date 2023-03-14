package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
    }

    public void init() {
        Game game = new Game(this);
        game.setNumColumns(20);
        game.setNumRows(10);
        game.setWahrscheinlichkeit(25);
        setContentView(game);
    }
}