package ddwu.mobile.finalproject.ma01_20191012;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyPlaceDBManager {
    MyPlaceDBHelper myPlaceDBHelper = null;
    Cursor cursor = null;

    public MyPlaceDBManager(Context context) {
        myPlaceDBHelper = new MyPlaceDBHelper(context);
    }

    public ArrayList<MyPlace> getAllPlace() {
        ArrayList placeList = new ArrayList();
        SQLiteDatabase db = myPlaceDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + MyPlaceDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(MyPlaceDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(MyPlaceDBHelper.COL_NAME));
            String placeId = cursor.getString(cursor.getColumnIndex(MyPlaceDBHelper.COL_PLACEID));
            String address = cursor.getString(cursor.getColumnIndex(MyPlaceDBHelper.COL_ADDRESS));
            String number = cursor.getString(cursor.getColumnIndex(MyPlaceDBHelper.COL_NUMBER));
            double lat = cursor.getDouble(cursor.getColumnIndex(MyPlaceDBHelper.COL_LAT));
            double lon = cursor.getDouble(cursor.getColumnIndex(MyPlaceDBHelper.COL_LON));
            String memo = cursor.getString(cursor.getColumnIndex(MyPlaceDBHelper.COL_MEMO));
            //int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            //float rating = cursor.getFloat(cursor.getColumnIndex(BookDBHelper.COL_RATING));
            //String str_rating = cursor.getString(cursor.getColumnIndex(bookDBHelper.COL_RATING));
            //float rating = Float.parseFloat(str_rating);
            MyPlace newplace = new MyPlace((int)id, placeId, name, number, address, lat, lon, memo);
            placeList.add ( newplace );
        }

        cursor.close();
        myPlaceDBHelper.close();

        return placeList;
    }


    public boolean addNewPlace(MyPlace newPlace) {
        SQLiteDatabase db = myPlaceDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(MyPlaceDBHelper.COL_PLACEID, newPlace.getPlaceId());
        value.put(MyPlaceDBHelper.COL_NAME, newPlace.getName());
        value.put(MyPlaceDBHelper.COL_NUMBER, newPlace.getNumber());
        value.put(MyPlaceDBHelper.COL_ADDRESS, newPlace.getAddress());
        value.put(MyPlaceDBHelper.COL_LAT, newPlace.getLatitude());
        value.put(MyPlaceDBHelper.COL_LON, newPlace.getLongitude());
        value.put(MyPlaceDBHelper.COL_MEMO, newPlace.getMemo());
        //value.put(BookDBHelper.COL_PRICE, newBook.getPrice());
        //value.put(BookDBHelper.COL_RATING, newBook.getRating());


        long count = db.insert(MyPlaceDBHelper.TABLE_NAME, null, value);
        if (count > 0)
            return true;
        return false;
    }
    public boolean modifyPlace(MyPlace place) {
        SQLiteDatabase sqLiteDatabase = myPlaceDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(MyPlaceDBHelper.COL_PLACEID, place.getPlaceId());
        value.put(MyPlaceDBHelper.COL_NAME, place.getName());
        value.put(MyPlaceDBHelper.COL_NUMBER, place.getNumber());
        value.put(MyPlaceDBHelper.COL_ADDRESS, place.getAddress());
        value.put(MyPlaceDBHelper.COL_LAT, place.getLatitude());
        value.put(MyPlaceDBHelper.COL_LON, place.getLongitude());
        value.put(MyPlaceDBHelper.COL_MEMO, place.getMemo());
        //value.put(BookDBHelper.COL_PRICE, book.getPrice());
        //value.put(BookDBHelper.COL_RATING, book.getRating());

        String whereClause = MyPlaceDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(place.get_id())};
        int result = sqLiteDatabase.update(MyPlaceDBHelper.TABLE_NAME, value, whereClause, whereArgs);
        myPlaceDBHelper.close();
        if (result > 0)
            return true;
        return false;
    }
    public boolean removePlace(long id) {
        SQLiteDatabase sqLiteDatabase = myPlaceDBHelper.getWritableDatabase();
        String whereClause = MyPlaceDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(MyPlaceDBHelper.TABLE_NAME, whereClause, whereArgs);
        myPlaceDBHelper.close();
        if (result > 0)
            return true;
        return false;
    }

    public void close() {
        if (myPlaceDBHelper != null) myPlaceDBHelper.close();
        if (cursor != null) cursor.close();
    };
}
