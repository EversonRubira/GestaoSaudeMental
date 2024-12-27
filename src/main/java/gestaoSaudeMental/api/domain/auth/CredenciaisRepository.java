package gestaoSaudeMental.api.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface CredenciaisRepository extends JpaRepository <Credenciais, Long>{



    Optional<Credenciais> findByUsuario_Email(String email);


}
