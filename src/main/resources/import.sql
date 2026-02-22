-- 1. ROLES
INSERT INTO roles (id, nombre, descripcion) VALUES (1, 'ADMIN', 'Administrador');
INSERT INTO roles (id, nombre, descripcion) VALUES (2, 'MESERO', 'Mesero');
INSERT INTO roles (id, nombre, descripcion) VALUES (3, 'COCINA', 'Cocina');
INSERT INTO roles (id, nombre, descripcion) VALUES (4, 'CAJERO', 'Cajero');

-- 2. ESTADOS
INSERT INTO estados_pedido (id, nombre, editable) VALUES (1, 'CREADO', 1);
INSERT INTO estados_pedido (id, nombre, editable) VALUES (2, 'EN_PREPARACION', 0);
INSERT INTO estados_pedido (id, nombre, editable) VALUES (3, 'LISTO', 0);
INSERT INTO estados_pedido (id, nombre, editable) VALUES (4, 'ENTREGADO', 0);
INSERT INTO estados_pedido (id, nombre, editable) VALUES (5, 'CANCELADO', 0);
INSERT INTO estados_pedido (id, nombre, editable) VALUES (6, 'PAGADO', 0);

-- 3. USUARIOS (Orden exacto de tu base de datos)
INSERT INTO usuarios (activo, fecha_creacion, role_id, username, nombre, password) VALUES (1, NOW(), 1, 'admin123', 'Administrador Sistema', '$2a$10$S13ogKB4gSHj5R1pfDL7sehRbu9w//m/d4SCmuiAVxKjtD9R0rzBy');
INSERT INTO usuarios (activo, fecha_creacion, role_id, username, nombre, password) VALUES (1, NOW(), 2, 'mesero123', 'Juan Mesero', '$2a$10$S13ogKB4gSHj5R1pfDL7sehRbu9w//m/d4SCmuiAVxKjtD9R0rzBy');
INSERT INTO usuarios (activo, fecha_creacion, role_id, username, nombre, password) VALUES (1, NOW(), 3, 'cocina123', 'Chef Ejecutivo', '$2a$10$S13ogKB4gSHj5R1pfDL7sehRbu9w//m/d4SCmuiAVxKjtD9R0rzBy');
INSERT INTO usuarios (activo, fecha_creacion, role_id, username, nombre, password) VALUES (1, NOW(), 4, 'cajero123', 'Ana Cajera', '$2a$10$S13ogKB4gSHj5R1pfDL7sehRbu9w//m/d4SCmuiAVxKjtD9R0rzBy');

-- 4. CATEGORÍAS
INSERT INTO categorias_plato (id, nombre, descripcion, activo) VALUES (1, 'Entradas', 'Para empezar', 1);
INSERT INTO categorias_plato (id, nombre, descripcion, activo) VALUES (2, 'Platos Fuertes', 'Principales', 1);

-- 5. MESAS
INSERT INTO mesas (id, numero, capacidad, estado) VALUES (1, 1, 4, 'LIBRE');
INSERT INTO mesas (id, numero, capacidad, estado) VALUES (2, 2, 2, 'LIBRE');