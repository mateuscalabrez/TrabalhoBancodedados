package controle;

import java.sql.*;
import vendasOline.Product;

public class ProductControl {
    private Connection conn;

    public ProductControl(Connection conn) {
        this.conn = conn; // Usa a conexão passada como parâmetro
    }

    public void conectar() {
        String url = "jdbc:mysql://localhost:3306/meu_banco_de_dados"; // Ajuste a URL conforme necessário
        String user = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carrega o driver
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado ao banco de dados com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado: " + e.getMessage());
        }
    }
    
    public Connection getConnection() {
        return conn; // Retorna a conexão ativa
    }

    public void desconectar() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Desconectado do banco de dados com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao desconectar do banco de dados: " + e.getMessage());
            }
        }
    }

    public int contarProdutos() {
        String sql = "SELECT COUNT(*) AS TOTAL FROM PRODUTO";
        int total = 0;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                total = rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar produtos: " + e.getMessage());
        }
        return total; // Retorna o total de produtos sem fechar a conexão
    }

    public void inserirProduto(Product produto) {
        String sql = "INSERT INTO PRODUTO(NOME, DESCRICAO, PRECO, QUANTIDADE_ESTOQUE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidadeEstoque());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                produto.setId(rs.getInt(1)); // Define o ID gerado para o produto
            }
            System.out.println("Produto inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
        // Não fechar a conexão aqui
    }

    public void atualizarProduto(Product produto) {
        String sql = "UPDATE PRODUTO SET NOME = ?, DESCRICAO = ?, PRECO = ?, QUANTIDADE_ESTOQUE = ? WHERE PRODUTO_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidadeEstoque());
            pstmt.setInt(5, produto.getId());
            pstmt.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
        // Não fechar a conexão aqui
    }

    public void excluirProduto(int produtoId) {
        String sql = "DELETE FROM PRODUTO WHERE PRODUTO_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoId);
            pstmt.executeUpdate();
            System.out.println("Produto excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
        // Não fechar a conexão aqui
    }

    public void listarProdutos() {
        String sql = "SELECT * FROM PRODUTO";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(new Product(
                    rs.getInt("PRODUTO_ID"),
                    rs.getString("NOME"),
                    rs.getString("DESCRICAO"),
                    rs.getBigDecimal("PRECO"),
                    rs.getInt("QUANTIDADE_ESTOQUE")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        // Não fechar a conexão aqui
    }

    public Product listarProdutoPorId(int produtoId) {
        String sql = "SELECT * FROM PRODUTO WHERE PRODUTO_ID = ?";
        Product produto = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                produto = new Product(
                    rs.getInt("PRODUTO_ID"),
                    rs.getString("NOME"),
                    rs.getString("DESCRICAO"),
                    rs.getBigDecimal("PRECO"),
                    rs.getInt("QUANTIDADE_ESTOQUE")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produto por ID: " + e.getMessage());
        }
        return produto; // Retorna o produto encontrado
    }
}
