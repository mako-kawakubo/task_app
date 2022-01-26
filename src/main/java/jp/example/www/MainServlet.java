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
	
	 //String url = "jdbc:postgresql:taskapp_db";
	 String url = "jdbc:postgresql://ec2-18-211-86-133.compute-1.amazonaws.com:5432/dbd6l2q3ajmbuc";
	 //String user = "postgres";
	 String user = "udhtniyiqbliqs";
	 //String pass = "postgres";
	 String pass = "856a2a8df5d6335336ceddb52a75f4f4eeb935e15d673f49bfa52145c8cb1356";
	 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		 Connection con = null;
		 Statement smt = null;
		 ArrayList<HashMap<String, String>> record_list = new ArrayList<>();
		 try {
				Class.forName("org.postgresql.Driver");
				con = DriverManager.getConnection(url, user, pass);				
	            smt = con.createStatement();
	            
	            String select_memo 
	            = "select title ,cast(memo as character varying),modified_date from task_table2";
	            			            
	            ResultSet result = smt.executeQuery(select_memo);
	            while (result.next()) {
	                HashMap<String, String> record = new HashMap<>();
	                System.out.println("title: " + result.getString("title"));
	                System.out.println("memo: " + result.getString("memo"));
	                System.out.println("modify: " + result.getString("modified_date"));
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
		// TODO Auto-generated method stub
	
		
		//String path = "/WEB-INF/jsp/index.jsp";
        //RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        //dispatcher.forward(request, response);
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
	            
	            String create_table
	            = "create table if not exists task_table2 (\r\n"
	            		+ "  task_id INT not null ,\r\n"
	            		+ "  category INT ,\r\n"
	            		+ "  title VARCHAR(64) ,\r\n"
	            		+ "  memo VARCHAR(100) ,\r\n"
	            		+ "  create_date DATE ,\r\n"
	            		+ "  modified_date DATE ,\r\n"
	            		+ "  primary key (task_id)\r\n"
	            		+ ");";
	            // テーブル作成
	            smt.executeUpdate(create_table);
	            
	            String form_title = request.getParameter("title");
	            String form_memo = request.getParameter("memo");
	            System.out.println("title: " + form_title);
	            System.out.println("text: " + form_memo);
	            String insert_memo = "insert into task_table2 (" +
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
	            
	        }catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        // -- ここまでDB処理 --

	        //response.sendRedirect(".");	       	        
		doGet(request, response);
	}

}
