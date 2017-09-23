INSERT INTO `account_status`(`id`, `title`) values
(1, 'Active'),
(2, 'Inactive');

INSERT INTO `role`(`id`, `title`, `description`) values
(1, 'ADMIN', 'Admin'),
(2, 'USER', 'User');

INSERT INTO `user`(`id`, `email`, `password`, `first_name`, `last_name`, `account_status_id`, `cell`) values
(1, 'user1@gmail.com', 'pass', 'User', '1', 1, '01711123456');

INSERT INTO `user_role`(`id`, `user_id`, `role_id`) values
(1, 1, 1);

INSERT INTO `company`(`id`, `title`, `address`, `website`) values
(1, 'Company1', 'Dhaka', 'http://signtechbd.com');

INSERT INTO `profile`(`id`, `user_id`, `company_id`, `department`, `designation`) values
(1, 1, 1, 'Engineering', 'Developer');