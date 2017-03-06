-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 26, 2017 at 03:29 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `my_orders`
--

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) UNSIGNED NOT NULL,
  `productId` int(11) UNSIGNED NOT NULL DEFAULT '0',
  `buyerId` int(11) UNSIGNED NOT NULL DEFAULT '0',
  `quantity` int(5) UNSIGNED NOT NULL DEFAULT '1',
  `singlePrice` decimal(10,0) UNSIGNED NOT NULL,
  `totalPrice` decimal(10,0) UNSIGNED NOT NULL,
  `deliveryAddress` text NOT NULL,
  `dateOrdered` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `productId`, `buyerId`, `quantity`, `singlePrice`, `totalPrice`, `deliveryAddress`, `dateOrdered`, `status`) VALUES
(1, 1, 2, 1, '5', '10', '', '2017-02-21 23:53:23', 'notReceived'),
(2, 2, 2, 2, '10', '20', '', '2017-02-22 00:30:58', 'rejected'),
(3, 3, 2, 3, '10', '30', '', '2017-02-23 00:01:35', 'pending'),
(4, 4, 2, 3, '10', '30', '', '2017-02-23 00:02:53', 'pending'),
(5, 1, 2, 3, '10', '30', '', '2017-02-23 00:03:29', 'pending'),
(7, 1, 2, 3, '11', '32', 'geo: lat: 123, long: 321', '2017-02-26 11:02:48', 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) UNSIGNED NOT NULL,
  `sellerId` int(11) UNSIGNED NOT NULL,
  `title` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity` int(5) UNSIGNED NOT NULL DEFAULT '1',
  `imageIdentifier` varchar(500) DEFAULT NULL,
  `description` text,
  `dateAdded` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `sellerId`, `title`, `price`, `quantity`, `imageIdentifier`, `description`, `dateAdded`) VALUES
(1, 1, 'Phone Android', '10.50', 2, 'StI47JBhSNc', 'Android v1 Phone', '2017-02-21 22:22:35'),
(2, 1, 'Phone iOS', '15.00', 1, 'zaJSTp1Nb88', 'iOS v1 Phone', '2017-02-21 22:22:42'),
(3, 3, 'TV Android', '20.00', 1, 'ngMtsE5r9eI', 'Android v1 TV', '2017-02-21 22:22:35'),
(4, 3, 'TV iOS', '25.00', 5, 'null', 'iOS v1 TV', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) UNSIGNED NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(100) NOT NULL,
  `firstName` varchar(150) NOT NULL,
  `lastName` varchar(150) NOT NULL,
  `address` text NOT NULL,
  `phone` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `firstName`, `lastName`, `address`, `phone`) VALUES
(1, 'ivan', '123', 'seller', 'Ivan', 'Ivanov', 'bul. Ivan Vazov #11, Sofia, Bulgaria, 1000', '+359 888 88 88 88'),
(2, 'kolio', '123', 'buyer', 'Kolio', 'Kolev', 'bul. Ivan Vazov #10, Sofia, Bulgaria, 1000', '+359 899 99 99'),
(3, 'misho', '123', 'seller', 'Misho', 'Mishev', 'bul. Hristo Botev #60, Sofia, Bulgaria, 1000', '+359 877 77 77'),
(6, 'stancho', '123', 'buyer', 'Stancho', 'Stanchev', 'bul. Stancho #4, Sofia, Bulgaria, 1000', '+359 866 66 66 66');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_orders_products` (`productId`),
  ADD KEY `FK_orders_users` (`buyerId`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_products_products` (`sellerId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK_orders_products` FOREIGN KEY (`productId`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_orders_users` FOREIGN KEY (`buyerId`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FK_products_products` FOREIGN KEY (`sellerId`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
