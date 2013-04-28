package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Juego extends Activity {
	private VistaJuego vistaJuego;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		
		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
		vistaJuego.setPadre(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		vistaJuego.getThread().pausar();
		vistaJuego.detenerSensor();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		vistaJuego.getThread().reanudar();
		vistaJuego.registrarSensor();
		startService(new Intent(this, ServicioMusica.class));
	}

	
	@Override
	protected void onDestroy() {
		vistaJuego.getThread().detener();
		stopService(new Intent(this, ServicioMusica.class));
		super.onDestroy();
	}
	
	

}
