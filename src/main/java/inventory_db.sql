-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 24, 2017 at 06:25 PM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventory_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `account_status`
--

CREATE TABLE `account_status` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account_status`
--

INSERT INTO `account_status` (`id`, `title`) VALUES
(1, 'Active'),
(2, 'Inactive');

-- --------------------------------------------------------

--
-- Table structure for table `address_categories`
--

CREATE TABLE `address_categories` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `address_categories`
--

INSERT INTO `address_categories` (`id`, `title`) VALUES
(1, 'Business'),
(2, 'Shipping');

-- --------------------------------------------------------

--
-- Table structure for table `address_types`
--

CREATE TABLE `address_types` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `address_types`
--

INSERT INTO `address_types` (`id`, `title`) VALUES
(1, 'Commercial'),
(2, 'Residential');

-- --------------------------------------------------------

--
-- Table structure for table `company`
--

CREATE TABLE `company` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id` int(11) NOT NULL,
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
  `category_id` int(11) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `height` varchar(200) DEFAULT NULL,
  `length` varchar(200) DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `name` varchar(200) DEFAULT NULL,
  `purchase_uom_id` int(11) DEFAULT NULL,
  `sale_uom_id` int(11) DEFAULT NULL,
  `standard_uom_id` int(11) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `weight` varchar(200) DEFAULT NULL,
  `width` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `product_categories`
--

CREATE TABLE `product_categories` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product_categories`
--

INSERT INTO `product_categories` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Product category1'),
(2, 0, 0, 'Product category2');

-- --------------------------------------------------------

--
-- Table structure for table `product_types`
--

CREATE TABLE `product_types` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product_types`
--

INSERT INTO `product_types` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'Product type1'),
(2, 0, 0, 'Product type2');

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `id` int(11) NOT NULL,
  `company_id` int(11) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_orders`
--

CREATE TABLE `purchase_orders` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_date` int(11) UNSIGNED NOT NULL,
  `order_no` varchar(200) DEFAULT NULL,
  `requested_ship_date` int(11) UNSIGNED NOT NULL,
  `supplier_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `purchase_order_statuses`
--

CREATE TABLE `purchase_order_statuses` (
  `id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchase_order_statuses`
--

INSERT INTO `purchase_order_statuses` (`id`, `title`) VALUES
(1, 'Open'),
(2, 'In Progress'),
(3, 'Fully Received'),
(4, 'Paid'),
(5, 'Cancelled');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `description`, `title`) VALUES
(1, 'Admin', 'admin'),
(2, 'Staff', 'staff'),
(3, 'Customer', 'customer'),
(4, 'Supplier', 'supplier');

-- --------------------------------------------------------

--
-- Table structure for table `sale_orders`
--

CREATE TABLE `sale_orders` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `customer_user_id` int(11) NOT NULL,
  `discount` double DEFAULT NULL,
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `order_no` varchar(200) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `sale_date` int(11) NOT NULL,
  `status_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sale_order_products`
--

CREATE TABLE `sale_order_products` (
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

--
-- Dumping data for table `sale_order_statuses`
--

INSERT INTO `sale_order_statuses` (`id`, `title`) VALUES
(1, 'Quote'),
(2, 'Open'),
(3, 'In Progress'),
(4, 'Fully Shipped'),
(5, 'Invoiced'),
(6, 'Paid'),
(7, 'Cancelled');

-- --------------------------------------------------------

--
-- Table structure for table `showroom_stocks`
--

CREATE TABLE `showroom_stocks` (
  `id` int(11) NOT NULL,
  `created_on` int(11) UNSIGNED DEFAULT '0',
  `modified_on` int(11) UNSIGNED DEFAULT '0',
  `product_id` int(11) DEFAULT NULL,
  `purchase_order_no` varchar(200) DEFAULT NULL,
  `sale_order_no` varchar(200) DEFAULT NULL,
  `stock_in` double DEFAULT NULL,
  `stock_out` double DEFAULT NULL,
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

--
-- Dumping data for table `ss_transaction_categories`
--

INSERT INTO `ss_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete'),
(5, 'Sale In'),
(6, 'Sale partial Out'),
(7, 'Sale Delete');

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL,
  `remarks` int(11) DEFAULT NULL,
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

--
-- Dumping data for table `uoms`
--

INSERT INTO `uoms` (`id`, `created_on`, `modified_on`, `title`) VALUES
(1, 0, 0, 'UOM1'),
(2, 0, 0, 'UOM2');

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

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `account_status_id`, `cell`, `created_on`, `email`, `first_name`, `img`, `last_name`, `modified_on`, `password`, `user_name`) VALUES
(1, 1, '01711123456', 0, 'admin@gmail.com', 'Admin', 'img1', '1', 0, 'pass', 'admin'),
(2, 1, '01722123456', 0, 'staff@gmail.com', 'Staff', 'img2', '1', 0, 'pass', 'staff');

-- --------------------------------------------------------

--
-- Table structure for table `users_addresses`
--

CREATE TABLE `users_addresses` (
  `id` int(11) NOT NULL,
  `address` int(11) DEFAULT NULL,
  `address_category_id` int(11) NOT NULL,
  `address_type_id` int(11) NOT NULL,
  `city` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `zip` int(11) DEFAULT NULL
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

--
-- Dumping data for table `users_roles`
--

INSERT INTO `users_roles` (`id`, `role_id`, `user_id`) VALUES
(1, 1, 1),
(2, 2, 2);

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
-- Dumping data for table `ws_transaction_categories`
--

INSERT INTO `ws_transaction_categories` (`id`, `title`) VALUES
(1, 'Purchase In'),
(2, 'Purchase Partial In'),
(3, 'Purchase partial Out'),
(4, 'Purchase Delete');

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
-- Indexes for table `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`title`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `po_showroom_products`
--
ALTER TABLE `po_showroom_products`
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
  ADD PRIMARY KEY (`id`);

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
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`user_id`,`company_id`);

--
-- Indexes for table `purchase_orders`
--
ALTER TABLE `purchase_orders`
  ADD PRIMARY KEY (`id`);

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
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sale_order_products`
--
ALTER TABLE `sale_order_products`
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
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ss_transaction_categories`
--
ALTER TABLE `ss_transaction_categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`);

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
  ADD UNIQUE KEY `idx_name` (`first_name`,`last_name`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `address_categories`
--
ALTER TABLE `address_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `address_types`
--
ALTER TABLE `address_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `company`
--
ALTER TABLE `company`
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
-- AUTO_INCREMENT for table `product_categories`
--
ALTER TABLE `product_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `product_types`
--
ALTER TABLE `product_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `profile`
--
ALTER TABLE `profile`
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
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
-- AUTO_INCREMENT for table `sale_order_statuses`
--
ALTER TABLE `sale_order_statuses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `showroom_stocks`
--
ALTER TABLE `showroom_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ss_transaction_categories`
--
ALTER TABLE `ss_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `uoms`
--
ALTER TABLE `uoms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users_addresses`
--
ALTER TABLE `users_addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `users_roles`
--
ALTER TABLE `users_roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `warehouse_stocks`
--
ALTER TABLE `warehouse_stocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ws_transaction_categories`
--
ALTER TABLE `ws_transaction_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
