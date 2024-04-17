import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Use @BeforeEach to set up any prerequisites for each test method
    @BeforeEach
    public void setUp() {
        // You can set up any initial data or configurations needed for testing here
    }

    @Test
    public void testGetByNumber() throws Exception {
        mockMvc.perform(get("/account/{number}", 123456789)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/account")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"John Doe\", \"number\": 123456789, \"specialLimit\": 1000.0}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        mockMvc.perform(put("/account/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jane Doe\", \"number\": 987654321, \"specialLimit\": 500.0}"))
                .andExpect(status().isOk());
    }
}
