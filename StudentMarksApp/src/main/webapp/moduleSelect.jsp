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

        .module-list, .selected-modules {
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
<h1>Module Registration Page</h1>

<div class="container">
    <div class="module-list">
        <table>
            <thead>
            <tr>
                <th>Module Selection</th>
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
                <th>Selected Modules</th>
            </tr>
            </thead>
            <tbody id="selected-modules">
            <!-- Selected modules will appear here -->
            </tbody>
        </table>
    </div>
</div>

<div class="footer">
    <p id="current-cats">Current CATS: 0</p>
    <p>Required CATS: 90</p>
    <button id="btnConf" onclick="confirmSelection()">Confirm Selection</button>
</div>

<script>

    // State variables
    const modules = JSON.parse('<%= request.getAttribute("courseModules") %>'); // All available modules
    let selectedModules = []; // Selected modules
    let totalCats = 0;
    let mandatoryModules = modules.filter(module => module.optional === 'N');
    mandatoryModules.forEach(module => selectModule(modules.indexOf(module)));


    // Render the available modules
    function renderModuleList() {
        const moduleSelection = document.getElementById('module-selection');

        moduleSelection.innerHTML = '';

        // Render the remaining modules
        const remainingModules = modules.filter(module => module.optional === 'Y');
        remainingModules.forEach((module, index) => {
            if (!isModuleSelected(module)) {
                const row = document.createElement('tr');
                console.log(module.module_name);
                row.textContent = module.module_name;
                row.addEventListener('click', () => selectModule(index + mandatoryModules.length));
                moduleSelection.appendChild(row);
            }
        });
    }

    function renderSelectedModules() {
        const selectedModulesTable = document.getElementById('selected-modules');
        selectedModulesTable.innerHTML = '';
        selectedModules.forEach((module, index) => {
            const row = document.createElement('tr');
            row.textContent = module.module_name;
            row.addEventListener('click', () => deselectModule(index));
            selectedModulesTable.appendChild(row);
        });
        document.getElementById('current-cats').textContent = "Current CATS:" + totalCats;
    }

    function isModuleSelected(module) {
        return selectedModules.some(selectedModule => selectedModule.module_id === module.module_id);
    }

    function selectModule(index) {
        const module = modules[index];
        selectedModules.push(module);
        totalCats += module.module_cats;
        renderSelectedModules();
        renderModuleList();
    }

    function deselectModule(index) {
        const module = selectedModules[index];
        //modules.push(module);
        totalCats -= module.module_cats;
        selectedModules.splice(index, 1);
        renderModuleList();
        renderSelectedModules();
    }

    function confirmSelection() {
        if (totalCats !== 90) {
            alert("You must select exactly 90 CATS worth of modules");
            return;
        }
        const selectedModuleIds = selectedModules.map(module => module.module_id);
        console.log(selectedModuleIds);
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = "${pageContext.request.contextPath}/Module-Select-Servlet";
        selectedModuleIds.forEach(moduleId => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'selectedModules';
            input.value = selectedModuleIds;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }

    // Initialize the page
    document.addEventListener("DOMContentLoaded", function() {
        renderModuleList();});
</script>
</body>
</html>
