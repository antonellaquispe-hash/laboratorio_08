package com.tecsup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.dto.CategoriaDTO;
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
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String crearCategoria(String nombre, String descripcion) throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);

        String cuerpo = mockMvc.perform(post("/api/categorias")
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
    void testGuardarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Tecnologia");
        dto.setDescripcion("Productos electronicos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"))
                .andExpect(jsonPath("$.descripcion").value("Productos electronicos"))
                .andExpect(jsonPath("$.id").exists());
    }

    // TEST GET LIST
    @Test
    void testListarCategorias() throws Exception {
        crearCategoria("Muebles", "Muebles de oficina");

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // TEST GET BY ID
    @Test
    void testObtenerCategoriaPorId() throws Exception {
        String id = crearCategoria("Accesorios", "Accesorios varios");

        mockMvc.perform(get("/api/categorias/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Accesorios"));
    }

    // TEST GET BY ID INEXISTENTE -> 404
    @Test
    void testObtenerCategoriaInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/categorias/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    // TEST BUSCAR POR NOMBRE
    @Test
    void testBuscarPorNombre() throws Exception {
        crearCategoria("Tecnologia", "Productos electronicos");

        mockMvc.perform(get("/api/categorias/buscar").param("nombre", "tec"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Tecnologia"));
    }

    // TEST PUT
    @Test
    void testActualizarCategoria() throws Exception {
        String id = crearCategoria("Tecnologia", "Productos electronicos");

        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Tecnologia");
        dto.setDescripcion("Equipos de computo");

        mockMvc.perform(put("/api/categorias/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Equipos de computo"));
    }

    // TEST PUT INEXISTENTE -> 404
    @Test
    void testActualizarCategoriaInexistenteDevuelve404() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Fantasma");
        dto.setDescripcion("No existe");

        mockMvc.perform(put("/api/categorias/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE
    @Test
    void testEliminarCategoria() throws Exception {
        String id = crearCategoria("Temporal", "Categoria temporal");

        mockMvc.perform(delete("/api/categorias/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categorias/" + id))
                .andExpect(status().isNotFound());
    }

    // TEST DELETE INEXISTENTE -> 404
    @Test
    void testEliminarCategoriaInexistenteDevuelve404() throws Exception {
        mockMvc.perform(delete("/api/categorias/99999"))
                .andExpect(status().isNotFound());
    }

    // TEST VALIDACIÓN
    @Test
    void testValidacionCategoriaInvalida() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("A");
        dto.setDescripcion("");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre debe tener entre 3 y 50 caracteres"))
                .andExpect(jsonPath("$.descripcion").value("La descripcion es obligatoria"));
    }
}
