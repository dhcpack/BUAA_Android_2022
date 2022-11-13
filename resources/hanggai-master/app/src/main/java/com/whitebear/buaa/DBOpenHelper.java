package com.whitebear.buaa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Random;

public class DBOpenHelper extends SQLiteOpenHelper {
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.whitebear.buaa/databases/";
    private static String DB_NAME = "buaa.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public SQLiteDatabase getMyDataBase() {
        return myDataBase;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            //this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

//    private void copyDataBase() throws IOException {
//        //Open your local db as the input stream
//        InputStream myInput = myContext.getAssets().open(DB_NAME);
//        // Path to the just created empty db
//        String outFileName = DB_PATH + DB_NAME;
//        //Open the empty db as the output stream
//        OutputStream myOutput = new FileOutputStream(outFileName);
//        //transfer bytes from the inputfile to the outputfile
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//            myOutput.write(buffer, 0, length);
//        }
//        //Close the streams
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//    }

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        File dbPath = new File(DB_PATH);
        FileOutputStream myOutput = null;
        try {
            if (!dbPath.exists()) dbPath.mkdir();
            myOutput = new FileOutputStream(outFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        if (myInput != null & myOutput != null) {
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    public String[][] loadQuestionByColumn(String subject, String column,String value) {
        Cursor cursor;
        openDataBase();
        if (subject.equals("art")) {
            cursor = myDataBase.rawQuery("select * from art_questions where "+column+" = "+value,null);
        } else {
            cursor = myDataBase.rawQuery("select * from science_questions where "+column+" = "+value,null);
        }
        cursor.moveToFirst();
        int length=cursor.getCount();
        String[][] question = new String[length][10];
        for (int i=0;i<length;i++) {
            question[i][0] = cursor.getString(cursor.getColumnIndex("QUESTION"));
            question[i][1] = cursor.getString(cursor.getColumnIndex("A"));
            question[i][2] = cursor.getString(cursor.getColumnIndex("B"));
            question[i][3] = cursor.getString(cursor.getColumnIndex("C"));
            question[i][4] = cursor.getString(cursor.getColumnIndex("D"));
            question[i][5] = cursor.getString(cursor.getColumnIndex("ANSWER"));
            question[i][6] = cursor.getString(cursor.getColumnIndex("IMAGENUM"));
            question[i][7]=cursor.getString(cursor.getColumnIndex("ID"));
            question[i][8]=cursor.getString(cursor.getColumnIndex("COLLECTION"));
            question[i][9]=cursor.getString(cursor.getColumnIndex("TYPE"));

            cursor.moveToNext();
        }
        if (myDataBase != null) {
            myDataBase.close();
            cursor.close();
        }
        return question;
    }

    public int[] loadMainPage(String subject){
        int[] progress=new int[5];
        Cursor cursor;
        openDataBase();
        cursor = myDataBase.rawQuery("select * from "+subject+"_questions",null);
        progress[3]=cursor.getCount();
        cursor=null;

        cursor = myDataBase.rawQuery("select * from currentprogress where SUBJECT ='"+subject+"'",null);
        cursor.moveToFirst();
        progress[1]=cursor.getInt(cursor.getColumnIndex("chapter"));
        progress[2]=cursor.getInt(cursor.getColumnIndex("position"));
        cursor=null;
//        myDataBase=getReadableDatabase();

        cursor = myDataBase.rawQuery("select * from progress where SUBJECT ='"+subject+"'",null);
        cursor.moveToFirst();
        int length=cursor.getCount();
        progress[0]=0;
        for (int i=0;i<length;i++){
            progress[0]+=cursor.getInt(cursor.getColumnIndex("PROGRESS"));
            if (progress[1]==cursor.getInt(cursor.getColumnIndex("CHAPTER"))){
                progress[4]=cursor.getInt(cursor.getColumnIndex("PROGRESS"));
            }
            cursor.moveToNext();
        }
        if (myDataBase != null) {
            myDataBase.close();
            cursor.close();
        }
        return progress;
    }

    public String[][] loadTest(String subject,int number){
        Cursor multiple;
        Cursor single;
        int m=number;
        int s=100-number*2;
        Random random=new Random();
        openDataBase();

        if (subject.equals("art")) {
            single = myDataBase.rawQuery("select * from art_questions where type = 1",null);
            multiple = myDataBase.rawQuery("select * from art_questions where type = 2",null);
        } else {
            single = myDataBase.rawQuery("select * from science_questions where type = 1",null);
            multiple = myDataBase.rawQuery("select * from science_questions where type = 2",null);
        }

        single.moveToFirst();
        multiple.moveToFirst();
        int slength=single.getCount(),mlength=multiple.getCount();
        int sgap=slength/s,mgap=mlength/m;
        String[][] question=new String[s+m][10];
        int j=0,k;

        for (int i=0;i<s;i++) {
            k=random.nextInt(sgap);
            single.moveToPosition(j+k);
            question[i][0] = single.getString(single.getColumnIndex("QUESTION"));
            question[i][1] = single.getString(single.getColumnIndex("A"));
            question[i][2] = single.getString(single.getColumnIndex("B"));
            question[i][3] = single.getString(single.getColumnIndex("C"));
            question[i][4] = single.getString(single.getColumnIndex("D"));
            question[i][5] = single.getString(single.getColumnIndex("ANSWER"));
            question[i][6] = single.getString(single.getColumnIndex("IMAGENUM"));
            question[i][7]=single.getString(single.getColumnIndex("ID"));
            question[i][8]=single.getString(single.getColumnIndex("COLLECTION"));
            question[i][9]=single.getString(single.getColumnIndex("TYPE"));
            j+=sgap;
        }
        single.close();
        j=0;
        for (int i=0;i<m;i++) {
            k=random.nextInt(mgap);
            multiple.moveToPosition(j+k);
            question[i+s][0] = multiple.getString(multiple.getColumnIndex("QUESTION"));
            question[i+s][1] = multiple.getString(multiple.getColumnIndex("A"));
            question[i+s][2] = multiple.getString(multiple.getColumnIndex("B"));
            question[i+s][3] = multiple.getString(multiple.getColumnIndex("C"));
            question[i+s][4] = multiple.getString(multiple.getColumnIndex("D"));
            question[i+s][5] = multiple.getString(multiple.getColumnIndex("ANSWER"));
            question[i+s][6] = multiple.getString(multiple.getColumnIndex("IMAGENUM"));
            question[i+s][7]=multiple.getString(multiple.getColumnIndex("ID"));
            question[i+s][8]=multiple.getString(multiple.getColumnIndex("COLLECTION"));
            question[i+s][9]=multiple.getString(multiple.getColumnIndex("TYPE"));
            j+=mgap;
        }
        multiple.close();
        if (myDataBase != null) {
            myDataBase.close();
        }
        return question;
    }

    //public void upDate(String subject,String column,)

}
