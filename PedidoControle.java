package controle;

import java.math.BigDecimal;
import java.sql.*;
import vendasOline.Pedido;

public class PedidoControle {
    private Connection conn;

    public PedidoControle(Connection conn) {
        this.conn = conn; // Usa a conexão passada como parâmetro
    }

    public void conectar() {
        String url = "jdbc:mysql://localhost:3306/meu_banco_de_dados"; // Ajuste a URL conforme necessário
        String user = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado ao banco de dados com sucesso.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
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

    public Connection getConnection() {
        return conn; // Método para retornar a conexão ativa
    }

    // Métodos para inserir, atualizar, excluir e listar pedidos...

    public int contarPedidos() {
        String sql = "SELECT COUNT(*) AS TOTAL FROM PEDIDO";
        int total = 0;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                total = rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar pedidos: " + e.getMessage());
        }
        return total; // Retorna o total de pedidos sem fechar a conexão
    }

    public void inserirPedido(Pedido pedido) {
        String sql = "INSERT INTO PEDIDO(PEDIDO_ID, DATA, VALOR_TOTAL, CLIENTE_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedido.getId());
            pstmt.setDate(2, java.sql.Date.valueOf(pedido.getData())); // Converter LocalDate para java.sql.Date
            pstmt.setBigDecimal(3, pedido.getValorTotal());
            pstmt.setInt(4, pedido.getClienteId());
            pstmt.executeUpdate();
            System.out.println("Pedido inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir pedido: " + e.getMessage());
        }
        // Não fechar a conexão aqui
    }

    public void atualizarPedido(Pedido pedido) {
        conectar();
        String sql = "UPDATE PEDIDO SET DATA = ?, VALOR_TOTAL = ?, CLIENTE_ID = ? WHERE PEDIDO_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(pedido.getData())); // Converter LocalDate para java.sql.Date
            pstmt.setBigDecimal(2, pedido.getValorTotal());
            pstmt.setInt(3, pedido.getClienteId());
            pstmt.setInt(4, pedido.getId());
            pstmt.executeUpdate();
            System.out.println("Pedido atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pedido: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    public void excluirPedido(int pedidoId) {
        conectar();
        String sql = "DELETE FROM PEDIDO WHERE PEDIDO_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            pstmt.executeUpdate();
            System.out.println("Pedido excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir pedido: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    public void listarPedidos() {
        conectar();
        String sql = "SELECT * FROM PEDIDO";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("PEDIDO_ID");
                Date data = rs.getDate("DATA");
                BigDecimal valorTotal = rs.getBigDecimal("VALOR_TOTAL");
                int clienteId = rs.getInt("CLIENTE_ID");
                System.out.println(new Pedido(id, data.toLocalDate(), valorTotal, clienteId));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        } finally {
            desconectar();
        }
    }
    
    

    public Pedido listarPedidoPorId(int pedidoId) {
        conectar();
        String sql = "SELECT * FROM PEDIDO WHERE PEDIDO_ID = ?";
        Pedido pedido = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("PEDIDO_ID");
                Date data = rs.getDate("DATA");
                BigDecimal valorTotal = rs.getBigDecimal("VALOR_TOTAL");
                int clienteId = rs.getInt("CLIENTE_ID");
                pedido = new Pedido(id, data.toLocalDate(), valorTotal, clienteId);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pedido por ID: " + e.getMessage());
        } finally {
            desconectar();
        }
        return pedido;
    }
}
