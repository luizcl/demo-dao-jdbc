package model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection con;
	
	public DepartmentDaoJDBC(Connection con) {
		this.con = con;
	}

	
	@Override
	public void insert(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			String sql = "INSERT INTO department " +
						"(Name) VALUES (?)";
			st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}
				DB.closeResultSet(rs);
			}
			
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = con.prepareStatement("UPDATE department " +
									"SET Name=? " +
									"WHERE id=?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			
			String sql = "DELETE "
					+ "FROM department "
					+ "WHERE id = ?";
			st = con.prepareStatement(sql);
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = con.prepareStatement("SELECT * " +
									"FROM department " +
									"WHERE id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()) {
				return instantiateDepartment(rs);
			} else {
				throw new DbException("No rows were find");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = con.prepareStatement("SELECT * FROM department");
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			
			while(rs.next()) {
				
				list.add(instantiateDepartment(rs));
				
			}
			
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		return new Department(rs.getInt("Id"), rs.getString("Name"));
	}

}
