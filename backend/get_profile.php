<?php
require 'config.php';
$conn = db();

$user_id = (int)($_GET['user_id'] ?? 0);
if ($user_id <= 0) {
    json(["success" => false, "message" => "شناسه کاربر نامعتبر است"]);
}

$stmt = $conn->prepare(
    "SELECT id, username, role, full_name, phone, address, created_at
     FROM users WHERE id = ?"
);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    json(["success" => false, "message" => "کاربر یافت نشد"]);
}

$row = $result->fetch_assoc();
json([
    "success" => true,
    "user" => [
        "id"         => (int)$row["id"],
        "username"   => $row["username"],
        "role"       => $row["role"],
        "full_name"  => $row["full_name"],
        "phone"      => $row["phone"],
        "address"    => $row["address"],
        "created_at" => $row["created_at"],
    ],
]);
