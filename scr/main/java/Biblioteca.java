import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Biblioteca {
    private static final String FILENAME = "src/main/resources/livros.json";

    public List<Acervo_Livro> listarLivros() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(FILENAME)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonObject.get("livros");

            List<Acervo_Livro> livros = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSONObject livroObject = (JSONObject) obj;
                Acervo_Livro livro = new Acervo_Livro(
                        (String) livroObject.get("titulo"),
                        (String) livroObject.get("autor"),
                        (String) livroObject.get("genero"),
                        ((Long) livroObject.get("exemplares")).intValue()
                );
                livros.add(livro);
            }
            return livros;
        }
    }

    @SuppressWarnings("unchecked")
    public void salvarLivros(List<Acervo_Livro> livros) throws IOException {
        JSONArray jsonArray = new JSONArray();
        for (Acervo_Livro livro : livros) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("titulo", livro.getTitulo());
            jsonObject.put("autor", livro.getAutor());
            jsonObject.put("genero", livro.getGenero());
            jsonObject.put("exemplares", livro.getExemplares());
            jsonArray.add(jsonObject);
        }


        JSONObject jsonFileContent = new JSONObject();
        jsonFileContent.put("livros", jsonArray);

        try (Writer writer = new FileWriter(FILENAME)) {
            writer.write(jsonFileContent.toJSONString());
            writer.flush();
        }
    }
}
