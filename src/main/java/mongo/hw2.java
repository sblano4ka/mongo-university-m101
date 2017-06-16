package mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

public class hw2 {

    public void doQuery(){
        MongoClient client = new MongoClient("localhost", 27017);

        MongoDatabase db = client.getDatabase("m101");

        MongoCollection coll = db.getCollection("grades");

        MongoCursor<Document> itr = coll.find().iterator();


        itr = coll.find(new Document("type", "homework"))
                .sort(orderBy(Sorts.ascending("student_id"), Sorts.ascending("score")))
                .iterator();

        int prevStudId = -1;
        int currentStudId;
        while (itr.hasNext()) {
            Document res = itr.next();
            System.out.println(res.toString());
            currentStudId = res.get("student_id", Integer.class);
            if (currentStudId != prevStudId) {
                coll.deleteOne(res);
                System.out.println("To remove: ");
                System.out.println(res.toString());
            }
            prevStudId = currentStudId;
        }

        // AFTER
        System.out.println(coll.count());

        System.out.println(coll.find()
                .sort(new Document("score", -1))
                .skip(100)
                .limit(1)
                .first());

        // RESULT

        coll.find()
                .projection(Projections.fields(
                        Projections.include("student_id", "type", "score"),
                        Projections.exclude("_id")))
                .sort(orderBy(Sorts.ascending("student_id"), ascending("sort")))
                .limit(5);

    }
}
