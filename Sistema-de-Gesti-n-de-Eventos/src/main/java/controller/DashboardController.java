package controller;

import model.Usuario;
import view.FrmDashboard;
import view.FrmEvento;
import model.EventoService;
import model.IEventoRepository;
import model.EventoRepository;
import java.awt.Container;

public class DashboardController {

    private final FrmDashboard vistaDashboard;
    private final Usuario usuarioActual;
    
    // Controladores hijos
    private EventoController controladorEvento;

    public DashboardController(FrmDashboard vistaDashboard, Usuario usuarioActual) {
        this.vistaDashboard = vistaDashboard;
        this.usuarioActual = usuarioActual;
        
        initView();
        initControllers();
        initEvents();
    }
    
    private void initView() {
        if (usuarioActual == null) {
            vistaDashboard.setLblUserRole("Invitado");
            vistaDashboard.setLblUsuarioHeader("Invitado");
            vistaDashboard.getBtnSalir().setText("🔑  Iniciar Sesión");
            vistaDashboard.getBtnSalir().setForeground(new java.awt.Color(80, 220, 130)); // Verde para iniciar sesión
            vistaDashboard.getBtnSalir().setBackground(new java.awt.Color(46, 204, 113, 25));
            vistaDashboard.getBtnSalir().setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.LineBorder(new java.awt.Color(46, 204, 113, 70), 1, true),
                    new javax.swing.border.EmptyBorder(8, 14, 8, 14)
            ));
        } else {
            vistaDashboard.setLblUserRole(usuarioActual.getRol());
            vistaDashboard.setLblUsuarioHeader(usuarioActual.getUsername());
        }
        vistaDashboard.setVisible(true);
    }
    
    private void initControllers() {
        IEventoRepository repository = EventoRepository.getInstance();
        EventoService servicioEvento = new EventoService(repository);

        // 1. Inicializar el módulo de Inicio (Dashboard Principal)
        view.PnlInicio panelInicio = new view.PnlInicio();
        if (usuarioActual != null) {
            panelInicio.setNombreBienvenida(usuarioActual.getUsername());
        }
        InicioController inicioController = new InicioController(panelInicio, servicioEvento);
        inicioController.iniciar();
        vistaDashboard.agregarPanelContenido(panelInicio, "DASHBOARD");

        // 2. Inicializar el módulo de Eventos
        FrmEvento vistaEvento = new FrmEvento();
        controladorEvento = new EventoController(vistaEvento, servicioEvento);
        controladorEvento.setUsuarioActual(usuarioActual);
        vistaDashboard.agregarPanelContenido(vistaEvento.getContentPane(), "EVENTOS");
        controladorEvento.iniciar();

        // 3. Inicializar Calendario
        view.PnlCalendario pnlCalendario = new view.PnlCalendario();
        CalendarioController calController = new CalendarioController(pnlCalendario);
        vistaDashboard.agregarPanelContenido(pnlCalendario, "CALENDARIO");

        // 4. Inicializar Participantes
        view.PnlParticipantes pnlParticipantes = new view.PnlParticipantes();
        ParticipantesController partController = new ParticipantesController(pnlParticipantes, usuarioActual);
        vistaDashboard.agregarPanelContenido(pnlParticipantes, "PARTICIPANTES");

        // 5. Inicializar Informes
        view.PnlInformes pnlInformes = new view.PnlInformes();
        InformesController infController = new InformesController(pnlInformes);
        vistaDashboard.agregarPanelContenido(pnlInformes, "INFORMES");

        // 6. Inicializar Configuración
        view.PnlConfiguracion pnlConfig = new view.PnlConfiguracion();
        ConfiguracionController confController = new ConfiguracionController(pnlConfig, usuarioActual);
        vistaDashboard.agregarPanelContenido(pnlConfig, "CONFIGURACION");

        // 7. Inicializar Recursos y Actividades
        view.PnlRecursosActividades pnlRecursos = new view.PnlRecursosActividades();
        RecursosActividadesController recurController = new RecursosActividadesController(pnlRecursos, usuarioActual);
        vistaDashboard.agregarPanelContenido(pnlRecursos, "RECURSOS");

        // 8. Inicializar Notificaciones
        view.PnlNotificaciones pnlNotificaciones = new view.PnlNotificaciones();
        NotificacionesController notifController = new NotificacionesController(pnlNotificaciones);
        vistaDashboard.agregarPanelContenido(pnlNotificaciones, "NOTIFICACIONES");

        // Conectar acciones rápidas del dashboard de inicio
        panelInicio.getBtnAccionCrear().addActionListener(e -> {
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnEventos());
            vistaDashboard.mostrarPanel("EVENTOS");
        });
        panelInicio.getBtnAccionCalendario().addActionListener(e -> {
            calController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnCalendario());
            vistaDashboard.mostrarPanel("CALENDARIO");
        });
        panelInicio.getBtnAccionParticipantes().addActionListener(e -> {
            partController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnParticipantes());
            vistaDashboard.mostrarPanel("PARTICIPANTES");
        });
        panelInicio.getBtnAccionInformes().addActionListener(e -> {
            infController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnInformes());
            vistaDashboard.mostrarPanel("INFORMES");
        });

        // Refrescar datos y marcar botón activo al navegar desde el sidebar
        vistaDashboard.getBtnDashboard().addActionListener(e -> {
            inicioController.actualizarDatos();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnDashboard());
            vistaDashboard.mostrarPanel("DASHBOARD");
        });
        vistaDashboard.getBtnCalendario().addActionListener(e -> {
            calController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnCalendario());
            vistaDashboard.mostrarPanel("CALENDARIO");
        });
        vistaDashboard.getBtnParticipantes().addActionListener(e -> {
            partController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnParticipantes());
            vistaDashboard.mostrarPanel("PARTICIPANTES");
        });
        vistaDashboard.getBtnInformes().addActionListener(e -> {
            infController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnInformes());
            vistaDashboard.mostrarPanel("INFORMES");
        });
        vistaDashboard.getBtnConfiguracion().addActionListener(e -> {
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnConfiguracion());
            vistaDashboard.mostrarPanel("CONFIGURACION");
        });
        vistaDashboard.getBtnActividades().addActionListener(e -> {
            recurController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnActividades());
            vistaDashboard.mostrarPanel("RECURSOS");
        });
        vistaDashboard.getBtnNotificaciones().addActionListener(e -> {
            notifController.iniciar();
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnNotificaciones());
            vistaDashboard.mostrarPanel("NOTIFICACIONES");
        });
        vistaDashboard.getBtnEventos().addActionListener(e -> {
            vistaDashboard.marcarBotonActivo(vistaDashboard.getBtnEventos());
            vistaDashboard.mostrarPanel("EVENTOS");
        });
    }
    
    private void initEvents() {
        // Los listeners de los módulos ya fueron asignados en initControllers
        vistaDashboard.getBtnSalir().addActionListener(e -> cerrarSesion());
    }
    
    private void cerrarSesion() {
        if (usuarioActual != null) {
            int resp = javax.swing.JOptionPane.showConfirmDialog(vistaDashboard, "¿Estás seguro que deseas cerrar sesión?", "Cerrar Sesión", javax.swing.JOptionPane.YES_NO_OPTION);
            if (resp == javax.swing.JOptionPane.YES_OPTION) {
                // Reiniciar el dashboard en modo invitado
                vistaDashboard.dispose();
                view.FrmDashboard nuevaVista = new view.FrmDashboard();
                DashboardController nuevoControlador = new DashboardController(nuevaVista, null);
            }
        } else {
            abrirLogin(); // Si ya es invitado, este botón dice "Iniciar Sesión"
        }
    }

    private void abrirLogin() {
        vistaDashboard.dispose();
        view.FrmLogin vistaLogin = new view.FrmLogin();
        model.IUsuarioRepository userRepo = model.UsuarioRepository.getInstance();
        model.UsuarioService userService = new model.UsuarioService(userRepo);
        LoginController loginController = new LoginController(vistaLogin, userService);
        loginController.iniciar();
    }
}
