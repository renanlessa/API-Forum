package rs.lessa.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.lessa.forum.modelo.Usuario;
import rs.lessa.forum.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> optUsuario = repository.findByEmail(email);
        if(optUsuario.isPresent()) {
            return optUsuario.get();
        }
        throw new UsernameNotFoundException("Dados invalidos");
    }
}
