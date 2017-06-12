import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;
import static com.mongodb.client.model.Updates.inc;

public class Demo {

    public static void main(String[] args) {


        MongoClient client = new MongoClient("localhost", 27017);

        MongoDatabase db = client.getDatabase("m101");

        MongoCollection coll = db.getCollection("grades");

        MongoCursor<Document> itr = coll.find().iterator();

//        while (itr.hasNext()) {
//            Document res = itr.next();
//            System.out.println(res.toString());
//        }


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

    private static void examples(final MongoCollection coll) {
        Document doc = new Document("", "")
                .append("", "");


        coll.drop();

        coll.insertOne(doc);

        //coll.insertMany(list);


        coll.find().first();
        coll.find().into(new ArrayList());

        MongoCursor curdor = coll.find().iterator();

        while (curdor.hasNext()) {
            curdor.next();
        }

        coll.count();

        // filter

        Bson filter = new Document("x", 0)
                .append("y", new Document("$gt", 10));

        coll.find(filter).into(new ArrayList());


        Bson filter2 = and(eq("x", 10), gt("x", 20));
        coll.find(filter2).into(new ArrayList());

        // projection
        Bson projection = new Document("x", 0)
                .append("_id", 0);


        // OR
        Bson project = Projections.exclude("x", "_id");
        Bson projectinclude = Projections.include("x", "y");

        Projections.fields(Projections.include("x", "y"), Projections.exclude("_id"));


        coll.find(filter)
                .projection(projection)
                .into(new ArrayList<Document>());

        // Sorting

        Bson sort = new Document("i", 1).append("j", -1);
        Bson sort2 = orderBy(Sorts.ascending("i"), Sorts.descending("j"));

        coll.find(filter)
                .projection(projection)
                .sort(sort)
                .skip(20)
                .limit(50)
                .into(new ArrayList<Document>());


        // Update Replace

        // replace whole entity:
        coll.replaceOne(eq("x", 5), new Document("_id", 5).append("upd", true));
        //not changed others data:
        coll.updateOne(eq("x", 5), new Document("$set",
                new Document("_id", 5).append("upd", true)));


        coll.updateOne(eq("x", 5), Updates.set("x", 5));
        coll.updateOne(eq("x", 5), Updates.combine(Updates.set("x", 5), Updates.set("y", 7)));


        // not create new one
        coll.updateOne(eq("_id", 9), Updates.combine(Updates.set("x", 5), Updates.set("y", 7)));


        // will create new entity if not exists because of upsert = true
        coll.updateOne(eq("_id", 9),
                Updates.combine(Updates.set("x", 5), Updates.set("y", 7)),
                new UpdateOptions().upsert(true));


        coll.updateMany(gte("x", 9),
                inc("x", 1));

        // DELETE
        coll.deleteMany(gt("_id", 4));
        coll.deleteOne(gt("_id", 4));
    }
}
