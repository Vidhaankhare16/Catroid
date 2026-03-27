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

package org.catrobat.catroid.test.devices.mqtt

import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.catrobat.catroid.devices.mqtt.MqttSettings
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MqttSettingsTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private lateinit var savedPreferences: Map<String, Any?>

    @Before
    fun setUp() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        savedPreferences = mapOf(
            MqttSettings.SETTINGS_SHOW_MQTT_BRICKS to prefs.getBoolean(MqttSettings.SETTINGS_SHOW_MQTT_BRICKS, false),
            MqttSettings.MQTT_BROKER_HOST to prefs.getString(MqttSettings.MQTT_BROKER_HOST, null),
            MqttSettings.MQTT_BROKER_PORT to prefs.getString(MqttSettings.MQTT_BROKER_PORT, null),
            MqttSettings.MQTT_USE_TLS to prefs.getBoolean(MqttSettings.MQTT_USE_TLS, false),
            MqttSettings.MQTT_USERNAME to prefs.getString(MqttSettings.MQTT_USERNAME, null),
            MqttSettings.MQTT_PASSWORD to prefs.getString(MqttSettings.MQTT_PASSWORD, null),
            MqttSettings.MQTT_CLIENT_ID to prefs.getString(MqttSettings.MQTT_CLIENT_ID, null)
        )
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

    @After
    fun tearDown() {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit().clear()
        savedPreferences.forEach { (key, value) ->
            when (value) {
                is Boolean -> editor.putBoolean(key, value)
                is String -> editor.putString(key, value)
            }
        }
        editor.commit()
    }

    @Test
    fun defaultEnabledIsFalse() {
        assertFalse(MqttSettings.isEnabled(context))
    }

    @Test
    fun defaultBrokerHostIsCorrect() {
        assertEquals(MqttSettings.DEFAULT_BROKER_HOST, MqttSettings.brokerHost(context))
    }

    @Test
    fun defaultBrokerPortIsCorrect() {
        assertEquals(MqttSettings.DEFAULT_BROKER_PORT, MqttSettings.brokerPort(context))
    }

    @Test
    fun defaultTlsIsDisabled() {
        assertFalse(MqttSettings.isTlsEnabled(context))
    }

    @Test
    fun defaultUsernameIsEmpty() {
        assertEquals("", MqttSettings.username(context))
    }

    @Test
    fun defaultPasswordIsEmpty() {
        assertEquals("", MqttSettings.password(context))
    }

    @Test
    fun defaultClientIdIsEmpty() {
        assertEquals("", MqttSettings.clientId(context))
    }

    @Test
    fun enabledIsPersisted() {
        MqttSettings.setEnabled(context, true)
        assertTrue(MqttSettings.isEnabled(context))
    }

    @Test
    fun disabledIsPersisted() {
        MqttSettings.setEnabled(context, true)
        MqttSettings.setEnabled(context, false)
        assertFalse(MqttSettings.isEnabled(context))
    }

    @Test
    fun brokerHostIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_BROKER_HOST, "mqtt.example.com")
            .commit()
        assertEquals("mqtt.example.com", MqttSettings.brokerHost(context))
    }

    @Test
    fun brokerPortIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_BROKER_PORT, "8883")
            .commit()
        assertEquals(8883, MqttSettings.brokerPort(context))
    }

    @Test
    fun tlsEnabledIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putBoolean(MqttSettings.MQTT_USE_TLS, true)
            .commit()
        assertTrue(MqttSettings.isTlsEnabled(context))
    }

    @Test
    fun usernameIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_USERNAME, "testuser")
            .commit()
        assertEquals("testuser", MqttSettings.username(context))
    }

    @Test
    fun passwordIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_PASSWORD, "secret")
            .commit()
        assertEquals("secret", MqttSettings.password(context))
    }

    @Test
    fun clientIdIsPersisted() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_CLIENT_ID, "catroid-client-1")
            .commit()
        assertEquals("catroid-client-1", MqttSettings.clientId(context))
    }

    @Test
    fun portBelowRangeIsRejectedAndDefaultReturned() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_BROKER_PORT, "0")
            .commit()
        assertEquals(MqttSettings.DEFAULT_BROKER_PORT, MqttSettings.brokerPort(context))
    }

    @Test
    fun portAboveRangeIsRejectedAndDefaultReturned() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_BROKER_PORT, "65536")
            .commit()
        assertEquals(MqttSettings.DEFAULT_BROKER_PORT, MqttSettings.brokerPort(context))
    }

    @Test
    fun nonNumericPortIsRejectedAndDefaultReturned() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(MqttSettings.MQTT_BROKER_PORT, "notaport")
            .commit()
        assertEquals(MqttSettings.DEFAULT_BROKER_PORT, MqttSettings.brokerPort(context))
    }

    @Test
    fun minValidPortIsAccepted() {
        assertTrue(MqttSettings.isValidPort(1))
    }

    @Test
    fun maxValidPortIsAccepted() {
        assertTrue(MqttSettings.isValidPort(65535))
    }

    @Test
    fun zeroPortIsInvalid() {
        assertFalse(MqttSettings.isValidPort(0))
    }

    @Test
    fun portOverMaxIsInvalid() {
        assertFalse(MqttSettings.isValidPort(65536))
    }

    @Test
    fun connectionSettingsAreInactiveWhenMqttIsDisabled() {
        MqttSettings.setEnabled(context, false)
        assertFalse(MqttSettings.isEnabled(context))
    }

    @Test
    fun connectionSettingsAreActiveWhenMqttIsEnabled() {
        MqttSettings.setEnabled(context, true)
        assertTrue(MqttSettings.isEnabled(context))
    }
}
