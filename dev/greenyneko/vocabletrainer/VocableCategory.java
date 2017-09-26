package dev.greenyneko.vocabletrainer;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by this on 14.01.2017.
 */

public class VocableCategory
{
    private VocableLanguage parent;
    private Vocables vocables = new Vocables();
    private LinearLayout layout;
    private Button button;
    private String name;

    public VocableCategory(Context context, String name, VocableLanguage parent)
    {
        this.parent = parent;
        this.button = new Button(context);
        this.name = name;
        this.layout = new LinearLayout(context);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.button.setText(name);
    }

    public String getName()
    {
        return this.name;
    }

    public VocableLanguage getParent()
    {
        return this.parent;
    }

    public void addVocable(String voc, String trans)
    {
        vocables.addVocable(voc, trans);
    }

    public void deleteVocable(String voc, String trans)
    {
        vocables.removeVocable(voc, trans);
    }

    public Vocables getVocables()
    {
        return this.vocables;
    }

    public LinearLayout getLayout()
    {
        return this.layout;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
