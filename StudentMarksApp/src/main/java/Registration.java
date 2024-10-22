import com.mongodb.client.*;

import org.bson.Document;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
import static java.util.Collections.sort;

@WebServlet(name = "Registration", value = "/registration")
public class Registration extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("Processing user registration ........");
        Document customer = new Document()
                .append("user_id", getUserID())
                .append("First_name", request.getParameter("First_name"))
                .append("Password", request.getParameter("Password"));
        createCustomer(customer);
    }

    public void createCustomer(Document customer) {
        MongoClient mongo = MongoClients.create();
        MongoDatabase database = mongo.getDatabase("StudentMarks");
        MongoCollection collection = database.getCollection("Users");
        collection.insertOne(customer);
    }

    public int getUserID() {
        MongoClient mongo = MongoClients.create();
        MongoDatabase db = mongo.getDatabase("StudentMarks");
        MongoCollection<Document> login = db.getCollection("Users");
        MongoCursor<Document> cursor = login.find()
                .projection(fields(include("user_id"))) // Replace with the field you want
                .sort(descending("timestampField")) // Replace with your sorting field
                .limit(1)
                .iterator();
        if (cursor.hasNext()) {
            return cursor.next().getInteger("user_id") + 1;

        } else {
            return 0;
        }
    }
}
