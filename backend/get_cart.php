<?php
require 'config.php';
$conn = db();

$user_id = (int)($_GET['user_id'] ?? 0);
if ($user_id <= 0) {
    json(["success" => false, "items" => [], "total" => 0, "message" => "شناسه کاربر نامعتبر است"]);
}

$stmt = $conn->prepare(
    "SELECT c.id AS cart_id, c.product_id, c.quantity,
            p.name, p.price, p.image_url
     FROM cart c
     JOIN products p ON p.id = c.product_id
     WHERE c.user_id = ?
     ORDER BY c.id DESC"
);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$items = [];
$total = 0.0;
while ($row = $result->fetch_assoc()) {
    $price    = (float)$row["price"];
    $quantity = (int)$row["quantity"];
    $subtotal = $price * $quantity;
    $total   += $subtotal;

    $items[] = [
        "cart_id"    => (int)$row["cart_id"],
        "product_id" => (int)$row["product_id"],
        "name"       => $row["name"],
        "price"      => $price,
        "quantity"   => $quantity,
        "subtotal"   => $subtotal,
        "image_url"  => fix_image_url($row["image_url"]),
    ];
}

json(["success" => true, "items" => $items, "total" => $total, "message" => null]);
