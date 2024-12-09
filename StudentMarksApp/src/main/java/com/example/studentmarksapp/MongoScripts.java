package com.example.studentmarksapp;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class MongoScripts {
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
    public static ArrayList<String> getCourses()
    {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> courses = db.getCollection("Course");
            return courses.distinct("course_name", String.class).into(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public int getUserCourseID(int userID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> users = db.getCollection("Users");
            // Find the user from the ID
            Document user = users.find(new Document("user_id", userID)).first();
            // Get the course name from the user
            String courseName = user.getString("course");
            MongoCollection<Document> courses = db.getCollection("Course");
            // Find the course ID from the course name
            Document course = courses.find(new Document("course_name", courseName)).first();
            return course.getInteger("course_id");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void addStudentModule(int studentID, int moduleID) {
        try {
            MongoClient mongo = MongoClients.create();
            MongoDatabase db = mongo.getDatabase("StudentMarks");
            MongoCollection<Document> users = db.getCollection("Users");
            Document user = users.find(new Document("user_id", studentID)).first();
            ArrayList<Integer> modules = user.get("modules", ArrayList.class);
            if (modules == null) {
                modules = new ArrayList<>();
            }
            modules.add(moduleID);
            user.append("modules", modules);
            users.updateOne(new Document("user_id", studentID), new Document("$set", user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
