package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

//import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @Before
    public void setUp() throws Exception {
        validCustomer = CustomerDto.builder()
                .ID(UUID.randomUUID())
                .name("Test Customer")
                .build();
    }


    @Test
    public void getCustomerTest() throws Exception {

        when(customerService.getCustomerById(any(UUID.class))).thenReturn(validCustomer);

        mockMvc.perform(get("/api/v1/customer/" + validCustomer.getID().toString()))
                .andExpect(status().isOk())
                /*.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id",is(validCustomer.getID().toString())))*/;
    }

    @Test
    public void handlePostTest() throws  Exception{

        validCustomer.setID(null);
        String newCustomerDtoJson = objectMapper.writeValueAsString(validCustomer);

        CustomerDto savedCustomerDto = CustomerDto.builder().ID(UUID.randomUUID()).name("New Customer").build();

        when(customerService.saveNewCustomer(any())).thenReturn(savedCustomerDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCustomerDtoJson))
                .andReturn();

        String expectedResult = String.format("{\"id\":%s,\"name\":\"%s\"}",savedCustomerDto.getID(),savedCustomerDto.getName());

        JSONAssert.assertEquals(expectedResult,mvcResult.getResponse().getContentAsString(),false);
    }

    @Test
    public void handleUpdateTest() throws Exception{

        String updateCustomerJson = objectMapper.writeValueAsString(validCustomer);

        mockMvc.perform(
                    put("/api/v1/customer/"+validCustomer.getID().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateCustomerJson))
                .andExpect(status().isNoContent());
    }
}
