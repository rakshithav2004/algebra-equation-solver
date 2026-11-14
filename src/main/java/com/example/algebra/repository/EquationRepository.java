package com.example.algebra.repository;

import com.example.algebra.model.Equation;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class EquationRepository {

  private final Map<String, Equation> equations = new ConcurrentHashMap<>();

  public void save(Equation equation) {
    equations.put(equation.getEquationId(), equation);
  }

  public Optional<Equation> findById(String equationId) {
    return Optional.ofNullable(equations.get(equationId));
  }

  public Collection<Equation> findAll() {
    return equations.values();
  }
}
