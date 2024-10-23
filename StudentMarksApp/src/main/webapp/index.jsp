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
    /* The switch - the box around the slider */
    .switch {
      position: absolute;
      display: inline-block;
      width: 60px;
      height: 34px;
      top: 20px;
      left: 20px;
    }

    /* Hide default HTML checkbox */
    .switch input {
      opacity: 0;
      width: 0;
      height: 0;
    }

    /* The slider */
    .slider {
      position: absolute;
      cursor: pointer;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: #ccc;
      -webkit-transition: .4s;
      transition: .4s;
    }

    .slider:before {
      position: absolute;
      content: "";
      height: 26px;
      width: 26px;
      left: 4px;
      bottom: 4px;
      background-color: white;
      -webkit-transition: .4s;
      transition: .4s;
    }

    input:checked + .slider {
      background-color: #2196F3;
    }

    input:focus + .slider {
      box-shadow: 0 0 1px #2196F3;
    }

    input:checked + .slider:before {
      -webkit-transform: translateX(26px);
      -ms-transform: translateX(26px);
      transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
      border-radius: 34px;
    }

    .slider.round:before {
      border-radius: 50%;
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


  <!-- Button to open the login pop-up -->
  <button class="login-button" onclick="openLogin()">Login</button>

  <!-- The login pop-up -->
  <div class="login-popup" id="loginPopup">
    <div class="login-box">
      <h3>Sign In</h3>
      <form action="${pageContext.request.contextPath}/login-servlet" method ="post">
        <label for="toggle-switch" class="switch">
          <input type="checkbox" id="toggle-switch" name="toggle-switch" value="checked">
          <span class="slider round"></span>
        </label>
        <label for="user-id">User ID</label>
        <input type="text" id="user-id" name="user-id" placeholder="18263">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="********">

        <input type="submit" value="Sign in">
      </form>
      <a href="#">Forgot Password?</a>
      <a href="registration.jsp">Sign Up</a>
      <!-- Button to close the login pop-up -->
      <button onclick="closeLogin()">Close</button>
    </div>
  </div>

</div>

<script>
  // Function to open the login pop-up
  function openLogin() {
    document.getElementById("loginPopup").style.display = "flex";
  }

  // Function to close the login pop-up
  function closeLogin() {
    document.getElementById("loginPopup").style.display = "none";
  }
</script>

</body>
</html>
