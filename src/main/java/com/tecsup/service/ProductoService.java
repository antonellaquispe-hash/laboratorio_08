package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto guardar(Producto p) {
        return repo.save(p);
    }

    public Producto obtener(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public Producto actualizar(Long id, Producto p) {
        Producto existente = repo.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setNombre(p.getNombre());
        existente.setPrecio(p.getPrecio());
        existente.setStock(p.getStock());
        existente.setCategoria(p.getCategoria());
        return repo.save(existente);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return repo.findByCategoriaIgnoreCase(categoria);
    }

    public List<Producto> listarBajoStock(int minimo) {
        return repo.findByStockLessThanEqual(minimo);
    }

    public List<Producto> buscarPorRangoPrecio(double min, double max) {
        return repo.findByPrecioBetween(min, max);
    }
}