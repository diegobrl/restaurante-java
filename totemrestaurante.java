import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

// Interface para itens do menu
interface MenuItem {
    String getNome();
    String getDescricao();
    double getPreco();
}

// Implementação concreta de MenuItem
class Prato implements MenuItem {
    private String nome;
    private String descricao;
    private double preco;

    public Prato(String nome, String descricao, double preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    @Override
    public String getNome() { return nome; }

    @Override
    public String getDescricao() { return descricao; }

    @Override
    public double getPreco() { return preco; }
}

// Interface para o menu
interface Menu {
    void adicionarItem(MenuItem item);
    void removerItem(MenuItem item);
    List<MenuItem> listarItens();
}

// Implementação concreta do Menu
class MenuRestaurante implements Menu {
    private List<MenuItem> itens;

    public MenuRestaurante() {
        this.itens = new ArrayList<>();
    }

    @Override
    public void adicionarItem(MenuItem item) {
        itens.add(item);
    }

    @Override
    public void removerItem(MenuItem item) {
        itens.remove(item);
    }

    @Override
    public List<MenuItem> listarItens() {
        return new ArrayList<>(itens);
    }
}

// Classe abstrata para Pessoa
abstract class Pessoa {
    protected String nome;
    protected String telefone;

    public Pessoa(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
}

// Classe Cliente que herda de Pessoa
class Cliente extends Pessoa {
    private String endereco;

    public Cliente(String nome, String telefone, String endereco) {
        super(nome, telefone);
        this.endereco = endereco;
    }

    public String getEndereco() { return endereco; }
}

// Interface para Pedido
interface Pedido {
    void adicionarItem(MenuItem item);
    void removerItem(MenuItem item);
    List<MenuItem> getItens();
    double getTotal();
    Cliente getCliente();
    String getStatus();
    void setStatus(String status);
}

// Implementação concreta de Pedido
class PedidoRestaurante implements Pedido {
    private List<MenuItem> itens;
    private Cliente cliente;
    private String status;

    public PedidoRestaurante(Cliente cliente) {
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = "Pendente";
    }

    @Override
    public void adicionarItem(MenuItem item) {
        itens.add(item);
    }

    @Override
    public void removerItem(MenuItem item) {
        itens.remove(item);
    }

    @Override
    public List<MenuItem> getItens() {
        return new ArrayList<>(itens);
    }

    @Override
    public double getTotal() {
        return itens.stream().mapToDouble(MenuItem::getPreco).sum();
    }

    @Override
    public Cliente getCliente() { return cliente; }

    @Override
    public String getStatus() { return status; }

    @Override
    public void setStatus(String status) { this.status = status; }
}

// Interface para Pagamento
interface Pagamento {
    void processar();
    boolean foiPago();
    Pedido getPedido();
    String getMetodo();
}

// Implementação concreta de Pagamento
class PagamentoRestaurante implements Pagamento {
    private Pedido pedido;
    private String metodo;
    private boolean pago;

    public PagamentoRestaurante(Pedido pedido, String metodo) {
        this.pedido = pedido;
        this.metodo = metodo;
        this.pago = false;
    }

    @Override
    public void processar() {
        // Lógica de processamento de pagamento
        this.pago = true;
        pedido.setStatus("Pago");
    }

    @Override
    public boolean foiPago() { return pago; }

    @Override
    public Pedido getPedido() { return pedido; }

    @Override
    public String getMetodo() { return metodo; }
}

// Classe para gerar relatórios
class RelatorioVendas {
    public void gerarRelatorio(List<Pedido> pedidos, Date inicio, Date fim) {
        double totalVendas = pedidos.stream()
                .filter(p -> p.getStatus().equals("Pago"))
                .mapToDouble(Pedido::getTotal)
                .sum();

        System.out.println("Relatório de Vendas");
        System.out.println("Período: " + inicio + " a " + fim);
        System.out.println("Total de vendas: R$" + totalVendas);
    }
}

// Classe principal que simula o totem de autoatendimento
class TotemAutoatendimento {
    private Scanner scanner;
    private Menu menu;
    private Pedido pedidoAtual;
    private List<Pedido> pedidos;

