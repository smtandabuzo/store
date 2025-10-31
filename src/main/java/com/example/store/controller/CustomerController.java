package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CustomerDTO>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<CustomerDTO> pagedAssembler) {
        
        Page<CustomerDTO> customers = customerRepository
                .findAll(PageRequest.of(page, size))
                .map(customerMapper::customerToCustomerDTO);
                
        PagedModel<EntityModel<CustomerDTO>> pagedModel = pagedAssembler.toModel(customers);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody Customer customer) {
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @GetMapping("/search")
    public List<CustomerDTO> searchCustomers(@RequestParam String name) {
        return customerMapper.customersToCustomerDTOs(customerRepository.findByNameContainingIgnoreCase(name));
    }
}
