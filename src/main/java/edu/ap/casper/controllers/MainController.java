package edu.ap.casper.controllers;

import edu.ap.casper.model.EightBallAnswer;
import edu.ap.casper.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class MainController implements WebMvcConfigurer {
    private RedisService service; // pattern : "exams":exam:student:reason:date

    @Autowired
    public void setRedisService(RedisService service) {
        this.service = service;
    }
    @RequestMapping("/listAll")
    @ResponseBody
    public String giveAnswer(){
        List<EightBallAnswer> eightBallAnswers = new ArrayList<>();
        Set<String> keys = service.keys("eightBallAnswers:*");
        for(String key : keys) {
            String[] parts = key.split(":");
            eightBallAnswers.add(new EightBallAnswer(parts[1]));
        }
        StringBuilder b = new StringBuilder();
        b.append("<html><body><table>");
        b.append("<tr><th>Datum</th><th>Student</th><th>Examen</th><th>Reden</th></tr>");

        for(EightBallAnswer ex : eightBallAnswers) {
            b.append("<tr>");
            b.append("<td>");
            b.append(ex.getAnswer());
            b.append("</td>");
            b.append("</tr>");
        }
        b.append("</table></body></html>");

        return b.toString();
    }


    @RequestMapping("/question")
    @ResponseBody
    public String addForm() {
        String html = "<html>\r\n" +
                "<head>\r\n" +
                "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\r\n" +
                "\r\n" +
                "<link rel='stylesheet' href='/resources/css/bootstrap.min.css'>\r\n" +
                "<script type='text/javascript' src='/resources/js/app.js'></script>\r\n" +
                "\r\n" +
                "<title>Inhaal examen</title>\r\n" +
                "</head>\r\n" +
                "\r\n" +
                "<body>\r\n" +
                "\r\n" +
                "<div class='well'>\r\n" +
                "<h1>Magic Eightball</h1>\r\n" +
                "<br/>\r\n" +
                "<form method=POST action='new' onsubmit='return validate()'>\r\n" +
                "		<div class='form-group row'>\r\n" +
                "		 	<div class='col-xs-4'>\r\n" +
                "				<label for='firstName'>Your question : </label>\r\n" +
                "		    		<input type='text' class='form-control' name='question' id='question'>\r\n" +
                "	    		</div>\r\n" +
                "	    	</div>\r\n" +

                "	    	\r\n" +
                "		<input type=SUBMIT value='Sumbit'>\r\n" +
                "</form>\r\n" +
                "\r\n" +
                "<br/><br/>\r\n" +
                
                "</div>\r\n" +
                "\r\n" +
                "</body>\r\n" +
                "</html>";
        return html;
    }
}
