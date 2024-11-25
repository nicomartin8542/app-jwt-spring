# 🌟 Aplicación Spring Boot con Autenticación JWT

Este proyecto es una aplicación desarrollada con Spring Boot que implementa autenticación básica mediante JWT. Incluye dos entidades principales: **User** y **Role**, permite configuraciones dinámicas a través de archivos de propiedades específicos por entorno y utiliza Docker Compose para inicializar la base de datos y herramientas administrativas.

---

## 📋 Características
- **Autenticación con JWT** para proteger endpoints.
- Configuración dinámica mediante tres archivos:
  - `application.properties` (general)
  - `application-dev.properties` (desarrollo)
  - `application-pro.properties` (producción)
- Base de datos MySQL configurada con **Docker Compose**.
- Interfaz de administración de base de datos con **phpMyAdmin**.
- Script SQL para roles predefinidos: `ROLE_ADMIN` y `ROLE_USER`.
- Uso de **variables de entorno** para configuraciones sensibles.

---

## 🛠️ Requisitos Previos
1. **Java 17** o superior instalado.  
2. **Maven** instalado.  
3. **Docker** y **Docker Compose** instalados.  
4. Configurar las siguientes variables de entorno:  

```bash
export SERVER_PORT=8080
export MYSQL_ROOT_PASSWORD=root123
export MYSQL_DATABASE=springboot_crud
export MYSQL_USER=spring_user
export MYSQL_PASSWORD=password123
```

## 🚀Configuración del Proyecto

### 1️⃣ Clonar el Repositorio
### 2️⃣ Configurar las Variables de Entorno
```bash
export SERVER_PORT=
export MYSQL_ROOT_PASSWORD=
export MYSQL_DATABASE=
export MYSQL_USER=
export MYSQL_PASSWORD=
```
### 3️⃣ Iniciar los Servicios con Docker Compose
Ejecuta el siguiente comando para inicializar MySQL y phpMyAdmin:
```bash
docker-compose up -d
```
Esto creará:
- Un contenedor para MySQL, expuesto en el puerto 3306.
- Un contenedor para phpMyAdmin, accesible en http://localhost:{SERVER_PORT}.

### 4️⃣ Crear las Bases de Datos
Luego de configurar las variables de entorno en el archivo docker-compose.yml, ingresa al phpMyAdmin en http://localhost:{SERVER_PORT} y crea las bases de datos con el nombre que se haya definido en el archivo docker-compose.yml.

### 5️⃣ Compilar y Ejecutar la Aplicación
Compila la aplicación con Maven:
```bash
mvn clean install
```
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

## 📝 Notas adicionales
- Modifica los archivos application-dev.properties o application-pro.properties según sea necesario para cada entorno.
- Asegúrate de que los contenedores de Docker estén en ejecución antes de iniciar la aplicación.

## 👨‍💻 Autor
Nicolás Martín \
📍 Residente en La Rioja, Argentina. \
💻 Programador Frontend/Backend, actualmente trabajando en el sector bancario.

