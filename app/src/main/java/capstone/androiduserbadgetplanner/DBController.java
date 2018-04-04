package capstone.androiduserbadgetplanner;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.renderscript.Float2;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBController extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expenses.db";

    // Table Name
    public static final String TABLE_EXPENSES = "expenses";
    public static final String TABLE_INCOMES = "income";

    // Each Column
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_AMOUNT = "_amount";
    public static final String COLUMN_SAVING = "_saving";
    public static final String COLUMN_CATEGORY = "_category";
    public static final String COLUMN_MONTH = "_month";
    public static final String COLUMN_YEAR = "_year";

    // Create Constructor
    public DBController(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the database table for expenses
        String query = "CREATE TABLE "+ TABLE_EXPENSES + " ("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " INTEGER ," +
                COLUMN_SAVING + " INTEGER ," +
                COLUMN_CATEGORY + " TEXT ," +
                COLUMN_MONTH + " TEXT ," +
                COLUMN_YEAR + " TEXT " +
                ");";

        // Execute the SQL Query
        db.execSQL(query);

        // Create the database table for incomes
        String query2 = "CREATE TABLE "+ TABLE_INCOMES + " ("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " INTEGER ," +
                COLUMN_MONTH + " TEXT ," +
                COLUMN_YEAR + " TEXT " +
                ");";

        // Execute the SQL Query
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Example to delete the current table
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EXPENSES);
        onCreate(db);
    }


    public void resetDatabase() {
        // Example to delete the current table
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EXPENSES);

        // Restart the Database
        onCreate(db);
    }


    // method used to add a new row
    public void addExpenseData(Expenses expenses){
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;

        String monthName = "";
        switch (month){
            case 1: monthName = "January"; break;
            case 2: monthName = "February"; break;
            case 3: monthName = "March"; break;
            case 4: monthName = "April"; break;
            case 5: monthName = "May"; break;
            case 6: monthName = "June"; break;
            case 7: monthName = "July"; break;
            case 8: monthName = "August"; break;
            case 9: monthName = "September"; break;
            case 10: monthName = "October"; break;
            case 11: monthName = "November"; break;
            case 12: monthName = "December"; break;
        }

        // add amount in the amount column
        values.put(COLUMN_AMOUNT, expenses.get_amount());
        values.put(COLUMN_CATEGORY, expenses.get_category());
        values.put(COLUMN_SAVING, expenses.get_saving());
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MONTH, monthName);

        // This is the database references
        SQLiteDatabase db = getWritableDatabase();
        // The Command to be used
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public void updateIncome(int new_amount){

        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, new_amount);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;

        String monthName = "";
        switch (month){
            case 1: monthName = "January"; break;
            case 2: monthName = "February"; break;
            case 3: monthName = "March"; break;
            case 4: monthName = "April"; break;
            case 5: monthName = "May"; break;
            case 6: monthName = "June"; break;
            case 7: monthName = "July"; break;
            case 8: monthName = "August"; break;
            case 9: monthName = "September"; break;
            case 10: monthName = "October"; break;
            case 11: monthName = "November"; break;
            case 12: monthName = "December"; break;
        }

        // This is the database references
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_INCOMES, values, COLUMN_MONTH+"= ? AND "+COLUMN_YEAR+"= ?", new String[] {monthName, Integer.toString(year)});
        db.close();
    }

    // method used to add a new row
    public void addIncomeData(Income income){
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;

        String monthName = "";
        switch (month){
            case 1: monthName = "January"; break;
            case 2: monthName = "February"; break;
            case 3: monthName = "March"; break;
            case 4: monthName = "April"; break;
            case 5: monthName = "May"; break;
            case 6: monthName = "June"; break;
            case 7: monthName = "July"; break;
            case 8: monthName = "August"; break;
            case 9: monthName = "September"; break;
            case 10: monthName = "October"; break;
            case 11: monthName = "November"; break;
            case 12: monthName = "December"; break;
        }

        // add amount in the amount column
        values.put(COLUMN_AMOUNT, income.get_amount());
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MONTH, monthName);

        // This is the database references
        SQLiteDatabase db = getWritableDatabase();

        // The Command to be used
        db.insert(TABLE_INCOMES, null, values);
        db.close();
    }


    // Method to get all of the amounts
    public List <Integer> dbGetAmounts(String month, String year){
        List <Integer> sumsContainer = new ArrayList<Integer>();

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT SUM(_amount) as amounts_sum FROM " + TABLE_EXPENSES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "' GROUP BY _category ;";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){

            while(!c.isAfterLast()){
                int name = c.getInt(c.getColumnIndex("amounts_sum"));
                sumsContainer.add(name);
                c.moveToNext();
            }
        }
        return sumsContainer;
    }

    // Method to get all of the amounts
    public int dbIncomeAmount(String month, String year){
        int sum = 0;

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT SUM(_amount) as amounts_sum FROM " + TABLE_INCOMES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "';";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            sum = c.getInt(c.getColumnIndex("amounts_sum"));
        }
        return sum;
    }

    // Method to get all of the amounts
    public int dbExpenseAmount(String month, String year){
        int sum = 0;

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT SUM(_amount) as amounts_sum FROM " + TABLE_EXPENSES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "';";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            sum = c.getInt(c.getColumnIndex("amounts_sum"));
        }
        return sum;
    }

    // Method to get all of the amounts
    public int dbGetSavings(String month, String year){
        int sum = 0;

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT SUM(_saving) as amounts_sum FROM " + TABLE_EXPENSES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "'";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            sum = c.getInt(c.getColumnIndex("amounts_sum"));
        }
        return sum;

    }
    // Method to get all of the amounts
    public int dbGetExpenses(String month, String year){
        int sum = 0;

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT SUM(_amount) as amounts_sum FROM " + TABLE_EXPENSES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "'";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            sum = c.getInt(c.getColumnIndex("amounts_sum"));
        }
        return sum;

    }

    // Method to get all of the amounts
    public ArrayList<String> dbGetLabels(String month, String year){
        ArrayList <String> allCategories = new ArrayList<String>();

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT _category, SUM(_amount) as amounts_sum FROM " + TABLE_EXPENSES + " WHERE " +COLUMN_MONTH+ " = '" + month + "' AND " +COLUMN_YEAR+ " = '" + year + "' GROUP BY _category ;";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){

            while(!c.isAfterLast()){
                String name = c.getString(c.getColumnIndex("_category"));
                allCategories.add(name);
                c.moveToNext();
            }
        }
        return allCategories;

    }

    // Method to get all of the amounts
    public String[] dbGetYears(){
        List<String> years = new ArrayList<>();

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT _year FROM " + TABLE_EXPENSES + " GROUP BY _year;";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){

            while(!c.isAfterLast()){
                String name = c.getString(c.getColumnIndex("_year"));
                years.add(name);
                c.moveToNext();
            }
        }
        String[] allYears = new String[ years.size() ];
        years.toArray( allYears );

        return allYears;
    }

    // Method to get all of the amounts
    public String[] dbGetIncomeYears(){
        List<String> years = new ArrayList<>();

        // This is the database reference
        SQLiteDatabase db = getWritableDatabase();

        // This is the sql query
        String query = "SELECT _year FROM " + TABLE_INCOMES + " GROUP BY _year;";

        // cursor point to a location of a result
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){

            while(!c.isAfterLast()){
                String name = c.getString(c.getColumnIndex("_year"));
                years.add(name);
                c.moveToNext();
            }
        }
        String[] allYears = new String[ years.size() ];
        years.toArray( allYears );

        return allYears;
    }

}
