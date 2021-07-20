package com.shashank.mentalhealth.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.core.internal.view.SupportMenu;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "GraphData.DB", factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DEPRESSION(NAME TEXT, DEPRESSION INTEGER);");
        db.execSQL("CREATE TABLE ANXIETY(NAME TEXT, ANXIETY INTEGER );");
        db.execSQL("CREATE TABLE BIPOLAR(NAME TEXT,BIPOLAR TEXT);");
        db.execSQL("CREATE TABLE QUESTIONS(TYPE TEXT, QUESTIONS TEXT UNIQUE)");
        db.execSQL("CREATE TABLE CHAT(NAME TEXT, CHAT TEXT)");
        db.execSQL("INSERT INTO QUESTIONS VALUES ('DEPRESSION','Little interest or pleasure in doing things'), ('DEPRESSION','Feeling down, depressed, or hopeless'),('DEPRESSION','Trouble falling or staying asleep, or sleeping too much'),('DEPRESSION','Feeling tired or having little energy'),('DEPRESSION','Poor appetite or overeating'),('DEPRESSION','Feeling bad about yourself - or that you are a failure or have let yourself or your family down'),('DEPRESSION','Trouble concentrating on things, such as reading the newspaper or watching television'),('DEPRESSION','Moving or speaking so slowly that other people could have noticed Or the opposite - being so fidgety or restless that you have been moving around a lot more than usual'),('DEPRESSION','Thoughts that you would be better off dead, or of hurting yourself'),('DEPRESSION','If you checked off any problems, how difficult have these problems made it for you at work, home, or with other people?'),('BIPOLAR','You felt so good or hyper that other people thought you were not your normal self or were so hyper that you got into trouble?'),('BIPOLAR','You were so irritable that you shouted at people or started fights or arguments?'),('BIPOLAR','You felt much more self-confident than usual?'),('BIPOLAR','You got much less sleep than usual and found you didn’t really miss it?'),('BIPOLAR','You were much more talkative or spoke much faster than usual?'),('BIPOLAR','Thoughts raced through your head or you couldn’t slow your mind down?'),('BIPOLAR','You were so easily distracted by things around you that you had trouble concentrating or staying on track?'),('BIPOLAR','You had much more energy than usual?'),('BIPOLAR','You were much more social or outgoing than usual, for example, you telephoned friends in the middle of the night?'),('BIPOLAR','I sometimes feel very hyper or very irritated.'),('ANXIETY','Feeling nervous, anxious, or on edge'),('ANXIETY','Not being able to stop or control worrying'),('ANXIETY','Worrying too much about different things'),('ANXIETY','Trouble relaxing'),('ANXIETY','Being so restless that it is hard to sit still'),('ANXIETY','Becoming easily annoyed or irritable'),('ANXIETY','Feeling afraid, as if something awful might happen'),('ANXIETY','Are you working on stuff thats meaningful to you?'),('ANXIETY','do you met up with your friends in real life ?'),('ANXIETY','Do you sleep 7-8 hours?');");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS DEPRESSION;");
        db.execSQL("DROP TABLE IF EXISTS BIPOLAR;");
        db.execSQL("DROP TABLE IF EXISTS ANXIETY;");
        db.execSQL("DROP TABLE IF EXISTS QUESTIONS");
        onCreate(db);
    }

//    public void defaultQuestions() {
//        getWritableDatabase().execSQL("INSERT INTO QUESTIONS VALUES ('DEPRESSION','Little interest or pleasure in doing things'), ('DEPRESSION','Feeling down, depressed, or hopeless'),('DEPRESSION','Trouble falling or staying asleep, or sleeping too much'),('DEPRESSION','Feeling tired or having little energy'),('DEPRESSION','Poor appetite or overeating'),('DEPRESSION','Feeling bad about yourself - or that you are a failure or have let yourself or your family down'),('DEPRESSION','Trouble concentrating on things, such as reading the newspaper or watching television'),('DEPRESSION','Moving or speaking so slowly that other people could have noticed Or the opposite - being so fidgety or restless that you have been moving around a lot more than usual'),('DEPRESSION','Thoughts that you would be better off dead, or of hurting yourself'),('DEPRESSION','If you checked off any problems, how difficult have these problems made it for you at work, home, or with other people?'),('BIPOLAR','You felt so good or hyper that other people thought you were not your normal self or were so hyper that you got into trouble?'),('BIPOLAR','You were so irritable that you shouted at people or started fights or arguments?'),('BIPOLAR','You felt much more self-confident than usual?'),('BIPOLAR','You got much less sleep than usual and found you didn’t really miss it?'),('BIPOLAR','You were much more talkative or spoke much faster than usual?'),('BIPOLAR','Thoughts raced through your head or you couldn’t slow your mind down?'),('BIPOLAR','You were so easily distracted by things around you that you had trouble concentrating or staying on track?'),('BIPOLAR','You had much more energy than usual?'),('BIPOLAR','You were much more social or outgoing than usual, for example, you telephoned friends in the middle of the night?'),('BIPOLAR','I sometimes feel very hyper or very irritated.'),('ANXIETY','Feeling nervous, anxious, or on edge'),('ANXIETY','Not being able to stop or control worrying'),('ANXIETY','Worrying too much about different things'),('ANXIETY','Trouble relaxing'),('ANXIETY','Being so restless that it is hard to sit still'),('ANXIETY','Becoming easily annoyed or irritable'),('ANXIETY','Feeling afraid, as if something awful might happen'),('ANXIETY','Are you working on stuff thats meaningful to you?'),('ANXIETY','do you met up with your friends in real life ?'),('ANXIETY','Do you sleep 7-8 hours?');");
//    }

    public void fetchQuestions(String type, ArrayList<String> questions) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM QUESTIONS WHERE TYPE  = '" + type + "'", null);
        while (cursor.moveToNext()) {
            questions.add(cursor.getString(1));
        }
        cursor.close();
    }

    public void insertResult(String tableName, String name, int results) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put(tableName, Integer.valueOf(results));
        getWritableDatabase().insertOrThrow(tableName, "", contentValues);
    }

    public void insertChat(String name, String chat) {
        this.getWritableDatabase().execSQL("DELETE FROM CHAT WHERE NAME = '" + name + "'");
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("CHAT", chat);
        getWritableDatabase().insertOrThrow("CHAT", "", contentValues);
    }

    public String fetchChat(String name) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM CHAT WHERE NAME ='" + name + "'", null);
        try {
            cursor.moveToFirst();
            String chat = cursor.getString(1);
            cursor.close();
            return chat;
        } catch (IndexOutOfBoundsException e) {
            cursor.close();
            return "";
        }
    }

    public void deleteResult(String tableName, String Name) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DELETE FROM " + tableName + " WHERE NAME = '" + Name + "'");
    }

    public void showResults(String tableName, String Name, GraphView graphView, LineGraphSeries<DataPoint> series) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + tableName + " WHERE NAME = '" + Name + "'", null);
        graphView.setTitle("your results");
        int i = 0;
        while (cursor.moveToNext()) {
            series.appendData(new DataPoint((double) i, (double) cursor.getInt(cursor.getColumnIndex(tableName))), true, 100);
            i++;
        }
        cursor.close();
        series.setColor(SupportMenu.CATEGORY_MASK);
        graphView.addSeries(series);
        graphView.getViewport().setScalable(true);
    }
}