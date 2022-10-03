# API Rest  - Prueba técnica (MVC pattern)

** PDF con información detallada de cada parte del proyecto ** 



**He usado:**  
> Dependencies:
* Spring Web
* Spring JPA
* Spring Validation
* H2-DB
* Lombok - https://projectlombok.org/
* ModelMapper - http://modelmapper.org/
* Gson - https://github.com/google/gson/blob/master/UserGuide.md
* ModelMapper - http://modelmapper.org/
* Swagger - https://swagger.io/


Instrucciones
-------------

- El repositorio incluye en el directorio /target un jar con la aplicación empaquetada
    - Desde una consola de comandos:  java -jar target/jfc-0.0.1-SNAPSHOT.jar
    - Y estará disponible en la siguiente URL: http://localhost:8080
    - El gestor de BD embebido: http://localhost:8080/h2-console. (usuario=ps | pass en blanco)
    - La interfaz de Swagger: http://localhost:8080/swagger-ui/

- La aplicación contiene datos de ejemplo que se inicializan en memoria cada vez que se arranca. (DataLoader.class)
    
- En resumen, el funcionamiento general lo he planteado siguiendo XXXX premisas:
    - Que lo que se reciba en el Endpoint sea siempre un JSON con un mismo "formato": status + mensaje
	- Para pasar los Strings a Json he usado Gson antes de que ResponseEntity los devuelva.
    - Las Excepciones también son formateadas y contraladas desde una clase centralizada.
    - He usado ModelMapper en lugar de MapStrut porque ya estaba familiarizado con él.
	- Se incluye Dockerfile

