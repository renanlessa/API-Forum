package rs.lessa.forum.controller.dto;

public class TokenDto {

    private String token;
    private String tipo;

    public TokenDto(String token, String bearer) {
        this.token = token;
        this.tipo = bearer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
