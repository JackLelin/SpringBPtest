package jsonio;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path = "/greeting",method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name") String name) 
    {
        return new Greeting( counter.incrementAndGet(), String.format(template, name) );
    }

    @RequestMapping(path = "/bp" , method = RequestMethod.GET)
    public BpResult calculatebp(@RequestParam(value="name") String graph, @RequestParam(value="nodes") String problems ,@RequestParam(value="result") String result) 
    {
        BpResult bp = new BpResult(graph);
        bp.start(problems,result);
    	// Integer.parseInt(
        return bp;
    }

}
