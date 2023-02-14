package ifrn.pi.farmacia.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.farmacia.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);
}
