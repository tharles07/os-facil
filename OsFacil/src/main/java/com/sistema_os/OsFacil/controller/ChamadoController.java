package com.sistema_os.OsFacil.controller;

import com.sistema_os.OsFacil.model.Chamado;
import com.sistema_os.OsFacil.service.ChamadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    /* ==========================
       CRIAR CHAMADO
    ========================== */
    @PostMapping
    public Chamado criar(@RequestBody Chamado chamado) {
        return service.salvar(chamado);
    }

    /* ==========================
       LISTAR (POR EMPRESA AUTOMÁTICO)
    ========================== */
    @GetMapping
    public List<Chamado> listar() {
        return service.listarPorEmpresa();
    }

    /* ==========================
       BUSCAR POR ID (COM SEGURANÇA)
    ========================== */
    @GetMapping("/{id}")
    public Chamado buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdSeguro(id);
    }

    /* ==========================
       FINALIZAR CHAMADO
    ========================== */
    @PutMapping("/{id}/finalizar")
    public Chamado finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }

    /* ==========================
       ATUALIZAR CHAMADO
    ========================== */
    @PutMapping("/{id}")
    public Chamado atualizar(@PathVariable Long id,
                              @RequestBody Chamado chamado) {
        return service.atualizar(id, chamado);
    }

    /* ==========================
       EXCLUIR CHAMADO (ADMIN + EMPRESA)
    ========================== */
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}