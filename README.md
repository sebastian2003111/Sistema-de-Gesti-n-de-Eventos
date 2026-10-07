# Sistema de Gestión de Eventos (EVNTS Pro)

Un proyecto en Java desarrollado aplicando el **patrón arquitectónico MVC (Modelo-Vista-Controlador)** y los principios de diseño **SOLID**. Esta aplicación de escritorio permite gestionar el ciclo de vida completo de eventos de forma organizada y estructurada (CRUD completo).

## 🏗️ Arquitectura del Proyecto (MVC)

El proyecto está estrictamente estructurado en tres capas principales para mantener un bajo acoplamiento:

*   **Modelo (`model`)**: Contiene la lógica de negocio, las entidades y el acceso a los datos.
    *   `Evento`: Clase de dominio (POJO) que representa los datos de un evento (ID, nombre, fecha, etc.).
    *   `EventoService`: Capa de servicios que maneja las reglas de negocio, conversiones de fechas y validaciones de la información para todas las operaciones (Crear, Leer, Actualizar, Eliminar).
    *   `IEventoRepository` y `EventoRepository`: Contrato e implementación de la capa de persistencia simulada (almacenamiento en una lista en memoria `ArrayList`).
*   **Vista (`view`)**: Contiene la interfaz gráfica de usuario construida con **Java Swing**.
    *   `FrmEvento`: Formulario principal para registrar nuevos eventos.
    *   `FrmConsultarEvento`: Formulario para buscar eventos por ID, visualizar sus detalles, editarlos y eliminarlos.
    *   `FrmListarEventos`: Ventana con una tabla (`JTable`) que despliega dinámicamente todos los eventos registrados en el sistema.
*   **Controlador (`controller`)**: Orquesta la comunicación entre la Vista y el Modelo.
    *   `EventoController`: Gestiona las interacciones de la ventana principal (Crear y abrir Listado).
    *   `ConsultaEventoController`: Gestiona las operaciones de Buscar, Actualizar y Eliminar eventos.
    *   `ListarEventosController`: Se encarga de extraer la lista completa del Repositorio e inyectarla en la tabla visual.
*   **Main (`main`)**: Punto de entrada de la aplicación.
    *   `SistemaGestionEventos`: Funciona como contenedor IoC manual, inyectando las dependencias (Repositorio -> Servicio -> Controladores).

## 🚀 Principios SOLID Aplicados

1.  **S - Principio de Responsabilidad Única (SRP):** Cada clase tiene una única tarea. El controlador no valida datos, la vista no procesa lógica, y los datos se validan en el `EventoService`.
2.  **O - Principio de Abierto/Cerrado (OCP):** Los manejadores de eventos se implementan usando expresiones lambda vinculadas a sus métodos (ej. `e -> actualizarEvento()`).
3.  **D - Inversión de Dependencias (DIP):** El servicio interactúa con los datos a través de la interfaz `IEventoRepository`, facilitando en el futuro el cambio de una base de datos en memoria a una en SQL sin tocar la lógica de negocio.

## 🛠️ Requisitos Previos

*   **Java Development Kit (JDK):** Versión 17 o superior.
*   **Maven:** Herramienta de gestión de proyectos y construcción.
*   Un IDE compatible como Apache NetBeans, IntelliJ IDEA o Eclipse.

## 💻 Compilación y Ejecución (Maven)

Desde la terminal:
```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar el proyecto
mvn exec:java -Dexec.mainClass="main.SistemaGestionEventos"
```

## 📝 Uso del Programa (Ciclo CRUD Completo)

1.  **Crear (Create)**: En la pantalla inicial, llena todos los campos y haz clic en "Crear Evento". El sistema valida formatos y que el ID sea único.
2.  **Listar todos (Read All)**: Haz clic en "Ver Todos los Eventos" para desplegar una tabla en tiempo real con todos los registros activos.
3.  **Consultar (Read por ID)**: Ve a "Consultar Evento", ingresa el ID y presiona "Buscar Evento". Los datos llenarán el formulario.
4.  **Actualizar (Update)**: Una vez buscado un evento, modifica cualquier texto (ej. cambiar el nombre o el lugar) y haz clic en "Actualizar Evento" para sobrescribir los datos en memoria.
5.  **Eliminar (Delete)**: Al buscar un evento, presiona "Eliminar Evento". Confirmas la advertencia de seguridad y el registro será borrado permanentemente del repositorio.
