package com.thecoredepository.mobile_rpg.dice;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_rpg.R;
import com.thecoredepository.mobile_rpg.dice.dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DiceActivity extends AppCompatActivity {

    int numberOfDie = 0;
    int valueOfDie = 0;
    long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        Intent intent = getIntent();
        String diceIn = intent.getExtras().getString("dice");

        numberOfDie = numberOfDie(diceIn);
        valueOfDie = valueOfDie(diceIn);

        Button btnRoll = findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            //Roll Dice
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime > 1000)
                {
                    TextView txtRolls = findViewById(R.id.txtRolls);
                    TextView txtTotal = findViewById(R.id.txtTotal);
                    EditText editAdv = findViewById(R.id.editAdv);
                    EditText editDis = findViewById(R.id.editDis);
                    txtRolls.setText("");
                    //Roll Dice
                    int roll = 0;
                    int d20 = 0;
                    List<Integer> die = new ArrayList<Integer>();
                    int total = 0;
                    int advantage = 0;
                    boolean disadvantage = false;
                    try {
                        advantage = Integer.parseInt(String.valueOf(editAdv.getText())) - Integer.parseInt(String.valueOf(editDis.getText()));
                    } catch (Exception e)
                    {
                        advantage = 0;
                    }
                    //Roll d20
                    do
                    {
                        roll = dice.d20();
                        d20 += roll;
                        txtRolls.setText(txtRolls.getText() + "Roll d20: " + roll +"\n");
                    } while (roll == 20);

                    numberOfDie = (advantage * 2);
                    if (numberOfDie <= 0)
                    {
                        numberOfDie = numberOfDie * -1;
                        disadvantage = true;
                    }
                    //Roll Other Dice
                    for (int i = 0; i < numberOfDie; i++)
                    {
                        do
                        {
                            if (valueOfDie == 2)
                            {
                                roll = dice.d2();
                            }
                            else if (valueOfDie == 6)
                            {
                                roll = dice.d6();
                            }
                            else if (valueOfDie == 8)
                            {
                                roll = dice.d8();
                            }
                            else //d10
                            {
                                roll = dice.d10();
                            }
                            die.add(roll);
                            txtRolls.setText(txtRolls.getText() + "Roll d"+valueOfDie+" : " + roll +"\n");
                        } while (roll == valueOfDie);
                    }

                    if (disadvantage == true)
                    {
                        Arrays.sort(die.toArray());
                        for(int i=0; i < (advantage * -1); i++)
                        {
                            txtRolls.setText(txtRolls.getText() + "Removed d"+valueOfDie+" : " + die.get(i) +"\n");
                            die.remove(i);
                        }
                    }
                    else if (disadvantage == false)
                    {
                        Arrays.sort(die.toArray());
                        for(int i=die.size(); i > (advantage); i--)
                        {
                            txtRolls.setText(txtRolls.getText() + "Removed d"+valueOfDie+" : " + die.get(i) +"\n");
                            die.remove(i);
                        }
                    }

                    //Add Total
                    for (int i = 0; i < die.size(); i++)
                    {
                        total += die.get(i);
                    }
                    total += d20;

                    //Display Total
                    txtTotal.setText("Total: " + total);

                    //Reset Clock
                    lastClickTime = SystemClock.elapsedRealtime();
                }
            }
        });
    }

    private int numberOfDie(String dice)
    {
        //Log.i("Dice", "" + Character.getNumericValue(dice.charAt(0)));
        return Character.getNumericValue(dice.charAt(0));
    }

    private int valueOfDie(String dice)
    {
        Log.i("Dice", "" + Character.getNumericValue(dice.charAt(2)));
        int value = Character.getNumericValue(dice.charAt(2));
        if (value == 1)
        {
            value = 10;
        }
        return value;
    }
}
