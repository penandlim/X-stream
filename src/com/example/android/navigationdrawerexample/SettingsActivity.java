package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GoodComp on 6/25/2014.
 */

public class SettingsActivity extends PreferenceActivity {
    public static Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        //SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
    }
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preference);
            Preference enterLoginCredential = (Preference) findPreference("pref_login");
            assert enterLoginCredential != null;
            enterLoginCredential.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String streamURL = "";
                    new xvideoURLToStream().execute("http://www.xvideos.com/video7153264/true_anjelica_in_wowporn_in_the_outdoor");
                    return true;
                }
            });
        }
    }

    static class xvideoURLToStream extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String...urls) {
            String streamurl = "";
            try {
                streamurl = xvid_source(urls[0]);
                return streamurl;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return streamurl;
        }

        @Override
        protected void onPostExecute(String streamurl){
            play_video(streamurl);
        }

        // Parse URL and return the stream link on www.xvideos.com
        public String xvid_source(String url) throws IOException {
            //gets the line full of junk, turns into html doc for jsoup, and converts into the wanted vid url
            String line = Jsoup.parse(UrlToHtml(url)).select("embed").attr("flashvars");
            //makes the vid url into a proper url
            Pattern pattern = Pattern.compile("(?<=flv_url=).+(?=&url_bigthumb=)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group().replace("%3D", "=").replace("%26", "&").replace("%3F", "?")
                        .replace("%3B", ";").replace("%2F", "/").replace("%3A", ":");
            }
            else{
                return "";
            }
        }

        // Function to parse URL to HTML. Should work on all websites
        public String UrlToHtml(String give_url) throws IOException {
            URL url = new URL(give_url);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();
            return a.toString();
        }
        public static void play_video(String streamURL){
            if (!streamURL.equals("")) {
                Uri url_to_video = Uri.parse(streamURL);
                Intent intent = new Intent(Intent.ACTION_VIEW, url_to_video);
                intent.setDataAndType(url_to_video, "video/*");
                mContext.startActivity(intent);
            }
        }
    }
}
