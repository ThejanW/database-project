package lk.bhanuka.validators;

import lk.bhanuka.authentication.Auth;
import lk.bhanuka.database.DataService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bhanuka on 12/11/16.
 */
public class Validator {

    /**
     * ArrayList of required field in the request
     */
    //protected ArrayList<String> required;

    /**
     * Authentication level for a valid request - or more are valid
     */
    protected int accessLevel = 0;

    /**
     * Contains list of required database checks
     */
    protected ArrayList<HashMap> databaseCheck;

    /**
     * Database service for performing authentication checks
     */
    private DataService dataService;

    public Validator(){
        this.dataService = new DataService();
        //this.required = new ArrayList<String>();
        this.accessLevel = 0;
        this.databaseCheck = new ArrayList<HashMap>();
    }
    

    protected HashMap authenticate(HttpServletRequest request){

        HashMap returnResponse = new HashMap();

        if(Auth.getUser().getAccessLevel() >= this.accessLevel){

            return returnResponse;

        }

        returnResponse.put("error", "not authenticated");

        return returnResponse;

    }

    protected HashMap checkRequired(HttpServletRequest request, ArrayList<String> required){

        HashMap returnResponse = new HashMap();

        for(String requirement : required){

            if((Boolean)(request.getParameter(requirement) == null)){

                returnResponse.put("error", requirement+" required");

                return returnResponse;
            }

        }

        return returnResponse;
    }

    protected HashMap convert(HttpServletRequest request, ArrayList<String> required){

        HashMap response = new HashMap();

        for(String requirement: required){

            response.put(requirement, request.getParameter(requirement));

        }

        return response;

    }

    protected HashMap checkDatabase(HttpServletRequest request){

        HashMap returnResponse = new HashMap();

        for(HashMap element: this.databaseCheck){

            ArrayList<String> conditions = new ArrayList<String>();
            conditions.add(element.get("column") + " " + request.getAttribute(element.get("value").toString()));

            HashMap results = (HashMap) this.dataService.get(element.get("table").toString(), conditions );

            if( results.size() != 0){

                returnResponse.put("error", element.get("column")+" already exist");

                return returnResponse;

            }
        }

        return returnResponse;

    }
    
    
    //\\ New Addition //\\
    
    // TODO: Bhanuka check this
    
    public HashMap validate(HttpServletRequest request, ArrayList<String> required){

		HashMap returnResponse = this.checkRequired(request, required);

		System.out.println(returnResponse);

		if (returnResponse.get("error") != null) {
			return returnResponse;
		}
		return convert(request, required);
	}
    
    
    
    
    
    
    //\\ New Addition //\\
}
