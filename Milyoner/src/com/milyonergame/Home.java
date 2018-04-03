package com.milyonergame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;

public class Home extends Activity
{
	private StartAppAd startAppAd = new StartAppAd(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		StartAppAd.init(this, "112710646", "201312831");
		
		setContentView(R.layout.activity_home);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		startAppAd.onResume();
	}
	
	@Override
	public void onBackPressed()
	{
		startAppAd.onBackPressed();
		super.onBackPressed();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		startAppAd.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void Start(View v)
	{
		startAppAd.showAd(new AdDisplayListener()
		{
			@Override
			public void adHidden(Ad ad)
			{
				Intent i = new Intent(Home.this, Game.class);
				startActivity(i);
			}
			@Override
			public void adDisplayed(Ad ad) {} 
		});
		startAppAd.loadAd();
	}
	
	public void End(View v)
	{
		startAppAd.showAd(new AdDisplayListener()
		{
			@Override
			public void adHidden(Ad ad)
			{
				finish();
			}
			@Override
			public void adDisplayed(Ad ad) {} 
		});
		startAppAd.loadAd();
	}

}
