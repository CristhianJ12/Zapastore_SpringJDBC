-- =====================================
-- DESACTIVAR RESTRICCIONES TEMPORALMENTE
-- (H2 usa comandos distintos a MySQL)
-- =====================================
SET REFERENTIAL_INTEGRITY FALSE;

-- BORRAR TABLAS SI EXISTEN
DROP TABLE IF EXISTS pedido_detalle;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS usuarios;

-- =====================================
-- TABLA USUARIOS
-- =====================================
CREATE TABLE usuarios (
    IDUsuario VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    Rol VARCHAR(20) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo'
);

-- =====================================
-- TABLA CATEGORÍAS
-- =====================================
CREATE TABLE categorias (
    categoria_ID INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo'
);

-- =====================================
-- TABLA PRODUCTOS
-- =====================================
CREATE TABLE productos (
    producto_ID INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria_ID INT,
    Precio DECIMAL(10,2) NOT NULL,
    img_Url VARCHAR(255),
    descripcion VARCHAR(500),
    estado VARCHAR(20) DEFAULT 'Activo',
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_ID)
        REFERENCES categorias(categoria_ID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- =====================================
-- TABLA PEDIDOS
-- =====================================
CREATE TABLE pedidos (
    pedido_ID INT AUTO_INCREMENT PRIMARY KEY,
    IDCliente VARCHAR(36),
    total_pagar DECIMAL(10,2) NOT NULL,
    CostoEnvio DECIMAL(10,2) DEFAULT 0.00,
    fecha TIMESTAMP NOT NULL,
    estado VARCHAR(20) DEFAULT 'Pendiente',
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (IDCliente)
        REFERENCES usuarios(IDUsuario)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- =====================================
-- TABLA PEDIDO DETALLE
-- =====================================
CREATE TABLE pedido_detalle (
    pedidodetalle_ID INT AUTO_INCREMENT PRIMARY KEY,
    pedido_ID INT,
    producto_ID INT,
    nombre_producto VARCHAR(100),
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    talla INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_ID)
        REFERENCES pedidos(pedido_ID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_ID)
        REFERENCES productos(producto_ID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- =====================================
-- REACTIVAR RESTRICCIONES
-- =====================================
SET REFERENTIAL_INTEGRITY TRUE;