    public TotemAutoatendimento() {
        scanner = new Scanner(System.in);
        menu = new MenuRestaurante();
        pedidos = new ArrayList<>();
        inicializarMenu();
    }

    private void inicializarMenu() {
        menu.adicionarItem(new Prato("Hambúrguer", "Hambúrguer com queijo e salada", 25.0));
        menu.adicionarItem(new Prato("Pizza", "Pizza de mussarela", 35.0));
        menu.adicionarItem(new Prato("Salada Caesar", "Salada com frango grelhado", 20.0));
        menu.adicionarItem(new Prato("Refrigerante", "Lata 350ml", 5.0));
    }

    public void iniciar() {
        System.out.println("Bem-vindo ao Totem de Autoatendimento!");

        while (true) {
            exibirMenuPrincipal();
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    fazerPedido();
                    break;
                case 2:
                    visualizarPedidoAtual();
                    break;
                case 3:
                    finalizarPedido();
                    break;
                case 4:
                    System.out.println("Obrigado por utilizar nosso totem. Até logo!");
                    return;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Fazer Pedido");
        System.out.println("2. Visualizar Pedido Atual");
        System.out.println("3. Finalizar Pedido");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void fazerPedido() {
        if (pedidoAtual == null) {
            pedidoAtual = new PedidoRestaurante(new Cliente("Cliente Totem", "", ""));
        }

        while (true) {
            exibirCardapio();
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha um item (0 para voltar): ");
            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            if (escolha == 0) {
                break;
            } else if (escolha > 0 && escolha <= menu.listarItens().size()) {
                MenuItem itemEscolhido = menu.listarItens().get(escolha - 1);
                pedidoAtual.adicionarItem(itemEscolhido);
                System.out.println(itemEscolhido.getNome() + " adicionado ao pedido.");
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirCardapio() {
        System.out.println("\n=== Cardápio ===");
        List<MenuItem> itens = menu.listarItens();
        for (int i = 0; i < itens.size(); i++) {
            MenuItem item = itens.get(i);
            System.out.printf("%d. %s - R$ %.2f\n", i + 1, item.getNome(), item.getPreco());
            System.out.println("   " + item.getDescricao());
        }
    }

    private void visualizarPedidoAtual() {
        if (pedidoAtual == null || pedidoAtual.getItens().isEmpty()) {
            System.out.println("Não há itens no pedido atual.");
            return;
        }

        System.out.println("\n=== Seu Pedido Atual ===");
        for (MenuItem item : pedidoAtual.getItens()) {
            System.out.printf("%s - R$ %.2f\n", item.getNome(), item.getPreco());
        }
        System.out.printf("Total: R$ %.2f\n", pedidoAtual.getTotal());
    }

    private void finalizarPedido() {
        if (pedidoAtual == null || pedidoAtual.getItens().isEmpty()) {
            System.out.println("Não há itens para finalizar o pedido.");
            return;
        }

        visualizarPedidoAtual();
        System.out.println("\nEscolha o método de pagamento:");
        System.out.println("1. Dinheiro");
        System.out.println("2. Cartão");
        System.out.print("Escolha uma opção: ");
        int opcaoPagamento = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        String metodoPagamento = opcaoPagamento == 1 ? "Dinheiro" : "Cartão";
        Pagamento pagamento = new PagamentoRestaurante(pedidoAtual, metodoPagamento);
        pagamento.processar();

        System.out.println("Pagamento realizado com sucesso!");
        pedidos.add(pedidoAtual);
        pedidoAtual = null; // Reiniciar o pedido atual
    }

    public static void main(String[] args) {
        TotemAutoatendimento totem = new TotemAutoatendimento();
        totem.iniciar();
    }
}
