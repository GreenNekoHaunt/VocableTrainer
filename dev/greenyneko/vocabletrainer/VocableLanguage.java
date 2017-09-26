package dev.greenyneko.vocabletrainer;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by this on 15.01.2017.
 */

public class VocableLanguage
{
    private ArrayList child = new ArrayList();
    private Vocables vocables = new Vocables();
    private LinearLayout layout;
    private Button button;
    private String name;

    public VocableLanguage(Context context, String name)
    {
        this.button = new Button(context);
        this.name = name;
        this.layout = new LinearLayout(context);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.button.setText(name);
    }

    public void addCategory(VocableCategory child)
    {
        this.child.add(child);
    }

    public void deleteCategory(VocableCategory child)
    {
        this.child.remove(child);
    }

    public String getName()
    {
        return this.name;
    }

    public LinearLayout getLayout()
    {
        return this.layout;
    }

    public VocableCategory getChild(int index)
    {
        return (VocableCategory)child.get(index);
    }

    public Vocables getAllVocables()
    {
        for(int i = 0; i < this.getChildAmount(); i++)
        {
            Vocables currVocables = this.getChild(i).getVocables();
            for(int j = 0; j < currVocables.getVocableAmount(); j++)
            {
                Vocable currVocable = currVocables.getVocable(j);
                this.vocables.addVocable(currVocable.getVocable(), currVocable.getTranslation());
            }
        }
        return this.vocables;
    }

    public int getChildAmount()
    {
        return this.child.size();
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
