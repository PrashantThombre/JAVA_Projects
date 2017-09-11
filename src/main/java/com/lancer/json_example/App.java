package com.lancer.json_example;

import java.util.Scanner;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class App 
{
	public static String url = "http://api.fixer.io/latest?base=USD";
	public static Float from_rate = 1.0f;
	public static Float to_rate = 1.0f;
	public static void main( String[] args )
	{
		Scanner input = new Scanner(System.in);
		String choice = null;
				
		String from_currency;
		String to_currency;
		
		JsonNode json = apiCall();
		
		do{
			System.out.println("--------------------------------------------------");
			System.out.print("Enter The Currency You Want To Convert From (eg. USD): ");
			from_currency = input.nextLine().toUpperCase();
			if(!from_currency.equalsIgnoreCase("USD")){
				try{
					from_rate = Float.parseFloat(json.get("rates").get(from_currency).asText());
				}catch(NullPointerException e){
					System.err.println("Please enter the correct currency acronym from the above list");
				}
			}

			System.out.print("Enter The Currency You Want To Convert To (eg. INR): ");
			to_currency = input.nextLine().toUpperCase();
			if(!to_currency.equalsIgnoreCase("USD")){
				try{
					to_rate = Float.parseFloat(json.get("rates").get(to_currency).asText());
				}catch(NullPointerException e){
					System.err.println("Please enter the correct currency acronym from the above list");
				}
			}
			System.out.print("Enter The Amount in "+from_currency+": ");
			Float amount = Float.parseFloat(input.nextLine());
			
			String final_value = convertCurrency(from_rate,to_rate,amount);
			System.out.println(from_currency+" "+amount+" = "+to_currency+" "+final_value);
			System.out.println("Continue? (Y/N)");
			choice = input.nextLine().toUpperCase();
		}while(choice.equals("Y"));
		input.close();
	}

	public static JsonNode apiCall(){
		JsonNode json = null;
		try{
			ClientResponse response = null;
			Client client = Client.create();
			WebResource webResource = null;
			
			//Calling the API and getting response here
			webResource = client.resource(url);
			response = webResource.header("content-type", "application/json").get(ClientResponse.class);

			//Reading and parsing the response here.
			ObjectMapper mapper = new ObjectMapper();
			String jsonResponse = response.getEntity(String.class);
			json = mapper.readTree(jsonResponse);
			System.out.println("Rates: "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json.get("rates")));
		}catch(Exception e){

		}
		return json;
	}

	public static String convertCurrency(Float from_rate,Float to_rate, Float amount){
		Float final_value = to_rate / from_rate * amount;
		String formatted_value = String.format("%.02f", final_value);	
		return formatted_value;
	}
}
