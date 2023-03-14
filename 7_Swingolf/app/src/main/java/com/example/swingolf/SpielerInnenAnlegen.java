package com.example.swingolf;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.Objects;

public class SpielerInnenAnlegen extends AppCompatActivity {
    static final String SPIELER_FILE = "Spielerdaten.txt";

    private Textformatierung textformatierung;
    private EditText spielername;
    private EditText spitzname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_spieler_innen_anlegen);

        this.textformatierung = new Textformatierung();
        this.spielername = findViewById(R.id.spielername);
        this.spitzname = findViewById(R.id.spitzname);

        Button buttonZurueckZumHauptmenue = findViewById(R.id.zurueckZumHauptmenue);
        buttonZurueckZumHauptmenue.setOnClickListener(view -> openActivityZurueckZumHauptmenue());
    }

    public void openActivityZurueckZumHauptmenue() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivityZurueckZurSpieleruebersicht() {
        Intent intent = new Intent(this, SpielerInnenBearbeiten.class);
        startActivity(intent);
    }

    public void saveSpieler(View v) throws IOException {
        // TODO: Diese Methode noch in die Activity davor auslagern, so wie bei SwinGolfAnlageAnlagen!!
        File spielerDaten = new File(gibPfad());
        spielerDaten.createNewFile();

        String textSpielername = spielername.getText().toString();
        String textSpitzname = spitzname.getText().toString();
        String concat = textSpielername + " " + textSpitzname;

        if (textSpielername.isEmpty()) {
            Toast.makeText(this, "Eines Ihrer Pflichtfelder ist leer!", Toast.LENGTH_LONG).show();
            return;
        } else if (zeilenZaehler() > 10) {
            Toast.makeText(this, "Sie können nur maximal 10 Einträge machen!", Toast.LENGTH_LONG).show();
            return;
        } else if (textformatierung.containsWhitespace(textSpielername)) {
            Toast.makeText(this, "Ihr Spielername enthält Leerzeichen!", Toast.LENGTH_LONG).show();
            return;
        } else if (textformatierung.containsWhitespace(textSpitzname)) {
            Toast.makeText(this, "Ihr Spitzname enthält Leerzeichen!", Toast.LENGTH_LONG).show();
            return;
        } else if (concat.length() > 13) {
            Toast.makeText(this, "Sie haben die maximale Anzahl an Zeichen (12) überschritten!", Toast.LENGTH_LONG).show();
            return;
        }

        String pfad = gibPfad();

        FileWriter fw = new FileWriter(pfad, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(zeilenZaehler() + ". " + textSpielername + " " + textSpitzname);
        pw.close();

        spielername.getText().clear();
        spitzname.getText().clear();
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + SPIELER_FILE, Toast.LENGTH_LONG).show();
        openActivityZurueckZurSpieleruebersicht();
    }

    private Integer zeilenZaehler() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(gibPfad()));
        Integer lines = 1;
        while (reader.readLine() != null) {
            lines++;
        }
        reader.close();
        return lines;
    }

    private String gibPfad() {
        return getFilesDir() + "/" + SPIELER_FILE;
    }
}