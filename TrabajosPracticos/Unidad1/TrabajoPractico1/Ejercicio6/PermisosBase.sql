-- #### SP_FACTURA

GRANT EXECUTE ON PROCEDURE SP_FACTURA TO USUARIO;

GRANT ALL ON FACTURA TO PROCEDURE SP_FACTURA;

-- ## INSERT

GRANT ALL ON FACTURA_AUX TO TRIGGER TRG_BIFACTURA;

GRANT USAGE ON EXCEPTION EX_FECHA_FACTURA TO TRIGGER TRG_BIFACTURA;

GRANT ALL ON FACTURA_AUX TO TRIGGER TRG_AIFACTURA;

-- ## UPDATE

GRANT ALL ON PRODUCTO TO TRIGGER TRG_AUFACTURA;

GRANT ALL ON FACTURA_AUX TO TRIGGER TRG_AUFACTURA;

GRANT ALL ON DETALLE TO TRIGGER TRG_AUFACTURA;

GRANT USAGE ON EXCEPTION EX_TRANSISION_INVALIDA TO TRIGGER TRG_BUFACTURA;

GRANT USAGE ON EXCEPTION EX_FALTA_STOCK TO TRIGGER TRG_AUFACTURA;

-- ## DELETE

GRANT ALL ON FACTURA_AUX TO TRIGGER TRG_ADFACTURA;



-- #### SP_ESTADO_FACTURA

GRANT EXECUTE ON PROCEDURE SP_ESTADO_FACTURA TO USUARIO;

GRANT ALL ON FACTURA TO PROCEDURE SP_ESTADO_FACTURA;

-- #### SP_SFACTURA

GRANT EXECUTE ON PROCEDURE SP_SFACTURA TO USUARIO;

GRANT ALL ON FACTURA TO PROCEDURE SP_SFACTURA;



-- #### SP_PRODUCTO

GRANT EXECUTE ON PROCEDURE SP_PRODUCTO TO USUARIO;

GRANT ALL ON PRODUCTO TO PROCEDURE SP_PRODUCTO;



-- #### SP_SPRODUCTO

GRANT EXECUTE ON PROCEDURE SP_SPRODUCTO TO USUARIO;

GRANT ALL ON PRODUCTO TO PROCEDURE SP_SPRODUCTO;



-- ## SP_DETALLE

GRANT EXECUTE ON PROCEDURE SP_DETALLE TO USUARIO;

GRANT ALL ON DETALLE TO PROCEDURE SP_DETALLE;

-- ## INSERT

GRANT ALL ON PRODUCTO TO TRIGGER TRG_BIDETALLE;

GRANT USAGE ON EXCEPTION EX_FALTA_STOCK TO TRIGGER TRG_BIDETALLE;

GRANT USAGE ON EXCEPTION EX_PRECIO_BASE TO TRIGGER TRG_BIDETALLE;

GRANT ALL ON PRODUCTO TO TRIGGER TRG_AIDETALLE;

GRANT ALL ON FACTURA TO TRIGGER TRG_AIDETALLE;

-- ## UPDATE

GRANT ALL ON PRODUCTO TO TRIGGER TRG_BUDETALLE;

GRANT USAGE ON EXCEPTION EX_FALTA_STOCK TO TRIGGER TRG_BUDETALLE;

GRANT USAGE ON EXCEPTION EX_PRECIO_BASE TO TRIGGER TRG_BUDETALLE;

GRANT ALL ON FACTURA TO TRIGGER TRG_AUDETALLE;

GRANT ALL ON PRODUCTO TO TRIGGER TRG_AUDETALLE;

-- ## DELETE

GRANT ALL ON PRODUCTO TO TRIGGER TRG_ADDETALLE;

GRANT ALL ON FACTURA TO TRIGGER TRG_ADDETALLE;



-- ## SP_SDETALLE

GRANT EXECUTE ON PROCEDURE SP_SDETALLE TO USUARIO;

GRANT ALL ON DETALLE TO PROCEDURE SP_SDETALLE;

