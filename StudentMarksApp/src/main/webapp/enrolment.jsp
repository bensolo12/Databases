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
                <li onclick="updateModules('Computer Science')">Computer Science</li>
                <li onclick="updateModules('Cyber Security')">Cyber Security</li>
                <li onclick="updateModules('Cyber Forensics')">Cyber Forensics</li>
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
    const modules = {
"Computer Science": ["Algorithms", "Data Structures", "Operating Systems"],
"Cyber Security": ["Network Security", "Cryptography", "Ethical Hacking"],
"Cyber Forensics": ["Introduction to Cyber", "Malware Analysis", "Programming Basics"]
};

function updateModules(course) {
const moduleList = document.getElementById("module-list");
const moduleHeader = document.getElementById("module-header");

// Clear existing modules
moduleList.innerHTML = "";

// Update header and add new modules
moduleHeader.textContent = `${course} Modules`;
modules[course].forEach(module => {
const li = document.createElement("li");
li.textContent = module;
moduleList.appendChild(li);
});
}

function confirmSelection() {
alert("Selection confirmed!");
}
</script>
</body>
</html>
