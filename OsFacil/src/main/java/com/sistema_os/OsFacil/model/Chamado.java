package com.sistema_os.OsFacil.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private String status; // ABERTO, FINALIZADO, ATRASADO

    private LocalDate data;

    private Double valor;

    private Boolean pago;

    /* ==========================
       RELACIONAMENTO CLIENTE
    ========================== */
    @ManyToOne
    @JoinColumn(
            name = "cliente_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_chamado_cliente")
    )
    private Cliente cliente;

    /* ==========================
       MULTI-TENANT (SAAS)
    ========================== */
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    /* ==========================
       GETTERS E SETTERS
    ========================== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
