package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
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

public class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST LISTAR
    @Test
    void testListarProductos() {
        List<Producto> lista = List.of(
                new Producto(1L, "Monitor", 500, 10, "Tecnologia")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Producto> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNombre());

        verify(repo, times(1)).findAll();
    }

    // TEST GUARDAR
    @Test
    void testGuardarProducto() {
        Producto producto = new Producto(1L, "Laptop", 3000, 5, "Tecnologia");

        when(repo.save(producto)).thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());

        verify(repo).save(producto);
    }

    // TEST OBTENER
    @Test
    void testObtenerProducto() {
        Producto producto = new Producto(1L, "Teclado", 100, 20, "Perifericos");

        when(repo.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNombre());

        verify(repo).findById(1L);
    }

    // TEST OBTENER CUANDO NO EXISTE (Actividad propuesta - Parte 1)
    @Test
    void testObtenerProductoNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.obtener(99L);

        assertNull(resultado);

        verify(repo).findById(99L);
    }

    // TEST ACTUALIZAR (Actividad propuesta - Parte 1)
    @Test
    void testActualizarProducto() {
        Producto existente = new Producto(1L, "Monitor", 500, 10, "Tecnologia");
        Producto cambios = new Producto(null, "Monitor 4K", 700, 8, "Perifericos");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = service.actualizar(1L, cambios);

        assertNotNull(resultado);
        assertEquals("Monitor 4K", resultado.getNombre());
        assertEquals(700, resultado.getPrecio());
        assertEquals(8, resultado.getStock());
        assertEquals("Perifericos", resultado.getCategoria());

        verify(repo).findById(1L);
        verify(repo).save(existente);
    }

    // TEST ACTUALIZAR CUANDO NO EXISTE (Actividad propuesta - Parte 1)
    @Test
    void testActualizarProductoNoExiste() {
        Producto cambios = new Producto(null, "Monitor 4K", 700, 8, "Perifericos");

        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.actualizar(99L, cambios);

        assertNull(resultado);

        verify(repo).findById(99L);
        verify(repo, never()).save(any(Producto.class));
    }

    // TEST ELIMINAR
    @Test
    void testEliminarProducto() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}
