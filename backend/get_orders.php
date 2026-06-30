<?php
require 'config.php';
$conn = db();

$user_id = (int)($_GET['user_id'] ?? 0);
if ($user_id <= 0) {
    json(["success" => false, "orders" => [], "message" => "شناسه کاربر نامعتبر است"]);
}

$stmt = $conn->prepare(
    "SELECT id, full_name, phone, address, total_price, status, created_at
     FROM orders WHERE user_id = ? ORDER BY id DESC"
);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$orders = [];
while ($row = $result->fetch_assoc()) {
    $orders[] = [
        "id"          => (int)$row["id"],
        "full_name"   => $row["full_name"],
        "phone"       => $row["phone"],
        "address"     => $row["address"],
        "total_price" => (float)$row["total_price"],
        "status"      => $row["status"],
        "created_at"  => $row["created_at"],
    ];
}

json(["success" => true, "orders" => $orders, "message" => null]);
