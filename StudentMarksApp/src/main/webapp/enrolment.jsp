<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Enrollment Page</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<button class="logout-button" id="logout-button" onclick="logout()"></button>

<div class="container">
    <header>
        <h1>Student Enrollment Page</h1>
    </header>
    <main>
        <div class="course-selection">
            <h3>Course Selection</h3>
            <ul id="course-list">
                <%--TODO: These courses shouldn't be hard coded--%>
                <li id="course1" onclick="updateModules('Computer Science')"></li>
                <li id="course2" onclick="updateModules('Cyber Security')"></li>
                <li id="course3" onclick="updateModules('Cyber Forensics')"></li>
            </ul>
        </div>
        <div class="module-selection">
            <h3 id="module-header">Modules</h3>
            <ul id="module-list"></ul>
            <button id="btnConf" onclick="confirmSelection()">Confirm Selection</button>
        </div>
    </main>
</div>
<script>
    let selectedCourse = "";

function getCourseList() {
    var courseList = JSON.parse('<%= request.getAttribute("courses") %>');
    console.log(courseList);
    for (var course of courseList) {
        console.log(course);
    }
    return courseList;
}
function updateCourses() {
    var courseList = getCourseList();
    var course1 = document.getElementById("course1");
    var course2 = document.getElementById("course2");
    var course3 = document.getElementById("course3");
    course1.textContent = courseList[0];
    course2.textContent = courseList[1];
    course3.textContent = courseList[2];
}

    function updateModules(course) {
        selectedCourse = course;
        const modules = JSON.parse('<%= request.getAttribute("modules") %>');

        const moduleList = document.getElementById("module-list");
        const moduleHeader = document.getElementById("module-header");

        moduleList.innerHTML = "";

        moduleHeader.textContent = "Modules for " + course;

        for (var module of modules) {
            // Has to be like this because mongo and oracle have different ways of storing booleans
            if (module.course_name === course && module.optional === 'N' || module.optional === false) {
                const li = document.createElement("li");
                li.textContent = module.module_name;
                moduleList.appendChild(li);
            }
        }
    }
document.addEventListener("DOMContentLoaded", function() {
    updateCourses();});

function confirmSelection() {
    if (selectedCourse === "") {
        alert("Please select a course and module");
    } else {
        var form = document.createElement("form");
        form.method = "post";
        form.action = "${pageContext.request.contextPath}/Enrolment-Servlet";
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = "course";
        input.value = selectedCourse;
        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();
    }
}
function openLogin() {
    document.getElementById("loginPopup").style.display = "flex";
}
function closeLogin() {
    document.getElementById("loginPopup").style.display = "none";
}
</script>
</body>
</html>
