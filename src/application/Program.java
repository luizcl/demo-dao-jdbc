package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TESTE 1: seller findById ===");
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n=== TESTE 2: seller findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		list.forEach(System.out::println);
		
		System.out.println("\n=== TESTE 3: seller findAll ===");
		list = sellerDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n=== TESTE 4: Insert Seller ===");
		Seller s = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(s);
		System.out.println(s);
		
		System.out.println("\n=== TESTE 5: Update Seller ===");
		s = sellerDao.findById(1);
		s.setName("Martha Waine");
		sellerDao.update(s);
		System.out.println(s);
		
		System.out.println("\n=== TESTE 6: Delete Seller ===");
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter if for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		sc.close();
		
		System.out.println("\n======= Finished ======");
	}

}