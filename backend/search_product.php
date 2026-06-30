<?php
require 'config.php';
$conn = db();

$query = $_GET['query'] ?? '';

$stmt = $conn->prepare("SELECT id, name, price, image_url, category FROM products WHERE name LIKE ?");
$like = "%" . $query . "%";
$stmt->bind_param("s", $like);
$stmt->execute();
$result = $stmt->get_result();

$products = [];
while ($row = $result->fetch_assoc()) {
    $products[] = [
        "id"        => (int)$row["id"],
        "name"      => $row["name"],
        "price"     => $row["price"],
        "image_url" => fix_image_url($row["image_url"]),
        "category"  => $row["category"],
    ];
}

json(["success" => true, "data" => $products, "message" => null]);
