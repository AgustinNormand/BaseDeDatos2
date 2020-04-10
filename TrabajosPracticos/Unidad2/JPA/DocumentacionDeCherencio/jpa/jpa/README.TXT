------------------------------------------------------------------------
Universidad Nacional de Lujan
Departamento de Ciencias Basicas
Division Estadistica y Sistemas
11409 Programacion III / 11411 Base de Datos
Proyecto BlueJ que utiliza JPA para la persistencia de entidades en b.d.
Firebird
Atte. Guillermo Cherencio
------------------------------------------------------------------------

PROJECT TITLE: jpa 2.0

PURPOSE OF PROJECT: demostracion de uso de JPA desde Java SE

VERSION or DATE: 1.0

HOW TO START THIS PROJECT: Generar package (.jar) del proyecto incluyendo 
librerias jaybird (driver jdbc), 2 .jar de eclipseLink (JPA provider)

AUTHORS:Guillermo Cherencio

USER INSTRUCTIONS:
-Crear subdirectorio "META-INF" dentro del directorio del proyecto bluej
-Poner dentro de "META-INF" archivo persistence.xml 
-Dentro de persistence.xml se describe la unidad de persistencia "tapas" que sera
 usada por el proyecto
-La unidad de persistencia "tapas" permite conexion con b.d. firebird
/var/lib/firebird/2.0/data/tapasv.gdb utilizando usuario sysdba/masterkey


