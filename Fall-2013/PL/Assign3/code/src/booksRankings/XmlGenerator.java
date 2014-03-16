package booksRankings;

import java.util.HashMap;
import java.util.Map;

public class XmlGenerator{

    private String ACCESS_ID;
    private String SECRET_ACCESS_KEY;
    private String END_POINT;

    public void setAccessID(String accessID){
        ACCESS_ID = accessID;
    }

    public String getAccessID(){
        return ACCESS_ID;
    }

    public void setSecretAccessKey(String secretAccessKey){
        SECRET_ACCESS_KEY = secretAccessKey;
    }

    public String getSecretAccessKey(){
        return SECRET_ACCESS_KEY;
    }

    public void setEndPoint(String endPoint){
        END_POINT = endPoint;
    }

    public String getEndPoint(){
        return END_POINT;
    }

    public String getSignedUrl(String isbn){
        String url;
        try{
            SignedRequestsHelper helper = SignedRequestsHelper.getInstance(getEndPoint(), getAccessID(), getSecretAccessKey());

            Map<String, String> params =  new HashMap<>();
            params.put("AssociateTag", "vmeka-20");
            params.put("Operation", "ItemLookup");
            params.put("ResponseGroup", "Large");
            params.put("SearchIndex", "Books");
            params.put("IdType", "ISBN");

            params.put("ItemId", isbn);
            String signedUrl = helper.sign(params);
            url = signedUrl;

            return url;
        }catch(Exception e){
            return url = "Malformed URL/ Couldn't form URL properly";
        }
    }
}