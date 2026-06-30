package com.example.foodino

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test for the login function [LoginValidator.isValid].
 * Runs on the JVM, no device/emulator required.
 */
class LoginValidatorTest {

    @Test
    fun validCredentials_returnsTrue() {
        assertTrue(LoginValidator.isValid("test", "test"))
    }

    @Test
    fun emptyUsername_returnsFalse() {
        assertFalse(LoginValidator.isValid("", "test"))
    }

    @Test
    fun blankPassword_returnsFalse() {
        assertFalse(LoginValidator.isValid("test", "   "))
    }

    /**
     * Full check of the login function. If every case behaves correctly,
     * it prints "PASS" at the end.
     */
    @Test
    fun loginFunction_worksCorrectly_printsPass() {
        assertTrue("valid login should pass", LoginValidator.isValid("test", "test"))
        assertFalse("empty username must fail", LoginValidator.isValid("", "1234"))
        assertFalse("empty password must fail", LoginValidator.isValid("user", ""))
        assertFalse("both empty must fail", LoginValidator.isValid("", ""))

        println("✅ PASS - login function works correctly")
    }
}
