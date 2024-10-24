package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    // Mapas dos menus
    public static final Map<Integer, String> MENU_PRINCIPAL = new HashMap<>();
    public static final Map<Integer, String> MENU_ENTIDADES = new HashMap<>();
    public static final Map<Integer, String> MENU_RELATORIOS = new HashMap<>();

    // Configurações de conexão ao banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/meu_banco_de_dados"; // Ajuste conforme necessário
    private static final String USER = "root"; // Ajuste conforme necessário
    private static final String PASSWORD = "root"; // Ajuste conforme necessário

    static {
        // Menu Principal
        MENU_PRINCIPAL.put(1, "Gerenciar Clientes");
        MENU_PRINCIPAL.put(2, "Gerenciar Pedidos");
        MENU_PRINCIPAL.put(3, "Gerenciar Produtos");
        MENU_PRINCIPAL.put(0, "Sair");

        // Menu Entidades
        MENU_ENTIDADES.put(1, "Inserir");
        MENU_ENTIDADES.put(2, "Atualizar");
        MENU_ENTIDADES.put(3, "Excluir");
        MENU_ENTIDADES.put(4, "Listar");
        MENU_ENTIDADES.put(0, "Voltar");

        // Menu Relatórios
        MENU_RELATORIOS.put(1, "Pedidos por Cliente");
        MENU_RELATORIOS.put(2, "Produtos mais vendidos");
        MENU_RELATORIOS.put(0, "Voltar");
    }

    // Método para obter a conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
