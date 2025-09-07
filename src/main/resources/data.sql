INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name, voters_count)
VALUES ('MacDac', 0),
       ('KeeFCi', 0);

INSERT INTO DISH (name, price, r_id, create_date)
VALUES ('Breakfast', 50, 1, NOW()),
       ('Lunch', 10, 1, NOW()),
       ('AfterLunch', 50, 1, NOW()),
       ('Soop', 60, 1, NOW()),
       ('Potato', 30, 1, NOW()),
       ('Chachapuri', 15, 2, NOW()),
       ('Tea', 10, 2, NOW()),
       ('Coffee', 15, 2, NOW());