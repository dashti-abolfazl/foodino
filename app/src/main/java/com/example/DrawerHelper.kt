package com.example.foodino

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

/**
 * راه‌اندازی منوی همبرگری مشترک برای همه‌ی صفحات.
 *
 * منو بر اساس **نقش کاربر** ساخته می‌شود (نه بر اساس صفحه)، بنابراین:
 * - ادمین در همه‌ی صفحات همان منوی ادمین را می‌بیند (مدیریت محصولات / مدیریت فروشگاه / پروفایل / خروج)
 * - مشتری در همه‌ی صفحات منوی مشتری را می‌بیند (فروشگاه / پروفایل / سبد خرید / خروج)
 */
fun AppCompatActivity.setupFoodinoDrawer(
    drawerLayout: DrawerLayout,
    toolbar: MaterialToolbar,
    navView: NavigationView
) {
    val prefs = SharedPrefManager(this)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)

    val toggle = ActionBarDrawerToggle(
        this, drawerLayout, toolbar,
        R.string.app_name, R.string.app_name
    )
    drawerLayout.addDrawerListener(toggle)
    toggle.syncState()

    // اطلاعات کاربر در هدر منو
    val header = navView.getHeaderView(0)
    header.findViewById<TextView>(R.id.nav_header_username).text = prefs.getUsername() ?: "کاربر مهمان"
    header.findViewById<TextView>(R.id.nav_header_role).text = "نقش: ${prefs.getRole() ?: "مهمان"}"

    // انتخاب منو بر اساس نقش (یکدست در همه‌ی صفحات)
    val isAdmin = prefs.getRole() == "admin"
    navView.menu.clear()
    navView.inflateMenu(if (isAdmin) R.menu.nav_menu_admin else R.menu.nav_menu)

    navView.setNavigationItemSelectedListener { item ->
        when (item.itemId) {
            // فروشگاه (مشترک بین مشتری و ادمین)
            R.id.nav_shop, R.id.nav_admin_shop ->
                if (this !is ShopScreen) startActivity(Intent(this, ShopScreen::class.java))

            // پروفایل (مشترک)
            R.id.nav_profile, R.id.nav_admin_profile ->
                if (this !is CustomerProfile) startActivity(Intent(this, CustomerProfile::class.java))

            // مدیریت محصولات (مخصوص ادمین)
            R.id.nav_admin_products ->
                if (this !is AdminProductManagement) startActivity(Intent(this, AdminProductManagement::class.java))

            // سبد خرید (مخصوص مشتری)
            R.id.nav_cart ->
                if (this !is OrderScreen) startActivity(Intent(this, OrderScreen::class.java))

            // خروج (مشترک)
            R.id.nav_logout, R.id.nav_admin_logout -> {
                prefs.logout()
                val i = Intent(this, LoginScreen::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        true
    }
}
