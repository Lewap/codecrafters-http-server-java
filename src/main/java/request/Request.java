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
                    int indOfTheFirstColon = requestHeaderLine.indexOf(":");

                    String headerName = null;
                    String headerValue = null;
                    if (indOfTheFirstColon != -1)
                    {
                        headerName = requestHeaderLine.substring(0 , indOfTheFirstColon).replace(" ","");
                        headerValue = requestHeaderLine.substring(indOfTheFirstColon + 1).replace(" ","");
                    }

                    header.put(headerName, headerValue);
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

    @Override
    public String toString() {

        StringBuilder resultBuilder = new StringBuilder("[");
        String result;

        for ( Map.Entry<String,String> header : header.entrySet() ) {
            resultBuilder.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append(", ");
        }

        result = resultBuilder.substring(0, resultBuilder.length() - 2);
        result += "]";

        return result;

    }
}
