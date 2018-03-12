package GSoC.warmUp;
import org.jgrapht.Graph;
import org.jgrapht.alg.NaiveLcaFinder;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;

public class LCAFinder {

    public Set<String> findLCAs(Graph<String, DefaultEdge> graph, String person1, String person2){
        NaiveLcaFinder<String, DefaultEdge> naiveLcaFinder = new NaiveLcaFinder<>(graph);
        return naiveLcaFinder.findLcas(person1, person2);
    }
}
