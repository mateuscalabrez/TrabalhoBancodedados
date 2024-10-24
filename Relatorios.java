package reports;

import java.sql.*;

public class Relatorios {
    private Connection conn;

    public void conectar() {
        String url = "jdbc:mysql://localhost:3306/meu_banco_de_dados";
        String user = "root";
        String password = "root";
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Conexão estabelecida com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void desconectar() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao desconectar do banco de dados: " + e.getMessage());
            }
        }
    }

    public void relatorioPedidosPorCliente() {
        conectar();
        String sql = "SELECT c.NOME, COUNT(p.PEDIDO_ID) AS NUM_PEDIDOS, SUM(p.VALOR_TOTAL) AS VALOR_TOTAL " +
                     "FROM CLIENTE c JOIN PEDIDO p ON c.CLIENTE_ID = p.CLIENTE_ID " +
                     "GROUP BY c.NOME ORDER BY NUM_PEDIDOS DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("Cliente: %s, Número de Pedidos: %d, Valor Total: %.2f%n",
                        rs.getString("NOME"),
                        rs.getInt("NUM_PEDIDOS"),
                        rs.getDouble("VALOR_TOTAL"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao executar relatório: " + e.getMessage());
        } finally {
            desconectar();
        }
    }
}
