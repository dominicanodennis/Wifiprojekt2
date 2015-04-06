package de.compaso.wifisonde;

public class RssiRechner {
	
	int db;

	
	public  int rechneRSSIinProzent(int db) {

		int prozent = 0;
		prozent = 2 * (db + 100);
		if (db >= -50) {
			prozent = 100;
		} else if (db <= -100) {
			prozent = 0;
		}
		return prozent;
	}
}


