package com.example.swingolf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.*;

public class Game extends AppCompatActivity {

    private String anlagenname;
    private int anzahlBahnen;
    private HashMap<String, String> hashMapSpielernamen;
    private int anzahlSpieler;
    private EditText[][] arrayEditText;
    private Random random;
    private Textformatierung textformatierung;
    private View.OnFocusChangeListener focusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_game);

        Intent receiverIntent = getIntent();
        this.anlagenname = receiverIntent.getStringExtra("Anlagenname");
        this.anzahlBahnen = receiverIntent.getExtras().getInt("AnzahlBahnen");
        this.hashMapSpielernamen = (HashMap<String, String>) receiverIntent.getExtras().getSerializable("HashMapSpieler");
        this.anzahlSpieler = hashMapSpielernamen.size();
        this.arrayEditText = new EditText[anzahlBahnen+2][anzahlSpieler+2];
        this.random = new Random();
        this.textformatierung = new Textformatierung();
        this.focusListener = new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    berechneZwischenergebnisse();
                }
            }
        };

        editTextFelderDarstellen();

        Button buttonSpielAbbrechen = findViewById(R.id.spielabbruch);
        buttonSpielAbbrechen.setOnClickListener(view -> openActivitySpielabbruch());

        Button buttonNeuesSpiel = findViewById(R.id.neuesSpiel);
        buttonNeuesSpiel.setOnClickListener(view -> openActivityNeuesSpielStarten());
    }

    public void openActivitySpielabbruch() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivityNeuesSpielStarten() {
        Intent intent = new Intent(this, NeuesSpielStarten.class);
        startActivity(intent);
    }

    public void berechneZwischenergebnisse() {
        int anzahlAusgefuellterFelder = 0;
        int[] spielerZwischenergebnisse = new int[anzahlSpieler];

        for(int k = 1; k < anzahlBahnen + 1; k++) {
            for(int i = 2; i < anzahlSpieler + 2; i++) {
                if(!arrayEditText[k][i].getText().toString().isEmpty()) {
                    int tempZahl;
                    if((tempZahl = pruefeObEsSichUmZahlHandelt(arrayEditText[k][i].getText().toString())) != -1) {
                        spielerZwischenergebnisse[i-2] += tempZahl;
                        anzahlAusgefuellterFelder++;
                    } else {
                        Toast.makeText(this, "Bitte geben Sie eine Zahl ein!", Toast.LENGTH_LONG).show();
                        arrayEditText[k][i].getText().clear();
                    }
                    arrayEditText[k][i].setTextColor(getPrimaerfarbe());
                }
            }
        }
        for(int i = 0; i < anzahlSpieler; i++) {
            arrayEditText[anzahlBahnen+1][i+2].setHint(String.valueOf(spielerZwischenergebnisse[i]));
        }
        pruefeAufGewinner(anzahlAusgefuellterFelder, spielerZwischenergebnisse);
    }

    public int pruefeObEsSichUmZahlHandelt(String zahl) {
        if(android.text.TextUtils.isDigitsOnly(zahl)) {
            return Integer.parseInt(zahl);
        }
        return -1;
    }

    @SuppressLint("SetTextI18n")
    public void pruefeAufGewinner(int anzahlAusgefuellterFelder, int[] spielerZwischenergebnisse) {
        int gewinnerNummer = 0;
        if(anzahlAusgefuellterFelder == (anzahlSpieler*anzahlBahnen)) {
            int min = spielerZwischenergebnisse[0];
            for(int i = 0; i < spielerZwischenergebnisse.length; i++) {
                if(min > spielerZwischenergebnisse[i]) {
                    min = spielerZwischenergebnisse[i];
                    gewinnerNummer = i;
                }
            }
            int gewinnerAnzahl = 0;
            for(int i = 0; i < spielerZwischenergebnisse.length; i++) {
                if(min == spielerZwischenergebnisse[i]) {
                    gewinnerAnzahl++;
                }
            }

            lockTextEditEingabeFelder();
            String gewinner = hashMapSpielernamen.keySet().toArray()[gewinnerNummer].toString();

            gewinnerDarstellung(gewinnerAnzahl, gewinner, gewinnerNummer);
        }
    }

    @SuppressLint("SetTextI18n")
    private void gewinnerDarstellung(int gewinnerAnzahl, String gewinner, int gewinnerNummer) {
        TextView textView = new TextView(this);
        if(gewinnerAnzahl == 1) {
            gewinner = gewinner + " hat gewonnen!";
            arrayEditText[anzahlBahnen+1][gewinnerNummer+2].getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            textView.setText(gewinner);
        } else {
            textView.setText("Unentschieden!");
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getPrimaerfarbe());
        gd.setCornerRadius(0);
        gd.setStroke(1, 0xffffff00);

        textView.setTextSize(textformatierung.getTextsizeGross());
        textView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setY((float) Resources.getSystem().getDisplayMetrics().heightPixels / 2 - (float) textView.getMeasuredHeight()/2);
        textView.setX((float) Resources.getSystem().getDisplayMetrics().widthPixels / 2 - (float) textView.getMeasuredWidth()/2);
        textView.setTextColor(Color.YELLOW);
        textView.setBackground(gd);
        addContentView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void lockTextEditEingabeFelder() {
        for (int k = 1; k < anzahlBahnen + 1; k++) {
            for (int i = 2; i < anzahlSpieler + 2; i++) {
                arrayEditText[k][i].setFocusable(false);
            }
        }
    }

    public void editTextFelderDarstellen() {
        int xStartpunkt = textformatierung.getXstartpunktfelder();
        int yStartpunkt = textformatierung.getYstartpunktfelder();
        int gridOffset = textformatierung.getXgridoffsetfelder();
        int bildschirmbreite = Resources.getSystem().getDisplayMetrics().widthPixels;
        int bildschirmlaenge = Resources.getSystem().getDisplayMetrics().heightPixels;
        for(int k = 0; k < anzahlBahnen+2; k++) {
            for(int i = 0; i < anzahlSpieler+2; i++) {
            arrayEditText[k][i] = new EditText(this);
            arrayEditText[k][i].setWidth(bildschirmbreite/(anzahlSpieler+2));
            arrayEditText[k][i].setHeight((bildschirmlaenge-gridOffset)/(anzahlBahnen+2));
            arrayEditText[k][i].setBackground(ContextCompat.getDrawable(this, R.drawable.border));
            arrayEditText[k][i].setX(xStartpunkt);
            arrayEditText[k][i].setY(yStartpunkt);
            gridFeldAufteilung(k, i);
            arrayEditText[k][i].setHintTextColor(getPrimaerfarbe());
            xStartpunkt += bildschirmbreite/(anzahlSpieler+2);
            addContentView(arrayEditText[k][i], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            xStartpunkt = 0;
            yStartpunkt += (bildschirmlaenge-gridOffset)/(anzahlBahnen+2);
        }
    }

    public void gridFeldAufteilung(int k, int i) {
        if(k == 0 && i == 0) {
            arrayEditText[k][i].setHint("Hole");
            arrayEditText[k][i].setFocusable(false);
        } else if (k == 0 && i == 1) {
            arrayEditText[k][i].setHint("Par");
            arrayEditText[k][i].setFocusable(false);
        } else if (k != 0 && i == 1 && (k != anzahlBahnen + 2 - 1)) {
            arrayEditText[k][i].setHint(String.valueOf(random.nextInt(10)+5));
            arrayEditText[k][i].setFocusable(false);
        } else if (i == 0 && (k != anzahlBahnen + 2 - 1)) {
            arrayEditText[k][i].setHint(String.valueOf(k));
            arrayEditText[k][i].setFocusable(false);
        } else if (i == 0 && k == anzahlBahnen+2-1) {
            arrayEditText[k][i].setHint("Total");
            arrayEditText[k][i].setFocusable(false);
        } else if (i != 0 && i != 1 && k == anzahlBahnen+2-1) {
            arrayEditText[k][i].setFocusable(false);
        } else if (i == 1 && k == anzahlBahnen+2-1) {
            int parSum = 0;
            for(int m = 0; m < anzahlBahnen+2; m++) {
                if(m != 0 && m != anzahlBahnen+2-1) {
                    parSum += Integer.parseInt(arrayEditText[m][1].getHint().toString());
                }
            }
            arrayEditText[k][i].setHint(String.valueOf(parSum));
            arrayEditText[k][i].setFocusable(false);
        } else if (i != 0 && i != 1 && k == 0) {
            arrayEditText[k][i].setHint((CharSequence) hashMapSpielernamen.keySet().toArray()[i-2]);
            arrayEditText[k][i].setTextSize(12);
            arrayEditText[k][i].setFocusable(false);
        } else {
            arrayEditText[k][i].setOnFocusChangeListener(focusListener);
        }
        arrayEditText[k][i].setGravity(Gravity.CENTER);
    }

    public int getPrimaerfarbe() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
}