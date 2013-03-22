package com.example.asteroides;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServicioMusica extends Service{
	MediaPlayer reproductor;

	
	@Override
	public void onCreate() {
		reproductor = MediaPlayer.create(this, R.raw.musica);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int idArranque){
		reproductor.start();
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy(){
		reproductor.stop();
	}
	
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}

}
