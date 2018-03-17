CREATE TABLE `organizations` (
  `id` int(11) NOT NULL,
  `database_name` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `organizations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_name` (`database_name`);
  
ALTER TABLE `organizations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
 
INSERT INTO `organizations` (`id`, `database_name`, `title`) VALUES
(1, 10001, 'Client1'); 
