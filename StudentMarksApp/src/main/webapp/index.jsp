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
    /* Style for the login pop-up */
    .login-popup {
      display: none; /* Hidden by default */
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
    .DBSwitch-button {
      cursor: pointer;
      padding: 10px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 5px;
      position: absolute;
      top: 20px;
      left: 20px;
    }
  </style>
</head>
<body>

<div class="container">
  <div class="header">
    <h1>Home Page</h1>
    <p>Please sign in to see your home page</p>
  </div>

  <div class="grid-container">
    <div class="grid-item">University News</div>
    <div class="grid-item">What's on</div>
    <div class="grid-item">Restaurant</div>
    <div class="grid-item">Library Links</div>
    <div class="grid-item">Help Zone</div>
    <div class="grid-item">Equipment Hire</div>
  </div>

<%--  <form action="index.jsp" method="post">
    <button class="DBSwitch-button" id="DBSwitch-button" onclick="toggleLabel()">Switch to SQL</button>
  </form>--%>

  <button class="login-button" onclick="openLogin()">Login</button>

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
