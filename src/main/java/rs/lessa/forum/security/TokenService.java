package rs.lessa.forum.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import rs.lessa.forum.modelo.Usuario;
import rs.lessa.forum.repository.UsuarioRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        Usuario logado = (Usuario) authenticate.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("API-Forum-Alura")
                .setSubject(logado.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Usuario findUserById(String token) {
        Long userId = this.getUserId(token);
        Optional<Usuario> optUser = usuarioRepository.findById(userId);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        return null;
    }

    private Long getUserId(String token) {
        Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }

    public boolean isValidaToken(String token) {
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
