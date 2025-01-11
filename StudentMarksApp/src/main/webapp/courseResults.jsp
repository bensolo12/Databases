<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Enrollment Page</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        .container {
            display: flex;
            flex-direction: column;
        }
        .main-content {
            display: flex;
            justify-content: flex-start;
            width: 25%;
            padding: 0;
        }
        table {
            width: 25%;
        }
    </style>
</head>
<body>

<button class="logout-button" id="logout-button" onclick="logout()"></button>

<div class="container">
    <header>
        <h1>Course Results Page</h1>
    </header>
    <main class="main-content">
        <div class="container">
            <div class="course-list">
                <table>
                    <thead>
                    <tr>
                        <th>Courses</th>
                    </tr>
                    </thead>
                    <tbody id="course-selection">
                    <!-- Students will be populated here by JavaScript -->
                    </tbody>
                </table>
            </div>
        </div>
        <div class="container">
            <div class="course-passes">
                <table>
                    <thead>
                    <tr>
                        <th>Year</th>
                        <th>Students Passed</th>
                    </tr>
                    </thead>
                    <tbody id="grades">
                    <!-- Year and people passed will appear here -->
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>
<script>
    const courseData = JSON.parse('<%= request.getAttribute("coursePasses") %>');
    const courseIDs = JSON.parse(courseData.courseID);
    const passes = JSON.parse(courseData.passes);

    function renderCourseList() {
        const courseSelection = document.getElementById('course-selection');
        courseSelection.innerHTML = '';
        courseIDs.forEach((courseID, index) => {
            const row = document.createElement('tr');
            row.textContent = courseID;
            row.addEventListener('click', () => selectCourse(index));
            courseSelection.appendChild(row);
        });
    }

    function selectCourse(index) {
        const grades = document.getElementById('grades');
        grades.innerHTML = '';
        const row = document.createElement('tr');
        const yearCell = document.createElement('td');
        const passesCell = document.createElement('td');
        yearCell.textContent = "2024"; // Assuming the year is 2024
        passesCell.textContent = passes[index];
        row.appendChild(yearCell);
        row.appendChild(passesCell);
        grades.appendChild(row);
    }

    document.addEventListener("DOMContentLoaded", function() {
        renderCourseList();
    });
</script>
</body>
</html>