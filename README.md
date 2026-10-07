# 📅 Sistema de Gestión de Eventos

Un sistema de escritorio robusto y moderno desarrollado en **Java Swing**, diseñado para cubrir el ciclo de vida completo de la administración de eventos. Este proyecto permite gestionar desde la planificación de fechas y recursos hasta el registro de asistentes, todo bajo una arquitectura sólida **MVC (Modelo-Vista-Controlador)** y persistencia de datos mediante **SQLite**.

---

## 🚀 Características Principales

*   **Persistencia Local Autónoma:** Base de datos **SQLite** embebida (`gestion_eventos.db`), eliminando la necesidad de servidores externos como XAMPP o MySQL.
*   **Interfaz Gráfica Moderna:** UI estilizada con **FlatLaf (Dark Theme)**, bordes redondeados y tipografía moderna que rompe con la estética tradicional de Java Swing.
*   **Arquitectura MVC Estricta:** Separación absoluta entre la lógica de negocio, el acceso a datos y la interfaz de usuario, garantizando un código mantenible y escalable.
*   **Control de Accesos (Roles):** Sistema de login con permisos jerárquicos (Administrador, Operador, Invitado).
*   **Navegación Fluida:** Un Dashboard principal (Single Page Application - SPA style) que cambia de módulos sin abrir múltiples ventanas flotantes.

---

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Java (JDK 17+)
*   **Gestor de Dependencias:** Maven
*   **Interfaz de Usuario:** Java Swing
*   **Base de Datos:** SQLite (v3.45.2.0)
*   **Look and Feel (Temas):** FlatLaf (v3.4.1)
*   **Componentes Extra:** LGoodDatePicker (v11.2.1) para calendarios y selectores de hora interactivos.

---

## 🧱 Arquitectura del Sistema (MVC)

El proyecto está rigurosamente dividido en tres capas principales:

1.  **Modelo (`model/`):** 
    *   **Clases de Entidad:** `Evento`, `Usuario`, `Participante`, `Actividad`, `Recurso`, `Notificacion`.
    *   **Repositorios:** Encargados de ejecutar el SQL (Ej: `EventoRepository`). Implementan patrones Singleton e interfaces para la inyección de dependencias.
    *   **Servicios:** Lógica de negocio y validaciones (Ej: `EventoService`). Aquí se validan los campos antes de tocar la base de datos.
2.  **Vista (`view/`):** 
    *   Interfaces gráficas puras diseñadas con componentes nativos y FlatLaf. No contienen lógica de negocio, solo componentes (`JButtons`, `JTextFields`, etc.) y sus *getters/setters*.
3.  **Controlador (`controller/`):** 
    *   Actúan como el puente de comunicación. Escuchan los clics de la vista, recolectan los datos, llaman al servicio del modelo, y luego actualizan la vista con los resultados o mensajes de éxito/error.
4.  **Configuración (`config/`):** 
    *   `DatabaseConnection.java`: Clase Singleton encargada de la conexión JDBC a SQLite y de la auto-generación de tablas (DDL) al inicio.

---

## 🧩 Módulos del Sistema

El sistema está dividido en **8 submódulos funcionales**, accesibles desde la barra lateral del Dashboard:

### 1. 🏠 Inicio (Dashboard Principal)
*   Muestra un resumen rápido de bienvenida.
*   Se adapta al usuario logueado (muestra el rol y nombre de usuario).

### 2. 📅 Gestión de Eventos
*   **Crear Evento:** Formulario validado para registrar un evento (Nombre, Descripción, Tipo, Lugar, Fecha y Hora). Todo evento nace con el estado **"Programado"**.
*   **Consultar / Modificar Evento:** Permite buscar un evento por ID. Aquí el Administrador puede cambiar el **Estado** del evento (Ej: de "Programado" a "En Curso" o "Finalizado"), actualizar sus datos o **Eliminarlo**.
*   **Listar Eventos:** Una tabla general para ver todos los eventos registrados en el sistema.

