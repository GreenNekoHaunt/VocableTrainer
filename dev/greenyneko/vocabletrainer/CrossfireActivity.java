package dev.greenyneko.vocabletrainer;

import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.format;

public class CrossfireActivity extends AppCompatActivity {
    private Vocables vocables = new Vocables();
    private Vocable currVoc;
    private Vocable[] answers = new Vocable[4];
    private int correctAnswer = 0;
    private TextView textViewCorrectness;
    private TextView textViewVocable;
    private TextView textViewCorrectnessDetail;
    private Button buttonAnswer0;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Button buttonContinue;
    private SQLHandler sqlHandler;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfire);
        sqlHandler = new SQLHandler(this);
        textViewCorrectness = (TextView) findViewById(R.id.textViewCorrectness);
        textViewVocable = (TextView) findViewById(R.id.textViewVocable);
        textViewCorrectnessDetail = (TextView) findViewById(R.id.textViewCorrectnessDetail);
        buttonAnswer0 = (Button) findViewById(R.id.buttonAnswer0);
        buttonAnswer1 = (Button) findViewById(R.id.buttonAnswer1);
        buttonAnswer2 = (Button) findViewById(R.id.buttonAnswer2);
        buttonAnswer3 = (Button) findViewById(R.id.buttonAnswer3);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        String currCategory = this.getIntent().getStringExtra("currCategory");
        String currLanguage = this.getIntent().getStringExtra("currLanguage");
        boolean askAll = this.getIntent().getBooleanExtra("askAll", false);

        Log.v("CrossfireActivity", "debug? hello?");

        if(askAll)
        {
            ArrayList vocs = sqlHandler.getVocablesInLanguage(currLanguage);
            for(int i = 0; i < vocs.size(); i++)
            {
                ArrayList vocArray = (ArrayList)vocs.get(i);
                for(int j = 0; j < vocArray.size(); j++)
                {
                    String[] voc = (String[]) vocArray.get(j);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(Date.valueOf(voc[4]));
                    Log.v("CrossfireActivity", voc[4]);
                    Log.v("CrossfireActivity", Date.valueOf(voc[4]).toString());
                    Log.v("CrossfireActivity", calendar.getTime().toString());
                    vocables.addLoadedVocable(voc[0], voc[1], Integer.parseInt(voc[2]), Integer.parseInt(voc[3]), (GregorianCalendar)calendar);
                }
            }
        }
        else
        {
            ArrayList vocs = sqlHandler.getVocablesInCategory(currCategory);
            for(int i = 0; i < vocs.size(); i++)
            {
                String[] voc = (String[])vocs.get(i);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(Date.valueOf(voc[4]));
                Log.v("CrossfireActivity", voc[4]);
                vocables.addLoadedVocable(voc[0], voc[1], Integer.parseInt(voc[2]), Integer.parseInt(voc[3]), (GregorianCalendar)calendar);
            }
        }
        this.onUpdate();

        buttonAnswer0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!answered)
                {
                    if(correctAnswer == 0)
                    {
                        currVoc.gotAnswered(true);
                        buttonAnswer0.setBackgroundColor(Color.rgb(120,255,120));
                        buttonContinue.setVisibility(View.VISIBLE);
                        answered = true;
                    }
                    else
                    {
                        currVoc.gotAnswered(false);
                        buttonAnswer0.setBackgroundColor(Color.rgb(255,120,120));
                    }
                    sqlHandler.updateVocable(currVoc.getVocable(), currVoc.getVocable(), currVoc.getTranslation(), currVoc.getAnswers(), currVoc.getCorrectness(), GregorianCalendar.getInstance().getTime().toString(), currVoc.getStreak());
                }

            }
        });

        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!answered)
                {
                    if(correctAnswer == 1)
                    {
                        currVoc.gotAnswered(true);
                        buttonAnswer1.setBackgroundColor(Color.rgb(120,255,120));
                        buttonContinue.setVisibility(View.VISIBLE);
                        answered = true;
                    }
                    else
                    {
                        currVoc.gotAnswered(false);
                        buttonAnswer1.setBackgroundColor(Color.rgb(255,120,120));
                    }
                    sqlHandler.updateVocable(currVoc.getVocable(), currVoc.getVocable(), currVoc.getTranslation(), currVoc.getAnswers(), currVoc.getCorrectness(), GregorianCalendar.getInstance().getTime().toString(), currVoc.getStreak());
                }
            }
        });

        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!answered)
                {
                    if(correctAnswer == 2)
                    {
                        currVoc.gotAnswered(true);
                        buttonAnswer2.setBackgroundColor(Color.rgb(120,255,120));
                        buttonContinue.setVisibility(View.VISIBLE);
                        answered = true;
                    }
                    else
                    {
                        currVoc.gotAnswered(false);
                        buttonAnswer2.setBackgroundColor(Color.rgb(255,120,120));
                    }
                    sqlHandler.updateVocable(currVoc.getVocable(), currVoc.getVocable(), currVoc.getTranslation(), currVoc.getAnswers(), currVoc.getCorrectness(), GregorianCalendar.getInstance().getTime().toString(), currVoc.getStreak());
                }
            }
        });

        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!answered)
                {
                    if(correctAnswer == 3)
                    {
                        currVoc.gotAnswered(true);
                        buttonAnswer3.setBackgroundColor(Color.rgb(120,255,120));
                        buttonContinue.setVisibility(View.VISIBLE);
                        answered = true;
                    }
                    else
                    {
                        currVoc.gotAnswered(false);
                        buttonAnswer3.setBackgroundColor(Color.rgb(255,120,120));
                    }
                    sqlHandler.updateVocable(currVoc.getVocable(), currVoc.getVocable(), currVoc.getTranslation(), currVoc.getAnswers(), currVoc.getCorrectness(), GregorianCalendar.getInstance().getTime().toString(), currVoc.getStreak());
                }
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                answered = false;
                hideAnswers();
                onUpdate();
                buttonContinue.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onUpdate()
    {
        Random random = new Random();

        currVoc = vocables.getRandomVocable(true);

        int randPos[] = new int[4];
        randPos[0] = random.nextInt(4);
        do {
            randPos[1] = random.nextInt(4);
        } while(randPos[1] == randPos[0]);
        do {
            randPos[2] = random.nextInt(4);
        } while(randPos[2] == randPos[1] || randPos[2] == randPos[0]);
        do {
            randPos[3] = random.nextInt(4);
        } while(randPos[3] == randPos[2] || randPos[3] == randPos[1] || randPos[3] == randPos[0]);

        correctAnswer = randPos[0];
        float correctnessRate = currVoc.getCorrectnessRate();
        if(correctnessRate > -1)
        {
            if(correctnessRate < 0.50)
            {
                textViewCorrectness.setTextColor(Color.RED);
            }
            else if(correctnessRate < 0.65)
            {
                textViewCorrectness.setTextColor(Color.YELLOW);
            }
            else if(correctnessRate < 0.90)
            {
                textViewCorrectness.setTextColor(Color.GREEN);
            }
            else
            {
                textViewCorrectness.setTextColor(Color.BLUE);
            }
            textViewCorrectnessDetail.setText("Correct: " + String.valueOf(currVoc.getCorrectness()) + " / Wrong: " + String.valueOf(currVoc.getAnswers() - currVoc.getCorrectness()));
            textViewCorrectness.setText(String.format("%.0f", correctnessRate*100.0f) + "%");

        }
        else
        {
            textViewCorrectnessDetail.setText("");
            textViewCorrectness.setTextColor(Color.DKGRAY);
            textViewCorrectness.setText(R.string.ui_text_new_vocable_tag);
        }


        answers[0] = null;
        answers[1] = null;
        answers[2] = null;
        answers[3] = null;
        answers[randPos[0]] = currVoc;
        answers[randPos[1]] = vocables.getRandomVocableWithout(false, answers);
        answers[randPos[2]] = vocables.getRandomVocableWithout(false, answers);
        answers[randPos[3]] = vocables.getRandomVocableWithout(false, answers);

        textViewVocable.setText(currVoc.getVocable());
        buttonAnswer0.setText(answers[0].getTranslation());
        buttonAnswer1.setText(answers[1].getTranslation());
        buttonAnswer2.setText(answers[2].getTranslation());
        buttonAnswer3.setText(answers[3].getTranslation());
        buttonAnswer3.setText(answers[3].getTranslation());
    }

    public void hideAnswers()
    {
        buttonAnswer0.setBackgroundColor(Color.LTGRAY);
        buttonAnswer1.setBackgroundColor(Color.LTGRAY);
        buttonAnswer2.setBackgroundColor(Color.LTGRAY);
        buttonAnswer3.setBackgroundColor(Color.LTGRAY);
    }
}
