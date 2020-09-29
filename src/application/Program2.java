package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TESTE 1: department findById ===");
		Department dep = departmentDao.findById(1);
		System.out.println(dep);
		
		System.out.println("=== TESTE 2: department findAll ===");
		List<Department> list = departmentDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("=== TESTE 3: Insert department ===");
		dep.setName("Música");
		//departmentDao.insert(dep);
		
		System.out.println("=== TESTE 4: Update department ===");
		dep.setId(5);
		dep.setName("Outros");
		departmentDao.update(dep);
		
		System.out.println("=== TESTE 4: Delete department ===");
		departmentDao.deleteById(8);
		
		System.out.println("=== TESTE 2: department findAll ===");
		list = departmentDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n======= Finished ======");
	}
	
}
