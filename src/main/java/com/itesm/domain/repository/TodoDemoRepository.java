package com.itesm.domain.repository;

import com.itesm.domain.models.demo.LazyInitDemoResult;
import com.itesm.domain.models.demo.TodoView;

import java.util.List;

/**
 * Puerto de dominio para las estrategias de fetching de la demo.
 * El dominio solo conoce TodoView (DTO de dominio), no entidades JPA.
 * La infraestructura decide cómo implementar cada estrategia.
 */
public interface TodoDemoRepository {

    List<TodoView> findAllShallow();

    List<TodoView> findAllTriggeringNPlusOne();

    List<TodoView> findAllWithJoinFetch();

    List<TodoView> findAllWithEntityGraph();

    List<TodoView> findAllAsProjection();

    List<TodoView> findAllSimulatingEager();

    LazyInitDemoResult demonstrateLazyInitException();
}