package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
	
	public static Database instance;
	private Connection dbConnection = null;
	private Statement stmt = null;
	
	
	public Database() {
		dbConnection=ConnectDatabase.getInstance().getDBConnection();
		try {
			stmt = dbConnection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		if(instance==null)
			instance=new Database();
		return instance;
	}
	
	public boolean createUser(String name,String pass) {
		
		if(!userExits(name)){
			String sql="INSERT INTO "+DatabaseConfig.TB_USER+"("+DatabaseConfig.USER_NAME+", "+DatabaseConfig.USER_PASS+")  VALUES ('"+name+"','"+pass+"')";
			try {
				stmt.execute(sql);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else{
			return false;			
		}
		
	}
	
	public boolean userExits(String name) {		
		String sql="SELECT * FROM "+DatabaseConfig.TB_USER+" WHERE "+DatabaseConfig.USER_NAME+" = '"+name+"'";
		try {			
			ResultSet rs =stmt.executeQuery(sql);
			return rs.next();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}		
	}
	
	public boolean login(String user,String pass){
		String sql="SELECT * FROM "+DatabaseConfig.TB_USER+" WHERE "+DatabaseConfig.USER_NAME+" = '"+user+"' AND "+DatabaseConfig.USER_PASS+" ='"+pass+"';";		
		try {
			ResultSet rs =stmt.executeQuery(sql);
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}

	public boolean addFriend(String myname,String friendName){
		if(isFriend(myname, friendName)){
			return false;
		}else{
			String sql="INSERT INTO "+DatabaseConfig.TB_FRIENDS+"("+DatabaseConfig.FRIEND_ID+", "+DatabaseConfig.FRIEND_NAME+")  VALUES ('"+myname+"','"+friendName+"')";
			try {
				stmt.execute(sql);
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}			
		}
	}
	
	public boolean isFriend(String myname,String friendName) {
		String sql1="SELECT * FROM "+DatabaseConfig.TB_FRIENDS+" WHERE "
				+DatabaseConfig.FRIEND_ID+" = '"+myname
				+"' AND "+DatabaseConfig.FRIEND_NAME+" ='"+friendName+"'";
		try {			
			ResultSet rs =stmt.executeQuery(sql1);
			return rs.next();
		} catch (SQLException e) {			
			e.printStackTrace();
			return false;
		}
	}

	public List<String> listFriend(String myname){
		List<String> list =new ArrayList<String>();
		
		String sql="SELECT * FROM "+DatabaseConfig.TB_FRIENDS+" WHERE "+DatabaseConfig.FRIEND_ID+" = '"+myname+"';";		
		try {
			ResultSet rs =stmt.executeQuery(sql);
			//int indexMyName=rs.getInt(DatabaseConfig.FRIEND_ID);
			int indexFriendName=rs.getInt(DatabaseConfig.FRIEND_NAME);
			while(rs.next()){
				String name=rs.getString(indexFriendName).trim();
				list.add(name);
				}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
}
