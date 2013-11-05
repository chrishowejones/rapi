import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClientPut {

    public static void main(final String[] args) {
        try {

            Client client = Client.create();

            WebResource webResource = client.resource("http://localhost:9090/rapi/persons/A01345");

            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

            String input = response.getEntity(String.class);
            System.out.println(input);
            input = "{\"code\":\"A01345\"," + "\"name\":\"MR FINTAN BARNES TEST\","
                    + "\"email\":\"changedAgain@jhc.co.uk\"}";

            response = webResource.type("application/json").put(ClientResponse.class, input);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
