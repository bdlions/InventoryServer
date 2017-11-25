INSERT INTO `account_status` (`id`, `title`) VALUES
(1, 'Active'),
(2, 'Inactive');

INSERT INTO `roles` (`id`, `description`, `title`) VALUES
(1, 'Admin', 'admin'),
(2, 'Staff', 'staff'),
(3, 'Supplier', 'supplier'),
(4, 'Customer', 'customer');

INSERT INTO `address_categories` (`id`, `title`) VALUES
(1, 'Business'),
(2, 'Shipping');

INSERT INTO `address_types` (`id`, `title`) VALUES
(1, 'Commercial'),
(2, 'Residential');

INSERT INTO `users` (`id`, `account_status_id`, `cell`, `created_on`, `email`, `first_name`, `img`, `last_name`, `modified_on`, `password`, `user_name`) VALUES
(1, 1, '01711123456', 0, 'admin@gmail.com', 'Signature', 'img1', 'Technology', 0, 'pass', 'admin'),
(2, 1, '01722123456', 0, 'staff@gmail.com', 'Redoy', 'img2', 'Rahman', 0, 'pass', 'staff'),
(3, 0, '01733123456', 0, 'nazmul@gmail.com', 'Nazmul', 'img3', 'Hasan', 0, 'pass', 'nazmul'),
(4, 0, '01744123456', 0, 'alamgir@gmail.com', 'Alamgir', 'img3', 'Kabir', 0, 'pass', 'alamgir'),
(5, 0, '01755123456', 0, 'zobaer@gmail.com', 'Zobaer', 'img4', 'Badal', 0, 'pass', 'zobaer'),
(6, 0, '01766123456', 0, 'alamin@gmail.com', 'Alamin', 'img4', 'Kazi', 0, 'pass', 'alamin');

INSERT INTO `users_roles` (`id`, `role_id`, `user_id`) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 3, 4),
(5, 4, 5),
(6, 4, 6);

INSERT INTO `suppliers` (`id`, `balance`, `remarks`, `user_id`) VALUES
(1, 0, '', 3),
(2, 0, '', 4);

INSERT INTO `customers` (`id`, `balance`, `user_id`) VALUES
(1, 0, 5),
(2, 0, 6);

INSERT INTO `uoms` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'cases'),
(2, 0, 0, 'ea'),
(3, 0, 0, 'packs'),
(4, 0, 0, 'pcs');

INSERT INTO `product_types` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Stocked Product'),
(2, 0, 0, 'Serialized Product'),
(3, 0, 0, 'Non-Stocked Product'),
(4, 0, 0, 'Service');

INSERT INTO `product_categories` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Cloth');

INSERT INTO `products` (`id`, `category_id`, `category_title`, `code`, `created_on`, `height`, `length`, `modified_on`, `name`, `purchase_uom_id`, `sale_uom_id`, `standard_uom_id`, `type_id`, `type_title`, `unit_price`, `weight`, `width`) VALUES
(1, 1, 'Cloth', 'jeans', 0, NULL, NULL, 0, 'jeans', 4, 4, 4, 1, 'Stocked Product', 100, NULL, NULL),
(2, 1, 'Cloth', 'tshirt', 0, NULL, NULL, 0, 'tshirt', 4, 4, 4, 1, 'Stocked Product', 200, NULL, NULL),
(3, 1, 'Cloth', 'pant', 0, NULL, NULL, 0, 'pant', 4, 4, 4, 1, 'Stocked Product', 300, NULL, NULL),
(4, 1, 'Cloth', 'trouser', 0, NULL, NULL, 0, 'trouser', 4, 4, 4, 1, 'Stocked Product', 400, NULL, NULL),
(5, 1, 'Cloth', 'shirt', 0, NULL, NULL, 0, 'shirt', 4, 4, 4, 1, 'Stocked Product', 500, NULL, NULL);

INSERT INTO `purchase_order_statuses` (`id`, `title`) VALUES
(1, 'Open'),
(2, 'In Progress'),
(3, 'Fully Received'),
(4, 'Paid'),
(5, 'Cancelled');

INSERT INTO `sale_order_statuses` (`id`, `title`) VALUES
(1, 'Quote'),
(2, 'Open'),
(3, 'In Progress'),
(4, 'Fully Shipped'),
(5, 'Invoiced'),
(6, 'Paid'),
(7, 'Cancelled');  

INSERT INTO `ss_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete'),
(5, 'Sale Out'),
(6, 'Sale Return partial In'),
(7, 'Sale Delete');



INSERT INTO `ws_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete');
