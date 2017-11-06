/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sofely_donorschoose_exam;

import java.net.*;
import java.io.*;


import java.nio.charset.Charset;

// org.json library at: http://mvnrepository.com/artifact/org.json/json/20160212
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Scanner;



/**
 *
 * @author max
 */

public class Sofely_donorschoose_exam {
   private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        return json;
      } finally {
        is.close();
      }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in); 
        System.out.println("Welcome, please submit your search parameter for proposal querying...");
        String searchParam=sc.next();
        System.out.println();
        int maxProposals=5;
        String stateProposal="CA";
        int minCostProposal=0, maxCostProposal=2000, urgencyProposalFilter=0;
        JSONObject json=null;
        
        try{
            json = readJsonFromUrl("http://api.donorschoose.org/common/json_feed.html?APIKey=DONORSCHOOSE&keywords=%22"+searchParam+"%22&max="+maxProposals+"&state="+stateProposal+"&costToCompleteRange="+minCostProposal+"+TO+"+maxCostProposal+"&sortBy="+urgencyProposalFilter);
        }catch(Exception e){System.out.println("An error occured, please review your search parameters, verify that you have an active internet connection, and try again.");System.exit(0);}
        
        int totalProposals=json.getInt("totalProposals"), queriedProposals;
        if(totalProposals < maxProposals){queriedProposals=totalProposals;}else{queriedProposals=maxProposals;}
        
        System.out.println("Total proposals matching your search: "+totalProposals+", showing top "+queriedProposals+" urgent records.\r\n");
        float netPercentFunded=0.0f, netNumberOfDonors=0.0f, netCostToComplete=0.0f, netNumberOfStudents=0.0f, netTotalPrice=0.0f;
        
        for (int i=0;i<queriedProposals;i++) {
            JSONObject item=json.getJSONArray("proposals").getJSONObject(i);
            
            netPercentFunded+=item.getDouble("percentFunded");
            netNumberOfDonors+=item.getDouble("numDonors");
            netCostToComplete+=item.getDouble("costToComplete");
            netNumberOfStudents+=item.getDouble("numStudents");
            netTotalPrice+=item.getDouble("totalPrice");
            
            System.out.println("Proposal "+(i+1)+":");
            System.out.println("Title: "+item.get("title"));
            System.out.println("Short Description:"+item.get("shortDescription"));
            System.out.println("Proposal URL:"+item.get("proposalURL"));
            System.out.println("Cost to Complete:"+item.get("costToComplete"));
            System.out.println();
        }
        
        
        float avgPercentFunded,avgNumberOfDonors,avgCostToComplete,avgNumberOfStudents,avgTotalPrice;
        avgPercentFunded=netPercentFunded/queriedProposals;
        avgNumberOfDonors=netNumberOfDonors/queriedProposals;
        avgCostToComplete=netCostToComplete/queriedProposals;
        avgNumberOfStudents=netNumberOfStudents/queriedProposals;
        avgTotalPrice=netTotalPrice/queriedProposals;
        
        System.out.println("===============================\r\n");
        System.out.println("Statistics:");
        System.out.println("Average Percent Funded: "+avgPercentFunded);
        System.out.println("Average Number of Donors: "+avgNumberOfDonors);
        System.out.println("Average Cost to Complete: "+avgCostToComplete);
        System.out.println("Average Number of Students: "+avgNumberOfStudents);
        System.out.println("Average Total Price: "+avgTotalPrice);
    }
   
}
