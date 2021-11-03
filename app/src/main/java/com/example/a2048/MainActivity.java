package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;

    TextView[][] cells = new TextView[4][4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int first_cells = 0;
boolean prueba = true;
        cells[0][0] = findViewById(R.id.cell_0);
        cells[0][1] = findViewById(R.id.cell_1);
        cells[0][2] = findViewById(R.id.cell_2);
        cells[0][3] = findViewById(R.id.cell_3);

        cells[1][0] = findViewById(R.id.cell_4);
        cells[1][1] = findViewById(R.id.cell_5);
        cells[1][2] = findViewById(R.id.cell_6);
        cells[1][3] = findViewById(R.id.cell_7);

        cells[2][0] = findViewById(R.id.cell_8);
        cells[2][1] = findViewById(R.id.cell_9);
        cells[2][2] = findViewById(R.id.cell_10);
        cells[2][3] = findViewById(R.id.cell_11);

        cells[3][0] = findViewById(R.id.cell_12);
        cells[3][1] = findViewById(R.id.cell_13);
        cells[3][2] = findViewById(R.id.cell_14);
        cells[3][3] = findViewById(R.id.cell_15);


                        cells[0][3].setText("2");
                        cells[0][3].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cell_64, null));



        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    // GESTURE DETECTION

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    static class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            float x1 = event1.getX();
            float y1 = event1.getY();

            float x2 = event2.getX();
            float y2 = event2.getY();


            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());

            Direction direction = getDirection(x1, y1, x2, y2);
            return onSwipe(direction);
        }

        public boolean onSwipe(Direction direction) {
            return false;
        }

        public enum Direction {
            up,
            down,
            left,
            right;

            public static Direction fromAngle(double angle) {
                if (inRange(angle, 45, 135)) {
                    Log.d(DEBUG_TAG, "onFling: up");
                    return Direction.up;
                } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                    Log.d(DEBUG_TAG, "onFling: right");
                    return Direction.right;
                } else if (inRange(angle, 225, 315)) {
                    Log.d(DEBUG_TAG, "onFling: down");
                    return Direction.down;
                } else {
                    Log.d(DEBUG_TAG, "onFling: left");
                    return Direction.left;
                }

            }

            private static boolean inRange(double angle, float init, float end) {
                return (angle >= init) && (angle < end);
            }
        }

        public double getAngle(float x1, float y1, float x2, float y2) {

            double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
            return (rad * 180 / Math.PI + 180) % 360;
        }

        public Direction getDirection(float x1, float y1, float x2, float y2) {
            double angle = getAngle(x1, y1, x2, y2);
            return Direction.fromAngle(angle);
        }
    }

}