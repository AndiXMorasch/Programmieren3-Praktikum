package com.example.swingolf;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.example.swingolf.SpielerInnenAnlegen.SPIELER_FILE;
import static com.example.swingolf.SwinGolfAnlageAnlegen.ANLAGEN_FILE;

public class NeuesSpielStarten extends AppCompatActivity {
    private Textformatierung textformatierung;
    private ArrayList<CheckBox> myCheckBoxesAnlagen;
    private ArrayList<CheckBox> myCheckBoxesSpieler;
    private String anlagenname;
    private int bahnanzahl;
    private final HashMap<String, String> spielerNamen = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_neues_spiel_starten);
        this.textformatierung = new Textformatierung();
        this.myCheckBoxesAnlagen = new ArrayList<>(textformatierung.getMaxkapazitaet());
        this.myCheckBoxesSpieler = new ArrayList<>(textformatierung.getMaxkapazitaet());

        try {
            zeigAuswahlAnAnlagen(this);
            zeigAuswahlAnSpielern(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Button buttonZurueckZumHauptmenue = findViewById(R.id.zurueckZumHauptmenue);
        buttonZurueckZumHauptmenue.setOnClickListener(view -> openActivityZurueckZumHauptmenue());

        Button buttonSpielBeginnen = findViewById(R.id.spielStarten);
        buttonSpielBeginnen.setOnClickListener(view -> {
            try {
                if(pruefeAusgewaehlteAnlage() && pruefeAusgewaehlteSpieler()) {
                    openActivitySpielBeginnen();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openActivityZurueckZumHauptmenue() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivitySpielBeginnen() {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("Anlagenname", anlagenname);
        intent.putExtra("AnzahlBahnen", bahnanzahl);
        intent.putExtra("HashMapSpieler", spielerNamen);
        startActivity(intent);
    }

    // TODO: Hier ggf. versuchen Radio Buttons zu implementieren!
    public void zeigAuswahlAnAnlagen(Context context) throws IOException {
        File file = new File(gibPfadVonAnlagen());
        BufferedReader checkReader = new BufferedReader(new FileReader(gibPfadVonAnlagen()));
        BufferedReader mainReader = new BufferedReader(new FileReader(gibPfadVonAnlagen()));

        if(!file.exists() || checkReader.readLine() == null) {
            //keineEintraegeVorhandenTextausgabe(this);
            checkReader.close();
            return;
        }

        String line;
        int y = textformatierung.getYstartpunkt();
        int i = 0;
        while ((line = mainReader.readLine()) != null) {
            y += textformatierung.getOffset();
            myCheckBoxesAnlagen.add(new CheckBox(context));
            myCheckBoxesAnlagen.get(i).setText(line);
            myCheckBoxesAnlagen.get(i).setY(y);
            myCheckBoxesAnlagen.get(i).setX(textformatierung.getXstartpunkt()-textformatierung.getOffset());
            myCheckBoxesAnlagen.get(i).setTextColor(getPrimaerfarbe());
            myCheckBoxesAnlagen.get(i).setTextSize(textformatierung.getTextsizeKlein());
            addContentView(myCheckBoxesAnlagen.get(i), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            i++;
        }
        mainReader.close();
    }

    public void zeigAuswahlAnSpielern(Context context) throws IOException {
        File file = new File(gibPfadVonSpielern());
        BufferedReader checkReader = new BufferedReader(new FileReader(gibPfadVonSpielern()));
        BufferedReader mainReader = new BufferedReader(new FileReader(gibPfadVonSpielern()));

        if(!file.exists() || checkReader.readLine() == null) {
            //keineEintraegeVorhandenTextausgabe(this);
            checkReader.close();
            return;
        }

        String line;
        int y = textformatierung.getYstartpunkt();
        int i = 0;
        while ((line = mainReader.readLine()) != null) {
            y += textformatierung.getOffset();
            myCheckBoxesSpieler.add(new CheckBox(context));
            myCheckBoxesSpieler.get(i).setText(line);
            myCheckBoxesSpieler.get(i).setY(y);
            myCheckBoxesSpieler.get(i).setX((float) Resources.getSystem().getDisplayMetrics().widthPixels / 2);
            myCheckBoxesSpieler.get(i).setTextColor(getPrimaerfarbe());
            myCheckBoxesSpieler.get(i).setTextSize(textformatierung.getTextsizeKlein());
            addContentView(myCheckBoxesSpieler.get(i), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            i++;
        }
        mainReader.close();
    }

    public boolean pruefeAusgewaehlteAnlage() throws IOException {
        if(myCheckBoxesAnlagen.isEmpty()) {
            Toast.makeText(this, "Sie haben bislang noch keine Anlage angelegt!", Toast.LENGTH_LONG).show();
            return false;
        }

        File inputFile = new File(gibPfadVonAnlagen());
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        boolean haekchenPruefung = false;
        int haekchenZaehler = 0;
        int zaehler = 0;
        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            if(myCheckBoxesAnlagen.get(zaehler).isChecked()) {
                extractDataFromString(currentLine, "Anlagen");
                haekchenPruefung = true;
                haekchenZaehler++;
            }
            zaehler++;
        }

        if(!haekchenPruefung) {
            Toast.makeText(this, "Sie haben kein Häkchen bei Anlagen gesetzt!", Toast.LENGTH_LONG).show();
            return false;
        } else if (haekchenZaehler > 1) {
            Toast.makeText(this, "Sie können nur eine Anlage auswählen!", Toast.LENGTH_LONG).show();
            return false;
        }
        reader.close();
        return true;
    }

    public boolean pruefeAusgewaehlteSpieler() throws IOException {
        if(myCheckBoxesSpieler.isEmpty()) {
            Toast.makeText(this, "Sie haben bislang noch keine Spieler angelegt!", Toast.LENGTH_LONG).show();
            return false;
        }

        File inputFile = new File(gibPfadVonSpielern());
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        boolean haekchenPruefung = false;
        int zaehler = 0;
        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            if(myCheckBoxesSpieler.get(zaehler).isChecked()) {
                extractDataFromString(currentLine, "Spieler");
                haekchenPruefung = true;
            }
            zaehler++;
        }

        if(!haekchenPruefung) {
            Toast.makeText(this, "Sie haben kein Häkchen bei Spieler gesetzt!", Toast.LENGTH_LONG).show();
            return false;
        }
        reader.close();
        return true;
    }

    public void extractDataFromString(String str, String kontext) {
        if(kontext.equals("Anlagen")) {
        this.anlagenname = str.split(" ")[1];
        Log.d("LOG ", anlagenname);

        this.bahnanzahl = Integer.parseInt(str.split(" ")[2]);
        Log.d("LOG ", String.valueOf(bahnanzahl));
        } else {
            try {
                spielerNamen.put(str.split(" ")[1], str.split(" ")[2]);
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                spielerNamen.put(str.split(" ")[1], "");
            }
        }
    }

    public String gibPfadVonAnlagen() {
        return getFilesDir() + "/" + ANLAGEN_FILE;
    }

    public String gibPfadVonSpielern() {
        return getFilesDir() + "/" + SPIELER_FILE;
    }

    public int getPrimaerfarbe() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}