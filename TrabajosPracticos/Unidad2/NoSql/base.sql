CREATE TABLE RUBRO(
   CodRub INTEGER NOT NULL,
   NomRub VARCHAR(32) NOT NULL
);

CREATE TABLE PRODUCTO(
   CodRub INTEGER NOT NULL,
   CodPro INTEGER NOT NULL,
   NomPro VARCHAR(32) NOT NULL,
   CONSTRAINT PK_PRODUCTO PRIMARY KEY (CodRub,CodPro)
);
