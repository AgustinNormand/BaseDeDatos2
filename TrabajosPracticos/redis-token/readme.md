##El proyecto contiene un conjunto de interfaces que deben ser implementadas, tambien se ofrece un vista cuya utilizacion es opcional.

# Para exporta para los alumnos
1 - Eliminar las clases: repoository.impl.UsuarioDaoImpl, repository.utils.UserKeyBuilder, repository.utils.UserValidator

2 - Vaciar metodo **preparar** en la clase Menu.

3 - Comprimir carpeta entera.

# Para ejecutar
1 - Modificar datos de conexion a Redis en la clase ConnectionManager.

2 - Ejecutar el comando de maven "package".

3 - Abrir linea de comandos en la raiz del proyecto y ejecutar: 
java -jar target/redis-token-1.0-SNAPSHOT-jar-with-dependencies.jar 

4 - De poder conectarse a Redis e instanciar todas las clases mostrara el menu.