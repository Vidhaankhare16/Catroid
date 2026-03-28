/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2025 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.ui.settingsfragments

import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.EditTextPreference
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import org.catrobat.catroid.R
import org.catrobat.catroid.devices.mqtt.MqttSettings

class MqttSettingsFragment : PreferenceFragment() {

    companion object {
        @JvmField
        val TAG: String = MqttSettingsFragment::class.java.simpleName
        const val MQTT_CONNECTION_SETTINGS_CATEGORY = "setting_mqtt_connection_settings_category"
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = preferenceScreen.title
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        SettingsFragment.setToChosenLanguage(activity)
        addPreferencesFromResource(R.xml.mqtt_preferences)

        val mqttCheckBox = findPreference(MqttSettings.SETTINGS_SHOW_MQTT_BRICKS) as CheckBoxPreference
        val connectionSettings = findPreference(MQTT_CONNECTION_SETTINGS_CATEGORY) as PreferenceCategory
        connectionSettings.isEnabled = mqttCheckBox.isChecked

        mqttCheckBox.setOnPreferenceChangeListener { _, isChecked ->
            connectionSettings.isEnabled = isChecked as Boolean
            true
        }

        val host = findPreference(MqttSettings.MQTT_BROKER_HOST) as EditTextPreference
        host.summary = host.text
        host.setOnPreferenceChangeListener { _, newValue ->
            host.summary = newValue.toString()
            true
        }

        val port = findPreference(MqttSettings.MQTT_BROKER_PORT) as EditTextPreference
        port.summary = port.text
        port.setOnPreferenceChangeListener { _, newValue ->
            val portValue = newValue.toString().toIntOrNull()
            if (portValue != null && MqttSettings.isValidPort(portValue)) {
                port.summary = newValue.toString()
                true
            } else {
                false
            }
        }
    }
}
