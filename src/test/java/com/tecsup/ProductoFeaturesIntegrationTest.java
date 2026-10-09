package com.tecsup;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class ProductoFeaturesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private void crear(String nombre, double precio, int stock, String categoria) throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"" + nombre + "\",\"precio\":" + precio
                                + ",\"stock\":" + stock + ",\"categoria\":\"" + categoria + "\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void buscarPorCategoria() throws Exception {
        crear("Laptop", 3000, 10, "Tecnologia");
        crear("Mouse", 50, 100, "Tecnologia");
        crear("Escritorio", 500, 5, "Muebles");

        mockMvc.perform(get("/api/productos/categoria/Tecnologia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void listarProductosBajoStock() throws Exception {
        crear("Teclado", 80, 3, "Tecnologia");
        crear("Monitor", 900, 50, "Tecnologia");

        mockMvc.perform(get("/api/productos/bajo-stock").param("minimo", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Teclado"));
    }
}
