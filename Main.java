package vendasOline;

import controle.ClienteControle;
import controle.PedidoControle;
import controle.ProductControl;
import utils.Config;
import utils.SplashScreen;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        try {
            conn = Config.getConnection(); 
            ClienteControle clienteControle = new ClienteControle(conn);
            PedidoControle pedidoControle = new PedidoControle(conn);
            ProductControl produtoControle = new ProductControl(conn);
            
            System.out.println(SplashScreen.getSplashScreen(conn));

            while (true) {
                Config.MENU_PRINCIPAL.forEach((k, v) -> System.out.println(k + ". " + v));
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        gerenciarClientes(clienteControle, scanner);
                        break;
                    case 2:
                        gerenciarPedidos(pedidoControle, scanner);
                        break;
                    case 3:
                        gerenciarProdutos(produtoControle, scanner);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o sistema: " + e.getMessage());
        } finally {
            // Fechar a conexão
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println("Erro ao fechar a conexão: " + e.getMessage());
                }
            }
            // scanner.close(); // Mantenha comentado se for usar o scanner novamente
        }
    }

    private static void gerenciarClientes(ClienteControle controller, Scanner scanner) {
        try {
            controller.conectar();
            while (true) {
                System.out.println("Gerenciar Clientes:");
                System.out.println("1. Inserir Cliente");
                System.out.println("2. Atualizar Cliente");
                System.out.println("3. Excluir Cliente");
                System.out.println("4. Listar Clientes");
                System.out.println("0. Voltar");

                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();

                switch (opcao) {
                    case 1: // Inserir Cliente
                        try {
                            scanner.nextLine(); // Limpa o buffer
                            System.out.print("Nome: ");
                            String nome = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Telefone: ");
                            String telefone = scanner.nextLine();
                            System.out.print("Endereço: ");
                            String endereco = scanner.nextLine();
                            controller.inserirCliente(new Cliente(0, nome, email, telefone, endereco)); // ID gerado automaticamente
                        } catch (Exception e) {
                            System.out.println("Erro ao inserir cliente: " + e.getMessage());
                        }
                        break;
                    case 2: // Atualizar Cliente
                        try {
                            System.out.print("ID do Cliente a ser atualizado: ");
                            int idAtualizar = scanner.nextInt();
                            Cliente cliente = controller.listarClientePorId(idAtualizar);
                            if (cliente != null) {
                                scanner.nextLine(); // Limpa o buffer
                                System.out.print("Novo Nome (deixe em branco para não alterar): ");
                                String novoNome = scanner.nextLine();
                                if (!novoNome.isEmpty()) {
                                    cliente.setNome(novoNome);
                                }
                                System.out.print("Novo Email (deixe em branco para não alterar): ");
                                String novoEmail = scanner.nextLine();
                                if (!novoEmail.isEmpty()) {
                                    cliente.setEmail(novoEmail);
                                }
                                System.out.print("Novo Telefone (deixe em branco para não alterar): ");
                                String novoTelefone = scanner.nextLine();
                                if (!novoTelefone.isEmpty()) {
                                    cliente.setTelefone(novoTelefone);
                                }
                                System.out.print("Novo Endereço (deixe em branco para não alterar): ");
                                String novoEndereco = scanner.nextLine();
                                if (!novoEndereco.isEmpty()) {
                                    cliente.setEndereco(novoEndereco);
                                }
                                controller.atualizarCliente(cliente);
                            } else {
                                System.out.println("Cliente não encontrado.");
                            }
                        } catch (Exception e) {
                            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
                        }
                        break;
                    case 3: // Excluir Cliente
                        try {
                            System.out.print("ID do Cliente a ser excluído: ");
                            int idExcluir = scanner.nextInt();
                            controller.excluirCliente(idExcluir);
                        } catch (Exception e) {
                            System.out.println("Erro ao excluir cliente: " + e.getMessage());
                        }
                        break;
                    case 4: // Listar Clientes
                        try {
                            controller.listarClientes();
                        } catch (Exception e) {
                            System.out.println("Erro ao listar clientes: " + e.getMessage());
                        }
                        break;
                    case 0: // Voltar
                        System.out.println(SplashScreen.getSplashScreen(controller.getConnection())); // Exibe o splash screen ao voltar
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao gerenciar clientes: " + e.getMessage());
        } finally {
            controller.desconectar();
        }
    }

    private static void gerenciarPedidos(PedidoControle controller, Scanner scanner) {
        while (true) {
            System.out.println("Gerenciar Pedidos:");
            System.out.println("1. Inserir Pedido");
            System.out.println("2. Atualizar Pedido");
            System.out.println("3. Excluir Pedido");
            System.out.println("4. Listar Pedidos");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1: // Inserir Pedido
                    try {
                        System.out.print("Data (YYYY-MM-DD): ");
                        LocalDate data = LocalDate.parse(scanner.nextLine());
                        System.out.print("Valor Total: ");
                        BigDecimal valorTotal = new BigDecimal(scanner.nextLine());
                        System.out.print("ID do Cliente: ");
                        int clienteId = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        Pedido novoPedido = new Pedido(0, data, valorTotal, clienteId);
                        controller.inserirPedido(novoPedido);
                    } catch (Exception e) {
                        System.out.println("Erro ao inserir pedido: " + e.getMessage());
                    }
                    break;
                case 2: // Atualizar Pedido
                    try {
                        System.out.print("ID do Pedido a ser atualizado: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        Pedido pedidoAtualizar = controller.listarPedidoPorId(id);
                        if (pedidoAtualizar != null) {
                            System.out.print("Nova Data (YYYY-MM-DD): ");
                            LocalDate data = LocalDate.parse(scanner.nextLine());
                            System.out.print("Novo Valor Total: ");
                            BigDecimal valorTotal = new BigDecimal(scanner.nextLine());
                            System.out.print("Novo ID do Cliente: ");
                            int clienteId = scanner.nextInt();
                            scanner.nextLine(); // Limpa o buffer
                            pedidoAtualizar.setData(data);
                            pedidoAtualizar.setValorTotal(valorTotal);
                            pedidoAtualizar.setClienteId(clienteId);
                            controller.atualizarPedido(pedidoAtualizar);
                        } else {
                            System.out.println("Pedido não encontrado.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao atualizar pedido: " + e.getMessage());
                    }
                    break;
                case 3: // Excluir Pedido
                    try {
                        System.out.print("ID do Pedido a ser excluído: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        controller.excluirPedido(id);
                    } catch (Exception e) {
                        System.out.println("Erro ao excluir pedido: " + e.getMessage());
                    }
                    break;
                case 4: // Listar Pedidos
                    try {
                        controller.listarPedidos();
                    } catch (Exception e) {
                        System.out.println("Erro ao listar pedidos: " + e.getMessage());
                    }
                    break;
                case 0: // Voltar
                    System.out.println(SplashScreen.getSplashScreen(controller.getConnection())); // Exibe o splash screen ao voltar
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }


    private static void gerenciarProdutos(ProductControl controller, Scanner scanner) {
        while (true) {
            System.out.println("\nGerenciar Produtos:");
            System.out.println("1. Inserir Produto");
            System.out.println("2. Atualizar Produto");
            System.out.println("3. Excluir Produto");
            System.out.println("4. Listar Produtos");
            System.out.println("0. Voltar");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1: // Inserir Produto
                    try {
                        System.out.print("Nome do Produto: ");
                        String nome = scanner.nextLine();
                        System.out.print("Descrição: ");
                        String descricao = scanner.nextLine();
                        System.out.print("Preço: ");
                        BigDecimal preco = new BigDecimal(scanner.nextLine()); // Lê como string e converte
                        System.out.print("Quantidade em Estoque: ");
                        int quantidadeEstoque = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        controller.inserirProduto(new Product(0, nome, descricao, preco, quantidadeEstoque)); // ID gerado automaticamente
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Preço inválido. Por favor, insira um número válido.");
                    } catch (Exception e) {
                        System.out.println("Erro ao inserir produto: " + e.getMessage());
                    }
                    break;
                case 2: // Atualizar Produto
                    try {
                        System.out.print("ID do Produto a ser atualizado: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        Product produtoAtualizar = controller.listarProdutoPorId(id);
                        if (produtoAtualizar != null) {
                            System.out.print("Novo Nome (deixe em branco para não alterar): ");
                            String novoNome = scanner.nextLine();
                            if (!novoNome.isEmpty()) {
                                produtoAtualizar.setNome(novoNome);
                            }
                            System.out.print("Nova Descrição (deixe em branco para não alterar): ");
                            String novaDescricao = scanner.nextLine();
                            if (!novaDescricao.isEmpty()) {
                                produtoAtualizar.setDescricao(novaDescricao);
                            }
                            System.out.print("Novo Preço (deixe em branco para não alterar): ");
                            String precoInput = scanner.nextLine();
                            if (!precoInput.isEmpty()) {
                                produtoAtualizar.setPreco(new BigDecimal(precoInput));
                            }
                            System.out.print("Nova Quantidade em Estoque (deixe em branco para não alterar): ");
                            String quantidadeInput = scanner.nextLine();
                            if (!quantidadeInput.isEmpty()) {
                                produtoAtualizar.setQuantidadeEstoque(Integer.parseInt(quantidadeInput));
                            }
                            controller.atualizarProduto(produtoAtualizar);
                        } else {
                            System.out.println("Produto não encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Quantidade inválida. Por favor, insira um número válido.");
                    } catch (Exception e) {
                        System.out.println("Erro ao atualizar produto: " + e.getMessage());
                    }
                    break;
                case 3: // Excluir Produto
                    try {
                        System.out.print("ID do Produto a ser excluído: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer
                        controller.excluirProduto(id);
                    } catch (Exception e) {
                        System.out.println("Erro ao excluir produto: " + e.getMessage());
                    }
                    break;
                case 4: // Listar Produtos
                    try {
                        controller.listarProdutos();
                    } catch (Exception e) {
                        System.out.println("Erro ao listar produtos: " + e.getMessage());
                    }
                    break;
                case 0: // Voltar
                    System.out.println(SplashScreen.getSplashScreen(controller.getConnection())); // Exibe o splash screen ao voltar
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

}