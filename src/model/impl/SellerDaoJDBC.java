package model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection con;
	
	public SellerDaoJDBC(Connection con) {
		this.con = con;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("INSERT INTO seller " + 
					"(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
					"VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected  = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("UPDATE seller " + 
					"SET Name=?, Email=?, BirthDate=?, BaseSalary=?, DepartmentId=? " +
					"WHERE Id=?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null; 
		
		try {
			st = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " +
					"FROM seller INNER JOIN department " + 
					"ON seller.DepartmentId = department.Id " + 
					"WHERE seller.Id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller sel = instantiateSeller(rs, dep);
				return sel;
			}
			return null;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		return new Seller(
				rs.getInt("Id"),
				rs.getString("Name"),
				rs.getString("Email"),
				rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"),
				dep);
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +  
					"ORDER BY Name");
			
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<Seller>();
			
			Map<Integer, Department> map = new HashMap<>(); 
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				} 
							
				Seller sel = instantiateSeller(rs, dep);
				
				list.add(sel);
				
			}
			return list;
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +  
					"WHERE DepartmentId = ? " + 
					"ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<Seller>();
			
			Map<Integer, Department> map = new HashMap<>(); 
			
			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				} 
							
				Seller sel = instantiateSeller(rs, dep);
				
				list.add(sel);
				
			}
			return list;
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
		return null;
	}

}
