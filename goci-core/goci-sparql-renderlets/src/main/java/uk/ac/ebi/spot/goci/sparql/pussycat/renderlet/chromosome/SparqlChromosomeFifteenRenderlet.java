package uk.ac.ebi.spot.goci.sparql.pussycat.renderlet.chromosome;

import uk.ac.ebi.spot.goci.spi.ServiceProvider;
import uk.ac.ebi.spot.goci.ontology.OntologyConstants;

import java.net.URI;
import java.net.URL;

/**
 * Created by IntelliJ IDEA. User: dwelter Date: 01/03/12 Time: 10:35 To change this template use File | Settings | File
 * Templates.
 */

@ServiceProvider
public class SparqlChromosomeFifteenRenderlet extends SparqlChromosomeRenderlet {
    private URI chromosomeURI = URI.create(OntologyConstants.GWAS_ONTOLOGY_SCHEMA_IRI + "/Chromosome_15");
    private int position = 14;

    @Override public String getName() {
        return "Chromosome 15";
    }

    @Override
    protected URL getSVGFile() {
        return getClass().getClassLoader().getResource("chromosomes/15.svg");
    }

    @Override
    protected URI getChromosomeURI() {
        return chromosomeURI;
    }

    @Override
    protected int getPosition() {
        return position;
    }
}
