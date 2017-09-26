package dev.greenyneko.vocabletrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by this on 20.01.2017.
 */

public class SQLHandler extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "VocableTrainerData.db";

    public SQLHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqliteDatabase)
    {
        sqliteDatabase.execSQL("CREATE TABLE table_languages (IDlang INTEGER PRIMARY KEY, " +
                "Name TEXT)");
        sqliteDatabase.execSQL("CREATE TABLE table_categories (IDcat INTEGER PRIMARY KEY, " +
                "IDlang INTEGER, Name TEXT)");
        sqliteDatabase.execSQL("CREATE TABLE table_vocables (IDvoc INTEGER PRIMARY KEY, " +
                "IDcat INTEGER, vocable TEXT, translation TEXT, times INTEGER, correct INTEGER, " +
                "lastTime DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        if(oldVersion == 1 && newVersion == 2)
        {
            sqLiteDatabase.execSQL("ALTER TABLE table_vocables ADD streak INT");
        }
        //onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion == 2 && newVersion == 1)
        {
            db.execSQL("ALTER TABLE table_vocables DROP streak");
        }
        //onCreate(db);
    }

    public void addLanguage(String name)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        long newRowId = this.getWritableDatabase().insert("table_languages", null, contentValues);
    }

    public void addCategory(String name, String parentName)
    {
        String[] IDarray = new String[1];
        String[] parentArray = new String[1];
        IDarray[0] = "IDlang";
        parentArray[0] = parentName;
        Cursor cursor = this.getReadableDatabase().query("table_languages", IDarray,
                "Name = ?", parentArray, null, null, "IDlang DESC");
        cursor.moveToFirst();
        long parentId = cursor.getLong(cursor.getColumnIndexOrThrow("IDlang"));
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("IDlang", parentId);
        long newRowId = this.getWritableDatabase().insert("table_categories", null, contentValues);
        cursor.close();
    }

    public void createVocable(String vocable, String translation, String parentName)
    {
        String[] IDarray = new String[1];
        String[] parentArray = new String[1];
        IDarray[0] = "IDcat";
        parentArray[0] = parentName;
        Cursor cursor = this.getReadableDatabase().query("table_categories", IDarray,
                "Name = ?", parentArray, null, null, "IDcat DESC");
        cursor.moveToFirst();
        long parentId = cursor.getLong(cursor.getColumnIndexOrThrow("IDcat"));
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDcat", parentId);
        contentValues.put("vocable", vocable);
        contentValues.put("translation", translation);
        contentValues.put("times", 0);
        contentValues.put("correct", 0);
        contentValues.put("lastTime", GregorianCalendar.getInstance().getTimeInMillis());
        contentValues.put("streak", 0);
        long newRowId = this.getWritableDatabase().insert("table_vocables", null, contentValues);
        cursor.close();
    }

    public void updateLanguage(String oldName, String newName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", newName);

        String selection = "Name LIKE ?";
        String[] selectionArgs = { oldName };

        this.getReadableDatabase().update("table_languages", contentValues, selection, selectionArgs);
    }

    public void updateCategory(String oldName, String newName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", newName);

        String selection = "Name LIKE ?";
        String[] selectionArgs = { oldName };

        this.getReadableDatabase().update("table_categories", contentValues, selection, selectionArgs);
    }

    public void updateVocable(String oldVoc, String newVoc, String newTrans, int times, int correct, String date, int streak)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("vocable", newVoc);
        contentValues.put("translation", newTrans);
        if(times > 0)
        {
            contentValues.put("times", times);
        }
        if(correct > 0)
        {
            contentValues.put("correct", correct);
        }
        if(date != null)
        {
            contentValues.put("lastTime", date);
        }
        if(streak > 0)
        {
            contentValues.put("streak", streak);
        }

        String selection = "vocable LIKE ?";
        String[] selectionArgs = { oldVoc };

        this.getReadableDatabase().update("table_vocables", contentValues, selection, selectionArgs);
    }

    public void deleteLanguage(String name)
    {
        String[] selectionArgs = { name };
        this.getWritableDatabase().delete("table_languages", "Name LIKE ?", selectionArgs);
    }

    public void deleteCategory(String name)
    {
        String[] selectionArgs = { name };
        this.getWritableDatabase().delete("table_categories", "Name LIKE ?", selectionArgs);
    }

    public void deleteVocable(String vocable)
    {
        String[] selectionArgs = { vocable };
        this.getWritableDatabase().delete("table_vocables", "vocable LIKE ?", selectionArgs);
    }

    public ArrayList getVocablesInLanguage(String language)
    {
        ArrayList vocables = new ArrayList();
        ArrayList categoriesVocables;
        String[] IDarray = new String[1];
        String[] langArray = new String[1];
        String[] catName = new String[1];
        String[] langIdArray = new String[1];
        IDarray[0] = "IDlang";
        langArray[0] = language;
        Cursor cursor = this.getReadableDatabase().query("table_languages", IDarray,
                "Name = ?", langArray, null, null, "IDlang DESC");
        cursor.moveToFirst();
        long langId = cursor.getLong(cursor.getColumnIndexOrThrow("IDlang"));

        catName[0] = "Name";
        langIdArray[0] = String.valueOf(langId);
        cursor = this.getReadableDatabase().query("table_categories", catName, "IDlang = ?",
                langIdArray, null, null, "IDcat DESC");
        while(cursor.moveToNext())
        {
            categoriesVocables = new ArrayList(getVocablesInCategory(cursor.getString(cursor.getColumnIndexOrThrow("Name"))));
            vocables.add(categoriesVocables);
        }
        return vocables;
    }

    public ArrayList getVocablesInCategory(String category)
    {
        ArrayList vocables = new ArrayList();
        String[] IDarray = new String[1];
        String[] parentArray = new String[1];
        String[] vocable;
        IDarray[0] = "IDcat";
        Cursor cursorFillData;
        parentArray[0] = category;
        Cursor cursorSearchID = this.getReadableDatabase().query("table_categories", IDarray,
                "Name = ?", parentArray, null, null, "IDcat DESC");
        cursorSearchID.moveToFirst();
        long parentId = cursorSearchID.getLong(cursorSearchID.getColumnIndexOrThrow("IDcat"));
        String[] vocableInfo = new String[]{
            "vocable", "translation", "times", "correct", "lastTime", "streak"
        };
        parentArray[0] = String.valueOf(parentId);
        cursorSearchID.close();
        cursorFillData = this.getReadableDatabase().query("table_vocables", vocableInfo, "IDcat = ?",
                parentArray, null, null, "IDvoc");

        while(cursorFillData.moveToNext())
        {
            vocable = new String[6];
            vocable[0] = cursorFillData.getString(cursorFillData.getColumnIndexOrThrow("vocable"));
            vocable[1] = cursorFillData.getString(cursorFillData.getColumnIndexOrThrow("translation"));
            vocable[2] = String.valueOf(cursorFillData.getInt(cursorFillData.getColumnIndexOrThrow("times")));
            vocable[3] = String.valueOf(cursorFillData.getInt(cursorFillData.getColumnIndexOrThrow("correct")));
            vocable[4] = String.valueOf(cursorFillData.getString(cursorFillData.getColumnIndexOrThrow("lastTime")));
            vocable[5] = String.valueOf(cursorFillData.getString(cursorFillData.getColumnIndexOrThrow("streak")));
            vocables.add(vocable);
        }
        cursorFillData.close();
        return vocables;
    }

    public ArrayList getLanguages()
    {
        ArrayList languages = new ArrayList();
        String[] IDarray = new String[1];
        IDarray[0] = "Name";
        Cursor cursor = this.getReadableDatabase().rawQuery("select Name from table_languages ORDER BY IDlang ASC", null);
        while(cursor.moveToNext())
        {
            languages.add(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
        }
        return languages;
    }

    public ArrayList getCategories(String parentName)
    {
        ArrayList categories = new ArrayList();
        String[] IDarray = new String[1];
        String[] parentArray = new String[1];
        IDarray[0] = "IDlang";
        parentArray[0] = parentName;
        Cursor cursorSearchID = this.getReadableDatabase().query("table_languages", IDarray,
                "Name = ?", parentArray, null, null, "IDlang DESC");
        cursorSearchID.moveToFirst();
        long parentId = cursorSearchID.getLong(cursorSearchID.getColumnIndexOrThrow("IDlang"));

        String[] fieldArray = new String[1];
        String[] parentID = new String[1];
        parentID[0] = String.valueOf(parentId);
        fieldArray[0] = "Name";
        Cursor cursorData = this.getReadableDatabase().query("table_categories", fieldArray, "IDlang = ?", parentID, null, null, "IDcat ASC");
        while(cursorData.moveToNext())
        {
            categories.add(cursorData.getString(cursorData.getColumnIndexOrThrow("Name")));
        }
        return categories;
    }
}
