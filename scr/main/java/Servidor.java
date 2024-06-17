import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.List;

public class Servidor {
    private static final int PORT = 3600;
    private Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) {
        new Servidor().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                    new Thread(new ClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    System.out.println("Erro ao aceitar cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar servidor: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream())) {

                String Instrucao;
                while ((Instrucao = (String) input.readObject()) != null) {
                    System.out.println("Okay,e para ja o item " + Instrucao);
                    switch (Instrucao) {
                        case "LISTAR":
                            List<Acervo_Livro> livros = biblioteca.listarLivros();
                            System.out.println("Enviando lista de livros para o cliente.");
                            output.writeObject(livros);
                            output.flush();
                            System.out.println("Livros enviados para o cliente.");
                            output.writeObject("OK");
                            output.flush();
                            break;
                        case "CADASTRAR":
                        Acervo_Livro novoLivro = (Acervo_Livro) input.readObject();
                            cadastrarLivro(novoLivro);
                            output.writeObject("Prontinho,livro cadastrado");
                            output.flush();
                            output.writeObject("OK");
                            output.flush();
                            break;
                        case "ALUGAR":
                            String tituloLivroAlugar = (String) input.readObject();
                            String respostaAluguel = alugarLivro(tituloLivroAlugar);
                            output.writeObject(respostaAluguel);
                            output.flush();
                            output.writeObject("OK");
                            output.flush();
                            break;
                        case "DEVOLVER":
                            String tituloLivroDevolver = (String) input.readObject();
                            devolverLivro(tituloLivroDevolver);
                            output.writeObject("Livro devolvido com sucesso");
                            output.flush();
                            output.writeObject("OK");
                            output.flush();
                            break;
                        case "REMOVER":
                            String tituloLivroRemover = (String) input.readObject();
                            String respostaRemover = removerLivro(tituloLivroRemover);
                            output.writeObject(respostaRemover);
                            output.flush();
                            output.writeObject("OK");
                            output.flush();
                            break;
                        default:
                            output.writeObject("Comando desconhecido");
                            output.flush();
                            output.writeObject("OK");
                            output.flush();
                    }
                    System.out.println("Comando processado: " + Instrucao);
                }
            } catch (IOException | ClassNotFoundException | ParseException e) {
                System.out.println("Erro ao processar comando: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar socket: " + e.getMessage());
                }
            }
        }

        private void cadastrarLivro(Acervo_Livro livro) throws IOException, ParseException {
            List<Acervo_Livro> livros = biblioteca.listarLivros();
            livros.add(livro);
            biblioteca.salvarLivros(livros);
        }

        private String alugarLivro(String tituloLivro) throws IOException, ParseException {
            List<Acervo_Livro> livros = biblioteca.listarLivros();
            for (Acervo_Livro livro : livros) {
                if (livro.getTitulo().equals(tituloLivro)) {
                    if (livro.getExemplares() > 0) {
                        livro.setExemplares(livro.getExemplares() - 1);
                        biblioteca.salvarLivros(livros);
                        return "Livro alugado com sucesso";
                    } else {
                        return "Nenhum exemplar disponível para aluguel";
                    }
                }
            }
            return "Livro não encontrado";
        }

        private void devolverLivro(String tituloLivro) throws IOException, ParseException {
            List<Acervo_Livro> livros = biblioteca.listarLivros();
            for (Acervo_Livro livro : livros) {
                if (livro.getTitulo().equals(tituloLivro)) {
                    livro.setExemplares(livro.getExemplares() + 1);
                    biblioteca.salvarLivros(livros);
                    break;
                }
            }
        }

        private String removerLivro(String tituloLivro) throws IOException, ParseException {
            List<Acervo_Livro> livros = biblioteca.listarLivros();
            for (int i = 0; i < livros.size(); i++) {
                if (livros.get(i).getTitulo().equals(tituloLivro)) {
                    livros.remove(i);
                    biblioteca.salvarLivros(livros);
                    return "Livro removido com sucesso";
                }
            }
            return "Livro não encontrado";
        }
    }
}