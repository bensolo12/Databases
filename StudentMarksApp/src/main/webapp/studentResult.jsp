<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Module Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        h1 {
            text-align: center;
        }

        .container {
            display: flex;
            justify-content: space-between;
        }

        .student-list {
            width: 45%;
            border: 1px solid #ccc;
            padding: 10px;
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
    </style>
</head>
<body>
<h1>Student Result Page</h1>

<div class="container">
    <div class="student-list">
        <table>
            <thead>
            <tr>
                <th>Students</th>
            </tr>
            </thead>
            <tbody id="student-selection">
            <!-- Students will be populated here by JavaScript -->
            </tbody>
        </table>
    </div>

    <div class="selected-student-grades">
        <table>
            <thead>
            <tr>
                <th>Module</th>
                <th>Grade</th>
            </tr>
            </thead>
            <tbody id="grades">
            <!-- Students and grades will appear here -->
            </tbody>
        </table>
    </div>
</div>
<script>
    const students = JSON.parse('<%= request.getAttribute("studentList") %>');
    const moduleResults = JSON.parse('<%= request.getAttribute("moduleGrades") %>');

    function renderStudentList() {
        const studentSelection = document.getElementById('student-selection');
        studentSelection.innerHTML = '';
        students.forEach((student, index) => {
            const row = document.createElement('tr');
            row.textContent = student.user_id;
            row.addEventListener('click', () => selectStudent(student.user_id));
            studentSelection.appendChild(row);
        });
    }

    function selectStudent(studentID) {
        //Use the studentID to fetch the student's grades per module
        //Then render the grades in the grades table
        //console.log(moduleResults);
        const grades = document.getElementById('grades');
        grades.innerHTML = '';
        moduleResults.forEach((module, index) => {
            if (module.students === studentID) {
                const row = document.createElement('tr');
                const moduleCell = document.createElement('td');
                const gradeCell = document.createElement('td');
                moduleCell.textContent = module.module_id;
                gradeCell.textContent = module.student_result;
                row.appendChild(moduleCell);
                row.appendChild(gradeCell);
                grades.appendChild(row);
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function() {
        renderStudentList();
    });
</script>
</body>
</html>