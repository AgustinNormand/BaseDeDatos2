CREATE TABLE PELICULA
(
   IDPELICULA INTEGER NOT NULL,
   NOMBRE VARCHAR(30) NOT NULL,
   VALOR FLOAT NOT NULL CHECK ( VALOR >= 0 ) ,

   CONSTRAINT PK_PELICULA PRIMARY KEY (IDPELICULA)
);

CREATE TABLE SALA
(
   IDSALA INTEGER NOT NULL,
   CAPACIDAD INTEGER NOT NULL CHECK ( CAPACIDAD > 0 ),

   CONSTRAINT PK_SALA PRIMARY KEY (IDSALA) 
);

CREATE TABLE SALAPELICULA
(
   IDSALA INTEGER NOT NULL,
   IDPELICULA INTEGER NOT NULL,
   FECHAINICIO TIMESTAMP NOT NULL,
   FECHAFIN TIMESTAMP NOT NULL CHECK ( FECHAFIN > FECHAINICIO ),
   DISPONIBLE INTEGER NOT NULL CHECK ( DISPONIBLE > 0 ),

   CONSTRAINT FK_SALAPELICULA_SALA FOREIGN KEY (IDSALA) REFERENCES SALA,
   CONSTRAINT FK_SALAPELICULA_PELICUKA FOREIGN KEY (IDPELICULA) REFERENCES PELICULA,
   CONSTRAINT PK_SALAPELICULA PRIMARY KEY (IDSALA,IDPELICULA)
);

CREATE TABLE ENTRADA
(
   IDENTRADA INTEGER NOT NULL,
   IDSALA INTEGER NOT NULL,
   IDPELICULA INTEGER NOT NULL,
   FECHA TIMESTAMP NOT NULL /*CHECK ( FECHA >= (SELECT FECHAINICIO FROM SALAPELICULA WHERE IDSALA = IDSALA)  
                                AND FECHA <= (SELECT FECHAFIN FROM SALAPELICULA WHERE IDSALA = IDSALA) )*/,
   --DESCOMENTANDO EL BLOQUE, VERIFICARÍA QUE LA FECHA Y HORA DE LA ENTRADA ESTE ENTRE LA FECHA DE INICIO Y FIN DE LA PELICULA.  
   EDAD INTEGER NOT NULL CHECK ( EDAD >= 0 AND EDAD <= 100 ),
   VALOR FLOAT NOT NULL CHECK ( VALOR >= 0 ),

   CONSTRAINT FK_ENTRADA_SALA FOREIGN KEY (IDSALA) REFERENCES SALA,
   CONSTRAINT FK_ENTRADA_PELICULA FOREIGN KEY (IDPELICULA) REFERENCES PELICULA,
   CONSTRAINT PK_ENTRADA PRIMARY KEY (IDENTRADA, IDSALA, IDPELICULA)
);

SET TERM ^ ;

CREATE TRIGGER TRG_BIENTRADA FOR ENTRADA
ACTIVE
BEFORE INSERT
POSITION 0
AS
   DECLARE VALOR_BASE FLOAT;
BEGIN
   IF ( NEW.EDAD < 10 ) THEN
      VALOR_BASE = 0;
   ELSE
   BEGIN
      VALOR_BASE = ( SELECT VALOR FROM PELICULA WHERE IDPELICULA = NEW.IDPELICULA );
      IF ( NEW.EDAD >= 10 AND NEW.EDAD < 14 ) THEN
         VALOR_BASE = VALOR_BASE * 0.5;
      -- SI LA EDAD ES MAYOR A 14, NO SE MODIFICA EL VALOR_BASE.
   END
   NEW.VALOR = VALOR_BASE;
   NEW.FECHA = CURRENT_TIMESTAMP;
END^

CREATE TRIGGER TRG_BISALAPELICULA FOR SALAPELICULA
ACTIVE
BEFORE INSERT
POSITION 0
AS
BEGIN
   NEW.DISPONIBLE = ( SELECT CAPACIDAD FROM SALA WHERE IDSALA = NEW.IDSALA );
END^

CREATE PROCEDURE SP_INSERT_ENTRADA 
(
   IDENTRADA TYPE OF COLUMN ENTRADA.IDENTRADA,
   IDSALA TYPE OF COLUMN ENTRADA.IDSALA,
   IDPELICULA TYPE OF COLUMN ENTRADA.IDPELICULA,
   EDAD TYPE OF COLUMN ENTRADA.EDAD
)

RETURNS
(
   ERROR_CODE INTEGER,
   ERROR_MESSAGE VARCHAR(50)
)

AS
BEGIN
   ERROR_CODE = 0;
   ERROR_MESSAGE = 'OK';
   INSERT INTO ENTRADA (IDENTRADA,IDSALA,IDPELICULA,EDAD)
   VALUES (:IDENTRADA,:IDSALA,:IDPELICULA,:EDAD);
   WHEN ANY DO 
   BEGIN
      ERROR_CODE = SQLCODE;
      ERROR_MESSAGE = 'ERROR IN SP_INSERT_ENTRADA, ERROR= ' || SQLCODE;
   END
END^

CREATE PROCEDURE SP_TOTAL_ENTRADAS
RETURNS
(
   TOTAL TYPE OF COLUMN ENTRADA.VALOR
)
AS
BEGIN
   TOTAL = (SELECT SUM(VALOR) FROM ENTRADA);
   SUSPEND;
END^

CREATE PROCEDURE SP_ENTRADAS_DESDE_HASTA
(
   FECHA_DESDE TYPE OF COLUMN ENTRADA.FECHA,
   FECHA_HASTA TYPE OF COLUMN ENTRADA.FECHA
)
RETURNS
(
   IDENTRADA TYPE OF COLUMN ENTRADA.IDENTRADA,
   IDSALA TYPE OF COLUMN ENTRADA.IDSALA,
   IDPELICULA TYPE OF COLUMN ENTRADA.IDPELICULA,
   FECHA TYPE OF COLUMN ENTRADA.FECHA,
   EDAD TYPE OF COLUMN ENTRADA.EDAD,
   VALOR TYPE OF COLUMN ENTRADA.VALOR
)
AS
BEGIN
   FOR
      SELECT IDENTRADA,IDSALA,IDPELICULA,FECHA,EDAD,VALOR
      FROM ENTRADA
      WHERE FECHA >= :FECHA_DESDE AND FECHA <= :FECHA_HASTA
   INTO :IDENTRADA,:IDSALA,:IDPELICULA,:FECHA,:EDAD,:VALOR
   DO
      SUSPEND;
END^

CREATE PROCEDURE SP_TOTAL_ENTRADAS_MAYORES
RETURNS
(
   TOTAL TYPE OF COLUMN ENTRADA.VALOR
)
AS
BEGIN
   TOTAL = (SELECT SUM(VALOR) FROM ENTRADA WHERE EDAD > 13);
   SUSPEND;
END^


SET TERM ; ^
