package com.laptrinhjavaweb.repository.impl;

import com.laptrinhjavaweb.entity.CustomerEntity;
import com.laptrinhjavaweb.repository.ICustomerRepository;

public class CustomerRepository extends SimpleJpaRepository<CustomerEntity, Long> implements ICustomerRepository {

}
