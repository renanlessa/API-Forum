package rs.lessa.forum.controller.request;

import rs.lessa.forum.modelo.Curso;
import rs.lessa.forum.modelo.Topico;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TopicoRequest {

    @NotNull
    @NotEmpty
    private String titulo;

    @NotNull
    @NotEmpty
    private String mensagem;

    @NotNull
    @NotEmpty
    private String nomeCurso;

    public Topico converter(Curso curso) {
        return new Topico(titulo, mensagem, curso);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
}