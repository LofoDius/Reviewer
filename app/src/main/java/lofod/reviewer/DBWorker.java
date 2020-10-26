package lofod.reviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBWorker extends SQLiteOpenHelper {
    private static final String DbName = "Reviewer";
    public static final String categoriesTable = "categories";
    public static final String reviewsTable = "reviews";

    public DBWorker(Context context) {
        super(context, DbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"categories\" (\n" +
                "  \"category_name\" TEXT NOT NULL,\n" +
                "  \"parent_name\" TEXT\n" +
                ");");

        db.execSQL("CREATE TABLE \"reviews\" (\n" +
                "  \"product_name\" TEXT NOT NULL,\n" +
                "  \"price\" REAL,\n" +
                "  \"manufacturer\" TEXT,\n" +
                "  \"amount\" TEXT,\n" +
                "  \"review\" TEXT,\n" +
                "  \"mark\" integer,\n" +
                "  \"category\" TEXT,\n" +
                "  \"amount_type\" integer,\n" +
                "  \"date\" TEXT,\n" +
                "  \"photo_uri\" TEXT,\n" +
                "  PRIMARY KEY (\"product_name\", \"product_name\")\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
