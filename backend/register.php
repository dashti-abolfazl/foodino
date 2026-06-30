<?php
require 'config.php';
$conn = db();

$username  = trim($_POST['username'] ?? '');
$password  = $_POST['password'] ?? '';
$role      = $_POST['role'] ?? '';
$full_name = trim($_POST['full_name'] ?? '');

if ($username === '' || $password === '' || $role === '') {
    json(["success" => false, "message" => "اطلاعات ناقص است"]);
}

// فقط دو نقش مجاز
if ($role !== 'admin' && $role !== 'customer') {
    $role = 'customer';
}

// بررسی تکراری بودن نام کاربری
$stmt = $conn->prepare("SELECT id FROM users WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();
if ($stmt->get_result()->num_rows > 0) {
    json(["success" => false, "message" => "نام کاربری قبلاً ثبت شده"]);
}

// ذخیره‌ی رمز به صورت هش‌شده
$hash = password_hash($password, PASSWORD_DEFAULT);

$stmt = $conn->prepare(
    "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)"
);
$stmt->bind_param("ssss", $username, $hash, $role, $full_name);

if ($stmt->execute()) {
    json([
        "success" => true,
        "message" => "ثبت نام موفق",
        "user_id" => (int)$conn->insert_id,
    ]);
} else {
    json(["success" => false, "message" => "خطا در ثبت نام"]);
}
