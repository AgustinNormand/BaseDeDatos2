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

Las clase que tenga una relacion OneToMany, es una relacion 1 a N en la base de datos, haciendo el modelo logico, la clave se va para el lado de la N.
Por lo tanto, una clase que tiene un atrituto OneToMany, JPA completa ese atributo haciendo JOINS.

Lo mismo pasa con los atributos OneToOne, la clave tiene que ir para una de las dos tablas segun el modelo logico.
La clase que tiene el JoinColumn, es la tabla en la base de datos que tiene la clave del otro extremo
La clase que tiene el @OneToOne(mappedBy = "...") en un atributo, JPA completa ese atributo haciendo JOINS, pero no existe en realidad en esa tabla a la que hace referencia esa clase, un atributo.



Tambien es importante comprender que si elimino una instancia de una lista generada por JPA (con joins) no se va a eliminar de la base de datos.
Ejemplo, si un autor tiene muchos libros, y un libro pertenece a un autor.
El que tiene la clave foranea es el libro, es en donde esta almacenada esa relacion en la base de datos.
Si yo elimino el libro, en la lista libros del cliente, que es generada por JPA, no se va a borrar el libro de la base de datos.
Tengo que ir a la lista del cliente y establecer a null el campo del libro "Cliente".
