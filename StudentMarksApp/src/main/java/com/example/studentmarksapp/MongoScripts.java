package com.example.studentmarksapp;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


public class MongoScripts {
    static Gson gson = new Gson();

    //Enrol a student in a course
    public static void enrolStudent(String course, String userID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> users = db.getCollection("Users");
            Document user = users.find(new Document("user_id", Integer.parseInt(userID))).first();
            user.append("enrolled", "Y");
            user.append("course", course);
            users.updateOne(new Document("user_id", Integer.parseInt(userID)), new Document("$set", user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Create a mongoDB schema - not used in the application
    public void createMongoSchema()
    {
        MongoClient mongo = MongoClients.create();
        MongoDatabase database = mongo.getDatabase("StudentMarks");
        MongoCollection collection = database.getCollection("Users");
        Document user = new Document()
                .append("user_id", 101)
                .append("First_name", "Student")
                .append("Second_name", "Test")
                .append("DOB", "01/01/2000")
                .append("Password", "password")
                .append("enrolled", "N");
        collection.insertOne(user);
        collection = database.getCollection("Course");
        Document course1 = new Document()
                .append("course_id", 1)
                .append("course_name", "Computer Science");
        collection.insertOne(course1);
        collection = database.getCollection("Course");
        Document course2 = new Document()
                .append("course_id", 2)
                .append("course_name", "Cyber Security");
        collection.insertOne(course2);
        collection = database.getCollection("Course");
        Document course3 = new Document()
                .append("course_id", 3)
                .append("course_name", "Cyber Forensics");
        collection.insertOne(course3);
        collection = database.getCollection("Modules");
        Document module1 = new Document()
                .append("module_id", 1)
                .append("module_name", "Principles of Programming")
                .append("course_id", 1);
        Document module2 = new Document()
                .append("module_id", 2)
                .append("module_name", "Web Development")
                .append("course_id", 1);
        Document module3 = new Document()
                .append("module_id", 3)
                .append("module_name", "Maths for Computer Science")
                .append("course_id", 1);
        Document module4 = new Document()
                .append("module_id", 4)
                .append("module_name", "Network Security")
                .append("course_id", 2);
        Document module5 = new Document()
                .append("module_id", 5)
                .append("module_name", "Ethical Hacking")
                .append("course_id", 2);
        Document module6 = new Document()
                .append("module_id", 6)
                .append("module_name", "Cyber Security Management")
                .append("course_id", 2);
        Document module7 = new Document()
                .append("module_id", 7)
                .append("module_name", "Cyber Forensics")
                .append("course_id", 3);
        Document module8 = new Document()
                .append("module_id", 8)
                .append("module_name", "Computational Law")
                .append("course_id", 3);
        Document module9 = new Document()
                .append("module_id", 9)
                .append("module_name", "Malware Analysis")
                .append("course_id", 3);
        ArrayList<Document> modules = new ArrayList<Document>();
        modules.addAll(Arrays.asList(module1, module2, module3, module4, module5, module6, module7, module8, module9));
        collection.insertMany(modules);
    }

    //Get all courses from the course collection
    public static ArrayList<Integer> getCourses()
    {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> courses = db.getCollection("Course");
            ArrayList<Integer> courseList = new ArrayList<>();
            courses.find().forEach((Block<? super Document>) (Document course) -> {
                courseList.add(course.getInteger("course_id"));
            });
            return courseList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get all modules from the modules collection
    public static ArrayList<String> getModules() {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> modules = db.getCollection("Modules");
            MongoCollection<Document> courses = db.getCollection("Course");
            var moduleList = new ArrayList<String>();
            modules.find().forEach((Block<? super Document>) (Document module) -> {
                Document course = courses.find(new Document("course_id", module.getInteger("course_id"))).first();
                module.append("course_name", course.getString("course_name"));
                moduleList.add(module.toJson());
            });
            return moduleList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get the modules for a course
    public ArrayList<String> getModules(int courseID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> modules = db.getCollection("Modules");
            MongoCollection<Document> courses = db.getCollection("Course");
            Document course = courses.find(new Document("course_id", courseID)).first();
            var moduleList = new ArrayList<String>();
            modules.find(new Document("course_id", courseID)).forEach((Block<? super Document>) (Document module) -> {
                module.append("course_name", course.getString("course_name"));
                moduleList.add(module.toJson());
            });
            return moduleList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get the course ID from the user ID
    public int getUserCourseID(int userID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> users = db.getCollection("Users");
            Document user = users.find(new Document("user_id", userID)).first();
            String courseName = user.getString("course");
            MongoCollection<Document> courses = db.getCollection("Course");
            Document course = courses.find(new Document("course_name", courseName)).first();
            return course.getInteger("course_id");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //Add a student module
    public void addStudentModule(int studentID, int moduleID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> modules = db.getCollection("Modules");
            modules.updateOne(
                    new Document("module_id", moduleID),
                    new Document("$addToSet", new Document("students", Arrays.asList(studentID, 0)))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get the course names from the course IDs
    public static ArrayList<String> getCourseNamesFromID(ArrayList<Integer> courseIDs) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> courses = db.getCollection("Course");
            ArrayList<String> courseNames = new ArrayList<>();
            for (int courseID : courseIDs) {
                Document course = courses.find(new Document("course_id", courseID)).first();
                courseNames.add(gson.toJson(course.getString("course_name")));
            }
            return courseNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get the modules a staff member is teaching
    public ArrayList<String> getStaffModules(int staffID){
        // Get the modules for a staff member
        // Find the staff member in the users collection if the userID is starts with 2
        // Find the modules for the course that the staff member is teaching
        // Return the modules
        ArrayList<String> staffModules = new ArrayList<>();
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> modules = db.getCollection("Modules");
        modules.find(new Document("module_teacher", staffID)).forEach((Block<? super Document>) (Document staffModule) -> {
            staffModules.add(staffModule.toJson());
        });
        return staffModules;
    }

    //Add a student result
    public void addStudentResult(int studentID, int moduleID, int result){
        // Add a result for a student based on their ID, module ID and the result
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> results = db.getCollection("Results");
        Document studentResult = new Document()
                .append("student_id", studentID)
                .append("module_id", moduleID)
                .append("result", result);
        results.insertOne(studentResult);
    }

    //Get all students
    public ArrayList<String> getAllStudents() {
        // Get all students from the users collection who are enrolled
        ArrayList<String> studentList = new ArrayList<>();
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> users = db.getCollection("Users");
        users.find(new Document("enrolled", "Y")).forEach((Block<? super Document>) (Document student) -> {
            studentList.add(student.toJson());
        });
        return studentList;
    }

    //Get all module grades
    public ArrayList<String> getModuleGrades() {
        // Get all module grades from the results collection
        ArrayList<String> moduleGrades = new ArrayList<>();
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> results = db.getCollection("Results");
        results.find().forEach((Block<? super Document>) (Document moduleGrade) -> {
            moduleGrades.add(moduleGrade.toJson());
        });
        return moduleGrades;
    }

    //for each student that's enrolled get their course and
    // all of their module grades from results tables and average them
    private ArrayList<Integer> getEnrolledStudents(){
        ArrayList<Integer> enrolledStudents = new ArrayList<>();
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> students = db.getCollection("Users");
        students.find(new Document("enrolled", "Y")).forEach((Block<? super Document>) (Document student) -> {
            enrolledStudents.add(student.getInteger("user_id"));
        });
        return enrolledStudents;
    }

    //Calculate the number of passes for a course
    public Integer calculatePasses(int courseID){
        int allStudentGrades = 0;
        for (int student : getEnrolledStudents()){
            ArrayList<Integer> studentGrades = new ArrayList<>();
            AtomicReference<String> studentID = new AtomicReference<>("");
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> results = db.getCollection("Results");
            results.find(new Document("students", student)).forEach((Block<? super Document>) (Document studentGrade) -> {
                studentID.set(String.valueOf(studentGrade.getInteger("students")));
                studentGrades.add(studentGrade.getInteger("student_result"));
            });
            int total = 0;
            for (int grade : studentGrades){
                total += grade;
            }
            int average = total / studentGrades.size();
            int userCourseID = getUserCourseID(Integer.parseInt(String.valueOf(studentID)));
            if (average > 40 && userCourseID == courseID){
                allStudentGrades += 1;;
            }
        }
        return allStudentGrades;
    }
}
