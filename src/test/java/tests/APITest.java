package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {
    Playwright playwright;
    APIRequestContext apiRequest;

    @Test
    public void e2eApiTest() {

        //Ex-40, 41 ,42

        //2. Get the user details using GET request and verify the details
        //3. Update the user details using PUT request and verify the update using GET request
        //4. Delete the user using DELETE request and verify the deletion using GET request

        //1. Login API Call
        HashMap<Object, Object> payloadDetails = new HashMap<>();
        payloadDetails.put("email", "veenarecordsinc@gmail.com");
        payloadDetails.put("password", "Narayan@123");


        playwright = Playwright.create();
        apiRequest = playwright.request().newContext();
        APIResponse apiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(payloadDetails));
        Assert.assertTrue(apiResponse.ok());
        System.out.println(apiResponse.text());

        // Ex- 43
        String token = JsonPath.read(apiResponse.text(),"$.token");
        System.out.println("Login Success: "+token);


        // Create Event
        String eventTitle = "Playwright Api Testing";
        HashMap<Object, Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title",eventTitle);
        createEventPayload.put("description","Api Testing");
        createEventPayload.put("category","Conference");
        createEventPayload.put("venue","Main Road");
        createEventPayload.put("city","Bangalore");
        createEventPayload.put("eventDate","2026-07-31T05:41:00.000Z");
        createEventPayload.put("price",100);
        createEventPayload.put("totalSeats",500);


       APIResponse eventResponse =  apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization","Bearer "+token)
                        .setData(createEventPayload));

       Assert.assertTrue(eventResponse.ok(),"Create event API should succeed");


        int eventId = JsonPath.read(eventResponse.text(),"$.data.id");
        System.out.println("Event Created with ID: "+eventId);

        // Get Event
       APIResponse retrieveEvents =  apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
               RequestOptions.create()
                .setQueryParam("page","").setQueryParam("limit","12")
                .setHeader("Authorization","Bearer "+token));

        Assert.assertTrue(retrieveEvents.ok(),"API details retrieved successfully");

        List<Integer> allEventIds = JsonPath.read(retrieveEvents.text(),"$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventId),"Created event should appear in the event list");


        //Ex-45
        //Delete Event
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events"+eventId,
                RequestOptions.create().setHeader("Authorization","Bearer "+token));
                Assert.assertTrue(deleteResponse.ok(),"Event Deleted Successfully");


        //Ex-46
        // Verify Deletion is success. -> GetEvents and confirm that eventID does not exist anymore
        APIResponse verifyDeletion =  apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setQueryParam("page","").setQueryParam("limit","12")
                        .setHeader("Authorization","Bearer "+token));

            Assert.assertTrue(verifyDeletion.ok(),"Post-delete event call should succeed");
            List<String> titleAfterDelete = JsonPath.read(verifyDeletion.text(),"$.data[*].id");
            Assert.assertFalse(titleAfterDelete.contains(eventTitle),
                    "Deleted event should not appear in the event list");
            System.out.println("Deletion Verified : Event no longer in the list");


        }



}