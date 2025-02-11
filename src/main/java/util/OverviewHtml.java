package util;

import java.util.Set;
import main.Main;

public class OverviewHtml {

    private static Set<Thread>  getAllRunningThreads () {

        return Thread.getAllStackTraces().keySet();
    }

    private static String threadHtmlTable (Set<Thread> threads) {
        StringBuilder resultBuilder = new StringBuilder();

        for (Thread t : threads) {
            resultBuilder.append("<tr>")
                    .append("<td>" + t.getName() + "</td>")
                    .append("<td>" + t.getState() + "</td>")
                    .append("</tr>");
        }

        return resultBuilder.toString();
    }

    public static String getHtml () {

        String result = null;

        result = """
        <!DOCTYPE html>
                <html lang="en">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Server Overview</title>
                <style>
                body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        header {
            background-color: #333;
            color: white;
            width: 100%;
            padding: 20px;
            text-align: center;
        }
        header img {
            max-width: 200px;
            height: auto;
            margin-bottom: 10px;
        }
        header h1 {
            margin: 0;
        }
        .info {
            font-size: 1.1em;
            margin-top: 10px;
        }
        .container {
            width: 90%;
            max-width: 1000px;
            margin-top: 30px;
        }
        .table-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        td {
            font-size: 0.95em;
        }
    </style>
                </head>
                <body>

                <header>
                <img src="$src" alt="Application Logo" width="200" height="200">
                <h1>Server Overview</h1>
                <p class="info">Server Startup Time:\s""".replace("$src","/image/server_logo_1024x1024.jpg") + Main.STARTUPTIME +  """
                </p>
                </header>

                <div class="container">
                <div class="table-container">
                <h2>Currently Running Threads</h2>
                <table>
                <thead>
                <tr>
                <th>Thread name</th>
                <th>Status</th>
                </tr>
                </thead>
                <tbody>"""
                +
                threadHtmlTable( getAllRunningThreads() )
                +
                """
                </tbody>
                </table>
                </div>
                </div>

                <script>
        // Example of dynamically setting the server startup time (use a real timestamp in a production system)
    const startupTime = new Date("2025-02-01T08:00:00Z").toLocaleString();
        document.getElementById("startup-time").textContent = startupTime;
</script>

                </body>
                </html>""";

        return result;

    }

}
