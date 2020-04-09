SET TERM ^ ;

CREATE TRIGGER TRG_AUPERSONA FOR PERSONA
ACTIVE
AFTER UPDATE
AS
   DECLARE COMANDO VARCHAR(512);
BEGIN
   COMANDO = 'UPDATE PERSONA SET DNI = ' || NEW.DNI ||
                                ',' ||
                                'NOMBRE = ' || '''' || NEW.NOMBRE || '''' ||
             ' WHERE DNI = ' || OLD.DNI || ';';
   EXECUTE STATEMENT :COMANDO WITH COMMON TRANSACTION
   ON EXTERNAL '192.168.0.29/3050:/var/lib/firebird/2.5/data/dbDebian1.fdb'
   AS USER 'SYSDBA' PASSWORD 'masterkey';
END^

SET TERM ; ^
