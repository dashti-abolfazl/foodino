<?php
require 'config.php';
$conn = db();

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

if ($username === '' || $password === '') {
    json(["success" => false, "message" => "اطلاعات ناقص است"]);
}

$stmt = $conn->prepare(
    "SELECT id, username, password, role, full_name, phone, address
     FROM users WHERE username = ?"
);
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    json(["success" => false, "message" => "نام کاربری یا رمز اشتباه است"]);
}

$row = $result->fetch_assoc();
$stored = $row['password'];
$ok = false;

if (password_verify($password, $stored)) {
    // رمز هش‌شده
    $ok = true;
} elseif ($stored === $password) {
    // رمز قدیمی (plain-text) — درجا به هش امن ارتقا می‌دهیم
    $ok = true;
    $newHash = password_hash($password, PASSWORD_DEFAULT);
    $up = $conn->prepare("UPDATE users SET password = ? WHERE id = ?");
    $up->bind_param("si", $newHash, $row['id']);
    $up->execute();
}

if (!$ok) {
    json(["success" => false, "message" => "نام کاربری یا رمز اشتباه است"]);
}

json([
    "success"   => true,
    "message"   => "ورود موفق",
    "role"      => $row['role'],
    "user_id"   => (int)$row['id'],
    "username"  => $row['username'],
    "full_name" => $row['full_name'],
    "phone"     => $row['phone'],
    "address"   => $row['address'],
]);
