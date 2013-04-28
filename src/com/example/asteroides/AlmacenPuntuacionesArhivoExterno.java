package com.example.asteroides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class AlmacenPuntuacionesArhivoExterno implements AlmacenPuntuaciones{
	private static String ARCHIVO = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";
	private Context context;

	public AlmacenPuntuacionesArhivoExterno(Context context){
		this.context = context;
	}
	
	
	@Override
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {
		try {
			FileOutputStream f = new FileOutputStream(ARCHIVO, true);
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
			FileInputStream f = new FileInputStream(ARCHIVO);
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
