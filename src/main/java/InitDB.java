import model.UsuarioRepository;
import model.NotificacionRepository;

public class InitDB {
    public static void main(String[] args) {
        System.out.println("Initializing repos...");
        UsuarioRepository.getInstance();
        NotificacionRepository.getInstance();
        System.out.println("Done!");
    }
}
