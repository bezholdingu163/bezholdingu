package cz.fromgithub.bezholdingu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import cz.fromgithub.bezholdingu.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // nastaveni toolbaru
        ActionBar supportActionBar=getSupportActionBar();
        if (supportActionBar!=null) {
            supportActionBar.setTitle("Nastavení");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        // naplnit formular daty
        nacistData();
    }

    // sipka zpet v toolbaru
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void nacistData()
    {
        // otevreme soubor pro cteni preferenci
        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);
        // nacteme ulozenou hodnotu
        boolean barcodeBeep = sharedPreferences.getBoolean("barcodeBeep", true);
        boolean barcodeVibrate = sharedPreferences.getBoolean("barcodeVibrate", false);
        boolean toman = sharedPreferences.getBoolean("toman", false);
        int displayOrientation = sharedPreferences.getInt("displayOrientation", 1);
        int dataNotification = sharedPreferences.getInt("dataNotification", 1);
        int adsAllow = sharedPreferences.getInt("adsAllow", 1);
        // String text2 = Integer.toString(sharedPreferences.getInt("text2", 0));

        // ulozime hodnoty do formulare
        ((Switch)findViewById(R.id.swPipnout)).setChecked(barcodeBeep);
        ((Switch)findViewById(R.id.swZavibrovat)).setChecked(barcodeVibrate);

        ((Switch)findViewById(R.id.swToman)).setChecked(toman);

        if(displayOrientation == 0)
            ((RadioButton) findViewById(R.id.rb_otaceni_neblokovat)).setChecked(true);
        else if(displayOrientation == 1)
            ((RadioButton) findViewById(R.id.rb_otaceni_portrait)).setChecked(true);
        else if(displayOrientation == 2)
            ((RadioButton) findViewById(R.id.rb_otaceni_landscape)).setChecked(true);

        if(dataNotification == 0)
            ((RadioButton) findViewById(R.id.rb_aktualizace_vypnout)).setChecked(true);
        else if(dataNotification == 1)
            ((RadioButton) findViewById(R.id.rb_aktualizace_mesic)).setChecked(true);
        else if(dataNotification == 2)
            ((RadioButton) findViewById(R.id.rb_aktualizace_3mesice)).setChecked(true);

        if(adsAllow == 2)
            ((RadioButton) findViewById(R.id.rb_reklamy_povolit)).setChecked(true);
        else if(adsAllow == 1)
            ((RadioButton) findViewById(R.id.rb_reklamy_wifi)).setChecked(true);
        else if(adsAllow == 0)
            ((RadioButton) findViewById(R.id.rb_reklamy_zakazat)).setChecked(true);
    }

    public void ulozitZmenu(View sender)
    {
        // otevreme soubor pro zapis preferenci
        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences.xml", Context.MODE_PRIVATE);
        // vytvorime objekt editor preferenci
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // pipnuti/vibrace
        if (sender.getId()==R.id.swPipnout) {
            editor.putBoolean("barcodeBeep", ((Switch)sender).isChecked());
        }
        else if (sender.getId()==R.id.swZavibrovat) {
            editor.putBoolean("barcodeVibrate", ((Switch)sender).isChecked());
        }

        // Toman
        if (sender.getId()==R.id.swToman) {
            editor.putBoolean("toman", ((Switch)sender).isChecked());
        }

        // otaceni displeje
        if (sender.getId()==R.id.rb_otaceni_neblokovat) {
            editor.putInt("displayOrientation", 0);
        }
        else if (sender.getId()==R.id.rb_otaceni_portrait) {
            editor.putInt("displayOrientation", 1);
        }
        else if (sender.getId()==R.id.rb_otaceni_landscape) {
            editor.putInt("displayOrientation", 2);
        }

        // notifikace na zastarala data
        if (sender.getId()==R.id.rb_aktualizace_vypnout) {
            editor.putInt("dataNotification", 0);
        }
        else if (sender.getId()==R.id.rb_aktualizace_mesic) {
            editor.putInt("dataNotification", 1);
        }
        else if (sender.getId()==R.id.rb_aktualizace_3mesice) {
            editor.putInt("dataNotification", 2);
        }

        // reklamy
        if (sender.getId()==R.id.rb_reklamy_povolit) {
            Toast.makeText(this, "Děkujeme, že si necháváte zobrazovat reklamy. Podporujete tím další rozvoj aplikace.", Toast.LENGTH_LONG).show();
            editor.putInt("adsAllow", 2);
        }
        else if (sender.getId()==R.id.rb_reklamy_wifi) {
            Toast.makeText(this, "Reklamy se budou zobrazovat pouze pokud budete připojeni k internetu přes WiFi.\nDěkujeme, že si je necháváte zobrazovat. Podporujete tím další rozvoj aplikace.", Toast.LENGTH_LONG).show();
            editor.putInt("adsAllow", 1);
        }
        else if (sender.getId()==R.id.rb_reklamy_zakazat) {
            Toast.makeText(this, "Reklamy se v aplikaci nebudou zobrazovat.\nPokud se je rozhodnete v budoucnu opět zapnout, podpoříte tím další rozvoj aplikace.", Toast.LENGTH_LONG).show();
            editor.putInt("adsAllow", 0);
        }

        // data ulozime
        editor.apply();
    }
}