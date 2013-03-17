package com.example.asteroides;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener{
	private Vector<Grafico> asteroides;
	private int numAsteroides = 5;
	private int numFragmentos = 3;
	private Grafico nave;
	private int giroNave;
	private float aceleracionNave;
	private float mX = 0, mY = 0;
	private boolean disparo = false;
	
	private static final int PASE_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;
	
	private ThreadJuego thread = new ThreadJuego();
	private static int PERIODO_PROCESO = 50;
	private long ultimoProceso = 0;
	
	private boolean hayValorInicial = false;
	private float valorInicial;
	
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private boolean misilActivo = false;
	private int tiempoMisil;
	
	public VistaJuego(Context context, AttributeSet attrs){
		
		super(context, attrs);
		
		Drawable drawableNave, drawableAsteroide, drawableMisil;
		
		drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		
		nave = new Grafico(this, drawableNave);
		asteroides = new Vector<Grafico>();
		
		ShapeDrawable dMisil  = new ShapeDrawable(new RectShape());
		dMisil.getPaint().setColor(Color.WHITE);
		dMisil.getPaint().setStyle(Style.STROKE);
		dMisil.setIntrinsicWidth(15);
		dMisil.setIntrinsicHeight(3);
		drawableMisil = dMisil;
		
		misil = new Grafico(this, dMisil);
		
		
		for (int i = 0; i < numAsteroides; i++){
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			
			asteroides.add(asteroide);
		}
		
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		
		if(!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	synchronized protected void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		
		if ( ultimoProceso + PERIODO_PROCESO > ahora){
			return;
		}
		
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;
		
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		
		if(Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()){
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}	
		
		nave.incrementaPos(retardo);
		for(Grafico asteroide : asteroides){
			asteroide.incrementaPos(retardo);
		}
		
		if(misilActivo){
			misil.incrementaPos(retardo);
			tiempoMisil -= retardo;
			
			if(tiempoMisil < 0){
				misilActivo = false;
			}else {
				for(int i = 0; i < asteroides.size(); i++){
					if(misil.verificaColision(asteroides.elementAt(i))){
						destruyeAsteroide(i);
						break;
					}
				}
			}
		}
	}
	
	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter)
	{
		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
		
		nave.setPosX(ancho / 2);
		nave.setPosY(alto / 2);
		
		for(Grafico asteroide: asteroides){
			do {
				asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));								
			} while(asteroide.distancia(nave) < nave.getAlto() + nave.getAncho() );
		}
		
		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}
	
	@Override
	synchronized protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		nave.dibujarGrafico(canvas);
		
		if(misilActivo){
			misil.dibujarGrafico(canvas);
		}
		
		for(Grafico asteroide: asteroides){
			asteroide.dibujarGrafico(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent event){
		super.onTouchEvent(event);
		
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			disparo = true;
			break;
			
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs( x - mX);
			float dy = Math.abs( y - mY);
			if( dy < 6 && dx > 6){
				giroNave = Math.round(( x - mX) / 2);
				disparo = false;
			} else if(dx < 6 && dy > 6){
				aceleracionNave = Math.round((mY - y) / 25);
				disparo = false;
			}
			break;
			
		case MotionEvent.ACTION_UP:
			giroNave = 0;
			aceleracionNave = 0;
			if(disparo){
				ActivaMisil();
			}
			break;
		}
		mX = x;
		mY = y;
		return true;
		
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){
	
	}
	
	
	@Override
	public void onSensorChanged(SensorEvent event){
		float valor = event.values[1];
		if (!hayValorInicial) {
			valorInicial = valor;
			hayValorInicial = true;
		}
		giroNave = (int) (valor - valorInicial) / 3;
	}
	
	private void destruyeAsteroide(int i){
		asteroides.remove(i);
		misilActivo = false;
	}
	
	private void ActivaMisil() {
		misil.setPosX(nave.getPosX()+ nave.getAncho() / 2 - misil.getAncho() / 2);
		misil.setPosY(nave.getPosY()+ nave.getAncho() / 2 - misil.getAlto() / 2);
		
		misil.setAngulo(nave.getAngulo());
		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
		
		tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
		misilActivo = true;
	}
	
	class ThreadJuego extends Thread {
		private boolean pausa, corriendo;
		
		public synchronized void pausar(){
			pausa = true;
		}
		
		public synchronized void reanudar(){
			pausa = false;
			notify();
		}
		
		public void detener(){
			corriendo = false;
			if( pausa) reanudar();
		}
		
		@Override
		public void run(){
			corriendo = true;
			
			while(corriendo) {
				actualizaFisica();
				
				synchronized (this){
					while(pausa){
						try {
							wait();
						} catch(Exception e){
							
						}
					}
				}
			}
		}
	}

	public ThreadJuego getThread() {
		return thread;
	}
	
}
