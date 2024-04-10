package br.com.challange.repository;

import br.com.challange.models.Marking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MarkingRepository extends JpaRepository<Marking, Integer>, JpaSpecificationExecutor<Marking> {

    List<Marking> findAllByUserId(final int userId);

}
