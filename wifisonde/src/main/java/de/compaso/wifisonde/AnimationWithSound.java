package de.compaso.wifisonde;

import android.content.Context;
import android.media.MediaPlayer;

import com.abhi.gif.lib.AnimatedGifImageView;
import com.abhi.gif.lib.AnimatedGifImageView.TYPE;


public class AnimationWithSound {

	int rssi;
	private AnimatedGifImageView animatedGifImageView;
	int switchValue;
	Context appContext;
	MediaPlayer mp;

	public AnimationWithSound(int rssi, Context appcontext) {
		super();
		this.rssi = rssi;
		this.appContext = appcontext;
	}

	public AnimatedGifImageView setAnimationAndSound(AnimatedGifImageView view) {

		if (rssi <= 0 && rssi >= -55) {
			switchValue = 1;
		} else if (rssi <= -56 && rssi >= -60) {
			switchValue = 2;
		} else if (rssi <= -61 && rssi >= -70) {
			switchValue = 3;
		} else if (rssi <= -71 && rssi >= -80) {
			switchValue = 4;
		} else if (rssi <= -81 && rssi >= -90) {
			switchValue = 5;
		} else if (rssi <= -91 && rssi >= -100) {
			switchValue = 6;

		}
		switch (switchValue) {
		case 1:
			view.setAnimatedGif(R.raw.gruen, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_five);
			break;
		case 2:
			view.setAnimatedGif(R.raw.gelb, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_four);
			break;
		case 3:
			view.setAnimatedGif(R.raw.blau, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_three);
			break;
		case 4:
			view.setAnimatedGif(R.raw.lila, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_two);
			break;
		case 5:
			view.setAnimatedGif(R.raw.rot, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_one);
			break;
		case 6:
			view.setAnimatedGif(R.raw.rot, TYPE.FIT_CENTER);
			mp = MediaPlayer.create(appContext, R.raw.song_one);
			break;

		default:
			animatedGifImageView.setAnimatedGif(R.raw.rot, TYPE.FIT_CENTER);
			// mp = Mediaplayer.create(appcontext, R.raw.song_one);
		}
		
		if (mp!=null) {
			mp.start();
		}
		return view;

	}

}
