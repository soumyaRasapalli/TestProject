package com.pro;

import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee,Employee> {

	@Override
	public Employee process(Employee empl) throws Exception {
		System.out.println("Inserting employee : " + empl);
		return empl;
	}
	
	

}
