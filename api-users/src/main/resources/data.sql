INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (NAME) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (NAME) DO NOTHING;

--INSERT INTO users (username, email, password, enabled) VALUES ('martin', 'martintan@live.com', 'hulun', true) ON CONFLICT (NAME) DO NOTHING;
--INSERT INTO users (username, email, password, enabled) VALUES ('yuan', 'tanyuan@live.com', 'hulun', true) ON CONFLICT (NAME) DO NOTHING;
