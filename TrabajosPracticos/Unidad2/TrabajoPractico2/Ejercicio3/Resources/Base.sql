   CREATE TABLE DIRECCION(
      ID INTEGER NOT NULL,
      CALLE VARCHAR(50) NOT NULL,
      NRO INTEGER NOT NULL,
      LOCALIDAD VARCHAR(50) NOT NULL,

      CONSTRAINT PK_DIRECCION PRIMARY KEY (ID)
   );    

  CREATE TABLE CLIENTE(
      ID INTEGER NOT NULL,
      NOMBRE VARCHAR (25) NOT NULL,
      ID_DIRECCION INTEGER NOT NULL,

      CONSTRAINT PK_CLIENTE PRIMARY KEY (ID)
      CONSTRAINT FK_CLIENTE_DIRECCION FOREIGN KEY (ID_DIRECCION) REFERENCES DIRECCION (ID)
   );
   
   CREATE TABLE FACTURA (
      NRO INTEGER NOT NULL,
      IMPORTE INTEGER DEFAULT 0 NOT NULL,
      ID_CLIENTE INTEGER NOT NULL,

      CONSTRAINT PK_FACTURA PRIMARY KEY (NRO),
      CONSTRAINT FK_FACTURA_CLIENTE FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTE (ID)
   );

   CREATE TABLE PRODUCTO (
      ID INTEGER NOT NULL,
      DESCR VARCHAR(50) NOT NULL,
      STOCK INTEGER NOT NULL,

      CONSTRAINT PK_PRODUCTO PRIMARY KEY (ID)
   );

   CREATE TABLE DETALLE (
      NRO INTEGER NOT NULL,
      ID INTEGER NOT NULL,
      CANTIDAD INTEGER NOT NULL,
      PRECIO INTEGER NOT NULL,
      
      CONSTRAINT PK_DETALLE PRIMARY KEY (NRO,ID),
      CONSTRAINT FK_DETALLE_FACTURA FOREIGN KEY (NRO) REFERENCES FACTURA,
      CONSTRAINT FK_DETALLE_PRODUCTO FOREIGN KEY (ID) REFERENCES PRODUCTO
   );

   CREATE TABLE FACTURA_AUX(
      NRO INTEGER NOT NULL,
      FECHA DATE NOT NULL,
      
      CONSTRAINT PK_FACTURA_AUX PRIMARY KEY (NRO)
   );

   CREATE TABLE PROVEEDOR(
      ID INTEGER NOT NULL,
      NOMBRE VARCHAR(25) NOT NULL,

      CONSTRAINT PK_PROVEEDOR PRIMARY KEY (ID)
   );

   CREATE TABLE PROD_PROV(
      ID_PRODUCTO INTEGER NOT NULL,
      ID_PROVEEDOR INTEGER NOT NULL,

     CONSTRAINT FK_PROD_PROV_PRODUCTO FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTO,
     CONSTRAINT FK_PROD_PROV_PROVEEDOR FOREIGN KEY (ID_PROVEEDOR) REFERENCES PROVEEDOR,
     CONSTRAINT PK_PROD_PROV PRIMARY KEY (ID_PRODUCTO,ID_PROVEEDOR)
   );
   
   ALTER TABLE PRODUCTO
   ADD PRECIO_BASE INTEGER;

   ALTER TABLE PRODUCTO
   ADD PRECIO_COSTO INTEGER;

   ALTER TABLE FACTURA
   ADD ESTADO SMALLINT DEFAULT 0;

   ALTER TABLE FACTURA
   ADD FECHA DATE;

