package com.example.top10downloadedapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listView;
    private TextView textView;
    private String feedCachedUrl = "INVALIDATED";
    public static final String STATE_URL = "feedURL";
    String feedUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.xmlListView);
        textView = (TextView) findViewById(R.id.appTextView);

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL);
        }
        downloadURL("https://www.indiatoday.in/rss/1206584");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuSports:
                feedUrl = "https://www.indiatoday.in/rss/1206550";
                break;
            case R.id.menuNews:
                feedUrl = "https://www.indiatoday.in/rss/1206584";
                break;
            case R.id.menuWorld:
                feedUrl = "https://www.indiatoday.in/rss/1206577";
                break;
            case R.id.menuCover:
            case R.id.menuBig:
                Log.i(TAG, "onOptionsItemSelected: Story  " + item.isChecked());
                if (item.isChecked()) {
                    Log.i(TAG, "Big Story: ");
                    feedUrl = "https://www.indiatoday.in/rss/1206509";
                } else {
                    Log.i(TAG, "Cover Story: y wala islilye chl raha h kuki jo big story wala item h bo checked nh h! ");
                    feedUrl = "https://www.indiatoday.in/rss/1206614";
                }
                break;
            case R.id.menuRefresh:
                feedCachedUrl = "INVALIDATED";
                break;
            default:
                return onOptionsItemSelected(item);
        }
        downloadURL(feedUrl);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL, feedUrl);
        super.onSaveInstanceState(outState);
    }

    private void downloadURL(String url) {
        if (!url.equalsIgnoreCase(feedCachedUrl)) {
            DownloadData data = new DownloadData();
            data.execute(url);
            feedCachedUrl = url;
        } else {
            Log.i(TAG, "downloadURL: not downloaded");
        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String xmlData) {
            super.onPostExecute(xmlData);
//            Log.d(TAG, "onPostExecute: parameter is " + xmlData);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(xmlData);

//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
//                    MainActivity.this, R.layout.app_layout, parseApplications.getApplications());
//            listView.setAdapter(arrayAdapter);
            FeedAdapter feedAdapter = new FeedAdapter(
                    MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listView.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
//            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charRead;
                char inputBuffer[] = new char[500];
                while (true) {
                    charRead = reader.read(inputBuffer);
                    if (charRead < 0) {
                        break;
                    }
                    if (charRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charRead));
                    }
                }
                Log.i(TAG, "downloadXML: " + xmlResult.toString());
                reader.close();
                return xmlResult.toString();

            } catch (MalformedURLException me) {
                Log.e(TAG, "downloadXML: Invalid URL " + me.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data " + e.getMessage());
            } catch (SecurityException se) {
                Log.e(TAG, "downloadXML: Security Exception. Needs Permission? " + se.getMessage());
            }
            return null;
        }
    }
}