package de.compaso.wifisonde;

import java.util.List;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class Helperclass {

	int netId;
	WifiManager wifiManager;// = MainActivity.wifiManager;
	
	// Versuch wifiManager als Constructor zu übergeben

	public Helperclass(WifiManager wifiManager) {
		super();
		this.wifiManager = wifiManager;
	}

	public void disableAllNetworks(int netId) {
		this.netId = netId;
		List<WifiConfiguration> wificonfigliste = wifiManager
				.getConfiguredNetworks();

		for (WifiConfiguration config : wificonfigliste) {
			this.netId = config.networkId;
			wifiManager.disableNetwork(this.netId);
		}

	}

	public void enableAllNetworks(int netId) {
		this.netId = netId;
		List<WifiConfiguration> wificonfigliste = wifiManager
				.getConfiguredNetworks();

		for (WifiConfiguration config : wificonfigliste) {
			this.netId = config.networkId;
			wifiManager.enableNetwork(this.netId, false);
		}

	}
}
