package org.microg.gms.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.R

class AccountsFragment: PreferenceFragmentCompat() {

    private val TAG = AccountsFragment::class.java.simpleName

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_accounts)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findPreference<Preference>("pref_manage_accounts")?.setOnPreferenceClickListener {
            try {
                startActivity(Intent(Settings.ACTION_SYNC_SETTINGS))
            } catch (activityNotFoundException: ActivityNotFoundException) {
                Log.e(TAG, "Failed to launch sync settings", activityNotFoundException)
            }
            true
        }
    }
}
