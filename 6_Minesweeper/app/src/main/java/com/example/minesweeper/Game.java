package com.example.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Game extends View {
    private int numColumns, numRows;
    private float cellWidth, cellHeight;
    private int anzahlDerBomben = 0;
    boolean spielVerloren = false;
    boolean spielGewonnen = false;
    int wahrscheinlichkeit;
    private final MainActivity mainActivity;
    private final Paint paint = new Paint();
    private boolean[][] oberesRaster;
    private int[][] unteresRaster;
    private final Random random = new Random();
    private boolean initialerKlick = true;

    public Game(Context context) {
        this(context, null);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity) context;
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        calculateDimensions();
    }

    public void setWahrscheinlichkeit(int wahrscheinlichkeit) {
        this.wahrscheinlichkeit = wahrscheinlichkeit;
    }

    public void setNumRows(int numColumns) {
        this.numRows = numColumns; // rows = cols ist hier vertauscht aufgrund der landscape Darstellung!
        calculateDimensions();
    }

    public void setNumColumns(int numRows) {
        this.numColumns = numRows; // cols = rows ist hier vertauscht aufgrund der landscape Darstellung!
        calculateDimensions();
    }

    public void ermittleWahrscheinlichkeit(int wahrscheinlichkeit, int colInitial, int rowInitial) {
        for (int i = 0; i < this.unteresRaster.length; i++) {
            for (int k = 0; k < this.unteresRaster[i].length; k++) {
                if ((i < colInitial - 1 || i > colInitial + 1) || (k < rowInitial - 1 || k > rowInitial + 1)) {
                    int zufall = random.nextInt(101);
                    if (zufall < wahrscheinlichkeit) {
                        this.unteresRaster[i][k] = -1;
                        ++anzahlDerBomben;
                    } else {
                        this.unteresRaster[i][k] = 0;
                    }
                }
            }
        }
        zaehleNachbarBombenFreierFelder();
    }

    private void zaehleNachbarBombenFreierFelder() {
        for (int i = 0; i < this.unteresRaster.length; i++) {
            for (int k = 0; k < this.unteresRaster[i].length; k++) {
                if (this.unteresRaster[i][k] != -1) {
                    this.unteresRaster[i][k] = ermittleAnzahlNachbarBomben(i, k);
                }
            }
        }
    }

    private int ermittleAnzahlNachbarBomben(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1 && (i >= 0 && i < unteresRaster.length); i++) {
            for (int j = col - 1; j <= col + 1; j++)
                if (j >= 0 && j < unteresRaster[i].length && (i != row || j != col) && (unteresRaster[i][j] == -1))
                    ++count;
        }
        return count;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }
        cellWidth = getWidth() / (float) numColumns;
        cellHeight = getHeight() / (float) numRows;

        oberesRaster = new boolean[numColumns][numRows];
        unteresRaster = new int[numColumns][numRows];
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        zeichneUnteresRaster(canvas);
        zeichneOberesRaster(canvas);
        zeichneLiniengitter(canvas);
        zeichneAnzahlFelder(canvas);
        if (!initialerKlick) {
            zeichneAnzahlBomben(canvas);
        }

        try {
            pruefeObSpielVerlorenWurde(canvas);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (!spielVerloren) {
            pruefeObSpielGewonnenWurde(canvas);
        }
    }

    private void zeichneUnteresRaster(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                setzeFarbeFuerZahlen(i, j);
                paint.setTextSize(35f);
                if (unteresRaster[i][j] != -1 && unteresRaster[i][j] != 0) {
                    canvas.drawText(Integer.toString(unteresRaster[i][j]), i * cellWidth + (cellWidth / 2) - paint.getTextSize() / 2 + 7,
                            j * cellHeight + (cellHeight / 2) + paint.getTextSize() / 2 - 7, paint);
                } else if (unteresRaster[i][j] == -1) {
                    canvas.drawCircle(i * cellWidth + (cellWidth / 2), j * cellHeight + (cellHeight / 2), 15, paint);
                }
            }
        }
    }

    private void zeichneOberesRaster(Canvas canvas) {
        paint.setColor(Color.GRAY);
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!oberesRaster[i][j]) {
                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            paint);
                }
            }
        }
    }

    private void zeichneLiniengitter(Canvas canvas) {
        if (numColumns == 0 || numRows == 0) {
            return;
        }

        paint.setColor(Color.BLACK);
        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), paint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, paint);
        }
    }

    private void zeichneAnzahlFelder(Canvas canvas) {
        paint.setTextSize(20f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Felder: " + (numColumns * numRows), 10, 30, paint);
    }

    private void zeichneAnzahlBomben(Canvas canvas) {
        paint.setTextSize(20f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Bomben: " + anzahlDerBomben, 10, 50, paint);
    }

    private void setzeFarbeFuerZahlen(int i, int j) {
        if (unteresRaster[i][j] == 1) {
            paint.setColor(Color.BLUE);
        } else if (unteresRaster[i][j] == 2) {
            paint.setColor(Color.MAGENTA);
        } else if (unteresRaster[i][j] == 3) {
            paint.setColor(Color.YELLOW);
        } else if (unteresRaster[i][j] == 4) {
            paint.setColor(Color.CYAN);
        } else if (unteresRaster[i][j] == 5) {
            paint.setColor(Color.GREEN);
        } else if (unteresRaster[i][j] == 6) {
            paint.setColor(Color.WHITE);
        } else if (unteresRaster[i][j] == 7) {
            paint.setColor(Color.LTGRAY);
        } else if (unteresRaster[i][j] == -1) {
            paint.setColor(Color.RED);
        }
    }

    private void pruefeObSpielVerlorenWurde(Canvas canvas) throws InterruptedException {
        if (spielVerloren) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                paint.setColor(Color.argb((float) 0.25, 100, 0, 0));
            }
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            paint.setColor(Color.BLACK);
            float alteTextgroesse = paint.getTextSize();
            paint.setTextSize(80f);
            canvas.drawText("GAME OVER!", (float) getWidth() / 2 - 200, (float) getHeight() / 2, paint);
            paint.setTextSize(20f);
            canvas.drawText("BERÜHREN SIE DEN BILDSCHIRM UM EIN NEUES SPIEL ZU STARTEN.", (float) getWidth() / 2 - 300, (float) getHeight() / 2 + 30, paint);
            paint.setTextSize(alteTextgroesse);
        }
    }

    private void pruefeObSpielGewonnenWurde(Canvas canvas) {
        int counter = 0;
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!oberesRaster[i][j]) {
                    ++counter;
                }
            }
        }
        if (counter == anzahlDerBomben) {
            spielGewonnen = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                paint.setColor(Color.argb((float) 0.25, 0, 100, 0));
            }
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            paint.setColor(Color.GREEN);
            float alteTextgroesse = paint.getTextSize();
            paint.setTextSize(50f);
            canvas.drawText("SIE HABEN GEWONNEN!", (float) getWidth() / 2 - 250, (float) getHeight() / 2, paint);
            paint.setTextSize(20f);
            canvas.drawText("BERÜHREN SIE DEN BILDSCHIRM UM NOCH EIN SPIEL ZU STARTEN!.", (float) getWidth() / 2 - 270, (float) getHeight() / 2 + 30, paint);
            paint.setTextSize(alteTextgroesse);
            paint.setColor(Color.BLACK);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (spielVerloren || spielGewonnen) {
                mainActivity.init();
            }
            int column = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);
            if (!oberesRaster[column][row] && !spielVerloren) {
                oberesRaster[column][row] = !oberesRaster[column][row];
            }

            if (initialerKlick) {
                ermittleWahrscheinlichkeit(this.wahrscheinlichkeit, column, row);
                initialerKlick = false;
            }

            if (unteresRaster[column][row] == -1) {
                spielVerloren = true;
                gebeAlleBombenFrei();
            } else if (unteresRaster[column][row] == 0) {
                gebeAlleUmliegendenNachbarnFrei(column, row);
            }
            invalidate();
        }
        return true;
    }

    private void gebeAlleBombenFrei() {
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!oberesRaster[i][j] && unteresRaster[i][j] == -1) {
                    oberesRaster[i][j] = !oberesRaster[i][j];
                }
            }
        }
    }

    private void gebeAlleUmliegendenNachbarnFrei(int col, int row) {
        for (int i = col - 1; i <= col + 1; i++) {
            if (i >= 0 && i < unteresRaster.length)
                for (int j = row - 1; j <= row + 1; j++) {
                    if (j >= 0 && j < unteresRaster[i].length)
                        if (i != col || j != row) {
                            if (unteresRaster[i][j] != -1 && !oberesRaster[i][j]) {
                                oberesRaster[i][j] = !oberesRaster[i][j];
                                if (unteresRaster[i][j] == 0) {
                                    gebeAlleUmliegendenNachbarnFrei(i, j);
                                }
                            }
                        }
                }
        }
    }
}
