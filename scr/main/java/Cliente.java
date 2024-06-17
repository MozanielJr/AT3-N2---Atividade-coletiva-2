import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;


public class Cliente {
    private static final String Endereco_Servidor = "localhost";
    private static final int Porta_do_servidor = 3600;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Socket socket = new Socket(Endereco_Servidor, Porta_do_servidor);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Bem-vindo a biblioteca 'Versos Vazios'");

            while (true) {
                mostrarMenu();
                int escolha = lerEscolha();

                switch (escolha) {
                    case 1:
                        listarLivros(output, input);
                        break;
                    case 2:
                        cadastrarLivro(output, input);
                        break;
                    case 3:
                        alugarLivro(output, input);
                        break;
                    case 4:
                        devolverLivro(output, input);
                        break;
                    case 5:
                        removerLivro(output, input);
                        break;
                    case 6:
                        System.out.println("Saindo...");
                        
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nMenu:");
        System.out.println("|1.| Listar livros");
        System.out.println("|2.| Cadastrar novo livro");
        System.out.println("|3.| Alugar livro");
        System.out.println("|4.| Devolver livro");
        System.out.println("|5.| Remover livro");
        System.out.println("|6.| Sair");
        System.out.printf("Escolha uma opção: ");
        
    }

    private static int lerEscolha() {
        while (!scanner.hasNextInt()) {
            System.out.printf("Numero invalido,tente novamente");
            scanner.next();
        }
        int escolha = scanner.nextInt();
        scanner.nextLine(); 
        return escolha;
    }

    private static void listarLivros(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        output.writeObject("LISTAR");
        output.flush();
        System.out.println("Comando LISTAR enviado.");
        @SuppressWarnings("unchecked")
        List<Acervo_Livro> livros = (List<Acervo_Livro>) input.readObject();
        System.out.println("Resposta recebida do servidor:");
        for (Acervo_Livro livro : livros) {
            System.out.println(livro);
        }
        esperarConfirmacao(input);
    }

    private static void cadastrarLivro(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        System.out.printf("Qual o titulo do livro: ");
        String titulo = scanner.nextLine();
        System.out.printf("Qual o autor do livro: ");
        String autor = scanner.nextLine();
        System.out.printf("Qual o genero do livro: ");
        String genero = scanner.nextLine();
        System.out.printf("Quantos exemplares,ha: ");
        int exemplares = lerNumeroExemplares();

        output.writeObject("CADASTRAR");
        output.writeObject(new Acervo_Livro(titulo, autor, genero, exemplares));
        output.flush();
        System.out.println("Comando CADASTRAR enviado.");
        String respostaCadastro = (String) input.readObject();
        System.out.println("Resposta recebida do servidor: " + respostaCadastro);
        esperarConfirmacao(input);
    }

    private static void alugarLivro(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        System.out.print("Preciso que me forneca o titulo para deixar aluga-lo,qual e? ");
        String titulo = scanner.nextLine();

        output.writeObject("ALUGAR");
        output.writeObject(titulo);
        output.flush();
        System.out.println("Comando ALUGAR enviado.");
        String respostaAluguel = (String) input.readObject();
        System.out.println("Resposta recebida do servidor: " + respostaAluguel);
        esperarConfirmacao(input);
    }

    private static void devolverLivro(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        System.out.printf("Qual o titulo do livro para eu dar baixa aqui? ");
        String titulo = scanner.nextLine();

        output.writeObject("DEVOLVER");
        output.writeObject(titulo);
        output.flush();
        System.out.println("Comando DEVOLVER enviado.");
        String respostaDevolucao = (String) input.readObject();
        System.out.println("Resposta recebida do servidor: " + respostaDevolucao);
        esperarConfirmacao(input);
    }

    private static void removerLivro(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        System.out.printf("Qual o titulo do livro para eu remove-lo: ");
        String titulo = scanner.nextLine();

        output.writeObject("REMOVER");
        output.writeObject(titulo);
        output.flush();
        System.out.println("Comando REMOVER enviado.");
        String respostaRemover = (String) input.readObject();
        System.out.println("Resposta recebida do servidor: " + respostaRemover);
        esperarConfirmacao(input);
    }

    private static void esperarConfirmacao(ObjectInputStream input) throws IOException, ClassNotFoundException {
        String confirmacao = (String) input.readObject();
        if (!"OK".equals(confirmacao)) {
            throw new IOException("Erro na confirmacao de recebimento");
        }
    }

    private static int lerNumeroExemplares() {
        while (!scanner.hasNextInt()) {
            System.out.printf("Por favor, insira um numero valido de exemplares: ");
            scanner.next();
        }
        int exemplares = scanner.nextInt();
        scanner.nextLine(); 
        return exemplares;
    }
}




