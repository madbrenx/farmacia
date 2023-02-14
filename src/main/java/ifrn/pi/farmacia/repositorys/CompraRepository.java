package ifrn.pi.farmacia.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.farmacia.models.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {
	
	
}
