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

    public Todo findBId(String id) {
        UUID uuid = UUID.fromString(id);
        return todoRepository.findBiId(uuid);
    }

}