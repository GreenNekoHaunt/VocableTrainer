package dev.greenyneko.vocabletrainer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ScrollView scrollViewLanguages;
    private LinearLayout linearLayoutLanguages;
    private PopupWindow popupWindow;
    private boolean editMode = false;
    private TextView textViewWindow[] = new  TextView[2];
    private EditText editTextWindow[] = new EditText[2];
    private Button buttonWindow[] = new Button[2];
    private LinearLayout windowLayout;
    private LinearLayout windowLayoutButtons;
    private FloatingActionButton fab;
    private int viewType;
    private VocableLanguage currLanguage;
    private VocableCategory currCategory;
    private Vocable currVocable;
    private LinearLayout currLayout;
    private ArrayList<VocableLanguage> vocableLanguages = new ArrayList<>();
    private SQLHandler sqlHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewType = 0;


        sqlHandler = new SQLHandler(this);
        popupWindow = new PopupWindow(this);
        windowLayout = new LinearLayout(this);
        windowLayoutButtons = new LinearLayout(this);
        textViewWindow[0] = new TextView(this);
        textViewWindow[1] = new TextView(this);
        editTextWindow[0] = new EditText(this);
        editTextWindow[1] = new EditText(this);
        buttonWindow[0] = new Button(this);
        buttonWindow[1] = new Button(this);
        ViewGroup.LayoutParams windowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams editTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextWindow[0].setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextWindow[0].setLayoutParams(editTextParams);
        editTextWindow[0].setText("");
        editTextWindow[1].setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextWindow[1].setLayoutParams(editTextParams);
        editTextWindow[1].setText("");
        buttonWindow[0].setText(R.string.button_add);
        buttonWindow[1].setText(R.string.button_cancel);
        windowLayout.setOrientation(LinearLayout.VERTICAL);
        windowLayout.addView(textViewWindow[0], windowParams);
        windowLayout.addView(editTextWindow[0], editTextParams);
        windowLayout.addView(textViewWindow[1], windowParams);
        windowLayout.addView(editTextWindow[1], editTextParams);
        windowLayout.removeView(textViewWindow[1]);
        windowLayout.removeView(editTextWindow[1]);
        windowLayoutButtons.setOrientation(LinearLayout.HORIZONTAL);
        windowLayoutButtons.addView(buttonWindow[0], windowParams);
        windowLayoutButtons.addView(buttonWindow[1], windowParams);
        popupWindow.setContentView(windowLayout);
        popupWindow.setOutsideTouchable(false);
        scrollViewLanguages = (ScrollView) findViewById(R.id.scrollViewLanguages);
        linearLayoutLanguages = (LinearLayout) findViewById(R.id.linearLayoutLanguages);
        currLayout = linearLayoutLanguages;
        fab = (FloatingActionButton) findViewById(R.id.fab);

        loadSave();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                buttonWindow[0].setText(R.string.button_add);
                switch(viewType)
                {
                    case 1:
                        editMode = false;
                        windowLayout.removeView(textViewWindow[1]);
                        windowLayout.removeView(editTextWindow[1]);
                        windowLayout.addView(windowLayoutButtons);
                        textViewWindow[0].setText(R.string.ui_text_name_new_category);
                        editTextWindow[0].setText("");
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                        popupWindow.update(160, 100, 480, 280);
                        break;
                    case 2:
                        editMode = false;
                        windowLayout.removeView(textViewWindow[1]);
                        windowLayout.removeView(editTextWindow[1]);
                        windowLayout.removeView(windowLayoutButtons);
                        windowLayout.addView(textViewWindow[1]);
                        windowLayout.addView(editTextWindow[1]);
                        windowLayout.addView(windowLayoutButtons);
                        textViewWindow[0].setText(R.string.ui_text_name_new_vocable);
                        textViewWindow[1].setText(R.string.ui_text_name_new_translation);
                        editTextWindow[0].setText("");
                        editTextWindow[1].setText("");
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                        popupWindow.update(160, 100, 480, 480);
                        break;
                    default:
                        editMode = false;
                        windowLayout.removeView(textViewWindow[1]);
                        windowLayout.removeView(editTextWindow[1]);
                        windowLayout.addView(windowLayoutButtons);
                        textViewWindow[0].setText(R.string.ui_text_name_new_language);
                        editTextWindow[0].setText("");
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                        popupWindow.update(160, 100, 480, 280);
                }
            }
        });

        buttonWindow[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                switch(viewType)
                {
                    case 1:
                        if(editMode)
                        {
                            String newCat = editTextWindow[0].getText().toString();
                            if(!newCat.equals(""))
                            {
                                for(int i = 0; i < currLayout.getChildCount(); i++)
                                {
                                    if(currLayout.getChildAt(i) instanceof Button)
                                    {
                                        Button button = (Button)currLayout.getChildAt(i);
                                        if(button.getText().equals(currCategory.getName()))
                                        {
                                            button.setText(newCat);
                                        }
                                    }
                                }
                                sqlHandler.updateCategory(currCategory.getName(), newCat);
                                popupWindow.dismiss();
                            }
                        }
                        else
                        {
                            String newCategory = editTextWindow[0].getText().toString();
                            if (!newCategory.equals("")) {
                                addCategory(newCategory, false);
                                sqlHandler.addCategory(newCategory, currLanguage.getName());
                                popupWindow.dismiss();
                            }
                        }
                        break;
                    case 2:
                        if(editMode)
                        {
                            String newVocable = editTextWindow[0].getText().toString();
                            String newTranslation = editTextWindow[1].getText().toString();
                            if(!newVocable.equals("") && !newTranslation.equals(""))
                            {
                                for(int i = 0; i < currLayout.getChildCount(); i++)
                                {
                                    if(currLayout.getChildAt(i) instanceof Button)
                                    {
                                        Button button = (Button)currLayout.getChildAt(i);
                                        if(button.getText().equals(currVocable.getVocable() + " - " + currVocable.getTranslation()))
                                        {
                                            button.setText(newVocable + " - " + newTranslation);
                                        }
                                    }
                                }
                                sqlHandler.updateVocable(currVocable.getVocable(), newVocable, newTranslation, -1, -1, null, -1);
                                currVocable.setVocable(newVocable, newTranslation);
                                windowLayout.removeView(textViewWindow[1]);
                                windowLayout.removeView(editTextWindow[1]);
                                popupWindow.dismiss();
                            }
                        }
                        else
                        {
                            String newVocable = editTextWindow[0].getText().toString();
                            String newTranslation = editTextWindow[1].getText().toString();
                            if (!newVocable.equals("") && !newTranslation.equals("")) {
                                addVocable(newVocable, newTranslation, false);
                                sqlHandler.createVocable(newVocable, newTranslation, currCategory.getName());
                                popupWindow.dismiss();
                                windowLayout.removeView(textViewWindow[1]);
                                windowLayout.removeView(editTextWindow[1]);
                            }
                        }
                        break;
                    default:
                        if(editMode)
                        {
                            String newLang = editTextWindow[0].getText().toString();
                            if(!newLang.equals(""))
                            {
                                for(int i = 0; i < currLayout.getChildCount(); i++)
                                {
                                    if(currLayout.getChildAt(i) instanceof Button)
                                    {
                                        Button button = (Button)currLayout.getChildAt(i);
                                        if(button.getText().equals(currLanguage.getName()))
                                        {
                                            button.setText(newLang);
                                        }
                                    }
                                }
                                sqlHandler.updateLanguage(currLanguage.getName(), newLang);
                                currLanguage.setName(newLang);
                                popupWindow.dismiss();
                            }
                        }
                        else
                        {
                            String newLanguage = editTextWindow[0].getText().toString();
                            if (!newLanguage.equals("")) {
                                popupWindow.dismiss();
                                sqlHandler.addLanguage(newLanguage);
                                addLanguage(newLanguage, false);
                            }
                        }
                }
                windowLayout.removeView(windowLayoutButtons);
                windowLayout.removeView(windowLayoutButtons);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            public void onDismiss()
            {
                if (viewType == 2)
                {
                    windowLayout.removeView(textViewWindow[1]);
                    windowLayout.removeView(editTextWindow[1]);
                }
                windowLayout.removeView(windowLayoutButtons);
                popupWindow.dismiss();
            }
        });

        buttonWindow[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });
    }

    public void addLanguage(final String name, boolean loadFlag)
    {
        final Button newElement = new Button(this);
        final Button askAll = new Button(this);
        final PopupMenu popupMenu = new PopupMenu(this, newElement);
        final Intent intent = new Intent(this, CrossfireActivity.class);
        final VocableLanguage newLanguage = new VocableLanguage(this, name);
        popupMenu.getMenu().add("Delete");
        popupMenu.getMenu().add("Edit");
        askAll.setText(R.string.button_ask_all);
        vocableLanguages.add(newLanguage);
        newElement.setAllCaps(false);
        newElement.setText(name);
        newLanguage.getLayout().addView(askAll);
        currLayout.addView(newElement);

        askAll.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view)
           {
                if(currLanguage.getAllVocables().getVocableAmount() >= 4)
                {
                    intent.putExtra("currLanguage", currLanguage.getName());
                    intent.putExtra("currCategory", currCategory.getName());
                    intent.putExtra("askAll", true);
                    startActivity(intent);
                }
                else
                {
                    showVocableMinWarning();
                }

           }
        });

        newElement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                currLanguage = newLanguage;
                popupMenu.show();
                return false;
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == "Edit")
                {
                    editMode = true;
                    windowLayout.removeView(textViewWindow[1]);
                    windowLayout.removeView(editTextWindow[1]);
                    windowLayout.addView(windowLayoutButtons);
                    buttonWindow[0].setText(R.string.button_apply);
                    textViewWindow[0].setText("Rename '" + currLanguage.getName() + "'");
                    editTextWindow[0].setText(currLanguage.getName());
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                    popupWindow.update(160, 100, 480, 280);
                }
                else if(item.getTitle() == "Delete")
                {
                    sqlHandler.deleteLanguage(newLanguage.getName());
                    vocableLanguages.remove(newLanguage);
                    currLayout.removeView(newElement);
                }
                return false;
            }
        });

        newElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                scrollViewLanguages.removeView(currLayout);
                scrollViewLanguages.addView(newLanguage.getLayout());
                currLayout = newLanguage.getLayout();
                currLanguage = newLanguage;
                viewType = 1;
            }
        });

        if(loadFlag)
        {
            newElement.callOnClick();
        }
    }

    public void addCategory(String name, boolean loadFlag)
    {
        final Button newElement = new Button(this);
        final VocableCategory newCategory = new VocableCategory(this, name, currLanguage);
        final Button askCategory = new Button(this);
        final PopupMenu popupMenu = new PopupMenu(this, newElement);
        final Intent intent = new Intent(this, CrossfireActivity.class);
        popupMenu.getMenu().add("Delete");
        popupMenu.getMenu().add("Edit");
        askCategory.setText(R.string.button_ask_category);
        currLanguage.addCategory(newCategory);
        newElement.setAllCaps(false);
        newElement.setText(name);
        newCategory.getLayout().addView(askCategory);
        currLayout.addView(newElement);

        askCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(currCategory.getVocables().getVocableAmount() >= 4) {
                    intent.putExtra("currLanguage", currLanguage.getName());
                    intent.putExtra("currCategory", currCategory.getName());
                    intent.putExtra("askAll", false);
                    startActivity(intent);
                }
                else
                {
                    showVocableMinWarning();
                }
            }
        });

        newElement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                currCategory = newCategory;
                popupMenu.show();
                return false;
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == "Edit")
                {
                    editMode = true;
                    windowLayout.removeView(textViewWindow[1]);
                    windowLayout.removeView(editTextWindow[1]);
                    windowLayout.addView(windowLayoutButtons);
                    buttonWindow[0].setText(R.string.button_apply);
                    textViewWindow[0].setText("Rename '" + currCategory.getName() + "'");
                    editTextWindow[0].setText(currCategory.getName());
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                    popupWindow.update(160, 100, 480, 280);
                }
                else if(item.getTitle() == "Delete")
                {
                    sqlHandler.deleteCategory(newCategory.getName());
                    newCategory.getParent().deleteCategory(newCategory);
                    currLayout.removeView(newElement);
                }
                return false;
            }
        });

        newElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                scrollViewLanguages.removeView(currLayout);
                scrollViewLanguages.addView(newCategory.getLayout());
                currLayout = newCategory.getLayout();
                currCategory = newCategory;
                viewType = 2;
            }
        });

        if(loadFlag)
        {
            newElement.callOnClick();
        }
    }

    public void addVocable(String voc, String trans, boolean loadFlag)
    {
        final Button newElement = new Button(this);
        final PopupMenu popupMenu = new PopupMenu(this, newElement);
        final String vocable = voc;
        final String translation = trans;
        popupMenu.getMenu().add("Delete");
        popupMenu.getMenu().add("Edit");
        newElement.setAllCaps(false);
        newElement.setText(vocable + " - " + translation);
        currCategory.addVocable(vocable, translation);

        newElement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                currLanguage = currCategory.getParent();
                currVocable = currCategory.getVocables().getVocable(vocable, translation);
                popupMenu.show();
                return false;
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if(item.getTitle() == "Edit")
                {
                    editMode = true;
                    windowLayout.addView(textViewWindow[1]);
                    windowLayout.addView(editTextWindow[1]);
                    windowLayout.addView(windowLayoutButtons);
                    buttonWindow[0].setText(R.string.button_apply);
                    textViewWindow[0].setText(R.string.ui_text_modify_vocable);
                    textViewWindow[1].setText(R.string.ui_text_modify_translation);
                    editTextWindow[0].setText(vocable);
                    editTextWindow[1].setText(translation);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(scrollViewLanguages, Gravity.CENTER_HORIZONTAL, 10, 10);
                    popupWindow.update(160, 100, 480, 480);
                }
                else if(item.getTitle() == "Delete")
                {
                    sqlHandler.deleteVocable(vocable);
                    currCategory.deleteVocable(vocable, translation);
                    currLayout.removeView(newElement);
                }
                return false;
            }
        });

        newElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        currLayout.addView(newElement);
        if(loadFlag)
        {
            currLanguage = currCategory.getParent();
            currVocable = currCategory.getVocables().getVocable(vocable, translation);
        }
    }

    public void onBackPressed()
    {
        if(viewType == 1)
        {
            scrollViewLanguages.removeView(currLayout);
            currLayout = linearLayoutLanguages;
            scrollViewLanguages.addView(currLayout);
            viewType = 0;
        }
        if(viewType == 2) {
            scrollViewLanguages.removeView(currLayout);
            currLayout = currCategory.getParent().getLayout();
            scrollViewLanguages.addView(currLayout);
            viewType = 1;
        }
    }

    public void showVocableMinWarning()
    {
        Toast.makeText(this, "Asking requires at least 4 vocables.", Toast.LENGTH_LONG).show();
    }

    public void loadSave()
    {
        ArrayList languages = sqlHandler.getLanguages();

        for(int i = 0; i < languages.size(); i++)
        {
            addLanguage((String)languages.get(i), true);

            ArrayList categories = sqlHandler.getCategories((String)languages.get(i));
            for(int j = 0; j < categories.size(); j++)
            {
                addCategory((String)categories.get(j), true);

                ArrayList vocables = sqlHandler.getVocablesInCategory((String)categories.get(j));
                for(int k = 0; k < vocables.size(); k++)
                {
                    String[] vocs = (String[])vocables.get(k);
                    addVocable(vocs[0], vocs[1], true);
                }
                this.onBackPressed();
            }
            this.onBackPressed();
        }
    }
}
