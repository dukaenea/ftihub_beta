package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.ServerClient;

public class DBQuery {
	
	public int insertSignUpCredentials(String name,String password, String email) {
		int rowsaffected = 0;
		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(
					 "insert into t_users(username,password,email) select*from(select ?,?,?) as INS where not exists(select username from t_users where username = ?) LIMIT 1"
					 ,ResultSet.TYPE_SCROLL_INSENSITIVE
					 ,ResultSet.CONCUR_READ_ONLY);
					)
					{
						ps.setString(1,name);
						ps.setString(2,password);
						ps.setString(3,email);
						ps.setString(4,name);
					    rowsaffected = ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
		
		   return rowsaffected;
		}
	
	public void insertGlobalMessageToDb(String username,String message) {
		try(
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = con.prepareStatement(
				"insert into t_pinnedMessages(username,message)values(?,?)",
				 ResultSet.TYPE_SCROLL_INSENSITIVE
				 ,ResultSet.CONCUR_READ_ONLY);
				)
				{
					ps.setString(1,username);
					ps.setString(2,message);
					ps.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	public void insertChatHistoryOnline(int sender,int receiver, String message) {
		try(
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps=con.prepareStatement(
						"insert into t_privatechat(id_sender,id_receiver,online,message) values(?,?,true,?)"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);

				) 
				{
					ps.setInt(1, sender);
					ps.setInt(2, receiver);
					ps.setString(3, message);
					ps.executeUpdate();
					 
					} 
					catch (SQLException e) {
						
						e.printStackTrace();
					}
	}
	
	public void insertChatHistoryOffline(int sender,int receiver, String message) {
		try(
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps=con.prepareStatement(
						"insert into t_privatechat(id_sender,id_receiver,online,message) values(?,?,false,?)"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);

				) 
				{
					ps.setInt(1, sender);
					ps.setInt(2, receiver);
					ps.setString(3, message);
					ps.executeUpdate();
					 
					} 
					catch (SQLException e) {
						
						e.printStackTrace();
					}
		
	
	}
	
	public String changeChatTab(int idTab,int idSender) {
		
	
		StringBuilder s=new StringBuilder();
		
		if(idTab==-1) {
			try (
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps = con.prepareStatement(
						  "select * from t_pinnedMessages"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);
						)
						{
				            s.append("{\"type\": \"new-tab\",\"idsender\": \""+idTab+"\",\"messages\":[");
							ResultSet rs = ps.executeQuery();
							
							while(rs.next()) {
								String name = rs.getString("username");
								String message = rs.getString("message");
								s.append("{\"name\":\""+name+"\",\"message\":\""+message+"\"},");
							}
							 if(s.charAt(s.length()-1)==',') {
								 s.setLength(s.length() - 1); 	 
							 }
							 s.append("]}");
						}
			 catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
		
		try(
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps=con.prepareStatement(
						"select message,online,id_sender from t_privatechat where (id_sender=?  and id_receiver=?) or (id_sender=?  and id_receiver=?)"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);

				) 
				{
					ps.setInt(1, idTab);
					ps.setInt(2, idSender);
					ps.setInt(3, idSender);
					ps.setInt(4, idTab);
					
					s.append("{\"type\": \"new-tab\",\"idsender\": \""+idTab+"\",\"messages\":[");
					
					 try (ResultSet rs = ps.executeQuery()) {
						 
						 int nrMessages=0;
						 int nrNotification=0;
						 while(rs.next()) {
						  
						    String message = rs.getString("message");
						    boolean online= rs.getBoolean("online");
						    int id_sender = rs.getInt("id_sender");
						    if(!online) nrNotification++;
						    s.append("{\"online\":\""+Boolean.toString(online)+"\",\"message\":\""+message+"\",\"id_sender\":\""+id_sender+"\"},");
						    nrMessages++; 
						 }
						 if(s.charAt(s.length()-1)==',') {
							 s.setLength(s.length() - 1); 	 
						 }
						 s.append("],\"nrNotification\":\""+nrNotification+"\",\"nrMessages\":\""+nrMessages+"\"}");						 
					    }// try
					} // try
					catch (SQLException e) {
						
							e.printStackTrace();
						}
		}
				
			return s.toString();
	}
	
	
	public void updateChatHistory(int idTab, int idSender) {
		
		try(
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps=con.prepareStatement(
						"update t_privatechat set online=true where id_sender=?  and id_receiver=?"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);

				) 
				{
					ps.setInt(1, idSender);
					ps.setInt(2, idTab);
					ps.executeUpdate();
					 
					} 
					catch (SQLException e) {
						
						e.printStackTrace();
					}
		
	}
	
	public int validateCredentials(String username,String password){
		try(
				Connection con=DBConnection.getConnection();
				
				PreparedStatement ps=con.prepareStatement(
						"select id from t_users where username=? and password=?"
						,ResultSet.TYPE_SCROLL_INSENSITIVE
						,ResultSet.CONCUR_READ_ONLY);

				) 
				{
					ps.setString(1, username);
					ps.setString(2, password);
					 int id=0;
					 int rowCount=0;
					 try (ResultSet rs = ps.executeQuery()) {
						 
						
						 while(rs.next()) {
						  
						  rowCount++;
					      id = rs.getInt("id");
					      
						 }
						 
					     if(rowCount==1) {
					    	 return id;
					     }
					         
					    } // try
					} // try
				catch (SQLException e) {
					
						e.printStackTrace();
					}
		
		return -1;
	}
}
