package com.example.swingolf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.swingolf.SpielerInnenAnlegen.SPIELER_FILE;

public class SpielerInnenBearbeiten extends AppCompatActivity {
    private Textformatierung textformatierung;
    private ArrayList <CheckBox> myCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_spieler_innen_bearbeiten);
        this.textformatierung = new Textformatierung();
        this.myCheckBoxes = new ArrayList<>(textformatierung.getMaxkapazitaet());

        try {
            pruefeObFileExistiert();
            spielerDarstellen(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Button buttonZurueckZumHauptmenue = findViewById(R.id.zurueckZumHautmenue);
        buttonZurueckZumHauptmenue.setOnClickListener(view -> openActivityZurueckZumHauptmenue());

        Button buttonSpielerAnlegen = findViewById(R.id.neuenSpielerAnlegen);
        buttonSpielerAnlegen.setOnClickListener(view -> openActivityNeuenSpielerAnlegen());

        Button deleteButton = findViewById(R.id.spielerLoeschen);
        deleteButton.setOnClickListener(view -> {
            try {
                loescheAusgewaehleSpieler();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void openActivityZurueckZumHauptmenue() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivityNeuenSpielerAnlegen() {
        Intent intent = new Intent(this, SpielerInnenAnlegen.class);
        startActivity(intent);
    }

    public void pruefeObFileExistiert() throws IOException {
        File anlageDaten = new File(gibPfad());
        anlageDaten.createNewFile();
    }

    public void spielerDarstellen(Context context) throws IOException {
        File file = new File(gibPfad());
        BufferedReader checkReader = new BufferedReader(new FileReader(gibPfad()));
        BufferedReader mainReader = new BufferedReader(new FileReader(gibPfad()));

        if(!file.exists() || checkReader.readLine() == null) {
            keineEintraegeVorhandenTextausgabe(this);
            checkReader.close();
            return;
        }

        String line;
        int y = textformatierung.getYstartpunkt();
        int i = 0;
        while ((line = mainReader.readLine()) != null) {
            y += textformatierung.getOffset();
            myCheckBoxes.add(new CheckBox(context));
            myCheckBoxes.get(i).setText(line);
            myCheckBoxes.get(i).setY(y);
            myCheckBoxes.get(i).setX(textformatierung.getXstartpunkt());
            myCheckBoxes.get(i).setTextColor(getPrimaerfarbe());
            myCheckBoxes.get(i).setTextSize(textformatierung.getTextsizeKlein());
            addContentView(myCheckBoxes.get(i), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            i++;
        }
        mainReader.close();
    }

    public void loescheAusgewaehleSpieler() throws IOException {
        if(myCheckBoxes.isEmpty()) {
            Toast.makeText(this, "Es gibt nichts zum löschen!", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<CheckBox> anlageCheckboxenUpdated = new ArrayList<>();
        File inputFile = new File(gibPfad());
        File tempFile = new File(getFilesDir() + "/myTempFile.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        boolean haekchenPruefung = false;
        int zaehler = 0;
        int neueNummerierung = 1;
        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            if(myCheckBoxes.get(zaehler).isChecked()) {
                //myCheckBoxes.get(zaehler).setText("");
                haekchenPruefung = true;
                zaehler++;
                continue;
            }
            anlageCheckboxenUpdated.add(myCheckBoxes.get(zaehler));
            String currentLineUpdated = aktualisiereNummerierungen(currentLine, neueNummerierung);
            writer.write(currentLineUpdated + System.getProperty("line.separator"));
            zaehler++;
            neueNummerierung++;
        }

        if(!haekchenPruefung) {
            Toast.makeText(this, "Sie haben kein Häkchen gesetzt!", Toast.LENGTH_LONG).show();
            return;
        }

        this.myCheckBoxes = anlageCheckboxenUpdated;

        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile);
        // TODO: Wie kann ich hier am Ende die alten TextViews löschen und die neuen anzeigen
        //  lassen? Durch den Aufruf von anlagenDarstellen passiert nichts... logischerweise
        //  weil die alten TextViews solange bestehen bleiben wie die onCreate Methode läuft.
    }

    private String aktualisiereNummerierungen(String currentLine, int neueNummerierung) {
        String[] arr = currentLine.split(" ", 2);
        return neueNummerierung + ". " + arr[1];
    }

    public String gibPfad() {
        return getFilesDir() + "/" + SPIELER_FILE;
    }

    @SuppressLint("SetTextI18n")
    void keineEintraegeVorhandenTextausgabe(Context context) {
        TextView textView = new TextView(context);
        textView.setText("Sie haben noch keine Spieler angelegt!");
        textView.setY(textformatierung.getYstartpunkt()+textformatierung.getOffset());
        textView.setX(textformatierung.getXstartpunkt());
        textView.setTextColor(getPrimaerfarbe());
        textView.setTextSize(textformatierung.getTextsizeKlein());
        addContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public int getPrimaerfarbe() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}