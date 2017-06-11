import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Demo {

    public static void main(String[] args) {


        MongoClient client = new MongoClient("localhost", 27017);

        MongoDatabase db = client.getDatabase("test");

        MongoCollection coll = db.getCollection("test");

    }
}
