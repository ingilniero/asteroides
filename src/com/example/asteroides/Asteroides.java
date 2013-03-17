package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Asteroides extends Activity {
	private Button btnPuntuaciones;
	private Button btnPreferencias;
	private Button btnSalir;
	
	private MediaPlayer mp;
	
	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnPuntuaciones = (Button)findViewById(R.id.btn_puntuaciones);
        btnPreferencias = (Button)findViewById(R.id.btn_configurar);
        btnSalir = (Button)findViewById(R.id.btn_salir);
        
        btnPuntuaciones.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				lanzarPuntuaciones(null);
			}
        });
        
        btnPreferencias.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
        		lanzarPreferencias(null);
        	}
        });
        
        btnSalir.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View view){
        		lanzarSalir(null);
        	}
        });
        mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    
    public void lanzarJuego(View view) {
    	Intent i = new Intent(this, Juego.class);
    	startActivity(i);
    }
     		
    public void lanzarAcercaDe(View view){
    	Intent i = new Intent(this, AcercaDe.class);
    	startActivity(i);
    }
    
    public void lanzarPuntuaciones(View view) {
    	Intent i = new Intent(this, Puntuaciones.class);
    	startActivity(i);
    }
    
    public void lanzarPreferencias(View view) {
    	Intent i = new Intent(this, Preferencias.class);
    	startActivity(i);
    }
    
    public void lanzarSalir(View view) {
    	finish();
    }
    
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	   
	   switch (item.getItemId()) {
	   case R.id.action_acerca_de:
		   lanzarAcercaDe(null);
		   break;
	   case R.id.action_config:
		   lanzarPreferencias(null);
		   break;
	   }
	   return true;
   }
   
   
   @Override
   protected void onStart(){
	   super.onStart();
	   Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
   }
   
   @Override
   protected void onResume(){
	   super.onResume();
	   mp.start();
	   Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
   }
   
   @Override
   protected void onPause(){
	   super.onPause();
	   Toast.makeText(this,"onPause", Toast.LENGTH_SHORT).show();
	   mp.pause();
   }
   
   @Override
   protected void onStop(){
	   super.onStop();
	   Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	   
   }
   
   @Override
   protected void onRestart(){
	   super.onRestart();
	   Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
   }
   
   @Override
   protected void onDestroy(){
	   Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	   super.onDestroy();
   }
}
