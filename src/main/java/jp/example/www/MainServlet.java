package jp.example.www;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//localの場合
	 //String url = "jdbc:postgresql:taskapp_db";
	 String url = "jdbc:postgresql://ec2-3-210-29-54.compute-1.amazonaws.com:5432/d2f2mlmrsgg11q";
	 //String user = "postgres";
	 String user = "gqakyteoruehxc";
	 //String pass = "postgres";
	 String pass = "dc1254a85aa015099c3f3ccc66761bdf0c14c4614497660fe18ee5c0c692cc77";
	 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		 Connection con = null;
		 Statement smt = null;
		 ArrayList<HashMap<String, String>> record_list = new ArrayList<>();
		 try {
				Class.forName("org.postgresql.Driver");
				con = DriverManager.getConnection(url, user, pass);				
	            smt = con.createStatement();
	            
	            String select_memo 
	            = "select task_id, title ,cast(memo as character varying),modified_date from task_table3";
	            			            
	            ResultSet result = smt.executeQuery(select_memo);
	            while (result.next()) {
	                HashMap<String, String> record = new HashMap<>();
	                System.out.println("task_id: " + result.getString("task_id"));
	                System.out.println("title: " + result.getString("title"));
	                System.out.println("memo: " + result.getString("memo"));
	                System.out.println("modify: " + result.getString("modified_date"));
	                record.put("task_id", result.getString("task_id"));
	                record.put("title", result.getString("title"));
	                record.put("memo", result.getString("memo"));
	                record.put("modified_date", result.getString("modified_date"));
	                record_list.add(record);
	            }			 
		 } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
		 request.setAttribute("record_list", record_list);

	        String view ="/WEB-INF/jsp/index.jsp";
	        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
	        dispatcher.forward(request, response);
	      
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		 	
		request.setCharacterEncoding("UTF-8");
	        System.out.println("title input: " + request.getParameter("title"));
	        System.out.println("memo input: " + request.getParameter("memo"));
		
	        // DB接続 //
	        Connection con = null;
	        Statement smt = null;
	        
	        try {
				Class.forName("org.postgresql.Driver");
				con = DriverManager.getConnection(url, user, pass);				
	            smt = con.createStatement();
	            System.out.println("smt: " + smt);
	            // テーブル作成
	            
	            String create_table
	            = "create table if not exists task_table3 (\r\n"
	            		+ "  task_id SERIAL not null ,\r\n"
	            		+ "  category INT ,\r\n"
	            		+ "  title VARCHAR(64) ,\r\n"
	            		+ "  memo VARCHAR(100) ,\r\n"
	            		+ "  create_date DATE ,\r\n"
	            		+ "  modified_date DATE ,\r\n"
	            		+ "  primary key (task_id)\r\n"
	            		+ ");";
	            
	            smt.executeUpdate(create_table);
	            
	            
	         // データ登録(insert)
	        	
	            String Submit = request.getParameter("Submit");
	            String form_title = request.getParameter("title");
	            String form_memo = request.getParameter("memo");
	            
	            String task_id = request.getParameter("delete_id");
	            
	            System.out.println("Submit: " + Submit);
	            System.out.println("INSERT_title: " + form_title);
	            System.out.println("INSERT_text: " + form_memo);
	            
	            if ( ( request.getParameter("title") ) != null 
	            		&& (request.getParameter("memo") !=null) )
	            			{
				            String insert_memo = "insert into task_table3 (" +
				                    "category, title, memo, create_date, modified_date" +
				                ") values (" +
				                    "0," +
				                    "'" + form_title + "'," +
				                    "'" + form_memo + "'," +                              
				                    "now()::date," +
				                    "now()::date" +
				                ");";
				            System.out.println("sql: " + insert_memo);
				            smt.executeUpdate(insert_memo);
	            	
	            }else if(form_title==null && form_memo==null) {
	            	System.out.println("削除処理" );
	            	System.out.println("DELETE:task_id="+ task_id);
		            String delete_task
		            = "DELETE FROM task_table3 WHERE task_id="+task_id;
		            //delete_task.setString(1,"task_id");
		            smt.executeUpdate(delete_task);
	            
	            }else {
	            	System.out.println("失敗" );
	            }
	                        
	            
	        }catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        //削除処理
	        
	        // -- ここまでDB処理 --

	        //response.sendRedirect(".");	       	        
		doGet(request, response);
	}
	
	

}
