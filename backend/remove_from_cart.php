<?php
require 'config.php';
$conn = db();

$user_id    = (int)($_POST['user_id'] ?? 0);
$product_id = (int)($_POST['product_id'] ?? 0);

if ($user_id <= 0 || $product_id <= 0) {
    json(["success" => false, "message" => "اطلاعات ناقص است"]);
}

$stmt = $conn->prepare("DELETE FROM cart WHERE user_id = ? AND product_id = ?");
$stmt->bind_param("ii", $user_id, $product_id);

if ($stmt->execute()) {
    json(["success" => true, "message" => "آیتم از سبد حذف شد"]);
} else {
    json(["success" => false, "message" => "خطا در حذف آیتم"]);
}
