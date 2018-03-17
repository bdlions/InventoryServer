--
-- Table structure for table `account_status`
--

CREATE TABLE `account_status` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `address_categories`
--

CREATE TABLE `address_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `address_types`
--

CREATE TABLE `address_types` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `company_info`
--

CREATE TABLE `company_info` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `cell` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `logo` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
  `balance` double DEFAULT '0',
  `cell` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `customer_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `po_showroom_products`
--

CREATE TABLE `po_showroom_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `po_showroom_return_products`
--

CREATE TABLE `po_showroom_return_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `po_warehouse_products`
--

CREATE TABLE `po_warehouse_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT '1',
  `category_title` varchar(200) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `cost_price` double DEFAULT '0',
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `default_sale_quantity` double DEFAULT '1',
  `height` varchar(200) DEFAULT NULL,
  `length` varchar(200) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `name` varchar(200) DEFAULT NULL,
  `purchase_uom_id` int(11) UNSIGNED DEFAULT '1',
  `sale_uom_id` int(11) UNSIGNED DEFAULT '1',
  `standard_uom_id` int(11) UNSIGNED DEFAULT '1',
  `type_id` int(11) DEFAULT '1',
  `type_title` varchar(200) DEFAULT NULL,
  `unit_price` double DEFAULT '0',
  `vat` double DEFAULT '0',
  `weight` varchar(200) DEFAULT NULL,
  `width` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `products_suppliers`
--

CREATE TABLE `products_suppliers` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `supplier_price` double DEFAULT NULL,
  `supplier_product_code` varchar(255) DEFAULT NULL,
  `supplier_user_id` int(11) DEFAULT NULL,
  `supplier_user_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `product_categories`
--

CREATE TABLE `product_categories` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(200) DEFAULT NULL,
  `vat` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `product_types`
--

CREATE TABLE `product_types` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_orders`
--

CREATE TABLE `purchase_orders` (
  `id` int(11) NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `cell` varchar(255) DEFAULT NULL,
  `created_by_user_id` int(11) UNSIGNED DEFAULT '0',
  `created_by_user_name` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `modified_by_user_id` int(11) UNSIGNED DEFAULT '0',
  `modified_by_user_name` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `next_order_no` int(11) DEFAULT '1',
  `order_date` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `paid` double DEFAULT '0',
  `remarks` varchar(1000) DEFAULT NULL,
  `requested_ship_date` int(11) UNSIGNED DEFAULT '0',
  `subtotal` double DEFAULT '0',
  `supplier_name` varchar(255) DEFAULT NULL,
  `supplier_user_id` int(11) NOT NULL,
  `total` double DEFAULT '0',
  `total_return` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_order_statuses`
--

