package ifrn.pi.farmacia.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ifrn.pi.farmacia.models.Remedio;

public interface RemedioRepository extends JpaRepository<Remedio, Long> {
	
	List<Remedio> findBySelecionado(boolean selecionado);
	@Query(value="DELETE FROM remedio.compra_remedios WHERE remedios_id=? AND compra_id=any", nativeQuery=true) Remedio deletarVinculo(Long idRemedio);
}
