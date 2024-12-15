package gestaoSaudeMental.api.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface CredenciaisRepository extends JpaRepository <Credenciais, Long>{


    UserDetails findByLogin(String login);
}
