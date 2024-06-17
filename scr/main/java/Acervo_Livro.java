
import java.io.Serializable;

public class Acervo_Livro implements Serializable {
    private String titulo;
    private String genero;
    private int exemplares;
    private String autor;

    public Acervo_Livro(String titulo, String autor, String genero, int exemplares) {
        this.titulo = titulo;
        this.genero = genero;
        this.exemplares = exemplares;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getExemplares() {
        return exemplares;
    }

    public String getAutor() {
        return autor;
    }

    

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Livro: %s\nAutor: %s\nGênero: %s\nExemplares Disponíveis: %d\n--------------------------------------",
            titulo, autor, genero, exemplares
        );
    }
}
