package org.myaqua.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.myaqua.R;
import org.myqaua.arcusweather.forecastio.ForecastIO;
import org.myqaua.arcusweather.forecastio.ForecastIODataPoint;
import org.myqaua.arcusweather.forecastio.ForecastIOResponse;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ForcastIOFragment extends Fragment {
	private final String API_KEY = "f7786cefe243362b557b67130c4663c9";
	private final Double LATITUDE = 9.546697; // 9.546697,-98.621796
	private final Double LONGITUDE = -98.621796; // 37.8267,-122.423

	public ForcastIOFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_forcast_io, container, false);
		return rootView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new ForecastIOAsync().execute();
		Log.e("ForcastIOFragment", "onResume");

	}

	@SuppressWarnings("unused")
	private void forecastIOStuff(ForecastIOResponse FIOR) {
		Log.e("ForcastIOFragment", "forcastIOStuff");
		// The library provides an easy way to access values as strings and data points as a list.
		String currentSummary = FIOR.getValue("current-summary");
		String thirdHourlyTemperature = FIOR.getValue("hourly-2-temperature");
		String firstDailyIcon = FIOR.getValue("daily-0-icon");

		// alerts defaults to first alert if not given an index. (Usually there is only one alert).
		String alertDescription = FIOR.getValue("alerts-description");

		ForecastIODataPoint[] minutelyPoints = FIOR.getDataPoints("minutely");
		// double thirtiethMinutePrecipitation = minutelyPoints[29].getValueAsDouble("precipitationIntensity");

		ForecastIODataPoint[] hourlyPoints = FIOR.getDataPoints("hourly");
		ForecastIODataPoint[] dailyPoints = FIOR.getDataPoints("daily");

		for (int i = 0; i < dailyPoints.length; i++) {

			// int day of week
			Date date = new Date();
			date.setTime((Long.valueOf(dailyPoints[i].getValue("time"))) * 1000);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

			// name of weekday
			String weekDay;
			SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

			weekDay = dayFormat.format(c.getTime());
			Log.e("ForcastIOFragment", "dayOfWeek: " + dayOfWeek + " " + weekDay);
			Log.e("ForcastIOFragment", dailyPoints[i].getValue("precipProbability"));
			Log.e("ForcastIOFragment", "precipProbability " + (Double.valueOf(dailyPoints[i].getValue("precipProbability")) * 100) + "%");
		}

		// you can also do it the hard way
		// String currentSummary2 = FIOR.getCurrently().getValue("summary");
		// String firstDailyIcon2 = FIOR.getDaily().getData()[0].getValue("icon");

	}

	private class ForecastIOAsync extends AsyncTask<Void, Void, Void> {

		ForecastIOResponse FIOR;

		@Override
		protected void onPostExecute(Void result) {
			forecastIOStuff(FIOR);
			Log.e("ForcastIOFragment", "onPostExecute");
		}

		@Override
		protected Void doInBackground(Void... params) {
			ForecastIO FIO = new ForecastIO(API_KEY, LATITUDE, LONGITUDE);
			// ability to set the units, exclude blocks, extend options and user agent for the request. This is not required.
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("units", "us");
			requestParams.put("userAgent", "Custom User Agent 1.0");
			FIO.setRequestParams(requestParams);
			FIO.makeRequest();

			String responseString = FIO.getResponseString();
			Log.e("ForcastIOFragment", "responseString: " + responseString);
			FIOR = new ForecastIOResponse(responseString);
			Log.e("ForcastIOFragment", "doInBackground");
			return null;
		}
	}

}
