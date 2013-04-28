package com.example.asteroides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.util.Log;

public class AlmacenPuntuacionesArhivoInterno implements AlmacenPuntuaciones{
	private static String ARCHIVO = "puntuaciones.txr";
	private Context context;

	public AlmacenPuntuacionesArhivoInterno(Context context){
		this.context = context;
	}
	
	
	@Override
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {
		try {
			FileOutputStream f = context.openFileOutput(ARCHIVO, Context.MODE_APPEND);
			String texto = puntos + " "+ nombre + "\n";
			f.write(texto.getBytes());
			f.close();
		} catch(Exception e) {
			Log.e("Asteroides", e.getMessage(), e);
		}
		
	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {
		Vector<String> result = new Vector<String>();
		try{
			FileInputStream f = context.openFileInput(ARCHIVO);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
			
			int n = 0;
			String linea;
			do {
				linea = entrada.readLine();
				if (linea != null){
					result.add(linea);
					n++;
				}
			}while (n < cantidad && linea != null);
			f.close();
		} catch(Exception e){
			Log.e("Asteroides", e.getMessage(), e);
		}
		return result;
	}
}
