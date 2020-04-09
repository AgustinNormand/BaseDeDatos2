SET TERM ^ ;

CREATE TRIGGER TRG_AIPERSONA FOR PERSONA
ACTIVE
AFTER INSERT
POSITION 0
AS
   DECLARE COMANDO VARCHAR(512);
BEGIN
   COMANDO = 'INSERT INTO PERSONA(DNI,NOMBRE) VALUES(' || NEW.DNI
                                                       || ','
                                                       || ''''
                                                       || NEW.NOMBRE
                                                       || ''''
                                                       || ');';
   EXECUTE STATEMENT :COMANDO WITH COMMON TRANSACTION
   ON EXTERNAL '192.168.0.29/3050:/var/lib/firebird/2.5/data/dbDebian1.fdb'
   AS USER 'SYSDBA' PASSWORD 'masterkey';
END^
SET TERM ; ^
