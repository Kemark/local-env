import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 */
public class PathListener {

      private Map<String, Long> pathList = new HashMap<String, Long>();

      /**
       * 
       */
      public static void main(String[] args) throws IOException, InterruptedException {
            var listener = new PathListener();
            var path = Paths.get(args.length > 0 ? args[0] :  ".");
            while(true) {
                  listener.listen(path);
                  TimeUnit.SECONDS.sleep(5);
            }
      }

      /**
       * 
       */
      public void listen(Path path) throws IOException {

            try (Stream<Path> walk = Files.walk(path)) {
                  var currentPathList = walk.filter(Files::isRegularFile)
                              .collect(Collectors.toMap(p1 -> path.relativize(p1).toString(), p2 -> p2.toFile().lastModified()));

                  currentPathList.entrySet().forEach(file -> {
                        var existingModified = pathList.get(file.getKey());
                        if(existingModified == null) {
                              System.out.println("%s|creeated".formatted(file.getKey()));                              
                        } else if(existingModified.compareTo(file.getValue()) != 0) {
                              System.out.println("%s|modified".formatted(file.getKey()));                              
                        }
                  });  
                  
                  this.pathList = currentPathList;
            }
      }
}