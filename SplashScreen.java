package utils;

import java.sql.*;

public class SplashScreen {
    private static final String CREATED_BY = "Gabriel Nascimento, Henrique Gonçalves, Arthur de Castro, Igor Pena, Mateus Calabrez";
    private static final String PROFESSOR = "Howard Roatti";
    private static final String DISCIPLINA = "Banco de Dados";
    private static final String SEMESTRE = "2024/2";

    // Método auxiliar para contar registros
    private static int contarRegistros(Connection conn, String tabela) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM " + tabela;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar " + tabela + ": " + e.getMessage());
        }
        return count;
    }

    public static String getSplashScreen(Connection conn) {
        int countCliente = contarRegistros(conn, "CLIENTE");
        int countPedido = contarRegistros(conn, "PEDIDO");
        int countProduto = contarRegistros(conn, "PRODUTO");

        return String.format("""
            ################################################
            #                                              #
            #         SISTEMA DE PEDIDOS E PRODUTOS        #
            #                                              #
            ################################################
            #                   CONTAGENS                  #
            #                                              #
            #  TOTAL DE CLIENTES:    %5d                   #
            #  TOTAL DE PEDIDOS:     %5d                   #
            #  TOTAL DE PRODUTOS:    %5d                   #
            ################################################
            # CRIADO POR: %s
            # PROFESSOR:  %s
            # DISCIPLINA: %s
            # SEMESTRE:   %s
            ################################################
            """, countCliente, countPedido, countProduto, CREATED_BY, PROFESSOR, DISCIPLINA, SEMESTRE);
    }
}
