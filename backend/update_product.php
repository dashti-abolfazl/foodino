<?php
require 'config.php';
$conn = db();

$product_id = (int)($_POST["id"] ?? 0);
$name       = trim($_POST["name"] ?? '');
$price      = $_POST["price"] ?? '';

if ($product_id <= 0) {
    json(["status" => "error", "message" => "کد محصول نامعتبر است"]);
}

// بررسی وجود محصول
$check = $conn->prepare("SELECT id FROM products WHERE id = ?");
$check->bind_param("i", $product_id);
$check->execute();
$check->store_result();
if ($check->num_rows === 0) {
    json(["status" => "error", "message" => "محصولی با این کد وجود ندارد."]);
}
$check->close();

$image_url = null;

// آپلود عکس در صورت وجود
if (isset($_FILES["image"]) && $_FILES["image"]["error"] === 0) {
    $target_dir = "uploads/";
    if (!is_dir($target_dir)) {
        mkdir($target_dir, 0777, true);
    }
    $image_name  = uniqid() . "_" . basename($_FILES["image"]["name"]);
    $target_file = $target_dir . $image_name;

    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        $image_url = base_url() . "uploads/" . $image_name;
    } else {
        json(["status" => "error", "message" => "آپلود عکس انجام نشد."]);
    }
}

if ($image_url !== null) {
    // با عکس جدید: name(s), price(d), image_url(s), id(i)
    $stmt = $conn->prepare("UPDATE products SET name = ?, price = ?, image_url = ? WHERE id = ?");
    $stmt->bind_param("sdsi", $name, $price, $image_url, $product_id);
} else {
    // بدون عکس: name(s), price(d), id(i)
    $stmt = $conn->prepare("UPDATE products SET name = ?, price = ? WHERE id = ?");
    $stmt->bind_param("sdi", $name, $price, $product_id);
}

if ($stmt->execute()) {
    json(["status" => "success", "success" => true, "message" => "محصول با موفقیت بروزرسانی شد."]);
} else {
    json(["status" => "error", "success" => false, "message" => "خطا در اجرای کوئری."]);
}
