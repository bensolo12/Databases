<%--@elvariable id="userType" type="java.lang.String"--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="styles.css">
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
    .nav-button {
      cursor: pointer;
      padding: 10px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 5px;
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
    <div class="grid-item" id="grid-item1"><button class="nav-button" id="button1" onclick="handleClick('grid-item1')"></button></div>
    <div class="grid-item" id="grid-item2"><button class="nav-button" id="button2" onclick="handleClick('grid-item2')"></button></div>
    <div class="grid-item" id="grid-item3"><button class="nav-button" id="button3" onclick="handleClick('grid-item3')"></button></div>
    <div class="grid-item" id="grid-item4"><button class="nav-button" id="button4" onclick="handleClick('grid-item4')"></button></div>
    <div class="grid-item" id="grid-item5"><button class="nav-button" id="button5" onclick="handleClick('grid-item5')"></button></div>
    <div class="grid-item" id="grid-item6"><button class="nav-button" id="button6" onclick="handleClick('grid-item6')"></button></div>
  </div>

  <button class="login-button" id="login-button" onclick="openLogin()">Login</button>

  <!-- The login pop-up -->
  <div class="login-popup" id="loginPopup">
    <div class="login-box" id="loginBox">
      <h3 id="popup-title">Sign In</h3>
      <form id="login-form" action="${pageContext.request.contextPath}/login-servlet" method="post">
        <label for="user-id">User ID</label>
        <input type="text" id="user-id" name="user-id" placeholder="18263">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="********">
        <input type="submit" value="Sign in">
      </form>
      <a href="#" id="forgot-password">Forgot Password?</a>
      <a href="registration.jsp" id="sign-up">Sign Up</a>
      <button onclick="closeLogin()">Close</button>
    </div>
    <div class="login-box" id="studentBox" style="display: none;">
      <h3>Student Info</h3>
      <p id="student-id"></p>
      <p id="student-course"></p>
      <form id="logout-form" action="${pageContext.request.contextPath}/logout-servlet" method="post">
        <input type="submit" value="Sign out">
      </form>
      <button onclick="closeLogin()">Close</button>
    </div>
  </div>

</div>

<script>
  function openLogin() {
    document.getElementById("popup-title").textContent = "Sign In";
    document.getElementById("login-form").style.display = "block";
    document.getElementById("studentBox").style.display = "none";
    document.getElementById("loginPopup").style.display = "flex";
  }

  function openStudentBox(studentId, course) {
    document.getElementById("student-id").textContent = "Student ID: " + studentId;
    document.getElementById("student-course").textContent = "Course: " + course;
    document.getElementById("login-form").style.display = "none";
    document.getElementById("studentBox").style.display = "block";
    document.getElementById("loginPopup").style.display = "flex";
  }

  document.addEventListener("DOMContentLoaded", function() {
    var userType = "<%= request.getAttribute("userType") %>";
    var userName = "<%= request.getAttribute("userName") %>";
    var studentId = "<%= request.getAttribute("studentId") %>";
    var course = "<%= request.getAttribute("course") %>";
    renderHomePage(userType, userName, studentId, course);
  });

  function handleClick(itemId) {
    var userType = "<%= request.getAttribute("userType") %>";
    if (userType === "STUDENT"){
        if (itemId === "grid-item1"){
          var form = document.createElement("form");
          form.method = "post";
          form.action = "${pageContext.request.contextPath}/Enrolment-Servlet";
          document.body.appendChild(form);
          form.submit();
        }
        else if (itemId === "grid-item2"){
          var form = document.createElement("form");
          form.method = "post";
          form.action = "${pageContext.request.contextPath}/Module-Select-Servlet";
          document.body.appendChild(form);
          form.submit();
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
          var form = document.createElement("form");
          form.method = "post";
          form.action = "${pageContext.request.contextPath}/Result-Entry-Servlet";
          document.body.appendChild(form);
          form.submit();
        }
        else if (itemId === "grid-item2"){
          var form = document.createElement("form");
          form.method = "post";
          form.action = "${pageContext.request.contextPath}/Student-Result-Servlet";
          document.body.appendChild(form);
          form.submit();
        }
        else if (itemId === "grid-item3"){
          var form = document.createElement("form");
          form.method = "post";
          form.action = "${pageContext.request.contextPath}/Course-Result-Servlet";
          document.body.appendChild(form);
          form.submit();
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
      document.getElementById("button1").textContent = "Enrollment";
      document.getElementById("button2").textContent = "Module Registration";
      document.getElementById("button3").textContent = "Module Results";
      document.getElementById("button4").textContent = "Timetable";
      document.getElementById("welcomeMsg").textContent = "Student Homepage";
      document.getElementById("login-button").textContent = userName;
      document.getElementById("logout-button").style.display = "block";
    }
    else if (userType === "TEACHER"){
      document.getElementById("button1").textContent = "Module Result Entry";
      document.getElementById("button2").textContent = "Student Results";
      document.getElementById("button3").textContent = "Courses";
      document.getElementById("button4").textContent = "Timetable";
      document.getElementById("welcomeMsg").textContent = "Staff Homepage";
      document.getElementById("login-button").textContent = userName;
    }
    else if (userType === "ADMIN"){
      document.getElementById("button1").textContent = "View All Users";
      document.getElementById("button2").textContent = "Add New User";
      document.getElementById("button3").textContent = "Remove User";
      document.getElementById("login-button").textContent = userName;
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
