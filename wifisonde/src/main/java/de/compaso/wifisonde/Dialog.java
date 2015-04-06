package de.compaso.wifisonde;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class Dialog extends DialogFragment {

	Activity activity;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	String detailProvider;

	public Dialog(Activity a, String detailProvader) {
		this.activity = a;
		this.detailProvider = detailProvader;
		builder = new AlertDialog.Builder(a);

		builder.setPositiveButton("Tracken", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Intent intent = new Intent(getActivity(),
				// TrackingActivity.class);
				// startActivity(intent);

			}
		});
		builder.setNeutralButton("zurück", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void showDialog() {

		builder.setTitle("Wifi Detailinformation");
		builder.setMessage(detailProvider);

		AlertDialog alert = builder.create();
		alert.show();

	}

}
