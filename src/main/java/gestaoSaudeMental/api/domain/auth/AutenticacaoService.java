package gestaoSaudeMental.api.domain.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private CredenciaisRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByUsuario_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas para o e-mail: " + email));


    }
}
