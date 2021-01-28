package com.kamikaze.eightqueen;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Pair> queenPosition = new ArrayList<>();
    public ArrayList<ArrayList<Pair>> positionRecord = new ArrayList<>();
    public TextView textView;
    public StringBuilder stringBuilder = new StringBuilder();
    public Pair disposePosition = null;
    public final int QUEEN_TOTAL = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result);
    }

    public void onClickStart(View view) {
        new Thread(new Runnable() {
            Handler handler = new Handler();

            @Override
            public void run() {
                calculate();
                handler.post(() -> {
                    showResult();
//                    Toast.makeText(getApplicationContext(), String.valueOf(positionRecord.size()), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void calculate() {
        for (int i = 0; i < QUEEN_TOTAL; i++) {
            Pair initPair = new Pair(i, 0);
            queenPosition.add(initPair);
            findNextPosition();
        }
    }

    private void findNextPosition() {
        int currentRow = 1;
        while (currentRow < QUEEN_TOTAL) {
            int queenAmountBeforeCalc = queenPosition.size();
            for (int x = 0; x < QUEEN_TOTAL; x++) {
                if (disposePosition == null) {
                    if (isValidPosition(x, currentRow)) {
                        addToQueenPosition(x, currentRow);
                        if (isAddRowNeeded()) {
                            currentRow++;
                            break;
                        }
                    }
                } else {
                    if (x > (int) (disposePosition.first)) {
                        disposePosition = null;
                        if (isValidPosition(x, currentRow)) {
                            addToQueenPosition(x, currentRow);
                            if (isAddRowNeeded()) {
                                currentRow++;
                                break;
                            }
                        }
                    }
                }
            }
            if (queenPosition.size() == queenAmountBeforeCalc) {
                disposePosition = queenPosition.get(queenPosition.size() - 1);
                queenPosition.remove(queenPosition.size() - 1);
                queenAmountBeforeCalc = queenPosition.size();
                if (queenAmountBeforeCalc == 0) {
                    disposePosition = null;
                    break;
                }
                currentRow--;
            }
        }
        if (queenPosition.size() == QUEEN_TOTAL) {
            addToSolution();
        }
        queenPosition.clear();
    }

    private void addToQueenPosition(int x, int currentRow) {
        Pair newPair = new Pair(x, currentRow);
        queenPosition.add(newPair);
    }

    private boolean isValidPosition(int x, int y) {
        for (int i = 0; i < queenPosition.size(); i++) {
            Pair target = queenPosition.get(i);
            float slope = (float) ((y - (int) target.second)) / (float) (x - (int) target.first);
            if ((int) target.first == x) {
                return false;
            } else if (slope == 1.0f ||
                    slope == -1.0f) {
                return false;
            }
        }
        return true;
    }

    private boolean isAddRowNeeded() {
        if (queenPosition.size() == QUEEN_TOTAL) {
            addToSolution();
            disposePosition = queenPosition.get(queenPosition.size() - 1);
            queenPosition.remove(queenPosition.size() - 1);
            return false;
        }
        return true;
    }

    private void addToSolution() {
        ArrayList<Pair> tempArray = new ArrayList<>(queenPosition);
        positionRecord.add(tempArray);
    }

    private void showResult() {
        for (int i = 0; i < positionRecord.size(); i++) {
            ArrayList<Pair> currentRecord = new ArrayList<>(positionRecord.get(i));
            for (int row = 0; row < QUEEN_TOTAL; row++) {
                for (int col = 0; col < QUEEN_TOTAL; col++) {
                    if ((int) currentRecord.get(row).first == col) {
                        stringBuilder.append("Q");
                    } else {
                        stringBuilder.append(".");
                    }
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append("==========================\n");
        }
        textView.setText(stringBuilder.toString());
    }
}