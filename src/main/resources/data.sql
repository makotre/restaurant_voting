INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('MacDac'),
       ('KeeFCi');

INSERT INTO DISH (name, price, r_id, serving_date)
VALUES ('Breakfast', 50, 1, '2025-09-26'),
       ('Lunch', 10, 1, '2025-09-26'),
       ('AfterLunch', 50, 1, '2025-09-26'),
       ('Soop', 60, 1, '2025-09-27'),
       ('Potato', 30, 1, '2025-09-27'),
       ('Chachapuri', 15, 2, '2025-09-26'),
       ('Tea', 10, 2, '2025-09-26'),
       ('Coffee', 15, 2, '2025-09-26');

INSERT INTO VOTE (user_id, restaurant_id, date_time, choice)
VALUES (1, 1, '2025-09-26 10:00', true)