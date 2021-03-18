package cz.fromgithub.bezholdingu.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.List;

import cz.fromgithub.bezholdingu.MainActivity;
import cz.fromgithub.bezholdingu.R;

public class AdsHelper {

    enum AdvState {
        SHOWING,
        HIDDEN
    }

    enum BroadcastState {
        ON,
        OFF
    }

    private static AdView mAdView;  // View s reklamou na formulari
    private static BroadcastState broadcastState = BroadcastState.OFF;  // priznak, jestli se aktualne odchytavaji zmeny datoveho pripojeni
    private static AdvState advState = AdvState.HIDDEN;  // priznak, jestli se aktualne odchytavaji zmeny datoveho pripojeni

    // aktivuje testovaci reklamy (pozor, pouze pro muj Sony telefon)
    public static void zapnoutTestovaciReklamy() {
        List<String> testDeviceIds = Arrays.asList("B91ABE6887BA67346C7F9AEC5F119C79");
        RequestConfiguration configuration = new	RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    // prvotni nicializace reklam (zatim se ale jejich nahravani a zobrazovani nespusti)
    public static void reklamyInit(MainActivity context) {

        mAdView = (AdView)context.findViewById(R.id.adView);

        // reklama
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                int test = 1;
            }
        });

        // pokud jsou reklamy v Nastaveni zapnute, spustit jejich zobrazovani
    //    AdsHelper.zapnoutReklamy(context);

        // udalosti reklamniho prouzku
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int test = 2;
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                int test = 3;
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    // public static String logText = "";

    // pokud jsou reklamy v Nastaveni zapnute, spustit jejich zobrazovani
    private static void zapnoutReklamy(Context context, byte wifiState) {
        // int currentConnectionType = getConnectionType(context);
        int nastaveniReklam = SettingsHelper.getSettingInt(context, SettingsHelper.Preference.ADS_ALLOW);
        boolean povolit = (nastaveniReklam == 2);
        if (nastaveniReklam == 1)
            povolit = wifiState == 1;
            // povolit = currentConnectionType > 0;

        // String logMsg = String.format("\n%s stav: %s, connType: %d, povol: %b", new SimpleDateFormat("ss.SSS").format(new Date()), advState.name(), currentConnectionType, povolit);
        // String logMsg = String.format("\n%s stav: %s, wifiState: %d, povol: %b", new SimpleDateFormat("ss.SSS").format(new Date()), advState.name(), wifiState, povolit);
        // logText += logMsg;
        // Toast.makeText(context, logText, Toast.LENGTH_LONG).show();


        if (povolit) {
            if (advState == AdvState.HIDDEN) {
                mAdView.setEnabled(true);
                mAdView.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                advState = AdvState.SHOWING;
            }
        }
        else {
            if (advState == AdvState.SHOWING) {
                mAdView.setEnabled(false);
                mAdView.setVisibility(View.GONE);
                advState = AdvState.HIDDEN;
            }
        }

        // pokud se maji reklamy zobrazovat jen pri wifi, tak spustit broadcast, jinak ho zastavit
        if (nastaveniReklam == 1)  // WiFi
            startBroadcast(context);    // broadcast spustit
        else
            stopBroadcast(context);     // broadcast zastavit
    }

    // pozastaveni broadcastu - ignorovat zmeny typu pripojeni
    public static void stopReklamy(MainActivity context) {
        stopBroadcast(context);     // odregistrovat receiver (broadcast zastavit)
    }

    // spusteni reklam, pokud byly mezitim zastaveny
    public static void restartReklamy(MainActivity context) {
        zapnoutReklamy(context, (byte)-1);
    }


    // Overi dostupnost datoveho pripojeni
    // POZOR toto bohuzel nefunguje dobre s broadcastem - ten triggeruje driv, nez tato metoda dokaze detekovat zmenu
    // (cili v debugu to vypada jako funkcni, v runtime ne). Takze to radej zjistuji uz v Broadcastu, zastaralym zpusobem.
    /*
    private static byte getConnectionType(Context context) {
        // Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return -3;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network ntwrk = cm.getActiveNetwork();
            if (ntwrk == null)
                return -5;

            NetworkCapabilities capabilities = cm.getNetworkCapabilities(ntwrk);
            if (capabilities != null) { // && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    return 0;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) // || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE))
                    return 1;
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    return 3;
                else
                    return -4;
            }
            else
                return -6;
        }
        else {
            return cm.getActiveNetworkInfo() == null ? -1 : (byte)0;    // jako by to bylo pres data
        }
        // return -2;
    }*/


    // zaregistrovat receiver pro zmenu typu pripojeni (pokud zatim zaregistrovany neni)
    private static void startBroadcast(Context context) {
        if (broadcastState == BroadcastState.OFF) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            context.registerReceiver(broadcastReceiver, intentFilter);
            broadcastState = BroadcastState.ON;
        }
    }

    // odregistrovat receiver pro zmenu typu pripojeni (pokud je zaregistrovany)
    private static void stopBroadcast(Context context) {
        if (broadcastState == BroadcastState.ON) {
            context.unregisterReceiver(broadcastReceiver);
            broadcastState = BroadcastState.OFF;
        }
    }

    // odchytavani zmeny typu datoveho pripojeni
    private static final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            byte wifiStatus = (networkInfo.getState() == NetworkInfo.State.CONNECTED) ? (byte)1 : (byte)0;

            zapnoutReklamy(context, wifiStatus);
        }
    };

}
