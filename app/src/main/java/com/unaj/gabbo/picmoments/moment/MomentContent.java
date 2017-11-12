package com.unaj.gabbo.picmoments.moment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unaj.gabbo.picmoments.db.SQLiteDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gabbo on 27/10/2017.
 */

public class MomentContent {

    public static final List<Moment> ITEMS = new ArrayList<Moment>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Moment> ITEM_MAP = new HashMap<String, Moment>();

//    private static final int COUNT = 25;
    private static SQLiteDBHelper dbHelper;
    SQLiteDatabase db;
    private static ArrayList<Moment> momentsFromDB;


    public List<Moment> getMomentsToList (SQLiteDBHelper dbHelper){
       ITEMS.clear();
        momentsFromDB = getAllMoments (dbHelper);
        if (momentsFromDB!= null) {
            for (Moment momentToAdd : momentsFromDB) {
                addMoment(momentToAdd);
            }
            return ITEMS;
        }
        return null;
    }

    public List<Moment> getMomentsToListFilter (SQLiteDBHelper dbHelper, String filter){
        ITEMS.clear();
        momentsFromDB = getAllMomentsFilter (dbHelper, filter);
        if (momentsFromDB!= null) {
            for (Moment momentToAdd : momentsFromDB) {
                addMoment(momentToAdd);
            }
            return ITEMS;
        }
        return null;
    }

    private ArrayList<Moment> getAllMomentsFilter(SQLiteDBHelper dbHelper, String filter) {
        SQLiteDatabase db       =   dbHelper.getWritableDatabase();
        String sql              =   "SELECT * FROM moment WHERE description LIKE '%"+'#'+filter+"%'";
        Cursor cursor           =   db.rawQuery(sql, new String[] {});
        ArrayList<Moment> moments = new ArrayList<Moment>();
        Moment moment;
//        int id = -1;

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int _id = cursor.getShort(0);
                byte[] imageAsBytes = cursor.getBlob(1);
                String location = cursor.getString(2);
                String description = cursor.getString(3);
                String photoDate = cursor.getString(4);
                int userId = cursor.getInt(5);
//                id = id + 1;

                moment = new Moment(String.valueOf(_id), imageAsBytes, location, description, photoDate, userId);
                moments.add(moment);
                cursor.moveToNext();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        if(cursor.getCount() == 0){
            return null;
        }

        return moments;

    }

    private static void addMoment(Moment item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

//    private static Moment createMomentItem(int position) {
//        String ubicacion = null;
//        String titulo = null;
//        return new Moment(String.valueOf(position),  makeDetails(position), titulo, ubicacion, new Date());
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        /*for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }*/
        return builder.toString();
    }

    public  ArrayList<Moment> getAllMoments(SQLiteDBHelper dbHelper) {
        SQLiteDatabase db       =   dbHelper.getWritableDatabase();
        String sql              =   "SELECT * FROM moment";
        Cursor cursor           =   db.rawQuery(sql, new String[] {});
        ArrayList<Moment> moments = new ArrayList<Moment>();
        Moment moment;
//        int id = -1;

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int _id = cursor.getInt(0);
                byte[] imageAsBytes = cursor.getBlob(1);
                String location = cursor.getString(2);
                String description = cursor.getString(3);
                String photoDate = cursor.getString(4);
                int userId = cursor.getInt(5);
//                id = id + 1;

                moment = new Moment(String.valueOf(_id), imageAsBytes, location, description, photoDate, userId);
                moments.add(moment);
                cursor.moveToNext();
                }
            }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        if(cursor.getCount() == 0){
            return null;
        }

        return moments;
    }
}
