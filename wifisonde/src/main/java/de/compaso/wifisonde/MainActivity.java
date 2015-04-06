package de.compaso.wifisonde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

	static WifiManager wifiManager;
	private WifiReceiver wifiReceiver;
	private ListView listView;
	private String wifiListe[];
	private Toast toast1, toast2, toast3, toast4;
	private List<ScanResult> scanResultate;
	private int netId;
	private String bssid, ssid;
	private boolean forward = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("CREATE", "On create");
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listView1);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		WifiManager.WifiLock lock = wifiManager.createWifiLock(
				WifiManager.WIFI_MODE_SCAN_ONLY, "Scan_only");

		lock.acquire();

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (!wifiManager.isWifiEnabled()) {
			Log.i("WIFI-DISABLED", "Wifi is diabled: enabling");

			toast4 = Toast
					.makeText(this, "schalte Wlan ein", Toast.LENGTH_LONG);
			toast4.setGravity(Gravity.CENTER, 0, 75);
			toast4.show();
			toast2 = Toast.makeText(this, "Wlannetze werden gesucht",
					Toast.LENGTH_LONG);
			toast2.setGravity(Gravity.CENTER, 0, 75);
			toast2.show();
			wifiManager.setWifiEnabled(true);

		} else {

			Helperclass helper1 = new Helperclass(wifiManager);
			helper1.disableAllNetworks(this.netId);

			NetworkInfo networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			Log.i("WIFI-Connected",
					"Is connected: " + networkInfo.isConnected());
		}

		Helperclass helper2 = new Helperclass(wifiManager);
		helper2.disableAllNetworks(this.netId);

		scanne();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				bssid = scanResultate.get(position).BSSID;
				ssid = scanResultate.get(position).SSID;

				finish();
				Intent intent = new Intent(MainActivity.this,
						TrackingActivity.class);
				intent.putExtra("mac", bssid);
				intent.putExtra("wifiname", ssid);
				forward = true;
				startActivity(intent);

			}
		});

		if (listView != null) {
			toast1 = Toast.makeText(getApplicationContext(),
					"Bitte klicken Sie\ndas gewünschte Wlannetz",
					Toast.LENGTH_LONG);
			toast1.setGravity(Gravity.CENTER, 0, 75);
			toast1.show();

		}
	}

	public void disableAllNetworks() {
		List<WifiConfiguration> wificonfigliste = wifiManager
				.getConfiguredNetworks();

		for (WifiConfiguration config : wificonfigliste) {
			this.netId = config.networkId;
			wifiManager.disableNetwork(this.netId);
		}

	}

	public void enableAllNetworks() {
		List<WifiConfiguration> wificonfigliste = wifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration config : wificonfigliste) {
			this.netId = config.networkId;
			wifiManager.enableNetwork(this.netId, true);
		}
	}

	public void scanne() {
		wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();

	}

	public void restartActivity() {
		finish();
		startActivity(getIntent());
		Helperclass helper3 = new Helperclass(wifiManager);
		helper3.disableAllNetworks(this.netId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			restartActivity();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
		unregisterReceiver(wifiReceiver);
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (!forward) {
			Helperclass helper4 = new Helperclass(wifiManager);
			helper4.enableAllNetworks(this.netId);
			enableAllNetworks();
		} else {
			forward = false;
		}
		super.onStop();
	}

	@Override
	protected void onStart() {
		if (!wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(true);
		Helperclass helper5 = new Helperclass(wifiManager);
		helper5.disableAllNetworks(this.netId);
		super.onStart();
	}

	protected void onResume() {
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Helperclass helper6 = new Helperclass(wifiManager);
		helper6.disableAllNetworks(this.netId);
		disableAllNetworks();

		super.onResume();
	}

	@Override
	public void onBackPressed() {
		Helperclass helper5 = new Helperclass(wifiManager);
		helper5.enableAllNetworks(this.netId);
		super.onBackPressed();
	}

	class WifiReceiver extends BroadcastReceiver {

		public void onReceive(Context c, Intent intent) {
			scanResultate = wifiManager.getScanResults();
			wifiListe = new String[scanResultate.size()];

			RssiRechner rssiRechner = new RssiRechner();

			Collections.sort(scanResultate, new Comparator<ScanResult>() {

				@Override
				public int compare(ScanResult lhs, ScanResult rhs) {
					return (lhs.level > rhs.level ? -1
							: (lhs.level == rhs.level ? 0 : 1));

				}
			});
			Map<String, ScanResult> filteredResults = new LinkedHashMap<>();

			for (ScanResult scanResult : scanResultate) {
				if (!filteredResults.containsKey(scanResult.SSID)
						|| scanResult.level > filteredResults
								.get(scanResult.SSID).level) {
					filteredResults.put(scanResult.SSID, scanResult);
				}
			}
			List<String> resultLinesList = new LinkedList<>();

			for (ScanResult scanResult : filteredResults.values()) {
				resultLinesList.add(scanResult.SSID + "  " + scanResult.level
						+ " dbm / "
						+ rssiRechner.rechneRSSIinProzent(scanResult.level)
						+ " %");
			}
			String[] resultLines = resultLinesList
					.toArray(new String[resultLinesList.size()]);

			listView.setAdapter(new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_list_item_1, resultLines));

			if (listView != null) {
				toast3 = Toast.makeText(getApplicationContext(),
						R.string.update_info, Toast.LENGTH_SHORT);
				toast3.setGravity(Gravity.CENTER, 0, 75);
				toast3.show();
			}
		}

	}

}
