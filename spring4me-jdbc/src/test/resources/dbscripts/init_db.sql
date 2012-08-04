CREATE TABLE tb_users(
	i_id INTEGER IDENTITY,
	c_username VARCHAR(50),
	c_password VARCHAR(20),
	i_age INTEGER
);

INSERT INTO tb_users(c_username, c_password, i_age) VALUES('gavin', '123456', 27);
INSERT INTO tb_users(c_username, c_password, i_age) VALUES('aimy', '654321', 27);