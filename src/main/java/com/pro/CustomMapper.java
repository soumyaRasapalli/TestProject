package com.pro;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomMapper implements FieldSetMapper<Employee> {

	@Override
	public Employee mapFieldSet(FieldSet fs) throws BindException {
		Employee employee=new Employee();
		try {
			employee.setId(fs.readInt("Id"));
			employee.setName(fs.readString("Name"));
			employee.setAge(fs.readInt("Age"));
			employee.setProfession(fs.readString("Profession"));
			
		}catch(Exception e){
			System.out.println("Something went wrong.");
		}
		return employee;
	}

}
