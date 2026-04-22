package  com.itesm.application.usecase;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class FindByIdUseCase {
    private TodoRepository todoRepository;

    @Inject
    public FindByIdUseCase(TodoRepository todoRepository) {this.todoRepository = todoRepository;}

    /**
     * Encuentra un Todo por ID con eager loading de owner, categories y comments.
     * Usa JOIN FETCH para evitar N+1 queries.
     * 
     * @param id ID del Todo en formato String (UUID)
     * @return Todo con todas sus relaciones cargadas, o null si no existe
     */
    public Todo findBId(String id) {
        UUID uuid = UUID.fromString(id);
        return todoRepository.findByIdWithJoinFetch(uuid);
    }

}