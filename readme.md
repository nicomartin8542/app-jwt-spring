# 🌟 Spring Boot CRUD con Autenticación JWT

Este proyecto es una API RESTful desarrollada en Spring Boot que implementa autenticación y autorización mediante JWT. Permite la gestión de usuarios y roles, y está preparada para entornos de desarrollo y producción. Utiliza MySQL como base de datos y Docker Compose para facilitar la configuración del entorno.

---

## 📋 Características principales
- **Autenticación y autorización con JWT**: protege endpoints y gestiona sesiones seguras.
- **Gestión de usuarios y roles**: CRUD completo sobre entidades `User` y `Role`.
- **Configuración flexible**: soporta archivos `application.properties`, `application-dev.properties` y `application-pro.properties`.
- **Variables de entorno**: para datos sensibles y configuración dinámica.
- **Base de datos MySQL**: inicializable con Docker Compose.
- **phpMyAdmin**: para administración visual de la base de datos.
- **Carga automática de roles**: los roles `ROLE_ADMIN` y `ROLE_USER` se insertan automáticamente.
- **Validaciones y manejo de errores**: incluye controladores globales de excepciones y validaciones personalizadas.

---

## 🚀 Dependencias principales
El proyecto utiliza las siguientes dependencias (ver `pom.xml`):
- `spring-boot-starter-web`: API REST.
- `spring-boot-starter-data-jpa`: Persistencia con JPA/Hibernate.
- `spring-boot-starter-security`: Seguridad y autenticación.
- `spring-boot-starter-validation`: Validaciones.
- `spring-boot-starter-mail`: Envío de emails (para confirmación y recuperación de contraseña).
- `mysql-connector-j`: Driver de MySQL.
- `jjwt`: Manejo de JWT.
- `modelmapper`: Mapeo de DTOs.
- `lombok`: Reducción de boilerplate (anotaciones).

---

## 🛠️ Requisitos Previos
1. **Java 17** o superior
2. **Maven**
3. **Docker** y **Docker Compose**

## ⚙️ Configuración de Variables de Entorno (`.env`)
El archivo `.env` centraliza toda la configuración sensible y dinámica del proyecto. **Todas las variables aquí listadas impactan directamente en el comportamiento de la aplicación.**

### Tabla resumen de variables de entorno
| Variable                | Descripción                                                                                  | Ejemplo / Valor por defecto              |
|-------------------------|---------------------------------------------------------------------------------------------|------------------------------------------|
| DB_URL                  | Cadena JDBC para conexión a MySQL                                                           | jdbc:mysql://localhost:3306/db_task      |
| DB_USERNAME             | Usuario de la base de datos                                                                 | root                                     |
| DB_PASSWORD             | Contraseña de la base de datos                                                              | root                                     |
| DB_DRIVER               | Driver JDBC utilizado                                                                       | com.mysql.cj.jdbc.Driver                 |
| DB_DIALECT              | Dialecto Hibernate                                                                          | org.hibernate.dialect.MySQL8Dialect      |
| DB_DDL_AUTO             | Estrategia de actualización de esquema (update/create/drop)                                 | update                                   |
| DB_SHOW_SQL             | Mostrar las consultas SQL en consola                                                        | true                                     |
| DB_PORT                 | Puerto expuesto de MySQL (debe coincidir con docker-compose y DB_URL)                       | 3306                                     |
| MAIL_HOST               | Servidor SMTP para envío de correos                                                         | smtp.gmail.com                           |
| MAIL_PORT               | Puerto SMTP                                                                                 | 587                                      |
| MAIL_USERNAME           | Usuario/correo SMTP                                                                         | tu_correo@gmail.com                      |
| MAIL_PASSWORD           | Contraseña o clave de aplicación SMTP                                                       | tu_clave_app                             |
| MAIL_SMTP_AUTH          | Habilita autenticación SMTP                                                                 | true                                     |
| MAIL_SMTP_STARTTLS      | Habilita TLS/SSL para SMTP                                                                  | true                                     |
| MAIL_SMTP_CONNECTIONTIMEOUT | Timeout de conexión SMTP (ms)                                                           | 5000                                     |
| MAIL_SMTP_TIMEOUT       | Timeout general SMTP (ms)                                                                   | 5000                                     |
| MAIL_SMTP_WRITETIMEOUT  | Timeout de escritura SMTP (ms)                                                              | 5000                                     |
| FRONT_URL               | URL del frontend (usada en emails de confirmación, links, etc)                              | http://localhost:5173                    |
| SPRING_PROFILES_ACTIVE  | Entorno activo de Spring Boot (`dev`, `pro`, etc)                                           | dev                                      |
| SERVER_PORT             | Puerto donde corre la API Spring Boot                                                       | 3000                                     |

