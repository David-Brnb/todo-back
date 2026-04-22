package com.itesm.application.usecase.demo;

import com.itesm.domain.models.demo.LazyInitDemoResult;
import com.itesm.domain.models.demo.TodoDemoResult;
import com.itesm.domain.models.demo.TodoView;
import com.itesm.domain.repository.TodoDemoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.function.Supplier;

@ApplicationScoped
public class TodoFetchDemoUseCase {

    private final TodoDemoRepository repo;

    @Inject
    public TodoFetchDemoUseCase(TodoDemoRepository repo) {
        this.repo = repo;
    }

    public TodoDemoResult runLazyOk() {
        return measure("lazy-ok",
                "1 sola query. LAZY no carga relaciones hasta que las tocas. " +
                        "Si nunca las tocas, nunca gastas DB — este es el beneficio de LAZY como default.",
                repo::findAllShallow);
    }

    public TodoDemoResult runEagerSimulation() {
        return measure("eager-simulation",
                "Con EAGER pagas los joins SIEMPRE, aunque solo uses title+completed. " +
                        "Imagina un listado con 10 relaciones EAGER: cada findAll() trae todo el grafo. " +
                        "Por eso EAGER es un mal default.",
                repo::findAllSimulatingEager);
    }

    public TodoDemoResult runNPlusOne() {
        return measure("lazy-n-plus-1",
                "1 query base + 1 query extra por cada todo para owner/categories/comments. " +
                        "Para 10 todos son ~31 queries. Mira la consola: el problema N+1 en vivo.",
                repo::findAllTriggeringNPlusOne);
    }

    public TodoDemoResult runJoinFetch() {
        return measure("join-fetch",
                "2 queries totales (todos+owner+categories en una, comments+authors en otra). " +
                        "El problema N+1 desaparece.",
                repo::findAllWithJoinFetch);
    }

    public TodoDemoResult runEntityGraph() {
        return measure("entity-graph",
                "@NamedEntityGraph('Todo.full') como hint — Hibernate arma los joins automáticamente. " +
                        "Más declarativo que JPQL manual, reutilizable entre queries.",
                repo::findAllWithEntityGraph);
    }

    public TodoDemoResult runProjection() {
        return measure("projection",
                "Proyección directa a tuplas: 1 sola query, sin hidratar entidades. " +
                        "Ideal para listados de solo lectura (dashboards, reportes).",
                repo::findAllAsProjection);
    }

    public LazyInitDemoResult runLazyInitException() {
        return repo.demonstrateLazyInitException();
    }

    private TodoDemoResult measure(String strategy, String leccion, Supplier<List<TodoView>> fetcher) {
        long start = System.currentTimeMillis();
        List<TodoView> data = fetcher.get();
        long duration = System.currentTimeMillis() - start;
        return new TodoDemoResult(strategy, duration, data.size(), leccion, data);
    }
}