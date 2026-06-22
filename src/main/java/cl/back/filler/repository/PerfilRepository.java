package cl.back.filler.repository;

import cl.back.filler.model.WaifuHusbando;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<WaifuHusbando, Long> {
    
}