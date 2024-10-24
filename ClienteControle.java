package controle;

import java.sql.*;
import vendasOline.Cliente;

public class ClienteControle {
    private Connection conn;

    public ClienteControle(Connection conn) {
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

    public int contarClientes() {
        conectar();
        String sql = "SELECT COUNT(*) AS TOTAL FROM CLIENTE";
        int total = 0;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                total = rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar clientes: " + e.getMessage());
        } finally {
            desconectar();
        }
        return total;
    }
    
    public void inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE(NOME, EMAIL, TELEFONE, ENDERECO) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getTelefone());
            pstmt.setString(4, cliente.getEndereco());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                cliente.setId(rs.getInt(1)); // Define o ID gerado para o cliente
            }

            System.out.println("Cliente inserido com sucesso! ID: " + cliente.getId());
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }


    public void atualizarCliente(Cliente cliente) {
        String sql = "UPDATE CLIENTE SET NOME = ?, EMAIL = ?, TELEFONE = ?, ENDERECO = ? WHERE CLIENTE_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getTelefone());
            pstmt.setString(4, cliente.getEndereco());
            pstmt.setInt(5, cliente.getId());
            pstmt.executeUpdate();
            System.out.println("Cliente atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public void excluirCliente(int clienteId) {
        String sql = "DELETE FROM CLIENTE WHERE CLIENTE_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
            System.out.println("Cliente excluído com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    public void listarClientes() {
        String sql = "SELECT * FROM CLIENTE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("CLIENTE_ID");
                String nome = rs.getString("NOME");
                String email = rs.getString("EMAIL");
                String telefone = rs.getString("TELEFONE");
                String endereco = rs.getString("ENDERECO");
                System.out.println(new Cliente(id, nome, email, telefone, endereco));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
    }

    public Cliente listarClientePorId(int clienteId) {
        String sql = "SELECT * FROM CLIENTE WHERE CLIENTE_ID = ?";
        Cliente cliente = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("CLIENTE_ID");
                String nome = rs.getString("NOME");
                String email = rs.getString("EMAIL");
                String telefone = rs.getString("TELEFONE");
                String endereco = rs.getString("ENDERECO");
                cliente = new Cliente(id, nome, email, telefone, endereco);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar cliente por ID: " + e.getMessage());
        }
        return cliente;
    }
}

