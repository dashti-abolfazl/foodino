<?php
require 'config.php';
$conn = db();

$user_id    = (int)($_POST['user_id'] ?? 0);
$product_id = (int)($_POST['product_id'] ?? 0);
$quantity   = (int)($_POST['quantity'] ?? 0);

if ($user_id <= 0 || $product_id <= 0) {
    json(["success" => false, "message" => "اطلاعات ناقص است"]);
}

if ($quantity <= 0) {
    // تعداد صفر یعنی حذف آیتم از سبد
    $stmt = $conn->prepare("DELETE FROM cart WHERE user_id = ? AND product_id = ?");
    $stmt->bind_param("ii", $user_id, $product_id);
    $stmt->execute();
    json(["success" => true, "message" => "آیتم از سبد حذف شد"]);
}

$stmt = $conn->prepare(
    "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?"
);
$stmt->bind_param("iii", $quantity, $user_id, $product_id);

if ($stmt->execute()) {
    json(["success" => true, "message" => "سبد خرید بروزرسانی شد"]);
} else {
    json(["success" => false, "message" => "خطا در بروزرسانی سبد"]);
}
