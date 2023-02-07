package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "books.db";
    public final static String TABLE_NAME = "book_table";
    public final static String COL_ID = "_id";
    public final static String COL_IMAGE = "image";
    public final static String COL_NAME = "name";
    public final static String COL_AUTHOR = "author";
    public final static String COL_PUBLISHER = "publisher";
    public final static String COL_MEMO = "memo";
    public final static String COL_CATE = "cate";
    public final static String COL_DESCRIPTION = "description";
    public final static String COL_PLUSIMAGE = "plusImage";
    public final static String COL_PLUSTYPE = "plusType";
    //public final static String COL_PRICE = "price";
   // public final static String COL_RATING = "rating";

    public BookDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " integer primary key autoincrement, " +
                COL_IMAGE + " TEXT, " + COL_NAME + " TEXT, " + COL_AUTHOR + " TEXT, " + COL_PUBLISHER + " TEXT, " + COL_MEMO  + " TEXT, " +
                COL_DESCRIPTION  + " TEXT, " + COL_CATE + " integer, " + COL_PLUSIMAGE + " TEXT, "  + COL_PLUSTYPE + " integer )";

        db.execSQL(sql);
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 11, '달러구트 꿈 백화점', '이미예', '팩토리나인', 13000, 5.0)");
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 12, '지구 속 여행', '쥘 베른', '크레용 하우스', 15000, 4.0)");
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 13, '당신의 과거를 지워드립니다', '비프케 로렌츠', '레드박스', 13800, 3.5)");
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 14, '명탐정 코난 99', '아오야마 고쇼', '서울문화사', 5500, 4.5)");
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 15, '나미야 잡화점의 기적', '히가시노 게이고', '현대문학', 14800, 4.0)");
        //db.execSQL("insert into " + TABLE_NAME + " values (null, 16, 'The Midnight Library', 'Haig, Matt', 'Cannongate', 14000, 3.0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
