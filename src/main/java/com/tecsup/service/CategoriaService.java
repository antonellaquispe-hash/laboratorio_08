package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Categoria guardar(Categoria c) {
        return repo.save(c);
    }

    public Categoria obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Categoria actualizar(Long id, Categoria c) {
        Categoria existente = repo.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setNombre(c.getNombre());
        existente.setDescripcion(c.getDescripcion());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
