package com.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);
	}

	@Override
	protected void onStop(){
		super.onStop();
		Asteroides.tipoDeAlmacenamiento(this);
	}
}