CREATE TABLE `purchase_order_statuses` (
  `id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sale_orders`
--

CREATE TABLE `sale_orders` (
  `id` int(11) NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `cell` varchar(255) DEFAULT NULL,
  `created_by_user_id` int(11) UNSIGNED DEFAULT '0',
  `created_by_user_name` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_user_id` int(11) NOT NULL,
  `discount` double DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `modified_by_user_id` int(11) UNSIGNED DEFAULT '0',
  `modified_by_user_name` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `next_order_no` int(11) DEFAULT '1',
  `order_no` varchar(200) DEFAULT NULL,
  `paid` double DEFAULT '0',
  `remarks` varchar(1000) DEFAULT NULL,
  `sale_date` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `subtotal` double DEFAULT '0',
  `total` double DEFAULT '0',
  `total_return` double DEFAULT '0',
  `vat` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sale_order_products`
--

CREATE TABLE `sale_order_products` (
  `id` int(11) NOT NULL,
  `cost_price` double DEFAULT '0',
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `product_id` int(11) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `vat` double DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sale_order_return_products`
--

CREATE TABLE `sale_order_return_products` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `product_id` int(11) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `unit_price` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sale_order_statuses`
--

CREATE TABLE `sale_order_statuses` (
  `id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `showroom_stocks`
--

CREATE TABLE `showroom_stocks` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `stock_in` double DEFAULT '0',
  `stock_out` double DEFAULT '0',
  `transaction_category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ss_transaction_categories`
--

CREATE TABLE `ss_transaction_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL,
  `balance` double DEFAULT '0',
  `cell` varchar(255) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `remarks` varchar(1000) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `uoms`
--

CREATE TABLE `uoms` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

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

-- --------------------------------------------------------

--
-- Table structure for table `users_addresses`
--

CREATE TABLE `users_addresses` (
  `id` int(11) NOT NULL,
  `address` varchar(500) DEFAULT NULL,
  `address_category_id` int(11) NOT NULL,
  `address_type_id` int(11) NOT NULL,
  `city` varchar(500) DEFAULT NULL,
  `state` varchar(500) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `zip` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users_roles`
--

CREATE TABLE `users_roles` (
  `id` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `warehouse_stocks`
--

CREATE TABLE `warehouse_stocks` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `product_id` int(11) UNSIGNED DEFAULT '0',
  `stock_in` int(11) UNSIGNED DEFAULT '0',
  `stock_out` int(11) UNSIGNED DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ws_transaction_categories`
--

CREATE TABLE `ws_transaction_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account_status`
--
ALTER TABLE `account_status`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`title`);

--
-- Indexes for table `address_categories`
--
ALTER TABLE `address_categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `address_types`
--
ALTER TABLE `address_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `company_info`
--
ALTER TABLE `company_info`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`name`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_customer_name` (`customer_name`),
  ADD KEY `idx_customer_cell` (`cell`),
  ADD KEY `idx_customer_email` (`email`);

--
-- Indexes for table `po_showroom_products`
--
ALTER TABLE `po_showroom_products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `po_showroom_return_products`
--
ALTER TABLE `po_showroom_return_products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `po_warehouse_products`
--
ALTER TABLE `po_warehouse_products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_product_name` (`name`),
  ADD UNIQUE KEY `idx_product_code` (`code`),
  ADD KEY `idx_category_id` (`category_id`);

--
-- Indexes for table `products_suppliers`
--
ALTER TABLE `products_suppliers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_supplier_user_id` (`supplier_user_id`);

--
-- Indexes for table `product_categories`
--
ALTER TABLE `product_categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_types`
--
ALTER TABLE `product_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `purchase_orders`
--
ALTER TABLE `purchase_orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_purchase_order_no` (`order_no`),
  ADD KEY `idx_supplier_name` (`supplier_name`),
  ADD KEY `idx_supplier_cell` (`cell`),
  ADD KEY `idx_supplier_email` (`email`);

--
-- Indexes for table `purchase_order_statuses`
--
ALTER TABLE `purchase_order_statuses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`title`);

--
-- Indexes for table `sale_orders`
--
ALTER TABLE `sale_orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_sale_order_no` (`order_no`),
  ADD KEY `idx_customer_name` (`customer_name`),
  ADD KEY `idx_customer_cell` (`cell`),
  ADD KEY `idx_customer_email` (`email`);

--
-- Indexes for table `sale_order_products`
--
ALTER TABLE `sale_order_products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sale_order_return_products`
--
ALTER TABLE `sale_order_return_products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sale_order_statuses`
--
ALTER TABLE `sale_order_statuses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `showroom_stocks`
--
ALTER TABLE `showroom_stocks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_showroom_stock_product_name` (`product_name`);

--
-- Indexes for table `ss_transaction_categories`
--
ALTER TABLE `ss_transaction_categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_supplier_name` (`supplier_name`),
  ADD KEY `idx_supplier_cell` (`cell`),
  ADD KEY `idx_supplier_email` (`email`);

--
-- Indexes for table `uoms`
--
ALTER TABLE `uoms`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_email` (`email`);

--
-- Indexes for table `users_addresses`
--
ALTER TABLE `users_addresses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_roles`
--
ALTER TABLE `users_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`user_id`,`role_id`);

--
-- Indexes for table `warehouse_stocks`
--
ALTER TABLE `warehouse_stocks`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ws_transaction_categories`
--
ALTER TABLE `ws_transaction_categories`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account_status`
--
ALTER TABLE `account_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `address_categories`
--
ALTER TABLE `address_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `address_types`
--
ALTER TABLE `address_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `company_info`
--
ALTER TABLE `company_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `po_showroom_products`
--
ALTER TABLE `po_showroom_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `po_showroom_return_products`
--
ALTER TABLE `po_showroom_return_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `po_warehouse_products`
--
ALTER TABLE `po_warehouse_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `products_suppliers`
--
ALTER TABLE `products_suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_categories`
--
ALTER TABLE `product_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_types`
--
ALTER TABLE `product_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `purchase_orders`
--
ALTER TABLE `purchase_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `purchase_order_statuses`
--
ALTER TABLE `purchase_order_statuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sale_orders`
--
ALTER TABLE `sale_orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sale_order_products`
--
ALTER TABLE `sale_order_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sale_order_return_products`
--
ALTER TABLE `sale_order_return_products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sale_order_statuses`
--
ALTER TABLE `sale_order_statuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `showroom_stocks`
--
ALTER TABLE `showroom_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ss_transaction_categories`
--
ALTER TABLE `ss_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `uoms`
--
ALTER TABLE `uoms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users_addresses`
--
ALTER TABLE `users_addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users_roles`
--
ALTER TABLE `users_roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `warehouse_stocks`
--
ALTER TABLE `warehouse_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ws_transaction_categories`
--
ALTER TABLE `ws_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;