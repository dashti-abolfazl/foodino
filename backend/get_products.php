<?php
require 'config.php';
$conn = db();

$result = $conn->query("SELECT id, name, price, image_url, category FROM products ORDER BY id DESC");

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

json(["success" => true, "products" => $products]);
