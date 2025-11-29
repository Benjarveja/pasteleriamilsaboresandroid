package cl.milsabores.pasteleria.repository;

import cl.milsabores.pasteleria.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, String> {
}

