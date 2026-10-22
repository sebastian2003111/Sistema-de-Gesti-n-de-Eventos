package model;

public class UsuarioService {
    public static final String CLAVE_MAESTRA_ADMIN_DEFAULT = "ADMIN2026*";
    private static String claveMaestraAdmin = CLAVE_MAESTRA_ADMIN_DEFAULT;

    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    public static String getClaveMaestraAdmin() {
        return claveMaestraAdmin;
    }

    public static void setClaveMaestraAdmin(String nuevaClave) {
        if (nuevaClave != null && !nuevaClave.trim().isEmpty()) {
            claveMaestraAdmin = nuevaClave.trim();
        }
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
        registrarUsuario(username, correo, password, rol, null);
    }

    public void registrarUsuario(String username, String correo, String password, String rol, String codigoAutorizacion) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (repository.existeCorreo(correo.trim())) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con este correo electrónico.");
        }
        if (repository.existeUsuario(username.trim())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        
        // Validación de dominio y autorización según el rol
        if (rol.equals("Administrador")) {
            if (!correo.endsWith("@admin.com")) {
                throw new IllegalArgumentException("Los administradores deben registrarse con correo @admin.com");
            }
            if (codigoAutorizacion == null || !codigoAutorizacion.trim().equals(claveMaestraAdmin)) {
                throw new IllegalArgumentException("Código de autorización inválido. Para registrar un Administrador se requiere la clave maestra del sistema.");
            }
        } else if ((rol.equals("Operador") || rol.equals("Usuario")) && !correo.endsWith("@gmail.com")) {
            throw new IllegalArgumentException("Los operadores deben registrarse con correo @gmail.com");
        }

        // Validación de seguridad de la contraseña
        validarSeguridadPassword(password.trim());

        Usuario nuevoUsuario = new Usuario(username.trim(), correo.trim(), password.trim(), rol);
        repository.registrar(nuevoUsuario);
    }

    public void cambiarPassword(String correo, String nuevaPassword) throws IllegalArgumentException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
        }
        validarSeguridadPassword(nuevaPassword.trim());
        repository.actualizarPassword(correo.trim(), nuevaPassword.trim());
    }

    public void cambiarPasswordConVerificacion(String correo, String passwordActual, String nuevaPassword, String confirmarPassword) throws IllegalArgumentException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (passwordActual == null || passwordActual.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingrese su contraseña actual para verificar su identidad.");
        }
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingrese la nueva contraseña.");
        }
        if (confirmarPassword == null || confirmarPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor confirme la nueva contraseña.");
        }
        if (!nuevaPassword.equals(confirmarPassword)) {
            throw new IllegalArgumentException("La nueva contraseña y su confirmación no coinciden.");
        }
        if (nuevaPassword.equals(passwordActual)) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser idéntica a la contraseña actual.");
        }

        Usuario verificado = repository.autenticar(correo.trim(), passwordActual.trim());
        if (verificado == null) {
            throw new IllegalArgumentException("La contraseña actual ingresada es incorrecta.");
        }

        validarSeguridadPassword(nuevaPassword.trim());
        repository.actualizarPassword(correo.trim(), nuevaPassword.trim());
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
