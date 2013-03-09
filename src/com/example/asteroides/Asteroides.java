package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity {
	private Button btnSalir;
	private Button btnPreferencias;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnSalir = (Button)findViewById(R.id.btn_salir);
        btnPreferencias = (Button)findViewById(R.id.btn_configurar);
        
        btnSalir.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				lanzarSalir();
			}
        });
        
        btnPreferencias.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
        		lanzarPreferencias();
        	}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
     		
    public void lanzarAcercaDe(View view){
    	Intent i = new Intent(this, AcercaDe.class);
    	startActivity(i);
    }
    
    public void lanzarSalir() {
    	finish();
    }
    
    public void lanzarPreferencias() {
    	Intent i = new Intent(this, Preferencias.class);
    	startActivity(i);
    }
    
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	   
	   switch (item.getItemId()) {
	   case R.id.action_acerca_de:
		   lanzarAcercaDe(null);
		   break;
	   case R.id.action_config:
		   lanzarPreferencias();
		   break;
	   }
	   return true;
   }
}
