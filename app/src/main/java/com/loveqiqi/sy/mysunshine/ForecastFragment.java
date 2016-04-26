package com.loveqiqi.sy.mysunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sy on 2016/4/17.
 */
public class ForecastFragment extends Fragment{
    ListView listView;
    ArrayAdapter<String> adapter;
    public ForecastFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // updateWeather();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootVIew = inflater.inflate(R.layout.fragment_mail, container, false);
        String[] weathers = {"Today-Sunny-88/63", "Tommorrow-Foggy-70/46", "Wesd-Cloudy-72/63",
                "Thurs-Rainy-64/51", "Fri-Froggy-70/46", "Sat-Sunny-76/58"};
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(weathers));
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview,new ArrayList<String>());

        listView = (ListView) rootVIew.findViewById(R.id.ListViewForecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = (TextView) view;
//                String text = textView.getText().toString();
                String forecast = adapter.getItem(position);
//                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });
        return rootVIew;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_fresh:
                updateWeather();
                return true;
            case R.id.action_set:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_map:
                openPreferredLocationInMap();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPreferredLocationInMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String location = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        Uri geoLoaction = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLoaction);
        if((intent.resolveActivity(getContext().getPackageManager()) != null)){
            startActivity(intent);
        }

    }

    private void updateWeather() {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String syncConnPref = sharedPref.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
//        Log.e("syncConnPref", syncConnPref);
//        //ArrayAdapter<String> adapter = new ArrayAdapter<String>();
//        new FetchWeatherTask(getContext(), adapter).execute(syncConnPref);


        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weatherTask.execute(location);
    }
    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

}
