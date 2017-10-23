--
-- Table structure for table `account_status`
--
CREATE TABLE `account_status` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `account_status`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`title`);
ALTER TABLE `account_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;  
INSERT INTO `account_status` (`id`, `title`) VALUES
(1, 'Active'),
(2, 'Inactive');

--
-- Table structure for table `roles`
--
CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`title`);
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `roles` (`id`, `description`, `title`) VALUES
(1, 'Admin', 'admin'),
(2, 'Staff', 'staff'),
(3, 'Supplier', 'supplier'),
(4, 'Customer', 'customer');

--
-- Table structure for table `address_categories`
--
CREATE TABLE `address_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `address_categories`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `address_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `address_categories` (`id`, `title`) VALUES
(1, 'Business'),
(2, 'Shipping');

--
-- Table structure for table `address_types`
--
CREATE TABLE `address_types` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `address_types`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `address_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `address_types` (`id`, `title`) VALUES
(1, 'Commercial'),
(2, 'Residential');

--
-- Table structure for table `users`
--
CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `account_status_id` int(11) NOT NULL DEFAULT '1',
  `cell` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`first_name`,`last_name`);
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `users` (`id`, `account_status_id`, `cell`, `created_on`, `email`, `first_name`, `img`, `last_name`, `modified_on`, `password`, `user_name`) VALUES
(1, 1, '01711123456', 0, 'admin@gmail.com', 'Admin', 'img1', '1', 0, 'pass', 'admin'),
(2, 1, '01722123456', 0, 'staff@gmail.com', 'Staff', 'img2', '1', 0, 'pass', 'staff'),
(3, 0, '01733123456', 0, 'supplier1@gmail.com', 'Supplier', 'img3', '1', 0, 'pass', 'supplier1'),
(4, 0, '01744123456', 0, 'supplier2@gmail.com', 'Supplier', 'img3', '2', 0, 'pass', 'supplier2'),
(5, 0, '01755123456', 0, 'customer1@gmail.com', 'Customer', 'img4', '1', 0, 'pass', 'customer1'),
(6, 0, '01766123456', 0, 'customer2@gmail.com', 'Customer', 'img4', '2', 0, 'pass', 'customer2');

--
-- Table structure for table `users_roles`
--
CREATE TABLE `users_roles` (
  `id` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `users_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`user_id`,`role_id`);
ALTER TABLE `users_roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `users_roles` (`id`, `role_id`, `user_id`) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 3, 4),
(5, 4, 5),
(6, 4, 6);

--
-- Table structure for table `users_addresses`
--
CREATE TABLE `users_addresses` (
  `id` int(11) NOT NULL,
  `address` int(11) DEFAULT NULL,
  `address_category_id` int(11) NOT NULL,
  `address_type_id` int(11) NOT NULL,
  `city` varchar(255) DEFAULT '',
  `state` varchar(255) DEFAULT '',
  `user_id` int(11) NOT NULL,
  `zip` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `users_addresses`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `users_addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Table structure for table `suppliers`
--
CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL,
  `balance` double DEFAULT NULL,
  `remarks` varchar(255) DEFAULT '',
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `suppliers` (`id`, `balance`, `remarks`, `user_id`) VALUES
(1, 0, '', 3),
(2, 0, '', 4);

--
-- Table structure for table `customers`
--
CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `balance` double DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `customers` (`id`, `balance`, `user_id`) VALUES
(1, 0, 5),
(2, 0, 6);

--
-- Table structure for table `uoms`
--
CREATE TABLE `uoms` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `uoms`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `uoms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `uoms` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'cases'),
(2, 0, 0, 'ea'),
(3, 0, 0, 'packs'),
(4, 0, 0, 'pcs');

--
-- Table structure for table `product_types`
--
CREATE TABLE `product_types` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `product_types`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `product_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `product_types` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Stocked Product'),
(2, 0, 0, 'Serialized Product'),
(3, 0, 0, 'Non-Stocked Product'),
(4, 0, 0, 'Service');

--
-- Table structure for table `product_categories`
--
CREATE TABLE `product_categories` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `product_categories`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `product_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `product_categories` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Default Category');

--
-- Table structure for table `products`
--
CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT '1',
  `category_title` varchar(200) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `height` varchar(200) DEFAULT NULL,
  `length` varchar(200) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `name` varchar(200) DEFAULT NULL,
  `purchase_uom_id` int(11) UNSIGNED DEFAULT '1',
  `sale_uom_id` int(11) UNSIGNED DEFAULT '1',
  `standard_uom_id` int(11) UNSIGNED DEFAULT '1',
  `type_id` int(11) DEFAULT '1',
  `type_title` varchar(200) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `weight` varchar(200) DEFAULT NULL,
  `width` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `products` (`id`, `category_id`, `category_title`, `code`, `created_on`, `height`, `length`, `modified_on`, `name`, `purchase_uom_id`, `sale_uom_id`, `standard_uom_id`, `type_id`, `type_title`, `unit_price`, `weight`, `width`) VALUES
