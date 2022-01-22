package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;

    static int SIZE = 4;

    static TextView[][] cells = new TextView[SIZE][SIZE];
    static String[] cell_values = {"", "2", "4", "8", "16", "32", "64",
            "128", "256", "512", "1024", "2048"};
    static int[] cell_bg = {R.drawable.cell_empty, R.drawable.cell_2, R.drawable.cell_4,
            R.drawable.cell_8, R.drawable.cell_16, R.drawable.cell_32, R.drawable.cell_64,
            R.drawable.cell_128, R.drawable.cell_256, R.drawable.cell_512, R.drawable.cell_1024,
            R.drawable.cell_2048};

    static String lastCombined = "";

    static int impossibleMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        getSupportActionBar().hide();
    }

    private static void checkPossibleMovements() {

        int movesUp = 0;
        int movesLeft = 0;
        int movesRight = 0;
        int movesDown = 0;

        int movesTotal = 4;

        //Checks all impossible moves Up and Left. If there are 48 impossible moves, you can move in
        //that direction
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    impossibleMoves = 0;
                    checkMovement(i, j, -1, 0, true);
                    movesUp += impossibleMoves;
                    impossibleMoves = 0;
                    checkMovement(i, j, 0, -1, true);
                    movesLeft += impossibleMoves;
                }
            }
        }

        //Checks all impossible moves Down and Rigth. If there are 48 impossible moves, you can move
        //in that direction
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = SIZE - 1; j >= 0; j--) {
                for (int k = SIZE - 1; k >= 0; k--) {
                    impossibleMoves = 0;
                    checkMovement(i, j, 1, 0, true);
                    movesDown += impossibleMoves;
                    impossibleMoves = 0;
                    checkMovement(i, j, 0, 1, true);
                    movesRight += impossibleMoves;
                }
            }
        }

        if (movesDown == 48) movesTotal -= 1;
        if (movesRight == 48) movesTotal -= 1;
        if (movesUp == 48) movesTotal -= 1;
        if (movesLeft == 48) movesTotal -= 1;

        //TODO Make a lose message window or change to a different activity here
        if (movesTotal == 0) System.out.println("You lose");
    }

    private static void generateCell(int quantity) {
        int emptyCells = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cells[i][j].getText() == "") {
                    emptyCells += 1;
                }
            }
        }

        if (emptyCells >= 1) {
            int counter = 0;
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
            }
        } else {
            checkPossibleMovements();
        }

    }

    private static int getIndex(String text) {
        for (int i = 0; i < cell_values.length; i++) {
            if (cell_values[i].contentEquals(text)) {
                return i;
            }
        }
        return 0;
    }

    private static boolean checkBounds(int x, int y, int vertical, int horizontal) {

        //left
        if (horizontal == -1) {
            return y < 0;
        }

        //right
        else if (horizontal == 1) {
            return y >= SIZE;
        }

        //up
        else if (vertical == -1) {
            return x < 0;
        }

        //down
        else if (vertical == 1) {
            return x >= SIZE;
        }
        return false;

    }

    private static void checkMovement(int x, int y, int vertical, int horizontal, boolean check) {
        boolean canMove = true;

        int newx = x;
        int newy = y;

        String cellText = (String) cells[x][y].getText();

        if (cellText == "") canMove = false;

        while (canMove) {
            newx += vertical;
            newy += horizontal;

            //Check if the next cell is outside bounds
            if (checkBounds(newx, newy, vertical, horizontal)) break;

            String newText = (String) cells[newx][newy].getText();

            if (newText == "") {
                if (!check) {
                    cells[newx][newy].setText(cell_values[getIndex(cellText)]);
                    cells[newx][newy].setBackgroundResource(cell_bg[getIndex(cellText)]);

                    cells[newx - vertical][newy - horizontal].setText(cell_values[0]);
                    cells[newx - vertical][newy - horizontal].setBackgroundResource(cell_bg[0]);
                }
                impossibleMoves = 0;

            } else if (newText == cellText && cellText != lastCombined) {
                if (!check) {
                    cells[newx][newy].setText(cell_values[getIndex(cellText) + 1]);
                    cells[newx][newy].setBackgroundResource(cell_bg[getIndex(cellText) + 1]);

                    cells[newx - vertical][newy - horizontal].setText(cell_values[0]);
                    cells[newx - vertical][newy - horizontal].setBackgroundResource(cell_bg[0]);

                    lastCombined = cell_values[getIndex(cellText) + 1];
                }
                canMove = false;
                impossibleMoves = 0;
            } else {
                impossibleMoves = 1;
                canMove = false;
            }

        }
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

                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            for (int k = 0; k < SIZE; k++) {
                                checkMovement(i, j, -1, 0, false);
                            }
                        }
                    }

                    generateCell(1);
                    lastCombined = "";

                    return Direction.up;

                } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                    Log.d(DEBUG_TAG, "onFling: right");

                    for (int i = SIZE - 1; i >= 0; i--) {
                        for (int j = SIZE - 1; j >= 0; j--) {
                            for (int k = SIZE - 1; k >= 0; k--) {
                                checkMovement(i, j, 0, 1, false);
                            }
                        }
                    }

                    generateCell(1);
                    lastCombined = "";

                    return Direction.right;

                } else if (inRange(angle, 225, 315)) {
                    Log.d(DEBUG_TAG, "onFling: down");

                    for (int i = SIZE - 1; i >= 0; i--) {
                        for (int j = SIZE - 1; j >= 0; j--) {
                            for (int k = SIZE - 1; k >= 0; k--) {
                                checkMovement(i, j, 1, 0, false);
                            }
                        }
                    }

                    generateCell(1);
                    lastCombined = "";

                    return Direction.down;

                } else {
                    Log.d(DEBUG_TAG, "onFling: left");

                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            for (int k = 0; k < SIZE; k++) {
                                checkMovement(i, j, 0, -1, false);
                            }
                        }
                    }

                    generateCell(1);
                    lastCombined = "";

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