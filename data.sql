create table location
(
    id          bigint auto_increment
        primary key,
    description varchar(4096) not null,
    name        varchar(255)  not null
);

INSERT INTO wob.location (id, description, name) VALUES (1, 'Your home, a small cottage in lower Ossiriand.', 'Home');
INSERT INTO wob.location (id, description, name) VALUES (2, 'An easterly mountain range that marks the largest land border of Beleriand. Inhabited by small troops of dwarves.', 'Ered Luin');
INSERT INTO wob.location (id, description, name) VALUES (3, 'A small mountain at the eastern edge of the Andram range, which originates beyond the Gates of Sirion.', 'Amon Ereb');
INSERT INTO wob.location (id, description, name) VALUES (4, 'A series of caves and passageways that cuts through the Andram mountains, following the path of the mighty Sirion river southward to the sea.', 'Gates of Sirion');
INSERT INTO wob.location (id, description, name) VALUES (5, 'Small forested region south of the Andram range where the Sirion meets with the Narog river, before continuing further south to the Bay of Balar.', 'Nan-Tathren');
INSERT INTO wob.location (id, description, name) VALUES (6, 'An expansive forested region south of the Andram mountains, which forms a border to Ossiriand on the east, and the Bay of Balar on the west.', 'Taur-Im-Duinath');
INSERT INTO wob.location (id, description, name) VALUES (7, 'Western foothills of the Ered Luin mountains, and the last inhabited region before reaching the March of Maedhros to the north.', 'Thargelion');
INSERT INTO wob.location (id, description, name) VALUES (8, 'This rolling hillside forms the southern reach of Iron Mountains that are home to Morgoth''s impenetrable fortress. Named after Maedhros, eldest son of the great Fëanor.', 'March of Maedhros');
