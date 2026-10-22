package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private IUsuarioRepository fakeRepo;

    @BeforeEach
    public void setUp() {
        fakeRepo = new IUsuarioRepository() {
            private String passGuardada = "admin123";

            @Override
            public Usuario autenticar(String username, String password) {
                if ("admin@admin.com".equals(username) && passGuardada.equals(password)) {
                    return new Usuario("admin", "admin@admin.com", passGuardada, "Administrador");
                }
                return null;
            }

            @Override
            public void registrar(Usuario usuario) {
            }

            @Override
            public boolean existeUsuario(String username) {
                return "admin".equals(username);
            }

            @Override
            public boolean existeCorreo(String correo) {
                return "admin@admin.com".equals(correo);
            }

            @Override
            public void actualizarPassword(String correo, String nuevaPassword) {
                if ("admin@admin.com".equals(correo)) {
                    this.passGuardada = nuevaPassword;
                }
            }
        };

        usuarioService = new UsuarioService(fakeRepo);
    }

    @Test
    public void registrarUsuario_fallaSiCorreoYaExiste() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario("nuevoUser", "admin@admin.com", "ClaveSegura123!", "Administrador");
        });

        assertEquals("Ya existe una cuenta registrada con este correo electrónico.", exception.getMessage());
    }

    @Test
    public void cambiarPassword_fallaSiPasswordEsDebil() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.cambiarPassword("admin@admin.com", "1234");
        });

        assertTrue(exception.getMessage().contains("al menos 8 caracteres"));
    }

    @Test
    public void cambiarPassword_exitosoSiCumpleRequisitos() {
        assertDoesNotThrow(() -> {
            usuarioService.cambiarPassword("admin@admin.com", "NuevaPassword2026!");
        });

        Usuario autenticado = fakeRepo.autenticar("admin@admin.com", "NuevaPassword2026!");
        assertNotNull(autenticado);
    }

    @Test
    public void registrarUsuario_fallaSiOperadorNoUsaGmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario("operador1", "operador@hotmail.com", "ClaveSegura123!", "Operador");
        });
        assertEquals("Los operadores deben registrarse con correo @gmail.com", exception.getMessage());
    }

    @Test
    public void registrarUsuario_exitosoOperador() {
        assertDoesNotThrow(() -> {
            usuarioService.registrarUsuario("operadorValido", "nuevo.operador@gmail.com", "ClaveSegura123!", "Operador");
        });
    }

    @Test
    public void registrarUsuario_fallaSiAdminNoProveeCodigoAutorizacion() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario("nuevoAdmin", "nuevo.admin@admin.com", "ClaveSegura123!", "Administrador", null);
        });
        assertTrue(exception.getMessage().contains("Código de autorización"));
    }

    @Test
    public void registrarUsuario_fallaSiAdminProveeCodigoErroneo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario("nuevoAdmin", "nuevo.admin@admin.com", "ClaveSegura123!", "Administrador", "CLAVE_INCORRECTA");
        });
        assertTrue(exception.getMessage().contains("Código de autorización"));
    }

    @Test
    public void registrarUsuario_exitosoAdminConCodigoCorrecto() {
        assertDoesNotThrow(() -> {
            usuarioService.registrarUsuario("nuevoAdmin", "nuevo.admin@admin.com", "ClaveSegura123!", "Administrador", UsuarioService.CLAVE_MAESTRA_ADMIN_DEFAULT);
        });
    }

    @Test
    public void cambiarPasswordConVerificacion_fallaSiClaveActualEsErronea() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.cambiarPasswordConVerificacion("admin@admin.com", "claveIncorrecta", "NuevaClave123*", "NuevaClave123*");
        });
        assertTrue(exception.getMessage().contains("actual"));
    }

    @Test
    public void cambiarPasswordConVerificacion_fallaSiNoCoinciden() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.cambiarPasswordConVerificacion("admin@admin.com", "admin123", "NuevaClave123*", "OtraClaveDiferente1*");
        });
        assertTrue(exception.getMessage().contains("no coinciden"));
    }

    @Test
    public void cambiarPasswordConVerificacion_exitoso() {
        assertDoesNotThrow(() -> {
            usuarioService.cambiarPasswordConVerificacion("admin@admin.com", "admin123", "NuevaClave123*", "NuevaClave123*");
        });
    }
}

