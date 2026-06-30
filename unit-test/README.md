# 🧪 Unit Test

This folder contains a simple unit test for the Foodino project.

## What is tested?
[`LoginValidatorTest.kt`](com/example/foodino/LoginValidatorTest.kt) tests the login function
`LoginValidator.isValid(username, password)`
(defined in `app/src/main/java/com/example/LoginValidator.kt`).

Cases covered:
- Valid username and password → must return `true`
- Empty username → must return `false`
- Blank/whitespace password → must return `false`
- A full check that prints **`PASS`** when the login function behaves correctly

> This folder is wired into the test build via `sourceSets` in `app/build.gradle.kts`,
> so it runs like a normal unit test. It runs on the JVM and **does not need** a device/emulator.

## How to run

### Option 1 — Terminal
From the project root:
```bash
./gradlew test
```
HTML report is generated at:
```
app/build/reports/tests/testDebugUnitTest/index.html
```
If everything is green, it's a **PASS** (and the console prints `✅ PASS - login function works correctly`).

### Option 2 — Android Studio
Open `LoginValidatorTest.kt` and click the green triangle next to the class/method →
**Run 'LoginValidatorTest'**.
