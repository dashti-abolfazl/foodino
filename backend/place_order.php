<?php
require 'config.php';
$conn = db();

$user_id   = (int)($_POST['user_id'] ?? 0);
$full_name = trim($_POST['full_name'] ?? '');
$phone     = trim($_POST['phone'] ?? '');
$address   = trim($_POST['address'] ?? '');

if ($user_id <= 0 || $full_name === '' || $phone === '' || $address === '') {
    json(["success" => false, "message" => "لطفاً تمام فیلدها را پر کنید"]);
}

// خواندن سبد خرید کاربر
$stmt = $conn->prepare(
    "SELECT c.product_id, c.quantity, p.name, p.price
     FROM cart c JOIN products p ON p.id = c.product_id
     WHERE c.user_id = ?"
);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$cart = $stmt->get_result();

if ($cart->num_rows === 0) {
    json(["success" => false, "message" => "سبد خرید شما خالی است"]);
}

$items = [];
$total = 0.0;
while ($row = $cart->fetch_assoc()) {
    $price = (float)$row["price"];
    $qty   = (int)$row["quantity"];
    $total += $price * $qty;
    $items[] = [
        "product_id" => (int)$row["product_id"],
        "name"       => $row["name"],
        "price"      => $price,
        "quantity"   => $qty,
    ];
}

// تراکنش: ثبت سفارش + اقلام + خالی کردن سبد
$conn->begin_transaction();
try {
    $o = $conn->prepare(
        "INSERT INTO orders (user_id, full_name, phone, address, total_price, status)
         VALUES (?, ?, ?, ?, ?, 'pending')"
    );
    $o->bind_param("isssd", $user_id, $full_name, $phone, $address, $total);
    $o->execute();
    $order_id = $conn->insert_id;

    $oi = $conn->prepare(
        "INSERT INTO order_items (order_id, product_id, product_name, price, quantity)
         VALUES (?, ?, ?, ?, ?)"
    );
    foreach ($items as $it) {
        $oi->bind_param(
            "iisdi",
            $order_id,
            $it["product_id"],
            $it["name"],
            $it["price"],
            $it["quantity"]
        );
        $oi->execute();
    }

    // خالی کردن سبد خرید
    $del = $conn->prepare("DELETE FROM cart WHERE user_id = ?");
    $del->bind_param("i", $user_id);
    $del->execute();

    $conn->commit();
    json([
        "success"  => true,
        "message"  => "سفارش شما با موفقیت ثبت شد",
        "order_id" => (int)$order_id,
        "total"    => $total,
    ]);
} catch (Exception $e) {
    $conn->rollback();
    json(["success" => false, "message" => "خطا در ثبت سفارش"]);
}
