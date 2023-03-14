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

public class SwinGolfAnlageAnlegen extends AppCompatActivity {
    static final String ANLAGEN_FILE = "Spielanlagedaten.txt";
    private Textformatierung textformatierung;
    private EditText nameDerAnlage;
    private EditText anzahlDerBahnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_swin_golf_anlage_anlegen);

        this.textformatierung = new Textformatierung();
        this.nameDerAnlage = findViewById(R.id.name_der_anlage);
        this.anzahlDerBahnen = findViewById(R.id.anzahl_der_bahnen);

        Button buttonZurueckZumHauptmenue = findViewById(R.id.zurueckZumHauptmenue);
        buttonZurueckZumHauptmenue.setOnClickListener(view -> openActivityZurueckZumHauptmenue());
    }

    public void openActivityZurueckZumHauptmenue() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // TODO: Kann man das noch besser lösen als mit einem Intent?
    public void openActivityZurueckZurAnlagenuebersicht() {
        Intent intent = new Intent(this, SwinGolfAnlageBearbeiten.class);
        startActivity(intent);
    }

    public void saveAnlage(View v) throws IOException {
        String textNameDerAnlage = nameDerAnlage.getText().toString();
        String textAnzahlDerBahnen = anzahlDerBahnen.getText().toString();
        String concat = textNameDerAnlage + " " + textAnzahlDerBahnen;
        boolean pruefungAufZahl = pruefeAufZahleneingabe(textAnzahlDerBahnen);

        if (textNameDerAnlage.isEmpty() || textAnzahlDerBahnen.isEmpty()) {
            Toast.makeText(this, "Eines Ihrer Pflichtfelder ist leer.", Toast.LENGTH_LONG).show();
            return;
        } else if (zeilenZaehler() > 10) {
            Toast.makeText(this, "Sie können nur maximal 10 Einträge machen.", Toast.LENGTH_LONG).show();
            return;
        } else if (!pruefungAufZahl) {
            Toast.makeText(this, "Geben Sie bitte eine Zahl bei Anzahl Bahnen ein.", Toast.LENGTH_LONG).show();
            return;
        } else if (textformatierung.containsWhitespace(textNameDerAnlage)) {
            Toast.makeText(this, "Der Name der Anlage darf keine Leerzeichen enthalten.", Toast.LENGTH_LONG).show();
            return;
        } else if (concat.length() > 13) {
            Toast.makeText(this, "Sie haben die maximale Anzahl an Zeichen (14) überschritten!", Toast.LENGTH_LONG).show();
            return;
        } else if (Integer.parseInt(textAnzahlDerBahnen) > 7) {
            Toast.makeText(this, "Sie können nicht mehr als 7 Bahnen anlegen.", Toast.LENGTH_LONG).show();
            return;
        }

        String pfad = gibPfad();

        FileWriter fw = new FileWriter(pfad, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(zeilenZaehler() + ". " + textNameDerAnlage + " " + textAnzahlDerBahnen);
        pw.close();

        nameDerAnlage.getText().clear();
        anzahlDerBahnen.getText().clear();
        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + ANLAGEN_FILE, Toast.LENGTH_LONG).show();
        openActivityZurueckZurAnlagenuebersicht();
    }

    private boolean pruefeAufZahleneingabe(String anzDerBahnen) {
        return android.text.TextUtils.isDigitsOnly(anzDerBahnen);
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
        return getFilesDir() + "/" + ANLAGEN_FILE;
    }

    // TODO: Prototyp: Diese Methode könnte man noch in Anlage Bearbeiten einbauen...
    private void loescheAlleEintraege() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(gibPfad());
        writer.print("");
        writer.close();
    }
}