### Ejemplo de archivo `.env`
```env
# --- Base de datos ---
DB_URL=jdbc:mysql://localhost:3306/db_task
DB_USERNAME=root
DB_PASSWORD=root
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_DIALECT=org.hibernate.dialect.MySQL8Dialect
DB_DDL_AUTO=update
DB_SHOW_SQL=true
DB_PORT=3306

# --- Configuración de correo ---
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_clave_app
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS=true
MAIL_SMTP_CONNECTIONTIMEOUT=5000
MAIL_SMTP_TIMEOUT=5000
MAIL_SMTP_WRITETIMEOUT=5000

# --- Configuración de la aplicación ---
FRONT_URL=http://localhost:5173
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=3000
```

**Notas importantes:**
- Cambia los valores de las variables según tu entorno y necesidades.
- `DB_PORT` debe coincidir en `.env`, en `docker-compose.yml` y en la URL de conexión.
- `SPRING_PROFILES_ACTIVE` determina qué archivo de configuración de Spring se usará (`dev`, `pro`, etc). Puedes cambiar el entorno fácilmente modificando solo esta variable.
- `FRONT_URL` es fundamental para los enlaces enviados por correo (confirmaciones, recuperaciones, etc).
- Los timeouts de correo ayudan a evitar bloqueos si hay problemas de red.
- Nunca subas tu archivo `.env` a un repositorio público.

---

## 🚀 Configuración del Proyecto

### 1️⃣ Clonar el Repositorio

### 2️⃣ Configurar las Variables de Entorno
- Crea o edita el archivo `.env` en la raíz del proyecto con la estructura mostrada arriba.
- Alternativamente, puedes exportar las variables manualmente en tu terminal antes de ejecutar la app.

### 3️⃣ Iniciar los Servicios con Docker Compose
Ejecuta:
```bash
docker-compose up -d
```
Esto levantará:
- Un contenedor MySQL en el puerto definido por `DB_PORT`.
- Un contenedor phpMyAdmin para administración visual.

### 4️⃣ Crear la Base de Datos (si no existe)
Accede a phpMyAdmin en `http://localhost:{DB_PORT}` y crea la base de datos con el nombre definido en `DB_DATABASE` o `MYSQL_DATABASE`.

### 5️⃣ Compilar y Ejecutar la Aplicación
Compila con:
```bash
mvn clean install
```
Y ejecuta:
```bash
java -jar target/{NOMBRE_DEL_JAR}.jar
```
O desde tu IDE favorito.

---
Ejecuta la aplicación con Java:
```bash
java -jar target/{NOMBRE_DEL_JAR}.jar
```
`Tambien puedes levantar la aplicacion desde el ide que estes usando, por ejemplo, Visual Studio Code.`
### 5️⃣ Cargar los Roles Predefinidos
Al iniciar la aplicación, los roles ROLE_ADMIN y ROLE_USER se insertarán automáticamente en la base de datos desde el archivo import.sql.

### 🌐 Acceso a phpMyAdmin
- URL: http://localhost:8080
- Credenciales: Configuradas en las variables de entorno (MYSQL_ROOT_PASSWORD).

## 🌍 Entornos y archivos de configuración
Spring Boot permite separar la configuración según el entorno (desarrollo, producción, testing, etc.) usando perfiles. En este proyecto:

- **`application.properties`**: configuración general (común a todos los entornos).
- **`application-dev.properties`**: configuración específica para desarrollo.
- **`application-pro.properties`**: configuración específica para producción.

La variable de entorno `SPRING_PROFILES_ACTIVE` (o la propiedad en el `.env`) determina qué archivo se usará. Por ejemplo:
- Para desarrollo: `SPRING_PROFILES_ACTIVE=dev`
- Para producción: `SPRING_PROFILES_ACTIVE=pro`

Puedes cambiar de entorno simplemente modificando el valor de esta variable, sin tocar el código fuente. Así puedes tener distintas bases de datos, credenciales, o configuraciones según el entorno.

> **Recomendación:** Mantén tus credenciales sensibles y configuraciones personalizadas fuera del repositorio, usando `.env` o variables de entorno en tu sistema.

---

## 👨‍💻 Autor
Nicolás Martín \
📍 Residente en La Rioja, Argentina. \
💻 Programador Frontend/Backend, actualmente trabajando en el sector bancario.

