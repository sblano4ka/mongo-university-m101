package mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Sorts.orderBy;

public class Hw31 {

    public static void doAction() {
        MongoClient client = new MongoClient("localhost", 27017);

        MongoDatabase db = client.getDatabase("school");

        MongoCollection coll = db.getCollection("students");

        MongoCursor<Document> itr = coll.find().iterator();

        itr = coll.find()
                //.sort(orderBy(Sorts.ascending("student_id"), Sorts.ascending("score")))
                .iterator();

        while(itr.hasNext()) {
            Document student = itr.next();
            List scores = (List) student.get("scores");

            for (int i = 0; i < scores.size(); i++) {
                double min = 0;
                Document doc = ((Document) (scores.get(i)));
                if (doc.get("type") == "homework") {
                    double currentHW = doc.get("score", Double.class);
                }
            }

            Document first = (Document) scores.get(0);
            Object score = ((Document) scores.get(0)).get("score");

        }
    }
}
