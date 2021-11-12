package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;

    static int prueba = 3;

    static int SIZE = 4;

    static TextView[][] cells = new TextView[SIZE][SIZE];
    static String[] cell_values = {"", "2", "4", "8", "16", "32", "64",
            "128", "256", "512", "1024", "2048"};
    static int[] cell_bg = {R.drawable.cell_empty, R.drawable.cell_2, R.drawable.cell_4,
            R.drawable.cell_8, R.drawable.cell_16, R.drawable.cell_32, R.drawable.cell_64,
            R.drawable.cell_128, R.drawable.cell_256, R.drawable.cell_512, R.drawable.cell_1024,
            R.drawable.cell_2048};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int first_cells = 0;

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


        generateCell(2);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    private void generateCell(int quantity) {

        cells[2][1].setText(cell_values[1]);
        cells[2][1].setBackgroundResource(cell_bg[1]);

        cells[2][3].setText(cell_values[1]);
        cells[2][3].setBackgroundResource(cell_bg[1]);
       /* int counter = 0;
        Random r = new Random();

        while (counter < quantity) {

            int r_1 = r.nextInt(SIZE);
            int r_2 = r.nextInt(SIZE);

            if (cells[r_1][r_2].getText() == "") {
                int r_3 = r.nextInt(2) + 1;

                cells[r_1][r_2].setText(cell_values[r_3]);
                cells[r_1][r_2].setBackgroundResource(cell_bg[r_3]);
                counter += 1;
            }

        }*/
    }

    private static int getIndex(String text) {
        for (int i = 0; i < cell_values.length; i++) {
            if (cell_values[i].contentEquals(text)) {
                return i;
            }
        }
        return 0;
    }

    private static void moveCell(int u_d, int l_r) {

        for (int i = SIZE - 1; i >= 0; i--) {


            if (i + l_r>= 0) {

                String cellText = (String) cells[2][i].getText();
                String next_cellText = (String) cells[2][i + l_r].getText();

                if (cellText == next_cellText && next_cellText != "") {

                    cells[2][i + l_r].setText(cell_values[getIndex(cellText) + 1]);
                    cells[2][i + l_r].setBackgroundResource(cell_bg[getIndex(cellText) + 1]);

                } else {
                    cells[2][i + l_r].setText(cell_values[getIndex(cellText)]);
                    cells[2][i + l_r].setBackgroundResource(cell_bg[getIndex(cellText)]);

                }

                cells[2][i].setText(cell_values[0]);
                cells[2][i].setBackgroundResource(cell_bg[0]);
            }
        }
        --prueba;
     /*   for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                //Comprobamos que no nos salimos del array
                if (i + u_d >= 0 && j + l_r >= 0 &&
                        i + u_d < SIZE && j + l_r < SIZE) {
                    CharSequence cellText = cells[i][j].getText();
                    CharSequence next_cellText = cells[i + u_d][j + l_r].getText();

                    if (cellText == next_cellText && next_cellText != "") {
                        cells[i + u_d][j + l_r].setText(cell_values[getIndex(cellText) + 1]);
                        cells[i + u_d][j + l_r].setBackgroundResource(cell_bg[getIndex(cellText) + 1]);

                        cells[i][j].setText(cell_values[0]);
                        cells[i][j].setBackgroundResource(cell_bg[0]);

                    } else if (next_cellText != "") {

                        cells[i + u_d][j + l_r].setText(cell_values[getIndex(cellText)]);
                        cells[i + u_d][j + l_r].setBackgroundResource(cell_bg[getIndex(cellText)]);

                        cells[i][j].setText(cell_values[0]);
                        cells[i][j].setBackgroundResource(cell_bg[0]);
                    }
                }
            }
        }*/

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
                    moveCell(-1, 0);

                    return Direction.up;

                } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                    Log.d(DEBUG_TAG, "onFling: right");
                    moveCell(0, 1);

                    return Direction.right;

                } else if (inRange(angle, 225, 315)) {
                    Log.d(DEBUG_TAG, "onFling: down");
                    moveCell(1, 0);

                    return Direction.down;

                } else {
                    Log.d(DEBUG_TAG, "onFling: left");
                    moveCell(0, -1);

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