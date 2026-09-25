# Sistema de Gestión de Eventos

Un proyecto en Java desarrollado aplicando el **patrón arquitectónico MVC (Modelo-Vista-Controlador)** y los principios de diseño **SOLID**. Esta aplicación de escritorio permite crear y consultar eventos de forma organizada y estructurada.

## 🏗️ Arquitectura del Proyecto

El proyecto está estructurado en tres capas principales (MVC):

*   **Modelo (`model`)**: Contiene la lógica de negocio, las entidades y el acceso a los datos.
    *   `Evento`: Clase de dominio (POJO) que representa los datos de un evento.
    *   `EventoService`: Capa de servicios que maneja las reglas de negocio, conversiones de fechas y validaciones de la información.
    *   `IEventoRepository` y `EventoRepository`: Contrato e implementación de la capa de persistencia simulada (almacenamiento en memoria).
*   **Vista (`view`)**: Contiene la interfaz gráfica de usuario.
    *   `FrmEvento`: Formulario Swing construido para ingresar y mostrar datos. Se comunica de forma segura sin exponer sus componentes internos de diseño.
*   **Controlador (`controller`)**: Actúa como intermediario entre la Vista y el Modelo.
    *   `EventoController`: Escucha las interacciones del usuario en la vista (clics en botones) y delega la ejecución de las operaciones al `EventoService`, para luego indicar a la Vista qué resultados debe mostrar.
*   **Main (`main`)**: Punto de entrada de la aplicación.
    *   `SistemaGestionEventos`: Se encarga de inicializar e inyectar las dependencias de todas las capas para poner en marcha el sistema.

## 🚀 Principios SOLID Aplicados

Durante el desarrollo y refactorización, el proyecto fue estructurado en torno a los siguientes principios:

1.  **S - Principio de Responsabilidad Única (SRP):** Cada clase tiene una única tarea. El controlador no valida datos, la vista no procesa lógica, y los datos se validan en el `EventoService`.
2.  **O - Principio de Abierto/Cerrado (OCP):** Los manejadores de eventos (ActionListeners) se implementan usando expresiones lambda vinculadas directamente a sus métodos. Si se agregan nuevas funciones, no se requiere modificar un condicional central `if-else` o un bloque gigantesco.
3.  **I - Segregación de Interfaces (ISP) y D - Inversión de Dependencias (DIP):** El controlador y los servicios no dependen de clases concretas para interactuar con los datos. En su lugar, el proyecto cuenta con `IEventoRepository`. Las instancias son proporcionadas (inyectadas) mediante constructores en el punto de entrada de la aplicación, lo que fomenta el desacoplamiento y facilita la escalabilidad y las pruebas.

## 🛠️ Requisitos Previos

*   **Java Development Kit (JDK):** Versión 17 o superior.
*   **Maven:** Herramienta de gestión de proyectos y construcción.
*   Un IDE compatible como Apache NetBeans (en el que fue estructurado inicialmente), IntelliJ IDEA, o Eclipse.

## 💻 Compilación y Ejecución

Para compilar el proyecto usando Maven desde la terminal:

```bash
mvn clean compile
```

Para ejecutar el programa (si estás usando un entorno de terminal, o desde tu IDE simplemente ejecutando la clase principal):

```bash
mvn exec:java -Dexec.mainClass="main.SistemaGestionEventos"
```

## 📝 Uso del Programa

1.  **Crear un Evento**: Completa todos los campos del formulario asegurándote de proporcionar datos válidos (ID numérico, seleccionar un tipo, llenar todos los textos) y haz clic en "Crear Evento".
2.  **Consultar un Evento**: Ingresa el ID del evento previamente creado en el campo correspondiente y haz clic en "Consultar Evento". Obtendrás un resumen en una ventana emergente de todo lo ingresado.
