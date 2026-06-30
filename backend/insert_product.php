<?php
require 'config.php';
$conn = db();

$code  = trim($_POST['code'] ?? '');   // کد محصول (به‌عنوان id)
$name  = trim($_POST['name'] ?? '');
$price = trim($_POST['price'] ?? '');

if ($code === '' || $name === '' || $price === '') {
    json(["success" => false, "message" => "❌ اطلاعات ناقص است"]);
}

$image_url = "";

// آپلود عکس در صورت وجود
if (isset($_FILES['image']) && $_FILES['image']['error'] === 0) {
    $ext = pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
    $image_name = uniqid("img_", true) . "." . $ext;
    $upload_dir = "uploads/";
    if (!is_dir($upload_dir)) {
        mkdir($upload_dir, 0777, true);
    }
    $image_path = $upload_dir . $image_name;
    if (move_uploaded_file($_FILES['image']['tmp_name'], $image_path)) {
        $image_url = base_url() . "uploads/" . $image_name;
    } else {
        json(["success" => false, "message" => "❌ خطا در آپلود عکس"]);
    }
}

$stmt = $conn->prepare("INSERT INTO products (id, name, price, image_url) VALUES (?, ?, ?, ?)");
$stmt->bind_param("isds", $code, $name, $price, $image_url);

if ($stmt->execute()) {
    json(["success" => true, "message" => "✅ محصول با موفقیت اضافه شد"]);
} else {
    json(["success" => false, "message" => "❌ خطا در افزودن محصول: " . $stmt->error]);
}
