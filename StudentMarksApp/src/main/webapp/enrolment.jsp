<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Enrollment Page</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Student Enrollment Page</h1>
    </header>
    <main>
        <div class="course-selection">
            <h3>Course Selection</h3>
            <ul id="course-list">
                <li id="course1" onclick="updateModules('Computer Science')">Course 1</li>
                <li id="course2" onclick="updateModules('Cyber Security')">Course 2</li>
                <li id="course3" onclick="updateModules('Cyber Forensics')">Course 3</li>
            </ul>
        </div>
        <div class="module-selection">
            <h3 id="module-header">Modules</h3>
            <ul id="module-list"></ul>
            <button onclick="confirmSelection()">Confirm Selection</button>
        </div>
    </main>
</div>
<script>
    let selectedCourse = "";

function getCourseList() {
    var courseList = JSON.parse('<%= request.getAttribute("courses") %>');
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
            if (module.course_name === course) {
                const li = document.createElement("li");
                li.textContent = module.module_name;
                moduleList.appendChild(li);
            }
        }
    }
document.addEventListener("DOMContentLoaded", function() {
    updateCourses();});

function confirmSelection() {
    // ask user if they're sure
}
</script>
</body>
</html>
