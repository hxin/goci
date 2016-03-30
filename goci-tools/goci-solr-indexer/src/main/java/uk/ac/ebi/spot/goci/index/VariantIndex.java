package uk.ac.ebi.spot.goci.index;

import org.springframework.data.solr.repository.SolrCrudRepository;
import uk.ac.ebi.spot.goci.model.VariantDocument;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 22/12/14
 */
public interface VariantIndex extends SolrCrudRepository<VariantDocument, String> {
}