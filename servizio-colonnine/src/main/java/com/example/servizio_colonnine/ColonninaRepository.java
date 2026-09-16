package com.example.servizio_colonnine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColonninaRepository extends JpaRepository<Colonnina, Long> {
}
