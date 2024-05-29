use rxmedical;

DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS product;

-- User
SELECT * FROM user; 

INSERT INTO `user` (`emp_code`, dept, title, `name`, email, salt, password, auth_level, create_date, update_date) VALUES
('00000', '衛材庫房', 'root', 'root', 'root@root.com', '8b4c873ac087c7a553642d528f60aa8e', '6e29fc2a04004b7a9aa1fe986309e49bf9e4123171993d896ad4a179565c7caa', 'root', NOW(), NOW()),
('73174', '秘書室', '替代役', '王俊傑', 'school832@gmail.com', '94f725be236567bb57d77708ac9b45aa', 'caa6a26d3a35d0c99c161543cac20d2e68258a211993091d3ff6298ae52fe95f', 'admin', NOW(), NOW()),
('12345', '文書中心', '一般專員', '金一蓉', 'test@test.com', '24d518fae3d305a2df5a1ca3aaeca769', 'f3f6446f9cb3f981912a21e25d33246019ddb650361f31fd1d8f5a431aa939ad', 'staff', NOW(), NOW());


