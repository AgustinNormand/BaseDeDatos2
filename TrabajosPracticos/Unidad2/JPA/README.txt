Orden de complejidad de las anotaciones:

gestorBaseDeDatos.txt
persistenceFileExample.txt
managedOrDetached.txt
relations.txt
relationBidirectional.txt
oneToMany.txt
ManyToOne.txt


La clave creo que esta en entender que en la aplicacion, gracias a las anotaciones de JPA, se pueden definir listas con objetos que si se quisieran obtener en la base de datos, requieren joins.

En la instancia de Factura, puedo tener un array con todos los detalles que tiene, pero en la base de datos factura no tiene ningun detalle en la tabla, sino que es detalle el que tiene la foreign key a factura.
