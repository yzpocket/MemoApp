package com.multicamp;
import java.sql.*;
import java.util.*;
//Data Access Object : 모델에 속하며, Persistence 계층이라고 부른다.
//DAO(Data Access Object, 실제적으로 DB에 접근하는 객체) 클래스   [Persistence Layer] 
public class MemoDAO {
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	
	/** DB접근해서 인서트하는 메서드*/
	public int insertMemo(MemoVO memo) throws SQLException{
		try {
			con=DBUtil.getCon();
			
			return 0;
		}finally {
			close();
		}
	}//-----
	
	public void close() {
		try {
		if(rs!=null)rs.close();
		if(ps!=null)ps.close();
		if(con!=null)con.close();
		}catch(SQLException e) {
			
		}
	}
}
