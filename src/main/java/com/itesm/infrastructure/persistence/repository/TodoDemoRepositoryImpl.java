package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.demo.LazyInitDemoResult;
import com.itesm.domain.models.demo.TodoView;
import com.itesm.domain.repository.TodoDemoRepository;
import com.itesm.infrastructure.mapper.TodoViewMapper;
import com.itesm.infrastructure.persistence.entity.TodoEntity;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TodoDemoRepositoryImpl implements TodoDemoRepository {

    @Inject
    EntityManager em;

    /**
     * LAZY sin tocar relaciones → 1 sola query.
     */
    @Override
    @Transactional
    public List<TodoView> findAllShallow() {
        List<TodoEntity> todos = em.createQuery("SELECT t FROM TodoEntity t", TodoEntity.class)
                .getResultList();
        return todos.stream()
                .map(TodoViewMapper::toShallowView)
                .collect(Collectors.toList());
    }

    /**
     * LAZY + acceso a todas las relaciones → dispara N+1.
     */
    @Override
    @Transactional
    public List<TodoView> findAllTriggeringNPlusOne() {
        List<TodoEntity> todos = em.createQuery("SELECT t FROM TodoEntity t", TodoEntity.class)
                .getResultList();
        return todos.stream()
                .map(TodoViewMapper::toFullView)
                .collect(Collectors.toList());
    }

    /**
     * JOIN FETCH explícito. Dos colecciones se traen en pasos separados
     * para evitar MultipleBagFetchException.
     */
    @Override
    @Transactional
    public List<TodoView> findAllWithJoinFetch() {
        List<TodoEntity> todos = em.createQuery(
                        "SELECT DISTINCT t FROM TodoEntity t " +
                                "LEFT JOIN FETCH t.owner " +
                                "LEFT JOIN FETCH t.categories", TodoEntity.class)
                .getResultList();

        if (!todos.isEmpty()) {
            em.createQuery(
                            "SELECT DISTINCT t FROM TodoEntity t " +
                                    "LEFT JOIN FETCH t.comments c " +
                                    "LEFT JOIN FETCH c.author " +
                                    "WHERE t IN :todos", TodoEntity.class)
                    .setParameter("todos", todos)
                    .getResultList();
        }

        return todos.stream()
                .map(TodoViewMapper::toFullView)
                .collect(Collectors.toList());
    }

    /**
     * @NamedEntityGraph("Todo.full") como hint — alternativa declarativa.
     */
    @Override
    @Transactional
    public List<TodoView> findAllWithEntityGraph() {
        EntityGraph<?> graph = em.getEntityGraph("Todo.notSofull");
        TypedQuery<TodoEntity> query = em.createQuery("SELECT t FROM TodoEntity t", TodoEntity.class);
        query.setHint("jakarta.persistence.loadgraph", graph);
        return query.getResultList().stream()
                .map(TodoViewMapper::toFullView)
                .collect(Collectors.toList());
    }

    /**
     * Proyección directa a tuplas → sin entidades, sin proxies, sin N+1.
     */
    @Override
    @Transactional
    public List<TodoView> findAllAsProjection() {
        List<Object[]> rows = em.createQuery(
                        "SELECT t.id, t.title, t.completed, u.fullName, u.email " +
                                "FROM TodoEntity t JOIN t.owner u", Object[].class)
                .getResultList();

        List<TodoView> out = new ArrayList<>();
        for (Object[] r : rows) {
            out.add(new TodoView(
                    r[0].toString(),
                    (String) r[1],
                    (boolean) r[2],
                    (String) r[3],
                    (String) r[4],
                    Collections.emptyList(),
                    Collections.emptyList()
            ));
        }
        return out;
    }

    @Transactional
    public List<TodoView> findByOwnerId(String owner_uuid){
        EntityGraph<?> graph = em.getEntityGraph("Todo.notSofull");
        TypedQuery<TodoEntity> query = em.createQuery("SELECT t FROM TodoEntity t WHERE t.owner.id=:ownerId", TodoEntity.class);
        query.setParameter("ownerId", owner_uuid);
        query.setHint("jakarta.persistence.loadgraph", graph);
        return query.getResultList().stream()
                .map(TodoViewMapper::toFullView)
                .collect(Collectors.toList());
    }

    /**
     * Simula el efecto de FetchType.EAGER: Hibernate trae todos los joins
     * aunque el consumidor solo necesite title + completed.
     */
    @Override
    @Transactional
    public List<TodoView> findAllSimulatingEager() {
        List<TodoEntity> todos = em.createQuery(
                        "SELECT DISTINCT t FROM TodoEntity t " +
                                "LEFT JOIN FETCH t.owner " +
                                "LEFT JOIN FETCH t.categories", TodoEntity.class)
                .getResultList();
        // Solo exponemos title+completed; el costo de los joins ya se pagó igual
        return todos.stream()
                .map(TodoViewMapper::toShallowView)
                .collect(Collectors.toList());
    }

    /**
     * Fetch en una tx interna, luego acceso fuera de sesión → LazyInitializationException.
     */
    @Override
    public LazyInitDemoResult demonstrateLazyInitException() {
        List<TodoEntity> todos = QuarkusTransaction.requiringNew().call(() ->
                em.createQuery("SELECT t FROM TodoEntity t", TodoEntity.class).getResultList()
        );

        if (todos.isEmpty()) {
            return new LazyInitDemoResult(
                    "lazy-init-exception",
                    "sin datos",
                    null,
                    "No hay todos en la DB. Arranca con el seed para ver la demo completa.");
        }

        try {
            String ownerName = todos.get(0).getOwner().getFullName();
            return new LazyInitDemoResult(
                    "lazy-init-exception",
                    "no explotó (cache de primer nivel lo resolvió)",
                    ownerName,
                    "Normalmente el proxy lanza LazyInitializationException; en esta corrida quedó en cache.");
        } catch (LazyInitializationException e) {
            return new LazyInitDemoResult(
                    "lazy-init-exception",
                    "EXPLOTÓ — LazyInitializationException",
                    e.getMessage(),
                    "Hibernate necesita una sesión abierta para cargar relaciones lazy. " +
                            "Siempre accede a lazy dentro de @Transactional, o usa join fetch/entity graph, o mapea a DTO.");
        }
    }
}