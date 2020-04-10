-- TESTING

-- #### INICIALIZAR LA CAPACIDAD DE SALAPELICULA, CON LA CAPACIDAD DE lA SALA.

-- ## TUPLAS NECESARIAS

-- INSERT INTO PELICULA (IDPELICULA,NOMBRE,VALOR) VALUES (1,'RELATOS SALVAJES',500);

-- INSERT INTO SALA (IDSALA,CAPACIDAD) VALUES (1,50);

-- INSERT INTO SALAPELICULA (IDSALA,IDPELICULA,FECHAINICIO,FECHAFIN) VALUES (1,1,'24.03.2020, 12:00:00.000','24.03.2020, 15:00:00.000');

-- LA CAPACIDAD DE SALAPELICULA DEBERÍA SER 50.

-- ## LOS MENORES DE 10 PASAN GRATIS

-- EXECUTE PROCEDURE SP_INSERT_ENTRADA(1,1,1,9);

-- EL VALOR DE LA ENTRADA DEBERIA SER 0

-- ## LOS MENORES DE 14 PAGAN EL 50%

-- EXECUTE PROCEDURE SP_INSERT_ENTRADA(2,1,1,13);

-- EL VALOR DEBERIA SER 250

-- ## LOS MAYORES DE 13 PAGAN EL 100%

-- EXECUTE PROCEDURE SP_INSERT_ENTRADA(3,1,1,14);

-- EL VALOR DEBERIA SER 500

-- ## MOSTRAR EL TOTAL GENERAL DE LAS ENTRADAS

-- EXECUTE PROCEDURE SP_TOTAL_ENTRADAS;

-- DEBERIA SER 750

-- ## FECHA_DESDE Y FECHA_HASTA TESTEARLO VIENDO LAS FECHAS DE LAS ENTRADAS

-- ## TOTAL RECAUDADO EN ENTRADAS PARA LOS MAYORES DE 13

-- EXECUTE PROCEDURE SP_TOTAL_ENTRADAS_MAYORES;

-- DEBERIA SER 500

-- #### TESTEOS MAS DELICADOS

-- INSERT INTO PELICULA (IDPELICULA,NOMBRE,VALOR) VALUES 2,'A',-1); -- NO DEBERIA PODER

-- ##

-- INSERT INTO SALA (IDSALA,CAPACIDAD) VALUES (2,-1); -- NO DEBERIA PODER

-- ##

-- INSERT INTO SALA (IDSALA,CAPACIDAD) VALUES (2,50); -- COMITEAR

-- INSERT INTO SALAPELICULA (IDSALA,IDPELICULA,FECHAINICIO,FECHAFIN) VALUES (2,1,'24.03.2020, 15:00:00.000','24.03.2020, 12:00:00.000'); -- NO DEBERIA PODER

-- ##

-- INSERT INTO SALAPELICULA (IDSALA,IDPELICULA,FECHAINICIO,FECHAFIN) VALUES (2,1,'24.04.2020, 12:00:00.000','24.04.2020, 15:00:00.000'); -- DEBERIA PODER

-- EXECUTE PROCEDURE SP_INSERT_ENTRADA(1,2,1,10); --NO DEBERIA PODER POR LA FECHA

-- EXECUTE PROCEDURE SP_INSERT_ENTRADA(1,2,1,-1); --NO DEBERIA PODER POR LA EDAD


