package com.multicamp;
import java.sql.*;
import java.util.*;
import java.util.List;
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
			
			//이전에 쓰던방식 아래
			//String : immutable (불변성)을 가져 원본은 변하지 않음.
//			String sql="INSERT INTO MEMO(idx, name, msg, wdate)";
//            sql+=" values(memo_seq.nextval,?,?,sysdate)";

			//String 방식.
			//StringBulder, StringBuffer : 문자열을 메모리 버퍼에 넣고 수정,삭제,삽입 등의 편집작업을 할 수 있음.
			StringBuilder buf=new StringBuilder("INSERT INTO MEMO(idx, name, msg, wdate)") //이부분이 메모리버퍼에 올라가고
												.append(" values(memo_seq.nextval,?,?,sysdate)"); //이부분이 변경되면서 붙게된다.
			String sql=buf.toString();
            
            ps=con.prepareStatement(sql);
            ps.setString(1, memo.getName());
            ps.setString(2, memo.getMsg());
            int n=ps.executeUpdate();           
			return n;
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
	

	
//--------------------------------------------------------------------------------------- 세트임
	/** DB접근해서 셀렉트하는 메서드*/
	public List<MemoVO> selectMemoAll() throws SQLException{
		try {
			con=DBUtil.getCon();
			StringBuilder buf=new StringBuilder("select idx, rpad(name,12,' ') name, rpad(msg,100,' ') msg,")
									.append(" wdate from memo order by idx desc");
			String sql=buf.toString();
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			List<MemoVO> arr=makeList(rs);
			return arr;
		}finally {
			close();
		}
	}
	//위의 arr에 담길 makeList 만들어줘야함.
	private List<MemoVO> makeList(ResultSet rs) throws SQLException {
		List<MemoVO> arr=new ArrayList<>();
		while(rs.next()) {
			int idx=rs.getInt("idx");
			String name=rs.getString("name");
			String msg=rs.getString("msg");
			java.sql.Date wdate=rs.getDate("wdate"); //컬럼데이터들 다꺼내와서
			//vo에 담아준다
			MemoVO vo=new MemoVO(idx,name,msg,wdate);
			arr.add(vo); //ArrayList에 vo를 담는다
		}
		return arr;//담긴 ArrayList를 반환
	}
//--------------------------------------------------------------------------------------- 세트임

	
	public int deleteMemo(int idx) throws SQLException {
		try {
			con=DBUtil.getCon();
			String sql="delete from memo where idx=?";
			ps=con.prepareStatement(sql);
			ps.setInt(1, idx);
			return ps.executeUpdate();			
		}finally {
			close();
		}
	}//-----------------------------------
	
	public MemoVO selectMemo(int idx) throws SQLException{
		try {
			con=DBUtil.getCon();
			String sql="select idx,name,msg,wdate from memo where idx=?";
			ps=con.prepareStatement(sql);
			ps.setInt(1, idx);
			rs=ps.executeQuery();
			List<MemoVO> arr=makeList(rs);
			if(arr!=null && arr.size()==1) {
				return arr.get(0);
			}
			return null;
		}finally {
			close();
		}
	}//-----------------------------------
	
	//update문 
		public int updateMemo(MemoVO vo) throws SQLException{
			try {
				con=DBUtil.getCon();
				StringBuilder buf=new StringBuilder("update memo set name=?, msg=? ")
												.append(" where idx=?");
				String sql=buf.toString();
				ps=con.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getMsg());
				ps.setInt(3, vo.getIdx());
				return ps.executeUpdate();
			}finally {
				close();
			}
		}//------------------------------------
	
		
		public List<MemoVO> findMemo(int type, String keyword) throws SQLException {
			try {
				con=DBUtil.getCon();
				String colName=(type==0)?"name":"msg";
				//String sql = "SELECT * FROM memo WHERE msg like '%"+keyword+"%' order by idx desc";
				StringBuilder buf=new StringBuilder("select idx,name,msg,wdate from memo")
						.append(" where "+colName+" like ?");
				String sql=buf.toString();
				ps=con.prepareStatement(sql);
				ps.setString(1, "%"+keyword+"%");
				rs=ps.executeQuery();
				List<MemoVO> arr=makeList(rs);
				return arr;
			}finally {
				close();
			}
		}//------------------------------------
	
}
