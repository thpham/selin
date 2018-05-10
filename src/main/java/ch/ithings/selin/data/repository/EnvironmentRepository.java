package ch.ithings.selin.data.repository;

import ch.ithings.selin.data.entity.Environment;
import org.springframework.data.repository.CrudRepository;

/**
 * A {@link CrudRepository} for our {@link Environment} entity.
 *
 * @author Thomas Pham
 */
public interface EnvironmentRepository extends CrudRepository<Environment, Long> {
}
