package org.benek.empleadoservice;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void retornaListaEmpleados() {
        ResponseEntity<String> response = restTemplate.getForEntity("/empleados", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int empleadosCount = documentContext.read("$.length()");
        assertThat(empleadosCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1, 2, 3);

        JSONArray nombres = documentContext.read("$..nombre");
        assertThat(nombres).containsExactlyInAnyOrder("Javier Ramirez", "Juan Rodriguez", "Blanca Pardo");
    }

    @Test
    void retornaUnEmpleadoPorId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/empleados/3", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(3);

        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Blanca Pardo");

        String puesto = documentContext.read("$.puesto");
        assertThat(puesto).isEqualTo("Project Manager");
    }
}
