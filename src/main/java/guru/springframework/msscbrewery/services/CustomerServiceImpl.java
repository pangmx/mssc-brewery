package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder().ID(UUID.randomUUID()).name("Mary Jane").build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        return  CustomerDto.builder()
                .ID(UUID.randomUUID())
                .name(customerDto.getName())
                .build();
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        log.debug("Updating customer by Id: "+customerId.toString());
    }

    @Override
    public void deleteById(UUID customerId) {
        log.debug("Deleting customer by Id: "+customerId.toString());
    }
}
