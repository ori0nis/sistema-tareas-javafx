package gm.tareas.servicio;

import gm.tareas.TareasApplication;
import gm.tareas.modelo.Tarea;

import java.util.List;

public interface ITareaServicio {
    public List<Tarea> listarTareas();

    public Tarea buscarTareaPorId(Integer id);

    public void guardarTarea(Tarea tarea); // Recordar que este método guarda o actualiza automáticamente gracias
                                           // a Hibernate

    public void eliminarTarea(Tarea tarea);
}
