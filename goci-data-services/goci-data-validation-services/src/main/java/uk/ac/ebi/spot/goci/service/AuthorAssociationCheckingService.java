package uk.ac.ebi.spot.goci.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.goci.model.Association;
import uk.ac.ebi.spot.goci.model.ValidationError;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by emma on 01/04/2016.
 *
 * @author emma
 *         <p>
 *         Full checking service will run all checks
 */
@Service
@Lazy
public class AuthorAssociationCheckingService implements AssociationCheckingService {

    @Override
    public Collection<ValidationError> runChecks(Association association,
                                                 ValidationChecksBuilder validationChecksBuilder) {

        // Create collection to store all newly created errors
        Collection<ValidationError> associationValidationErrors = new ArrayList<>();

        Collection<ValidationError> riskFrequencyErrors = validationChecksBuilder.runRiskFrequencyChecks(association);
        if (!riskFrequencyErrors.isEmpty()) {
            associationValidationErrors.addAll(riskFrequencyErrors);
        }

        // Run loci attribute checks i.e. risk allele, snp amd author reported genes
        Collection<ValidationError> lociAttributeErrors = validationChecksBuilder.runLociAttributeChecks(association);
        if (!lociAttributeErrors.isEmpty()) {
            associationValidationErrors.addAll(lociAttributeErrors);
        }

        Collection<ValidationError> pvalueErrors = validationChecksBuilder.runPvalueChecks(association);
        if (!pvalueErrors.isEmpty()) {
            associationValidationErrors.addAll(pvalueErrors);
        }

        String effectType = determineIfAssociationIsOrType(association);

        // Run checks depending on effect type
        if (effectType.equalsIgnoreCase("or")) {
            Collection<ValidationError> orErrors =
                    validationChecksBuilder.runAuthorLevelOrChecks(association);
            if (!orErrors.isEmpty()) {
                associationValidationErrors.addAll(orErrors);
            }
        }

        else if (effectType.equalsIgnoreCase("beta")) {
            Collection<ValidationError> betaErrors =
                    validationChecksBuilder.runAuthorLevelBetaChecks(association);
            if (!betaErrors.isEmpty()) {
                associationValidationErrors.addAll(betaErrors);
            }
        }

        else {
            Collection<ValidationError> noEffectErrors =
                    validationChecksBuilder.runAuthorLevelNoEffectChecks(association);
            if (!noEffectErrors.isEmpty()) {
                associationValidationErrors.addAll(noEffectErrors);
            }
        }
        return associationValidationErrors;
    }
}