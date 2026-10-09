package com.tecsup.controller;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Categoria;
import com.tecsup.service.CategoriaService;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    // GET /api/categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // GET /api/categorias/buscar?nombre=Tec
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    // GET /api/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        Categoria c = service.obtener(id);
        if (c == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        return ResponseEntity.ok(c);
    }

    // POST /api/categorias
    @PostMapping
    public ResponseEntity<Categoria> guardar(@Valid @RequestBody CategoriaDTO dto) {
        Categoria c = new Categoria();
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        Categoria guardada = service.guardar(c);
        return ResponseEntity.status(201).body(guardada);
    }

    // PUT /api/categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody CategoriaDTO dto) {
        Categoria datos = new Categoria();
        datos.setNombre(dto.getNombre());
        datos.setDescripcion(dto.getDescripcion());
        Categoria actualizada = service.actualizar(id, datos);
        if (actualizada == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Categoria c = service.obtener(id);
        if (c == null) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        service.eliminar(id);
        return ResponseEntity.ok("Categoria eliminada correctamente");
    }
}
