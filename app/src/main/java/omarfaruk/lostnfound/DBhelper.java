package omarfaruk.lostnfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBname = "Test_DB";
    public static final int version = 17;
    public static final String userTBL = "userTBL";
    public static final String col_id = "id";
    public static final String col_name = "name";
    public static final String col_email = "email";
    public static final String col_password = "password";

    public static final String TBL_found = "tbl_found";
    public static final String col_found_id = "postID";
    public static final String col_found_by = "foundBy";
    public static final String col_mobile = "mobile";
    public static final String col_item_name = "itemName";
    public static final String col_description = "description";
    public static final String col_date = "date";
    public static final String col_location = "location";
    public static final String col_image = "image";
    public static final String col_cat = "category";

    public static final String TBL_lost = "tbl_lost";
    public static final String col_lost_id = "lostID";
    public static final String col_lost_by = "lostBy";
    public static final String col_l_mobile = "lostMobile";
    public static final String col_l_item_name = "lostItemName";
    public static final String col_l_description = "lostDescription";
    public static final String col_l_date = "lostDate";
    public static final String col_l_location = "lostLocation";
    public static final String col_l_image = "lostImage";
    public static final String col_l_cat = "lostCategory";

    public DBhelper(Context context) {
        super(context, DBname, null, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + userTBL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_found);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_lost);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + userTBL + " (" +
                col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_name + " TEXT, " +
                col_email + " TEXT, " +
                col_password + " TEXT)");

        db.execSQL("CREATE TABLE " + TBL_found + " (" +
                col_found_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_found_by + " TEXT, " +
                col_mobile + " TEXT, " +
                col_cat + " TEXT, " +
                col_item_name + " TEXT, " +
                col_description + " TEXT, " +
                col_date + " TEXT, " +
                col_location + " TEXT, " +
                col_image + " BLOB)");

        db.execSQL("CREATE TABLE " + TBL_lost + " (" +
                col_lost_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_lost_by + " TEXT, " +
                col_l_mobile + " TEXT, " +
                col_l_cat + " TEXT, " +
                col_l_item_name + " TEXT, " +
                col_l_description + " TEXT, " +
                col_l_date + " TEXT, " +
                col_l_location + " TEXT, " +
                col_l_image + " BLOB)");
    }

    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(col_name, name);
        cv.put(col_email, email);
        cv.put(col_password, password);

        long result = db.insert(userTBL, null, cv);

        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + userTBL + " WHERE " + col_email + " = ? AND " + col_password + " = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertFoundItem(String foundBy, String cat, String name, String description, String date, String location, String mobile, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(col_found_by, foundBy);
        values.put(col_cat, cat);
        values.put(col_item_name, name);
        values.put(col_description, description);
        values.put(col_date, date);
        values.put(col_location, location);
        values.put(col_mobile, mobile);
        values.put(col_image, imageByteArray);
        long res = db.insert(TBL_found, null, values);

        if (res == -1) {
            Log.e("DBhelper", "Error inserting data into tbl_found");
        } else {
            Log.d("DBhelper", "Data inserted successfully into tbl_found");
        }
        return res != -1;
    }

    public boolean insertLostItem(String lostBy, String cat, String name, String mobile, String description, String date, String location, byte[] imageByteArray) {
        Log.d("DBhelper", "lostBy received: " + lostBy);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(col_lost_by, lostBy);
        values.put(col_l_cat, cat);
        values.put(col_l_item_name, name);
        values.put(col_l_mobile, mobile);
        values.put(col_l_description, description);
        values.put(col_l_date, date);
        values.put(col_l_location, location);
        values.put(col_l_image, imageByteArray);
        long res = db.insert(TBL_lost, null, values);

        if (res == -1) {
            Log.e("DBhelper", "Error inserting data into tbl_lost");
        } else {
            Log.d("DBhelper", "Data inserted successfully into tbl_lost");
        }
        return res != -1;
    }

    //    public Cursor getAllPost() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM " + TBL_found, null);
