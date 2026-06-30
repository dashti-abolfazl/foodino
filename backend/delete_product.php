<?php
// تنظیم هدر خروجی برای ارسال پاسخ JSON با کدگذاری UTF-8
header('Content-Type: application/json; charset=utf-8');

// اتصال به دیتابیس MySQL با استفاده از mysqli
$connect = new mysqli("localhost", "root", "", "store_db");

// بررسی اینکه آیا اتصال به دیتابیس موفق بوده یا نه
if ($connect->connect_error) {
    echo json_encode([
        'success' => false,
        'message' => "خطا در اتصال به دیتابیس: " . $connect->connect_error
    ]);
    exit(); // خروج از اسکریپت در صورت خطای اتصال
}

// بررسی اینکه آیا کلید 'id' از طریق POST ارسال شده یا نه
if (!isset($_POST['id']) || empty($_POST['id'])) {
    echo json_encode([
        'success' => false,
        'message' => "کد محصول ارسال نشده است."
    ]);
    exit(); // خروج از اسکریپت در صورت نبودن id
}

// تبدیل مقدار id به عدد صحیح برای امنیت بیشتر (جلوگیری از SQL Injection)
$id = intval($_POST['id']);

// آماده‌سازی یک کوئری SELECT برای بررسی وجود داشتن محصول با این id
$stmt = $connect->prepare("SELECT id FROM products WHERE id = ?");
$stmt->bind_param("i", $id); // مقدار id را به صورت امن bind می‌کنیم
$stmt->execute(); // اجرای کوئری
$result = $stmt->get_result(); // دریافت نتیجه کوئری

// اگر هیچ محصولی با این id پیدا نشد
if ($result->num_rows == 0) {
    echo json_encode([
        'success' => false,
        'message' => "محصولی با این کد پیدا نشد."
    ]);
    $stmt->close(); // بستن statement
    $connect->close(); // بستن اتصال به دیتابیس
    exit(); // خروج از اسکریپت
}

// اگر محصول وجود داشت، statement قبلی را می‌بندیم
$stmt->close();

// آماده‌سازی کوئری DELETE برای حذف محصول
$delete_stmt = $connect->prepare("DELETE FROM products WHERE id = ?");
$delete_stmt->bind_param("i", $id); // مقدار id را به صورت امن bind می‌کنیم

// اجرای کوئری حذف و بررسی موفقیت آن
if ($delete_stmt->execute()) {
    echo json_encode([
        'success' => true,
        'message' => "محصول با موفقیت حذف شد."
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => "خطا در حذف محصول: " . $connect->error
    ]);
}

// بستن statement و اتصال
$delete_stmt->close();
$connect->close();
?>