--   ALTER TABLE FACTURA
--   ADD CONSTRAINT CHK_FECHA CHECK ( FECHA >= (SELECT MAX(FECHA) FROM FACTURA_AUX));
-- LO TUVE QUE SACAR PORUQ LO VERIFICABA TAMBIEN EN CADA UPDATE, ENTONCES NO PODIA MODIFICAR FACTURAS VIEJAS.

   ALTER TABLE DETALLE
   ADD SUBTOTAL INTEGER;

   CREATE EXCEPTION EX_FALTA_STOCK 'No hay stock suficiente para el producto';

   CREATE EXCEPTION EX_PRECIO_BASE 'No se puede vender un producto por debajo de su precio base';

   CREATE EXCEPTION EX_TRANSISION_INVALIDA 'Transision de estado de factura no valido';

   CREATE EXCEPTION EX_ESTADO_INVALIDO 'Transision de estado de factura no valido';

   CREATE EXCEPTION EX_FECHA_FACTURA 'La fecha de la factura debe ser mayor a las ingresadas actualmente';

   SET TERM ^ ; 

   CREATE PROCEDURE SP_FACTURA (OPERATION CHAR(1),
                               NRO TYPE OF COLUMN FACTURA.NRO,                         
                               FECHA TYPE OF COLUMN FACTURA.FECHA)
   RETURNS (ERROR_CODE INTEGER,
            ERROR_MESSAGE VARCHAR (50))
   AS
   BEGIN
      ERROR_CODE = 0;
      ERROR_MESSAGE = 'OK';
      
      IF (OPERATION = 'I') THEN
         INSERT INTO FACTURA (NRO,FECHA) VALUES (:NRO,:FECHA);
      IF (OPERATION = 'U') THEN
         UPDATE FACTURA SET FECHA = :FECHA WHERE NRO = :NRO;   
      IF (OPERATION = 'D') THEN
         DELETE FROM FACTURA WHERE NRO = :NRO;

      WHEN ANY DO
      BEGIN
         ERROR_CODE = SQLCODE;
         ERROR_MESSAGE = 'OPERATION = ' || OPERATION || ' TABLE = FACTURA ';
      END
   END^

   CREATE PROCEDURE SP_ESTADO_FACTURA 
      (ESTADO TYPE OF COLUMN FACTURA.ESTADO,
      NRO TYPE OF COLUMN FACTURA.NRO)
   RETURNS (ERROR_CODE INTEGER,
            ERROR_MESSAGE VARCHAR (50))
   AS
   BEGIN
      ERROR_CODE = 0;
      ERROR_MESSAGE = 'OK';     
      IF ( ESTADO = 1) THEN
         UPDATE FACTURA SET ESTADO = 1 WHERE NRO = :NRO;
      IF ( ESTADO = 2) THEN
         UPDATE FACTURA SET ESTADO = 2 WHERE NRO = :NRO;
      WHEN ANY DO
         BEGIN
            ERROR_CODE = SQLCODE;
            ERROR_MESSAGE = 'ERROR IN SP_ESTADO_FACTURA';
         END
   END^


   CREATE PROCEDURE SP_SFACTURA
   RETURNS (NRO TYPE OF COLUMN FACTURA.NRO,
            IMPORTE TYPE OF COLUMN FACTURA.IMPORTE,
            ESTADO TYPE OF COLUMN FACTURA.ESTADO,
            FECHA TYPE OF COLUMN FACTURA.FECHA)
   AS
   BEGIN
      FOR SELECT NRO,IMPORTE,ESTADO,FECHA FROM FACTURA INTO :NRO,:IMPORTE,:ESTADO,:FECHA
      DO SUSPEND;
   END^

   CREATE PROCEDURE SP_PRODUCTO (OPERATION CHAR(1),
                                 ID TYPE OF COLUMN PRODUCTO.ID,
                                 DESCR TYPE OF COLUMN PRODUCTO.DESCR,
                                 STOCK TYPE OF COLUMN PRODUCTO.STOCK,
                                 PRECIO_BASE TYPE OF COLUMN PRODUCTO.PRECIO_BASE,
                                 PRECIO_COSTO TYPE OF COLUMN PRODUCTO.PRECIO_COSTO)
   RETURNS (ERROR_CODE INTEGER,
            ERROR_MESSAGE VARCHAR (50))
   AS
   BEGIN
      ERROR_CODE = 0;
      ERROR_MESSAGE = 'OK';

      IF (OPERATION = 'I') THEN
         INSERT INTO PRODUCTO (ID,DESCR,STOCK,PRECIO_BASE,PRECIO_COSTO) VALUES (:ID,:DESCR,:STOCK,:PRECIO_BASE,:PRECIO_COSTO);
      IF (OPERATION = 'U') THEN
         UPDATE PRODUCTO SET DESCR = :DESCR, STOCK = :STOCK, PRECIO_BASE = :PRECIO_BASE, PRECIO_COSTO = :PRECIO_COSTO WHERE ID = :ID;
      IF (OPERATION = 'D') THEN
         DELETE FROM PRODUCTO WHERE ID = :ID;

      WHEN ANY DO
      BEGIN
         ERROR_CODE = SQLCODE;
         ERROR_MESSAGE = 'OPERATION = ' || OPERATION || ' TABLE = PRODUCTO ';
      END
   END^

   CREATE PROCEDURE SP_SPRODUCTO
   RETURNS (ID TYPE OF COLUMN PRODUCTO.ID,
            DESCR TYPE OF COLUMN PRODUCTO.DESCR,
            STOCK TYPE OF COLUMN PRODUCTO.STOCK,
            PRECIO_BASE TYPE OF COLUMN PRODUCTO.PRECIO_BASE,
            PRECIO_COSTO TYPE OF COLUMN PRODUCTO.PRECIO_COSTO)
   AS
   BEGIN
      FOR SELECT ID,DESCR,STOCK,PRECIO_BASE,PRECIO_COSTO FROM PRODUCTO INTO :ID,:DESCR,:STOCK,:PRECIO_BASE,:PRECIO_COSTO
      DO SUSPEND;
   END^

   CREATE PROCEDURE SP_DETALLE (OPERATION CHAR(1),
                                NRO TYPE OF COLUMN DETALLE.NRO,
                                ID TYPE OF COLUMN DETALLE.ID,
                                CANTIDAD TYPE OF COLUMN DETALLE.CANTIDAD,
                                PRECIO TYPE OF COLUMN DETALLE.PRECIO)
   RETURNS (ERROR_CODE INTEGER,
            ERROR_MESSAGE VARCHAR (50))
   AS
   BEGIN
      ERROR_CODE = 0;
      ERROR_MESSAGE = 'OK';

      IF (OPERATION = 'I') THEN
         INSERT INTO DETALLE (NRO,ID,CANTIDAD,PRECIO,SUBTOTAL) VALUES (:NRO,:ID,:CANTIDAD,:PRECIO,0);
      IF (OPERATION = 'U') THEN
         UPDATE DETALLE SET CANTIDAD = :CANTIDAD, PRECIO = :PRECIO WHERE NRO = :NRO AND ID = :ID;
      IF (OPERATION = 'D') THEN
         DELETE FROM DETALLE WHERE NRO = :NRO AND ID = :ID;

      WHEN ANY DO
      BEGIN
         ERROR_CODE = SQLCODE;
         ERROR_MESSAGE = 'OPERATION = ' || OPERATION || ' TABLE = DETALLE ';
      END
   END^

   CREATE PROCEDURE SP_SDETALLE
   RETURNS (NRO TYPE OF COLUMN DETALLE.NRO,
            ID TYPE OF COLUMN DETALLE.ID,
            CANTIDAD TYPE OF COLUMN DETALLE.CANTIDAD,
            PRECIO TYPE OF COLUMN DETALLE.PRECIO,
            SUBTOTAL TYPE OF COLUMN DETALLE.SUBTOTAL)
   AS
   BEGIN
      FOR SELECT NRO,ID,CANTIDAD,PRECIO,SUBTOTAL FROM DETALLE INTO :NRO,:ID,:CANTIDAD,:PRECIO,:SUBTOTAL
      DO SUSPEND;
   END^

   -- TRIGGERS DETALLE --

   CREATE TRIGGER TRG_BIDETALLE FOR DETALLE
   ACTIVE
   BEFORE INSERT
   POSITION 0
   AS
   BEGIN
      IF ( (SELECT STOCK FROM PRODUCTO WHERE ID = NEW.ID) < NEW.CANTIDAD ) THEN
         EXCEPTION EX_FALTA_STOCK;
      IF ( NEW.PRECIO < (SELECT PRECIO_BASE FROM PRODUCTO WHERE ID = NEW.ID) ) THEN
         EXCEPTION EX_PRECIO_BASE;
      NEW.SUBTOTAL = NEW.CANTIDAD * NEW.PRECIO;
      
   END^

   CREATE TRIGGER TRG_AIDETALLE FOR DETALLE
   ACTIVE
   AFTER INSERT
   POSITION 0
   AS
   BEGIN
      UPDATE PRODUCTO SET STOCK = STOCK - NEW.CANTIDAD WHERE ID = NEW.ID;
      UPDATE FACTURA SET IMPORTE = IMPORTE + NEW.SUBTOTAL WHERE NRO = NEW.NRO;
   END^


   CREATE TRIGGER TRG_BUDETALLE FOR DETALLE
   ACTIVE
   BEFORE UPDATE
   POSITION 0
   AS
      DECLARE DELTACANTIDAD INTEGER;
   BEGIN
      IF (NEW.CANTIDAD <> OLD.CANTIDAD) THEN
      BEGIN
         DELTACANTIDAD = NEW.CANTIDAD - OLD.CANTIDAD;
         IF (DELTACANTIDAD > 0) THEN -- AGREGÓ PRODUCTOS AL DETALLE
            IF ( (SELECT STOCK FROM PRODUCTO WHERE ID = OLD.ID) < DELTACANTIDAD ) THEN --NEW.ID = OLD.ID (No permito cambios en pk)
               EXCEPTION EX_FALTA_STOCK;
      END
      IF (NEW.PRECIO <> OLD.PRECIO) THEN
         IF ( NEW.PRECIO < (SELECT PRECIO_BASE FROM PRODUCTO WHERE ID = NEW.ID) ) THEN
            EXCEPTION EX_PRECIO_BASE;   
      NEW.SUBTOTAL = NEW.CANTIDAD * NEW.PRECIO;      
   END^

   CREATE TRIGGER TRG_AUDETALLE FOR DETALLE
   ACTIVE
   AFTER UPDATE
   POSITION 0
   AS
      DECLARE DELTACANTIDAD INTEGER;
      DECLARE DELTASUBTOTAL FLOAT;
   BEGIN
      IF (NEW.SUBTOTAL <> OLD.SUBTOTAL) THEN
      BEGIN
         DELTASUBTOTAL = NEW.SUBTOTAL - OLD.SUBTOTAL;
         IF (DELTASUBTOTAL > 0 ) THEN -- EL DETALLE AUMENTÓ EN SUBTOTAL
            UPDATE FACTURA SET IMPORTE = (IMPORTE + :DELTASUBTOTAL) WHERE NRO = OLD.NRO;
         ELSE 
         --EL DETALLE DISMINUYÓ EN SUBTOTAL
            UPDATE FACTURA SET IMPORTE = (IMPORTE - ABS(:DELTASUBTOTAL)) WHERE NRO = OLD.NRO;
      END
      IF (NEW.CANTIDAD <> OLD.CANTIDAD) THEN
      BEGIN
         DELTACANTIDAD = NEW.CANTIDAD - OLD.CANTIDAD;  
         --CANTIDAD DE PRODUCTOS QUE SE AGREGARON O SE RESTARON DEL DETALLE    
         IF (DELTACANTIDAD > 0) THEN  
         --AGREGÓ PRODUCTOS AL DETALLE
            UPDATE PRODUCTO SET STOCK = (STOCK - :DELTACANTIDAD) WHERE ID = NEW.ID;  
            --SE VERIFICÓ EN TRG_BUDETALLE QUE TENGA STOCK  DISPONIBLE
         ELSE
         --RESTÓ PRODUCTOS DEL DETALLE
            UPDATE PRODUCTO SET STOCK = (STOCK + ABS(:DELTACANTIDAD));
            --SUMO LOS PRODUCTOS QUE SE AGREGARON DEL DETALLE, ANTES LOS HAGO POSITIVOS.
      END
   END^

   CREATE TRIGGER TRG_ADDETALLE FOR DETALLE
   ACTIVE
   AFTER DELETE
   POSITION 0
   AS
   BEGIN
      UPDATE PRODUCTO SET STOCK = STOCK + OLD.CANTIDAD WHERE ID = OLD.ID;
      UPDATE FACTURA SET IMPORTE = IMPORTE - OLD.SUBTOTAL WHERE NRO = OLD.NRO;
   END^

   
   -- TRIGGERS FACTURA --

   CREATE TRIGGER TRG_BIFACTURA FOR FACTURA
   ACTIVE
   BEFORE INSERT
   POSITION 0
   AS
   BEGIN
      IF ( NEW.FECHA < ( SELECT MAX(FECHA) FROM FACTURA_AUX ) ) THEN
         EXCEPTION EX_FECHA_FACTURA;
   END^

   CREATE TRIGGER TRG_AIFACTURA FOR FACTURA
   ACTIVE
   AFTER INSERT
   POSITION 0
   AS
   BEGIN      
      INSERT INTO FACTURA_AUX
      VALUES (NEW.NRO,NEW.FECHA);
   END^

   CREATE TRIGGER TRG_ADFACTURA FOR FACTURA
   ACTIVE
   AFTER DELETE
   POSITION 0
   AS
   BEGIN
      DELETE FROM FACTURA_AUX
      WHERE NRO = OLD.NRO;
   END^

   CREATE TRIGGER TRG_BUFACTURA FOR FACTURA
   ACTIVE
   BEFORE UPDATE
   POSITION 0
   AS
   BEGIN
      IF (NEW.ESTADO = 0) THEN
      BEGIN
         IF (OLD.ESTADO = 1) THEN
            EXCEPTION EX_TRANSISION_INVALIDA;   
         IF (OLD.ESTADO = 2) THEN
            EXCEPTION EX_TRANSISION_INVALIDA;   
      END
   END^

   CREATE TRIGGER TRG_AUFACTURA FOR FACTURA
   ACTIVE
   AFTER UPDATE
   POSITION 0
   AS
      DECLARE STOCK INTEGER;
      DECLARE ID INTEGER;
      DECLARE STOCK_PRODUCTO INTEGER;
   BEGIN
      IF (NEW.ESTADO = 2 AND OLD.ESTADO = 1) THEN  --PASA DE FINALIZADA A ANULADA
         --DEVOLVER TODOS LOS PRODUCTOS AL STOCK
         FOR SELECT CANTIDAD,ID FROM DETALLE WHERE NRO = OLD.NRO --OLD O NEW ES IGUAL, NO PERMITO CAMBIOS EN PK
         INTO :STOCK,:ID
         DO
            UPDATE PRODUCTO SET STOCK = STOCK + :STOCK WHERE ID = :ID;
         
      IF (NEW.ESTADO = 1 AND OLD.ESTADO = 2) THEN  --PASA DE ANULADA A FINALIZADA.
         --RESTAR TODOS LOS PRODUCTOS AL STOCK   
         FOR SELECT CANTIDAD,ID FROM DETALLE WHERE NRO = OLD.NRO --OLD O NEW ES IGUAL, NO PERMITO CAMBIOS EN PK
         INTO :STOCK,:ID
         DO
         BEGIN
            STOCK_PRODUCTO = (SELECT STOCK FROM PRODUCTO WHERE ID = :ID);
            IF (:STOCK_PRODUCTO < :STOCK) THEN
               EXCEPTION EX_FALTA_STOCK;
            UPDATE PRODUCTO SET STOCK = STOCK - :STOCK WHERE ID = :ID;
         END
      
      IF (NEW.FECHA <> OLD.FECHA) THEN
         UPDATE FACTURA_AUX SET FECHA = NEW.FECHA WHERE NRO = NEW.NRO; -- SI NO ES LA FACTURA CON MAYOR FECHA, NO VA A ACTUALIZAR NADA.
        
      END^
   SET TERM ; ^
