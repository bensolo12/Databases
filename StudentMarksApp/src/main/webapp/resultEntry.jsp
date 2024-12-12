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

        .module-list {
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

        button {
            padding: 10px;
            margin: 10px;
            background-color: #007bff;
            color: white;
            border: none;
        }

        .footer {
            margin-top: 20px;
            text-align: center;
            font-size: 16px;
        }
    </style>
</head>
<body>
<h1>Result Entry Page</h1>

<div class="container">
    <div class="module-list">
        <table>
            <thead>
            <tr>
                <th>My Modules</th>
            </tr>
            </thead>
            <tbody id="module-selection">
            <!-- Modules will be populated here by JavaScript -->
            </tbody>
        </table>
    </div>

    <div class="selected-modules">
        <table>
            <thead>
            <tr>
                <th>Students</th>
                <th>Grade</th>
            </tr>
            </thead>
            <tbody id="students">
            <!-- Students and grades will appear here -->
            </tbody>
        </table>
    </div>
</div>

<div class="footer">
    <button id="btnConf" onclick="confirmSelection()">Confirm Selection</button>
</div>

<script>
    // State variables
    const modules = JSON.parse('<%= request.getAttribute("courseModules") %>'); // All available modules
    let studentList = [];
    //let selectedModules = []; // Selected modules
    let gradeList = [];
    let selectedModuleID = null;

    // Render the available modules
    function renderModuleList() {
        const moduleSelection = document.getElementById('module-selection');
        moduleSelection.innerHTML = '';
        modules.forEach((module, index) => {
            const row = document.createElement('tr');
            row.textContent = module.module_name;
            //console.log(module.students);
            row.addEventListener('click', () => selectModule(index));
            moduleSelection.appendChild(row);
        });
    }

    function selectModule(index) {
        studentList = [];
        selectedModuleID = modules[index].module_id;
        try{
            modules[index].students.forEach(student => {
                if (!studentList.includes(student)) {
                    studentList.push(student);
                }
            });
        }
        catch(err){
            studentList.push("No students enrolled");
        }

        const students = document.getElementById('students');
        students.innerHTML = '';
        studentList.forEach((student, index) => {
            const row = document.createElement('tr');
            const studentCell = document.createElement('td');
            studentCell.textContent = student;
            const gradeCell = document.createElement('td');
            const gradeInput = document.createElement('input');
            gradeInput.type = 'number';
            gradeInput.min = 0;
            gradeInput.max = 100;
            gradeInput.placeholder = 'Enter grade';
            gradeCell.appendChild(gradeInput);
            row.appendChild(studentCell);
            row.appendChild(gradeCell);
            students.appendChild(row);
        });
    }

    function confirmSelection() {
        const studentsTable = document.getElementById('students');
        const rows = studentsTable.getElementsByTagName('tr');
        const studentGrades = [];

        for (let i = 0; i < rows.length; i++) {
            const studentID = rows[i].cells[0].textContent;
            const grade = rows[i].cells[1].getElementsByTagName('input')[0].value;
            studentGrades.push({moduleID: selectedModuleID, studentID: studentID, grade: grade });
        }

        console.log(studentGrades);

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = "${pageContext.request.contextPath}/Result-Entry-Servlet";
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'studentGrades';
        input.value = JSON.stringify(studentGrades);
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    }

    // Initialize the page
    document.addEventListener("DOMContentLoaded", function() {
        renderModuleList();
    });
</script>
</body>
</html>