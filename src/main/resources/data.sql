-- =====================================
-- INSERTAR USUARIOS
-- =====================================
INSERT INTO usuarios (IDUsuario, nombre, correo, contrasena, telefono, Rol) VALUES
('U001','Carlos Cliente','carlos@mail.com','1234','987654321','CLIENTE'),
('U002','Admin ZapaStore','admin@mail.com','admin123','999888777','ADMIN'),
('U003','Ana Perez','ana@mail.com','ana2025','912345678','CLIENTE');

-- =====================================
-- INSERTAR CATEGORÍAS
-- =====================================
INSERT INTO categorias (nombre, estado) VALUES
('Running','Activo'),
('Casual','Activo'),
('Skate','Activo'),
('Basketball','Activo'),
('Trail','Activo'),
('Fitness','Activo');

-- =====================================
-- INSERTAR PRODUCTOS
-- =====================================
INSERT INTO productos (nombre, categoria_ID, Precio, img_Url, descripcion) VALUES
('Nike Air Zoom Pegasus', 1, 450.00, 'img/zapatilla1.jpg', 'Ligeras, media distancia.'),
('Adidas Adizero Boston', 1, 480.00, 'img/zapatilla2.jpg', 'Velocidad y amortiguación superior.'),
('Converse Chuck 70 Classic', 2, 320.00, 'img/zapatilla3.jpg', 'Estilo urbano, cómodo.'),
('Puma Suede Mayze', 2, 180.00, 'img/zapatilla4.jpg', 'Confort diario.'),
('Vans Sk8-Hi Pro X', 3, 210.50, 'img/zapatilla5.jpg', 'Alto rendimiento para skate.'),
('DC Shoes Pure Street', 3, 230.00, 'img/zapatilla6.jpg', 'Agilidad y durabilidad.'),
('Jordan Air XXVI', 4, 500.00, 'img/zapatilla-urb.jpg', 'Dominio total en la cancha.'),
('Salomon Speedcross 6', 5, 350.00, 'img/zapatilla-run.jpg', 'Correr en montaña y senderos.'),
('Reebok Nano X4', 6, 200.00, 'img/zapatilla-dep.jpg', 'Entrenamiento cómodo y seguro.');

-- =====================================
-- PEDIDOS Y DETALLES
-- =====================================

-- Pedido 1
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 770.00, 10.00, TIMESTAMP '2025-11-07 09:30:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT p.pedido_ID, pr.producto_ID, pr.nombre, pr.Precio, 1, 39, 450.00
FROM pedidos p, productos pr WHERE p.pedido_ID=1 AND pr.producto_ID=1;

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT p.pedido_ID, pr.producto_ID, pr.nombre, pr.Precio, 2, 42, 360.00
FROM pedidos p, productos pr WHERE p.pedido_ID=1 AND pr.producto_ID=4;

-- Pedido 2
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U003', 480.00, 10.00, TIMESTAMP '2025-11-07 15:45:00', 'Pendiente');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 2, pr.producto_ID, pr.nombre, pr.Precio, 1, 40, 480.00 FROM productos pr WHERE pr.producto_ID=2;

-- Pedido 3
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 930.00, 15.00, TIMESTAMP '2025-11-08 10:15:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 3, pr.producto_ID, pr.nombre, pr.Precio, 1, 39, 450.00 FROM productos pr WHERE pr.producto_ID=1;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 3, pr.producto_ID, pr.nombre, pr.Precio, 2, 42, 421.00 FROM productos pr WHERE pr.producto_ID=5;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 3, pr.producto_ID, pr.nombre, pr.Precio, 1, 40, 180.00 FROM productos pr WHERE pr.producto_ID=4;

-- Pedido 4
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U003', 680.00, 10.00, TIMESTAMP '2025-11-08 14:50:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 4, pr.producto_ID, pr.nombre, pr.Precio, 1, 40, 480.00 FROM productos pr WHERE pr.producto_ID=2;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 4, pr.producto_ID, pr.nombre, pr.Precio, 1, 41, 200.00 FROM productos pr WHERE pr.producto_ID=6;

-- Pedido 5
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 350.00, 5.00, TIMESTAMP '2025-11-09 11:20:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 5, pr.producto_ID, pr.nombre, pr.Precio, 1, 38, 200.00 FROM productos pr WHERE pr.producto_ID=9;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 5, pr.producto_ID, pr.nombre, pr.Precio, 1, 40, 150.00 FROM productos pr WHERE pr.producto_ID=8;

-- Pedido 6
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U003', 500.00, 10.00, TIMESTAMP '2025-11-09 16:10:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 6, pr.producto_ID, pr.nombre, pr.Precio, 1, 42, 500.00 FROM productos pr WHERE pr.producto_ID=7;

-- Pedido 7
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 600.00, 10.00, TIMESTAMP '2025-10-20 10:00:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 7, pr.producto_ID, pr.nombre, pr.Precio, 1, 39, 450.00 FROM productos pr WHERE pr.producto_ID=1;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 7, pr.producto_ID, pr.nombre, pr.Precio, 1, 42, 150.00 FROM productos pr WHERE pr.producto_ID=4;

-- Pedido 8
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U003', 720.00, 12.00, TIMESTAMP '2025-12-05 14:30:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 8, pr.producto_ID, pr.nombre, pr.Precio, 1, 40, 480.00 FROM productos pr WHERE pr.producto_ID=2;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 8, pr.producto_ID, pr.nombre, pr.Precio, 2, 42, 240.00 FROM productos pr WHERE pr.producto_ID=5;

-- Pedido 9
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 800.00, 15.00, TIMESTAMP '2026-01-10 09:45:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 9, pr.producto_ID, pr.nombre, pr.Precio, 1, 41, 320.00 FROM productos pr WHERE pr.producto_ID=3;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 9, pr.producto_ID, pr.nombre, pr.Precio, 1, 42, 480.00 FROM productos pr WHERE pr.producto_ID=7;

-- Pedido 10
INSERT INTO pedidos (IDCliente, total_pagar, CostoEnvio, fecha, estado)
VALUES ('U001', 650.00, 10.00, TIMESTAMP '2026-01-10 09:45:00', 'Completado');

INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 10, pr.producto_ID, pr.nombre, pr.Precio, 1, 39, 450.00 FROM productos pr WHERE pr.producto_ID=1;
INSERT INTO pedido_detalle (pedido_ID, producto_ID, nombre_producto, precio_unitario, cantidad, talla, subtotal)
SELECT 10, pr.producto_ID, pr.nombre, pr.Precio, 1, 41, 200.00 FROM productos pr WHERE pr.producto_ID=6;
