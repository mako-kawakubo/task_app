<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script  src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script  src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>TaskMemo App</title>
</head>

<body>
    <div id="wrapper" class="container-fluid">
        <h1 align="center">☆task memo☆</h1>
    </div>
    
    <form method="post" align="center">
        <input type="text" name="title" size="50" />
        <br/>
        <br/>
        <textarea rows="5" cols="80" name="memo"></textarea>
        <br/>
        <br/>
     <button type="submit" class="btn btn-primary">Submit</button>
      <!-- input type="submit"/> -->
    </form>
    
 
 <%
     ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) request
             .getAttribute("record_list");
     Iterator<HashMap<String, String>> i = list.iterator();
     while (i.hasNext()) {
         HashMap map = i.next();
         out.println("<hr/>");
         out.println("<div>" +"タイトル："+ map.get("title") + "</div>");
         //out.println("<div>" + map.get("memo") + "</div>");
         out.println("<div>" +"日付："+ map.get("modified_date") + "</div>");       
         out.println("<div>" +"内容："+ ((String) map.get("memo")).replace("\n", "<br/>") + "</div>");
     }
 %>

</body>
</html>
