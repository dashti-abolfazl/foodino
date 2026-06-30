# 🤖 End-to-End (E2E) Test

This folder contains an E2E test that runs the real **login** flow on a device/emulator.

## What it does
[`LoginE2ETest.kt`](com/example/foodino/LoginE2ETest.kt):
1. Opens the app on the **Login** screen
2. Types the username and password **`test` / `test`**
3. Taps the **Login** button
4. If the whole flow runs without error, the test **passes** (shown green in the report and
   prints `✅ PASS - login E2E flow completed`)

> Written with Espresso and wired into the `androidTest` build via `sourceSets` in
> `app/build.gradle.kts`.

## Requirements
- A running **Android device** or **emulator** connected.
- For a truly successful login, the server (XAMPP) must be running and a user with
  username `test` and password `test` must exist in the database:
  ```sql
  INSERT INTO users (username, password, role) VALUES ('test', 'test', 'customer');
  ```

## How to run

### Option 1 — Terminal
From the project root (with an emulator running):
```bash
./gradlew connectedAndroidTest
```
Result report:
```
app/build/reports/androidTests/connected/index.html
```

### Option 2 — Android Studio
Open `LoginE2ETest.kt` and click the green triangle next to `loginWithTestUser_passes` →
**Run**. The app opens on the emulator, the login runs automatically, and at the end it shows
**Tests passed**.

## Note
The error `NoSuchMethodException: android.hardware.input.InputManager.getInstance` is caused by
Espresso's input injection, which crashes on very recent Android versions (e.g. Pixel 10 Pro /
Android preview images). To make the test reliable on **every** Android version, this E2E test is
written with **UiAutomator** (it injects input through the framework `UiAutomation` API instead of
Espresso). No extra setup is needed — the `androidx.test.uiautomator` dependency is already in
`gradle/libs.versions.toml` and `app/build.gradle.kts`.
