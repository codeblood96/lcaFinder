import GSoC.warmUp.GraphImporter;
import GSoC.warmUp.LCAFinder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ImportException;


import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;


public class Runner {

    private static GraphImporter importer;


    private static LCAFinder finder;



    public Runner(GraphImporter importer, LCAFinder finder) {
        this.importer = importer;
        this.finder = finder;
    }
    public static void main(String[] args) {
        importer = new GraphImporter();
        finder  = new LCAFinder();

        Runner runner = new Runner(importer, finder);
        Option fileInput =
                Option.builder("f")
                        .longOpt("file")
                        .required()
                        .valueSeparator()
                        .argName("file")
                        .hasArg()
                        .desc("path to the file")
                        .build();
        Option persons =
                Option.builder("p")
                        .longOpt("person")
                        .numberOfArgs(2)
                        .argName("person, person")
                        .required()
                        .desc("Name of the two persons")
                        .build();
        Options options = new Options();
        options.addOption(fileInput).addOption(persons);

        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            String path = commandLine.getOptionValue('f');
            String[] person = commandLine.getOptionValues('p');
            if (person.length >= 2) {
                runner.processInput(path, person[0], person[1]);
            } else {
                System.out.println("Give 2 persons valid name");
            }
        } catch (ParseException e) {
            //
        }
    }
    public void processInput(String path, String person1, String person2) {
        File file = new File(path);
            try {
                Graph<String, DefaultEdge> graph = importer.importGraph(file);
                if (!graph.containsVertex(person1)) {
                    System.out.println(
                            String.format("Vertex named %s does not exist",person1));
                } else if (!graph.containsVertex(person2)) {
                    System.out.println(
                            String.format("Vertex named %s does not exist",person2));
                } else if(person1.equals(person2)) {
                    System.out.println("Same vertices given as input");
                }else {
                    Set<String> lcas = finder.findLCAs(graph, person1, person2);
                    if (lcas.size() == 0) {
                        System.out.println(String.format("No ancestors found for %s and %s", person1, person2));
                    } else {
                        System.out.print(
                                String.format(
                                        "Lowest common " + (lcas.size() == 1 ? "ancestor" : "ancestors")
                                                + " of %s and %s: ", person1, person2));
                        System.out.println(lcas.stream().collect(Collectors.joining(", ")));
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ImportException e) {
                System.out.println(e.getMessage());
            }

    }
}

