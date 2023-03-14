package hsos.ab_05;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private Timer timer = new Timer();
    private final int breite = 800;
    private final int hoehe = 800;
    private final int textSize = 50;
    int grenzeLinks = 30;
    int grenzeRechts = 770;
    int grenzeOben = 400;
    int grenzeUnten = 770;
    int ballRadius = 20;
    float ballX = 100f;
    float ballY = 700f;
    float velociteX = 0.3f;
    float velociteY = 4.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bitmap = Bitmap.createBitmap(this.breite, this.hoehe, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
        this.imageView = new ImageView(this);
        this.imageView.setImageBitmap(this.bitmap);
        this.paint = new Paint();
        setContentView(imageView);
        this.canvas.drawColor(Color.argb(255, 0, 0, 255));
        this.paint.setTextSize(textSize);
        this.halloWelt();
        this.textZentrieren("Hallo Hendrik, hallo Katharina!", 180);
        this.zeichneSmiley(110);
        this.timer.schedule( new TimerTask(){ @Override public void run() { derSpringendePunkt(); } } ,0,17 );
    }

    private void halloWelt() {
        String text = "Hallo Welt!";
        float textWidth = this.paint.measureText(text);
        this.paint.setColor(Color.WHITE);
        this.canvas.drawText(text, breite/2 - textWidth/2, 100, this.paint);
    }

    private void textZentrieren(String text, int y) {
        float textWidth = this.paint.measureText(text);
        this.paint.setColor(Color.WHITE);
        this.canvas.drawText(text, breite/2 - textWidth/2, y, this.paint);
    }

    public void zeichneSmiley(int radius) {
        this.paint.setColor(Color.GREEN);
        this.canvas.drawCircle(breite/2, 390, radius, this.paint);

        this.paint.setColor(Color.BLACK);
        this.canvas.drawCircle(breite/2 - 40, 350, radius / 10, this.paint);

        this.paint.setColor(Color.BLACK);
        this.canvas.drawCircle(breite/2 + 40, 350, radius / 10, this.paint);

        this.paint.setColor(Color.BLACK);
        this.canvas.drawLine(breite/2 - 40, 400, breite/2 + 40, 400, this.paint);
    }

    private void derSpringendePunkt() {
        this.paint.setColor(Color.BLUE);
        this.canvas.drawCircle(this.ballX, this.ballY, this.ballRadius, this.paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("MainActivity", LocalDateTime.now() + ": der springende Punkt");
        }

        if (this.ballX < this.grenzeLinks || this.ballX > this.grenzeRechts) {
            this.velociteX *= -1;
        }

        if(this.ballY < this.grenzeOben || this.ballY > this.grenzeUnten) {
            this.velociteY *= -1;
        }
        this.ballX += this.velociteX;
        this.ballY += this.velociteY;
        this.imageView.invalidate();

        this.paint.setColor(Color.YELLOW);
        this.canvas.drawCircle(this.ballX, this.ballY, this.ballRadius, this.paint);
    }
}