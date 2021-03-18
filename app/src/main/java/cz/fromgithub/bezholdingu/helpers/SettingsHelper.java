package cz.fromgithub.bezholdingu.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsHelper {


    public enum Preference {
        FIRST_RUN,
        BARCODE_BEEP,
        BARCODE_VIBRATE,
        TOMAN,
        DISPLAY_ORIENTATION,
        DATA_NOTIFICATION,
        DATA_LASTNOTIFICATION,
        DATA_LASTDOWNLOAD,
        ADS_ALLOW
    }

    public static boolean getSettingBoolean (Context cont, Preference pref) {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = cont.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);

        // prvni spusteni po instalaci
        if (pref == Preference.FIRST_RUN) {
            // rovnou do preferenci zapiseme false - true to ma byt jen jednou jedinkrat
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            return sharedPreferences.getBoolean("firstRun", false);
        }

        // nacteme ulozenou hodnotu
        if (pref == Preference.BARCODE_BEEP)
            return sharedPreferences.getBoolean("barcodeBeep", true);
        else if (pref == Preference.BARCODE_VIBRATE)
            return sharedPreferences.getBoolean("barcodeVibrate", false);
        else if (pref == Preference.TOMAN)
            return sharedPreferences.getBoolean("toman", false);
        else
            return false;
        //throw new Exception("Chyba - pokus o čtení neexistujícího nastavení.");

    }

    public static int getSettingInt (Context cont, Preference pref) {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = cont.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);

        // nacteme ulozenou hodnotu
        if (pref == Preference.DISPLAY_ORIENTATION)
            return sharedPreferences.getInt("displayOrientation", 1);
        else if (pref == Preference.ADS_ALLOW)
            return sharedPreferences.getInt("adsAllow", 1);
        else if (pref == Preference.DATA_NOTIFICATION)
            return sharedPreferences.getInt("dataNotification", 1);
        else
            return 0;
    }

    public static String getSettingString (Context cont, Preference pref) {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = cont.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);

        // nacteme ulozenou hodnotu
        if (pref == Preference.DATA_LASTDOWNLOAD)
            return sharedPreferences.getString("lastDataDownload", "");
        if (pref == Preference.DATA_LASTNOTIFICATION)
            return sharedPreferences.getString("lastDataNotification", "");
        else
            return "";
    }

    public static void setSettingString (Context cont, Preference pref, String value) {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = cont.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);

        /*
        // vytvorime objekt editor preferenci
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("XXX doplnit nazev klice XXX, value);

        // data ulozime
        editor.apply();
         */
    }

    public static void setSettingStringDate (Context cont, Preference pref, Date date) {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = cont.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);

        // vytvorime objekt editor preferenci
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // zjisteni a naformatovani aktualniho data
        String value = new SimpleDateFormat("d.M.yyyy").format(date);
        if (pref == Preference.DATA_LASTDOWNLOAD)
            editor.putString("lastDataDownload", value);
        if (pref == Preference.DATA_LASTNOTIFICATION)
            editor.putString("lastDataNotification", value);

        // data ulozime
        editor.apply();
    }

}
