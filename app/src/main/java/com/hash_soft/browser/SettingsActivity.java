package com.hash_soft.browser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity {

    private static final String KEY_EDIT_DEFAULT_HOMEPAGE = "default_homepage_set";
    private static final String KEY_DEFAULT_SEARCH_ENGINE_SET = "search_engine";
    private static final String KEY_DEVEOPER_INFO = "developer_info";
    private static final String KEY_PRO_VERSION="pro_version";


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setOnPreferenceChange(findPreference("search_engine"));
        setOnPreferenceChange(findPreference("default_homepage_set"));
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
             if (preference instanceof ListPreference) {
                if (preference.getKey().equals(KEY_DEFAULT_SEARCH_ENGINE_SET)) {
                    System.out.println("ListPreference");
                    ListPreference search_engine_listPreference = (ListPreference) findPreference("search_engine");
                    int index = search_engine_listPreference.findIndexOfValue(stringValue);
                    String Search_Engine_Summary = null;

                    switch (index) {
                        case 0:
                            Search_Engine_Summary = "Google (Default)";
                            break;
                        case 1:
                            Search_Engine_Summary = "Google Japan";
                            break;
                        case 2:
                            Search_Engine_Summary = "Naver";
                            break;
                        case 3:
                            Search_Engine_Summary = "Daum";
                            break;
                        default:
                            Search_Engine_Summary = "Google (Default)";
                    }
                    preference.setSummary(Search_Engine_Summary);
                    SharedPreferences selectSearchEngine_sharedPreferences = getSharedPreferences("selectSearchEngine", 0);
                    SharedPreferences.Editor selectSearchEditor = selectSearchEngine_sharedPreferences.edit();
                    selectSearchEditor.putInt("selectSearchEngine", index);
                    selectSearchEditor.commit();
                }
            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals(KEY_EDIT_DEFAULT_HOMEPAGE)) {
//                    EditTextPreference default_homepage_editText = (EditTextPreference) findPreference("default_homepage_set");
//                    String default_homepage_string = default_homepage_editText.getText().toString();
                    String DefHmpg = stringValue;
                    SharedPreferences DefHmpgSharedPref = getSharedPreferences("default_homepage_set", 0);
                    SharedPreferences.Editor DefHmpgEdit = DefHmpgSharedPref.edit();
                    DefHmpgEdit.putString("default_homepage_set", DefHmpg);
                    DefHmpgEdit.commit();
                    preference.setSummary(stringValue);
                }
            }
            return true;
        }
    };

    public boolean onPreferenceTreeClick (PreferenceScreen preferenceScreen, Preference preference) {
        if (preference instanceof PreferenceScreen) {
            if (preference.getKey().equals(KEY_DEVEOPER_INFO)) {
                System.out.println("PreferenceScreen");
                Intent developer_info = new Intent(SettingsActivity.this, DeveloperInfo.class);
                startActivity(developer_info);
            } else if (preference.getKey().equals(KEY_PRO_VERSION)){

            }
        }
        return true;
    }

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        onPreferenceChangeListener.onPreferenceChange(mPreference,
                PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }
}