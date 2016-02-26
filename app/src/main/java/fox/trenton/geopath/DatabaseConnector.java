package fox.trenton.geopath;

/**
 * Created by trenton on 2/24/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseConnector {

    // Declare Variables
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DB_NAME = "GeoPath";

    //Table Names
    private static final String TABLE_LOC = "LOCATION";
    private static final String TABLE_PATH = "PATH";
    private static final String TABLE_USERS = "USERS";

    //Common Column names
    private static final String USERID = "USERID";
    private static final String PATHID = "PATHID";

    //Path Table - Column names
    private static final String PATHLABEL = "LABEL";
    private static final String PATHDESCRIPTION = "DESCRIPTION";

    //Location Table - Column names
    private static final String LOCATIONID = "LOCATIONID";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String TIMEDATE = "TIMEDATE";
    private static final String TYPE = "TYPE";
    private static final String LOCLABEL = "LABEL";
    private static final String LOCDESCRIPTION = "DESCRIPTION";


    private SQLiteDatabase database;
    private DatabaseHelper dbOpenHelper;

    public DatabaseConnector(Context context) {
        dbOpenHelper = new DatabaseHelper(context);
    }

    // Open Database function
    public void open() throws SQLException {
        // Allow database to be in writable mode
        database = dbOpenHelper.getWritableDatabase();
    }

    // Close Database function
    public void close() {
        if (database != null)
            database.close();
    }


    //Location
    // Insert Location function
    public void InsertLocation(Location location, String type, String label, String description, String deviceID, String pathid) {
        String locationID = (location.getTime()) + deviceID;
        Date timestamp = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.getDefault());
        String timedate = sdf.format(timestamp);

        ContentValues newCon = new ContentValues();
        newCon.put(LOCATIONID, locationID);
        newCon.put(LATITUDE, location.getLatitude());
        newCon.put(LONGITUDE, location.getLongitude());
        newCon.put(USERID, deviceID);
        newCon.put(TIMEDATE, timedate);
        newCon.put(TYPE, type);
        newCon.put(LOCLABEL, label);
        newCon.put(LOCDESCRIPTION, description);
        newCon.put(PATHID, pathid);

        open();
        database.insert(TABLE_LOC, null, newCon);
        close();
    }

    // Update Location function
    public void UpdateLocation(String locID, Location location, String type, String label, String description, String deviceID, String pathid) {
        Date timestamp = new Date(location.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.getDefault());
        String timedate = sdf.format(timestamp);

        ContentValues editCon = new ContentValues();
        editCon.put(LOCATIONID, locID);
        editCon.put(LATITUDE, location.getLatitude());
        editCon.put(LONGITUDE, location.getLongitude());
        editCon.put(USERID, deviceID);
        editCon.put(TIMEDATE, timedate);
        editCon.put(TYPE, type);
        editCon.put(LOCLABEL, label);
        editCon.put(LOCDESCRIPTION, description);
        editCon.put(PATHID, pathid);

        open();
        database.update(TABLE_LOC, editCon, LOCATIONID + "=" + locID, null);
        close();
    }

    // Delete Location function
    public void DeleteLocation(long locID) {
        open();
        database.delete(TABLE_LOC, LOCATIONID + "=" + locID, null);
        close();
    }

    // Capture single data by ID
    public Cursor GetOneLocation(long locID) {
        return database.query(TABLE_LOC, null, LOCATIONID + "=" + locID, null, null,
                null, null);
    }


    //Path
    // Insert Path function
    public void InsertPath(String pathID, String deviceID, String label, String description) {
        ContentValues newCon = new ContentValues();
        newCon.put(PATHID, pathID);
        newCon.put(USERID, deviceID);
        newCon.put(LOCLABEL, label);
        newCon.put(LOCDESCRIPTION, description);

        open();
        database.insert(TABLE_LOC, null, newCon);
        close();
    }

    // Update Path function
    public void UpdatePath(String pathID, String deviceID, String label, String description) {
        ContentValues editCon = new ContentValues();
        editCon.put(PATHID, pathID);
        editCon.put(USERID, deviceID);
        editCon.put(LOCLABEL, label);
        editCon.put(LOCDESCRIPTION, description);

        open();
        database.update(TABLE_LOC, editCon, PATHID + "=" + pathID, null);
        close();
    }

    // Delete Path function
    public void DeletePath(long pathID) {
        open();
        database.delete(TABLE_LOC, PATHID + "=" + pathID, null);
        close();
    }

    // Capture single data by ID
    public Cursor GetOnePath(long pathID) {
        return database.query(TABLE_PATH, null, PATHID + "=" + pathID, null, null,
                null, null);
    }

    //Users
    // Insert Users function
    public void InsertUser(String deviceID) {
        ContentValues newCon = new ContentValues();
        newCon.put(USERID, deviceID);

        open();
        database.insert(TABLE_LOC, null, newCon);
        close();
    }

    //Check for existing user
    public Cursor GetUser(String deviceID) {
        return database.query(TABLE_USERS, null, USERID + "=" + deviceID, null, null,
                null, null);
    }

    //Check if empty
    public boolean CheckUsersEmpty() {
        Cursor c = database.query(TABLE_USERS, null, null, null, null, null, null);
        if (c.getCount() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
