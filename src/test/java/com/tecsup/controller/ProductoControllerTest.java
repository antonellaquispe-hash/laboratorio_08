package com.tecsup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.dto.ProductoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String crearProducto(String nombre, double precio, int stock, String categoria) throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre(nombre);
        dto.setPrecio(precio);
        dto.setStock(stock);
        dto.setCategoria(categoria);

        String cuerpo = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(nombre))
                .andReturn().getResponse().getContentAsString();

        JsonNode nodo = objectMapper.readTree(cuerpo);
        return nodo.get("id").asText();
    }

    // TEST POST
    @Test
    void testGuardarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setPrecio(80);
        dto.setStock(20);
        dto.setCategoria("Perifericos");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mouse"))
                .andExpect(jsonPath("$.precio").value(80.0))
                .andExpect(jsonPath("$.stock").value(20));
    }

    // TEST GET
    @Test
    void testListarProductos() throws Exception {
        crearProducto("Teclado", 100, 15, "Perifericos");

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // TEST PUT /api/productos/{id} (Actividad propuesta - Parte 1)
    @Test
    void testActualizarProducto() throws Exception {
        String id = crearProducto("Monitor", 500, 10, "Tecnologia");

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Monitor 4K");
        dto.setPrecio(900);
        dto.setStock(4);
        dto.setCategoria("Perifericos");

        mockMvc.perform(put("/api/productos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
                .andExpect(jsonPath("$.nombre").value("Monitor 4K"))
                .andExpect(jsonPath("$.precio").value(900.0))
                .andExpect(jsonPath("$.stock").value(4))
                .andExpect(jsonPath("$.categoria").value("Perifericos"));
    }

    // TEST PUT producto inexistente -> 404 (Actividad propuesta - Parte 1)
    @Test
    void testActualizarProductoInexistenteDevuelve404() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Fantasma");
        dto.setPrecio(10);
        dto.setStock(1);
        dto.setCategoria("Ninguna");

        mockMvc.perform(put("/api/productos/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE /api/productos/{id} (Actividad propuesta - Parte 1)
    @Test
    void testEliminarProducto() throws Exception {
        String id = crearProducto("Cable HDMI", 25, 50, "Accesorios");

        mockMvc.perform(delete("/api/productos/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/productos/" + id))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE producto inexistente -> 404 (Actividad propuesta - Parte 1)
    @Test
    void testEliminarProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(delete("/api/productos/99999"))
                .andExpect(status().isNotFound());
    }

    // TEST VALIDACIÓN
    @Test
    void testValidacionNombreVacio() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
