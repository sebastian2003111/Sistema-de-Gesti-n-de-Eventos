package model;

public class UsuarioService {
    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario login(String correo, String password) throws IllegalArgumentException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        Usuario usuario = repository.autenticar(correo.trim(), password.trim());
        
        if (usuario == null) {
            throw new IllegalArgumentException("Credenciales incorrectas. Verifique su usuario y contraseña.");
        }

        return usuario;
    }

    public void registrarUsuario(String username, String correo, String password, String rol) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (repository.existeUsuario(username.trim())) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        
        // Validación de dominio según el rol
        if (rol.equals("Administrador") && !correo.endsWith("@admin.com")) {
            throw new IllegalArgumentException("Los administradores deben registrarse con correo @admin.com");
        } else if (rol.equals("Usuario") && !correo.endsWith("@gmail.com")) {
            throw new IllegalArgumentException("Los usuarios deben registrarse con correo @gmail.com");
        }

        // Validación de seguridad de la contraseña
        validarSeguridadPassword(password.trim());

        Usuario nuevoUsuario = new Usuario(username.trim(), correo.trim(), password.trim(), rol);
        repository.registrar(nuevoUsuario);
    }
    
    private void validarSeguridadPassword(String password) throws IllegalArgumentException {
        if (password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula.");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un número.");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("La contraseña debe contener al menos un símbolo (ej. !@#$%).");
        }
    }
}
