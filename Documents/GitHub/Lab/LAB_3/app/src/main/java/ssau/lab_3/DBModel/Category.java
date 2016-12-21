package ssau.lab_3.DBModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Category getCategory(DBHelper dbHelper,int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    cID = cursor.getColumnIndex(DBHelper.ID),
                    cName = cursor.getColumnIndex(DBHelper.NAME);
            do {
                Category category = new Category(
                        cursor.getInt(cID),
                        cursor.getString(cName)
                );
                if (category.id == id) {
                    cursor.close();
                    return category;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    public static Category[] getCategories(DBHelper dbHelper) {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_CATEGORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int
                    cID = cursor.getColumnIndex(DBHelper.ID),
                    cName = cursor.getColumnIndex(DBHelper.NAME);
            do {
                Category category = new Category(
                        cursor.getInt(cID),
                        cursor.getString(cName)
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories.toArray(new Category[0]);
    }
}