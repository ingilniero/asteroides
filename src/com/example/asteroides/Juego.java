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
		startService(new Intent(this, ServicioMusica.class));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		vistaJuego.getThread().pausar();
		vistaJuego.detenerSensor();
		stopService(new Intent(this, ServicioMusica.class));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		vistaJuego.getThread().reanudar();
		vistaJuego.registrarSensor();
	}
	
	@Override
	protected void onDestroy() {
		vistaJuego.getThread().detener();
		super.onDestroy();
	}
	
	

}