//    }
    public Cursor getAllLostItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT lostID, lostBy, lostMobile, lostCategory, lostItemName, lostDescription, lostDate, lostLocation, lostImage FROM " + TBL_lost, null);
    }


    public Cursor getAllFoundItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT postID, foundBy, mobile, category, itemName, description, date, location, image FROM " + TBL_found, null);
    }


    public String getMobileNumberByPostID(int postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT mobile FROM " + TBL_found + " WHERE postID = ?", new String[]{String.valueOf(postId)});
        String mobileNumber = null;
        if (cursor.moveToFirst()) {
            mobileNumber = cursor.getString(cursor.getColumnIndex("mobile"));
        }
        cursor.close();
        return mobileNumber;
    }

    public String getLostMobileNumberByLostID(int lostId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT lostMobile FROM " + TBL_lost + " WHERE lostID = ?", new String[]{String.valueOf(lostId)});
        String mobileNumber = null;
        if (cursor.moveToFirst()) {
            mobileNumber = cursor.getString(cursor.getColumnIndex("lostMobile"));
        }
        cursor.close();
        return mobileNumber;
    }


    public List<Post> getPostsByUserEmail(String userEmail) {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String foundQuery = "SELECT postID, 'found' as type, itemName, category, date, image, description, location, mobile FROM " + TBL_found +
                " WHERE " + col_found_by + " = ?";
        Cursor foundCursor = db.rawQuery(foundQuery, new String[]{userEmail});
        while (foundCursor.moveToNext()) {
            posts.add(new Post(
                    foundCursor.getInt(0),
                    foundCursor.getString(1),
                    foundCursor.getString(2),
                    foundCursor.getString(3),
                    foundCursor.getString(4),
                    foundCursor.getBlob(5),
                    foundCursor.getString(6),
                    foundCursor.getString(7),
                    foundCursor.getString(8)
            ));
        }
        foundCursor.close();

        String lostQuery = "SELECT lostID, 'lost' as type, lostItemName, lostCategory, lostDate, lostImage, lostDescription, lostLocation, lostMobile FROM " + TBL_lost +
                " WHERE " + col_lost_by + " = ?";
        Cursor lostCursor = db.rawQuery(lostQuery, new String[]{userEmail});
        while (lostCursor.moveToNext()) {
            posts.add(new Post(
                    lostCursor.getInt(0),
                    lostCursor.getString(1),
                    lostCursor.getString(2),
                    lostCursor.getString(3),
                    lostCursor.getString(4),
                    lostCursor.getBlob(5),
                    lostCursor.getString(6),
                    lostCursor.getString(7),
                    lostCursor.getString(8)
            ));
        }
        lostCursor.close();

        return posts;
    }

    public Post getPostById(int postId, String postType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Post post = null;
        String table = postType.equals("found") ? TBL_found : TBL_lost;
        String idColumn = postType.equals("found") ? col_found_id : col_lost_id;

        Cursor cursor = db.query(table, null, idColumn + "=?", new String[]{String.valueOf(postId)}, null, null, null);

        if (cursor.moveToFirst()) {
            String itemName = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_item_name : col_l_item_name));
            String category = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_cat : col_l_cat));
            String date = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_date : col_l_date));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(postType.equals("found") ? col_image : col_l_image));
            String description = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_description : col_l_description));
            String location = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_location : col_l_location));
            String mobile = cursor.getString(cursor.getColumnIndex(postType.equals("found") ? col_mobile : col_l_mobile));

            post = new Post(postId, postType, itemName, category, date, image, description, location, mobile);
        }
        cursor.close();
        return post;
    }

    public boolean updatePost(int postId, String postType, String name, String description, String date, String location, String mobile, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String table = postType.equals("found") ? TBL_found : TBL_lost;
        String idColumn = postType.equals("found") ? col_found_id : col_lost_id;

        values.put(postType.equals("found") ? col_item_name : col_l_item_name, name);
        values.put(postType.equals("found") ? col_description : col_l_description, description);
        values.put(postType.equals("found") ? col_date : col_l_date, date);
        values.put(postType.equals("found") ? col_location : col_l_location, location);
        values.put(postType.equals("found") ? col_mobile : col_l_mobile, mobile);
        values.put(postType.equals("found") ? col_cat : col_l_cat, category);


        int rowsAffected = db.update(table, values, idColumn + "=?", new String[]{String.valueOf(postId)});
        return rowsAffected > 0;
    }

    public boolean deletePost(int postId, String postType) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = postType.equals("found") ? TBL_found : TBL_lost;
        String idColumn = postType.equals("found") ? col_found_id : col_lost_id;

        int rowsDeleted = db.delete(table, idColumn + "=?", new String[]{String.valueOf(postId)});
        return rowsDeleted > 0;


    }

}
