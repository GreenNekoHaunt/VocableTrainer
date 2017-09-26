package dev.greenyneko.vocabletrainer;

import android.icu.util.GregorianCalendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Date;

/**
 * Created by this on 14.01.2017.
 */

public class Vocable implements Parcelable
{
    private String vocable;
    private String translation;
    private String[] fakeTranslations;
    private boolean fakeTranslation;
    private int correctness;
    private int answers;
    private int correctnessStreak;
    private GregorianCalendar date;

    public Vocable(String voc, String translation)
    {
        this.vocable = voc;
        this.translation = translation;
        this.fakeTranslation = false;
        this.correctness = 0;
    }

    public Vocable(String voc, String translation, int correctness, int answers, GregorianCalendar date)
    {
        this.vocable = voc;
        this.translation = translation;
        this.fakeTranslation = false;
        this.correctness = correctness;
        this.answers = answers;
        this.date = date;
    }

    public Vocable(String voc, String translation, String fake1, String fake2, String fake3)
    {
        this.vocable = voc;
        this.translation = translation;
        this.fakeTranslations[0] = fake1;
        this.fakeTranslations[1] = fake2;
        this.fakeTranslations[2] = fake3;
        this.fakeTranslation = true;
        this.correctness = 0;
    }

    public boolean hasFakeTranslations()
    {
        return this.fakeTranslation;
    }

    public String getFakeTranslation(int num)
    {
        return this.fakeTranslations[num];
    }

    public String getVocable()
    {
        return this.vocable;
    }

    public String getTranslation()
    {
        return this.translation;
    }

    public int getAnswers()
    {
        return this.answers;
    }

    public int getCorrectness()
    {
        return this.correctness;
    }

    public int getStreak()
    {
        return this.correctnessStreak;
    }

    public float getCorrectnessRate()
    {
        if(this.answers > 0)
        {
            float rate = (float)this.correctness / (float)this.answers;
            if(rate > 1)
            {
                return 1.00f;
            }
            else
            {
                return rate;
            }
        }
       return -1;
    }

    public boolean hasCooldown()
    {


        if(0 > this.date.compareTo(GregorianCalendar.getInstance()))
        {
            Log.v("Vocable", "has no cooldown");
            return false;
        }
        Log.v("Vocable", "has cooldown");
        return true;
    }

    public void setVocable(String voc, String translation)
    {
        if(voc != null)
        {
            this.vocable = voc;
        }
        if(translation != null)
        {
            this.translation = translation;
        }
    }

    public void gotAnswered(boolean correctly)
    {
        if(correctly)
        {
            this.correctness++;
            this.correctnessStreak++;
        }
        else
        {
            this.correctnessStreak = 1;
        }
        this.answers++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.vocable);
        dest.writeString(this.translation);
        dest.writeInt(this.correctness);
    }

    public static final Parcelable.Creator<Vocable> CREATOR = new Parcelable.Creator<Vocable>()
    {
        public Vocable createFromParcel(Parcel in)
        {
            return new Vocable(in);
        }

        public Vocable[] newArray(int size)
        {
            return new Vocable[size];
        }
    };

    private Vocable(Parcel in)
    {
        this.vocable = in.readString();
        this.translation = in.readString();
        this.correctness = in.readInt();
        this.fakeTranslation = false;
    }
}
