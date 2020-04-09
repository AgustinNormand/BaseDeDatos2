SET TERM ^ ;
ALTER PROCEDURE EJEMPLO_8 (
    NOMBRE_TABLA varchar(30) )
AS
DECLARE COMANDO VARCHAR(512);
   DECLARE COMANDO_DOS VARCHAR(1024);
BEGIN
   COMANDO = 'CREATE TABLE ' || :NOMBRE_TABLA ||
             '(DNI INTEGER NOT NULL,
              NOMBRE VARCHAR(30) NOT NULL,
              CONSTRAINT PK_' || :NOMBRE_TABLA || ' PRIMARY KEY (DNI));';

   COMANDO_DOS = 'CREATE PROCEDURE EJEMPLO_8(NOMBRE_TABLA VARCHAR(30))
                  AS
                     DECLARE COMANDO VARCHAR(512);
                  BEGIN
                     COMANDO = ' || '''' || :COMANDO || '''' || ';
                     EXECUTE STATEMENT :COMANDO;
                     EXECUTE STATEMENT :COMANDO WITH COMMON TRANSACTION
                     ON EXTERNAL ''192.168.0.5/3050:/var/lib/firebird/2.5/data/dbDebian2.fdb''
                     AS USER ''SYSDBA'' PASSWORD ''masterkey'';
                  END';

   EXECUTE STATEMENT :COMANDO_DOS WITH COMMON TRANSACTION
   ON EXTERNAL '192.168.0.29/3050:/var/lib/firebird/2.5/data/dbDebian1.fdb'
   AS USER 'SYSDBA' PASSWORD 'masterkey';

   EXECUTE STATEMENT 'EXECUTE PROCEDURE EJEMPLO_8('||''''||:NOMBRE_TABLA||''''||');' WITH COMMON TRANSACTION
   ON EXTERNAL '192.168.0.29/3050:/var/lib/firebird/2.5/data/dbDebian1.fdb'
   AS USER 'SYSDBA' PASSWORD 'masterkey';

   EXECUTE STATEMENT :COMANDO;   
   

END^
SET TERM ; ^

