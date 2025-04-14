package bookadvisor.bookadvisor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import bookadvisor.bookadvisor.domain.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long>{
    
}
