package com.cellgroup.cellapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cellgroup.cellapp.database.CellAppDbSchema.AnimationBackgroundTable;
import com.cellgroup.cellapp.database.CellAppDbSchema.AnimationItemTable;
import com.cellgroup.cellapp.database.CellAppDbSchema.DataVersionTable;
import com.cellgroup.cellapp.database.CellAppDbSchema.DocumentTable;
import com.cellgroup.cellapp.database.CellAppDbSchema.StepTable;
import com.cellgroup.cellapp.database.CellAppDbSchema.TopicTable;
import com.cellgroup.cellapp.models.AnimationBackground;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cellApp.db";

    public DatabaseHelper (Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AnimationBackgroundTable.NAME +
                "(" +
                    AnimationBackgroundTable.Cols.ID  + "TEXT PRIMARY KEY," +
                    AnimationBackgroundTable.Cols.IMAGE_URL + "TEXT," +
                    AnimationBackgroundTable.Cols.STEP_ID + "TEXT," +
                    "FOREIGN KEY (" + AnimationBackgroundTable.Cols.STEP_ID + ")" +
                    "REFERENCES " + StepTable.NAME + " (" + StepTable.Cols.ID + ")" +
                ")"
        );
        db.execSQL("create table " + AnimationItemTable.NAME +
                "(" +
                    AnimationItemTable.Cols.ID  + "TEXT PRIMARY KEY," +
                    AnimationItemTable.Cols.END_POSITION_X + "INTEGER," +
                    AnimationItemTable.Cols.END_POSITION_Y + "INTEGER," +
                    AnimationItemTable.Cols.IMAGE_URL  + "TEXT," +
                    AnimationItemTable.Cols.START_POSITION_X + "INTEGER," +
                    AnimationItemTable.Cols.START_POSITION_Y + "INTEGER," +
                    AnimationItemTable.Cols.STEP_ID + "TEXT," +
                    "FOREIGN KEY (" + AnimationItemTable.Cols.STEP_ID + ")" +
                    "REFERENCES " + StepTable.NAME + " (" + StepTable.Cols.ID + ")" +
                ")"
        );
        db.execSQL("create table " + DocumentTable.NAME +
                "(" +
                    DocumentTable.Cols.ID  + "TEXT PRIMARY KEY," +
                    DocumentTable.Cols.DIFFICULTY + "INTEGER," +
                    DocumentTable.Cols.IMAGE_URL + "TEXT," +
                    DocumentTable.Cols.INTRODUCTION + "TEXT," +
                    DocumentTable.Cols.TOPIC_ID + "TEXT," +
                    "FOREIGN KEY (" + DocumentTable.Cols.TOPIC_ID + ")" +
                    "REFERENCES " + TopicTable.NAME + " (" + TopicTable.Cols.ID + ")" +
                ")"
        );
        db.execSQL("create table " + StepTable.NAME +
                "(" +
                    StepTable.Cols.ID  + "TEXT PRIMARY KEY," +
                    StepTable.Cols.AUTO_ANIMATION + "INTEGER," +
                    StepTable.Cols.DOCUMENT_ID + "TEXT," +
                    StepTable.Cols.IMAGE_URL  + "TEXT," +
                    StepTable.Cols.PAGE_NUMBER + "INTEGER," +
                    StepTable.Cols.TEXT + "TEXT," +
                    "FOREIGN KEY (" + StepTable.Cols.DOCUMENT_ID + ")" +
                    "REFERENCES " + DocumentTable.NAME + " (" + DocumentTable.Cols.ID + ")" +
                ")"
        );
        db.execSQL("create table " + TopicTable.NAME +
                "(" +
                    TopicTable.Cols.ID  + "TEXT PRIMARY KEY," +
                    TopicTable.Cols.IMAGE_URL + "TEXT," +
                    TopicTable.Cols.TOPIC_NAME + "TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
