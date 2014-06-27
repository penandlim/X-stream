package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

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
                    Toast.makeText(mContext, "Yet to be implemented",
                            Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }
}
