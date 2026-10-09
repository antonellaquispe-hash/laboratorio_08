package com.tecsup;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String crearProducto(String nombre, double precio, int stock, String categoria) throws Exception {
        String cuerpo = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"" + nombre + "\",\"precio\":" + precio
                                + ",\"stock\":" + stock + ",\"categoria\":\"" + categoria + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode nodo = objectMapper.readTree(cuerpo);
        return nodo.get("id").asText();
    }

    @Test
    void crearProductoDevuelve201() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Monitor\",\"precio\":50,\"stock\":20,\"categoria\":\"Tecnologia\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Monitor"))
                .andExpect(jsonPath("$.precio").value(50.0))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoria").value("Tecnologia"));
    }

    @Test
    void listarProductosDevuelve200() throws Exception {
        crearProducto("Monitor", 50, 20, "Tecnologia");

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void buscarPorNombreParcialDevuelve200() throws Exception {
        crearProducto("Monitor", 50, 20, "Tecnologia");

        mockMvc.perform(get("/api/productos/buscar").param("nombre", "mon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Monitor"));
    }

    @Test
    void validacionRechazaProductoInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"precio\":0,\"stock\":-1,\"categoria\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.precio").value("El precio debe ser mayor a 0"))
                .andExpect(jsonPath("$.stock").value("El stock no puede ser negativo"))
                .andExpect(jsonPath("$.categoria").value("La categoria es obligatoria"));
    }

    @Test
    void obtenerProductoPorIdDevuelve200() throws Exception {
        String id = crearProducto("Monitor", 50, 20, "Tecnologia");

        mockMvc.perform(get("/api/productos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
                .andExpect(jsonPath("$.nombre").value("Monitor"));
    }

    @Test
    void obtenerProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/productos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarProductoDevuelve200() throws Exception {
        String id = crearProducto("Monitor", 50, 20, "Tecnologia");

        mockMvc.perform(put("/api/productos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Monitor actualizado\",\"precio\":50,\"stock\":20,\"categoria\":\"Perifericos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor actualizado"))
                .andExpect(jsonPath("$.categoria").value("Perifericos"));
    }

    @Test
    void actualizarProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(put("/api/productos/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Fantasma\",\"precio\":10,\"stock\":1,\"categoria\":\"Ninguna\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProductoDevuelve200() throws Exception {
        String id = crearProducto("Monitor", 50, 20, "Tecnologia");

        mockMvc.perform(delete("/api/productos/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/productos/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(delete("/api/productos/99999"))
                .andExpect(status().isNotFound());
    }
}