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

    @PostMapping
    public Chamado criar(@RequestBody Chamado chamado){
        return service.salvar(chamado);
    }

    @GetMapping
    public List<Chamado> listar(){
        return service.listar();
    }

    @PutMapping("/{id}/finalizar")
    public Chamado finalizar(@PathVariable Long id){
        return service.finalizar(id);
    }
}