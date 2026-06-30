<?php
/**
 * فایل پیکربندی مشترک
 * تمام اسکریپت‌ها این فایل را require می‌کنند تا اتصال دیتابیس و
 * توابع کمکی را یکجا داشته باشیم (مرتب‌سازی بک‌اند).
 */

error_reporting(0);
ini_set('display_errors', 0);
header('Content-Type: application/json; charset=utf-8');

define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'store_db');

/** اتصال به دیتابیس و برگرداندن شیء mysqli */
function db(): mysqli {
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    if ($conn->connect_error) {
        echo json_encode(
            ["success" => false, "message" => "خطا در اتصال به دیتابیس"],
            JSON_UNESCAPED_UNICODE
        );
        exit;
    }
    $conn->set_charset("utf8mb4");
    return $conn;
}

/** خروجی JSON یکدست و خروج از اسکریپت */
function json($data) {
    echo json_encode($data, JSON_UNESCAPED_UNICODE);
    exit;
}

/** آدرس پایه‌ی سرور بر اساس میزبانی که درخواست از آن آمده (مثلاً 10.0.2.2 روی امولاتور) */
function base_url(): string {
    $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? 'localhost';
    return $scheme . '://' . $host . '/Foodino/';
}

/**
 * آدرس عکس‌های ذخیره‌شده در دیتابیس معمولاً با localhost ذخیره شده‌اند.
 * این تابع فقط بخش scheme://host را با میزبان درخواست فعلی جایگزین می‌کند تا
 * عکس‌ها روی موبایل/امولاتور هم لود شوند (مسیر را دست‌نخورده نگه می‌دارد).
 */
function fix_image_url($url) {
    if (empty($url)) return $url;
    $path = parse_url($url, PHP_URL_PATH);
    if (!$path) return $url;
    $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? 'localhost';
    return $scheme . '://' . $host . $path;
}
