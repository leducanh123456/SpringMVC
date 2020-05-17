package com.laptrinhjavaweb.oop;

import java.lang.reflect.Field;

import com.laptrinhjavaweb.annotation.Column;
import com.laptrinhjavaweb.annotation.Table;
import com.laptrinhjavaweb.repository.impl.SimpleJpaRepository;

public class FieldDemo {

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

//		Field field = SampleClass.class.getField("sampleField2");
		
		SampleClass sampleClass = new SampleClass();
		repoClass repoClass = new repoClass();
		
		repoClass.update(sampleClass);
	}
}

@Table(name = "ducanh")
class SampleClass {
	@Column
	private  Long sampleField ;
	@Column
	private  Long sampleField2 = 3L ;
}

class repoClass extends SimpleJpaRepository<SampleClass, Long>{
	
}