(1, 1, 'Default Category', 'jeans', 0, NULL, NULL, 0, 'jeans', 4, 4, 4, 1, 'Stocked Product', 100, NULL, NULL),
(2, 1, 'Default Category', 'tshirt', 0, NULL, NULL, 0, 'tshirt', 4, 4, 4, 1, 'Stocked Product', 200, NULL, NULL),
(3, 1, 'Default Category', 'belt', 0, NULL, NULL, 0, 'belt', 4, 4, 4, 1, 'Stocked Product', 300, NULL, NULL),
(4, 1, 'Default Category', 'trouser', 0, NULL, NULL, 0, 'trouser', 4, 4, 4, 1, 'Stocked Product', 400, NULL, NULL),
(5, 1, 'Default Category', 'shirt', 0, NULL, NULL, 0, 'shirt', 4, 4, 4, 1, 'Stocked Product', 500, NULL, NULL);

--
-- Table structure for table `purchase_order_statuses`
--
CREATE TABLE `purchase_order_statuses` (
  `id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `purchase_order_statuses`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `purchase_order_statuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `purchase_order_statuses` (`id`, `title`) VALUES
(1, 'Open'),
(2, 'In Progress'),
(3, 'Fully Received'),
(4, 'Paid'),
(5, 'Cancelled');

--
-- Table structure for table `purchase_orders`
--
CREATE TABLE `purchase_orders` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `discount` int(11) UNSIGNED DEFAULT 0,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `order_date` int(11) UNSIGNED DEFAULT 0,
  `order_no` varchar(200) DEFAULT NULL,
  `paid` int(11) UNSIGNED DEFAULT 0,
  `requested_ship_date` int(11) UNSIGNED DEFAULT 0,
  `subtotal` int(11) UNSIGNED DEFAULT 0,
  `supplier_user_id` int(11) NOT NULL,
  `total` int(11) UNSIGNED DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `purchase_orders`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `purchase_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- Table structure for table `po_showroom_products`
--
CREATE TABLE `po_showroom_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `po_showroom_products`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `po_showroom_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;  
  
--
-- Table structure for table `sale_order_statuses`
--
CREATE TABLE `sale_order_statuses` (
  `id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `sale_order_statuses`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `sale_order_statuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `sale_order_statuses` (`id`, `title`) VALUES
(1, 'Quote'),
(2, 'Open'),
(3, 'In Progress'),
(4, 'Fully Shipped'),
(5, 'Invoiced'),
(6, 'Paid'),
(7, 'Cancelled');  

--
-- Table structure for table `sale_orders`
--
CREATE TABLE `sale_orders` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `customer_user_id` int(11) NOT NULL,
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `order_no` varchar(200) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `sale_date` int(11) NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `sale_orders`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `sale_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- Table structure for table `sale_order_products`
--
CREATE TABLE `sale_order_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `product_id` int(11) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `sale_order_products`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `sale_order_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
 
--
-- Table structure for table `ss_transaction_categories`
--
CREATE TABLE `ss_transaction_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `ss_transaction_categories`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `ss_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `ss_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete'),
(5, 'Sale Out'),
(6, 'Sale Return partial In'),
(7, 'Sale Delete');

--
-- Table structure for table `showroom_stocks`
--
CREATE TABLE `showroom_stocks` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `product_id` int(11) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `stock_in` double DEFAULT NULL,
  `stock_out` double DEFAULT NULL,
  `transaction_category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `showroom_stocks`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `showroom_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;


-- right now we are not handling warehouse stock  
--
-- Table structure for table `ws_transaction_categories`
--
CREATE TABLE `ws_transaction_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `ws_transaction_categories`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `ws_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
INSERT INTO `ws_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete');

--
-- Table structure for table `warehouse_stocks`
--
CREATE TABLE `warehouse_stocks` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) UNSIGNED DEFAULT 0,
  `stock_in` int(11) UNSIGNED DEFAULT 0,
  `stock_out` int(11) UNSIGNED DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `warehouse_stocks`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `warehouse_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
 
--
-- Table structure for table `po_warehouse_products`
-- 
CREATE TABLE `po_warehouse_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT 0,
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT 0,
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
ALTER TABLE `po_warehouse_products`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `po_warehouse_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
-- --------------------------------------------------------