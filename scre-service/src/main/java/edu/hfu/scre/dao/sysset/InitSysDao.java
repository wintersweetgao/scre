package edu.hfu.scre.dao.sysset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;

import edu.hfu.scre.dao.base.BaseDaoImpl;
@Repository
public class InitSysDao {

	@Resource
	private BaseDaoImpl dao;
	
	public void initView(List<Map<String,String>> ls_view) throws Exception{
		Session session =dao.getSession();
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				try {
					Statement stat=conn.createStatement();
					for (Map<String,String> map:ls_view){
						if (map.get("type").equalsIgnoreCase("mysql")){
							//select table_name from information_schema.tables where table_schema='scre' and table_name='viewscreachieve'
							String query="SELECT table_name FROM information_schema.tables WHERE table_schema='scre'  and  TABLE_TYPE='BASE TABLE' and table_name = '"+map.get("viewname").toLowerCase()+"'";
							ResultSet rs=stat.executeQuery(query);
							if (rs.next()){
								stat.executeUpdate("DROP table "+map.get("viewname"));//先删除
							}	
							String updsql="create or replace view "+map.get("viewname")+" as "+map.get("viewContent");								
							stat.executeUpdate(updsql);
						}
					}
					stat.close();						
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
