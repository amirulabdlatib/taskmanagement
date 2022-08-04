-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 04, 2022 at 09:20 AM
-- Server version: 5.7.33
-- PHP Version: 7.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `taskdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE `files` (
  `id` int(11) NOT NULL,
  `file` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `taskID` int(10) NOT NULL,
  `taskName` varchar(256) NOT NULL,
  `description` varchar(256) NOT NULL,
  `assignedDate` date NOT NULL,
  `taskDue` date NOT NULL,
  `staffName` varchar(256) NOT NULL,
  `status` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`taskID`, `taskName`, `description`, `assignedDate`, `taskDue`, `staffName`, `status`) VALUES
(13, 'Staff Handling', 'manage staff task', '2022-08-03', '2022-09-05', 'qatrun', 'Completed'),
(14, 'Handling invoice', 'Manage month 8 invoice', '2022-08-03', '2022-08-18', 'amirul', 'Assigned'),
(15, 'tido', 'TLDM', '2022-08-03', '2022-08-03', 'shad', 'Assigned');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `Address` varchar(256) DEFAULT NULL,
  `token` varchar(50) NOT NULL DEFAULT '00000000-00000-0000-0000-000000000000',
  `lease` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  `role` varchar(50) DEFAULT 'user',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `secret` varchar(50) NOT NULL DEFAULT '206b2dbe-ecc9-490b-b81b-83767288bc5e'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `username`, `password`, `PhoneNumber`, `Address`, `token`, `lease`, `role`, `is_active`, `secret`) VALUES
(1, 'tiwa@example.com', 'tiwa', '17c4520f6cfd1ab53d8745e84681eb49', '01110140458', 'Kampus Jasin', 'bd7338f7-64eb-41cf-9e4d-11caf8679298', '2022-08-03 01:13:00', 'Superadmin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(2, 'sabrina@example.com', 'sabrina', '21232f297a57a5a743894a0e4a801fc3', '0139152210', 'Kampus Jasin', '988a2c3a-b711-4a37-96e4-b403feaae27d', '2022-07-31 02:02:31', 'Admin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(3, 'qatrun@example.com', 'qatrun', 'ee11cbb19052e40b07aac0ca060c23ee', '0123304905', 'Kampus Jasin', '3d3d444e-3945-4e76-ac95-675ef1823f13', '2022-07-31 02:02:45', 'Designer', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(4, 'laila@example.com', 'laila', '81dc9bdb52d04dc20036dbd8313ed055', '0175466619', 'Kampus Jasin', '6206fd5b-7c1c-4ef9-bd00-28c011a7eac4', '2022-08-04 03:10:24', 'Founder', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(5, 'amirulal73@example.com', 'amirul', '81b073de9370ea873f548e31b8adc081', '01164134572', 'Kampus Jasin', '125bdad6-91fc-428d-bd9c-df0e972171e9', '2022-08-04 03:11:51', 'Staff', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`taskID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `files`
--
ALTER TABLE `files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `taskID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
