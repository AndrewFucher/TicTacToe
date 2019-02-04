package com.example.mcandrii.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button[][] buttons = new Button[3][3];
    private Game game;

    public MainActivity() {
        game = new Game();
        game.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);

        buildGameField();
    }

    private void buildGameField() {
        Square[][] field = game.getField();
        for (int i = 0, lenI = field.length; i < lenI; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0, lenJ = field[i].length; j < lenJ; j++) {
                Button button = new Button(this);
                buttons[i][j] = button;
                button.setOnClickListener(new Listener(i, j));
                row.addView(button, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                button.setWidth(160);
                button.setHeight(160);
            }
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public class Listener implements View.OnClickListener {
        private int x = 0;
        private int y = 0;

        public Listener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void onClick(View view) {
            Button button = (Button) view;
            Game g = game;
            Player player = g.getCurrentActivePlayer();
            if (g.makeTurn(x, y)) {
                button.setText(player.getName());
            }
            Player winner = g.checkWinner();
            if (winner != null) {
                gameOver(winner);
            }
            if (g.isFieldFilled()) {  // в случае, если поле заполнено
                gameOver();
            }
        }

        private void gameOver(Player player) {
            CharSequence text = "Player \"" + player.getName() + "\" won!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            game.reset();
            refresh();
        }

        private void gameOver() {
            CharSequence text = "Draw";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            game.reset();
            refresh();
        }

        private void refresh() {
            Square[][] field = game.getField();

            for (int i = 0, len = field.length; i < len; i++) {
                for (int j = 0, len2 = field[i].length; j < len2; j++) {
                    if (field[i][j].getPlayer() == null) {
                        buttons[i][j].setText("");
                    } else {
                        buttons[i][j].setText(field[i][j].getPlayer().getName());
                    }
                }
            }
        }
    }
}