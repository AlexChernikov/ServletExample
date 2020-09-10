package servlets;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/main-servlet"})
public class MainServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log("init");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("service enter\n");
        super.service(req, resp);
        resp.getWriter().write("service exit\n");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String params = new String();
        ResultSet rs = DBAdmin.selectAllFromTable();
        try {
            while (rs.next()) {
                String phoneNumber = rs.getString("PHONE_NUMBER");
                String problemMessage = rs.getString("PROBLEM_MESSAGE");
                params = "phoneNumber : " + phoneNumber + "\n" + "problemMessage : " + problemMessage;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.getWriter().write("doGet\n" + "URI: " + uri + "\n Params:\n" + params + "\n");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        List<String> phoneAndMessage = splitParams(req);
        System.out.println(phoneAndMessage.get(0));
        System.out.println(phoneAndMessage.get(1));
        try {
            DBAdmin.insertIntoTable(phoneAndMessage.get(0), phoneAndMessage.get(1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("doPost\n" + "URI: " + uri);
    }

    private String formatParams(HttpServletRequest req) {
        return req.getParameterMap()
                .entrySet().stream().map(stringEntry -> {
                    String param = String.join("and ", stringEntry.getValue());
                    return stringEntry.getKey() + " => " + param;
                }).collect(Collectors.joining("\n"));
    }

    private List<String> splitParams(HttpServletRequest req) throws IOException {
        List<String> params = new ArrayList<>();
        String test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        test = test.replace("\"","");
        test = test.replace("{","");
        test = test.replace("}","");

        String[] split = test.split("(,)|(:)");

        params.add(split[1]);
        params.add(split[3]);
        return params;
    }

    @Override
    public void destroy() {
        log("destroy");
    }
}
