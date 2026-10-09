package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarCategorias() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Tecnologia", "Productos electronicos")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Categoria> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Tecnologia", resultado.get(0).getNombre());

        verify(repo, times(1)).findAll();
    }

    @Test
    void testGuardarCategoria() {
        Categoria categoria = new Categoria(1L, "Muebles", "Muebles de oficina");

        when(repo.save(categoria)).thenReturn(categoria);

        Categoria resultado = service.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Muebles", resultado.getNombre());

        verify(repo).save(categoria);
    }

    @Test
    void testObtenerCategoria() {
        Categoria categoria = new Categoria(1L, "Accesorios", "Accesorios varios");

        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Accesorios", resultado.getNombre());

        verify(repo).findById(1L);
    }

    @Test
    void testObtenerCategoriaNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = service.obtener(99L);

        assertNull(resultado);

        verify(repo).findById(99L);
    }

    @Test
    void testActualizarCategoria() {
        Categoria existente = new Categoria(1L, "Tecnologia", "Productos electronicos");
        Categoria cambios = new Categoria(null, "Tecnologia", "Equipos de computo");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Categoria resultado = service.actualizar(1L, cambios);

        assertNotNull(resultado);
        assertEquals("Tecnologia", resultado.getNombre());
        assertEquals("Equipos de computo", resultado.getDescripcion());

        verify(repo).findById(1L);
        verify(repo).save(existente);
    }

    @Test
    void testActualizarCategoriaNoExiste() {
        Categoria cambios = new Categoria(null, "Tecnologia", "Equipos de computo");

        when(repo.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = service.actualizar(99L, cambios);

        assertNull(resultado);

        verify(repo).findById(99L);
        verify(repo, never()).save(any(Categoria.class));
    }

    @Test
    void testEliminarCategoria() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        List<Categoria> lista = List.of(
                new Categoria(1L, "Tecnologia", "Productos electronicos")
        );

        when(repo.findByNombreContainingIgnoreCase("tec")).thenReturn(lista);

        List<Categoria> resultado = service.buscarPorNombre("tec");

        assertEquals(1, resultado.size());
        assertEquals("Tecnologia", resultado.get(0).getNombre());

        verify(repo).findByNombreContainingIgnoreCase("tec");
    }
}
