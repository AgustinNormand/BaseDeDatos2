CREATE TABLE FACTURA(
	NRO INT NOT NULL,
	IDC INT NOT NULL,
	FECHA DATE NOT NULL,
	
	CONSTRAINT PK_FACTURA PRIMARY KEY (NRO),
	CONSTRAINT FK_FACTURA_CLIENTE FOREIGN KEY (IDC) REFERENCES CLIENTE,
);

CREATE TABLE FACTURA_AUX(
	NRO INT NOT NULL,
	ESTADO INT DEFAULT 0,
	NETO INT DEFAULT 0,
	IVA1 DOUBLE DEFAULT 0,
	IVA2 DOUBLE DEFAULT 0,
	MONTO DOUBLE DEFAULT 0,
	
	CONSTRAINT PK_FACTURA_AUX PRIMARY KEY (NRO),
);

CREATE TABLE CLIENTE(
	IDC INT NOT NULL,
	DESCR VARCHAR(20) NOT NULL,
	TIVA INT NOT NULL,
	
	CONSTRAINT PK_CLIENTE PRIMARY KEY (IDC)
);

CREATE TABLE PRODUCTO(
	IDP INT NOT NULL,
	PRECIO_BASE DOUBLE NOT NULL,
	
	CONSTRAINT PK_PRODUCTO PRIMARY KEY (IDP)
);

CREATE TABLE DETALLE(
	NRO INT NOT NULL,
	IDP INT NOT NULL,
	CANT INT NOT NULL,
	PRECIO DOUBLE NOT NULL,
	
	CONSTRAINT PK_DETALLE PRIMARY KEY (NRO,IDP)
	CONSTRAINT FK_DETALLE_PRODUCTO FOREIGN KEY (IDP) REFERENCES PRODUCTO,
	CONSTRAINT FK_DETALLE_FACTURA FOREIGN KEY (NRO) REFERENCES FACTURA
);

CREATE EXCEPTION EX_CLAVE_PRIMARIA 'No es posible modificar la clave primaria';

SET TERM ^ ;

CREATE TRIGGER TRG_AIFACTURA FOR FACTURA
ACTIVE
AFTER INSERT
POSITION 0
AS
BEGIN
	INSERT INTO FACTURA_AUX(NRO) VALUES (NEW.NRO);
END^

CREATE TRIGGER TRG_AIDETALLE FOR DETALLE
ACTIVE 
AFTER INSERT
POSITION 0
AS
	DECLARE TIPO_IVA_CLIENTE INT;
BEGIN
	UPDATE FACTURA_AUX SET NETO = NETO + NEW.CANTIDAD*NEW.PRECIO WHERE NRO = NEW.NRO;
	TIPO_IVA_CLIENTE = (SELECT TIVA FROM CLIENTE WHERE IDC = (SELECT IDC FROM FACTURA WHERE NRO = NEW.NRO));
	IF(TIPO_IVA_CLIENTE = 1 OR TIPO_IVA_CLIENTE = 2 OR TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA1 = NETO * 0,21 WHERE NRO = NEW.NRO;
	IF(TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA2 = (NETO * 10,5) / 100 WHERE NRO = NEW.NRO;
	UPDATE FACTURA_AUX SET MONTO = NETO + IVA1 + IVA2 WHERE NRO = NEW.NRO;
END^

CREATE TRIGGER TRG_BUDETALLE FOR DETALLE
ACTIVE 
BEFORE UPDATE
POSITION 0
AS
BEGIN
	IF (NEW.NRO <> OLD.NRO OR NEW.IDP <> OLD.IDP) THEN
		EXCEPTION EX_CLAVE_PRIMARIA;
END^

CREATE TRIGGER TRG_AUDETALLE FOR DETALLE
ACTIVE 
AFTER UPDATE
POSITION 0
AS
	DECLARE TIPO_IVA_CLIENTE INT;
BEGIN
	UPDATE FACTURA_AUX SET NETO = NETO - OLD.CANTIDAD*OLD.PRECIO WHERE NRO = NEW.NRO; -- ELIMINO LA CANTIDAD Y PRECIO ANTERIOR DEL NETO
	UPDATE FACTURA_AUX SET NETO = NETO + NEW.CANTIDAD*NEW.PRECIO WHERE NRO = NEW.NRO; -- ACTUALIZO EL NETO CON LA CANTIDAD Y PRECIO NUEVOS
	TIPO_IVA_CLIENTE = (SELECT TIVA FROM CLIENTE WHERE IDC = (SELECT IDC FROM FACTURA WHERE NRO = NEW.NRO));
	IF(TIPO_IVA_CLIENTE = 1 OR TIPO_IVA_CLIENTE = 2 OR TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA1 = NETO * 0,21 WHERE NRO = NEW.NRO;
	IF(TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA2 = (NETO * 10,5) / 100 WHERE NRO = NEW.NRO;
	UPDATE FACTURA_AUX SET MONTO = NETO + IVA1 + IVA2 WHERE NRO = NEW.NRO;
END^

CREATE TRIGGER TRG_ADDETALLE FOR DETALLE
ACTIVE 
AFTER DELETE
POSITION 0
AS
	DECLARE TIPO_IVA_CLIENTE INT;
BEGIN
	UPDATE FACTURA_AUX SET NETO = NETO - OLD.CANTIDAD*OLD.PRECIO WHERE NRO = NEW.NRO;
	TIPO_IVA_CLIENTE = (SELECT TIVA FROM CLIENTE WHERE IDC = (SELECT IDC FROM FACTURA WHERE NRO = NEW.NRO));
	IF(TIPO_IVA_CLIENTE = 1 OR TIPO_IVA_CLIENTE = 2 OR TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA1 = NETO * 0,21 WHERE NRO = NEW.NRO;
	IF(TIPO_IVA_CLIENTE = 4) THEN
		UPDATE FACTURA_AUX SET IVA2 = (NETO * 10,5) / 100 WHERE NRO = NEW.NRO;
	UPDATE FACTURA_AUX SET MONTO = NETO + IVA1 + IVA2 WHERE NRO = NEW.NRO;
END^

SET TERM ; ^

