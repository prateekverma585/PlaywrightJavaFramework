package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderDemoTest {
    //Ex- 61
    @DataProvider(name ="basicData")
    public Object[][] basicData(){
        return new Object[][] {{"pkv@gmail.com","Prateek","29"},{"sapna@gmail.com","Sapna","29"}};
    }


    @Test(dataProvider = "basicData")
    public void testFillForm(String email, String name, String age){
        System.out.println(email + " | " + name + " | " + age);
    }

    //Ex-62
    @DataProvider(name="hashMapData")
    public Object[][] hashMapData(){

        HashMap<String,String> user1 = new HashMap<>();
        user1.put("email","pkv@gmail.com");
        user1.put("password","KGf@123");

        HashMap<String, String> user2 = new HashMap<>();
        user2.put("email","sapna@gmail.com");
        user2.put("password","sapna@123");

        return new Object[][]{{user1},{user2}};
    }

    @Test(dataProvider="hashMapData")
    public void testWithHashMap(HashMap<String,String> data){
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }

    //Ex-63
    @DataProvider(name ="jsonData")
    public Object[][] jsonData() throws IOException {
        //reading the bytes of the file and converting that into string format
        String jsonContent = new String(Files.readAllBytes
                (Paths.get(System.getProperty("user.dir")+ "/src/test/resources/testData_TC1.json")));

        Type type = new TypeToken<List<HashMap<String, String>>>(){}.getType();
        List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);
        Object[][] table = new Object[list.size()][1];
        for(int i=0;i<list.size();i++)
        {
            table[i][0] = list.get(i);
        }
        return table;
    }

    @Test(dataProvider ="jsonData")
    public void testWithJsonFile(HashMap<String, String>data){
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }
}
