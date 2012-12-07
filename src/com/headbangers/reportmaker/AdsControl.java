package com.headbangers.reportmaker;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.AdRequest.Gender;

import android.app.Activity;
import android.view.View;

public class AdsControl {

	private static final boolean IS_ENABLE = true;

	public static void buildAdIfEnable(Activity activity) {
		final AdView adView = (AdView) activity.findViewById(R.id.adView);
		if (IS_ENABLE) {
			AdRequest adRequest = new AdRequest();

			adRequest.setGender(Gender.MALE);
			adRequest.addKeyword("warhammer");
			adRequest.addKeyword("40000");
			adRequest.addKeyword("40k");
			adRequest.addKeyword("wargame");
			adRequest.addKeyword("battle");
			adRequest.addKeyword("bataille");
			adRequest.addKeyword("report");
			adRequest.addKeyword("rapport");
			adRequest.addKeyword("modelism");
			adRequest.addKeyword("modelisme");
			adRequest.addKeyword("games");
			adRequest.addKeyword("GW");

			adRequest.addTestDevice("EE8FDD470A72D400B66510DA5A45EBA0");
			
			adView.loadAd(adRequest);
		} else {
			adView.setVisibility(View.GONE);
		}
	}
}
