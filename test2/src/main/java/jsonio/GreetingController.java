package jsonio;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path = "/greeting")
    public Greeting greeting(@RequestParam(value="name",defaultValue="World") String name) 
    {
        return new Greeting( counter.incrementAndGet(), String.format(template, name) );
    }

    @RequestMapping(path = "/test")
    public JSONObject test(@RequestParam(value="name",defaultValue="test") String graph, @RequestParam(value="nodes",defaultValue="a$ab$abc") String problems ,@RequestParam(value="result",defaultValue="1$1$0") String result)
    {
        JSONObject obj = new JSONObject();
        obj.put("name",graph);
        JSONArray a = new JSONArray();
        String[] parts1 = problems.split("$");
        for (String s : parts1)
            a.add(s);
        JSONArray b = new JSONArray();
        String[] parts2 = result.split("$");
        for (String s :parts2)
            b.add(s);
        obj.put("nodes",a);
        obj.put("result",b);
        return obj;
    }

    @RequestMapping(path = "/bp")
    public BpResult calculatebp(@RequestParam(value="name") String graph, @RequestParam(value="nodes") String problems ,@RequestParam(value="result") String result) 
    {

        BpResult bp = new BpResult(graph);
        bp.start(problems,result);
    	// Integer.parseInt(
        return bp;
    }

}
