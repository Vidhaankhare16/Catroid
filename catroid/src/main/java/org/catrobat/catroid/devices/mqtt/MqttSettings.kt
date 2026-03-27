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

package org.catrobat.catroid.devices.mqtt

import android.content.Context
import android.preference.PreferenceManager

object MqttSettings {

    const val SETTINGS_SHOW_MQTT_BRICKS = "setting_mqtt_bricks"
    const val MQTT_BROKER_HOST = "setting_mqtt_broker_host"
    const val MQTT_BROKER_PORT = "setting_mqtt_broker_port"
    const val MQTT_USE_TLS = "setting_mqtt_use_tls"
    const val MQTT_USERNAME = "setting_mqtt_username"
    const val MQTT_PASSWORD = "setting_mqtt_password"
    const val MQTT_CLIENT_ID = "setting_mqtt_client_id"

    const val DEFAULT_BROKER_HOST = "192.168.0.1"
    const val DEFAULT_BROKER_PORT = 1883
    private const val MIN_PORT = 1
    private const val MAX_PORT = 65535

    fun isEnabled(context: Context): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(SETTINGS_SHOW_MQTT_BRICKS, false)

    fun setEnabled(context: Context, enabled: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(SETTINGS_SHOW_MQTT_BRICKS, enabled)
            .apply()
    }

    fun brokerHost(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MQTT_BROKER_HOST, DEFAULT_BROKER_HOST) ?: DEFAULT_BROKER_HOST

    fun brokerPort(context: Context): Int {
        val stored = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MQTT_BROKER_PORT, DEFAULT_BROKER_PORT.toString()) ?: DEFAULT_BROKER_PORT.toString()
        return stored.toIntOrNull()?.takeIf { isValidPort(it) } ?: DEFAULT_BROKER_PORT
    }

    fun isTlsEnabled(context: Context): Boolean =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(MQTT_USE_TLS, false)

    fun username(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MQTT_USERNAME, "") ?: ""

    fun password(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MQTT_PASSWORD, "") ?: ""

    fun clientId(context: Context): String =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MQTT_CLIENT_ID, "") ?: ""

    fun isValidPort(port: Int): Boolean = port in MIN_PORT..MAX_PORT
}
