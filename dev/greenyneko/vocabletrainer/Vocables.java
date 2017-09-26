package dev.greenyneko.vocabletrainer;

import android.icu.util.GregorianCalendar;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by this on 14.01.2017.
 */

public class Vocables implements Parcelable
{
    private ArrayList vocables;
    private Random random = new Random();

    public Vocables()
    {
        this.vocables = new ArrayList();
    }

    public void addVocable(String voc, String translation)
    {
        this.vocables.add(new Vocable(voc, translation));
    }

    public void addLoadedVocable(String voc, String translation, int correctness, int answers, GregorianCalendar date)
    {
        this.vocables.add(new Vocable(voc, translation, correctness, answers, date));
    }

    public int removeVocable(String voc, String translation)
    {
        Vocable vocable = this.getVocable(voc, translation);
        if(vocable != null)
        {
            this.vocables.remove(vocable);
            return 0;
        }
        return 1;
    }

    public Vocable getVocable(int pos)
    {
        Vocable voc = (Vocable)this.vocables.get(pos);
        return voc;
    }

    public Vocable getVocable(String name, String translation)
    {
        for(int i = 0; i < this.vocables.size(); i++)
        {
            Vocable vocable = (Vocable)this.vocables.get(i);
            Vocable vocableCompare = new Vocable(name, translation);
            if(this.vocablesEqual(vocable, vocableCompare)
                    && this.translationsEqual(vocable, vocableCompare))
            {
                return vocable;
            }
        }
        return null;
    }

    public Vocable getRandomVocable(boolean respectCorrectness)
    {
        if(respectCorrectness)
        {
            float minCorrectnessRate = 1.00f;
            ArrayList savedPositions = new ArrayList();
            for(int i = 0; i < vocables.size(); i++)
            {
                Vocable voc = (Vocable)this.vocables.get(i);
                if(voc.getCorrectnessRate() <= minCorrectnessRate)
                {
                    if(!voc.hasCooldown())
                    {
                        savedPositions.add(i);
                        minCorrectnessRate = voc.getCorrectnessRate();
                    }
                }
            }
            int randomPos = random.nextInt(savedPositions.size());
            return (Vocable)this.vocables.get((int)savedPositions.get(randomPos));
        }
        else
        {
            int randomPos = random.nextInt(this.vocables.size());
            return (Vocable)this.vocables.get(randomPos);
        }
    }

    public Vocable getRandomVocableWithout(boolean respectCorrectness, Vocable[] exp)
    {
        Vocable voc = getRandomVocable(respectCorrectness);
        boolean unique = false;
        while(!unique)
        {
            voc = getRandomVocable(respectCorrectness);
            unique = true;

            for(int i = 0; i < exp.length; i++)
            {
                if(vocablesEqual(voc, exp[i]) && translationsEqual(voc, exp[i]))
                {
                    unique = false;
                    break;
                }
            }
        }
        return voc;
    }

    public int getVocableAmount()
    {
        return this.vocables.size();
    }

    public boolean vocablesEqual(Vocable voc1, Vocable voc2)
    {
        if(voc2 == null && voc1 != null)
        {
            return false;
        }
        else if(voc1 == null && voc2 != null)
        {
            return false;
        }
        else if(voc1 == null &&  voc2 == null)
        {
            return true;
        }
        return voc1.getVocable().equalsIgnoreCase(voc2.getVocable());
    }

    public boolean translationsEqual(Vocable trans1, Vocable trans2)
    {
        if(trans2 == null && trans1 != null)
        {
            return false;
        }
        else if(trans1 == null && trans2 != null)
        {
            return false;
        }
        else if(trans1 == null &&  trans2 == null)
        {
            return true;
        }
        return trans1.getTranslation().equalsIgnoreCase(trans2.getTranslation());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.vocables.size());
        for(int i = 0; i < this.vocables.size(); i++)
        {
            dest.writeParcelable((Vocable)this.vocables.get(i), 0);
        }
    }

    public static final Parcelable.Creator<Vocables> CREATOR = new Parcelable.Creator<Vocables>()
    {
        public Vocables createFromParcel(Parcel in)
        {
            return new Vocables(in);
        }

        public Vocables[] newArray(int size)
        {
            return new Vocables[size];
        }
    };

    private Vocables(Parcel in)
    {
        this.vocables = new ArrayList();
        int size = in.readInt();
        for(int i = 0; i < size; i++)
        {
            this.vocables.add(in.readParcelable(getClass().getClassLoader()));
        }

    }
}