### 3. 🗓️ Calendario
*   Una cuadrícula visual e interactiva que muestra el mes actual.
*   Inyecta los eventos directamente en sus días correspondientes con indicadores de colores según su estado (Verde = Programado, Azul = Finalizado, etc.).
*   Panel lateral que resalta el "Próximo Evento" a realizarse.

### 4. 👥 Participantes
*   Módulo de registro de asistencia.
*   Permite registrar Personas (Nombre y Correo) y vincularlas directamente a un Evento existente.

### 5. 📦 Recursos y Actividades
El corazón logístico del evento:
*   **Actividades:** Permite armar el cronograma interno del evento (Ej: "10:00 AM - Discurso Inicial - A cargo de: Juan").
*   **Recursos:** Gestión de inventario físico requerido para el evento (Ej: "Sillas x 50", "Proyector x 2").

### 6. 📊 Informes (Dashboard Estadístico)
*   Un panel analítico de solo lectura.
*   Muestra KPI's (Tarjetas de indicadores) calculando la cantidad de eventos totales, finalizados y en proceso.
*   Gráficas de barras horizontales (usando `JProgressBar`) que muestran porcentajes de eventos por "Tipo" y por "Estado".

### 7. 🔔 Notificaciones
*   Buzón del sistema. Registra automáticamente acciones importantes (Ej: "Nuevo Evento creado").
*   Las notificaciones no leídas se acumulan y pueden marcarse como leídas.

### 8. ⚙️ Configuración
*   Panel de ajustes del sistema y perfil del usuario actual (en desarrollo / personalización visual).

---

## 🔐 Roles y Permisos (Seguridad)

El sistema soporta múltiples tipos de acceso:

1.  **Administrador (admin):** Acceso total. Puede crear, modificar, cambiar estados, eliminar eventos y ver reportes estadísticos.
2.  **Operador (operador):** Acceso operativo. Puede gestionar la logística (Participantes, Recursos, Actividades) y registrar eventos, pero con restricciones en reportes y configuraciones avanzadas.
3.  **Invitado (Guest):** Puede navegar por las vistas para conocer el sistema, consultar eventos públicos en el calendario, pero **no puede alterar la base de datos**. Al intentar guardar o borrar, el controlador bloquea la acción solicitando inicio de sesión.

*(Nota: Las credenciales por defecto quemadas en la inicialización son `admin@admin.com / admin123` y `operador@gmail.com / operador123`)*.

---

## 🗄️ Esquema de Base de Datos (SQLite)

La persistencia se maneja mediante 6 tablas relacionales (creadas automáticamente si no existen):

1.  **`eventos`**: `id` (PK), `nombre`, `descripcion`, `tipo`, `lugar`, `fecha`, `hora`, `estado`.
2.  **`usuarios`**: `correo` (PK), `nombre`, `contrasena`, `rol`.
3.  **`participantes`**: `id` (PK, Auto), `nombre`, `correo`, `evento_asignado`.
4.  **`actividades`**: `id` (PK, Auto), `nombre`, `horario`, `responsable`, `evento_asignado`.
5.  **`recursos`**: `id` (PK, Auto), `tipo`, `cantidad`, `evento_asignado`.
6.  **`notificaciones`**: `id` (PK, Auto), `titulo`, `mensaje`, `fechaHora`, `leida`.

---

## ⚙️ Guía de Ejecución

Debido a que el proyecto utiliza Maven, las dependencias de SQLite y FlatLaf deben resolverse antes de compilar.

1.  **Clonar / Abrir** el proyecto en Apache NetBeans (u otro IDE compatible con Maven).
2.  Hacer **Clic Derecho** en la raíz del proyecto (`SistemaGestionEventos`) y seleccionar **Clean and Build** (Limpiar y Construir).
    *   *Paso crítico para descargar el `.jar` de SQLite.*
3.  Hacer clic en **Run** (Ejecutar proyecto).
4.  El sistema inicializará el archivo `gestion_eventos.db` en la carpeta raíz y mostrará la pantalla de Login.
5.  Para ingresar con privilegios completos, usa:
    *   **Usuario:** `admin@admin.com`
    *   **Contraseña:** `admin123`

---
*Documentación generada para el Incremento N° 7 del Sistema de Gestión de Eventos.*
