# Fondos BTG - Aplicación de Gestión de Fondos de Inversión

Este proyecto es una aplicación backend desarrollada en Spring Boot que permite gestionar fondos de inversión, donde los clientes pueden suscribirse a diferentes fondos, cancelar suscripciones y ver su historial de transacciones. La aplicación también cuenta con notificaciones automáticas mediante email y SMS a través de Amazon SNS.

## Características

- Gestión de clientes y fondos de inversión.
- Suscripción y cancelación de fondos.
- Notificaciones automáticas al cliente mediante correo electrónico y SMS al realizar suscripciones.
- Historial de transacciones detallado para cada cliente.
- Integración con MongoDB para almacenamiento de datos.
- Despliegue de microservicios en AWS (opcional).

## Tecnologías

- Java 17
- Spring Boot 3.x
- MongoDB
- Amazon SNS
- Lombok
- JUnit y Mockito para pruebas unitarias
- Docker y Docker Compose

## Requisitos

Antes de empezar, asegúrate de tener instalado:

- Java 17
- Maven 3.6+
- MongoDB (local o en la nube)
- Docker (opcional, para contenedores)
- Credenciales de AWS configuradas en variables de entorno para el envío de notificaciones SMS

## Instalación y Configuración

1. **Clona el repositorio**:

   ```bash
   git clone https://github.com/Guillermo1994/fondosBTG
   cd fondosBTG
   ```
2. **Instala las dependencias:**
    ```bash
   mvn install
   ```
3. **Configura MongoDB: Asegúrate de tener un contenedor o instancia de MongoDB corriendo. Configura la conexión en 
application.properties o mediante un archivo .env con variables de entorno.**
4. **Configura las credenciales de AWS: Establece las variables de entorno para las credenciales de AWS que permiten el
uso de Amazon SNS**
    ```bash
    export AWS_ACCESS_KEY_ID=tu_access_key
    export AWS_SECRET_ACCESS_KEY=tu_secret_key
   ```
5. **Ejecuta la aplicación:**
```bash
    mvn spring-boot:run
   ```
## Endpoints de la API
1. **Cliente**
- GET /clientes/{id}: Obtener los detalles de un cliente.
- POST /clientes: Crear un nuevo cliente.
2. **Fondo**
- GET /fondos: Obtener todos los fondos disponibles.
- POST /fondos: Crear un nuevo fondo.
3. **Transacciones**
- POST /transacciones/apertura/{clienteId}/{fondoId}: Abrir un fondo para un cliente específico.
- POST /transacciones/cancelacion/{clienteId}/{fondoId}/{transaccionId}: Cancelar una transacción existente.
- GET /transacciones/historial/{clienteId}: Ver el historial de transacciones de un cliente.

## Calidad de Código
La calidad del código es fundamental en este proyecto. Se implementaron las siguientes herramientas:

- **CheckStyle**: Se utiliza para garantizar que el código siga las convenciones de estilo definidas.
- **SpotBugs**: Identifica errores comunes en el código que podrían llevar a problemas en tiempo de ejecución.
- **PMD**: Ayuda a encontrar problemas de mantenimiento y calidad, como código duplicado o métodos excesivamente largos.
- **JaCoCo**: Se utiliza para medir la cobertura de pruebas, asegurando que el código crítico esté bien probado.

## Pruebas
La aplicación incluye pruebas unitarias para los servicios principales. Puedes ejecutar las pruebas usando:
```bash
    mvn test
  ```
## Despliegue
Esta aplicación está preparada para ser desplegada en AWS utilizando Docker y Amazon SNS para notificaciones. Puedes desplegar la aplicación usando AWS CloudFormation y configurar servicios EC2, S3, y SNS según sea necesario.