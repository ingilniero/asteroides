package com.example.asteroides;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View {
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
	
	public VistaJuego(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		Drawable drawableNave, drawableAsteroide, drawableMisil;
		
		drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		
		nave = new Grafico(this, drawableNave);
		asteroides = new Vector<Grafico>();
		
		
		for (int i = 0; i < numAsteroides; i++){
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			
			asteroides.add(asteroide);
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
//				ActivaMisil();
			}
			break;
		}
		mX = x;
		mY = y;
		return true;
		
	}
	
	
	class ThreadJuego extends Thread {
		@Override
		public void run(){
			while(true) {
				actualizaFisica();
			}
		}
	}
}
