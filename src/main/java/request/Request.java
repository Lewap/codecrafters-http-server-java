package request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private final List<String> requestHeaderLines;
    private final String[] args;
    private String body;

    public Request ( List<String> requestHeaderLines, String[] args ) {
        this.requestHeaderLines = requestHeaderLines;
        this.args = args;
        setHeader();
    }

    public String[] getArgs () {
        return this.args;
    }
    
    private Map<String, String> header;
    
    private void setHeader () {
        
        header = new HashMap<>();
        
        for (int i=0; i<this.requestHeaderLines.size(); i++) {
            if ( i==0 ) {
                header.put("Start-Line",requestHeaderLines.get(i));
            } else {
                String requestHeaderLine = requestHeaderLines.get(i);
                if ( requestHeaderLine.length() > 0 ) {
                    String[] requestHeaderLinesParsed = requestHeaderLines.get(i).split(" ");
                    header.put(requestHeaderLinesParsed[0].replace(":",""), requestHeaderLinesParsed[1]);
                }
            }
        }
    }

    public Map<String,String> getHeader () {
        return header;
    }

    public String getRootOfTheEndpoint () {

        String result;
        String[] StartLineParsed = header.get("Start-Line").split(" ");
        String uri = StartLineParsed[StartLineParsed.length-2];
        String[] uriParsed = uri.split("/");

        if (uriParsed.length == 0) {
            result = "/";
        } else {
            result = uriParsed[1];
        }

        return result;
    }

    public String[] getEndpointParsed () {

        String[] StartLineParsed = header.get("Start-Line").split(" ");
        String uri = StartLineParsed[StartLineParsed.length-2];

        return uri.split("/");

    }

    public String getVERB () {

        return header.get("Start-Line").split(" ")[0];

    }

    public void setBody (String body) {
        this.body = body;
    }

    public String getBody () {
        return this.body;
    }

}
