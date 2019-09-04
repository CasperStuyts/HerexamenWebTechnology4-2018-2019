package edu.ap.casper.controllers;

import edu.ap.casper.EightBallRepository;
import edu.ap.casper.model.EightBall;
import edu.ap.casper.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class MainController {



    @Autowired
    private EightBallRepository eightBallRepository;
    private RedisService service;
    private String[] answerOptions = {"It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes - definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes", "Signs point to yes",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful",
            "Reply hazy, try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again"};

    @RequestMapping("/getanswer")
    @ResponseBody
    public String giveAnswer(){
        List<EightBall> eightBalls= new ArrayList<>();
        Set<String> keys = service.keys("question:*");
        for(String key : keys) {
            String[] parts = key.split(":");
            eightBalls.add(new EightBall(parts[1],parts[2]));
        }
        StringBuilder b = new StringBuilder();
        b.append("<html><body><table>");
        b.append("<tr><th>Answer</th></tr>");

        for(EightBall ex : eightBalls) {
            b.append("<tr>");
            b.append("<td>");
            b.append(ex.getAnswer());
            b.append("</td>");
            b.append("</tr>");
        }
        b.append("</table></body></html>");

        return b.toString();
    }


    @PostMapping(value = "/new")
    @ResponseBody
    public String addExam(@RequestParam(value = "question") String question) {

        EightBall e = new EightBall(answerOptions[question.length()],question);
        /* check of het bestaat */

        for(EightBall in : this.eightBallRepository.getAll()) {
            if(in.getAnswer().equalsIgnoreCase(e.getAnswer()) && in.getQuestion().equalsIgnoreCase(e.getQuestion())) {
                return "<html>BESTAAT AL</html>";
            }
        }
        this.eightBallRepository.saveQuestion(e);
        return giveAnswer();
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
                "<title>Magic Eightball</title>\r\n" +
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
