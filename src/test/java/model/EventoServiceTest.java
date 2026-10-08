package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class EventoServiceTest {

    private EventoService eventoService;
    private IEventoRepository fakeRepository;

    @BeforeEach
    public void setUp() {
        // Creamos un "Mock" o repositorio falso (Stub) para probar la lógica sin tocar la base de datos real
        fakeRepository = new IEventoRepository() {
            private Evento guardado = null;

            @Override
            public void guardar(Evento evento) {
                guardado = evento;
            }

            @Override
            public Evento buscarPorId(int id) {
                if (id == 999) { // Simulamos que el ID 999 ya existe
                    return new Evento(999, "Evento Falso", "Desc", "Académico", null, null, "Lugar");
                }
                return guardado != null && guardado.getIdEvento() == id ? guardado : null;
            }

            @Override
            public void actualizar(Evento evento) { }
            
            @Override
            public void eliminar(int id) { }
            
            @Override
            public List<Evento> obtenerTodos() { return null; }
            
            @Override
            public int obtenerSiguienteId() { return 1; }
        };

        eventoService = new EventoService(fakeRepository);
    }

    @Test
    public void crearEvento_fallaSiNombreEsVacio() {
        // Arrange
        String id = "1";
        String nombre = ""; // Nombre vacío!
        String desc = "Descripción";
        String tipo = "Académico";
        String lugar = "Auditorio";
        Date fecha = new Date();
        Date hora = new Date();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventoService.crearEvento(id, nombre, desc, tipo, lugar, fecha, hora);
        });

        assertEquals("El nombre es obligatorio.", exception.getMessage());
    }

    @Test
    public void crearEvento_fallaSiTipoNoEsValido() {
        // Arrange
        String id = "2";
        String nombre = "Evento de Prueba";
        String desc = "Descripción";
        String tipo = "Seleccione un tipo..."; // Tipo inválido
        String lugar = "Auditorio";
        Date fecha = new Date();
        Date hora = new Date();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventoService.crearEvento(id, nombre, desc, tipo, lugar, fecha, hora);
        });

        assertTrue(exception.getMessage().contains("Has olvidado indicarnos qué tipo de evento es"));
    }

    @Test
    public void crearEvento_fallaSiIdYaExiste() {
        // Arrange
        String id = "999"; // El ID 999 está quemado en nuestro FakeRepository como "ya existente"
        String nombre = "Nuevo Evento";
        String desc = "Descripción";
        String tipo = "Social";
        String lugar = "Patio";
        Date fecha = new Date();
        Date hora = new Date();

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventoService.crearEvento(id, nombre, desc, tipo, lugar, fecha, hora);
        });

        assertEquals("Ya existe un evento registrado con el ID: 999", exception.getMessage());
    }

    @Test
    public void crearEvento_exitosoSiDatosCorrectos() {
        // Arrange
        String id = "3";
        String nombre = "Conferencia Anual";
        String desc = "Descripción de prueba";
        String tipo = "Empresarial";
        String lugar = "Sala de Juntas";
        Date fecha = new Date();
        Date hora = new Date();

        // Act
        // No debe lanzar ninguna excepción
        assertDoesNotThrow(() -> {
            eventoService.crearEvento(id, nombre, desc, tipo, lugar, fecha, hora);
        });
        
        // Assert: Verificamos que se guardó en nuestro repositorio falso
        assertNotNull(fakeRepository.buscarPorId(3));
        assertEquals("Conferencia Anual", fakeRepository.buscarPorId(3).getNombre());
    }
}
