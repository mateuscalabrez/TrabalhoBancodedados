package vendasOline;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pedido {
    private int id;
    private LocalDate data;
    private BigDecimal valorTotal;
    private int clienteId;

    public Pedido(int id, LocalDate data, BigDecimal valorTotal, int clienteId) {
        this.id = id;
        this.data = data;
        this.valorTotal = valorTotal;
        this.clienteId = clienteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", data=" + data +
                ", valorTotal=" + valorTotal +
                ", clienteId=" + clienteId +
                '}';
    }
}
