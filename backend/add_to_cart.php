<?php
require 'config.php';
$conn = db();

$user_id    = (int)($_POST['user_id'] ?? 0);
$product_id = (int)($_POST['product_id'] ?? 0);
$quantity   = (int)($_POST['quantity'] ?? 1);
if ($quantity < 1) $quantity = 1;

if ($user_id <= 0 || $product_id <= 0) {
    json(["success" => false, "message" => "اطلاعات ناقص است"]);
}

// بررسی وجود محصول
$check = $conn->prepare("SELECT id FROM products WHERE id = ?");
$check->bind_param("i", $product_id);
$check->execute();
if ($check->get_result()->num_rows === 0) {
    json(["success" => false, "message" => "محصول یافت نشد"]);
}

// با کلید یکتا (user_id, product_id) اگر تکراری بود تعداد را زیاد می‌کنیم
$stmt = $conn->prepare(
    "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)
     ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)"
);
$stmt->bind_param("iii", $user_id, $product_id, $quantity);

if ($stmt->execute()) {
    json(["success" => true, "message" => "به سبد خرید اضافه شد"]);
} else {
    json(["success" => false, "message" => "خطا در افزودن به سبد خرید"]);
}
