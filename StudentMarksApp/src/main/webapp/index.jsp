<%--@elvariable id="userType" type="java.lang.String"--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Home Page</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .container {
      width: 100%;
      max-width: 1200px;
      padding: 20px;
    }
    .header {
      text-align: center;
      margin-bottom: 20px;
    }
    .grid-container {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
      margin-top: 20px;
    }
    .grid-item {
      border: 1px solid #000;
      padding: 20px;
      text-align: center;
      border-radius: 15px;
    }
    .login-popup {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      justify-content: center;
      align-items: center;
    }
    .login-box {
      background-color: white;
      border: 1px solid #ccc;
      padding: 20px;
      border-radius: 10px;
      width: 300px;
    }
    .login-box h3 {
      margin-bottom: 10px;
    }
    .login-box input[type="text"],
    .login-box input[type="password"] {
      width: 100%;
      padding: 8px;
      margin: 8px 0;
      box-sizing: border-box;
    }
    .login-box input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      padding: 10px;
      border: none;
      width: 100%;
      cursor: pointer;
      border-radius: 5px;
    }
    .login-box input[type="submit"]:hover {
      background-color: #45a049;
    }
    .login-box a {
      display: block;
      text-align: center;
      margin-top: 10px;
      color: #007BFF;
      text-decoration: none;
    }
    .login-box a:hover {
      text-decoration: underline;
    }
    .login-button {
      cursor: pointer;
      padding: 10px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 5px;
      position: absolute;
      top: 20px;
      right: 20px;
    }
  </style>
</head>
<body>

<div class="container">
  <div class="header">
    <h1>Home Page</h1>
    <div class="welcomeMsg" id="welcomeMsg">
<%--    <p>Welcome, ${userType}!</p>--%>
    </div>
  </div>

  <div class="grid-container">
    <div class="grid-item" id="grid-item1"><button onclick="handleClick('grid-item1')"></button></div>
    <div class="grid-item" id="grid-item2"><button onclick="handleClick('grid-item2')"></button></div>
    <div class="grid-item" id="grid-item3"><button onclick="handleClick('grid-item3')"></button></div>
    <div class="grid-item" id="grid-item4"><button onclick="handleClick('grid-item4')"></button></div>
    <div class="grid-item" id="grid-item5"><button onclick="handleClick('grid-item5')"></button></div>
    <div class="grid-item" id="grid-item6"><button onclick="handleClick('grid-item6')"></button></div>
  </div>

<%--  <form action="index.jsp" method="post">
    <button class="DBSwitch-button" id="DBSwitch-button" onclick="toggleLabel()">Switch to SQL</button>
  </form>--%>

  <button class="login-button" id="login-button" onclick="openLogin()">Login</button>

  <!-- The login pop-up -->
  <div class="login-popup" id="loginPopup">
    <div class="login-box">
      <h3>Sign In</h3>
      <form action="${pageContext.request.contextPath}/login-servlet" method="post">
        <label for="user-id">User ID</label>
        <input type="text" id="user-id" name="user-id" placeholder="18263">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="********">

        <input type="submit" value="Sign in">
      </form>
      <a href="#">Forgot Password?</a>
      <a href="registration.jsp">Sign Up</a>
      <button onclick="closeLogin()">Close</button>
    </div>
  </div>

</div>

<script>
  function openLogin() {
    document.getElementById("loginPopup").style.display = "flex";
  }
  document.addEventListener("DOMContentLoaded", function() {
    var userType = "<%= request.getAttribute("userType") %>";
    var userName = "<%= request.getAttribute("userName") %>";
    renderHomePage(userType, userName);
  });

  function handleClick(itemId) {
    var userType = "<%= request.getAttribute("userType") %>";
    if (userType === "STUDENT"){
        if (itemId === "grid-item1"){
            window.location.href = "enrolment.jsp";
        }
        else if (itemId === "grid-item2"){
            window.location.href = "moduleReg.jsp";
        }
        else if (itemId === "grid-item3"){
            window.location.href = "moduleRes.jsp";
        }
        else if (itemId === "grid-item4"){
            window.location.href = "timetable.jsp";
        }
    }
        else if (userType === "TEACHER"){
        if (itemId === "grid-item1"){
            window.location.href = "result-entry.jsp";
        }
        else if (itemId === "grid-item2"){
            window.location.href = "student-results.jsp";
        }
        else if (itemId === "grid-item3"){
            window.location.href = "courses.jsp";
        }
        else if (itemId === "grid-item4"){
            window.location.href = "timetable.jsp";
        }
        }
        else if (userType === "ADMIN"){
        if (itemId === "grid-item1"){
            window.location.href = "view-users.jsp";
        }
        else if (itemId === "grid-item2"){
            window.location.href = "add-user.jsp";
        }
        else if (itemId === "grid-item3"){
            window.location.href = "remove-user.jsp";
        }
    }
  }

  function renderHomePage(userType, userName){

    if (userType === "STUDENT"){
      document.getElementById("grid-item1").textContent = "Enrollment";
      document.getElementById("grid-item1").onclick = "window.location.href = 'enrolment.jsp'";
      document.getElementById("grid-item2").textContent = "Module Registration";
      document.getElementById("grid-item3").textContent = "Module Results";
      document.getElementById("grid-item4").textContent = "Timetable";
      document.getElementById("welcomeMsg").textContent = "Student Homepage";
      document.getElementById("login-button").textContent = userName;
    }
    else if (userType === "TEACHER"){
      document.getElementById("grid-item1").textContent = "Module Result Entry";
      document.getElementById("grid-item2").textContent = "Student Results";
      document.getElementById("grid-item3").textContent = "Courses";
      document.getElementById("grid-item4").textContent = "Timetable";
      document.getElementById("welcomeMsg").textContent = "Staff Homepage";
    }
    else if (userType === "ADMIN"){
      document.getElementById("grid-item1").textContent = "View All Users";
      document.getElementById("grid-item2").textContent = "Add New User";
      document.getElementById("grid-item3").textContent = "Remove User";
    }
    else{
      document.getElementById("grid-item1").textContent = "University News";
        document.getElementById("grid-item2").textContent = "What's On";
        document.getElementById("grid-item3").textContent = "Restaurant";
        document.getElementById("grid-item4").textContent = "Library Links";
        document.getElementById("grid-item5").textContent = "Help Zone";
        document.getElementById("grid-item6").textContent = "Equipment Hire";
      document.getElementById("welcomeMsg").textContent = "Please log in to view your homepage";
    }
  }

  function closeLogin() {
    document.getElementById("loginPopup").style.display = "none";
  }

  function toggleLabel() {
    var button = document.getElementById("DBSwitch-button");
    var currentText = button.textContent;
    var newText;
    if (currentText == "Switch to SQL"){
      newText = "Switch to Mongo";

    }
    else{
      newText = "Switch to SQL";
    }
    button.textContent = newText;
  }


</script>

</body>
</html>
