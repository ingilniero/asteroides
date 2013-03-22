package com.example.asteroides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServicioMusica extends Service{
	MediaPlayer reproductor;
	private NotificationManager nm;
	private static final int ID_NOTIFICACION_CREAR = 1;

	@Override
	public void onCreate() {
		reproductor = MediaPlayer.create(this, R.raw.musica);

		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int idArranque){
		reproductor.start();

		Notification notificacion = new Notification(R.drawable.ic_launcher, "Saliendo de Asteroides", System.currentTimeMillis());

		PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0, new Intent(this, Juego.class), 0);

		notificacion.setLatestEventInfo(this, "Asteroides", "Pulse para volver al juego.", intencionPendiente);

		nm.notify(ID_NOTIFICACION_CREAR, notificacion);

		return START_STICKY;
	}

	@Override
	public void onDestroy(){
		reproductor.stop();
		nm.cancel(ID_NOTIFICACION_CREAR);
	}

	@Override
	public IBinder onBind(Intent intent){
		return null;
	}

}
