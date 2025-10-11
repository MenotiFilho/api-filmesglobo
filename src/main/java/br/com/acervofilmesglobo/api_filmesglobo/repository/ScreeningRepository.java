package br.com.acervofilmesglobo.api_filmesglobo.repository;

import br.com.acervofilmesglobo.api_filmesglobo.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
}
