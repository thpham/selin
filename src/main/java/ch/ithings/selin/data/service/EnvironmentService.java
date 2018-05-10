package ch.ithings.selin.data.service;

import ch.ithings.selin.data.entity.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.*;
import ch.ithings.selin.data.repository.EnvironmentRepository;

/**
 * A classical transactional service facade.
 *
 * @author Thomas Pham
 */
@Service
@Transactional
public class EnvironmentService {

  @Autowired
  EnvironmentRepository environmentRepository;

  public Environment save(Environment environment) {
    return environmentRepository.save(environment);
  }

  public List<Environment> getAll() {
    Iterable<Environment> all = environmentRepository.findAll();
    return StreamSupport.stream(all.spliterator(), false).collect(toList());
  }

}
