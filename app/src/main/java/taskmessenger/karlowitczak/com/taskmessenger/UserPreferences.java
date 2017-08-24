package taskmessenger.karlowitczak.com.taskmessenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jakub on 03.05.2017.
 */
public class UserPreferences {

    static String PREF_LOGIN = "username";
    static String PREF_SAVED_MESSAGE = "savedMessage";
    static String SAVED_ROOM_NAME = "savedRoomMessage";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // needed for keeping the login state
    public static void setLogin(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGIN, userName);
        editor.apply();
    }

    public static String getLogin(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGIN, "");
    }

    public static void saveMessageInput(Context ctx, String message) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SAVED_MESSAGE, message);
        editor.apply();
    }

    public static String getMessageInput(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_SAVED_MESSAGE, "");
    }

    public static void saveRoomName(Context ctx, String message) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SAVED_ROOM_NAME, message);
        editor.apply();
    }

    public static String getSavedMessageRoomName(Context ctx) {
        return getSharedPreferences(ctx).getString(SAVED_ROOM_NAME, "");
    }

}
