# Microservicio Backend Main - Gestión de Perfiles

## Descripción
Este microservicio se encarga de la administración central de los perfiles (Waifus y Husbandos) dentro de la aplicación Filler (Tinder Otaku) Maneja la creación, lectura y estructuración de los datos principales del negocio y se comunica mediante una API REST

## Tecnologías Utilizadas
* Java con Spring Boot
* Persistencia: Spring Data JPA con Base de Datos H2 en memoria

## Requisitos e Instalación
* Clonar el repositorio
* Configurar las propiedades del entorno en el archivo `src/main/resources/application.properties` (SOLO SI ES NECE)
* Ejecutar con el [botón de Ejecutar](https://i.imgur.com/pWEN7D2.png) o con el comando para ejecutar de manera local: `.\mvnw.cmd spring-boot:run`.

## Algunos Endpoints de la API (Endpoints REST) son:
* `GET /api/perfiles` - Obtiene el listado de perfiles disponibles (WaifuHusbando)
* `POST /api/perfiles/reaccion` - El frontend envia aquí la decisión (LIKE o DISLIKE)

## Ejecución de Pruebas Unitarias
* Comando para correr los tests: `.\mvnw.cmd test`.
