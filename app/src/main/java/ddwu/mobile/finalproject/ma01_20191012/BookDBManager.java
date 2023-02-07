package ddwu.mobile.finalproject.ma01_20191012;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BookDBManager {
    BookDBHelper bookDBHelper = null;
    Cursor cursor = null;

    public BookDBManager(Context context) {
        bookDBHelper = new BookDBHelper(context);
    }
    public ArrayList<BookDto> getAllBook() {
        ArrayList bookList = new ArrayList();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String image = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_IMAGE));
            String name = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_NAME));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            //int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            //float rating = cursor.getFloat(cursor.getColumnIndex(BookDBHelper.COL_RATING));
            //String str_rating = cursor.getString(cursor.getColumnIndex(bookDBHelper.COL_RATING));
            //float rating = Float.parseFloat(str_rating);
            String memo = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_MEMO));
            String description = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_DESCRIPTION));
            int cate = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_CATE));
            String plusImage = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PLUSIMAGE));
            int plusType = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PLUSTYPE));
            BookDto newbook = new BookDto((int)id, name, author, publisher, image, memo, cate, description, plusImage, plusType);
            bookList.add ( newbook );
        }

        cursor.close();
        bookDBHelper.close();

        return bookList;
    }

    public ArrayList<BookDto> getReadBook(String radioSelect) {
        ArrayList bookList = new ArrayList();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME + " WHERE CATE = ? ", new String[]{radioSelect});

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String image = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_IMAGE));
            String name = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_NAME));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            //int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            //float rating = cursor.getFloat(cursor.getColumnIndex(BookDBHelper.COL_RATING));
            //String str_rating = cursor.getString(cursor.getColumnIndex(bookDBHelper.COL_RATING));
            //float rating = Float.parseFloat(str_rating);
            String memo = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_MEMO));
            String description = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_DESCRIPTION));
            int cate = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_CATE));
            String plusImage = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PLUSIMAGE));
            int plusType = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PLUSTYPE));
            BookDto newbook = new BookDto((int)id, name, author, publisher, image, memo, cate, description, plusImage, plusType);
            bookList.add ( newbook );
        }

        cursor.close();
        bookDBHelper.close();

        return bookList;
    }

    public boolean addNewBook(BookDto newBook) {
        SQLiteDatabase db = bookDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(BookDBHelper.COL_IMAGE, newBook.getImageFileName());
        value.put(BookDBHelper.COL_NAME, newBook.getBookName());
        value.put(BookDBHelper.COL_AUTHOR, newBook.getBookAuthor());
        value.put(BookDBHelper.COL_PUBLISHER, newBook.getBookPublisher());
        value.put(BookDBHelper.COL_MEMO, newBook.getBookMemo());
        value.put(BookDBHelper.COL_CATE, newBook.getBookCate());
        value.put(BookDBHelper.COL_DESCRIPTION, newBook.getBookDescription());
        value.put(BookDBHelper.COL_PLUSIMAGE, newBook.getPlusimageFileName());
        value.put(BookDBHelper.COL_PLUSTYPE, newBook.getPlusType());
        //value.put(BookDBHelper.COL_PRICE, newBook.getPrice());
        //value.put(BookDBHelper.COL_RATING, newBook.getRating());


        long count = db.insert(BookDBHelper.TABLE_NAME, null, value);
        if (count > 0)
            return true;
        return false;
    }
    public boolean modifyBook(BookDto book) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(BookDBHelper.COL_IMAGE, book.getImageFileName());
        value.put(BookDBHelper.COL_NAME, book.getBookName());
        value.put(BookDBHelper.COL_AUTHOR, book.getBookAuthor());
        value.put(BookDBHelper.COL_PUBLISHER, book.getBookPublisher());
        value.put(BookDBHelper.COL_MEMO, book.getBookMemo());
        value.put(BookDBHelper.COL_CATE, book.getBookCate());
        value.put(BookDBHelper.COL_DESCRIPTION, book.getBookDescription());
        value.put(BookDBHelper.COL_PLUSIMAGE, book.getPlusimageFileName());
        value.put(BookDBHelper.COL_PLUSTYPE, book.getPlusType());
        //value.put(BookDBHelper.COL_PRICE, book.getPrice());
        //value.put(BookDBHelper.COL_RATING, book.getRating());

        String whereClause = BookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(book.get_id())};
        int result = sqLiteDatabase.update(BookDBHelper.TABLE_NAME, value, whereClause, whereArgs);
        bookDBHelper.close();
        if (result > 0)
            return true;
        return false;
    }
    public boolean removeBook(long id) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();
        String whereClause = BookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(BookDBHelper.TABLE_NAME, whereClause, whereArgs);
        bookDBHelper.close();
        if (result > 0)
            return true;
        return false;
    }
    public ArrayList<BookDto> getBookByName(String bookName) {
        ArrayList<BookDto> bookList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getReadableDatabase();
        String[] columns = null;
        String whereClause = bookDBHelper.COL_NAME + "=?";
        String[] whereArgs = new String[] {bookName};
        Cursor cursor = sqLiteDatabase.query(bookDBHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);
        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String image = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_IMAGE));
            String name = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_NAME));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            //int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            //String str_rating = cursor.getString(cursor.getColumnIndex(bookDBHelper.COL_RATING));
            //float rating = Float.parseFloat(str_rating);
            String memo = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_MEMO));
            int cate = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_CATE));
            String description = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_DESCRIPTION));
            String plusImage = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PLUSIMAGE));
            int plusType = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PLUSTYPE));
            bookList.add( new BookDto((int)id, name, author, publisher, image, memo, cate, description, plusImage, plusType));
        }
        cursor.close();
        bookDBHelper.close();
        return bookList;

    }
    public void close() {
        if (bookDBHelper != null) bookDBHelper.close();
        if (cursor != null) cursor.close();
    };
}
