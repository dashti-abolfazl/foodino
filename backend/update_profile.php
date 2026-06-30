<?php
require 'config.php';
$conn = db();

$user_id   = (int)($_POST['user_id'] ?? 0);
$full_name = trim($_POST['full_name'] ?? '');
$phone     = trim($_POST['phone'] ?? '');
$address   = trim($_POST['address'] ?? '');

if ($user_id <= 0) {
    json(["success" => false, "message" => "شناسه کاربر نامعتبر است"]);
}

$stmt = $conn->prepare(
    "UPDATE users SET full_name = ?, phone = ?, address = ? WHERE id = ?"
);
$stmt->bind_param("sssi", $full_name, $phone, $address, $user_id);

if ($stmt->execute()) {
    json(["success" => true, "message" => "پروفایل با موفقیت بروزرسانی شد"]);
} else {
    json(["success" => false, "message" => "خطا در بروزرسانی پروفایل"]);
}
