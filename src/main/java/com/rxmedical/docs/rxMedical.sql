use rxmedical;

DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS user;




INSERT INTO `rxmedical`.`user` (`id`, `auth_level`, `create_date`, `update_date`, `dept`, `email`, `emp_id`, `name`, `password`, `title`) VALUES 
('1', 'root', '2024-05-23', '2024-05-23', '衛材庫房', 'root@root', '00000', 'root', '123456', 'root'),
('2', 'admin', '2024-05-23', '2024-05-23', '秘書室', 'school832@gmail.com', '73174', '王俊傑', '123456', '替代役');