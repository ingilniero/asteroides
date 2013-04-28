package com.example.asteroides;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity {
	private Button btnPuntuaciones;
	private Button btnPreferencias;
	private Button btnSalir;
	
	private MediaPlayer mp;
	
	private static final int ARRAY = 0;
	private static final int SHARED_PREFERENCES = 1;
	private static final int ARCHIVO_MEMORIA_INTERNA = 2;
	
	
	public static AlmacenPuntuaciones almacen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnPuntuaciones = (Button)findViewById(R.id.btn_puntuaciones);
        btnPreferencias = (Button)findViewById(R.id.btn_configurar);
        btnSalir = (Button)findViewById(R.id.btn_salir);
        
        tipoDeAlmacenamiento(this); 

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    
    public void lanzarJuego(View view) {
    	Intent i = new Intent(this, Juego.class);
    	startActivityForResult(i, 1234);
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(requestCode == 1234 && resultCode == RESULT_OK && data != null){
    		int puntos = data.getExtras().getInt("puntuacion");
    		String nombre = "Yo";
    		almacen.guardarPuntuacion(puntos, nombre, System.currentTimeMillis());
    		lanzarPuntuaciones(null);
    	}
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
   protected void onSaveInstanceState(Bundle savedInstanceState){
	   super.onSaveInstanceState(savedInstanceState);
	   if(mp != null){
		   int pos = mp.getCurrentPosition();
		   savedInstanceState.putInt("posicion",pos);
	   }
   }
   
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState){
	   super.onRestoreInstanceState(savedInstanceState);
	   if(savedInstanceState != null && mp != null){
		   int pos = savedInstanceState.getInt("posicion");
		   mp.seekTo(pos);
	   }
   }
   
   public static void tipoDeAlmacenamiento(Context context){
	   SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(context);
       String tipoAlmacenamiento = preferencias.getString("tipos_de_almacenamiento", String.valueOf(ARRAY));
       
       switch(Integer.parseInt(tipoAlmacenamiento)){
       case ARRAY:
    	   almacen = new AlmacenPuntuacionesArray();
    	   break;
       case SHARED_PREFERENCES:
    	   almacen = new AlmacenPuntuacionesPreferencias(context);
    	   break;
       case ARCHIVO_MEMORIA_INTERNA:
    	   almacen = new AlmacenPuntuacionesArhivoInterno(context);
    	   break;
       }
   }
   
   
   @Override
   protected void onStart(){
	   super.onStart();
   }
   
   @Override
   protected void onResume(){
	   super.onResume();
	   mp.start();
   }
   
   @Override
   protected void onPause(){
	   super.onPause();
	   mp.pause();
   }
   
   @Override
   protected void onStop(){
	   super.onStop();
   }
   
   @Override
   protected void onRestart(){
	   super.onRestart();
	   mp.start();
   }
   
   @Override
   protected void onDestroy(){
	   super.onDestroy();
   }
}
