package com.example.foodino

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-End login test (UiAutomator).
 *
 * Opens the login screen, types username/password "test / test", taps Login,
 * and if the whole flow runs without error, prints PASS.
 *
 * Why UiAutomator instead of Espresso?
 * On very recent Android versions, Espresso's input injection crashes with
 * `NoSuchMethodException: android.hardware.input.InputManager.getInstance`.
 * UiAutomator injects input through the framework UiAutomation API, which works
 * reliably on every Android version.
 */
@RunWith(AndroidJUnit4::class)
class LoginE2ETest {

    private val pkg = "com.example.foodino"
    private val timeout = 8000L

    @Test
    fun loginWithTestUser_passes() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // 1) Open the Login screen directly
        ActivityScenario.launch(LoginScreen::class.java)

        // 2) Wait for the username field, then type "test"
        val userField = device.wait(Until.findObject(By.res(pkg, "user_field")), timeout)
        assertNotNull("Login screen did not open (username field not found)", userField)
        userField.text = "test"

        // 3) Type the password "test"
        val passField = device.wait(Until.findObject(By.res(pkg, "password_field")), timeout)
        assertNotNull("Password field not found", passField)
        passField.text = "test"

        // 4) Tap the Login button
        val loginBtn = device.wait(Until.findObject(By.res(pkg, "btn_login")), timeout)
        assertNotNull("Login button not found", loginBtn)
        loginBtn.click()

        // 5) Let the app react, then finish
        device.waitForIdle()

        println("✅ PASS - login E2E flow completed")
    }
}
