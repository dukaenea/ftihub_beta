-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 29, 2018 at 11:19 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ftihub`
--

-- --------------------------------------------------------

--
-- Table structure for table `t_pinnedmessages`
--

CREATE TABLE `t_pinnedmessages` (
  `username` varchar(30) NOT NULL,
  `message` varchar(1024) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `t_privatechat`
--

CREATE TABLE `t_privatechat` (
  `id` int(11) NOT NULL,
  `id_sender` int(11) NOT NULL,
  `id_receiver` int(11) NOT NULL,
  `online` tinyint(4) NOT NULL,
  `message` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `t_privatechat`
--

INSERT INTO `t_privatechat` (`id`, `id_sender`, `id_receiver`, `online`, `message`) VALUES
(132, 14, 13, 1, 'hey'),
(133, 13, 14, 1, 'hey'),
(134, 13, 14, 1, 'what up?'),
(135, 14, 13, 1, 'just chillin\''),
(136, 13, 14, 1, '1212'),
(137, 14, 13, 1, '3434'),
(138, 13, 14, 1, 'hello'),
(139, 14, 13, 1, 'hey'),
(140, 13, 14, 1, 'hey'),
(141, 14, 13, 1, '1234'),
(142, 14, 13, 1, '123'),
(143, 13, 14, 1, '11'),
(144, 13, 14, 1, '111'),
(145, 13, 14, 1, '121'),
(146, 15, 13, 1, 'hello'),
(147, 13, 15, 1, 'hey'),
(148, 15, 13, 1, '122'),
(149, 15, 13, 1, '122'),
(150, 14, 13, 1, 'hello'),
(151, 13, 14, 1, 'hello'),
(152, 13, 14, 1, 'hello'),
(153, 13, 14, 1, 'hello'),
(154, 13, 14, 1, '12'),
(155, 13, 14, 1, '123'),
(156, 13, 14, 1, '1234'),
(157, 13, 14, 1, 'hello'),
(158, 13, 14, 1, 'test'),
(159, 13, 14, 1, 'test'),
(160, 13, 14, 1, '11'),
(161, 13, 14, 1, '12312'),
(162, 13, 14, 1, 'wefwqaevfwaqe'),
(163, 13, 14, 1, 'hello'),
(164, 14, 13, 1, 'asdasd'),
(165, 14, 13, 1, 'hello');

-- --------------------------------------------------------

--
-- Table structure for table `t_users`
--

CREATE TABLE `t_users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `t_users`
--

INSERT INTO `t_users` (`id`, `username`, `password`, `email`) VALUES
(13, 'Enea Duka', 'a', 'enea.duka1@gmail.com'),
(14, 'Skender Paturri', 'a', 'nerip@gmail.com'),
(15, 'Emi Pali', 'emiemiemi', 'epali@gmail.com'),
(16, 'Barbara Koduzi', 'barbikoduzi', 'bkoduzi@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_privatechat`
--
ALTER TABLE `t_privatechat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sender_idx` (`id_sender`),
  ADD KEY `fk_receiver_idx` (`id_receiver`);

--
-- Indexes for table `t_users`
--
ALTER TABLE `t_users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_privatechat`
--
ALTER TABLE `t_privatechat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=166;

--
-- AUTO_INCREMENT for table `t_users`
--
ALTER TABLE `t_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `t_privatechat`
--
ALTER TABLE `t_privatechat`
  ADD CONSTRAINT `fk_receiver` FOREIGN KEY (`id_receiver`) REFERENCES `t_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_sender` FOREIGN KEY (`id_sender`) REFERENCES `t_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
