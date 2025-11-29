# Mil Sabores Backend

Backend Spring Boot para Mil Sabores, alineado con el frontend Vite/React (`version_web/pasteleriamilsabores-react`).

## Requisitos
- Java 21+
- Maven 3.9+
- XAMPP MySQL corriendo en `localhost:3306`
- Node 20+ (solo para el frontend)

## Configuración inicial
1. **Base de datos**
   ```sql
   CREATE DATABASE IF NOT EXISTS milsabores CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. **Variables de entorno recomendadas** (Windows PowerShell)
   ```powershell
   $env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/milsabores?useSSL=false&serverTimezone=UTC"
   $env:SPRING_DATASOURCE_USERNAME="root"
   $env:SPRING_DATASOURCE_PASSWORD=""
   $env:APP_SECURITY_JWT_SECRET="<clave-base64>"
   ```
   Ajusta credenciales también en `src/main/resources/application.properties` si prefieres valores por defecto.
3. Opcional: crea archivos `schema.sql` y `data.sql` si necesitas migraciones manuales.

### application.properties
- Datasource: `jdbc:mysql://localhost:3306/milsabores`
- CORS: `app.cors.allowed-origins=http://localhost:5173,http://localhost:4173`
- JWT: `app.security.jwt.secret`, `app.security.jwt.expiration-ms`, `app.security.jwt.refresh-expiration-ms`

## Ejecutar backend
```bash
mvn spring-boot:run
```
La API quedará disponible en `http://localhost:8080/api` y Swagger UI en `http://localhost:8080/swagger-ui`.

### Build empaquetado
```bash
mvn -q -DskipTests package
```
Genera `target/back-pasteleria-0.0.1-SNAPSHOT.jar` listo para desplegar (añade `-Pprod` si defines perfiles).

## Ejecutar frontend
```bash
cd version_web/pasteleriamilsabores-react
npm install
npm run dev
```
Por defecto se sirve en `http://localhost:5173`. Configura `VITE_API_BASE_URL` en `.env.local` si el backend vive en otra URL.

## Verificación manual sugerida
1. **Levanta MySQL + backend**: `mvn spring-boot:run`.
2. **Explora Swagger** (`/swagger-ui`) y prueba `/api/auth/login`, `/api/auth/register`, `/api/products`.
3. **Carga datos** con los scripts SQL entregados (productos, etc.).
4. **Frontend**: `npm run dev` → valida login, catálogo, checkout real.
5. **Persistencia**: revisa tablas `users`, `orders`, `order_items` para confirmar escritura.

## Swagger / Postman
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/api/docs`
- Exporta la especificación (`Download -> JSON`) para generarte una colección Postman.
- Recuerda incluir `Authorization: Bearer <token>` en requests protegidas.

## Despliegue local/QA
1. Ajusta `.env` o variables del sistema con las credenciales de MySQL/JWT.
2. Empaqueta: `mvn -q -DskipTests package`.
3. Ejecuta el JAR:
   ```bash
   java -jar target/back-pasteleria-0.0.1-SNAPSHOT.jar
   ```
4. Configura el frontend con la URL expuesta (`VITE_API_BASE_URL`).
5. Corre migraciones/scripts necesarios (por ejemplo, inserciones de productos) antes de abrir el sitio a usuarios.

## Variables por ambiente
| Variable | Descripción | Dev | QA/Prod |
| --- | --- | --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC MySQL | `jdbc:mysql://localhost:3306/milsabores` | URL del clúster gestionado |
| `SPRING_DATASOURCE_USERNAME` | Usuario DB | `root` | credencial provista |
| `SPRING_DATASOURCE_PASSWORD` | Password DB | vacío | secreto seguro |
| `APP_SECURITY_JWT_SECRET` | Clave JWT Base64 | valor demo | clave única por entorno |
| `APP_CORS_ALLOWED_ORIGINS` | CORS list | `http://localhost:5173,http://localhost:4173` | dominios frontend |
| `VITE_API_BASE_URL` | URL backend para front | `http://localhost:8080/api` | `https://api.<dominio>/api` |

## Estructura propuesta
```
src/main/java/cl/milsabores/pasteleria
 ├── BackPasteleriaApplication.java
 ├── config/
 ├── security/
 ├── entity/
 ├── dto/
 ├── repository/
 ├── service/
 └── controller/
```

Avance actual:
- Proyecto Spring Boot con dependencias Web/Security/JPA/JWT/OpenAPI.
- Entidades base (User, Product, Order, OrderItem, Branch) y repositorios.
- Configuración de seguridad/CORS/JWT y propiedades de ejemplo.

## Próximos pasos inmediatos
- Validar referencias estáticas (branches, payment methods, slots) contra el frontend o moverlas a BD/config antes de producción.
- Ajustar DiscountService para devolver mensajes alineados al front y añadir pruebas unitarias (pendiente, pospuesto por ahora).
- Documentar endpoints en Swagger con ejemplos y describir headers JWT.
- Preparar instrucciones detalladas para levantar XAMPP + migraciones SQL.
